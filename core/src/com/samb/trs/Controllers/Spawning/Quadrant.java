package com.samb.trs.Controllers.Spawning;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.samb.trs.Resources.Constants.Collision.QUADRANT_BIT;
import static com.samb.trs.Resources.Constants.Collision.ROCK_BIT;

public class Quadrant {
    private float x, y, width, height, initX, initY;
    private World world;
    private boolean targetRemoval, removed, overlapped;
    private Body body;

    public Quadrant(World world, float x, float y, float width, float height){
        this.world = world;
        this.x = x;
        this.y = y;
        this.initX = x;
        this.initY = y;
        this.width = width;
        this.height = height;
        defineQuadrant();
        targetRemoval = false;
        removed = false;
        overlapped = false;
    }

    public void defineQuadrant(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2((x + width / 2f), (y + height / 2f)));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);
        fixtureDef.shape = shape;

        fixtureDef.filter.maskBits = ROCK_BIT;
        fixtureDef.filter.categoryBits = QUADRANT_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void update(float dt, Viewport viewport){
        if(isTargetRemoval() && !isRemoved()){
            targetRemoval = false;
            world.destroyBody(body);
            removed = true;
        }

        body.setTransform(body.getPosition().x, (initY + 0.5f * getWidth() + viewport.getCamera().position.y), body.getAngle());
        y = initY+viewport.getCamera().position.y;
        overlapped = false;
    }

    public void draw(SpriteBatch batch, String s, BitmapFont font){
        font.draw(batch, s, body.getPosition().x, body.getPosition().y);
    }

    public boolean isOverlapped() {
        return overlapped;
    }

    public void setOverlapped(boolean overlapped) {
        this.overlapped = overlapped;
    }

    public void setTargetRemoval(boolean targetRemoval) {
        this.targetRemoval = targetRemoval;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean isTargetRemoval() {
        return targetRemoval;
    }

    public Body getBody() {
        return body;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public World getWorld() {
        return world;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    /**
     * @return Center of the body in px
     */
    public Vector2 getCenter(){
        if (!isRemoved()) return body.getPosition();
        return new Vector2(getX() + 0.5f * getWidth(), getY() + 0.5f * getHeight());
    }

    /**
     * @return Body position in meters
     */
    public Vector2 getWorldPosition(){
        if(!isRemoved()) return body.getPosition();
        return new Vector2(getX() + 0.5f * getWidth(), getY() + 0.5f * getHeight());
    }
}
