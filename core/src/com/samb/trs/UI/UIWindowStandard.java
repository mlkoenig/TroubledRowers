package com.samb.trs.UI;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.Constants;

public class UIWindowStandard extends UIWindow {
    private static final float DURATION = 1f;
    private static final Interpolation INTERPOLATION = Interpolation.fade;

    public UIWindowStandard(MainController mainController) {
        super(mainController, Constants.Rendering.WorldWidth * 0.9f, Constants.Rendering.WorldWidth * 0.9f);
        resetWindow();
    }

    @Override
    protected void onShow() {
        addAction(Actions.alpha(0.0f));
        addAction(Actions.moveTo(getCenterX(), -getHeight()));
        addAction(Actions.moveTo(getCenterX(), getCenterY(), DURATION, INTERPOLATION));
        addAction(Actions.alpha(1.0f, DURATION, INTERPOLATION));
    }

    @Override
    protected void onHide() {
        addAction(Actions.alpha(0.0f, DURATION, INTERPOLATION));
        addAction(Actions.moveTo(getCenterX(), -getHeight(), DURATION, INTERPOLATION));
    }

    @Override
    protected void resetWindow() {
        setSize(Constants.Rendering.WorldWidth * 0.9f, Constants.Rendering.WorldWidth * 0.9f);
        addAction(Actions.moveTo(getCenterX(), -getHeight()));
        addAction(Actions.alpha(0.0f));
    }
}
