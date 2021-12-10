package com.samb.trs.Model.Sprites;

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

public class Coin extends LocationBody implements Drawable {
    private float velocity;
    private Viewport viewport;
    private boolean enableRemoveWhenOutOfBounds = true;

    public static final float MAX_VELOCITY = 40f;
    public static final float DAMPING = 1.3f;
    public static final float EFFECT_DURATION = 0.3f;

    public Coin(MainController mainController, TextureRegions regions, World world, Viewport viewport, float px, float py, float radius) {
        super(mainController, regions, world, px, py, 2 * radius, 2 * radius);
        this.viewport = viewport;
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
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = DAMPING;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    @Override
    public void defineFixtures() {
        //create Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0.9f;
        fixtureDef.filter.categoryBits = Constants.Collision.COIN_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.BOAT_BIT |
                Constants.Collision.STAGE_BIT |
                Constants.Collision.ROCK_BIT |
                Constants.Collision.COIN_BIT |
                Constants.Collision.SHIELD_BIT |
                Constants.Collision.FISH_BIT |
                Constants.Collision.BOOST_BIT |
                Constants.Collision.TRUNK_BIT |
                Constants.Collision.PADDEL_BIT;

        bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, this, 1);
        for(Fixture f:body.getFixtureList())
            f.setUserData(this);
        body.setUserData(this);
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

    /**
     * Updates position and size of the rock in every loop.
     * @param dt LibGdx delta time used to update scale properties appropriately.
     */
    public void update(float dt){
        //Target a removal when coin is out of bounds
        if(enableRemoveWhenOutOfBounds) removeWhenOutOfBounds(viewport);
        //reposition sprite based on body
        super.update(dt);
    }

    /**
     * Repositions Rock when out of bounds.
     * <p>
     * Repositions the rock when it is out of bounds and sets it to a random position above the viewport.
     * </p>
     */
    private void translateWhenOutofBounds(){
        if (getY() < -HEIGHT / 2f - getHeight() || getY() > HEIGHT + getHeight()) {
            //Set random x coordinate
            float rand = (float) Math.random();
            float x = -WIDTH / 2f * rand + (1 - rand) * WIDTH / 2;

            //Set random y coordinate
            float rand2 = (float) Math.random();
            float y = HEIGHT / 2f * rand2 + (1 - rand2) * (HEIGHT);
            body.setTransform(x / PPM, y / PPM, body.getAngle());
            body.setLinearVelocity(new Vector2());
        }
    }

    /**
     * Removes rock when out of bounds.
     */
    public void removeWhenOutOfBounds(Viewport viewport) {
        if (getY() < -HEIGHT / 2f - getHeight() + viewport.getCamera().position.y || getY() > 3 * HEIGHT + getHeight() + viewport.getCamera().position.y) {
            setFlaggedForDelete(true);
        }
    }

    public void setVelocity(float velocity) {
        if(velocity > MAX_VELOCITY) this.velocity = MAX_VELOCITY;
        else this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    @Override
    public void dispose() {

    }

    public boolean isEnableRemoveWhenOutOfBounds() {
        return enableRemoveWhenOutOfBounds;
    }

    public void setEnableRemoveWhenOutOfBounds(boolean enableRemoveWhenOutOfBounds) {
        this.enableRemoveWhenOutOfBounds = enableRemoveWhenOutOfBounds;
    }
}
