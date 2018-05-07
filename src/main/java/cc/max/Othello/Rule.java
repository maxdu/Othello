package cc.max.Othello;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Rule {

	public static final Logger logger = (Logger) LogManager.getLogger(Rule.class);

	private List<Direction> directions = new ArrayList<Direction>();
	private List<Move> occupiedMoves = new ArrayList<Move>();
	private List<Move> updateOccupiedMoves = new ArrayList<Move>();
	private Move lastMove = null;

	public Rule() throws Exception {

		// Starting position
		Move move_d4 = new Move(Side.O, 'd', '4');
		Move move_e4 = new Move(Side.X, 'e', '4');
		Move move_d5 = new Move(Side.X, 'd', '5');
		Move move_e5 = new Move(Side.O, 'e', '5');
		occupiedMoves.add(move_d4);
		occupiedMoves.add(move_e4);
		occupiedMoves.add(move_d5);
		occupiedMoves.add(move_e5);

		// 8 possible directions
		directions.add(new Direction(0, -1, "up"));
		directions.add(new Direction(0, 1, "down"));
		directions.add(new Direction(-1, 0, "left"));
		directions.add(new Direction(1, 0, "right"));
		directions.add(new Direction(1, -1, "upright"));
		directions.add(new Direction(1, 1, "downright"));
		directions.add(new Direction(-1, 1, "downleft"));
		directions.add(new Direction(-1, -1, "upleft"));

	}

	public boolean teminationCheck() {
		return false;

	}

	public void updateTheMoves() {

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

	public boolean isValidMove(Move move, Side side) {

		// can't move to taken position
		if (occupiedMoves.stream().filter(x -> x.getAxisX() == move.getAxisX() && x.getAxisY() == move.getAxisY())
				.findAny().isPresent()) {
			System.out.println("That move already been taken.");
			return false;
		}

		// check each directions
		for (Direction direction : directions) {
			linerCheck(move, direction, side);
		}

		if (updateOccupiedMoves.size() > 0) {
			updateOccupiedMoves.add(move);
			lastMove = move;
			return true;
		} else {
			return false;
		}
	}

	public List<Move> getOccupiedMoves() {
		return occupiedMoves;
	}

	// get all existing move at same given direction and check if the side
	// criteria matched or not
	private void linerCheck(Move move, Direction direction, Side side) {
		List<Move> foundMoves = new ArrayList<Move>();
		Move transMove = move;
		do {
			transMove = checkByDirection(transMove, direction);
			if (transMove != null) {
				foundMoves.add(transMove);
			}
		} while (transMove != null);
		logger.debug(String.format("checking direction %s, foundMoves %s", direction.getName(), foundMoves.size()));
		if (foundMoves.size() > 0 && sideCriteriaCheck(foundMoves, side)) {
			// side criteria matched and need put them into
			// updateOccupiedMoves(flip the side)
			updateOccupiedMoves.addAll(foundMoves.subList(0, foundMoves.size() - 1));
		}

	}

	// get existing move at given direction
	private Move checkByDirection(Move move, Direction direction) {
		Move foundMove = null;
		for (Move occupiedMove : occupiedMoves) {
			if (occupiedMove.getAxisX() == move.getAxisX() + direction.getOffSetX()
					&& occupiedMove.getAxisY() == move.getAxisY() + direction.getOffSetY()) {
				foundMove = occupiedMove;
				break;
			}
		}
		return foundMove;
	}

	private boolean sideCriteriaCheck(List<Move> movesToCheck, Side side) {
		movesToCheck
				.forEach(x -> logger.debug(String.format("%s%s,side=%s", x.getAxisXc(), x.getAxisYc(), x.getSide())));

		// at least 3 moves (include the move being placed) to finish a line
		if (movesToCheck.size() < 2) {
			return false;
		}

		// the last one must be the same side
		if (movesToCheck.get(movesToCheck.size() - 1).getSide() != side)
			return false;

		// all others must be the other side
		if (movesToCheck.subList(0, movesToCheck.size() - 1).stream().filter(x -> x.getSide() == side).findAny()
				.isPresent()) {
			return false;
		}

		return true;
	}

}
