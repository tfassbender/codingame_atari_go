package com.codingame.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.codingame.game.go.FieldPosition;
import com.codingame.game.go.Game;
import com.codingame.game.go.Group;
import com.codingame.game.go.League;
import com.codingame.game.go.Move;
import com.codingame.game.go.PlayerColor;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
	
	public static final int FRAME_DURATION = 800;
	public static final int FRAME_WIDTH = 1920;
	public static final int FRAME_HEIGHT = 1080;
	public static final int BOARD_WIDTH = 950;
	
	@Inject
	private MultiplayerGameManager<Player> gameManager;
	@Inject
	private GraphicEntityModule graphicEntityModule;
	@Inject
	private EndScreenModule endScreenModule;
	
	private Action lastAction = Action.pass(null);
	
	private Game game;
	
	private PlayerColor[][] board;
	private PlayerColor[][] previousBoard;
	
	private PlayerColor lastMoveColor;
	
	private String moveError;
	
	private int maxTurns;
	
	private List<Sprite> whiteStonesBuffer = new ArrayList<Sprite>(100);
	private List<Sprite> blackStonesBuffer = new ArrayList<Sprite>(100);
	
	private Text textPointsWhite;
	private Text textPointsBlack;
	
	public Referee() {
		this(new Game());
	}
	
	public Referee(Game game) {
		this.game = game;
	}
	
	// ***************************************************************************************************************
	// *** CG stuff
	// ***************************************************************************************************************
	
	@Override
	public void init() {
		drawBackground();
		drawPointsInit();
		drawBoardBackground();
		drawHud();
		
		League league = League.getLeague(gameManager);
		maxTurns = league.getMaxTurns();
		gameManager.setFrameDuration(FRAME_DURATION);
		gameManager.setMaxTurns(league.getMaxTurns());
		
		gameManager.setFirstTurnMaxTime(1000);
		gameManager.setTurnMaxTime(100);
		
		board = new PlayerColor[league.getBoardSize()][league.getBoardSize()];
		
		game.setBoardSize(league.getBoardSize());
		
		gameManager.getPlayer(0).sendInputLine("B");
		gameManager.getPlayer(1).sendInputLine("W");
		gameManager.getPlayer(0).sendInputLine(Integer.toString(league.getBoardSize()));
		gameManager.getPlayer(1).sendInputLine(Integer.toString(league.getBoardSize()));
		
		gameManager.registerModule(endScreenModule);
	}
	
	private void drawBackground() {
		graphicEntityModule.createSprite().setImage("background.jpg").setAnchor(0).setBaseWidth(FRAME_WIDTH).setBaseHeight(FRAME_HEIGHT);
		graphicEntityModule.createSprite().setImage("logoCG.png").setX(FRAME_WIDTH - 280).setY(960).setAnchor(0.5);
	}
	
	private void drawBoardBackground() {
		graphicEntityModule.createSprite().setImage(League.getLeague(gameManager).getBoardImage()).setBaseWidth(BOARD_WIDTH)
				.setBaseHeight(BOARD_WIDTH).setX((FRAME_WIDTH - BOARD_WIDTH) / 2).setY((FRAME_HEIGHT - BOARD_WIDTH) / 2);
	}
	
	private void drawHud() {
		for (Player player : gameManager.getPlayers()) {
			int x = player.getIndex() == 0 ? 200 : FRAME_WIDTH - 200;
			int y = 160;
			
			graphicEntityModule.createRectangle().setWidth(140).setHeight(140).setX(x - 70).setY(y - 70).setLineWidth(0)
					.setFillColor(player.getColorToken());
			graphicEntityModule.createRectangle().setWidth(120).setHeight(120).setX(x - 60).setY(y - 60).setLineWidth(0).setFillColor(0xffffff);
			Text text = graphicEntityModule.createText(player.getNicknameToken()).setFontFamily("arial").setX(x).setY(y + 120).setZIndex(20)
					.setFontSize(40).setFillColor(0xffffff).setAnchor(0.5);
			Sprite avatar = graphicEntityModule.createSprite().setX(x).setY(y).setZIndex(20).setImage(player.getAvatarToken()).setAnchor(0.5)
					.setBaseHeight(116).setBaseWidth(116);
			player.hud = graphicEntityModule.createGroup(text, avatar);
		}
	}
	
	private void updateBoard() {
		blackStonesBuffer.forEach(sprite -> sprite.setVisible(false));
		whiteStonesBuffer.forEach(sprite -> sprite.setVisible(false));
		
		int blackStoneSpritesUsed = 0;
		int whiteStoneSpritesUsed = 0;
		
		int boardSize = League.getLeague(gameManager).getBoardSize();
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (board[row][col] != null) {
					Sprite stone;
					if (board[row][col] == PlayerColor.BLACK) {
						if (blackStonesBuffer.size() > blackStoneSpritesUsed) {
							stone = blackStonesBuffer.get(blackStoneSpritesUsed);
						}
						else {
							stone = graphicEntityModule.createSprite().setImage("go_stone_black.png");
							blackStonesBuffer.add(stone);
						}
						blackStoneSpritesUsed++;
					}
					else if (board[row][col] == PlayerColor.WHITE) {
						if (whiteStonesBuffer.size() > whiteStoneSpritesUsed) {
							stone = whiteStonesBuffer.get(whiteStoneSpritesUsed);
						}
						else {
							stone = graphicEntityModule.createSprite().setImage("go_stone_white.png");
							whiteStonesBuffer.add(stone);
						}
						whiteStoneSpritesUsed++;
					}
					else {
						throw new IllegalStateException("unknown player color on board: " + board[row][col]);
					}
					League league = League.getLeague(gameManager);
					stone.setBaseWidth(league.getStoneImageSize()).setBaseHeight(league.getStoneImageSize());
					stone.setX(league.getFirstStoneX() + (league.getStoneImageSize() + league.getStoneImageGap()) * col);
					stone.setY(league.getFirstStoneY() + (league.getStoneImageSize() + league.getStoneImageGap()) * row);
					
					stone.setVisible(true);
					graphicEntityModule.commitEntityState(0, stone);
				}
			}
		}
	}
	private void drawPointsInit() {
		final int x = 250;
		final int y = 500;
		final int capturedFontSize = 60;
		final int textColor = 0x000000;
		final int stoneSize = 100;
		final int stoneX = -75;
		final int stoneY = 100;
		final int stonesFontSize = 100;
		final int textX = 75;
		
		graphicEntityModule.createSprite().setImage("background_points.png").setAnchor(0).setBaseWidth(FRAME_WIDTH).setBaseHeight(FRAME_HEIGHT);
		graphicEntityModule.createText("Captured:").setFontFamily("arial").setX(x).setY(y).setZIndex(20).setFontSize(capturedFontSize)
				.setFillColor(textColor).setAnchor(0.5);
		graphicEntityModule.createText("Captured:").setFontFamily("arial").setX(FRAME_WIDTH - x).setY(y).setZIndex(20).setFontSize(capturedFontSize)
				.setFillColor(textColor).setAnchor(0.5);
		graphicEntityModule.createSprite().setImage("go_stone_white.png").setX(x + stoneX).setY(y + stoneY).setBaseWidth(stoneSize)
				.setBaseHeight(stoneSize).setAnchor(0.5);
		graphicEntityModule.createSprite().setImage("go_stone_black.png").setX(FRAME_WIDTH - x - stoneX).setY(y + stoneY).setBaseWidth(stoneSize)
				.setBaseHeight(stoneSize).setAnchor(0.5);
		textPointsWhite = graphicEntityModule.createText(Integer.toString(game.getWhiteStonesCaptured())).setFontFamily("arial").setX(x + textX)
				.setY(y + stoneY).setFontSize(stonesFontSize).setFillColor(textColor).setAnchor(0.5);
		textPointsBlack = graphicEntityModule.createText(Integer.toString(game.getBlackStonesCaptured())).setFontFamily("arial")
				.setX(FRAME_WIDTH - x - textX).setY(y + stoneY).setFontSize(stonesFontSize).setFillColor(textColor).setAnchor(0.5);
	}

	private void updatePoints() {
		textPointsWhite.setText(Integer.toString(game.getWhiteStonesCaptured()));
		textPointsBlack.setText(Integer.toString(game.getBlackStonesCaptured()));
	}
	
	private void sendInputs(Player player, Player opponent) {
		// last action
		if (lastAction != null) {
			player.sendInputLine(lastAction.toString());
		}
		else {
			player.sendInputLine("-1 -1");
		}
		
		// current points
		player.sendInputLine(Integer.toString(player.getScore()) + " " + Integer.toString(opponent.getScore()));
		
		// current board
		int boardSize = League.getLeague(gameManager).getBoardSize();
		for (int i = 0; i < boardSize; i++) {
			String line = Arrays.stream(board[i]).map(PlayerColor::toCharCode).collect(Collectors.joining());
			player.sendInputLine(line);
		}
	}
	
	@Override
	public void gameTurn(int turn) {
		Player player = gameManager.getPlayer((turn - 1) % gameManager.getPlayerCount());
		Player opponent = gameManager.getPlayer(turn % gameManager.getPlayerCount());
		sendInputs(player, opponent);
		player.execute();
		
		// Read inputs
		try {
			final Action action = player.getMove();
			final Move move;
			if (action.pass) {
				move = Move.getPassMove(getPlayerColor(action.player));
			}
			else {
				move = new Move(action.col, action.row, getPlayerColor(action.player));
			}
			gameManager.addToGameSummary(String.format("Player %s played (%d %d)", action.player.getNicknameToken(), action.row, action.col));
			
			moveError = null;
			if (!isValidMove(move)) {
				throw new InvalidAction("Invalid action!");
			}
			lastAction = action;
			
			addMove(move);
			
			Player player1 = gameManager.getPlayer(0);
			Player player2 = gameManager.getPlayer(1);
			player1.setScore(game.getWhiteStonesCaptured());
			player2.setScore(game.getBlackStonesCaptured());
			
			if (turn == maxTurns && player1.getScore() == 0 && player2.getScore() == 0) {
				// no player has captured any stones -> player with more groups wins
				int stonesPlayer1 = countStonesOnBoard(getPlayerColor(player1));
				int stonesPlayer2 = countStonesOnBoard(getPlayerColor(player2));
				if (stonesPlayer1 > stonesPlayer2) {
					player1.setScore(player1.getScore() + 1);
				}
				else if (stonesPlayer1 < stonesPlayer2) {
					player2.setScore(player2.getScore() + 1);
				}
			}
			
			updatePoints();
			updateBoard();
		}
		catch (NumberFormatException e) {
			player.deactivate("Wrong output!");
			player.setScore(-1);
			endGame();
		}
		catch (TimeoutException e) {
			gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
			player.deactivate(player.getNicknameToken() + " timeout!");
			player.setScore(-1);
			endGame();
		}
		catch (InvalidAction e) {
			String deactivateMessage = e.getMessage();
			if (moveError != null) {
				deactivateMessage = moveError;
			}
			player.deactivate(deactivateMessage);
			player.setScore(-1);
			endGame();
		}
	}
	
	private void endGame() {
		Player winner = null;
		Player looser = null;
		
		Player p0 = gameManager.getPlayers().get(0);
		Player p1 = gameManager.getPlayers().get(1);
		
		if (p0.getScore() > p1.getScore()) {
			winner = p0;
			looser = p1;
		}
		else if (p0.getScore() < p1.getScore()) {
			winner = p1;
			looser = p0;
		}
		
		if (winner != null && looser != null) {
			looser.hud.setAlpha(0.3);
			gameManager.addToGameSummary(GameManager.formatSuccessMessage(winner.getNicknameToken() + " won!"));
		}
		
		gameManager.endGame();
	}
	
	@Override
	public void onEnd() {
		Player p0 = gameManager.getPlayers().get(0);
		Player p1 = gameManager.getPlayers().get(1);
		int[] scores = new int[] {p0.getScore(), p1.getScore()};
		String[] texts = new String[] {p0.getScore() + " stones captured", p1.getScore() + " stones captured"};
		endScreenModule.setScores(scores, texts);
		endScreenModule.setTitleRankingsSprite("logo.png");
	}
	
	public PlayerColor getPlayerColor(Player player) {
		if (player.equals(gameManager.getPlayer(0))) {
			return PlayerColor.BLACK;
		}
		else {
			return PlayerColor.WHITE;
		}
	}
	
	// ***************************************************************************************************************
	// *** game methods
	// ***************************************************************************************************************
	
	public static PlayerColor[][] getBoard(String[] lines) {
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
	
	/**
	 * Execute all moves in the list to create the current board
	 */
	@VisibleForTesting
	/*private*/ void fillBoard(List<Move> moves) throws IllegalArgumentException {
		if (moves != null) {
			for (Move move : moves) {
				if (isValidMove(move)) {
					addMove(move);
				}
				else {
					throw new IllegalArgumentException("Invalid move. The given list of moves contains a move that is not valid: " + move);
				}
			}
		}
	}
	
	/**
	 * A deep copy of the current board
	 */
	public PlayerColor[][] getBoardCopy() {
		PlayerColor[][] newBoard = new PlayerColor[getBoardSize()][getBoardSize()];
		for (int i = 0; i < getBoardSize(); i++) {
			for (int j = 0; j < getBoardSize(); j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
	/**
	 * Check whether the move is valid
	 * 
	 * Checked are:
	 * <ul>
	 * <li>color (players turn)</li>
	 * <li>position (on field and empty)</li>
	 * <li>no suicidal move (placed stone is not directly beaten)</li>
	 * <li>ko rule (move doesn't create the same board that was there in the last move)</li>
	 * </ul>
	 */
	public boolean isValidMove(Move move) {
		if (move.isPass()) {
			//passing is always a valid move
			return true;
		}
		if (!move.getPos().exists(getBoardSize())) {
			//position doesn't exist
			moveError = "Position doesn't exist: " + move.getCol() + " " + move.getCol();
			return false;
		}
		if (getStoneColor(move.getPos()) != null) {
			//field not empty
			moveError = "Position is not empty: " + move.getCol() + " " + move.getRow();
			return false;
		}
		
		//execute the move and check whether the state is valid
		PlayerColor[][] tmpBoard = getBoardCopy();//keep a copy of the board for a rollback
		//make the move
		board[move.getRow()][move.getCol()] = move.getColor();
		removeBeaten(move);
		
		//check the group of the new stone
		Group group = Group.findGroup(this, move.getPos());
		if (group.isBeaten(this)) {
			//added stone is directly beaten
			moveError = "Suicidal move";
			board = tmpBoard;
			return false;
		}
		
		if (!lastAction.pass && boardsEqual(board, previousBoard)) {
			//restores the board from the last move (ko rule violated)
			moveError = "Ko-Rule violation";
			board = tmpBoard;
			return false;
		}
		
		board = tmpBoard;
		return true;
	}
	
	@VisibleForTesting
	/*private*/ static boolean boardsEqual(PlayerColor[][] board, PlayerColor[][] previousBoard) {
		if (previousBoard == null) {
			//previousBoard can be null if there was no previous move
			//in this case the ko-rule can't be violated and the boards are treated as not equal (so false is returned)
			return false;
		}
		boolean equal = true;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				equal &= board[i][j] == previousBoard[i][j];
			}
		}
		return equal;
	}
	
	/**
	 * Execute a move without checking whether it's valid. The new stone is added and beaten ones are removed
	 */
	public void addMove(Move move) {
		if (!move.isPass()) {
			//copy the board to the previous board field
			previousBoard = getBoardCopy();
			
			//set the new stone
			board[move.getRow()][move.getCol()] = move.getColor();
			//remove beaten stones (if any)
			removeBeaten(move, true);
		}
	}
	
	/**
	 * Remove all stones that were beaten by the move
	 */
	@VisibleForTesting
	/*private*/ void removeBeaten(Move move) {
		removeBeaten(move, false);
	}
	private void removeBeaten(Move move, boolean countBeaten) {
		//find the fields near to the move field
		List<FieldPosition> near = move.getPos().getFieldsNear(game.getBoardSize());
		for (FieldPosition pos : near) {
			//check whether the field has a stone of the different color on it
			if (getStoneColor(pos) == PlayerColor.getOpposizeColor(move.getColor())) {
				Group group = Group.findGroup(this, pos);
				if (group.isBeaten(this)) {
					removeStones(group.getStones(), countBeaten);
				}
			}
		}
	}
	
	/**
	 * Remove all stones in the collection from the field
	 */
	@VisibleForTesting
	/*private*/ void removeStones(Collection<FieldPosition> stones) {
		removeStones(stones, false);
	}
	private void removeStones(Collection<FieldPosition> stones, boolean countBeaten) {
		int stonesBeaten = stones.size();
		PlayerColor beatenStonesColor = null;
		for (FieldPosition pos : stones) {
			beatenStonesColor = board[pos.getRow()][pos.getCol()];
			board[pos.getRow()][pos.getCol()] = null;
		}
		
		if (countBeaten) {
			if (beatenStonesColor == PlayerColor.BLACK) {
				game.setBlackStonesCaptured(game.getBlackStonesCaptured() + stonesBeaten);
			}
			else if (beatenStonesColor == PlayerColor.WHITE) {
				game.setWhiteStonesCaptured(game.getWhiteStonesCaptured() + stonesBeaten);
			}
		}
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
	
	public PlayerColor getLastMoveColor() {
		return lastMoveColor;
	}
	public PlayerColor getNextMoveColor() {
		return PlayerColor.getOpposizeColor(lastMoveColor);
	}
	
	public boolean isFieldEmpty(FieldPosition pos) {
		return board[pos.getRow()][pos.getCol()] == null;
	}
	
	public PlayerColor getStoneColor(FieldPosition pos) {
		return board[pos.getRow()][pos.getCol()];
	}
	public int getBoardSize() {
		return game.getBoardSize();
	}
	
	@VisibleForTesting
	/*private*/ void setBoard(PlayerColor[][] board) {
		this.board = board;
	}
	@VisibleForTesting
	/*private*/ void setPlayersTurn(PlayerColor playersTurn) {
		this.lastMoveColor = PlayerColor.getOpposizeColor(playersTurn);
	}
	@VisibleForTesting
	/*private*/ void setPreviouseBoard(PlayerColor[][] board) {
		this.previousBoard = board;
	}
}
