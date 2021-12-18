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
    private FitViewport viewport;
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
        scoreLabel.setPosition(RenderController.p2w(50),
                RenderController.p2h(93.5f));
    }

    private void initIcons() {
        this.coinIcon = new CollectIcon(mainController,
                new TextureRegionDrawable(assetController.getAsset(TextureRegions.COIN_ANZEIGE)));
        this.boostIcon = new CollectIcon(mainController,
                new TextureRegionDrawable(assetController.getAsset(TextureRegions.BOOST_ANZEIGE)));

        coinIcon.setPosition(RenderController.p2w(62.5f),
                RenderController.p2h(92) - safeHeight);

        boostIcon.setPosition(RenderController.p2w(80),
                RenderController.p2h(92) - safeHeight);
    }

    private void initButtons() {
        this.pauseButton = new ImageButton(new TextureRegionDrawable(assetController.getAsset(TextureRegions.PAUSE_BUTTON)));
        pauseButton.setBounds(RenderController.p2w(5), RenderController.p2h(92) + RenderController.p2w(3.5f),
                RenderController.p2w(8), RenderController.p2w(8) - safeHeight);

        this.shieldButton = new ImageButton(new TextureRegionDrawable(assetController.getAsset(TextureRegions.SHIELD_BUTTON)));
        shieldButton.setBounds(RenderController.p2w(5), RenderController.p2w(5),
                RenderController.p2w(25), RenderController.p2w(25) - safeHeight);
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
}
