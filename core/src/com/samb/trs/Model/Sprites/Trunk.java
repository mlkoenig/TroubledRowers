package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class Trunk extends AiBody implements Drawable {
    private float velocity;
    private Viewport viewport;
    private boolean enableOutOfBounds = true;
    public static final float MAX_VELOCITY = 40f;
    public static final float MAX_ANGULAR_VELOCITY = 4f;
    public static final float LINEAR_DAMPING = 1.4f;
    public static final float ANGULAR_DAMPING = 1.4f;
    public static final float EFFECT_DURATION = 0.4f;

    public Trunk(MainController mainController, TextureRegions regions, World world, Viewport viewport, float px, float py, float pwidth, float pheight) {
        super(mainController, regions, world, px, py, pwidth, pheight);
        this.viewport = viewport;
    }

    @Override
    public void drawShadow(Batch batch) {
        draw(batch);
    }

    @Override
    public void drawUnder(Batch batch) {

    }

    @Override
    public void drawBase(Batch batch) {
        draw(batch);
    }

    @Override
    public void drawOver(Batch batch) {
        Vector2 pos = getWorldPosition();
    }

    @Override
    public void reset() {
        super.reset();
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
        fixtureDef.density = 500;
        fixtureDef.restitution = .6f;
        fixtureDef.friction = 4;
        fixtureDef.filter.categoryBits = Constants.Collision.TRUNK_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.BOAT_BIT |
                Constants.Collision.STAGE_BIT |
                Constants.Collision.ROCK_BIT |
                Constants.Collision.COIN_BIT |
                Constants.Collision.SHIELD_BIT |
                Constants.Collision.QUADRANT_BIT |
                Constants.Collision.FISH_BIT |
                Constants.Collision.BOOST_BIT |
                Constants.Collision.TRUNK_BIT;

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
        //Remove Trunk when out of bounds
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

    /**
     * Removes rock when out of bounds.
     */
    public void removeWhenOutOfBounds(Viewport viewport) {
        if (getY() < -HEIGHT / 2f - getHeight() + viewport.getCamera().position.y || getY() > 3 * HEIGHT + viewport.getCamera().position.y) {
            setFlaggedForDelete(true);
        }
    }

    @Override
    public void dispose(){

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
