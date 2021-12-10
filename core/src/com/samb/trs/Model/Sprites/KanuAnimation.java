package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.Sounds;
import com.samb.trs.Resources.TextureRegions;


public class KanuAnimation {
    private MainController mainController;
    private Sprite kanuPaddel, kanuMann;
    private Timer animationTimer;
    private Array<Fixture> fixtures;
    private float frequency;
    private Body body;
    private PaddleDirection direction;
    private boolean paddleSoundActive;
    private MouseJoint mouseJoint;

    public KanuAnimation(MainController mainController, SpriteBody spriteBody, float px, float py) {
        this.mainController = mainController;

        kanuPaddel = new Sprite(mainController.getAssetController().getAsset(TextureRegions.KANU_PADDEL));
        kanuMann = new Sprite(mainController.getAssetController().getAsset(TextureRegions.KANU_MANN));

        kanuMann.setBounds(px - 20.5f, py - 36.5f, 41, 73);
        kanuPaddel.setBounds(px - 127.5f, py - 13f, 255, 26);

        animationTimer = new Timer();

        fixtures = new Array<>();

        defineFixture(spriteBody);

        defineMouseJoint();

        frequency = 1f;
        direction = PaddleDirection.MIDDLE;
        paddleSoundActive = true;
    }

    public void update(float dt, Vector2 center){
        animationTimer.update(dt);
        if(direction == PaddleDirection.RIGHT)
            paddle(dt, Interpolation.slowFast, Interpolation.fastSlow, -45, 45);
        else if(direction == PaddleDirection.LEFT)
            paddle(dt, Interpolation.fastSlow, Interpolation.slowFast, -45, 45);
        else
            paddle(dt, Interpolation.slowFast, Interpolation.slowFast, -45, 45);

        //kanuPaddel.setRotation(body.getAngle()*MathUtils.radDeg);
        body.setTransform(body.getPosition(), kanuPaddel.getRotation() * MathUtils.degRad);
        //body.setTransform(center.x / PPM, (center.y - RenderController.cph() * 5) / PPM, body.getAngle());

        if(paddleSoundActive && inRangeOf(kanuPaddel.getRotation(), 45, 10f)){
            mainController.getSoundController().queueSound(kanuPaddel, Sounds.PADDLE);
            paddleSoundActive = false;
        }else if(paddleSoundActive && inRangeOf(kanuPaddel.getRotation(), -45, 10f)){
            mainController.getSoundController().queueSound(kanuPaddel, Sounds.PADDLE);
            paddleSoundActive = false;
        }else if(inRangeOf(kanuPaddel.getRotation(), 0, 10f)){
            paddleSoundActive = true;
        }
        moveTo(center);
    }

    public void moveTo(Vector2 center) {
        kanuPaddel.setCenter(center.x, center.y - RenderController.cph() * 5);
        kanuPaddel.setOriginCenter();

        kanuMann.setCenter(center.x, center.y - RenderController.cph() * 10);
        kanuMann.setOriginCenter();
    }

    private void paddle(float dt, Interpolation in, Interpolation out, float deg1, float deg2){
        float b = 0;
        float a = 0;
        float rotMann = 0.2f;

        if (animationTimer.passedAbsolute(frequency)) {
            animationTimer.reset();
        } else if (animationTimer.passedAbsolute(frequency / 2f)) {
            b = (animationTimer.getValue() - frequency / 2f) / (frequency / 2f);
            a = out.apply(b);
            //applyAngularImpulse((1-a)*deg1 + a*deg2);
            kanuPaddel.setRotation((1-a)*deg1 + a*deg2);
            kanuMann.setRotation((1-a)*deg1*rotMann + a*deg2*rotMann);
        }else{
            b = animationTimer.getValue() / (frequency / 2f);
            a = in.apply(b);
            //applyAngularImpulse(a*deg1+(1-a)*deg2);
            kanuPaddel.setRotation(a*deg1+(1-a)*deg2);
            kanuMann.setRotation(a*deg1*rotMann+(1-a)*deg2*rotMann);
        }
    }

