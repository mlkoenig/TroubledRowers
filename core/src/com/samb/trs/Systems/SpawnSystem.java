package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Components.BodyComponent;
import com.samb.trs.Components.QuadrantComponent;
import com.samb.trs.Components.SteeringComponent;
import com.samb.trs.Components.TextureComponent;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.Spawning.Patterns.Pattern;
import com.samb.trs.Controllers.Spawning.Patterns.Patterns;
import com.samb.trs.Controllers.TimeController;
import com.samb.trs.Factories.EntityFactory;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.Particles;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.Box2dAi.CustomPrioritySteering;
import com.samb.trs.Utilities.Box2dAi.SteeringPresets;
import com.samb.trs.Utilities.Mappers;

import static com.badlogic.gdx.math.MathUtils.degRad;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.samb.trs.Resources.Constants.Rendering.WorldHeight;
import static com.samb.trs.Resources.Constants.Rendering.WorldWidth;
import static com.samb.trs.Resources.Constants.*;

public class SpawnSystem extends EntitySystem implements Disposable {
    private Timer spawnTimer, fishTimer, coinTimer, boostTimer, trunkTimer;
    private TimeController timeController;
    private Patterns patterns;

    private EntityFactory entityFactory;
    private PooledEngine engine;
    private World world;
    private MainController mainController;
    private Array<Entity> quadrants;
    private Viewport viewport;
    private Entity player;

    public SpawnSystem(MainController mainController, PooledEngine engine, World world, Viewport viewport) {
        this.mainController = mainController;
        this.world = world;
        this.viewport = viewport;
        this.engine = engine;
        this.entityFactory = EntityFactory.getInstance(mainController, engine, world);

        this.timeController = new TimeController();
        this.spawnTimer = timeController.newTimer();
        this.fishTimer = timeController.newTimer();
        this.coinTimer = timeController.newTimer();
        this.boostTimer = timeController.newTimer();
        this.trunkTimer = timeController.newTimer();

        this.quadrants = new Array<>();

        this.patterns = new Patterns();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeController.update(deltaTime);

        // Check if it is time to spawn a new pattern
        float velocity = engine.getSystem(CameraSystem.class).getVelocity();
        if (spawnTimer.passed(Spawning.SPAWN_RATE / velocity)) {
            boolean bc = coinTimer.passedAbsolute(Spawning.COIN_SPAWN_TIMER / velocity);
            boolean bf = fishTimer.passedAbsolute(Spawning.FISH_SPAWN_TIMER / velocity);
            boolean bb = boostTimer.passedAbsolute(Spawning.BOOST_SPAWN_TIMER / velocity);
            boolean bt = trunkTimer.passedAbsolute(Spawning.TRUNK_SPAWN_TIMER / velocity);

            Pattern.Type type = Pattern.Type.Rock;
            float random = random();

            if (bb && random <= Spawning.BOOST_PROBABILITY) {
                boostTimer.reset();
                type = Pattern.Type.Boost;
            } else if (bc && random <= Spawning.COIN_PROBABILITY) {
                coinTimer.reset();
                type = Pattern.Type.Coin;
            } else if (bt && random <= Spawning.TRUNK_PROBABILITY) {
                trunkTimer.reset();
                type = Pattern.Type.Trunk;
            } else if (bf && random <= Spawning.FISH_PROBABILITY) {
                fishTimer.reset();
                type = Pattern.Type.BasicFish;
            }

            spawnPattern(patterns.getRandom(type));
        }
    }

    public void initializeQuadrants(Entity cameraEntity) {
        int vertical = Spawning.QUADRANT_DEPTH_VERTICAL;
        int horizontal = Spawning.QUADRANT_DEPTH_HORIZONTAL;

        quadrants = new Array<>();
        for (int i = 0; i < vertical; i++) {
            for (int j = 0; j < horizontal; j++) {
                float x = 230;
                float quad_width = (WorldWidth - 2 * x) / (float) (horizontal);
                Entity quadrant = entityFactory.makeQuadrantEntity(cameraEntity, -WorldWidth / 2f + x + (j + 0.5f) * quad_width, 200 + WorldHeight / 2f + i * quad_width, quad_width, quad_width);
                engine.addEntity(quadrant);
                quadrants.add(quadrant);
            }
        }
    }

    /**
     * Spawn pattern and a coin with given probability
     *
     * @param arr The pattern entries
     */
    public void spawnPattern(Array<Pattern> arr) {
        // Spawn Pattern
        for (Pattern po : arr) {
            spawnInQuadrant(quadrants.get(po.getIndex()), po.getType());
        }
    }

    /**
     * Spawns a rock in the given quadrant if the quadrant is not already overlapped by another rock.
     *
     * @param q The quadrant for the spawn range.
     */
    public void spawnInQuadrant(Entity q, Pattern.Type type) {
        switch (type) {
            case Rock:
                spawnRockInQuadrant(q);
                break;
            case Coin:
                spawnCoinInQuadrant(q);
                break;
            case Boost:
                spawnBoostInQuadrant(q);
                break;
            case BasicFish:
                spawnFishInQuadrant(q);
                break;
            case Trunk:
                spawnTrunkInQuadrant(q);
                break;
        }
    }

