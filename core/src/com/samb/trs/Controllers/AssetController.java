package com.samb.trs.Controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.samb.trs.Resources.*;
import com.samb.trs.Utilities.BodyEditorLoader;

import java.util.HashMap;

import static com.samb.trs.Resources.Constants.Fonts.DEFAULT_COLOR;

public class AssetController extends BaseController {
    private final AssetManager assetManager;
    private BodyEditorLoader bodyEditorLoader;
    private HashMap<BitmapFonts, BitmapFont> bitmapFonts;
    private Skin skin;

    public AssetController(MainController mainController) {
        super(mainController);
        this.assetManager = new AssetManager();
        this.bodyEditorLoader = new BodyEditorLoader(Gdx.files.internal("Box2d/box2d.json"));
        this.bitmapFonts = new HashMap<>();

        initSkin();
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

        for(BitmapFonts bmf : BitmapFonts.values()){
            bitmapFonts.put(bmf, getAsset(bmf));
        }
    }

    /**
     * Used to create different font sizes and add them to the skin.
     */
    private void initFonts(){

        // Initialize generator first font creation

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Iterate over all fonts and add to skin by given parameters

        for(Fonts f:Fonts.values()){

            // If font path changes, then dispose the old generator and create a new one with right path

            String fontName = f.getFontName();
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/" + fontName));

            // Set font parameters

            for(FontSizes s:FontSizes.values()) {

                param.size = s.getSize();
                param.color = DEFAULT_COLOR;

                // Add font to skin

                skin.add(f.getName(s), generator.generateFont(param));
            }

            generator.dispose();
        }

        // Create default font

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/" + Fonts.DEFAULT.getFontName()));

        param.size = FontSizes.F50.getSize();
        param.color = DEFAULT_COLOR;

        skin.add("default-font", generator.generateFont(param));

        generator.dispose();
    }

    /**
     * Loads the default skin.
     */
    private void initSkin(){
        //Create a new empty skin
        skin = new Skin();

        // Add the right texture atlas to the skin
        skin.addRegions(new TextureAtlas(Gdx.files.internal("Skins/uiskin.atlas")));

        // Add all fonts to the skin
        initFonts();

        // Finally add json to the skin
        skin.load(Gdx.files.internal("Skins/uiskin.json"));
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

    public BitmapFont getAsset(BitmapFonts bmf){
        if (!bitmapFonts.containsKey(bmf))
            return skin.getFont(bmf.getFont().getName(bmf.getFontSize()));

        return bitmapFonts.get(bmf);
    }

    public Label.LabelStyle getAsset(BitmapFonts bmf, Color labelColor){
        return new Label.LabelStyle(getAsset(bmf), labelColor);
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
