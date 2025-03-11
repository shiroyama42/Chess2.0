package com.shiroyama.chess2.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.shiroyama.chess2.chessboard.pieces.PieceType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Properties properties;
    private final String configFilePath = "stats.cfg";

    private ConfigurationManager() {
        properties = new Properties();
        loadConfiguration();
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadConfiguration() {
        FileHandle file = Gdx.files.internal(configFilePath);
        try {
            properties.load(file.reader());
        } catch (IOException e) {
            Gdx.app.error("ConfigurationManager", "Error loading stats.cfg: " + e.getMessage());
            initializeDefaultProperties();
        }
    }

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

    public int getHp(PieceType pieceType) {
        String value = properties.getProperty(pieceType.toString() + ".hp");
        return value != null ? Integer.parseInt(value) : 1;
    }

    public float getAttackRate(PieceType pieceType) {
        String value = properties.getProperty(pieceType.toString() + ".attackRate");
        return value != null ? Float.parseFloat(value) : 1.0f;
    }

    public void setHp(PieceType pieceType, int hp) {
        properties.setProperty(pieceType.toString() + ".hp", String.valueOf(hp));
    }

    public void setAttackRate(PieceType pieceType, float attackRate) {
        properties.setProperty(pieceType.toString() + ".attackRate", String.valueOf(attackRate));
    }

    public void saveConfiguration() {
        try {
            FileHandle file = Gdx.files.local(configFilePath);
            OutputStream out = file.write(false);
            properties.store(out, "Chess Piece Stats Configuration");
            out.close();
        } catch (IOException e) {
            Gdx.app.error("ConfigurationManager", "Error saving stats.cfg: " + e.getMessage());
        }
    }

    public void reloadConfiguration() {
        properties.clear();
        loadConfiguration();
    }
}
