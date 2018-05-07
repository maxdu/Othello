package cc.max.Othello;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import cc.max.Othello.pojo.Direction;
import cc.max.Othello.pojo.Move;
import cc.max.Othello.pojo.Side;

public class Controller {

	public static final Logger logger = (Logger) LogManager.getLogger(Controller.class);

	private Side currentPlayer = null;
	private int demension = -1;

	public static boolean noMoveAvaialbeOwen = true;
	public static boolean noMoveAvaiableXray = true;

	private List<Direction> directions = new ArrayList<Direction>();
	private boolean[][] slotsTaken = null;
	private List<Move> possibleMoveSlots = new ArrayList<Move>();
	private List<Move> occupiedMoves = new ArrayList<Move>();
	private List<Move> updateOccupiedMoves = new ArrayList<Move>();
	private Move lastMove = null;

	public Controller(Side startPlayer, int demension) throws Exception {

		if (demension < 4 || demension > 8 || demension % 2 != 0) {
			throw new Exception("Board size must be between 4 and 8 and must be even");
		}

		this.setDemension(demension);

		// Initialize slotsTaken
		slotsTaken = new boolean[this.getDemension()][this.getDemension()];
		for (int x = 0; x < this.getDemension(); x++) {
			for (int y = 0; y < this.getDemension(); y++) {
				slotsTaken[x][y] = false;
			}
		}

		// Set startSide
		currentPlayer = startPlayer;

		// 8 possible directions
		directions.add(new Direction(0, -1, "up"));
		directions.add(new Direction(0, 1, "down"));
		directions.add(new Direction(-1, 0, "left"));
		directions.add(new Direction(1, 0, "right"));
		directions.add(new Direction(1, -1, "upright"));
		directions.add(new Direction(1, 1, "downright"));
		directions.add(new Direction(-1, 1, "downleft"));
		directions.add(new Direction(-1, -1, "upleft"));

		// Starting position
		int base = this.getDemension() / 2;
		Move move_lt = new Move(Side.O, (char) ('a' + base - 1), (char) ('1' + base - 1));
		Move move_ld = new Move(Side.X, (char) ('a' + base - 1), (char) ('1' + base));
		Move move_rt = new Move(Side.X, (char) ('a' + base), (char) ('1' + base - 1));
		Move move_rd = new Move(Side.O, (char) ('a' + base), (char) ('1' + base));
		occupiedMoves.add(move_lt);
		occupiedMoves.add(move_ld);
		occupiedMoves.add(move_rt);
		occupiedMoves.add(move_rd);
		occupiedMoves.forEach(x -> {
			Move.takeSlot(x, slotsTaken);
			try {
				updatePossibleMoveSlots(x);
			} catch (Exception e) {
				logger.error(e);
			}
		});

	}

	private void updatePossibleMoveSlots(Move newMove) throws Exception {
		for (Direction direction : directions) {
			logger.debug(String.format("check direction %s", direction.getName()));
			// it will be possible move slot only if that slot not being taken
			// yet
			int shiftedX = newMove.getAxisX() + direction.getOffSetX();
			int shiftedY = newMove.getAxisY() + direction.getOffSetY();
			if (!insideTheBoard(shiftedX, shiftedY)) {
				continue;
			}
			if (slotsTaken[shiftedX][shiftedY] == false) {
				if (!possibleMoveSlots.stream().filter(pms -> pms.getAxisX() == shiftedX && pms.getAxisY() == shiftedY)
						.findAny().isPresent())
					possibleMoveSlots.add(new Move(shiftedX, shiftedY));
			}
		}
		// remove itself from possibleMoveSlots
		Optional<Move> itself = possibleMoveSlots.stream()
				.filter(pms -> pms.getAxisX() == newMove.getAxisX() && pms.getAxisY() == newMove.getAxisY()).findAny();
		if (itself.isPresent()) {
			possibleMoveSlots.remove(possibleMoveSlots.indexOf(itself.get()));
		}
		if (logger.isDebugEnabled()) {
			possibleMoveSlots.forEach(psm -> {
				logger.debug(String.format("possibleMoveSlot %s%s", psm.getAxisXc(), psm.getAxisYc()));
			});
			logger.debug(String.format("========================="));
		}

	}

	public boolean teminationCheck() {
		if (occupiedMoves.size() == this.getDemension() * this.getDemension()) {
			System.out.println(String.format("Game board is full! The winner is %s !", checkWinner()));
			return true;
		}

		if (noMoveAvaialbeOwen == false && noMoveAvaiableXray == false) {
			System.out.println(String.format("No move can be done! The winner is %s !", checkWinner()));
			return true;
		}
		return false;

	}

	public boolean hasAvailableMove() throws Exception {
		List<Move> availableMoves = new ArrayList<>();

		for (Move possibleMoveSlot : possibleMoveSlots) {
			Move moveToCheck = new Move(currentPlayer, possibleMoveSlot.getAxisX(), possibleMoveSlot.getAxisY());
			logger.debug(String.format("checking move slot %s%s", moveToCheck.getAxisXc(), moveToCheck.getAxisYc()));
			if (preCheckValidMove(moveToCheck)) {
				availableMoves.add(moveToCheck);
			}
		}
		availableMoves.forEach(am -> logger.info(String.format("avaialbe move %s%s", am.getAxisXc(), am.getAxisYc())));

		if (currentPlayer == Side.O) {
			noMoveAvaialbeOwen = availableMoves.size() > 0 ? true : false;
			return noMoveAvaialbeOwen;
		}

		if (currentPlayer == Side.X) {
			noMoveAvaiableXray = availableMoves.size() > 0 ? true : false;
			return noMoveAvaiableXray;
		}

		return false;

	}

