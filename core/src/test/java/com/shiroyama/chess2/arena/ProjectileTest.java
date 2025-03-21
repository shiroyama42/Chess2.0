package com.shiroyama.chess2.arena;

import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {

    private Projectile projectile;
    private Team mockTeam;
    private Team opposingTeam;
    private TargetPoint startPosition;
    private TargetPoint targetPosition;

    @BeforeEach
    void setUp(){
        mockTeam = Team.WHITE;
        opposingTeam = Team.BLACK;
        startPosition = new TargetPoint(1f, 1f);
        targetPosition = new TargetPoint(5f, 5f);
        projectile = new Projectile(mockTeam, startPosition, targetPosition);
    }

    @Test
    void testUpdate() {
        TargetPoint initialTargetPoint = new TargetPoint(projectile.getPosition().getX(), projectile.getPosition().getY());
        projectile.update(1f);

        assertNotEquals(initialTargetPoint.getX(), projectile.getPosition().getX());
        assertNotEquals(initialTargetPoint.getY(), projectile.getPosition().getY());
    }

    @Test
    void testIsOutOfBounds() {
        TargetPoint arenaBonds = new TargetPoint(10f, 10f);
        assertFalse(projectile.isOutOfBounds(arenaBonds));

        projectile.setPosition(new TargetPoint(-1f, -1f));
        assertTrue(projectile.isOutOfBounds(arenaBonds));

        projectile.setPosition(new TargetPoint(11f, 11f));
        assertTrue(projectile.isOutOfBounds(arenaBonds));
    }

    @Test
    void testHit() {
        PieceInfo mockPiece = Mockito.mock(PieceInfo.class);

        Mockito.when(mockPiece.getTeam()).thenReturn(mockTeam);
        Mockito.when(mockPiece.getPosition()).thenReturn(new TargetPoint(1f, 1f));
        assertFalse(projectile.hit(mockPiece));

        Mockito.when(mockPiece.getTeam()).thenReturn(opposingTeam);
        Mockito.when(mockPiece.getPosition()).thenReturn(new TargetPoint(1f, 1f));
        assertTrue(projectile.hit(mockPiece));

        Mockito.when(mockPiece.getPosition()).thenReturn(new TargetPoint(10f, 10f));
        assertFalse(projectile.hit(mockPiece));
    }
}
