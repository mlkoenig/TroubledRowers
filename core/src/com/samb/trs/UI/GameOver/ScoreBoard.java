package com.samb.trs.UI.GameOver;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.BitmapFonts;
import com.samb.trs.Resources.Bundles;
import com.samb.trs.Resources.Constants;
import de.golfgl.gdxgamesvcs.leaderboard.ILeaderBoardEntry;

public class ScoreBoard extends Table {
    private Label first, second, third;
    private Label.LabelStyle style50, style100, style120;
    private Color labelColor;

    public ScoreBoard(MainController mainController) {
        this.labelColor = new Color(1, 0.847f, 0.584f, 1);
        style50 = mainController.getAssetController().getAsset(BitmapFonts.BOLD50, labelColor);
        style100 = mainController.getAssetController().getAsset(BitmapFonts.BOLD100, labelColor);
        style120 = mainController.getAssetController().getAsset(BitmapFonts.BOLD120, labelColor);

        String loading = mainController.getAssetController().getAsset(Bundles.GAMEOVER).get("loading");
        Stage stage = mainController.getRenderController().getStage();

        first = new Label(loading, style100);
        first.setAlignment(Align.center);
        first.setEllipsis(true);
        second = new Label(loading, style50);
        second.setEllipsis(true);
        second.setAlignment(Align.center);
        third = new Label(loading, style50);
        third.setEllipsis(true);
        third.setAlignment(Align.center);

        add(second).width(Constants.Rendering.WorldWidth / 3f - Constants.Rendering.WorldWidth*0.05f).padRight(stage.getWidth()*0.01f).padTop(stage.getHeight()*0.04f);
        add(first).width(Constants.Rendering.WorldWidth / 3f - Constants.Rendering.WorldWidth*0.05f).padRight(stage.getWidth()*0.01f).padTop(-stage.getHeight()*0.01f);
        add(third).width(Constants.Rendering.WorldWidth / 3f - Constants.Rendering.WorldWidth*0.05f).padTop(stage.getHeight()*0.04f);
    }

    public void setLeaderBoardEntry(ILeaderBoardEntry entry, I18NBundle bundle){
        first.setText("#"+entry.getScoreRank().replace(".", ""));
        second.setText(entry.getFormattedValue());
        if(entry.isCurrentPlayer())
            third.setText(bundle.get("you"));
        else third.setText(entry.getUserDisplayName());
    }

    public void setBig(boolean b){
        if(b){
            first.setStyle(style120);
        }else{
            first.setStyle(style100);
        }
    }

    public Label getFirst() {
        return first;
    }

    public Label getSecond() {
        return second;
    }

    public Label getThird() {
        return third;
    }
}
