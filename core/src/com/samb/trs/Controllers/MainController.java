package com.samb.trs.Controllers;

import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.Disposable;
import com.samb.trs.Adapters.MainAdapter;
import com.samb.trs.GameApplication;
import com.samb.trs.Utilities.Box2dAi.Box2dLocation;

public class MainController implements Disposable {
    private final GameApplication game;
    private AssetController assetController;
    private RenderController renderController;
    private SaveController saveController;
    private GameServiceController gameServiceController;
    private SoundController soundController;
    private GameWorldController gameWorldController;
    private UIController uiController;
    private MainAdapter mainAdapter;
    private InputController inputController;

    public MainController(GameApplication gameApplication) {
        Box2D.init();
        this.game = gameApplication;
        this.mainAdapter = new MainAdapter();
        this.gameServiceController = new GameServiceController(this);
        this.assetController = new AssetController(this);
        this.saveController = new SaveController(this);
        this.soundController = new SoundController(this);
        this.renderController = new RenderController(this);
        this.gameWorldController = new GameWorldController(this);
        this.uiController = new UIController(this);
        this.inputController = new InputController(this);

        inputController.setInputControllers();
    }

    public UIController getUiController() {
        return uiController;
    }

    public GameApplication getGame() {
        return game;
    }

    public AssetController getAssetController() {
        return assetController;
    }

    public RenderController getRenderController() {
        return renderController;
    }

    public GameWorldController getGameWorldController() {
        return gameWorldController;
    }

    public MainAdapter getMainAdapter() {
        return mainAdapter;
    }

    public SoundController getSoundController() {
        return soundController;
    }

    public GameServiceController getGameServiceController() {
        return gameServiceController;
    }

    public SaveController getSaveController() {
        return saveController;
    }

    public InputController getInputController() {
        return inputController;
    }

    @Override
    public void dispose() {
        gameWorldController.dispose();
        renderController.dispose();
        soundController.dispose();
        saveController.dispose();
        assetController.dispose();
        gameServiceController.dispose();
    }
}
