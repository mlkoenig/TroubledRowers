package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.BodyEditorLoader;
import com.samb.trs.Utilities.Box2dAi.Box2dLocation;
import com.samb.trs.Utilities.Box2dAi.Box2dSteeringUtils;


public abstract class LocationBody extends SpriteBody implements Location<Vector2> {
    public LocationBody(Texture texture, World world, BodyEditorLoader bodyEditorLoader, float px, float py, float pwidth, float pheight){
        super(texture, world, bodyEditorLoader, px, py, pwidth, pheight);
    }

    public LocationBody(MainController mainController, TextureRegions regions, World world, float px, float py, float pwidth, float pheight) {
        super(mainController, regions, world, px, py, pwidth, pheight);
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }
}