    public void applyAngularImpulse(float desiredAngle) {
        float nextAngle = body.getAngle() + body.getAngularVelocity() * Constants.Rendering.TIMESTEP;
        float totalRotation = desiredAngle * MathUtils.degRad - nextAngle;
        while (totalRotation < -180 * MathUtils.degRad) totalRotation += 360 * MathUtils.degRad;
        while (totalRotation > 180 * MathUtils.degRad) totalRotation -= 360 * MathUtils.degRad;
        float desiredAngularVelocity = totalRotation / Constants.Rendering.TIMESTEP;
        float torque = body.getInertia() * desiredAngularVelocity / Constants.Rendering.TIMESTEP;
        body.applyTorque(torque, true);
    }

    private boolean inRangeOf(float deg1, float deg2, float range){
        return Math.abs(deg1 - deg2) < range;
    }

    private float getInverseTime(Interpolation in, Interpolation out, float deg, float deg1, float deg2){
        float t;
        float a;

        if (animationTimer.getValue() >= frequency / 2f) {
            a = Math.abs((deg - deg1) / (deg2 - deg1));
            t = (frequency / 2f)*(1+ com.samb.trs.Utilities.Interpolation.inverse(out, a));

        }else{
            a = Math.abs((deg - deg2) / (deg1 - deg2));
            t = (frequency / 2f) * com.samb.trs.Utilities.Interpolation.inverse(in, a);
        }

        return t;
    }

    public void defineFixture(SpriteBody spriteBody){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 5000;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.9f;
        fixtureDef.filter.categoryBits = Constants.Collision.PADDEL_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.ROCK_BIT | Constants.Collision.FISH_BIT | Constants.Collision.TRUNK_BIT;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(kanuPaddel.getX() + kanuPaddel.getWidth()/2f, kanuPaddel.getY() + kanuPaddel.getHeight()/2f);
        body = spriteBody.getWorld().createBody(bodyDef);
        spriteBody.bodyEditorLoader.attachFixture(body, TextureRegions.KANU_PADDEL.getIdentifier(), fixtureDef, kanuPaddel, 1);
        for(Fixture fixture:body.getFixtureList()){
            if (fixture.getFilterData().categoryBits == Constants.Collision.PADDEL_BIT) {
                fixtures.add(fixture);
                fixture.setSensor(false);
                fixture.setUserData(spriteBody);
            }
        }
    }

    /**
     * Defines the MouseJoint for later player movement
     */
    private void defineMouseJoint() {
        // TODO: cleaner solution for attaching paddle to the body while remaining physics on movement
        MouseJointDef def = new MouseJointDef();
        def.bodyA = findBoundsBody();
        def.bodyB = body;
        def.collideConnected = true;
        def.target.set(new Vector2());
        def.maxForce = 10000.0f * body.getMass();
        def.frequencyHz = 10.0f;

        mouseJoint = (MouseJoint) body.getWorld().createJoint(def);
    }

    private Body findBoundsBody() {
        Array<Body> bodies = new Array<>();
        body.getWorld().getBodies(bodies);
        Body bd = bodies.random();
        for (Body b : bodies) {
            if (b.getUserData() instanceof LeftBounds)
                bd = b;
        }
        return bd;
    }

    public enum PaddleDirection {RIGHT, LEFT, MIDDLE}

    public void draw(Batch batch){
        kanuMann.draw(batch);
        kanuPaddel.draw(batch);
    }

    public void setDirection(PaddleDirection direction) {
        if(this.direction != direction) {
            Interpolation in = Interpolation.slowFast;
            Interpolation out = Interpolation.slowFast;
            switch (direction){
                case RIGHT:
                    in = Interpolation.slowFast;
                    out = Interpolation.fastSlow;
                    break;
                case LEFT:
                    in = Interpolation.fastSlow;
                    out = Interpolation.slowFast;
                    break;
            }

            animationTimer.set(getInverseTime(in, out, kanuPaddel.getRotation(), -45, 45));
        }
        this.direction = direction;
    }

    public PaddleDirection getDirection() {
        return direction;
    }

    public void setAnimationFrequency(float animationFrequency) {
        frequency = animationFrequency;
    }

    public Body getBody() {
        return body;
    }

    public MouseJoint getMouseJoint() {
        return mouseJoint;
    }
}
