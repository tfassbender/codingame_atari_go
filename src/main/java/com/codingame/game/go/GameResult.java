package com.codingame.game.go;

public class GameResult {
	
	private int pointsBlack;
	private int pointsWhite;
	private int blackStonesCaptured;
	private int whiteStonesCaptured;
	private double comi;
	private double points;
	private PlayerColor winner;
	
	public GameResult() {
		
	}
	
	public GameResult(int pointsBlack, int pointsWhite, int blackStonesCaptured, int whiteStonesCaptured, double comi, double points,
			PlayerColor winner) {
		this.pointsBlack = pointsBlack;
		this.pointsWhite = pointsWhite;
		this.blackStonesCaptured = blackStonesCaptured;
		this.whiteStonesCaptured = whiteStonesCaptured;
		this.comi = comi;
		this.points = points;
		this.winner = winner;
	}
	
	public int getPointsBlack() {
		return pointsBlack;
	}
	
	public void setPointsBlack(int pointsBlack) {
		this.pointsBlack = pointsBlack;
	}
	
	public int getPointsWhite() {
		return pointsWhite;
	}
	
	public void setPointsWhite(int pointsWhite) {
		this.pointsWhite = pointsWhite;
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
	
	public double getComi() {
		return comi;
	}
	
	public void setComi(double comi) {
		this.comi = comi;
	}
	
	public double getPoints() {
		return points;
	}
	
	public void setPoints(double points) {
		this.points = points;
	}
	
	public PlayerColor getWinner() {
		return winner;
	}
	
	public void setWinner(PlayerColor winner) {
		this.winner = winner;
	}
}