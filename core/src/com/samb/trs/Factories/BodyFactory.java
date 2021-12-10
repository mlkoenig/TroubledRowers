package com.samb.trs.Factories;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.BodyEditorLoader;

import static com.samb.trs.Components.TypeComponent.*;
import static com.samb.trs.Resources.Constants.Rendering.PPM;

public class BodyFactory {
    private static BodyFactory bodyFactory;
    private World world;
    private BodyEditorLoader bodyEditorLoader;
    private MainController mainController;

    private BodyFactory(MainController mainController, World world) {
        this.world = world;
        this.bodyEditorLoader = mainController.getAssetController().getBodyEditorLoader();
        this.mainController = mainController;
    }

    public static BodyFactory getInstance(MainController mainController, World world) {
        if (bodyFactory == null) {
            bodyFactory = new BodyFactory(mainController, world);
        }

        return bodyFactory;
    }

    public Body makeBody(TextureRegions region, float x, float y, float width, float height) {
        Body body = world.createBody(defineBodyDef(region, x, y));
        defineFixtures(body, region, width, height);
        return body;
    }

    public Body makeQuadrantBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x * Constants.Rendering.PPM_INV, y * Constants.Rendering.PPM_INV);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / PPM / 2f, height / PPM / 2f);
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    private BodyDef defineBodyDef(TextureRegions region, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x * Constants.Rendering.PPM_INV, y * Constants.Rendering.PPM_INV);

        switch (region) {
            default:
            case COIN:
            case BOOST:
                bodyDef.linearDamping = 1.3f;
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
            case ROCK:
            case ROCK2:
                bodyDef.angularDamping = 1.4f;
                bodyDef.linearDamping = 1.4f;
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
            case FISH:
                bodyDef.linearDamping = 1;
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
            case UFER_RECHTS_NEU:
            case UFER_LINKS_NEU:
                bodyDef.type = BodyDef.BodyType.KinematicBody;
                break;
            case TRUNK:
                bodyDef.linearDamping = 1.4f;
                bodyDef.angularDamping = 1.4f;
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
            case KANU_RUMPF:
            case SHIELD_BACKGROUND:
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                bodyDef.gravityScale = 0;
                bodyDef.fixedRotation = true;
                break;
            case KANU_PADDEL:
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
        }

        return bodyDef;
    }

    private void defineFixtures(Body body, TextureRegions region, float width, float height) {
        FixtureDef fixtureDef = new FixtureDef();
        TextureRegion reg = mainController.getAssetController().getAsset(region);

        switch (region) {
            case COIN:
                fixtureDef.density = 1;
                fixtureDef.friction = 1;
                fixtureDef.restitution = 1;
                fixtureDef.filter.categoryBits = COIN;
                fixtureDef.filter.maskBits = BOAT |
                        SHORE |
                        ROCK |
                        COIN |
                        SHIELD |
                        FISH |
                        BOOST |
                        TRUNK |
                        PADDEL;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;
            case BOOST:
                fixtureDef.density = 1;
                fixtureDef.restitution = 1;
                fixtureDef.friction = 1;
                fixtureDef.filter.categoryBits = BOOST;
                fixtureDef.filter.maskBits = BOAT |
                        SHORE |
                        ROCK |
                        COIN |
                        SHIELD |
                        FISH |
                        BOOST |
                        TRUNK |
                        PADDEL;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;
            case ROCK:
            case ROCK2:
                fixtureDef.density = 250;
                fixtureDef.restitution = .8f;
                fixtureDef.friction = 4;
                fixtureDef.filter.categoryBits = ROCK;
                fixtureDef.filter.maskBits = BOAT |
                        SHORE |
                        ROCK |
                        COIN |
                        SHIELD |
                        QUADRANT |
                        FISH |
                        BOOST |
                        TRUNK |
                        PADDEL;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;

            case FISH:
                fixtureDef.density = 1;
                fixtureDef.restitution = 1;
                fixtureDef.friction = 0f;
                fixtureDef.filter.categoryBits = FISH;
                fixtureDef.filter.maskBits = BOAT |
                        SHORE |
                        ROCK |
                        COIN |
                        SHIELD |
                        QUADRANT |
                        FISH |
                        BOOST |
                        TRUNK;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;
            case UFER_LINKS_NEU:
            case UFER_RECHTS_NEU:
                fixtureDef.density = 1000;
                fixtureDef.restitution = 1;
                fixtureDef.friction = 1;
                fixtureDef.filter.categoryBits = SHORE;
                fixtureDef.filter.maskBits = BOAT |
                        ROCK |
                        COIN |
                        FISH |
                        BOOST |
                        TRUNK;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;
            case TRUNK:
                fixtureDef.density = 1200;
                fixtureDef.restitution = .6f;
                fixtureDef.friction = 4;
                fixtureDef.filter.categoryBits = TRUNK;
                fixtureDef.filter.maskBits = BOAT |
                        SHORE |
                        ROCK |
                        COIN |
                        SHIELD |
                        QUADRANT |
                        FISH |
                        BOOST |
                        TRUNK;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;

            case KANU_RUMPF:
                fixtureDef.density = 100;
                fixtureDef.restitution = 1;
                fixtureDef.friction = 0.9f;
                fixtureDef.filter.categoryBits = BOAT;
                fixtureDef.filter.maskBits = SHORE |
                        ROCK |
                        COIN |
                        SHIELD |
                        FISH |
                        BOOST |
                        TRUNK;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;

            case SHIELD_BACKGROUND:
                fixtureDef.density = 100;
                fixtureDef.restitution = 1;
                fixtureDef.friction = 0.9f;
                fixtureDef.filter.categoryBits = SHIELD;
                fixtureDef.filter.maskBits = ROCK | FISH | TRUNK;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                for (Fixture fixture : body.getFixtureList()) {
                    if (fixture.getFilterData().categoryBits == SHIELD) {
                        //fixture.setSensor(true);
                    }
                }
                break;
            case KANU_PADDEL:
                fixtureDef.density = 5000;
                fixtureDef.restitution = 0.5f;
                fixtureDef.friction = 0.9f;
                fixtureDef.filter.categoryBits = PADDEL;
                fixtureDef.filter.maskBits = ROCK | FISH | TRUNK;
                bodyEditorLoader.attachFixture(body, region.getIdentifier(), fixtureDef, (float) reg.getRegionWidth(), (float) reg.getRegionHeight(), width, height, 1);
                break;
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public MainController getMainController() {
        return mainController;
    }
}
