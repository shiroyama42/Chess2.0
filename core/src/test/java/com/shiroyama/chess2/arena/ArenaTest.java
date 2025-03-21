package com.shiroyama.chess2.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.shiroyama.chess2.chessboard.model.TargetPoint;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArenaTest {

    private Arena arena;
    private PieceInfo mockAttacker;
    private PieceInfo mockDefender;

    @BeforeEach
    void setUp() {
        mockAttacker = Mockito.mock(PieceInfo.class);
        mockDefender = Mockito.mock(PieceInfo.class);
        arena = new Arena(mockAttacker, mockDefender);

        TargetPoint attackerPosition = new TargetPoint(1, 1);
        TargetPoint defenderPosition = new TargetPoint(2, 2);
        Mockito.when(mockAttacker.getPosition()).thenReturn(attackerPosition);
        Mockito.when(mockDefender.getPosition()).thenReturn(defenderPosition);

        Gdx.graphics = Mockito.mock(Graphics.class);
        Mockito.when(Gdx.graphics.getWidth()).thenReturn(1000);
        Mockito.when(Gdx.graphics.getHeight()).thenReturn(1000);
    }

    @Test
    void testUpdate() {
        arena.startCombat();
        Mockito.when(mockAttacker.getAttackRate()).thenReturn(1f);
        Mockito.when(mockDefender.getAttackRate()).thenReturn(1f);
        Mockito.when(mockAttacker.getHp()).thenReturn(5);
        Mockito.when(mockDefender.getHp()).thenReturn(5);

        arena.update(1f);

        Mockito.verify(mockAttacker).shoot(Mockito.any(TargetPoint.class), Mockito.anyList());
        Mockito.verify(mockDefender).shoot(Mockito.any(TargetPoint.class), Mockito.anyList());
    }

    @Test
    void testStartCombat() {
        arena.startCombat();
        assertTrue(arena.isCombatOver());
    }

    @Test
    void testIsCombatOver() {
        Mockito.when(mockAttacker.getHp()).thenReturn(0);
        Mockito.when(mockDefender.getHp()).thenReturn(5);
        assertTrue(arena.isCombatOver());

        Mockito.when(mockAttacker.getHp()).thenReturn(5);
        Mockito.when(mockDefender.getHp()).thenReturn(0);
        assertTrue(arena.isCombatOver());

        Mockito.when(mockDefender.getHp()).thenReturn(5);
        Mockito.when(mockAttacker.getHp()).thenReturn(5);
        assertFalse(arena.isCombatOver());
    }

    @Test
    void testAttackerWon() {
        Mockito.when(mockDefender.getHp()).thenReturn(0);
        assertTrue(arena.attackerWon());

        Mockito.when(mockDefender.getHp()).thenReturn(5);
        assertFalse(arena.attackerWon());
    }

    @Test
    void testGetProjectiles() {
        List<Projectile> projectiles = arena.getProjectiles();
        assertNotNull(projectiles);
        assertTrue(projectiles.isEmpty());
    }
}
