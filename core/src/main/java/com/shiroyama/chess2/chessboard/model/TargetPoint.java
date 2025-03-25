package com.shiroyama.chess2.chessboard.model;

/**
 * Representing a point on the chess board with floating-point coordinates.
 * User for tracking positions and movement of pieces.
 */
public class TargetPoint {

    /**
     * the x- and y-coordinates of the point.
     */
    private float x, y;

    /**
     * Constructor for the class.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public TargetPoint(float x, float y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Compares this point with another object.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TargetPoint)){
            return false;
        }
        TargetPoint other = (TargetPoint) obj;
        return other.getX() == x && other.getY() == y;
    }

    /**
     * Gets the x-coordinate of the point.
     *
     * @return the x-coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the point.
     *
     * @param x the new x-coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the point.
     *
     * @return the y-coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the point.
     *
     * @param y the new y-coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Creates a new point by adding the specified offsets to this point's coordinates.
     *
     * @param x the x-offset to add
     * @param y the y-offset to add
     * @return a new TargetPoint with the resulting coordinates
     */
    public TargetPoint Transpose(float x, float y){
        return new TargetPoint(this.x + x, this.y + y);
    }
}
