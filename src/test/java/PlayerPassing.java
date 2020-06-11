import java.util.Scanner;

public class PlayerPassing {
	
	protected int boardSize;
	
	protected PlayerColor playerColor;
	
	public enum PlayerColor {
		
		BLACK, WHITE;
	}
	
	protected static class Pos {
		
		int y, x;
		
		public Pos(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static void main(String[] args) {
		new PlayerPassing();
	}
	
	public PlayerPassing() {
		run();
	}
	
	public void run() {
		Scanner in = new Scanner(System.in);
		
		String color = in.nextLine();
		boardSize = in.nextInt();
		
		while (true) {
			int opponentX = in.nextInt();
			int opponentY = in.nextInt();
			
			int myScore = in.nextInt();
			int opponentScore = in.nextInt();
			
			in.nextLine();
			
			String[] lines = new String[boardSize];
			for (int i = 0; i < boardSize; i++) {
				String line = in.nextLine();
				lines[i] = line;
			}
			
			System.out.println("PASS");
		}
	}
}
