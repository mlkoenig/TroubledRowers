package com.samb.trs.Controllers;

import com.badlogic.gdx.utils.Disposable;

public abstract class BaseController implements Disposable {
    private MainController mainController;

    public BaseController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMain(){
        return mainController;
    }

    public AssetController getAssets() {
        return mainController.getAssetController();
    }
}
