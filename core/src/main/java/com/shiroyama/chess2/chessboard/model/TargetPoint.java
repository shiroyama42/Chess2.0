package com.shiroyama.chess2.chessboard.model;

public class TargetPoint {

    private float x, y;

    public TargetPoint(float x, float y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TargetPoint)){
            return false;
        }
        TargetPoint other = (TargetPoint) obj;
        return other.getX() == x && other.getY() == y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public TargetPoint Transpose(float x, float y){
        return new TargetPoint(this.x + x, this.y + y);
    }
}
