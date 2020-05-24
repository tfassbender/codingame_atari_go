package com.codingame.game.go;

public class GameState {
	
	private PlayerColor[][] state;
	private PlayerColor playersTurn;
	private int boardSize;
	private boolean over;
	private double points;
	private double comi;
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
	
	public GameState(PlayerColor[][] state, PlayerColor playersTurn, int boardSize, boolean over, double points, double comi,
			int blackStonesCaptured, int whiteStonesCaptured) {
		this.state = state;
		this.playersTurn = playersTurn;
		this.boardSize = boardSize;
		this.over = over;
		this.points = points;
		this.comi = comi;
		this.blackStonesCaptured = blackStonesCaptured;
		this.whiteStonesCaptured = whiteStonesCaptured;
	}
	
	public PlayerColor[][] getState() {
		return state;
	}
	
	public void setState(PlayerColor[][] state) {
		this.state = state;
	}
	
	public PlayerColor getPlayersTurn() {
		return playersTurn;
	}
	
	public void setPlayersTurn(PlayerColor playersTurn) {
		this.playersTurn = playersTurn;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}
	
	public boolean isOver() {
		return over;
	}
	
	public void setOver(boolean over) {
		this.over = over;
	}
	
	public double getPoints() {
		return points;
	}
	
	public void setPoints(double points) {
		this.points = points;
	}
	
	public double getComi() {
		return comi;
	}
	
	public void setComi(double comi) {
		this.comi = comi;
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
}