package com.shiroyama.chess2.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shiroyama.chess2.ChessGame;
import com.shiroyama.chess2.arena.Arena;
import com.shiroyama.chess2.chessboard.model.ChessBoard;
import com.shiroyama.chess2.utils.PieceMovementHandler;
import com.shiroyama.chess2.arena.Projectile;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;
import com.shiroyama.chess2.utils.ScoreBoardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Represents the {@link Screen} where the chess arena combat takes place.
 * Handles user input, rendering, updating the game state during combat between two chess pieces.
 * Manages UI elements such as buttons and health bars, transitions to other screens when combat ends.
 */
public class ArenaScreen implements Screen {

    /**
     * The main game instance that manages screen transitions and game logic.
     */
    private ChessGame game;

    /**
     * A {@link SpriteBatch} used for rendering textures and sprites.
     */
    private SpriteBatch batch;

    /**
     * Textures for the attacker, defender, gun, projectile, and background.
     */
    private Texture attackerTexture, defenderTexture, gunTexture, projectileTexture, backgroundTexture;

    /**
     * The {@link Arena} instance that manages the combat logic between the attacker and defender.
     */
    private Arena arena;

    /**
     * Handles movement logic for the chess pieces.
     */
    private PieceMovementHandler movementHandler;

    /**
     * Handles movement logic for the chess pieces.
     */
    private BitmapFont font;

    /**
     * A {@link GlyphLayout} used to calculate text dimensions for proper alignment.
     */
    private GlyphLayout layout;

    /**
     * The {@link Stage} used for managing UI elements like buttons.
     */
    private Stage stage;

    /**
     * A {@link Skin} used for styling UI elements.
     */
    private Skin skin;

    /**
     * Indicates whether the king has died, ending the game.
     */
    private boolean kingDied = false;

    /**
     * Indicates whether the combat has ended.
     */
    private boolean combatOver = false;

    /**
     * Tracks whether the score has been added to the scoreboard after combat ends.
     */
    private boolean scoreAdded = false;

    /**
     * Stores the game over message to be displayed on the screen.
     */
    private String gameOverMessage;

    /**
     * A button that allows the player to return to the main menu.
     */
    private TextButton menuButton;

    /**
     * A button that allows the player to view the scoreboard.
     */
    private TextButton scoreButton;

    /**
     * A {@link ShapeRenderer} used for drawing shapes like health bars.
     */
    private ShapeRenderer shapeRenderer;

    /**
     * The {@link ScoreBoardManager} the scoreboard and records captures during combat.
     */
    private ScoreBoardManager scoreBoardManager;

    /**
     * Indicates whether the combat has started.
     */
    private boolean combatStarted = false;

    /**
     * A countdown timer before the combat begins.
     */
    private float countdownTimer = 3f;

    /**
     * Scale factor based on the screen's size for drawing pieces, health bar, projectiles and gun textures.
     */
    private float pieceScaleFactor, healthBarScaleFactor;

    /**
     * {@link Logger} for logging screen switch and arena combat results.
     */
    private static final Logger logger = LoggerFactory.getLogger(ArenaScreen.class);

