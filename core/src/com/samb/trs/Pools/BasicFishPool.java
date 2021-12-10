package com.samb.trs.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Sprites.BasicFish;
import com.samb.trs.Resources.TextureRegions;

public class BasicFishPool extends AbstractPool<BasicFish> {
    private MainController mainController;
    private World world;

    public BasicFishPool(MainController mainController, World world, Viewport viewport) {
        super(20, 200, 160, viewport);
        this.world = world;
        this.mainController = mainController;
        init(20);
    }

    @Override
    protected BasicFish newObject() {
        return new BasicFish(mainController, TextureRegions.FISH, world, 0, 0, width, height);
    }
}
