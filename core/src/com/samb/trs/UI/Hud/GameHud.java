package com.samb.trs.UI.Hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.samb.trs.Controllers.AssetController;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Interfaces.UIElement;
import com.samb.trs.Interfaces.Updatable;
import com.samb.trs.Model.Score;
import com.samb.trs.Resources.BitmapFonts;
import com.samb.trs.Resources.TextureRegions;

public class GameHud extends Group implements UIElement, Updatable {
    private MainController mainController;
    private AssetController assetController;
    private Viewport viewport;
    private CollectIcon coinIcon, boostIcon;
    private ImageButton pauseButton, shieldButton;
    private Label scoreLabel;
    private float safeHeight;
    private Score score;

    public GameHud(MainController mainController) {
        this.mainController = mainController;
        this.assetController = mainController.getAssetController();
        this.viewport = mainController.getRenderController().getStaticViewport();
        this.safeHeight = mainController.getRenderController().getIosSafeArea().y;
        this.score = mainController.getGameWorldController().getScore();

        initIcons();
        initButtons();
        initScore();

        addActors();

        addAction(Actions.sequence(
                Actions.alpha(0.0f),
                Actions.visible(false)
        ));
        setTouchable(Touchable.disabled);
    }

    private void initScore() {
        scoreLabel = new Label("0", assetController.getAsset(BitmapFonts.BOLD100, Color.WHITE));
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setPosition(RenderController.wperc(50),
                RenderController.hperc(93.5f) - safeHeight);
    }

    private void initIcons() {
        this.coinIcon = new CollectIcon(mainController,
                new TextureRegionDrawable(assetController.getAsset(TextureRegions.COIN_ANZEIGE)));
        this.boostIcon = new CollectIcon(mainController,
                new TextureRegionDrawable(assetController.getAsset(TextureRegions.BOOST_ANZEIGE)));

        coinIcon.setPosition(RenderController.wperc(62.5f),
                RenderController.hperc(92) - safeHeight);

        boostIcon.setPosition(RenderController.wperc(80),
                RenderController.hperc(92) - safeHeight);
    }

    private void initButtons() {
        this.pauseButton = new ImageButton(new TextureRegionDrawable(assetController.getAsset(TextureRegions.PAUSE_BUTTON)));
        pauseButton.setBounds(RenderController.wperc(5), RenderController.hperc(92) + RenderController.wperc(3.5f),
                RenderController.wperc(8), RenderController.wperc(8) - safeHeight);

        this.shieldButton = new ImageButton(new TextureRegionDrawable(assetController.getAsset(TextureRegions.SHIELD_BUTTON)));
        shieldButton.setBounds(RenderController.wperc(5), RenderController.wperc(5),
                RenderController.wperc(25), RenderController.wperc(25));
    }

    private void addActors() {
        addActor(coinIcon);
        addActor(boostIcon);
        addActor(pauseButton);
        addActor(shieldButton);
        addActor(scoreLabel);
    }

    @Override
    public void update(float dt) {
        scoreLabel.setText((int) score.getScore());
        coinIcon.setCount(score.getCollected_coins());
        boostIcon.setCount(score.getBoosts());
    }

    public void show(Runnable runnable, float duration, Interpolation interpolation) {
        setVisible(true);
        addAction(Actions.alpha(0.0f));
        addAction(Actions.sequence(
                Actions.alpha(1.0f, duration, interpolation),
                Actions.run(runnable)
        ));
        setTouchable(Touchable.enabled);
    }

    public void hide(Runnable runnable, float duration, Interpolation interpolation) {
        addAction(Actions.alpha(1.0f));
        addAction(
                Actions.sequence(
                        Actions.alpha(0.0f, duration, interpolation),
                        Actions.visible(false),
                        Actions.run(runnable)
                )
        );
        setTouchable(Touchable.disabled);
    }

    public ImageButton getShieldButton() {
        return shieldButton;
    }

    public ImageButton getPauseButton() {
        return pauseButton;
    }

    public Score getScore() {
        return score;
    }
}
