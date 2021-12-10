package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.samb.trs.Resources.Shaders;

import java.util.HashMap;

public class ShaderController extends BaseController{
    private HashMap<Shaders, ShaderProgram> shaders;

    public ShaderController(MainController mainController) {
        super(mainController);
        shaders = new HashMap<>();
        loadShaders();
    }

    private void loadShaders() {
        for(Shaders s : Shaders.values()) {
            ShaderProgram shader = getShader(s);
            if (!shader.isCompiled())
                Gdx.app.error("ShaderError","Couldn't compile shader " + s.toString() + ": " + shader.getLog());
        }
    }

    public ShaderProgram getShader(Shaders s) {
        if (!shaders.containsKey(s)) {
            ShaderProgram shader = new ShaderProgram(Gdx.files.internal(s.getIdentifier().concat(".vert")),
                    Gdx.files.internal(s.getIdentifier().concat(".frag")));
            shaders.put(s, shader);
        }

        return shaders.get(s);
    }

    @Override
    public void dispose() {
        for(Shaders s : shaders.keySet()) {
            shaders.get(s).dispose();
            shaders.remove(s);
        }

        shaders.clear();
    }
}
