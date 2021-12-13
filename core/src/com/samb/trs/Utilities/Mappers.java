package com.samb.trs.Utilities;

import com.badlogic.ashley.core.ComponentMapper;
import com.samb.trs.Components.*;

public class Mappers {
    public static ComponentMapper<BodyComponent> body = ComponentMapper.getFor(BodyComponent.class);
    public static ComponentMapper<CheckOutsideComponent> checkOutside = ComponentMapper.getFor(CheckOutsideComponent.class);
    public static ComponentMapper<CollectComponent> collect = ComponentMapper.getFor(CollectComponent.class);
    public static ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);
    public static ComponentMapper<DeathComponent> death = ComponentMapper.getFor(DeathComponent.class);
    public static ComponentMapper<MouseComponent> mouse = ComponentMapper.getFor(MouseComponent.class);
    public static ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static ComponentMapper<RowingComponent> rowing = ComponentMapper.getFor(RowingComponent.class);
    public static ComponentMapper<ShieldComponent> shield = ComponentMapper.getFor(ShieldComponent.class);
    public static ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static ComponentMapper<TypeComponent> type = ComponentMapper.getFor(TypeComponent.class);
    public static ComponentMapper<AttachedComponent> attached = ComponentMapper.getFor(AttachedComponent.class);
    public static ComponentMapper<QuadrantComponent> quadrant = ComponentMapper.getFor(QuadrantComponent.class);
    public static ComponentMapper<SteeringComponent> steering = ComponentMapper.getFor(SteeringComponent.class);
    public static ComponentMapper<FollowComponent> follow = ComponentMapper.getFor(FollowComponent.class);
    public static ComponentMapper<ParticleEffectComponent> peCom = ComponentMapper.getFor(ParticleEffectComponent.class);
}