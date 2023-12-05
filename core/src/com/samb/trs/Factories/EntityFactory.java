package com.samb.trs.Factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Components.*;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.Particles;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.Utilities.Mappers;

import static com.samb.trs.Resources.Constants.Rendering.*;

public class EntityFactory {
    private static final float frequencyHz = 2f;
    private static EntityFactory entityFactory;
    private BodyFactory bodyFactory;
    private PooledEngine engine;
    private MainController mainController;

    private EntityFactory(MainController mainController, PooledEngine engine, World world) {
        this.mainController = mainController;
        this.engine = engine;
        bodyFactory = BodyFactory.getInstance(mainController, world);
    }

    public static EntityFactory getInstance(MainController mainController, PooledEngine engine, World world) {
        if (entityFactory == null)
            entityFactory = new EntityFactory(mainController, engine, world);
        return entityFactory;
    }

    public static EntityFactory getInstance() {
        if (entityFactory == null){
            Gdx.app.error("FactoryError", "Instance not initialized yet");
            throw new GdxRuntimeException("InternalÉrror");
        }

        return entityFactory;

    }

    // METHODS FOR CREATING INGAME ENTITIES

    public static void copyBodyProperties(Entity from, Entity to) {
        if (Mappers.body.has(from) && Mappers.body.has(to)) {
            BodyComponent bc_from = Mappers.body.get(from);
            BodyComponent bc_to = Mappers.body.get(to);

            bc_to.body.setLinearVelocity(bc_from.linear_vel);
            bc_to.body.setLinearDamping(bc_from.linear_damp);
            bc_to.body.setAngularDamping(bc_from.angular_damp);
            bc_to.body.setAngularVelocity(bc_from.angular_vel);
        }
    }

    public static void copyVelocity(Entity from, Entity to) {
        if (Mappers.body.has(from) && Mappers.body.has(to)) {
            BodyComponent bc_from = Mappers.body.get(from);
            BodyComponent bc_to = Mappers.body.get(to);

            bc_to.body.setLinearVelocity(bc_from.linear_vel);
            bc_to.body.setAngularVelocity(bc_from.angular_vel);
        }
    }

    public static float compare(Entity entity1, Entity entity2) {
        if (Mappers.texture.has(entity1) && Mappers.texture.has(entity2)) {
            TextureComponent tc1 = Mappers.texture.get(entity1);
            TextureComponent tc2 = Mappers.texture.get(entity2);
            return tc1.width * tc1.height - tc2.width * tc2.height;
        } else return 0;
    }

    public Entity makePlayerEntity(float x, float y) {
        Entity player = makeBodyEntity(TypeComponent.BOAT, TextureRegions.KANU_RUMPF, x, y, 1, 80, 400);
        makeMovableEntity(player, 10000.0f * Mappers.body.get(player).body.getMass(), frequencyHz);
        makeShieldComponent(player);
        makeRowingComponent(player);
        makeSteeringEntity(player);

        Mappers.transform.get(player).isHidden = true;
        entityFactory.makeAttachedParticleEffect(Particles.WATER, player, 0, 0, 0, -1, -1, 0);

        return player.add(engine.createComponent(DeathComponent.class))
                .add(engine.createComponent(CollisionComponent.class))
                .add(engine.createComponent(PlayerComponent.class));
    }

    public Entity makeCoinEntity(float x, float y, Viewport viewport) {
        Entity coin = makeBodyEntity(TypeComponent.COIN, TextureRegions.COIN, x, y, 1, 0.25f, 0.25f);
        makeSteeringEntity(coin);
        return makeCheckOutsideEntity(coin, viewport).add(engine.createComponent(CollectComponent.class))
                .add(engine.createComponent(DeathComponent.class))
                .add(engine.createComponent(CollisionComponent.class));
    }

