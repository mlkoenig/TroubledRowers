package com.samb.trs.Interfaces;

import com.badlogic.gdx.math.Interpolation;
import com.samb.trs.Resources.Constants;

import static com.samb.trs.Resources.Constants.UI.DEFAULT_INTERPOLATION;

public interface UIElement {
    void show(Runnable runnable, float duration, Interpolation interpolation);
    void hide(Runnable runnable, float duration, Interpolation interpolation);
    default void show(Runnable runnable, float duration) {
        show(runnable, duration, DEFAULT_INTERPOLATION);
    }
    default void hide(Runnable runnable, float duration) {
        hide(runnable, duration, DEFAULT_INTERPOLATION);
    }
    default void show(Runnable runnable) {
        show(runnable, Constants.UI.DEFAULT_DURATION);
    }
    default void hide(Runnable runnable) {
        hide(runnable, Constants.UI.DEFAULT_DURATION);
    }
}
