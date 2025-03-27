package com.shiroyama.chess2.chessboard.pieces;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.shiroyama.chess2.arena.Projectile;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.utils.ConfigurationManager;

import java.util.List;

/**
 * Represents a chess piece with its properties and actions.
 */
public class PieceInfo {

    private Team team;
    private PieceType pieceType;
    private int hp;
    private float attackRate;
    private TargetPoint position;

    /**
     * Constructor for the class.
     *
     * @param team the {@link Team} of the piece (BLACK or WHITE)
     * @param pieceType the {@link PieceType} of the piece (PAWN, BISHOP, etc.)
     * @param position the initial position of the piece on the board
     */
    public PieceInfo(Team team, PieceType pieceType, TargetPoint position) {
        super();
        this.team = team;
        this.pieceType = pieceType;
        this.position = position;
        loadStats(pieceType);
    }

    /**
     * Gets the name of the piece, including its color and type.
     *
     * @return a string representation of the piece's name.
     */
    public String getName(){
        return ((team == Team.WHITE) ? "white" : "black") + "-" + pieceType.toString().toLowerCase();
    }

    /**
     * Gets the name of the piece for display after the combat is over.
     *
     * @param combatOver indicates whether combat has ended
     * @return a string representation of the piece's name for post-combat display
     */
    public String getName(boolean combatOver){
        return ((team == Team.WHITE) ? "WHITE" : "BLACK") + " " + pieceType.toString();
    }

    /**
     * Gets the default HP (hit points) for a given piece type.
     *
     * @param pieceType the type of the piece
     * @return the default HP value for the specified piece type
     */
    public static int getDefaultHp(PieceType pieceType){
        switch (pieceType){
            case PAWN: return 1;
            case KNIGHT: return 10;
            case BISHOP: return 3;
            case ROOK: return 10;
            case QUEEN: return 15;
            case KING: return 20;
            default: return 1;
        }
    }

    /**
     * Gets the default attack rate (number of projectiles it shoots in a second) for a given piece type.
     *
     * @param pieceType the type of the piece
     * @return the default attack rate value for the specified piece type
     */
    private float getDefaultAttackRate(PieceType pieceType){
        switch (pieceType){
            case PAWN: return 1f;
            case KNIGHT: return 1.5f;
            case BISHOP: return 2.5f;
            case ROOK: return 2f;
            case QUEEN: return 3f;
            case KING: return 1f;
            default: return 1f;
        }
    }

    /**
     * Creates and adds new {@link Projectile} to the list of active projectiles.
     *
     * @param targetPosition the target position for the projectile
     * @param projectiles the list of active projectiles which the new projectile will be added
     */
    public void shoot(TargetPoint targetPosition, List<Projectile> projectiles){

        TargetPoint centeredTargetPosition =
            new TargetPoint(targetPosition.getX() + 0.5f,targetPosition.getY() + 0.5f);
        Projectile projectile = new Projectile(this.team, position, centeredTargetPosition);
        projectiles.add(projectile);
    }

    /**
     * Gets the position of the piece.
     *
     * @return the position
     */
    public TargetPoint getPosition(){
        return position;
    }

    /**
     * Sets the position of the piece.
     *
     * @param position the new position
     */
    public void setPosition(TargetPoint position){
        this.position = position;
    }

    /**
     * Loads the stats of a piece type from a file.
     *
     * @param pieceType the piece type which stats are required
     */
    private void loadStats(PieceType pieceType){

        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        this.hp = configurationManager.getHp(pieceType);
        this.attackRate = configurationManager.getAttackRate(pieceType);
    }

    /**
     * Gets the {@link Team} of the piece.
     *
     * @return the team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Sets the {@link Team} of the piece.
     *
     * @param team the new team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Gets the {@link PieceType} of the piece.
     *
     * @return the piece type
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Sets the {@link PieceType} of the piece.
     *
     * @param pieceType the new piece type
     */
    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    /**
     * Gets the HP of the piece.
     *
     * @return the HP
     */
    public int getHp() {
        return hp;
    }

    /**
     * Sets the HP of the piece.
     *
     * @param hp the new HP
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Gets the attack rate of the piece.
     *
     * @return the attack rate
     */
    public float getAttackRate() {
        return attackRate;
    }

    /**
     * Sets the attack rate of the piece.
     *
     * @param attackRate the new attack rate
     */
    public void setAttackRate(float attackRate) {
        this.attackRate = attackRate;
    }
}