	private String checkWinner() {
		int countOwen = 0;
		int countXray = 0;
		for (Move occupiedMove : occupiedMoves) {
			if (occupiedMove.getSide() == Side.O) {
				countOwen += 1;
			} else {
				countXray += 1;
			}
		}
		return countOwen > countXray ? String.format("O (%s)", "" + countOwen + ":" + countXray)
				: (countOwen < countXray ? String.format("X (%s)", "" + countXray + ":" + countOwen)
						: String.format("Both (%s)", "" + countOwen + ":" + countXray));

	}

	public void updateTheMoves() throws Exception {

		if (updateOccupiedMoves.size() > 0) {
			for (Move updateOccupiedMove : updateOccupiedMoves) {
				for (Move occupiedMove : occupiedMoves) {
					if (occupiedMove.getAxisX() == updateOccupiedMove.getAxisX()
							&& occupiedMove.getAxisY() == updateOccupiedMove.getAxisY()) {
						logger.debug(String.format("occupiedMove %s%s,side= will flip the side",
								occupiedMove.getAxisXc(), occupiedMove.getAxisYc(), occupiedMove.getSide().name()));
						occupiedMove.setSide(flip(occupiedMove.getSide()));
					}
				}
			}
			occupiedMoves.add(lastMove);
			Move.takeSlot(lastMove, slotsTaken);
			updatePossibleMoveSlots(lastMove);
			lastMove = null;
			updateOccupiedMoves = new ArrayList<Move>();
		}
	}

	public Side flip(Side side) {
		if (side == Side.O)
			return Side.X;
		else
			return Side.O;
	}

	public void updateBoard(String[][] board) {
		occupiedMoves.forEach(x -> {
			board[x.getAxisX()][x.getAxisY()] = x.getSide().name();
		});
	}

	public boolean isValidMove(Move move) {
		return this.isValidMove(move, Optional.of(updateOccupiedMoves));
	}

	private boolean preCheckValidMove(Move move) {
		return this.isValidMove(move, Optional.empty());
	}

	private boolean isValidMove(Move move, Optional<List<Move>> slotToUpdate) {

		boolean isValid = false;

		// can't move to taken slot
		if (Move.slotIsTaken(move, slotsTaken)) {
			System.out.println("That slot already been taken.");
			return false;
		}

		// check each directions
		for (Direction direction : directions) {
			if (linerCheck(move, direction, currentPlayer, slotToUpdate) && !isValid) {
				isValid = true;
			}
		}

		if (slotToUpdate.isPresent() && slotToUpdate.get().size() > 0) {
			slotToUpdate.get().add(move);
			lastMove = move;
		}

		return isValid;
	}

	public List<Move> getOccupiedMoves() {
		return occupiedMoves;
	}

	// get all existing move at same given direction and check if the side
	// criteria matched or not
	private boolean linerCheck(Move move, Direction direction, Side side, Optional<List<Move>> slotToUpdate) {
		boolean matched = false;
		List<Move> foundMoves = new ArrayList<Move>();
		Move transMove = move;
		do {
			transMove = checkByDirection(transMove, direction);
			if (transMove != null) {
				foundMoves.add(transMove);
			}
		} while (transMove != null);
		logger.debug(String.format("checking direction %s, foundMoves %s", direction.getName(), foundMoves.size()));
		List<Move> moveToFlip = new ArrayList<>();
		matched = gameRuleCheck(foundMoves, side,
				slotToUpdate.isPresent() ? Optional.of(moveToFlip) : Optional.empty());
		if (matched && slotToUpdate.isPresent() && foundMoves.size() > 0) {
			// side criteria matched and need put them into
			// updateOccupiedMoves(flip the side)
			slotToUpdate.get().addAll(moveToFlip);
		}
		return matched;

	}

	// get existing move at given direction
	private Move checkByDirection(Move move, Direction direction) {
		Move foundMove = null;
		if (!insideTheBoard(move.getAxisX() + direction.getOffSetX(), move.getAxisY() + direction.getOffSetY())) {
			foundMove = null;
		}
		for (Move occupiedMove : occupiedMoves) {
			if (occupiedMove.getAxisX() == move.getAxisX() + direction.getOffSetX()
					&& occupiedMove.getAxisY() == move.getAxisY() + direction.getOffSetY()) {
				foundMove = occupiedMove;
				break;
			}
		}
		return foundMove;
	}

	private boolean gameRuleCheck(List<Move> movesToCheck, Side side, Optional<List<Move>> movesToFlip) {
		movesToCheck
				.forEach(x -> logger.debug(String.format("%s%s,side=%s", x.getAxisXc(), x.getAxisYc(), x.getSide())));

		// at least 3 moves (include the move being placed) to finish a capture
		if (movesToCheck.size() < 2) {
			return false;
		}

		// the first one must be different side
		if (movesToCheck.get(0).getSide() == side) {
			return false;
		}

		// all others should contains at least one same side
		Optional<Move> firstSameSideMove = movesToCheck.subList(1, movesToCheck.size()).stream()
				.filter(x -> x.getSide() == side).findFirst();
		if (!firstSameSideMove.isPresent()) {
			return false;
		}

		if (movesToFlip.isPresent())
			movesToFlip.get().addAll(movesToCheck.subList(0, movesToCheck.indexOf(firstSameSideMove.get())));

		return true;
	}

	public void swapPlayers() {
		currentPlayer = flip(currentPlayer);

	}

	private boolean insideTheBoard(int x, int y) {
		if (x > this.getDemension() - 1 || x < 0) {
			return false;
		}
		if (y > this.getDemension() - 1 || y < 0) {
			return false;
		}
		return true;
	}

	public Side getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Side currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getDemension() {
		return demension;
	}

	public void setDemension(int demension) {
		this.demension = demension;
	}

}
