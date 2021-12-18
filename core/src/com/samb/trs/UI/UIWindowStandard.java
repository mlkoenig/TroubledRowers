package com.samb.trs.UI;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;

public class UIWindowStandard extends UIWindow {

    public UIWindowStandard(MainController mainController) {
        super(mainController, Constants.Rendering.WorldWidth * 0.9f, Constants.Rendering.WorldWidth * 0.9f);
        resetWindow();
    }

    @Override
    protected void onShow(Runnable runnable, float duration, Interpolation interpolation) {
        setVisible(true);
        addAction(Actions.alpha(0.0f));
        addAction(Actions.moveTo(getCenterX(), -getHeight()));
        addAction(Actions.moveTo(getCenterX(), getCenterY(), duration, interpolation));
        addAction(Actions.sequence(
                    Actions.alpha(1.0f, duration, interpolation),
                    Actions.run(runnable)
                )
        );
    }

    @Override
    protected void onHide(Runnable runnable, float duration, Interpolation interpolation) {
        addAction(Actions.alpha(0.0f, duration, interpolation));
        addAction(
                Actions.sequence(
                        Actions.moveTo(getCenterX(), -getHeight(), duration, interpolation),
                        Actions.visible(false),
                        Actions.run(runnable)
                )
        );
    }

    @Override
    protected void resetWindow() {
        setSize(Constants.Rendering.WorldWidth * 0.9f, Constants.Rendering.WorldWidth * 0.9f);
        addAction(Actions.moveTo(getCenterX(), -getHeight()));
        addAction(Actions.alpha(0.0f));
    }
}
