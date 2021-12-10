package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Interfaces.Shore;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;

public class RightBounds extends SpriteBody implements Shore {
    private float velocity;
    public static float MAX_VELOCITY = 40f;
    public static final float EFFECT_DURATION = 0.4f;

    public RightBounds(MainController mainController, TextureRegions regions, World world, float px, float py, float pwidth, float pheight) {
        super(mainController, regions, world, px, py, pwidth, pheight);
    }

    @Override
    public void reset() {}

    @Override
    public void defineBody(){
        defineBodyDef();
        body = world.createBody(bodyDef);
        defineFixtures();
    }

    public void defineBodyDef(){
        //create Body
        bodyDef.position.set(new Vector2((getX() + getWidth() / 2f) / PPM, (getY() + getHeight() / 2f) / PPM));
        bodyDef.type = BodyDef.BodyType.KinematicBody;
    }

    @Override
    protected void defineFixtures() {
        //create Fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1000;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 1;
        fixtureDef.filter.categoryBits = Constants.Collision.STAGE_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.BOAT_BIT |
                Constants.Collision.ROCK_BIT |
                Constants.Collision.COIN_BIT |
                Constants.Collision.FISH_BIT |
                Constants.Collision.BOOST_BIT |
                Constants.Collision.TRUNK_BIT;

//        PolygonShape shape = new PolygonShape();
//        float[] vertices = new float[]{transformX(275), transformY(4509), transformX(100), transformY(4509), transformX(188), transformY(4174), transformX(275), transformY(4174)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        shape = new PolygonShape();
//        vertices = new float[]{transformX(275), transformY(4174), transformX(188), transformY(4174), transformX(100), transformY(3910), transformX(100), transformY(3429), transformX(188), transformY(3250), transformX(275), transformY(3250)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(3250), transformX(188), transformY(3250), transformX(100), transformY(2980), transformX(187), transformY(2830), transformX(275), transformY(2830)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(2830), transformX(187), transformY(2830), transformX(100), transformY(2724), transformX(187), transformY(2318), transformX(275), transformY(2318)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(2318), transformX(187), transformY(2318), transformX(100), transformY(2031), transformX(100), transformY(1748), transformX(188), transformY(1576), transformX(275), transformY(1576)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(1576), transformX(188), transformY(1576), transformX(188), transformY(1428), transformX(275), transformY(1428)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(1428), transformX(188), transformY(1428), transformX(0), transformY(1195), transformX(100), transformY(1086), transformX(275), transformY(1086)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(1086), transformX(100), transformY(1086), transformX(1), transformY(1005), transformX(1), transformY(1006), transformX(100), transformY(903), transformX(275), transformY(903)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(903), transformX(100), transformY(903), transformX(100), transformY(407), transformX(188), transformY(174), transformX(275), transformY(174)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(275), transformY(174), transformX(188), transformY(174), transformX(100), transformY(0), transformX(275), transformY(0)};
//
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//        shape.dispose();
        bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, this, 1);
        for(Fixture f:body.getFixtureList())
            f.setUserData(this);
        body.setUserData(this);
    }

    private float transformX(float x){
        return (x / PPM) * (getWidth() / 345f) - getWidth() / 2 / PPM + 40 / PPM;
    }

    private float transformY(float y){
        return (4509f - y) / PPM * (getHeight() / 4509f) - getHeight() / 2 / PPM;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        Vector2 pos = getWorldPosition();
    }

    public void update(float dt){
        // Set Speed
        //body.setLinearVelocity(new Vector2(0, -velocity));

        //Reposition sprite based on body
        super.update(dt);
    }

    public void setVelocity(float velocity) {
        if(velocity > MAX_VELOCITY) this.velocity = MAX_VELOCITY;
        else this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    @Override
    public void dispose() {}
}
