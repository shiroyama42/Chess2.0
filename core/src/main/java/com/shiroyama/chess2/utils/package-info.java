/**
 * The com.shiroyama.chess2.utils package provides utility classes for various game functions
 * in the Chess2 game.
 *
 * This package contains utility classes that handle configuration management, texture loading,
 * piece movement, and game statistics tracking. These utilities support core game functionality
 * while maintaining separation from the main game logic.
 *
 * <p>Key components include:
 * <ul>
 *   <li>{@link com.shiroyama.chess2.utils.ConfigurationManager} - Manages game settings including
 *       piece stats (HP and attack rate) and graphical preferences</li>
 *   <li>{@link com.shiroyama.chess2.utils.TextureLoader} - Handles loading and caching of chess
 *       piece textures for both teams</li>
 *   <li>{@link com.shiroyama.chess2.utils.PieceMovementHandler} - Controls piece movement within
 *       screen boundaries with appropriate speed calculations</li>
 *   <li>{@link com.shiroyama.chess2.utils.ScoreBoardManager} - Tracks game statistics including
 *       move counts and captured pieces for each team</li>
 * </ul>
 *
 * <p>The utils package provides essential support services that are used throughout the
 * Chess2 application to improve code organization and reusability.
 *
 * @author Vajas Benjámin - shiroyama42
 */
package com.shiroyama.chess2.utils;
