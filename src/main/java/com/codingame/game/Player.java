package com.codingame.game;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Group;

public class Player extends AbstractMultiplayerPlayer {
	
	public Group hud;
	
	public static final String PASS_MOVE = "PASS";
	
	@Override
	public int getExpectedOutputLines() {
		return 1;
	}
	
	public Action getMove() throws TimeoutException, NumberFormatException {
		String output = getOutputs().get(0);
		if (output.equals("PASS")) {
			return Action.pass(this);
		}
		String[] split = output.split(" ");
		return new Action(this, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}
}
