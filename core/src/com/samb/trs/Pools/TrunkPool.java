package com.samb.trs.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Sprites.Trunk;
import com.samb.trs.Resources.TextureRegions;

public class TrunkPool extends AbstractPool<Trunk> {
    private MainController mainController;
    private World world;

    public TrunkPool(MainController mainController, World world, Viewport viewport) {
        super(20,150, 450, viewport);
        this.world = world;
        this.mainController = mainController;
        init(20);
    }

    @Override
    protected Trunk newObject() {
        return new Trunk(mainController, TextureRegions.TRUNK, world, viewport, 0, 0, 150, 450);
    }
}
