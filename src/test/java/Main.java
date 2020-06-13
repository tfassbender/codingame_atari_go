import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
	
	public static void main(String[] args) {
		
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
		gameRunner.addAgent(Player.class);
		gameRunner.addAgent(Player.class);
		
		// The first league is classic tic-tac-toe
		gameRunner.setLeagueLevel(3);
		// The second league is ultimate tic-tac-toe
		//gameRunner.setLeagueLevel(2);
		
		gameRunner.start();
	}
}
