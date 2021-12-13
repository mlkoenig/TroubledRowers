package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.samb.trs.Systems.GameLogicSystem;

public class InputController extends BaseController implements InputProcessor {
    private Stage stage;
    private InputMultiplexer inputMultiplexer;

    public InputController(MainController mainController) {
        super(mainController);
        this.stage = mainController.getRenderController().getStage();
        this.inputMultiplexer = new InputMultiplexer(stage, this, getMain().getGameWorldController().getEventInputProcessor());

        setInputControllers();
    }

    public void setInputControllers(){
        Gdx.input.setInputProcessor(inputMultiplexer);
    }


    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (getMain().getGameWorldController().getGameState() == GameLogicSystem.GameState.MENU) {
            getMain().getGameWorldController().newGame();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
