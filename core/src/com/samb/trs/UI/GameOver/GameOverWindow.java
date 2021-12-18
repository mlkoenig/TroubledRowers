package com.samb.trs.UI.GameOver;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;
import com.samb.trs.Interfaces.UIElement;
import com.samb.trs.Resources.BitmapFonts;
import com.samb.trs.Resources.Bundles;
import com.samb.trs.Resources.Constants;
import com.samb.trs.UI.UIWindowStandard;

public class GameOverWindow extends UIWindowStandard {
    private Label.LabelStyle scoreStyle, bold50Style, bold150Style;
    private Label scoreLabel, highScoreLabel;
    private Table scoreTable;
    private ScoreBoard scoreBoard;
    private Separator separator;
    private ButtonTable buttonTable;
    private Stage stage;

    public GameOverWindow(MainController mainController) {
        super(mainController);
        this.stage = mainController.getRenderController().getStage();

        initStyles();
        initScoreTable();

        this.separator = new Separator(mainController);
        this.scoreBoard = new ScoreBoard(mainController);
        this.buttonTable = new ButtonTable(mainController);

        addActors();
    }

    private void initScoreTable(){
        this.scoreLabel = new Label("0", bold150Style);
        this.scoreLabel.setAlignment(Align.center);
        this.highScoreLabel = new Label("Best: 0", bold50Style);
        this.highScoreLabel.setAlignment(Align.center);

        this.scoreTable = new Table();
        scoreTable.add(new Label(mainController.getAssetController().getAsset(Bundles.GAMEOVER).get("score"), scoreStyle)).row();
        scoreTable.add(scoreLabel).row();
        scoreTable.add(highScoreLabel);
    }

    private void initStyles(){
        this.scoreStyle = assets.getAsset(BitmapFonts.SCORE, labelColor);
        this.bold50Style = assets.getAsset(BitmapFonts.BOLD50, labelColor);
        this.bold150Style = assets.getAsset(BitmapFonts.BOLD150, labelColor);
    }

    private void addActors(){
        add(scoreTable).row();
        add(separator).width(Constants.Rendering.WorldWidth * 0.9f - stage.getWidth()*0.25f)
                .padTop(RenderController.p2h(2.5f))
                .row();
        add(scoreBoard).padLeft(RenderController.p2w(5))
                .padRight(RenderController.p2w(5))
                .padTop(RenderController.p2h(2.5f))
                .row();
        add(buttonTable).padTop(RenderController.p2h(5));
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getHighScoreLabel() {
        return highScoreLabel;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Separator getSeparator() {
        return separator;
    }

    public ButtonTable getButtonTable() {
        return buttonTable;
    }
}
