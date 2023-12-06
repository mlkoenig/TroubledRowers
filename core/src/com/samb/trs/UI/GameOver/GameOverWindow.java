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

    public void update() {
        this.scoreLabel.setText(String.valueOf(mainController.getGameWorldController().getScore().getScore()));
        this.highScoreLabel.setText("Best: " + mainController.getSaveController().getAccount().getHighScore());
    }

    private void initStyles(){
        this.scoreStyle = assets.getAsset(BitmapFonts.SCORE, labelColor);
        this.bold50Style = assets.getAsset(BitmapFonts.BOLD50, labelColor);
        this.bold150Style = assets.getAsset(BitmapFonts.BOLD150, labelColor);
    }

    private void addActors(){
        add(scoreTable).row();
        add(separator).width(RenderController.wperc(90) - stage.getWidth() * 0.25f)
                .height(RenderController.hperc(0.8f))
                .padTop(RenderController.hperc(2.5f))
                .row();
        add(scoreBoard).padLeft(RenderController.wperc(5))
                .padRight(RenderController.wperc(5))
                .padTop(RenderController.wperc(3f))
                .row();
        add(buttonTable).padTop(RenderController.hperc(5));
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
