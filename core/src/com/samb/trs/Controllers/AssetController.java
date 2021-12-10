package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.samb.trs.Resources.*;
import com.samb.trs.Utilities.BodyEditorLoader;

public class AssetController extends BaseController {
    private final AssetManager assetManager;
    private BodyEditorLoader bodyEditorLoader;

    public AssetController(MainController mainController) {
        super(mainController);
        this.assetManager = new AssetManager();
        this.bodyEditorLoader = new BodyEditorLoader(Gdx.files.internal("Box2d/box2d.json"));

        loadAssets();

        assetManager.finishLoading();
    }

    private void loadAssets(){
        for (Atlases a : Atlases.values()) {
            loadAsset(a);
        }

        for (Bundles a : Bundles.values()) {
            loadAsset(a);
        }

        for (Musics m : Musics.values()) {
            loadAsset(m);
        }

        for (Sounds s : Sounds.values()) {
            loadAsset(s);
        }

        for (Textures t : Textures.values()) {
            loadAsset(t);
        }
    }

    public BodyEditorLoader getBodyEditorLoader() {
        return bodyEditorLoader;
    }

    /**
     * Load an asset to the assetManager.
     * @param resource the enum object of the asset
     * @param <T> the type of the asset
     */
    private <T> void loadAsset(Resource<T> resource){
        assetManager.load(resource.getIdentifier(), resource.getType());
    }

    public <T> T getAsset(Resource<T> resource) {
        return assetManager.get(resource.getIdentifier(), resource.getType());
    }

    public TextureRegion getAsset(TextureRegions t){
        return getAsset(t.getAtlas()).findRegion(t.getIdentifier());
    }

    public ParticleEffect getAsset(Particles p) {
        return assetManager.get(p.getIdentifier(), ParticleEffect.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
