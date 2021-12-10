package com.samb.trs.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.BitmapFonts;

public class GameOverWindow extends UIWindowStandard {
    private Label.LabelStyle scoreStyle, bold50Style, bold150Style;
    private Label scoreLabel, highScoreLabel;

    public GameOverWindow(MainController mainController) {
        super(mainController);
        initStyles();
        initScoreLabel();
        initScoreBoard();
        addActors();
    }

    private void initScoreLabel(){
        this.scoreLabel = new Label("Game Over", scoreStyle);
        scoreLabel.setAlignment(Align.center);
    }

    private void initStyles(){
        this.scoreStyle = assets.getAsset(BitmapFonts.SCORE, labelColor);
        this.bold50Style = assets.getAsset(BitmapFonts.BOLD50, labelColor);
        this.bold150Style = assets.getAsset(BitmapFonts.BOLD150, labelColor);
    }

    private void initScoreBoard(){

    }

    private void addActors(){
        add(scoreLabel);
    }


}
