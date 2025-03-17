package com.shiroyama.chess2.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;



public class PromotionDialog extends Dialog {

    public PromotionDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    public PromotionDialog(String title, Skin skin) {
        super(title, skin);
    }

    public PromotionDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    @Override
    protected void result(Object object) {
    }
}
