import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AbstractPlayer {
	
	protected int boardSize;
	
	protected PlayerColor playerColor;
	
	public enum PlayerColor {
		
		BLACK, WHITE;
	}
	
	protected static class Pos {
		
		int row, col;
		
		public Pos(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	
	public AbstractPlayer() {
		
	}
	
	public void run() {
		Scanner in = new Scanner(System.in);
		
		System.err.println("Reading");
		System.out.println("Reading");
		
		while (true) {
			int opponentRow = in.nextInt();
			int opponentCol = in.nextInt();
			if (opponentRow == -1) {
				opponentRow = 10;
				opponentCol = 10;
			}
			
			System.err.println(opponentRow + " " + opponentCol);
			
			boardSize = in.nextInt();
			System.err.println(boardSize);
			String[] lines = new String[boardSize];
			for (int i = 0; i < boardSize; i++) {
				String line = in.nextLine();
				System.err.println(line);
				lines[i] = line;
			}
			
			PlayerColor[][] board = getBoard(lines);
			
			List<Pos> possible = getPossibleActions(board, PlayerColor.BLACK);
			Pos chosen = possible.get((int) (Math.random() * possible.size()));
			
			System.out.println(String.format("%d %d", chosen.row, chosen.col));
		}
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
	
	protected List<Pos> getPossibleActions(PlayerColor[][] board, PlayerColor myColor) {
		List<Pos> blackStones = new ArrayList<>();
		List<Pos> whiteStones = new ArrayList<>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == PlayerColor.BLACK) {
					blackStones.add(new Pos(i, j));
				}
				if (board[i][j] == PlayerColor.WHITE) {
					whiteStones.add(new Pos(i, j));
				}
			}
		}
		
		List<Pos> opponentStones = null;
		if (myColor == PlayerColor.BLACK) {
			opponentStones = whiteStones;
		}
		if (myColor == PlayerColor.WHITE) {
			opponentStones = blackStones;
		}
		
		return opponentStones.stream().flatMap(pos -> getNear(pos).stream()).filter(pos -> board[pos.row][pos.col] == null)
				.collect(Collectors.toList());
	}
	
	protected List<Pos> getNear(Pos action) {
		List<Pos> fields = new ArrayList<Pos>();
		int[][] nearFields = new int[][] {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
		for (int[] near : nearFields) {
			Pos pos = new Pos(action.row + near[0], action.col + near[1]);
			if (pos.row >= 0 && pos.row < boardSize && pos.col >= 0 && pos.col < boardSize) {
				fields.add(pos);
			}
		}
		
		return fields;
	}
}
