package com.samb.trs.Pools;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Sprites.Rock;
import com.samb.trs.Resources.TextureRegions;

public class RockPool extends AbstractPool<Rock> {

    private MainController mainController;
    private World world;

    public RockPool(MainController mainController, World world, Viewport viewport) {
        super(30,250, 250, viewport);
        this.mainController = mainController;
        this.world = world;
        init(30);
    }

    @Override
    protected Rock newObject() {
        float rand = MathUtils.random();
        if(rand <= 0.5f) {
            return new Rock(mainController, TextureRegions.ROCK, world, viewport, 0, 0, width, height);
        } else return new Rock(mainController, TextureRegions.ROCK2, world, viewport, 0, 0, width, height);
    }
}
