package com.codingame.game.go;

import java.util.ArrayList;
import java.util.List;

import com.codingame.game.Referee;

public class Game {
	
	private String moves;
	private List<Move> moveList;
	
	private int boardSize;
	
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
	
	private static final String MOVE_STRING_DELEMITER = ";";
	private static final String MOVE_STRING_NUMBER_DELEMITER = ",";
	private static final char MOVE_STRING_WHITE = 'W';
	private static final char MOVE_STRING_BLACK = 'B';
	private static final char MOVE_STRING_PASS = 'P';
	
	/**
	 * Build a list of moves from a move string.
	 * 
	 * Examples for move strings:
	 * <ul>
	 * <li>W0,5; (white on row 0, col 5)</li>
	 * <li>B5,18; (black on row 5, col 18)</li>
	 * <li>WP; (white passed)</li>
	 * </ul>
	 */
	public static List<Move> fromMoveString(String moveString) {
		List<Move> moveList = new ArrayList<Move>(moveString.length() / 5);//move strings have a minimum of 5 chars
		String[] moves = moveString.split(MOVE_STRING_DELEMITER);
		for (int i = 0; i < moves.length; i++) {
			if (moves[i].length() > 0) {//prevent empty moves (because the move string could end with ';')
				Move move = null;
				//get the player that made the move
				PlayerColor color = null;
				if (moves[i].charAt(0) == MOVE_STRING_BLACK) {
					color = PlayerColor.BLACK;
				}
				else if (moves[i].charAt(0) == MOVE_STRING_WHITE) {
					color = PlayerColor.WHITE;
				}
				else {
					throw new IllegalStateException("Unexpected player color char in movement code: '" + moves[i].charAt(0) + "'");
				}
				
				//get the row and column of the move
				String fieldCode = moves[i].substring(1);
				String[] fields = fieldCode.split(MOVE_STRING_NUMBER_DELEMITER);
				int row = Integer.parseInt(fields[0]);
				int col = Integer.parseInt(fields[1]);
				
				move = new Move(row, col, color);
				
				//add the new move
				moveList.add(move);
			}
		}
		return moveList;
	}
	
	public List<Move> getMovesAsList() {
		if (moveList == null) {
			if (moves == null) {
				moves = "";
			}
			moveList = fromMoveString(moves);
		}
		return moveList;
	}
	
	public void startGame(int boardSize) {
		this.boardSize = boardSize;
	}
	
	/**
	 * Check whether the move is valid and execute it if it's valid.
	 */
	public void makeMove(Referee referee, Move move) throws IllegalArgumentException {
		if (referee.isValidMove(move)) {
			addMove(move);
		}
		else {
			throw new IllegalArgumentException("the move is not valid");
		}
	}
	
	/**
	 * Add a move without checking whether it's valid.
	 */
	public void addMove(Move move) {
		if (moveList == null) {
			moveList = new ArrayList<Move>();
		}
		moveList.add(move);
		moves += toMoveString(move);
	}
	
	private String toMoveString(Move move) {
		StringBuilder sb = new StringBuilder();
		if (move.getColor() == PlayerColor.BLACK) {
			sb.append(MOVE_STRING_BLACK);
		}
		else {
			sb.append(MOVE_STRING_WHITE);
		}
		sb.append(move.getRow());
		sb.append(MOVE_STRING_NUMBER_DELEMITER);
		sb.append(move.getCol());
		sb.append(MOVE_STRING_DELEMITER);
		
		return sb.toString();
	}
	
	public String getMoves() {
		return moves;
	}
	
	public void setMoves(String moves) {
		this.moves = moves;
	}
	
	public List<Move> getMoveList() {
		return moveList;
	}
	
	public void setMoveList(List<Move> moveList) {
		this.moveList = moveList;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}
	
	public int getBlackStonesCaptured() {
		return blackStonesCaptured;
	}
	
	public void setBlackStonesCaptured(int blackStonesCaptured) {
		this.blackStonesCaptured = blackStonesCaptured;
	}
	
	public int getWhiteStonesCaptured() {
		return whiteStonesCaptured;
	}
	
	public void setWhiteStonesCaptured(int whiteStonesCaptured) {
		this.whiteStonesCaptured = whiteStonesCaptured;
	}
	
	public static String getMoveStringDelemiter() {
		return MOVE_STRING_DELEMITER;
	}
	
	public static String getMoveStringNumberDelemiter() {
		return MOVE_STRING_NUMBER_DELEMITER;
	}
	
	public static char getMoveStringWhite() {
		return MOVE_STRING_WHITE;
	}
	
	public static char getMoveStringBlack() {
		return MOVE_STRING_BLACK;
	}
	
	public static char getMoveStringPass() {
		return MOVE_STRING_PASS;
	}
}