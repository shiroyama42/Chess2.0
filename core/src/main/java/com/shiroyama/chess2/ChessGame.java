package com.shiroyama.chess2;

import com.badlogic.gdx.Game;
import com.shiroyama.chess2.screens.MenuScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 * Represents the main game class for the chess application.
 * This class extends {@link Game} and serves as the entry point for the application, managing screen transitions
 * and initializing the game's primary components.
 */
public class ChessGame extends Game {

    /**
     * {@link Logger} for logging game start.
     */
    private static final Logger logger = LoggerFactory.getLogger(ChessGame.class);

    /**
     * Called when the application is created.
     * Initializes the game by setting the starting screen to the {@link MenuScreen}.
     */
    @Override
    public void create() {
        logger.info("Game started.");
        setScreen(new MenuScreen(this));
    }
}
