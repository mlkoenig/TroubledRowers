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
        add(label).padLeft(RenderController.wperc(8));
        setSize(RenderController.wperc(15), RenderController.wperc(15));
    }

    public void setCount(int i) {
        label.setText(i);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y - height * 0.275f, width, height);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - getHeight() * 0.275f);
    }
}
