package com.samb.trs.UI.Hud;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.samb.trs.Controllers.MainController;
import com.samb.trs.Controllers.RenderController;

public class CollectIcon extends Table {
    private MainController mainController;
    private Label label;

    public CollectIcon(MainController mainController, TextureRegionDrawable drawable) {
        this.mainController = mainController;

        label = new Label("0", mainController.getAssetController().getSkin());
        label.setAlignment(Align.right);

        setBackground(drawable);
        add(label).padLeft(120);
        setSize(RenderController.wperc(15), RenderController.wperc(15));
    }

    public void setCount(int i) {
        label.setText(i);
    }
}
