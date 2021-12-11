package com.samb.trs.Controllers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.samb.trs.UI.GameOver.GameOverWindow;

public class UIController extends BaseController{
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;

    public UIController(MainController mainController) {
        super(mainController);
        this.camera = getMain().getRenderController().getStaticCamera();
        this.viewport = getMain().getRenderController().getStaticViewport();
        this.stage = mainController.getRenderController().getStage();
        GameOverWindow uiWindowStandard = new GameOverWindow(mainController);
        uiWindowStandard.show(null);
        stage.addActor(uiWindowStandard);
    }

    public void render(float dt){
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
