package com.shiroyama.chess2.chessboard;

import java.util.Objects;

public class TargetPoint {

    private int x, y;

    public TargetPoint(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TargetPoint that = (TargetPoint) o;
        return x == that.x && y == that.y;
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
