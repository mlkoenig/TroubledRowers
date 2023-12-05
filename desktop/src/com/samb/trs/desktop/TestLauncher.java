package com.samb.trs.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.samb.trs.GameApplication;
import com.samb.trs.Tests.TestScene;

public class TestLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowSizeLimits(405, 720, 405, 720);
        config.useVsync(false);
        config.setForegroundFPS(0);
        config.setIdleFPS(0);
        new Lwjgl3Application(new TestScene(), config);
    }
}
