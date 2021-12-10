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

public class LeftBounds extends SpriteBody implements Shore {
    private float velocity;
    public static float MAX_VELOCITY = 40f;
    public static final float EFFECT_DURATION = 0.4f;

    public LeftBounds(MainController mainController, TextureRegions regions, World world, float px, float py, float pwidth, float pheight) {
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
//        float[] vertices = new float[]{transformX(0), transformY(4508), transformX(173), transformY(4508), transformX(86), transformY(4335), transformX(0), transformY(4335)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        shape = new PolygonShape();
//        vertices = new float[]{transformX(0), transformY(4335), transformX(86), transformY(4335), transformX(173), transformY(4100), transformX(173), transformY(3605), transformX(0), transformY(3605)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(3605), transformX(173), transformY(3605), transformX(273), transformY(3504), transformX(174), transformY(3422), transformX(0), transformY(3422)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(3422), transformX(174), transformY(3422), transformX(273), transformY(3313), transformX(86), transformY(3080), transformX(0), transformY(3080)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(3080), transformX(86), transformY(3080), transformX(86), transformY(2931), transformX(0), transformY(2931)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(2931), transformX(86), transformY(2931), transformX(173), transformY(2760), transformX(173), transformY(2475), transformX(86), transformY(2191), transformX(0), transformY(2191)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(2191), transformX(86), transformY(2191), transformX(174), transformY(1784), transformX(86), transformY(1679), transformX(0), transformY(1679)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(1679), transformX(86), transformY(1679), transformX(173), transformY(1529), transformX(86), transformY(1259), transformX(0), transformY(1259)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(1259), transformX(86), transformY(1259), transformX(173), transformY(1080), transformX(173), transformY(597), transformX(86), transformY(335), transformX(0), transformY(335)};
//        shape.set(vertices);
//        fixtureDef.shape = shape;
//
//        body.createFixture(fixtureDef).setUserData(this);
//
//        vertices = new float[]{transformX(0), transformY(335), transformX(86), transformY(335), transformX(174), transformY(0), transformX(0), transformY(0)};
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
        return (x / PPM) * (getWidth() / 345f) - getWidth() / 2 / PPM;
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
        // Set speed
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
