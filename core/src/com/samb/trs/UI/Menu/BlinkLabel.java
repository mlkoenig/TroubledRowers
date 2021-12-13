package com.samb.trs.UI.Menu;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.samb.trs.Model.Timer;

public class BlinkLabel extends Label {
    private static final float PLAY_ANIMATION_TIME = .8f;

    private Timer playAnimationTimer;
    private boolean playAnimation, animate;

    public BlinkLabel(CharSequence text, Skin skin) {
        super(text, skin);
        this.playAnimationTimer = new Timer();
        this.playAnimation = true;
        this.animate = true;
    }

    public void update(float dt) {
        if(animate) {
            playAnimationTimer.update(dt);
            if (playAnimationTimer.passed(PLAY_ANIMATION_TIME)) {
                if (!playAnimation) {
                    addAction(Actions.fadeIn(PLAY_ANIMATION_TIME));
                } else {
                    addAction(Actions.fadeOut(PLAY_ANIMATION_TIME));
                }
                playAnimation = !playAnimation;
            }
        }
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }
}
