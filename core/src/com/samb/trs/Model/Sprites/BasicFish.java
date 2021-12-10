package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.Box2dAi.CustomPrioritySteering;

public class BasicFish extends AiBody implements Pool.Poolable {
    public static final float DAMPING = 1f;
    public static final float EFFECT_DURATION = 0.4f;
    public static final float MAX_FOLLOW_TIMER = 2.6f;
    private Timer follow_timer;
    private boolean followed;
    private boolean enableRemoveWhenOutOfBounds = true;

    public BasicFish(MainController mainController, TextureRegions region, World world, float px, float py, float pwidth, float pheight) {
        super(mainController, region, world, px, py, pwidth, pheight);
        follow_timer = new Timer();
    }

    @Override
    public void reset() {
        super.reset();
        follow_timer = new Timer();
        followed = false;
    }

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
        fixtureDef.friction = 0f;
        fixtureDef.filter.categoryBits = Constants.Collision.FISH_BIT;
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
        for (Fixture fixture : body.getFixtureList())
            fixture.setUserData(this);
        body.setUserData(this);
    }

    @Override
    public void update(float dt) {
        follow_timer.update(dt);
        //Remove Fish when out of bounds
        if(enableRemoveWhenOutOfBounds) removeWhenOutOfBounds(mainController.getRenderController().getDynamicViewport());
        // Update sprite position and rotation
        if(steeringBehavior instanceof CustomPrioritySteering) {
            if (!followed && ((CustomPrioritySteering<Vector2>) steeringBehavior).getPlayer() != null &&
                    ((AiBody) ((CustomPrioritySteering<Vector2>) steeringBehavior).getPlayer()).getWorldPosition().dst2(getWorldPosition()) <= 150) {
                ((CustomPrioritySteering<Vector2>) steeringBehavior).getPursue().setEnabled(true);
                followed = true;
                follow_timer.stop();
            }

            if(((CustomPrioritySteering<Vector2>) steeringBehavior).getPursue().isEnabled() && follow_timer.passed(MAX_FOLLOW_TIMER)){
                ((CustomPrioritySteering<Vector2>) steeringBehavior).getPursue().setEnabled(false);
            }
        }

        super.update(dt);
    }

    /** apply steering to the Box2d body
     * @param steering the steering vector
     * @param deltaTime teh delta time
     */
    @Override
    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringAcceleration.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(new Vector2(steeringAcceleration.linear), true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringAcceleration.angular != 0) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringAcceleration.angular, true);
                anyAccelerations = true;
            }
        } else {
            // If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                setOrientation(newOrientation);
            }
        }

        if (anyAccelerations) {
            // Cap the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }
            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
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
    public void dispose() {

    }

    @Override
    public void setOrientation(float orientation) {
        super.setOrientation(orientation-MathUtils.PI/2f);
    }

    @Override
    public float getOrientation() {
        return super.getOrientation() + MathUtils.PI / 2f;
    }

    public boolean isEnableRemoveWhenOutOfBounds() {
        return enableRemoveWhenOutOfBounds;
    }

    public void setEnableRemoveWhenOutOfBounds(boolean enableRemoveWhenOutOfBounds) {
        this.enableRemoveWhenOutOfBounds = enableRemoveWhenOutOfBounds;
    }
}
