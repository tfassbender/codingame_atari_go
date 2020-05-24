import java.util.Scanner;

public class Player3 {
	
	static class Action {
		
		int row, col;
		
		public Action(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		while (true) {
			System.out.println("10 10");
			System.err.println(in.nextLine());
			int opponentRow = in.nextInt();
			int opponentCol = in.nextInt();
			if (opponentRow == -1) {
				opponentRow = 10;
				opponentCol = 10;
			}
			
			System.err.println(opponentRow + " " + opponentCol);
			
			int boardSize = in.nextInt();
			System.err.println(boardSize);
			String[] lines = new String[boardSize];
			for (int i = 0; i < boardSize; i++) {
				String line = in.nextLine();
				System.err.println(line);
				lines[i] = line;
			}
		}
	}
}
