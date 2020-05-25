import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Player {
	
	public int boardSize;
	
	public PlayerColor myColor;
	public PlayerColor[][] board;
	
	protected static class Pos {
		
		int y, x;
		
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
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
	}
	
	public static void main(String[] args) {
		new Player();
	}
	
	public Player() {
		run();
	}
	
	public void run() {
		Scanner in = new Scanner(System.in);
		
		String color = in.nextLine();
		
		if (color.equals("B")) {
			myColor = PlayerColor.BLACK;
		}
		else if (color.equals("W")) {
			myColor = PlayerColor.WHITE;
		}
		else {
			throw new IllegalStateException("Unknown color: " + color);
		}
		
		System.err.println("Color: " + myColor);
		
		while (true) {
			int opponentX = in.nextInt();
			int opponentY = in.nextInt();
			
			int myScore = in.nextInt();
			int opponentScore = in.nextInt();
			
			boardSize = in.nextInt();
			//System.err.println("board size set: " + boardSize);
			in.nextLine();
			
			String[] lines = new String[boardSize];
			for (int i = 0; i < boardSize; i++) {
				String line = in.nextLine();
				lines[i] = line;
			}
			
			board = getBoard(lines);
			
			List<Pos> possible = getPossibleActions(board, myColor, 0.1);
			if (possible.isEmpty()) {
				if (countStonesOnBoard(myColor) > 0) {
					System.out.println("PASS");
					return;
				}
				possible.add(new Pos((int) (Math.random() * 9), (int) (Math.random() * 9)));
			}
			Pos chosen = possible.get((int) (Math.random() * possible.size()));
			
			System.out.println(String.format("%d %d", chosen.x, chosen.y));
		}
	}
	
	public PlayerColor[][] getBoard() {
		return board;
	}
	
	public int getBoardSize() {
		return boardSize;
	}
	
	public PlayerColor[][] getBoard(String[] lines) {
		int size = lines.length;
		PlayerColor[][] board = new PlayerColor[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (lines[i].charAt(j) == 'B') {
					board[i][j] = PlayerColor.BLACK;
				}
				else if (lines[i].charAt(j) == 'W') {
					board[i][j] = PlayerColor.WHITE;
				}
			}
		}
		return board;
	}
	
	public List<Pos> getPossibleActions(PlayerColor[][] board, PlayerColor myColor, double addRandom) {
		List<Pos> blackStones = new ArrayList<>();
		List<Pos> whiteStones = new ArrayList<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == PlayerColor.BLACK) {
					blackStones.add(new Pos(j, i));
				}
				if (board[i][j] == PlayerColor.WHITE) {
					whiteStones.add(new Pos(j, i));
				}
			}
		}
		
		List<Pos> stones = null;
		stones = whiteStones;
		stones.addAll(blackStones);
		
		List<Pos> possibilities = stones.stream().flatMap(pos -> getNear(pos).stream()).filter(pos -> board[pos.y][pos.x] == null)
				.filter(pos -> !mightBeSuicidal(pos)).collect(Collectors.toList());
		int random = (int) (possibilities.size() * addRandom);
		for (int i = 0; i < random; i++) {
			Pos pos = new Pos((int) (Math.random() * boardSize), (int) (Math.random() * boardSize));
			if (board[pos.y][pos.x] == null) {
				possibilities.add(pos);
			}
		}
		
		return possibilities;
	}
	
	private boolean mightBeSuicidal(Pos pos) {
		List<Pos> near = getNear(pos);
		int stonesNear = (int) near.stream().filter(p -> board[p.y][p.x] != null).count();
		if (stonesNear >= 4) {
			return true;
		}
		else {
			int edge = 0;
			if (pos.x == 0 || pos.x == boardSize - 1) {
				edge++;
			}
			if (pos.y == 0 || pos.y == boardSize - 1) {
				edge++;
			}
			return stonesNear + edge >= 4;
		}
	}
	
	public List<Pos> getNear(Pos action) {
		List<Pos> fields = new ArrayList<Pos>();
		int[][] nearFields = new int[][] {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
		for (int[] near : nearFields) {
			Pos pos = new Pos(action.x + near[0], action.y + near[1]);
			if (pos.y >= 0 && pos.y < boardSize && pos.x >= 0 && pos.x < boardSize) {
				fields.add(pos);
			}
		}
		
		return fields;
	}
	
	private int countStonesOnBoard(PlayerColor color) {
		int stones = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == color) {
					stones++;
				}
			}
		}
		return stones;
	}
}
