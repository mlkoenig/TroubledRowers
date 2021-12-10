package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.RowingComponent;
import com.samb.trs.Components.TransformComponent;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.Sounds;
import com.samb.trs.Utilities.Mappers;

import static com.samb.trs.Resources.Constants.Camera.START_VELOCITY;

public class RowingSystem extends IteratingSystem {
    public static final float ROWING_EPS = 5f;

    private Array<Entity> entities;
    private Timer animationTimer;
    private MainController mainController;


    public RowingSystem(MainController mainController) {
        super(Family.all(RowingComponent.class).get());
        this.entities = new Array<>();
        this.animationTimer = new Timer();
        this.mainController = mainController;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : entities) {
            RowingComponent rc = Mappers.rowing.get(entity);
            updateFrequency(rc);
            if (Mappers.transform.has(entity))
                rc.posX.add(Mappers.transform.get(entity).position.x);
            if (rc.posX.size > 2)
                rc.posX.removeIndex(0);
            Entity paddle = rc.paddle;
            Entity man = rc.man;
            TransformComponent paddleTc = Mappers.transform.get(paddle);
            TransformComponent manTc = Mappers.transform.get(man);

            animationTimer.update(deltaTime);

            setPaddleDirection(rc, paddleTc.rotation, ROWING_EPS);

            RowingComponent.PaddleDirection direction = rc.direction;

            if (direction == RowingComponent.PaddleDirection.RIGHT)
                paddle(paddleTc, manTc, rc.frequency, Interpolation.slowFast, Interpolation.fastSlow, -45, 45);
            else if (direction == RowingComponent.PaddleDirection.LEFT)
                paddle(paddleTc, manTc, rc.frequency, Interpolation.fastSlow, Interpolation.slowFast, -45, 45);
            else
                paddle(paddleTc, manTc, rc.frequency, Interpolation.slowFast, Interpolation.slowFast, -45, 45);

            //kanuPaddel.setRotation(body.getAngle()*MathUtils.radDeg);
            Body body = Mappers.body.get(paddle).body;
            body.setTransform(body.getPosition(), paddleTc.rotation * MathUtils.degRad);
            //body.setTransform(center.x / PPM, (center.y - RenderController.cph() * 5) / PPM, body.getAngle());

            if (rc.paddleSoundActive && inRangeOf(paddleTc.rotation, 45, 10f)) {
                mainController.getSoundController().queueSound(paddle, Sounds.PADDLE);
                rc.paddleSoundActive = false;
            } else if (rc.paddleSoundActive && inRangeOf(paddleTc.rotation, -45, 10f)) {
                mainController.getSoundController().queueSound(paddle, Sounds.PADDLE);
                rc.paddleSoundActive = false;
            } else if (inRangeOf(paddleTc.rotation, 0, 10f)) {
                rc.paddleSoundActive = true;
            }
        }

        entities.clear();
    }

    private void updateFrequency(RowingComponent rc) {
        rc.frequency = (1f - 0.4f * (getEngine().getSystem(CameraSystem.class).getVelocity() / START_VELOCITY - 1.0f));
    }

    public void update(float dt, RowingComponent.PaddleDirection direction, Entity paddle, Entity man, Vector2 center) {

        // moveTo(center);
    }

//    public void moveTo(Vector2 center) {
//        kanuPaddel.setCenter(center.x, center.y - RenderController.cph() * 5);
//        kanuPaddel.setOriginCenter();
//
//        kanuMann.setCenter(center.x, center.y - RenderController.cph() * 10);
//        kanuMann.setOriginCenter();
//    }

    private void setPaddleDirection(RowingComponent rc, float paddleRotation, float eps) {
        float err = rc.posX.get(rc.posX.size - 1) - rc.posX.first();
        if (err > eps) {
            setDirection(rc, paddleRotation, RowingComponent.PaddleDirection.RIGHT);
        } else if (err < -eps) {
            setDirection(rc, paddleRotation, RowingComponent.PaddleDirection.LEFT);
        } else {
            setDirection(rc, paddleRotation, RowingComponent.PaddleDirection.MIDDLE);
        }
    }

    private void paddle(TransformComponent paddleTc, TransformComponent manTc, float frequency, Interpolation in, Interpolation out, float deg1, float deg2) {
        float b = 0;
        float a = 0;
        float rotMann = 0.2f;

        if (animationTimer.passedAbsolute(frequency)) {
            animationTimer.reset();
        } else if (animationTimer.passedAbsolute(frequency / 2f)) {
            b = (animationTimer.getValue() - frequency / 2f) / (frequency / 2f);
            a = out.apply(b);
            //applyAngularImpulse((1-a)*deg1 + a*deg2);
            paddleTc.rotation = (1 - a) * deg1 + a * deg2;
            manTc.rotation = (1 - a) * deg1 * rotMann + a * deg2 * rotMann;
        } else {
            b = animationTimer.getValue() / (frequency / 2f);
            a = in.apply(b);
            //applyAngularImpulse(a*deg1+(1-a)*deg2);
            paddleTc.rotation = (a * deg1 + (1 - a) * deg2);
            manTc.rotation = (a * deg1 * rotMann + (1 - a) * deg2 * rotMann);
        }
    }

    private boolean inRangeOf(float deg1, float deg2, float range) {
        return Math.abs(deg1 - deg2) < range;
    }

    private float getInverseTime(Interpolation in, Interpolation out, float deg, float deg1, float deg2, float frequency) {
        float t;
        float a;

        if (animationTimer.getValue() >= frequency / 2f) {
            a = Math.abs((deg - deg1) / (deg2 - deg1));
            t = (frequency / 2f) * (1 + com.samb.trs.Utilities.Interpolation.inverse(out, a));

        } else {
            a = Math.abs((deg - deg2) / (deg1 - deg2));
            t = (frequency / 2f) * com.samb.trs.Utilities.Interpolation.inverse(in, a);
        }

        return t;
    }

    // TODO: use function for smooth transition
    public void setDirection(RowingComponent rc, float paddleRotation, RowingComponent.PaddleDirection direction) {
        if (rc.direction != direction) {
            Interpolation in = Interpolation.slowFast;
            Interpolation out = Interpolation.slowFast;
            switch (direction) {
                case RIGHT:
                    in = Interpolation.slowFast;
                    out = Interpolation.fastSlow;
                    break;
                case LEFT:
                    in = Interpolation.fastSlow;
                    out = Interpolation.slowFast;
                    break;
            }

            animationTimer.set(getInverseTime(in, out, paddleRotation, -45, 45, rc.frequency));
        }
        rc.direction = direction;
    }
}
