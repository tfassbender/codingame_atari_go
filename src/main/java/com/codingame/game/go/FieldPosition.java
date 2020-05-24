package com.codingame.game.go;

import java.util.ArrayList;
import java.util.List;

public class FieldPosition {
	
	private int row;
	private int col;
	
	public FieldPosition(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
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
		FieldPosition other = (FieldPosition) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	public List<FieldPosition> getFieldsNear(int boardSize) {
		int[][] near = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		List<FieldPosition> nearFields = new ArrayList<FieldPosition>();
		for (int[] nearDiff : near) {
			FieldPosition position = new FieldPosition(row + nearDiff[0], col + nearDiff[1]);
			if (position.exists(boardSize)) {
				nearFields.add(position);
			}
		}
		return nearFields;
	}
	
	public boolean exists(int boardSize) {
		return row >= 0 && col >= 0 && row < boardSize && col < boardSize;
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
}