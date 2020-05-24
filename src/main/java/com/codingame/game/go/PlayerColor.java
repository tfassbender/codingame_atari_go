package com.codingame.game.go;

public enum PlayerColor {
	
	BLACK, WHITE;
	
	public static PlayerColor getOpposizeColor(PlayerColor lastMove) {
		switch (lastMove) {
			case BLACK:
				return WHITE;
			case WHITE:
				return BLACK;
			default:
				return null;
		}
	}
	
	public static String toCharCode(PlayerColor color) {
		if (color == BLACK) {
			return "B";
		}
		if (color == WHITE) {
			return "W";
		}
		return "."; 
	}
}