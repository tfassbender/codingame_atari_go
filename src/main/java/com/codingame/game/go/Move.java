package com.codingame.game.go;

/**
 * @author Tobias
 *
 */
public class Move {
	
	private int row;
	private int col;
	private Type type;
	private PlayerColor color;
	
	public enum Type {
		STONE, PASS;
	}
	
	public Move(int row, int col, PlayerColor color) {
		this.row = row;
		this.col = col;
		this.color = color;
		this.type = Type.STONE;
	}
	
	public static Move getPassMove(PlayerColor color) {
		Move pass = new Move(-1, -1, color);
		pass.setType(Type.PASS);
		return pass;
	}
	
	public FieldPosition getPos() {
		return new FieldPosition(row, col);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + row;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (col != other.col)
			return false;
		if (color != other.color)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	
	public PlayerColor getColor() {
		return color;
	}
	public void setColor(PlayerColor color) {
		this.color = color;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean isPass() {
		return type == Type.PASS;
	}
}