package com.samb.trs.Utilities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SBActions {
    public static MoveToAction center(Viewport viewport, Actor actor){
        return com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo(
                getCenterX(viewport, actor),
                getCenterY(viewport, actor)
        );
    }

    public static float getCenterX(Viewport viewport, Actor actor){
        return 0.5f*(viewport.getWorldWidth() - actor.getWidth());
    }

    public static float getCenterY(Viewport viewport, Actor actor){
        return 0.5f*(viewport.getWorldHeight() - actor.getHeight());
    }
}
