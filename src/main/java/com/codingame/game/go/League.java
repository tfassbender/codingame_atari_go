package com.codingame.game.go;

import com.codingame.game.Player;
import com.codingame.game.Referee;
import com.codingame.gameengine.core.MultiplayerGameManager;

public enum League {
	
	LEAGUE_1(1, 80, 9), //
	LEAGUE_2(2, 200, 9), //
	LEAGUE_3(3, 200, 9), //
	LEAGUE_4(4, 200, 9);
	
	private final int level;
	private final int maxTurns;
	private final int boardSize;
	
	private League(int level, int maxTurns, int boardSize) {
		this.level = level;
		this.maxTurns = maxTurns;
		this.boardSize = boardSize;
	}
	
	public static League getByLevel(int level) {
		for (League league : values()) {
			if (league.getLevel() == level) {
				return league;
			}
		}
		throw new IllegalStateException("Unknown league level: " + level);
	}
	
	public static League getLeague(MultiplayerGameManager<Player> gameManager) {
		return getByLevel(gameManager.getLeagueLevel());
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getMaxTurns() {
		return maxTurns;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public String getBoardImage() {
		return String.format("go_board_%dx%d.png", boardSize, boardSize);
	}
	
	public int getStoneImageSize() {
		return Referee.BOARD_WIDTH / boardSize;
	}
	
	public int getStoneImageGap() {
		return 0;
	}
	
	public int getFirstStoneX() {
		return (Referee.FRAME_WIDTH - Referee.BOARD_WIDTH) / 2 + getStoneImageGap();
	}
	
	public int getFirstStoneY() {
		return (Referee.FRAME_HEIGHT - Referee.BOARD_WIDTH) / 2 + getStoneImageGap();
	}
}
