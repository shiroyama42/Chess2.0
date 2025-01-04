package com.shiroyama.chess2;

import com.badlogic.gdx.Game;
import com.shiroyama.chess2.screens.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ChessGame extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }


}
