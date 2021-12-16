package com.samb.trs.Systems;

import com.badlogic.ashley.core.EntitySystem;

public class GameOverSystem extends EntitySystem {
    private boolean stateChanged;


    public GameOverSystem() {
        super();
        stateChanged = false;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (stateChanged)
            getEngine().getSystem(GameLogicSystem.class).setGameState(GameLogicSystem.GameState.GAMEOVER);
        stateChanged = false;
    }

    public void gameOver() {
        this.stateChanged = true;
    }
}
