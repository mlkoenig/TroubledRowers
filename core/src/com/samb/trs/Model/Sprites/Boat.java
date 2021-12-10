package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Interfaces.Drawable;
import com.samb.trs.Model.Score;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.*;

//TODO: Add particle effect for sprite movement in water

public class Boat extends AiBody implements Drawable {
    private static final int VERTEX_SIZE = 2 + 1 + 2;
    private static final int SPRITE_SIZE = 4*VERTEX_SIZE;
    private static final int SHIELD_TIME = 2;
    public enum State {ALIVE, DEAD}
    private State currentState, previousState;
    private Score score;
    private boolean dead;
    private boolean isProtected;
    private boolean shielded;
    private boolean godMode;
    private Timer shieldTimer;
    private Array<Vector2> pos;
    private MouseJoint mouseJoint;
    private ParticleEffect waveParticles;

    private Shield shield;

    private KanuAnimation kanuAnimation;

    public Boat(MainController mainController, TextureRegions region, World world, Score score, float px, float py) {
        super(mainController, region, world, px, py, 80, 400);

        kanuAnimation = new KanuAnimation(mainController, this, px, py);

        shield = new Shield(mainController, px, py, 450, 450);
        shield.defineFixture(this);

        this.score = score;

        //Initialize position array
        pos = new Array<>();
        pos.add(new Vector2(getWorldPosition().x, getWorldPosition().y));

        //Define MouseJoint
        defineMouseJoint();

        // Set player states
        previousState = State.ALIVE;
        currentState = State.ALIVE;
        dead = false;
        isProtected = false;
        shielded = false;
        godMode = false;
        shieldTimer = new Timer();
        boundingRadius = 0;

        waveParticles = new ParticleEffect();
        waveParticles.load(Gdx.files.internal("Particles/water.p"), mainController.getAssetController().getAsset(Atlases.TEXTURES));
        waveParticles.start();
    }

    @Override
    public void reset() {
        super.reset();
    }

    /**
     * Defines the MouseJoint for later player movement
     */
    private void defineMouseJoint(){
        MouseJointDef def = new MouseJointDef();
        def.bodyA = findBoundsBody();
        def.bodyB = body;
        def.collideConnected = true;
        def.target.set(new Vector2());
        def.maxForce = 10000.0f * body.getMass();
        def.frequencyHz = 10.0f;

        mouseJoint = (MouseJoint)world.createJoint(def);
    }

    private Body findBoundsBody(){
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        Body bd = bodies.random();
        for(Body b : bodies) {
            if (b.getUserData() instanceof LeftBounds)
                bd = b;
        }
        return bd;
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

    private void defineBodyDef(){
        //create Body
        bodyDef.position.set(new Vector2((getX() + getWidth() / 2f) / PPM, (getY() + getHeight() / 2f) / PPM));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0;
        bodyDef.fixedRotation = true;
    }

    @Override
    public void defineFixtures() {
        //create Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0.9f;
        fixtureDef.filter.categoryBits = Constants.Collision.BOAT_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.STAGE_BIT |
                Constants.Collision.ROCK_BIT |
                Constants.Collision.COIN_BIT |
                Constants.Collision.SHIELD_BIT |
                Constants.Collision.FISH_BIT |
                Constants.Collision.BOOST_BIT |
                Constants.Collision.TRUNK_BIT;

        bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, this, 1);

        for (Fixture f : body.getFixtureList())
            f.setUserData(this);

        body.setUserData(this);
    }

