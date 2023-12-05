package com.samb.trs.Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Components.*;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.Musics;
import com.samb.trs.Utilities.Mappers;
import com.samb.trs.Utilities.NullRunnable;

import java.util.Objects;

public class GameLogicSystem extends IteratingSystem {
    private GameState gameState;
    private Array<Entity> entities_player;
    private MainController mainController;
    private boolean stateChange;

    public GameLogicSystem(MainController mainController) {
        super(Family.one(PlayerComponent.class, ShieldComponent.class, RowingComponent.class, ParticleEffectComponent.class).get());
        this.mainController = mainController;
        this.gameState = GameState.LOADING;
        entities_player = new Array<>();
        stateChange = false;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        stateChange = true;

        Music river_sound = mainController.getSoundController().getMusic(Musics.RIVER);
        river_sound.setLooping(true);

        switch (gameState) {
            case PLAY:
                getEngine().getSystem(GameOverSystem.class).setProcessing(true);
                getEngine().getSystem(PhysicsSystem.class).setProcessing(true);
                getEngine().getSystem(CameraSystem.class).setProcessing(true);
                getEngine().getSystem(SpawnSystem.class).setProcessing(true);
                getEngine().getSystem(ShoreSystem.class).setProcessing(true);
                getEngine().getSystem(MouseControlSystem.class).setProcessing(true);
                getEngine().getSystem(PlayerControlSystem.class).setProcessing(true);
                getEngine().getSystem(AttachedSystem.class).setProcessing(true);
                getEngine().getSystem(ShieldSystem.class).setProcessing(true);
                getEngine().getSystem(SteeringSystem.class).setProcessing(true);
                getEngine().getSystem(CollisionSystem.class).setProcessing(true);
                getEngine().getSystem(RowingSystem.class).setProcessing(true);

                if(!river_sound.isPlaying()) river_sound.play();

                break;
            case PAUSE:
            case GAMEOVER:
                mainController.getUiController().hideAll(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainController.getUiController().getGameOverWindow().show(new NullRunnable());
                            }
                        }
                );
                getEngine().getSystem(GameOverSystem.class).setProcessing(false);
                getEngine().getSystem(PhysicsSystem.class).setProcessing(false);
                getEngine().getSystem(CameraSystem.class).setProcessing(false);
                getEngine().getSystem(SpawnSystem.class).setProcessing(false);
                getEngine().getSystem(ShoreSystem.class).setProcessing(false);
                getEngine().getSystem(MouseControlSystem.class).setProcessing(false);
                getEngine().getSystem(PlayerControlSystem.class).setProcessing(false);
                getEngine().getSystem(AttachedSystem.class).setProcessing(false);
                getEngine().getSystem(ShieldSystem.class).setProcessing(false);
                getEngine().getSystem(SteeringSystem.class).setProcessing(false);
                getEngine().getSystem(CollisionSystem.class).setProcessing(false);
                getEngine().getSystem(RowingSystem.class).setProcessing(false);

                if(river_sound.isPlaying()) river_sound.stop();

                break;
            case LOADING:
            case MENU:
                getEngine().getSystem(GameOverSystem.class).setProcessing(false);
                getEngine().getSystem(PhysicsSystem.class).setProcessing(true);
                getEngine().getSystem(CameraSystem.class).setProcessing(true);
                getEngine().getSystem(SpawnSystem.class).setProcessing(true);
                getEngine().getSystem(ShoreSystem.class).setProcessing(true);
                getEngine().getSystem(MouseControlSystem.class).setProcessing(false);
                getEngine().getSystem(PlayerControlSystem.class).setProcessing(false);
                getEngine().getSystem(AttachedSystem.class).setProcessing(true);
                getEngine().getSystem(ShieldSystem.class).setProcessing(false);
                getEngine().getSystem(SteeringSystem.class).setProcessing(true);
                getEngine().getSystem(CollisionSystem.class).setProcessing(true);
                getEngine().getSystem(RowingSystem.class).setProcessing(false);

                if(river_sound.isPlaying()) river_sound.pause();
                break;
        }

        if (Constants.General.DEBUGGING) {
            getEngine().getSystem(PhysicsDebugSystem.class).setProcessing(true);
        }

        entities_player.clear();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (entities_player.notEmpty() && stateChange) {
            switch (gameState) {
                case LOADING:
                case MENU:
                    setPlayerVisible(false);
                    break;
                case PLAY:
                case PAUSE:
                case GAMEOVER:
                    setPlayerVisible(true);
                    mainController.getSaveController().getAccount().increaseGamesPlayed();
                    mainController.getSaveController().saveGameState();
                    break;
            }
            stateChange = false;
        }

        if (gameState == GameState.PLAY) {
            mainController.getGameWorldController().getScore().update(deltaTime);
            mainController.getUiController().getGameHud().update(deltaTime);
        } else if(gameState == GameState.GAMEOVER) {
            mainController.getUiController().getGameOverWindow().update();
        }

        // Play sounds
        mainController.getSoundController().playSoundQueue();
    }

    private void setPlayerVisible(boolean visible) {
        for (Entity entity : entities_player) {
            if (Mappers.player.has(entity)) {
                Entity grid = Mappers.shield.get(entity).gridEntity;
                Entity shield = Mappers.shield.get(entity).shieldEntity;
                Entity man = Mappers.rowing.get(entity).man;
                Entity paddle = Mappers.rowing.get(entity).paddle;

                Mappers.transform.get(entity).isHidden = !visible;
                Mappers.transform.get(man).isHidden = !visible;
                Mappers.transform.get(paddle).isHidden = !visible;

                if (!visible) {
                    Mappers.transform.get(grid).isHidden = true;
                    Mappers.transform.get(shield).isHidden = true;
                }
            } else if(Mappers.peCom.has(entity)) {
                Mappers.transform.get(entity).isHidden = !visible;
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Mappers.peCom.has(entity) && Mappers.attached.has(entity)) {
            AttachedComponent ac = Mappers.attached.get(entity);

            if (ac.isAttached && Mappers.player.has(ac.attachedTo)) {
                entities_player.add(entity);
            }

        } else entities_player.add(entity);
    }

    public enum GameState {PLAY, PAUSE, GAMEOVER, MENU, LOADING, NONE}
}
