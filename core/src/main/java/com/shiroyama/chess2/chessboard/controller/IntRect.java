package com.shiroyama.chess2.chessboard.controller;

/**
 * Represents a rectangle with integer coordinates and properties.
 */
public class IntRect {

    private int x;
    private int y;
    private int height;
    private int width;

    /**
     * Constructor for the class.
     *
     * @param x the x-coordinate of the top-left corner
     * @param y the y-coordinate of the top left corner
     * @param height height of the rectangle
     * @param width width of the rectangle
     */
    public IntRect(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    /**
     * Gets the x-coordinate of the rectangle.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the rectangle.
     *
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the rectangle.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the rectangle.
     *
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the height of the rectangle.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the rectangle.
     *
     * @param height the new height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the width of the rectangle.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the rectangle.
     *
     * @param width the new width
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
