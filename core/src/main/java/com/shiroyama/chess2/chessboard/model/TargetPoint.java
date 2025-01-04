package com.shiroyama.chess2.chessboard.model;

public class TargetPoint {

    private int x, y;

    public TargetPoint(int x, int y) {
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TargetPoint Transpose(int x, int y){
        return new TargetPoint(this.x + x, this.y + y);
    }
}
