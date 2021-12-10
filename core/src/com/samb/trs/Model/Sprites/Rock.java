package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Interfaces.Drawable;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;

public class Rock extends AiBody implements Drawable {

    public enum State {NOTHIT, HIT1, HIT2, HIT3, BROKEN}
    private State currentState, previousState;
    private boolean target_break;
    private float velocity;
    private Viewport viewport;
    private boolean enableOutOfBounds = true;
    private ParticleEffectPool.PooledEffect waterParticles;

    public static final float MAX_VELOCITY = 40f;
    public static final float MAX_ANGULAR_VELOCITY = 4f;
    public static final float LINEAR_DAMPING = 1.4f;
    public static final float ANGULAR_DAMPING = 1.4f;
    public static final float EFFECT_DURATION = 0.4f;

    public Rock(MainController mainController, TextureRegions regions, World world, Viewport viewport, float px, float py, float pwidth, float pheight) {
        super(mainController, regions, world, px, py, pwidth, pheight);
        this.viewport = viewport;
        target_break = false;
        previousState = State.NOTHIT;
        currentState = State.NOTHIT;

        //load Particles
        // TODO: Pooling particles for better performance
        // TODO: PoolController for garbage optimization
    }

    @Override
    public void drawShadow(Batch batch) {
        draw(batch);
    }

    @Override
    public void drawUnder(Batch batch) {
        // Draw water particles
        waterParticles.setPosition(getCenter().x, getCenter().y);
        waterParticles.draw(batch);
    }

    @Override
    public void drawBase(Batch batch) {
        draw(batch);
    }

    @Override
    public void drawOver(Batch batch) {
        //Draw collision particles
        Vector2 pos = getWorldPosition();
    }

    @Override
    public void reset() {
        super.reset();
        previousState = State.NOTHIT;
        currentState = State.HIT1;
        updateWaterParticles();
    }

    private void updateWaterParticles() {
        waterParticles.reset();
        for (ParticleEmitter emitter : waterParticles.getEmitters()) {
            emitter.getSpawnWidth().setLow(getWidth() - 10);
            emitter.getSpawnWidth().setHigh(getWidth() - 10);
            emitter.getSpawnHeight().setLow(getHeight() - 10);
            emitter.getSpawnHeight().setHigh(getHeight() - 10);
        }
        waterParticles.start();
    }

    @Override
    public void redefineBody(float width, float height) {
        super.redefineBody(width, height);
        updateWaterParticles();
    }

    /**
     * Creates body and fixture
     */
    @Override
    public void defineBody(){
        defineBodyDef();
        body = world.createBody(bodyDef);
        defineFixtures();
    }

    public void defineBodyDef(){
        //create Body
        bodyDef.position.set(new Vector2((getX() + getWidth() / 2f) / PPM, (getY() + getHeight() / 2f) / PPM));
        bodyDef.fixedRotation = Gdx.app.getPreferences("SBPrefs").getBoolean("fixedRotation", false);
        bodyDef.angularDamping = ANGULAR_DAMPING;
        bodyDef.linearDamping = LINEAR_DAMPING;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void defineFixtures() {
        //create Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 250;
        fixtureDef.restitution = .8f;
        fixtureDef.friction = 4;
        fixtureDef.filter.categoryBits = Constants.Collision.ROCK_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.BOAT_BIT |
                Constants.Collision.STAGE_BIT |
                Constants.Collision.ROCK_BIT |
                Constants.Collision.COIN_BIT |
                Constants.Collision.SHIELD_BIT |
                Constants.Collision.QUADRANT_BIT |
                Constants.Collision.FISH_BIT |
                Constants.Collision.BOOST_BIT |
                Constants.Collision.TRUNK_BIT |
                Constants.Collision.PADDEL_BIT;

        bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, this, 1);
        for(Fixture fixture:body.getFixtureList())
            fixture.setUserData(this);
        body.setUserData(this);
    }

    /**
     * Updates position and size of the rock in every loop.
     * @param dt LibGdx delta time used to update scale properties appropriately.
     */

    @Override
    public void update(float dt){
        //shrink rock when a hit is detected by target_break
        if(target_break) {
            if (!isFlaggedForDelete()) {
                redefineBody(computeSize());
            }
            target_break = false;
        }

        if (isBroken())
            waterParticles.allowCompletion();
        waterParticles.update(dt);

        //Remove Rock when out of bounds
        if(enableOutOfBounds) removeWhenOutOfBounds(viewport);
        // Update sprite position and rotation
        capAngularVelocity();
        super.update(dt);
    }

    private void capAngularVelocity(){
        if(!isRemoved()) {
            body.setAngularDamping(ANGULAR_DAMPING);
            float speed = body.getAngularVelocity();
            if (Math.abs(speed) > MAX_ANGULAR_VELOCITY) {
                body.setAngularVelocity(Math.signum(speed) * MAX_ANGULAR_VELOCITY);
            }
        }
    }

    private boolean isRectangle() {
        return region == TextureRegions.ROCK2;
    }

    /**
     * Removes rock when out of bounds.
     */
    public void removeWhenOutOfBounds(Viewport viewport) {
        if (getY() < -HEIGHT / 2f - getHeight() + viewport.getCamera().position.y || getY() > 3 * HEIGHT + viewport.getCamera().position.y) {
            setFlaggedForDelete(true);
        }
    }