    /**
     * Constructor for the class.
     *
     * @param attacker the attacking piece
     * @param defender the attacked piece
     * @param game the main game instance
     */
    public ArenaScreen(PieceInfo attacker, PieceInfo defender, ChessGame game){
        this.batch = new SpriteBatch();
        this.arena = new Arena(attacker, defender);
        this.game = game;
        this.movementHandler = new PieceMovementHandler(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        attackerTexture = new Texture("chess_pieces/" + attacker.getName() + ".png");
        defenderTexture = new Texture("chess_pieces/" + defender.getName() + ".png");

        gunTexture = new Texture("arena_textures/gun.png");
        projectileTexture = new Texture("arena_textures/projectile.png");
        backgroundTexture = new Texture("arena_textures/arena_background.png");

        font = new BitmapFont();
        font.getData().setScale(3);
        font.setColor(1, 0, 0, 1);
        layout = new GlyphLayout();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        this.shapeRenderer = new ShapeRenderer();

        this.scoreBoardManager = ScoreBoardManager.getInstance();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        attacker.getPosition().setX(screenWidth / (2 * 50f));
        attacker.getPosition().setY((screenHeight * 0.8f) / 50f);

        defender.getPosition().setX(screenWidth / (2 * 50f));
        defender.getPosition().setY((screenHeight * 0.2f) / 50f);

        pieceScaleFactor = Math.min(screenWidth, screenHeight) / 400f;

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    /**
     * Renders the arena, chess pieces, projectiles, health bars and UI elements.
     *
     * @param delta the time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(254f / 255f, 156f / 255f, 28f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!kingDied && arena.isCombatOver()){
            PieceInfo winner = arena.attackerWon() ? arena.getAttacker() : arena.getDefender();
            PieceInfo loser = arena.attackerWon() ? arena.getDefender() : arena.getAttacker();

            if(loser.getPieceType() == PieceType.KING){
                kingDied = true;
                combatOver = true;

                String winningTeam = winner.getTeam() == Team.WHITE ? "WHITE" : "BLACK";
                String losingTeam = loser.getTeam() == Team.WHITE ? "WHITE" : "BLACK";

                logger.info("{} defeated, {} team won.", loser.getName(), winningTeam.toLowerCase());

                gameOverMessage = losingTeam + " KING DIED\n" + winningTeam + " TEAM WON!";

                menuButton = new TextButton("Return to Menu", skin);
                menuButton.setSize((float) (Gdx.graphics.getWidth() * 0.3), 60);
                menuButton.setPosition(
                    (Gdx.graphics.getWidth() - menuButton.getWidth()) / 2,
                    Gdx.graphics.getHeight() / 4f
                );
                menuButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        game.create();
                        logger.info("Menu button clicked, returning to menu screen.");
                    }
                });

                scoreButton = new TextButton("Scoreboard", skin);
                scoreButton.setSize((float) (Gdx.graphics.getWidth() * 0.3), 60);
                scoreButton.setPosition(
                    (Gdx.graphics.getWidth() - scoreButton.getWidth()) / 2,
                    Gdx.graphics.getHeight() / 10f
                );
                scoreButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        game.setScreen(new ScoreBoardScreen(game));
                        logger.info("Scoreboard button clicked, changing the screen to scoreboard screen.");
                    }
                });

                if (!scoreAdded){
                    scoreBoardManager.recordCapture(loser, winner.getTeam());
                    scoreAdded = true;
                }

                stage.addActor(menuButton);
                stage.addActor(scoreButton);

            }else{
                combatOver = true;
                gameOverMessage = winner.getName(true) + " WON!";

                menuButton = new TextButton("Continue", skin);
                menuButton.setSize((float) (Gdx.graphics.getWidth() * 0.3), 60);
                menuButton.setPosition(
                    (Gdx.graphics.getWidth() - menuButton.getWidth()) / 2,
                    Gdx.graphics.getHeight() / 4f
                );
                menuButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        ((GameScreen) game.getScreen()).exitArena(winner, loser);
                    }
                });

                if (!scoreAdded){
                    scoreBoardManager.recordCapture(loser, winner.getTeam());
                    scoreAdded = true;
                    logger.info("{} won the arena combat.", winner.getName());
                }

                stage.addActor(menuButton);
            }
        }

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        List<Projectile> projectiles = arena.getProjectiles();
        for (Projectile projectile : projectiles){
            float projectileSize = 10 * pieceScaleFactor;
            batch.draw(projectileTexture,
                projectile.getPosition().getX() * 50,
                projectile.getPosition().getY() * 50,
                projectileSize, projectileSize);
        }

        drawPiece(attackerTexture, arena.getAttacker().getPosition(), arena.getDefender().getPosition(), gunTexture, arena.getAttacker());
        drawPiece(defenderTexture, arena.getDefender().getPosition(), arena.getAttacker().getPosition(), gunTexture, arena.getDefender());

        batch.end();

        batch.begin();
        if (!combatStarted){
            countdownTimer -= delta;
            if (countdownTimer <= 0){
                combatStarted = true;
                arena.startCombat();

                logger.info("Arena combat started.");

                gameOverMessage = null;
            }else{
                gameOverMessage = String.format("%.1f", countdownTimer);
            }
        }

        if (/*kingDied && */gameOverMessage != null) {
            layout.setText(font, gameOverMessage);
            float textX = (Gdx.graphics.getWidth() - layout.width) / 2f;
            float textY = Gdx.graphics.getHeight() / 2f + layout.height / 2f;
            font.draw(batch, gameOverMessage, textX, textY);
        }

        batch.end();

        if (combatOver) {
            stage.act(delta);
            stage.draw();
        } else {
            arena.update(delta);
            if (combatStarted){
                handleMovement();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Removes all resources used by this screen, such as textures and fonts.
     */
    @Override
    public void dispose() {
        batch.dispose();
        attackerTexture.dispose();
        defenderTexture.dispose();
        gunTexture.dispose();
        font.dispose();
        stage.dispose();
        if (skin != null) {
            skin.dispose();
        }
        backgroundTexture.dispose();
    }

    /**
     * Draws the chess piece, the associated gun and health bar at the specified location.
     *
     * @param texture the texture of the chess piece
     * @param position the position of the chess piece
     * @param targetPosition the position of the target piece
     * @param gunTexture the texture of the gun
     */
    private void drawPiece(Texture texture, TargetPoint position, TargetPoint targetPosition, Texture gunTexture, PieceInfo piece){
        if(position == null || targetPosition == null){
            return;
        }

        float pieceWidth = 50 * pieceScaleFactor;
        float pieceHeight = 50 * pieceScaleFactor;

        batch.draw(texture,
            position.getX() * 50,
            position.getY() * 50,
            pieceWidth / 2,
            pieceHeight /2,
            pieceWidth,
            pieceHeight,
            1,
            1,
            0,
            0,
            0,
            texture.getWidth(),
            texture.getHeight(),
            false,
            false);

        float angle = getGunAngle(targetPosition, position);
        float gunWidth =  10 * pieceWidth * 0.11f;
        float gunHeight = 10 * pieceHeight * 0.11f;
        float gunOffset = 5 * pieceScaleFactor;

        batch.draw(gunTexture,
            position.getX() * 50 + gunOffset - gunWidth / 2,
            position.getY() * 50 + gunOffset - gunHeight / 2,
            gunWidth / 2,
            gunHeight / 2,
            gunWidth,
            gunHeight,
            1,
            1,
            angle,
            0,
            0,
            gunTexture.getWidth(),
            gunTexture.getHeight(),
            false,
            false
        );

        batch.end();

        if (!combatOver){
            drawHealthBar(piece, position.getX() * 50, position.getY() * 50, pieceWidth, pieceHeight);
        }

        batch.begin();
    }

    /**
     * Calculates the angle of the gun relative to the target position.
     *
     * @param targetPosition the position of the target piece
     * @param piecePosition the position of the piece
     * @return the angle in degrees
     */
    private float getGunAngle(TargetPoint targetPosition, TargetPoint piecePosition){
        float dx = targetPosition.getX() - piecePosition.getX();
        float dy = targetPosition.getY() - piecePosition.getY();
        return (float) Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * Handles movement input for the chess piece based on keyboard input.
     */
    private void handleMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movementHandler.moveUp((arena.getAttacker().getTeam() == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movementHandler.moveDown((arena.getAttacker().getTeam() == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movementHandler.moveLeft((arena.getAttacker().getTeam() == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movementHandler.moveRight((arena.getAttacker().getTeam() == Team.WHITE) ? arena.getAttacker() : arena.getDefender());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movementHandler.moveUp((arena.getDefender().getTeam() == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movementHandler.moveDown((arena.getDefender().getTeam() == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movementHandler.moveLeft((arena.getDefender().getTeam() == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movementHandler.moveRight((arena.getDefender().getTeam() == Team.BLACK) ? arena.getDefender() : arena.getAttacker());
        }
    }

    /**
     * Draws health bar above specified piece.
     *
     * @param piece the piece whose health bar needs to be drawn
     * @param x the x-coordinate of the health bar
     * @param y the y-coordinate of the health bar
     */
    private void drawHealthBar(PieceInfo piece, float x, float y, float pieceWidth, float pieceHeight){
        float healthPercentage = (float) piece.getHp() / loadDefaultHp(piece.getPieceType());

        float barWidth = Gdx.graphics.getWidth() * 0.1f; // 10% of screen width
        float barHeight = Gdx.graphics.getHeight() * 0.01f; // 1% of screen height

        // Calculate health bar position
        float healthBarX = x + (pieceWidth - barWidth) / 2; // Center horizontally with the piece
        float healthBarY = y + pieceHeight + barHeight;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        shapeRenderer.rect(healthBarX, healthBarY, barWidth, barHeight);

        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(healthBarX, healthBarY, barWidth * healthPercentage, barHeight);
        shapeRenderer.end();
    }

    /**
     * Loads the default HP value for a given piece type from the configuration file.
     *
     * @param pieceType the type of the piece
     * @return the HP value of the specified piece type
     */
    private int loadDefaultHp(PieceType pieceType){
        Properties props =  new Properties();
        FileHandle file = Gdx.files.internal("stats.cfg");

        try {
            props.load(file.reader());
            String pieceTypeName = pieceType.toString();

            return Integer.parseInt(props.getProperty(pieceTypeName + ".hp"));

        }catch (IOException e){
            Gdx.app.error("PieceInfo", "Error loading stats.cfg: " + e.getMessage());
            return PieceInfo.getDefaultHp(pieceType);
        }
    }
}
