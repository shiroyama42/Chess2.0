/**
 * The com.shiroyama.chess2.chessboard.utils package provides utility interfaces
 * for handling special events in the Chess2 game.
 *
 * This package contains event listener interfaces that allow components to respond
 * to specific game events such as piece attacks and pawn promotions. These utilities
 * enable loose coupling between the chess board model and components that need to
 * react to game events.
 *
 * <p>Key components include:
 * <ul>
 *   <li>{@link com.shiroyama.chess2.chessboard.utils.AttackListener} - Interface for
 *       handling attack events between chess pieces</li>
 *   <li>{@link com.shiroyama.chess2.chessboard.utils.PromotionListener} - Interface for
 *       handling pawn promotion events when pawns reach the opposite end of the board</li>
 * </ul>
 *
 * <p>The utils package facilitates event-driven communication between different parts
 * of the Chess2 game, allowing for modular design and separation of concerns.
 *
 * @author Vajas Benjámin - shiroyama42
 */
package com.shiroyama.chess2.chessboard.utils;