    public Entity makeBoostEntity(float x, float y, Viewport viewport) {
        Entity boost = makeBodyEntity(TypeComponent.BOOST, TextureRegions.BOOST, x, y, 1, 0.25f, 0.25f);
        makeSteeringEntity(boost);
        return makeCheckOutsideEntity(boost, viewport).add(engine.createComponent(CollectComponent.class))
                .add(engine.createComponent(DeathComponent.class))
                .add(engine.createComponent(CollisionComponent.class));
    }

    public Entity makeRockEntity(float x, float y, TextureRegions region, Viewport viewport) {
        Entity rock = makeBodyEntity(TypeComponent.ROCK, region, x, y, 1, 1.4f, 1.4f);
        makeSteeringEntity(rock);
        return makeCheckOutsideEntity(rock, viewport).add(engine.createComponent(CollisionComponent.class)).add(engine.createComponent(DeathComponent.class));
    }

    public Entity makeFishEntity(float x, float y, Viewport viewport) {
        Entity fish = makeBodyEntity(TypeComponent.FISH, TextureRegions.FISH2, x, y, 1, 0.94f, 1.11f);
        makeSteeringEntity(fish, null, SteeringComponent.SteeringState.NONE, MathUtils.PI);
        return makeCheckOutsideEntity(fish, viewport).add(engine.createComponent(CollisionComponent.class)).add(engine.createComponent(DeathComponent.class));
    }

    public Entity makeTrunkEntity(float x, float y, Viewport viewport) {
        Entity trunk = makeBodyEntity(TypeComponent.TRUNK, TextureRegions.TRUNK, x, y, 1, 0.83f, 2.5f);
        makeSteeringEntity(trunk);
        return makeCheckOutsideEntity(trunk, viewport).add(engine.createComponent(CollisionComponent.class)).add(engine.createComponent(DeathComponent.class));
    }

    public Entity makeLeftShoreEntity(float offsetY) {
        return makeBodyEntity(TypeComponent.SHORE, TextureRegions.UFER_LINKS, -WorldWidth / 2f + (410 / 4092f) * WorldHeight / 2f, offsetY, 2, (410 / 4092f) * WorldHeight, WorldHeight)
                .add(engine.createComponent(CollisionComponent.class));
    }

    // HELPING METHODS

    public Entity makeRightShoreEntity(float offsetY) {
        return makeBodyEntity(TypeComponent.SHORE, TextureRegions.UFER_RECHTS, WorldWidth / 2f - (410 / 4092f) * WorldHeight / 2f, offsetY, 2, (410 / 4092f) * WorldHeight, WorldHeight)
                .add(engine.createComponent(CollisionComponent.class));
    }

    public Entity makeBackground(Entity cameraEntity) {
        Entity entity = entityFactory.makeSprite(TextureRegions.HINTERGRUND, 0, 0, -1, WorldWidth, WorldHeight, false);
        AttachedComponent ac = engine.createComponent(AttachedComponent.class);
        ac.attachedTo = cameraEntity;

        return entity.add(ac);
    }

    public Entity makeCameraEntity(OrthographicCamera camera) {
        Entity entity = engine.createEntity();
        CameraComponent cc = engine.createComponent(CameraComponent.class);
        cc.camera = camera;
        BodyComponent bc = engine.createComponent(BodyComponent.class);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bc.body = bodyFactory.getWorld().createBody(bodyDef);

        return entity.add(cc).add(bc);
    }

    public Entity makeSteeringEntity(Entity entity) {
        return makeSteeringEntity(entity, null, SteeringComponent.SteeringState.NONE, 0);
    }

    public Entity makeFollowEntity(Entity entity, float maxFollowTime) {
        FollowComponent fc = engine.createComponent(FollowComponent.class);
        fc.maxFollowTime = maxFollowTime;
        return entity.add(fc);
    }

    public Entity makeSteeringEntity(Entity entity, SteeringBehavior<Vector2> steeringBehavior, SteeringComponent.SteeringState state, float orientationOffset) {
        SteeringComponent stc = engine.createComponent(SteeringComponent.class);
        stc.body = Mappers.body.get(entity).body;
        stc.orientationOffset = orientationOffset;
        stc.currentMode = state;
        stc.steeringBehavior = steeringBehavior;

        if (Mappers.texture.has(entity)){
            TextureComponent tc = Mappers.texture.get(entity);
            stc.boundingRadius = 0.5f * RenderController.s2w(Math.max(tc.width, tc.height));
        }

        return entity.add(stc);
    }

