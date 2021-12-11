package com.samb.trs.UI.GameOver;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Resources.TextureRegions;

public class Separator extends Table {
    public Separator(MainController mainController) {
        TextureRegionDrawable draw = new TextureRegionDrawable(mainController.getAssetController().getAsset(TextureRegions.SCOREBOARD_SEPARATOR));
        setBackground(draw);
    }
}
