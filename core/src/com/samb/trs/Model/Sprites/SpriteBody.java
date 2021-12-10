package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.BodyEditorLoader;

public abstract class SpriteBody extends Sprite implements Disposable, Pool.Poolable {
    protected static int WIDTH = Constants.Rendering.WorldWidth;
    protected World world;
    protected Body body;
    protected boolean flaggedForDelete;
    protected BodyEditorLoader bodyEditorLoader;
    protected Vector2 body_vel;
    protected float body_linear_damping;
    protected float body_angle;
    protected float body_angular_vel;
    protected float body_angular_damping;
    protected BodyDef bodyDef;
    protected static int HEIGHT = Constants.Rendering.WorldHeight;
    protected static float PPM = Constants.Rendering.PPM;
    protected MainController mainController;
    protected TextureRegions region;

    public SpriteBody(Texture texture, World world, float px, float py, float pwidth, float pheight) {
        super(texture);
        this.world = world;
        setBounds(px-pwidth/2f, py-pheight/2f, pwidth, pheight);
        body_vel = new Vector2();
        bodyDef = new BodyDef();
        defineBody();
    }

    public SpriteBody(Texture texture, World world, BodyEditorLoader bodyEditorLoader, float px, float py, float pwidth, float pheight) {
        super(texture);
        this.world = world;
        this.bodyEditorLoader = bodyEditorLoader;
        body_vel = new Vector2();
        setBounds(px-pwidth/2f, py-pheight/2f, pwidth, pheight);
        bodyDef = new BodyDef();
        defineBody();
    }

    public SpriteBody(TextureRegion region, World world, BodyEditorLoader bodyEditorLoader, float px, float py, float pwidth, float pheight) {
        super(region);
        this.world = world;
        this.bodyEditorLoader = bodyEditorLoader;
        body_vel = new Vector2();
        setBounds(px-pwidth/2f, py-pheight/2f, pwidth, pheight);
        bodyDef = new BodyDef();
        defineBody();
    }

    public SpriteBody(MainController mainController, TextureRegions regions, World world, float px, float py, float pwidth, float pheight) {
        super(mainController.getAssetController().getAsset(regions));
        this.mainController = mainController;
        this.world = world;
        this.bodyEditorLoader = mainController.getAssetController().getBodyEditorLoader();
        this.region = regions;
        body_vel = new Vector2();
        setBounds(px-pwidth/2f, py-pheight/2f, pwidth, pheight);
        bodyDef = new BodyDef();
        defineBody();
    }

    @Override
    public void reset() {
        flaggedForDelete = false;
        body_vel = new Vector2();
        body_angle = 0;
        body_angular_vel = 0;
        body_angular_damping = 0;
    }

    /**
     * Creates body and fixture
     */
    public abstract void defineBody();

    @Override
    public void draw(Batch batch) {
        if(!isRemoved())
            super.draw(batch);
    }

    /**
     * Redefines body and fixture.
     * <p>
     *     Scales sprite and body to the size given by the two parameters.
     * </p>
     * @param width New width of the sprite and body
     * @param height New height of the sprite an body
     */
    public void redefineBody(float width, float height) {
        destroyBody();
        setBounds(getX(), getY(), width, height);
        defineBody();
        Vector2 pos = getWorldPosition();
        body.setTransform(pos.x, pos.y, body_angle); // Set right angle
        body.setLinearVelocity(body_vel);
        body.setAngularVelocity(body_angular_vel);
        body.setAngularDamping(body_angular_damping);
        body.setLinearDamping(body_linear_damping);
    }

    protected abstract void defineFixtures();

    public void redefineBody(Vector2 v){
        redefineBody(v.x, v.y);
    }

    public void scale(float scl) {
        getBody().getFixtureList().get(0).getShape().setRadius(scl);
    }

    /**
     * Updates position and size of the rock in every loop.
     * @param dt LibGdx delta time used to update scale properties appropriately.
     */
    public void update(float dt) {
        //Reposition sprite based on body
        Vector2 center = getCenter();
        setCenter(center.x, center.y);
        setOriginCenter();
        setRotation(getDeg());

        if(isFlaggedForDelete()) {
            destroyBody();
            flaggedForDelete = false;
        }
    }

    /**
     * Destroys the body in the given world if not already destroyed.
     */
    public void destroyBody(){
        if(!isRemoved()){
            body_vel = body.getLinearVelocity();
            body_linear_damping = body.getLinearDamping();
            body_angle = body.getAngle();
            body_angular_vel = body.getAngularVelocity();
            body_angular_damping = body.getAngularDamping();
            world.destroyBody(body);
            body = null;
        }
    }

    /**
     * @return True if the body is already removed, else false.
     */
    public boolean isRemoved() {
        return body == null;
    }

    public void setPositionForBodyAndSprite(float px, float py){
        body.setTransform(px / PPM, py / PPM, getRad());
        setCenter(px, py);
        setOriginCenter();
        setRotation(getDeg());
    }

    /**
     * @return Body position in meters
     */
    public Vector2 getWorldPosition(){
        if(!isRemoved()) return body.getPosition();
        return new Vector2(getX() + getWidth() / 2f, getY() + getHeight() / 2f).scl(1f / PPM);
    }

    /**
     * @return Center of the body in px
     */
    public Vector2 getCenter(){
        if (!isRemoved()) return body.getPosition().scl(PPM);
        return new Vector2(getX()+getWidth() / 2f, getY()+getHeight()/2f);
    }

    /**
     * @return Body angle in rad
     */
    public float getRad(){
        if(!isRemoved()) return body.getAngle();
        else return getRotation()*MathUtils.degRad;
    }

    public float getDeg(){
        if(!isRemoved()) return body.getAngle()*MathUtils.radDeg;
        else return getRotation();
    }

    /**
     * Rotates the body and therefore also the sprite.
     * <p>
     *     Rotates the body by the given {@code degree} (not radians). Keep in mind that the sprite rotates to
     *     the given {@code degree} on the next call of {@link #update(float)}.
     * </p>
     * @param degree The degree to rotate.
     */
    public void setBodyRotation(float degree){
        getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, MathUtils.degRad*degree);
    }

    public World getWorld() {
        return world;
    }

    public Body getBody() {
        return body;
    }

    public abstract void dispose();

    public void setFlaggedForDelete(boolean flaggedForDelete) {
        this.flaggedForDelete = flaggedForDelete;
    }

    public boolean isFlaggedForDelete() {
        return flaggedForDelete;
    }

    public Vector2 getBody_vel() {
        return body_vel;
    }

    public float getBody_linear_damping() {
        return body_linear_damping;
    }

    public float getBody_angle() {
        return body_angle;
    }

    public float getBody_angular_vel() {
        return body_angular_vel;
    }

    public float getBody_angular_damping() {
        return body_angular_damping;
    }

    public void overWriteBodyValues(SpriteBody spriteBody){
        Vector2 pos = spriteBody.getWorldPosition();
        body.setTransform(pos.x, pos.y, body_angle); // Set right angle
        body.setLinearVelocity(spriteBody.body_vel);
        body.setAngularVelocity(spriteBody.body_angular_vel);
        body.setAngularDamping(spriteBody.body_angular_damping);
        body.setLinearDamping(spriteBody.body_linear_damping);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public MainController getMainController() {
        return mainController;
    }
}
