package com.samb.trs.Model.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.Constants;
import com.samb.trs.Resources.TextureRegions;

public class Shield {
    public static final float SHIELD_FREQUENCY = .6f;
    private MainController mainController;
    private Sprite bg, gitter;
    private ShaderProgram shieldShader;
    private Timer timer;
    private Array<Fixture> fixtures;

    public Shield(MainController mainController, float px, float py, float width, float height) {
        this.mainController = mainController;
        bg = new Sprite(mainController.getAssetController().getAsset(TextureRegions.SHIELD_BACKGROUND));
        gitter = new Sprite(mainController.getAssetController().getAsset(TextureRegions.SHIELD_GITTER));

        bg.setBounds(px-width/2f, py-height/2f, width, height);
        gitter.setBounds(px-width/2f, py-height/2f, width, height);

        shieldShader = new ShaderProgram(Gdx.files.internal("Shaders/shield.vert"), Gdx.files.internal("Shaders/shield.frag"));
        timer = new Timer();

        fixtures = new Array<>();
    }

    public void draw(Batch batch){
        bg.draw(batch);
        ShaderProgram shaderProgram = batch.getShader();
        batch.setShader(shieldShader);
        if(timer.passedAbsolute(SHIELD_FREQUENCY/2f)){
            shieldShader.setUniformf("t", 1-(timer.getValue()/SHIELD_FREQUENCY));
        }else{
            shieldShader.setUniformf("t", timer.getValue()/SHIELD_FREQUENCY);
        }
        gitter.draw(batch);
        batch.setShader(shaderProgram);
    }

    public void update(float dt, Vector2 center){
        timer.update(dt);
        if(timer.passedAbsolute(SHIELD_FREQUENCY)) timer.reset();
        moveTo(center.x, center.y);
    }

    public void moveTo(float px, float py){
        bg.setPosition(px-bg.getWidth()/2f, py-bg.getHeight()/2f);
        gitter.setPosition(px-gitter.getWidth()/2f, py-gitter.getHeight()/2f);
    }

    public void defineFixture(Boat boat){
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 100;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0.9f;
        fixtureDef.filter.categoryBits = Constants.Collision.SHIELD_BIT;
        fixtureDef.filter.maskBits = Constants.Collision.ROCK_BIT | Constants.Collision.FISH_BIT | Constants.Collision.TRUNK_BIT;
        boat.bodyEditorLoader.attachFixture(boat.getBody(), TextureRegions.SHIELD_BACKGROUND.getIdentifier(), fixtureDef, bg, 1);
        for(Fixture fixture:boat.getBody().getFixtureList()){
            if (fixture.getFilterData().categoryBits == Constants.Collision.SHIELD_BIT) {
                fixtures.add(fixture);
                fixture.setSensor(true);
                fixture.setUserData(boat);
            }
        }
    }

    public void setSensor(boolean isSensor){
        for(Fixture fixture:fixtures){
            fixture.setSensor(isSensor);
        }
    }

    public Sprite getBg() {
        return bg;
    }

    public void dispose(){
        shieldShader.dispose();
    }
}