    /**
     * Repositions Rock when out of bounds.
     * <p>
     * Repositions the rock when it is out of bounds and sets it to a random position above the viewport.
     * </p>
     */
    private void translateWhenOutOfBounds(){
        if (getY() < -HEIGHT / 2f - getHeight() || getY() > HEIGHT + getHeight()) {
            //Set random x coordinate
            float rand = (float) Math.random();
            float x = -WIDTH / 2f * rand + (1 - rand) * WIDTH / 2f;

            //Set random y coordinate
            float rand2 = (float) Math.random();
            float y = HEIGHT / 2f * rand2 + (1 - rand2) * (HEIGHT);
            body.setTransform(x / PPM, y / PPM, body.getAngle());
            body.setLinearVelocity(new Vector2());
        }
    }

    /**
     * Changes the state of the sprite accordingly.
     * <p>
     *     Changes the current state to the next higher state and targets a break to update the size of the body and sprite.
     * </p>
     */
    public void break_(){
        if(!isBroken()) {
            previousState = currentState;
            if (currentState == State.NOTHIT) {
                currentState = State.HIT1;
                target_break = true;
            }else if (currentState == State.HIT1) {
                currentState = State.HIT2;
                target_break = true;
            }else if (currentState == State.HIT2) {
                currentState = State.HIT3;
                target_break = true;
            }else{
                currentState = State.BROKEN;
                target_break = true;
                setFlaggedForDelete(true);
            }
        }
    }

    /**
     * Computes the correct size after a break occurred.
     * <p>
     *     Computes the correct sprite size after {@link #break_()} was called.
     *     This method is called in the update method, when target_break is set to true.
     * </p>
     * @return A Vector2 vector with the updated size after a state change.
     */
    private Vector2 computeSize(){
        if(currentState != State.BROKEN)
            return new Vector2(getWidth(), getHeight()).scl(0.8f);
        else return new Vector2();
    }


    /**
     * Compare method to detect difference in sizes when rocks hit each other.
     * @param r The rock to compare
     * @return Standard {@link Comparable} returns based on the size of the two rocks.
     */
    public int compare(Rock r){
        if(getCurrentState() == r.getCurrentState()) return 0;
        if(getCurrentState() == State.BROKEN && r.getCurrentState() == State.HIT3) return -1;
        if(getCurrentState() == State.BROKEN && r.getCurrentState() == State.HIT2) return -1;
        if(getCurrentState() == State.BROKEN && r.getCurrentState() == State.HIT1) return -1;
        if(getCurrentState() == State.BROKEN && r.getCurrentState() == State.NOTHIT) return -1;

        if(getCurrentState() == State.HIT3 && r.getCurrentState() == State.HIT2) return -1;
        if(getCurrentState() == State.HIT3 && r.getCurrentState() == State.HIT1) return -1;
        if(getCurrentState() == State.HIT3 && r.getCurrentState() == State.NOTHIT) return -1;

        if(getCurrentState() == State.HIT2 && r.getCurrentState() == State.HIT1) return -1;
        if(getCurrentState() == State.HIT2 && r.getCurrentState() == State.NOTHIT) return -1;
        if(getCurrentState() == State.HIT1 && r.getCurrentState() == State.NOTHIT) return -1;

        if(r.getCurrentState() == State.BROKEN && getCurrentState() == State.HIT3) return 1;
        if(r.getCurrentState() == State.BROKEN && getCurrentState() == State.HIT2) return 1;
        if(r.getCurrentState() == State.BROKEN && getCurrentState() == State.HIT1) return 1;
        if(r.getCurrentState() == State.BROKEN && getCurrentState() == State.NOTHIT) return 1;

        if(r.getCurrentState() == State.HIT3 && getCurrentState() == State.HIT2) return 1;
        if(r.getCurrentState() == State.HIT3 && getCurrentState() == State.HIT1) return 1;
        if(r.getCurrentState() == State.HIT3 && getCurrentState() == State.NOTHIT) return 1;

        if(r.getCurrentState() == State.HIT2 && getCurrentState() == State.HIT1) return 1;
        if(r.getCurrentState() == State.HIT2 && getCurrentState() == State.NOTHIT) return 1;
        if(r.getCurrentState() == State.HIT1 && getCurrentState() == State.NOTHIT) return 1;

        return 0;
    }

    /**
     *
     * @return {@code true} if the rock is broken,
     * that means {@link #currentState} equals {@link State#BROKEN} and {@link #flaggedForDelete} equals {@code true},
     * otherwise {@code false}.
     */
    public boolean isBroken(){
        return currentState == State.BROKEN;
    }

    public State getCurrentState(){
        return currentState;
    }

    public State getPreviousState(){
        return previousState;
    }

    @Override
    public void dispose(){

    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setVelocity(float velocity) {
        if(velocity > MAX_VELOCITY) this.velocity = MAX_VELOCITY;
        else this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public boolean isEnableOutOfBounds() {
        return enableOutOfBounds;
    }

    public void setEnableOutOfBounds(boolean enableOutOfBounds) {
        this.enableOutOfBounds = enableOutOfBounds;
    }
}
