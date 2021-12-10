package com.samb.trs.Pools;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Sprites.Coin;
import com.samb.trs.Resources.TextureRegions;

public class CoinPool extends AbstractPool<Coin> {
    private MainController mainController;
    private World world;

    public CoinPool(MainController mainController, World world, Viewport viewport) {
        super(20,50, 50, viewport);
        this.world = world;
        this.mainController = mainController;
        init(20);
    }

    @Override
    protected Coin newObject() {
        return new Coin(mainController, TextureRegions.COIN, world, viewport, 0, 0, 25);
    }
}