    private void spawnFishInQuadrant(Entity q) {
        BodyComponent bc = Mappers.body.get(q);
        com.samb.trs.Components.QuadrantComponent qc = Mappers.quadrant.get(q);

        float qx = bc.body.getPosition().x * Rendering.PPM;
        float qy = bc.body.getPosition().y * Rendering.PPM;

        float x = random(qx - qc.width / 2f + 100, qx + qc.width / 2f - 100);
        float y = random(qy - qc.height / 2f + 80, qy + qc.height / 2f - 80);

        Entity fish = entityFactory.makeFishEntity(x, y, viewport);

        SteeringComponent stc = Mappers.steering.get(fish);
        stc.currentMode = SteeringComponent.SteeringState.WANDER;
        CustomPrioritySteering<Vector2> steering;
        if (player != null && Mappers.steering.has(player)) {
            steering = new CustomPrioritySteering<>(stc, Mappers.steering.get(player));
            steering.add(SteeringPresets.getPursue(stc, steering.getPlayer()).setEnabled(false));
        } else {
            steering = new CustomPrioritySteering<>(stc, null);
        }
        steering.setEpsilon(0.0001f);
        steering.add(SteeringPresets.getCollisionAvoidance(stc, world, 3));
        //steering.add(SteeringPresets.getRayCastObstacleAvoidance(stc, world, 2));
        steering.add(SteeringPresets.getWander(stc));
        stc.steeringBehavior = steering;
        stc.setMaxLinearSpeed(13);
        stc.setMaxLinearAcceleration(10);
        stc.setMaxAngularAcceleration(20);
        stc.setMaxAngularSpeed(20);
        entityFactory.makeFollowEntity(fish, 2.6f);

        engine.addEntity(fish);
        entityFactory.makeAttachedParticleEffect(Particles.ROCK_WATER, fish, 0, 0, 0, 40, 40, 0);
    }

    private void spawnCoinInQuadrant(Entity q) {
        // Coin position
        BodyComponent bc = Mappers.body.get(q);
        com.samb.trs.Components.QuadrantComponent qc = Mappers.quadrant.get(q);

        float qx = bc.body.getPosition().x * Rendering.PPM;
        float qy = bc.body.getPosition().y * Rendering.PPM;

        float x = random(qx - qc.width / 2f + 25, qx + qc.width / 2f - 25);
        float y = random(qy - qc.height / 2f + 25, qy + qc.height / 2f - 25);

        Entity coin = entityFactory.makeCoinEntity(x, y, viewport);
        engine.addEntity(coin);

        TextureComponent tc = Mappers.texture.get(coin);
        entityFactory.makeAttachedParticleEffect(Particles.ROCK_WATER, coin, 0, 0, 0, tc.width - 10, tc.height - 10, 0);
    }

    private void spawnBoostInQuadrant(Entity q) {
        BodyComponent bc = Mappers.body.get(q);
        com.samb.trs.Components.QuadrantComponent qc = Mappers.quadrant.get(q);

        float qx = bc.body.getPosition().x * Rendering.PPM;
        float qy = bc.body.getPosition().y * Rendering.PPM;

        // Coin position
        float x = random(qx - qc.width / 2f + 25, qx + qc.width / 2f - 25);
        float y = random(qy - qc.height / 2f + 25, qy + qc.height / 2f - 25);

        Entity boost = entityFactory.makeBoostEntity(x, y, viewport);
        engine.addEntity(boost);

        TextureComponent tc = Mappers.texture.get(boost);
        entityFactory.makeAttachedParticleEffect(Particles.ROCK_WATER, boost, 0, 0, 0, tc.width - 10, tc.height - 10, 0);
    }

    private void spawnRockInQuadrant(Entity q) {
        BodyComponent bc = Mappers.body.get(q);
        QuadrantComponent qc = Mappers.quadrant.get(q);

        float qx = bc.body.getPosition().x * Rendering.PPM;
        float qy = bc.body.getPosition().y * Rendering.PPM;

        // Rock position
        float x = random(qx - qc.width / 2f + 125, qx + qc.width / 2f - 125);
        float y = random(qy - qc.height / 2f + 125, qy + qc.height / 2f - 125);

        int type = MathUtils.random(1);
        Entity rock;
        if (type == 0) rock = entityFactory.makeRockEntity(x, y, TextureRegions.ROCK, viewport);
        else rock = entityFactory.makeRockEntity(x, y, TextureRegions.ROCK2, viewport);

        int rand = MathUtils.random(3);
        TextureComponent tc = Mappers.texture.get(rock);
        float width = tc.width;
        float height = tc.height;

        for (int i = 0; i < rand; i++) {
            width *= 0.8f;
            height *= 0.8f;
        }

        entityFactory.resize(rock, width, height);

        entityFactory.makeAttachedParticleEffect(Particles.ROCK_WATER, rock, 0f, 0f, 0, tc.width - 10, tc.height - 10, 0);

        // Rotate rock randomly
        // float deg = random(-40f, 60f);
        float deg = random(0.0f, 360.0f);
        BodyComponent bodyComponent = Mappers.body.get(rock);
        bodyComponent.body.setTransform(bodyComponent.body.getPosition().x, bodyComponent.body.getPosition().y, deg * MathUtils.degRad);

        engine.addEntity(rock);
    }

    private void spawnTrunkInQuadrant(Entity q) {
        BodyComponent bc = Mappers.body.get(q);
        QuadrantComponent qc = Mappers.quadrant.get(q);

        float qx = bc.body.getPosition().x * Rendering.PPM;
        float qy = bc.body.getPosition().y * Rendering.PPM;

        // Rock position
        float x = random(qx - qc.width / 2f + 150, qx + qc.width / 2f - 150);
        float y = random(qy - qc.height / 2f + 150, qy + qc.height / 2f - 150);

        Entity trunk = entityFactory.makeTrunkEntity(x, y, viewport);
        BodyComponent bodyComponent = Mappers.body.get(trunk);

        float deg = random(40, 160);
        bodyComponent.body.setTransform(bodyComponent.body.getPosition().x, bodyComponent.body.getPosition().y, deg * degRad);

        engine.addEntity(trunk);

    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public TimeController getTimeController() {
        return timeController;
    }

    @Override
    public void dispose() {
        timeController.clear();
    }
}
