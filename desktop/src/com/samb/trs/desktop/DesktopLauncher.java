package com.samb.trs.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.samb.trs.GameApplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(430, 932);
		config.useVsync(false);
		config.setForegroundFPS(0);
		config.setIdleFPS(0);
		new Lwjgl3Application(new GameApplication(), config);
	}
}