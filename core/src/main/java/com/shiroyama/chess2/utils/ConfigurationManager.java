package com.shiroyama.chess2.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.shiroyama.chess2.chessboard.pieces.PieceType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Represents the configuration manager for the chess game.
 * This class is responsible for managing and persisting application settings, including chess piece stats
 * (HP and attack rate) and graphical preferences (window size, VSync, fullscreen mode).
 */
public class ConfigurationManager {

    /**
     * The singleton instance of the {@code ConfigurationManager}.
     */
    private static ConfigurationManager instance;

    /**
     * A {@link Properties} object used to store and manage chess piece stats (HP and attack rate).
     */
    private Properties properties;

    /**
     * The file path for the configuration file containing chess piece stats.
     */
    private final String configFilePath = "stats.cfg";

    /**
     * A {@link Preferences} object used to store and manage graphical preferences.
     */
    private Preferences graphicalPreferences;

    /**
     * Constructor for the class.
     * Initializes the configuration by loading the stats file and setting default graphical preferences.
     */
    private ConfigurationManager() {
        properties = new Properties();
        loadConfiguration();

        graphicalPreferences = Gdx.app.getPreferences("chess2-settings");

        if (!graphicalPreferences.contains("window_width")){
            graphicalPreferences.putInteger("window_width", 640);
        }
        if (!graphicalPreferences.contains("window_height")){
            graphicalPreferences.putInteger("window_height", 480);
        }
        if (!graphicalPreferences.contains("vsync")){
            graphicalPreferences.putBoolean("vsync", true);
        }
        if (!graphicalPreferences.contains("fullscreen")){
            graphicalPreferences.putBoolean("fullscreen", false);
        }
        graphicalPreferences.flush();
    }

    /**
     * Receives the singleton instance of the {@code ConfigurationManager}.
     *
     * @return the singleton instance of the {@code ConfigurationManager}.
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    /**
     * Loads the chess piece stats from the configuration file ({@code stats.cfg}).
     * If the file is missing or corrupted, default values are initialized.
     */
    private void loadConfiguration() {
        FileHandle file = Gdx.files.internal(configFilePath);
        try {
            properties.load(file.reader());
        } catch (IOException e) {
            Gdx.app.error("ConfigurationManager", "Error loading stats.cfg: " + e.getMessage());
            initializeDefaultProperties();
        }
    }

    /**
     * Initializes default values for all chess piece stats (HP and attack rate).
     * These values are saved to the configuration file if no existing configuration is found.
     */
    private void initializeDefaultProperties() {
        for (PieceType pieceType : PieceType.values()) {
            String type = pieceType.toString();
            switch (pieceType) {
                case PAWN:
                    properties.setProperty(type + ".hp", "1");
                    properties.setProperty(type + ".attackRate", "1.0");
                    break;
                case KNIGHT:
                    properties.setProperty(type + ".hp", "10");
                    properties.setProperty(type + ".attackRate", "1.5");
                    break;
                case BISHOP:
                    properties.setProperty(type + ".hp", "3");
                    properties.setProperty(type + ".attackRate", "2.5");
                    break;
                case ROOK:
                    properties.setProperty(type + ".hp", "10");
                    properties.setProperty(type + ".attackRate", "2.0");
                    break;
                case QUEEN:
                    properties.setProperty(type + ".hp", "15");
                    properties.setProperty(type + ".attackRate", "3.0");
                    break;
                case KING:
                    properties.setProperty(type + ".hp", "20");
                    properties.setProperty(type + ".attackRate", "1.0");
                    break;
            }
        }
        saveConfiguration();
    }

    /**
     * Retrieves the HP value for the specified chess {@link PieceType}.
     *
     * @param pieceType the {@link PieceType} of the piece
     * @return the HP value for the specified {@link PieceType}, or 1 if the value is not found
     */
    public int getHp(PieceType pieceType) {
        String value = properties.getProperty(pieceType.toString() + ".hp");
        return value != null ? Integer.parseInt(value) : 1;
    }

    /**
     * Retrieves the attack rate for the specified chess {@link PieceType}.
     *
     * @param pieceType the {@link PieceType} of the piece
     * @return the attack rate for the specified {@link PieceType}, or 1.0 if the value is not found
     */
    public float getAttackRate(PieceType pieceType) {
        String value = properties.getProperty(pieceType.toString() + ".attackRate");
        return value != null ? Float.parseFloat(value) : 1.0f;
    }

    /**
     * Sets the HP value for the specified chess {@link PieceType}.
     *
     * @param pieceType the {@link PieceType} of the piece
     * @param hp the new hp value for the specified {@link PieceType}
     */
    public void setHp(PieceType pieceType, int hp) {
        properties.setProperty(pieceType.toString() + ".hp", String.valueOf(hp));
    }

    /**
     * Sets the attack rate for the specified chess {@link PieceType}.
     *
     * @param pieceType the {@link PieceType} of the piece
     * @param attackRate the new attack rate for the specified {@link PieceType}
     */
    public void setAttackRate(PieceType pieceType, float attackRate) {
        properties.setProperty(pieceType.toString() + ".attackRate", String.valueOf(attackRate));
    }

    /**
     * Saves the current chess piece stats and graphical preferences to their respective storage locations.
     */
    public void saveConfiguration() {
        try {
            FileHandle file = Gdx.files.local(configFilePath);
            OutputStream out = file.write(false);
            properties.store(out, "Chess Piece Stats Configuration");
            out.close();

            saveGraphicsConfiguration();
        } catch (IOException e) {
            Gdx.app.error("ConfigurationManager", "Error saving stats.cfg: " + e.getMessage());
        }
    }

    /**
     * Reloads the chess piece stats from the configuration file.
     */
    public void reloadConfiguration() {
        properties.clear();
        loadConfiguration();
    }

    /**
     * Sets the desired window size for the application.
     * @param width the new width
     * @param height the new height
     */
    public void setWindowSize(int width, int height){
        graphicalPreferences.putInteger("window_height", height);
        graphicalPreferences.putInteger("window_width", width);
    }

    /**
     * Enables or disables VSync (vertical synchronization) for the application.
     * @param enabled whether the VSync should be enabled
     */
    public void setVSync(boolean enabled){
        graphicalPreferences.putBoolean("vsync", enabled);
    }

    /**
     * Enables or disables fullscreen mode for the application.
     *
     * @param enabled whether the fullscreen mode should be enabled
     */
    public void setFullscreen(boolean enabled){
        graphicalPreferences.putBoolean("fullscreen", enabled);
    }

    /**
     * Retrieves the current window width from the graphical preferences.
     *
     * @return the current window width
     */
    public int getWindowWidth(){
        return graphicalPreferences.getInteger("window_width");
    }

    /**
     * Retrieves the current window height from the graphical preferences.
     *
     * @return the current window height
     */
    public int getWindowHeight(){
        return graphicalPreferences.getInteger("window_height");
    }

    /**
     * Checks whether VSync is currently enabled in the graphical preferences.
     *
     * @return true if the VSync is enabled, false otherwise
     */
    public boolean isVSyncEnabled(){
        return graphicalPreferences.getBoolean("vsync");
    }

    /**
     * Checks whether fullscreen mode is currently enabled in the graphical preferences.
     * @return true if the fullscreen mode is enabled, false otherwise
     */
    public boolean isFullscreenEnabled(){
        return graphicalPreferences.getBoolean("fullscreen");
    }

    /**
     * Saves the current graphical preferences to persistent storage.
     */
    public void saveGraphicsConfiguration(){
        graphicalPreferences.flush();
    }
}
