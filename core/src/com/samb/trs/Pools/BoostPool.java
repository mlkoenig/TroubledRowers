package com.samb.trs.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Sprites.Boost;
import com.samb.trs.Resources.TextureRegions;

public class BoostPool extends AbstractPool<Boost> {
    private MainController mainController;
    private World world;

    public BoostPool(MainController mainController, World world, Viewport viewport) {
        super(20,50, 50, viewport);
        this.world = world;
        this.mainController = mainController;
        init(20);
    }

    @Override
    protected Boost newObject() {
        return new Boost(mainController, TextureRegions.BOOST, world, viewport, 0, 0, 25);
    }
}
