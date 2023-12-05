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