    /**
     * Moves the object to position {@code x} and {@code y}.
     * <p>
     *     Needs {@code camera} and {@code viewport} for unprojecting the given coordinates.
     * </p>
     * @param x The {@code x} coordinate
     * @param y The {@code y} coordinate
     * @param camera The current game's {@code camera}
     * @param viewport The current game's {@code viewport}
     */
    public void moveTo(float x, float y, Camera camera, Viewport viewport){
        Vector3 input = new Vector3(x, y, 0);
        input = camera.unproject(input, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        mouseJoint.setTarget(new Vector2(input.x / PPM, (input.y + getHeight() / 2f + 110) / PPM));
        kanuAnimation.getMouseJoint().setTarget(new Vector2(input.x / PPM, (input.y + getHeight() / 2f + 110) / PPM));
    }

    public void moveTo(float x, float y){
        mouseJoint.setTarget(new Vector2(x / PPM, y / PPM));
        kanuAnimation.getMouseJoint().setTarget(new Vector2(x / PPM, y / PPM));
    }

    @Override
    public void drawShadow(Batch batch) {
        if (!isRemoved()) {
            super.draw(batch);
            kanuAnimation.draw(batch);
        }
    }

    @Override
    public void drawUnder(Batch batch) {
        waveParticles.setPosition(getCenter().x, getCenter().y);
        waveParticles.draw(batch, Gdx.graphics.getDeltaTime());
    }

    @Override
    public void drawBase(Batch batch) {
        if(!isRemoved()){
            super.draw(batch);
            kanuAnimation.draw(batch);
        }
    }

    @Override
    public void drawOver(Batch batch) {
        if(isShielded()){
            shield.draw(batch);
        }
    }

    /**
     * Updates position and rotation of the sprite appropriately on every call. Should be called in the render method of the screen.
     * @param dt The LibGdx delta time to update properties scale appropriately.
     */
    public void update(float dt){
        if (isDead())
            waveParticles.allowCompletion();
        waveParticles.update(dt);

        //Update texture
        if(pos.size > 1) {
            int i = pos.size-1;
            if (pos.get(i).x > pos.get(i-1).x){
                kanuAnimation.setDirection(KanuAnimation.PaddleDirection.RIGHT);
            }else if(pos.get(i).x < pos.get(i-1).x){
                kanuAnimation.setDirection(KanuAnimation.PaddleDirection.LEFT);
            }else kanuAnimation.setDirection(KanuAnimation.PaddleDirection.MIDDLE);
        }

        if(shielded) {
            shield.setSensor(false);
            if (shieldTimer.passed(SHIELD_TIME)) {
                shield.setSensor(true);
                shielded = false;
                mainController.getSoundController().getMusic(Musics.SHIELD).stop();
            } else
                shieldTimer.update(dt);
        }else{
            shield.setSensor(true);
        }

        //Update position vector
        pos.add(new Vector2(getWorldPosition().x, getWorldPosition().y));
        if(pos.size > 5) pos.removeIndex(0);

        Vector2 center = getCenter();
        kanuAnimation.update(dt, center);

        super.update(dt);
        shield.update(dt, getCenter());
    }

    /**
     * Lets the player die and sets the player's {@link #currentState} to {@code State.DEAD} and {@link #dead} to {@code true}.
     */

    public void die(){
        if(!isDead() && currentState == State.ALIVE){
            previousState = State.ALIVE;
            currentState = State.DEAD;
            dead = true;
            mainController.getSoundController().queueSound(this, Sounds.CRASH);
            mainController.getSoundController().getMusic(Musics.SHIELD).stop();
        }
    }

    /**
     * Returns true if the player is dead.
     * @return {@link #dead}.
     */
    public boolean isDead(){
        return dead;
    }

    /**
     * Set {@link #isProtected} to the given parameter {@code boolean} {@code isProtected}
     */
    public void setProtected(boolean isProtected){
        this.isProtected = isProtected;
    }

    public void shield() {
        if (!isDead() && (!isShielded() || shieldTimer.getValue() > 4*SHIELD_TIME / 5f) && score.getBoosts() > 0){
            score.decreaseBoosts();
            shieldTimer.reset();
            shielded = true;
            mainController.getSoundController().getMusic(Musics.SHIELD).play();
        }
    }
    /**
     * Returns {@code true} if the player is protected by the head.
     * @return {@link #isProtected}
     */
    public boolean isProtected(){
        return isProtected;
    }

    /**
     * Returns {@code true} if the player is shielded
     * @return {@link #shielded}
     */
    public boolean isShielded() {
        return shielded;
    }

    public State getCurrentState(){
        return currentState;
    }

    public State getPreviousState(){
        return previousState;
    }

    public Array<Vector2> getPreviousPos() {
        return pos;
    }

    public Score getScore() {
        return score;
    }

    public Timer getShieldTimer() {
        return shieldTimer;
    }

    public boolean isGodMode() {
        return godMode;
    }

    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    @Override
    public void dispose() {
        shield.dispose();
    }

    public KanuAnimation getKanuAnimation() {
        return kanuAnimation;
    }
}
