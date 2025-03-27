package com.shiroyama.chess2;

import com.badlogic.gdx.Game;
import com.shiroyama.chess2.screens.MenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 * Represents the main game class for the chess application.
 * This class extends {@link Game} and serves as the entry point for the application, managing screen transitions
 * and initializing the game's primary components.
 */
public class ChessGame extends Game {
    /**
     * Called when the application is created.
     * Initializes the game by setting the starting screen to the {@link MenuScreen}.
     */
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