    public Entity makeBodyEntity(short type, TextureRegions region, float x, float y, float z, float width, float height) {
        Entity bodyEntity = makeSprite(region, x, y, z, width, height, true);

        BodyComponent bc = engine.createComponent(BodyComponent.class);
        bc.body = bodyFactory.makeBody(region, x, y, width, height);
        bc.body.setUserData(bodyEntity);
        bc.linear_vel.set(bc.body.getLinearVelocity());
        bc.linear_damp = bc.body.getLinearDamping();
        bc.angular_vel = bc.body.getAngularVelocity();
        bc.angular_damp = bc.body.getAngularDamping();
        bodyEntity.add(bc);

        TypeComponent tc = engine.createComponent(TypeComponent.class);
        tc.type = type;
        bodyEntity.add(tc);

        return bodyEntity;
    }

    public Entity makeQuadrantEntity(Entity cameraEntity, float x, float y, float width, float height) {
        Entity entity = engine.createEntity();

        BodyComponent bc = engine.createComponent(BodyComponent.class);
        bc.body = bodyFactory.makeQuadrantBody(x, y, width, height);

        AttachedComponent ac = engine.createComponent(AttachedComponent.class);
        ac.attachedTo = cameraEntity;
        ac.offset.set(x, y);

        QuadrantComponent qc = engine.createComponent(QuadrantComponent.class);
        qc.width = width;
        qc.height = height;

        return entity.add(bc).add(ac).add(qc);
    }

    public Entity makeCheckOutsideEntity(Entity entity, Viewport viewport, float offsetX1, float offsetX2, float offsetY1, float offsetY2) {
        CheckOutsideComponent coc = engine.createComponent(CheckOutsideComponent.class);
        coc.viewport = viewport;
        coc.offsetX.set(offsetX1, offsetX2);
        coc.offsetY.set(offsetY1, offsetY2);
        return entity.add(coc);
    }

    public Entity makeMovableEntity(Entity entity, float maxForce, float frequencyHz) {
        if (Mappers.body.has(entity)) {
            BodyComponent bc = Mappers.body.get(entity);
            MouseComponent mc = engine.createComponent(MouseComponent.class);
            mc.mouseJoint = defineMouseJoint(bc.body, maxForce, frequencyHz);
            mc.world = bc.body.getWorld();

            entity.add(mc);
        }

        return entity;
    }

    public Entity makeAttachedParticleEffect(Particles p, Entity attached) {
        return makeAttachedParticleEffect(p, attached, 0, 0);
    }

    public Entity makeAttachedParticleEffect(Particles p, Entity attached, float xo, float yo) {
        return makeAttachedParticleEffect(p, attached, xo, yo, 0, 0.5f);
    }

    public Entity makeAttachedParticleEffect(Particles p, Entity attached, float xo, float yo, float z, float timeTilDeath) {
        return makeAttachedParticleEffect(p, attached, xo, yo, z, -1, -1, timeTilDeath);
    }

    public Entity makeAttachedParticleEffect(Particles p, Entity attached, float xo, float yo, float z, float width, float height, float timeTilDeath) {
        Entity entity = engine.createEntity();
        ParticleEffectComponent pec = engine.createComponent(ParticleEffectComponent.class);
        pec.particleEffect = mainController.getAssetController().getParticleEffectController().getPooledParticleEffect(p);
        pec.timeTilDeath = timeTilDeath;

        AttachedComponent ac = engine.createComponent(AttachedComponent.class);
        ac.attachedTo = attached;
        ac.offset.x = xo;
        ac.offset.y = yo;

        TransformComponent tc = engine.createComponent(TransformComponent.class);
        tc.z = z;

        if (width > 0 && height > 0) {
            for (ParticleEmitter emitter : pec.particleEffect.getEmitters()) {
                emitter.getSpawnWidth().setLow(width);
                emitter.getSpawnWidth().setHigh(width);
                emitter.getSpawnHeight().setLow(height);
                emitter.getSpawnHeight().setHigh(height);
            }
        }

        pec.particleEffect.start();

        engine.addEntity(entity.add(pec).add(ac).add(tc));
        return entity;
    }

