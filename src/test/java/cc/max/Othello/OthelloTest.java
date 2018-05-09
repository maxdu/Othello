package cc.max.Othello;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.Assert;
import org.junit.Test;

import cc.max.Othello.pojo.Side;

public class OthelloTest {

	public static final Logger logger = (Logger) LogManager.getLogger(OthelloTest.class);

	@Test
	public void beforeGameStart() throws Exception {
		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);
		Assert.assertTrue(true);
	}

	@Test
	public void playAnValidMove() throws Exception {

		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);

		boolean validMove = gui.checkPlayerInput("c1", controller);

		Assert.assertTrue(validMove);
	}

	@Test
	public void playValidMove() throws Exception {

		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);

		boolean validMove = gui.checkPlayerInput("c1", controller);

		Assert.assertTrue(validMove);

		validMove = gui.checkPlayerInput("d2", controller);

		Assert.assertTrue(validMove);

	}

	@Test
	public void playInvalidMove() throws Exception {

		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);

		boolean validMove = gui.checkPlayerInput("b1", controller);

		Assert.assertFalse(validMove);

		validMove = gui.checkPlayerInput("c4", controller);

		Assert.assertFalse(validMove);
	}

	@Test
	public void invalidMoveInput() throws Exception {

		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);

		boolean validMoveInput = gui.checkPlayerInput("c5", controller);

		Assert.assertFalse(validMoveInput);

		validMoveInput = gui.checkPlayerInput("e5", controller);

		Assert.assertFalse(validMoveInput);

		validMoveInput = gui.checkPlayerInput("e4", controller);

		Assert.assertFalse(validMoveInput);
	}

	@Test
	public void playACompeleteSize4Games() throws Exception {
		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);
		gui.askingForInput();

		int totalMoves = 0;
		int limitMovesToPreventDeadLoop = 1000;

		// start with 'b1' move
		boolean validMoveInput = false;
		String mockingPlayerInput = "c1";
		do {

			if (controller.mustTerminateNow()) {
				break;
			}

			if (!controller.hasAvailableMove()) {
				controller.swapPlayers();
				continue;
			}

			if (controller.cheatingSheet != null) {
				mockingPlayerInput = controller.cheatingSheet;
			}
			logger.info(String.format("mockingPlayerInput %s", mockingPlayerInput));
			validMoveInput = gui.checkPlayerInput(mockingPlayerInput, controller);
			Assert.assertTrue(validMoveInput);
			controller.swapPlayers();
			gui.refresh(controller);
			gui.askingForInput();
			totalMoves += 1;

		} while (totalMoves <= limitMovesToPreventDeadLoop);

		if (totalMoves > limitMovesToPreventDeadLoop) {
			logger.error(String.format("limitMovesToPreventDeadLoop %s reached!", limitMovesToPreventDeadLoop));
			Assert.assertFalse(true);
		}

		// should end and reach here
		Assert.assertFalse(false);

	}

	@Test
	public void playACompeleteSize8Games() throws Exception {

		Controller controller = new Controller(Side.O, 8);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);
		gui.askingForInput();

		int totalMoves = 0;
		int limitMovesToPreventDeadLoop = 1000;

		// start with 'b1' move
		boolean validMoveInput = false;
		String mockingPlayerInput = "c1";
		do {

			if (controller.mustTerminateNow()) {
				break;
			}

			if (!controller.hasAvailableMove()) {
				controller.swapPlayers();
				continue;
			}

			if (controller.cheatingSheet != null) {
				mockingPlayerInput = controller.cheatingSheet;
			}
			logger.info(String.format("mockingPlayerInput %s", mockingPlayerInput));
			validMoveInput = gui.checkPlayerInput(mockingPlayerInput, controller);
			Assert.assertTrue(validMoveInput);
			controller.swapPlayers();
			gui.refresh(controller);
			gui.askingForInput();
			totalMoves += 1;

		} while (totalMoves <= limitMovesToPreventDeadLoop);

		if (totalMoves > limitMovesToPreventDeadLoop) {
			logger.error(String.format("limitMovesToPreventDeadLoop %s reached!", limitMovesToPreventDeadLoop));
			Assert.assertFalse(true);
		}

		// should end and reach here
		Assert.assertFalse(false);

	}

}
