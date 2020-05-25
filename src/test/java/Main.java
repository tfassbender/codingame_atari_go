import com.codingame.gameengine.runner.MultiplayerGameRunner;

public class Main {
	
	public static void main(String[] args) {
		
		MultiplayerGameRunner gameRunner = new MultiplayerGameRunner();
		gameRunner.addAgent(PlayerPassing.class);
		gameRunner.addAgent(Player.class);
		
		// The first league is classic tic-tac-toe
		gameRunner.setLeagueLevel(1);
		// The second league is ultimate tic-tac-toe
		//gameRunner.setLeagueLevel(2);
		
		gameRunner.start();
	}
}
