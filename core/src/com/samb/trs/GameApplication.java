package com.samb.trs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Resources.Constants;

public class GameApplication implements ApplicationListener {
	private MainController mainController;
	private GLProfiler glProfiler;
	private float drawCalls, textureBinds, fps, shadowSwitches;

	
	@Override
	public void create () {
		this.mainController = new MainController(this);
		glProfiler = new GLProfiler(Gdx.graphics);
		glProfiler.enable();
	}

	@Override
	public void resize(int width, int height) {
		mainController.getRenderController().resize(width, height);
	}

	@Override
	public void render () {
		glProfiler.reset();
		mainController.getRenderController().render(Gdx.graphics.getDeltaTime());
		drawCalls = glProfiler.getDrawCalls();
		textureBinds = glProfiler.getTextureBindings();
		fps = Gdx.graphics.getFramesPerSecond();
		shadowSwitches = glProfiler.getShaderSwitches();

		if (Constants.General.DEBUGGING)
			System.out.println("DrawCalls: " + drawCalls +
					"\n TextureBinds: " + textureBinds +
					"\n ShadowSwitches: " + shadowSwitches +
					"\n FPS: " + fps);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		mainController.dispose();
		glProfiler.disable();
	}
}
