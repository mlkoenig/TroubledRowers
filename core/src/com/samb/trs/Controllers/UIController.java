package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.samb.trs.Systems.GameLogicSystem;
import com.samb.trs.UI.GameOver.GameOverWindow;
import com.samb.trs.UI.Hud.GameHud;
import com.samb.trs.UI.Menu.GameMenu;
import com.samb.trs.Utilities.NullRunnable;

public class UIController extends BaseController{
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;
    private GameOverWindow gameOverWindow;
    private GameHud gameHud;
    private GameMenu gameMenu;

    public UIController(MainController mainController) {
        super(mainController);
        this.camera = getMain().getRenderController().getStaticCamera();
        this.viewport = getMain().getRenderController().getStaticViewport();
        this.stage = mainController.getRenderController().getStage();
        this.gameOverWindow = new GameOverWindow(mainController);
        this.gameHud = new GameHud(mainController);
        this.gameMenu = new GameMenu(mainController);

        stage.addActor(gameOverWindow);
        stage.addActor(gameHud);
        stage.addActor(gameMenu);

        addListeners();
    }

    public void render(float dt){
        gameMenu.update(dt);
        stage.act(dt);
        stage.draw();
    }

    public void hideAll(Runnable runnable) {
        gameOverWindow.hide(new NullRunnable());
        gameMenu.hide(new NullRunnable());
        gameHud.hide(runnable);
    }

    private void addListeners() {
        gameOverWindow.getButtonTable().getMenuButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getMain().getGameWorldController().setGameState(GameLogicSystem.GameState.MENU);
                hideAll(new Runnable() {
                    @Override
                    public void run() {
                        gameMenu.show(new NullRunnable());
                    }
                });
            }
        });

        gameOverWindow.getButtonTable().getRestartButton().addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                hideAll(new Runnable() {
                    @Override
                    public void run() {
                        getMain().getGameWorldController().newGame();
                    }
                });
            }
        });

        gameHud.getShieldButton().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getMain().getGameWorldController().getEventInputProcessor().setSpaceDown(true);
                //if (player.isShielded()) account.increaseUsedShields();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getMain().getGameWorldController().getEventInputProcessor().setSpaceDown(false);
            }
        });
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    public GameOverWindow getGameOverWindow() {
        return gameOverWindow;
    }

    public GameHud getGameHud() {
        return gameHud;
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }
}