    public Entity makeParticleEffect(Particles p, float x, float y) {
        Entity entity = engine.createEntity();
        ParticleEffectComponent pec = engine.createComponent(ParticleEffectComponent.class);
        pec.particleEffect = mainController.getAssetController().getParticleEffectController().getPooledParticleEffect(p);
        pec.particleEffect.setPosition(x, y);

        TransformComponent tc = engine.createComponent(TransformComponent.class);

        engine.addEntity(entity.add(pec).add(tc));
        return entity;
    }

    public Entity makeMovableEntity(Entity entity, Body bodyA, float maxForce, float frequencyHz) {
        if (Mappers.body.has(entity)) {
            BodyComponent bc = Mappers.body.get(entity);
            MouseComponent mc = engine.createComponent(MouseComponent.class);
            mc.mouseJoint = defineMouseJoint(bc.body, bodyA, maxForce, frequencyHz);
            mc.world = bc.body.getWorld();

            entity.add(mc);
        }

        return entity;
    }

    public Entity makeCheckOutsideEntity(Entity entity, Viewport viewport) {
        return makeCheckOutsideEntity(entity, viewport, 0, 0, 0, 2 * WorldHeight);
    }

    public Entity makeShieldComponent(Entity entity) {
        if (Mappers.body.has(entity)) {
            ShieldComponent sc = engine.createComponent(ShieldComponent.class);
            sc.duration = 1.8f;

            // Create new Shield Body
            BodyComponent bc = Mappers.body.get(entity);
            sc.shieldEntity = makeBodyEntity(TypeComponent.SHIELD, TextureRegions.SHIELD_BACKGROUND, bc.body.getPosition().x, bc.body.getPosition().y, 2, 2.5f, 2.5f);
            Mappers.transform.get(sc.shieldEntity).z = 3;

            sc.gridEntity = makeSprite(TextureRegions.SHIELD_GITTER, bc.body.getPosition().x, bc.body.getPosition().y, 3, 2.5f, 2.5f, true);

            // Add Entity as attached Component to the shield
            AttachedComponent ac1 = engine.createComponent(AttachedComponent.class);
            AttachedComponent ac2 = engine.createComponent(AttachedComponent.class);

            ac1.attachedTo = entity;

            makeMovableEntity(sc.shieldEntity, 10000.0f * Mappers.body.get(sc.shieldEntity).body.getMass(), frequencyHz);

            ac2.attachedTo = entity;

            sc.shieldEntity.add(ac1);
            sc.gridEntity.add(ac2);

            Mappers.transform.get(sc.shieldEntity).isHidden = true;
            Mappers.transform.get(sc.gridEntity).isHidden = true;


            engine.addEntity(sc.shieldEntity);
            engine.addEntity(sc.gridEntity);

            return entity.add(sc);
        }

        return entity;
    }

    private TransformComponent makeTransformComponent(float x, float y, float z, float rotation, boolean isHidden) {
        TransformComponent trc = engine.createComponent(TransformComponent.class);
        trc.position.set(x, y);
        trc.rotation = rotation;
        trc.z = z;
        trc.isHidden = isHidden;
        return trc;
    }

    private MouseJoint defineMouseJoint(Body body, float maxForce, float frequencyHz) {
        MouseJointDef def = new MouseJointDef();
        def.bodyA = findBoundsBody(body);
        def.bodyB = body;
        def.collideConnected = true;
        def.target.set(body.getPosition());
        def.maxForce = maxForce;
        def.frequencyHz = frequencyHz;

        return (MouseJoint) body.getWorld().createJoint(def);
    }

