package com.samb.trs.Factories;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.BodyEditorLoader;

import static com.samb.trs.Components.TypeComponent.*;

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
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f * width, 0.5f * height);
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    private BodyDef defineBodyDef(TextureRegions region, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);

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
            case FISH2:
                bodyDef.linearDamping = 1;
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
            case UFER_RECHTS:
            case UFER_LINKS:
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

    private void defineFixtures(Body body, TextureRegions regions, float width, float height) {
        FixtureDef fixtureDef = new FixtureDef();
        TextureRegion region = mainController.getAssetController().getAsset(regions);

        switch (regions) {
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
                break;

            case FISH:
            case FISH2:
                fixtureDef.density = 75;
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
                break;
            case UFER_LINKS:
            case UFER_RECHTS:
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
                break;

            case SHIELD_BACKGROUND:
                fixtureDef.density = 100;
                fixtureDef.restitution = 1;
                fixtureDef.friction = 0.9f;
                fixtureDef.filter.categoryBits = SHIELD;
                fixtureDef.filter.maskBits = ROCK | FISH | TRUNK;
                break;
            case KANU_PADDEL:
                fixtureDef.density = 5000;
                fixtureDef.restitution = 0.5f;
                fixtureDef.friction = 0.9f;
                fixtureDef.filter.categoryBits = PADDEL;
                fixtureDef.filter.maskBits = ROCK | FISH | TRUNK;
                break;
        }

        float norm_scl = 1.0f / region.getRegionWidth();
        bodyEditorLoader.attachFixture(body, regions.getIdentifier(), fixtureDef, (float) region.getRegionWidth(), (float) region.getRegionHeight(), width, height, norm_scl);
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
