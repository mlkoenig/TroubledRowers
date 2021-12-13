package com.samb.trs.UI.Menu;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.samb.trs.Controllers.AssetController;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Model.Timer;
import com.samb.trs.Resources.TextureRegions;
import com.samb.trs.UI.Hud.CollectIcon;

public class GameMenu extends Group {

    private MainController mainController;
    private AssetController assetController;
    private CollectIcon coinIcon;
    private BlinkLabel pressToPlay;
    private ImageButton settingsButton;

    public GameMenu(MainController mainController) {
        this.mainController = mainController;
        this.assetController = mainController.getAssetController();

        initCoins();
        initPressToPlay();
        initSettingsButton();
        addActors();
    }

    private void initCoins() {
        this.coinIcon = new CollectIcon(mainController,
                new TextureRegionDrawable(assetController.getAsset(TextureRegions.COIN_ANZEIGE)));
        coinIcon.setPosition(RenderController.p2w(80), RenderController.p2h(92));
    }

    private void initSettingsButton() {
        this.settingsButton = new ImageButton(new TextureRegionDrawable(assetController.getAsset(TextureRegions.GEAR_BUTTON)));
        settingsButton.setBounds(RenderController.p2w(5), RenderController.p2h(92),
                RenderController.p2w(10), RenderController.p2w(10));
    }

    private void initPressToPlay() {
        pressToPlay = new BlinkLabel("press to play", assetController.getSkin());
        pressToPlay.setAlignment(Align.center);
        pressToPlay.setBounds(0,
                RenderController.p2h(15),
                RenderController.p2w(100),
                RenderController.p2h(10)
        );
    }

    private void addActors() {
        addActor(coinIcon);
        addActor(pressToPlay);
        addActor(settingsButton);
    }

    public boolean insideActor(Actor actor, float x, float y){
        return !(new Vector2(actor.getX() + actor.getWidth() / 2f, actor.getY() + actor.getHeight() / 2f).dst2(x, y) <= actor.getWidth() * actor.getHeight() / 2f);
    }

    public void update(float dt) {
        pressToPlay.update(dt);
    }

    public void show(Runnable runnable) {
        setVisible(true);
        addAction(Actions.alpha(0.0f));
        addAction(Actions.sequence(
                Actions.alpha(1.0f, 0.5f, Interpolation.fade),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        pressToPlay.setAnimate(true);
                    }
                }),
                Actions.run(runnable)
        ));
        setTouchable(Touchable.enabled);
    }

    public void hide(Runnable runnable) {
        pressToPlay.setAnimate(false);
        addAction(Actions.alpha(1.0f));
        addAction(
                Actions.sequence(
                        Actions.alpha(0.0f, 0.5f, Interpolation.fade),
                        Actions.visible(false),
                        Actions.run(runnable)
                )
        );
        setTouchable(Touchable.disabled);
    }
}
