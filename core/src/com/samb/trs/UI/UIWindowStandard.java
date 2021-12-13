package com.samb.trs.UI;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;

public class UIWindowStandard extends UIWindow {
    private static final float DURATION = 0.5f;
    private static final Interpolation INTERPOLATION = Interpolation.fade;

    public UIWindowStandard(MainController mainController) {
        super(mainController, Constants.Rendering.WorldWidth * 0.9f, Constants.Rendering.WorldWidth * 0.9f);
        resetWindow();
    }

    @Override
    protected void onShow(Runnable runnable) {
        setVisible(true);
        addAction(Actions.alpha(0.0f));
        addAction(Actions.moveTo(getCenterX(), -getHeight()));
        addAction(Actions.moveTo(getCenterX(), getCenterY(), DURATION, INTERPOLATION));
        addAction(Actions.sequence(
                    Actions.alpha(1.0f, DURATION, INTERPOLATION),
                    Actions.run(runnable)
                )
        );
    }

    @Override
    protected void onHide(Runnable runnable) {
        addAction(Actions.alpha(0.0f, DURATION, INTERPOLATION));
        addAction(
                Actions.sequence(
                        Actions.moveTo(getCenterX(), -getHeight(), DURATION, INTERPOLATION),
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
