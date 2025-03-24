package com.shiroyama.chess2.chessboard.model;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shiroyama.chess2.chessboard.pieces.PieceInfo;
import com.shiroyama.chess2.chessboard.pieces.PieceType;
import com.shiroyama.chess2.chessboard.pieces.Team;
import com.shiroyama.chess2.chessboard.utils.AttackListener;
import com.shiroyama.chess2.chessboard.utils.PromotionListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.logging.FileHandler;

import static org.junit.jupiter.api.Assertions.*;

class ChessBoardTest {

    private ChessBoard chessBoard;
    private HashMap<String, Texture> mockTextures;
    private SpriteBatch mockBatch;
    private AttackListener mockAttackListener;
    private PromotionListener mockPromotionListener;

    @BeforeEach
    void setUp(){
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {
            @Override
            public void create(){}
        }, config);

        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Mockito.mock(GL20.class);

        mockTextures = new HashMap<>();
        mockTextures.put("white-pawn", new Texture(Gdx.files.internal("chess_pieces/white-pawn.png")));
        mockTextures.put("black-pawn", new Texture(Gdx.files.internal("chess_pieces/black-pawn.png")));
        mockTextures.put("white-king", new Texture(Gdx.files.internal("chess_pieces/white-king.png")));
        mockTextures.put("black-king", new Texture(Gdx.files.internal("chess_pieces/black-king.png")));
        mockTextures.put("white-queen", new Texture(Gdx.files.internal("chess_pieces/white-queen.png")));
        mockTextures.put("black-queen", new Texture(Gdx.files.internal("chess_pieces/black-queen.png")));
        mockTextures.put("white-rook", new Texture(Gdx.files.internal("chess_pieces/white-rook.png")));
        mockTextures.put("black-rook", new Texture(Gdx.files.internal("chess_pieces/black-rook.png")));
        mockTextures.put("white-bishop", new Texture(Gdx.files.internal("chess_pieces/white-bishop.png")));
        mockTextures.put("black-bishop", new Texture(Gdx.files.internal("chess_pieces/black-bishop.png")));
        mockTextures.put("white-knight", new Texture(Gdx.files.internal("chess_pieces/white-knight.png")));
        mockTextures.put("black-knight", new Texture(Gdx.files.internal("chess_pieces/black-knight.png")));

        chessBoard = new ChessBoard(400, mockTextures);
        mockBatch = Mockito.mock(SpriteBatch.class);
        mockAttackListener = Mockito.mock(AttackListener.class);
        mockPromotionListener = Mockito.mock(PromotionListener.class);

        chessBoard.setOnAttackListener(mockAttackListener);
        chessBoard.setPromotionListener(mockPromotionListener);
    }

    @Test
    void testInit(){
        assertNotNull(chessBoard.pieces);
        assertEquals(8, chessBoard.pieces.length);
        assertEquals(8, chessBoard.pieces[0].length);

        PieceInfo whiteKing = chessBoard.getPiece(new TargetPoint(3, 7));
        assertNotNull(whiteKing);
        assertEquals(whiteKing.getTeam(), Team.WHITE);
        assertNotEquals(whiteKing.getTeam(), Team.BLACK);
        assertEquals(whiteKing.getPieceType(), PieceType.KING);
        assertNotEquals(whiteKing.getPieceType(), PieceType.PAWN);

        PieceInfo blackQueen = chessBoard.getPiece(new TargetPoint(4, 0));
        assertNotNull(blackQueen);
        assertEquals(blackQueen.getTeam(), Team.BLACK);
        assertNotEquals(blackQueen.getTeam(), Team.WHITE);
        assertEquals(blackQueen.getPieceType(), PieceType.QUEEN);
        assertNotEquals(blackQueen.getPieceType(), PieceType.PAWN);
    }

    @Test
    void testGetPiece() {
        PieceInfo whitePawn = chessBoard.getPiece(new TargetPoint(0, 6));
        assertNotNull(whitePawn);
        assertEquals(Team.WHITE, whitePawn.getTeam());
        assertEquals(PieceType.PAWN, whitePawn.getPieceType());

        PieceInfo emptyField = chessBoard.getPiece(new TargetPoint(4, 4));
        assertNull(emptyField);

        PieceInfo outOfBounds = chessBoard.getPiece(new TargetPoint(9, 9));
        assertNull(outOfBounds);
    }

    @Test
    void testMovePiece() {

        TargetPoint from = new TargetPoint(0, 6);
        TargetPoint to = new TargetPoint(0, 4);
        chessBoard.movePiece(from, to);

        assertNull(chessBoard.getPiece(from));
        assertNotNull(chessBoard.getPiece(to));
        assertEquals(PieceType.PAWN, chessBoard.getPiece(to).getPieceType());
        assertEquals(Team.WHITE, chessBoard.getPiece(to).getTeam());

    }

    @Test
    void testAttack(){
        TargetPoint attackerPos = new TargetPoint(0, 6);
        TargetPoint targetPos = new TargetPoint(1, 5);

        PieceInfo blackPiece = new PieceInfo(Team.BLACK, PieceType.PAWN, targetPos);
        chessBoard.pieces[1][5] = blackPiece;

        chessBoard.movePiece(attackerPos, targetPos);
        Mockito.verify(mockAttackListener).onAttack(Mockito.any(PieceInfo.class), Mockito.eq(blackPiece));
    }

    @Test
    void testPromoting(){
        TargetPoint fromPos = new TargetPoint(0, 1);
        TargetPoint toPos = new TargetPoint(0, 0);
        PieceInfo whitePawn = new PieceInfo(Team.WHITE, PieceType.PAWN, fromPos);

        chessBoard.pieces[0][1] = whitePawn;
        chessBoard.pieces[0][0] = null;

        chessBoard.movePiece(fromPos, toPos);
        Mockito.verify(mockPromotionListener).onPromote(whitePawn);
    }

    @Test
    void testIsInBounds() {
        assertTrue(chessBoard.isInBounds(new TargetPoint(0, 0)));
        assertTrue(chessBoard.isInBounds(new TargetPoint(0, 7)));
        assertTrue(chessBoard.isInBounds(new TargetPoint(4, 4)));
        assertFalse(chessBoard.isInBounds(new TargetPoint(-1, 0)));
        assertFalse(chessBoard.isInBounds(new TargetPoint(8, 0)));
        assertFalse(chessBoard.isInBounds(new TargetPoint(0, -1)));
        assertFalse(chessBoard.isInBounds(new TargetPoint(0, 8)));
    }

    @Test
    void testPromotingFlag(){
        assertFalse(chessBoard.isPromoting());
        chessBoard.setPromoting(true);
        assertTrue(chessBoard.isPromoting());
    }
}