    private MouseJoint defineMouseJoint(Body body, Body bodyA, float maxForce, float frequencyHz) {
        MouseJointDef def = new MouseJointDef();
        def.bodyA = bodyA;
        def.bodyB = body;
        def.collideConnected = true;
        def.target.set(body.getPosition());
        def.maxForce = maxForce;
        def.frequencyHz = frequencyHz;

        return (MouseJoint) body.getWorld().createJoint(def);
    }

    private Body findBoundsBody(Body body) {
        Array<Body> bodies = new Array<>();
        body.getWorld().getBodies(bodies);
        Body bd = bodies.random();
        for (Body b : bodies) {
            if (b != body) {
                bd = b;
                break;
            }
        }
        return bd;
    }

    public Entity makeRowingComponent(Entity entity) {
        RowingComponent rc = engine.createComponent(RowingComponent.class);
        BodyComponent bc = Mappers.body.get(entity);
        rc.paddle = makeBodyEntity(TypeComponent.PADDEL, TextureRegions.KANU_PADDEL, bc.body.getPosition().x, bc.body.getPosition().y, 3, 225, 23);
        rc.man = makeSprite(TextureRegions.KANU_MANN, bc.body.getPosition().x, bc.body.getPosition().y, 2, 41, 73, true);
        rc.frequency = 1f;

        Mappers.transform.get(rc.paddle).isHidden = true;
        Mappers.transform.get(rc.man).isHidden = true;

        AttachedComponent ac1 = engine.createComponent(AttachedComponent.class);
        AttachedComponent ac2 = engine.createComponent(AttachedComponent.class);

        ac1.attachedTo = entity;

        makeMovableEntity(rc.paddle, 10000.0f * Mappers.body.get(rc.paddle).body.getMass(), frequencyHz);
        Mappers.mouse.get(rc.paddle).offset.set(0, -RenderController.w2h(1.0f) * 5);

        ac2.attachedTo = entity;
        ac2.offset.set(0, -RenderController.w2h(1.0f) * 10);

        rc.paddle.add(ac1);
        rc.man.add(ac2);

        engine.addEntity(rc.paddle);
        engine.addEntity(rc.man);

        return entity.add(rc);
    }

    public Entity makeSprite(TextureRegions region, float x, float y, float z, float width, float height, boolean hasShadow) {
        Entity entity = engine.createEntity();
        entity.add(makeTextureComponent(region, width, height, hasShadow));
        return entity.add(makeTransformComponent(x, y, z, 0, false));
    }

    private TextureComponent makeTextureComponent(TextureRegions region, float width, float height, boolean hasShadow) {
        TextureComponent tc = engine.createComponent(TextureComponent.class);
        tc.textureRegion = mainController.getAssetController().getAsset(region);
        tc.region = region;
        tc.width = width;
        tc.height = height;
        tc.hasShadow = hasShadow;
        return tc;
    }

    public void resize(Entity entity, float width, float height) {
        if (Mappers.body.has(entity) && Mappers.texture.has(entity)) {
            BodyComponent bc = Mappers.body.get(entity);
            TextureComponent tex = Mappers.texture.get(entity);
            TransformComponent trf = Mappers.transform.get(entity);

            tex.width = width;
            tex.height = height;

            if (Mappers.steering.has(entity)) {
                Mappers.steering.get(entity).boundingRadius = 0.5f * RenderController.s2w(Math.max(width, height));

            }

            World world = bc.body.getWorld();

            world.destroyBody(bc.body);
            bc.body = bodyFactory.makeBody(tex.region, trf.position.x, trf.position.y, width, height);
            bc.body.setTransform(trf.position.x, trf.position.y, trf.rotation * MathUtils.degRad);
            bc.body.setUserData(entity);

            bc.body.setLinearVelocity(bc.linear_vel);
            bc.body.setLinearDamping(bc.linear_damp);
            bc.body.setAngularDamping(bc.angular_damp);
            bc.body.setAngularVelocity(bc.angular_vel);
        }
    }
}
