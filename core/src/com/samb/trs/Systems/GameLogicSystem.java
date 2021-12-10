package com.samb.trs.Systems;

import com.badlogic.ashley.core.EntitySystem;
import com.samb.trs.Resources.Constants;

public class GameLogicSystem extends EntitySystem {
    private GameState gameState;

    public GameLogicSystem() {
        this.gameState = GameState.NONE;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        switch (gameState) {
            case PLAY:
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
                break;
            case PAUSE:
            case GAMEOVER:
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
                break;
            case LOADING:
            case MENU:
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
                break;
        }

        if (Constants.General.DEBUGGING) {
            getEngine().getSystem(PhysicsDebugSystem.class).setProcessing(false);
        }
    }

    public enum GameState {PLAY, PAUSE, GAMEOVER, MENU, LOADING, NONE}
}
