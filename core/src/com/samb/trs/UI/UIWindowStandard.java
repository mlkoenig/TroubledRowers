package com.samb.trs.UI;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;

public class UIWindowStandard extends UIWindow {

    public UIWindowStandard(MainController mainController) {
        super(mainController, RenderController.wperc(90), RenderController.wperc(90));
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
        setSize(RenderController.wperc(90), RenderController.wperc(90));
        addAction(Actions.moveTo(getCenterX(), -getHeight()));
        addAction(Actions.alpha(0.0f));
    }
}
