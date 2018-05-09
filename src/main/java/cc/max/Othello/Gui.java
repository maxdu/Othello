package cc.max.Othello;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import cc.max.Othello.pojo.Move;
import jline.console.ConsoleReader;

public class Gui {

	public static final Logger logger = (Logger) LogManager.getLogger(Gui.class);

	public static final String EXIT = "exit";
	public static final String NEXT = "next";

	private Controller controller = null;
	private Scanner scanner = null;

	// the board
	private static String[][] board = null;

	public Gui(int demension) {
		System.out.println("Welcome to Othello game, have fun!");

		// Initialize board
		board = new String[demension][demension];

		for (int x = 0; x < demension; x++) {
			for (int y = 0; y < demension; y++) {
				board[x][y] = "-";
			}
		}

		// initialize scanner for system in
		scanner = new Scanner(System.in);

	}

	public void show(Controller controller) {

		this.controller = controller;

		// update board
		controller.updateBoard(board);

		// clear console
		clearConsole();

		// print board
		printTheBoard(controller.getDemension());
	}

	public String waitForInput(Controller controller) throws Exception {

		try {

			if (!controller.hasAvailableMove()) {
				controller.swapPlayers();
				System.out.println(String.format("swap player since there is no available move"));
				System.out.println();
				return NEXT;
			}
			
			if (controller.mustTerminateNow()) {
				System.out.println("Game end...");
				return EXIT;
			}			

			askingForInput();

			String playerInput = null;

			do {

				playerInput = scanner.nextLine();

				if (StringUtils.isEmpty(playerInput)) {
					continue;
				} else {
					playerInput = playerInput.trim();
					System.out.print(String.format("Player input is %s, ", playerInput));
				}

				if (playerInput.equalsIgnoreCase(EXIT)) {
					System.out.println("Quit the game...");
					return EXIT;
				}

				boolean validInput = checkPlayerInput(playerInput, controller);

				if (validInput) {
					System.out.println(String.format("%s is not a valid move, please try other move ..", playerInput));
				} else {
					controller.swapPlayers();
					System.out.println(String.format("%s is a valid move", playerInput));
					break;
				}

			} while (true);

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("next ...");
		return NEXT;
	}

	public boolean checkPlayerInput(String playerInput, Controller controller) {
		Move theMove = new Move();
		theMove.setSide(controller.getCurrentPlayer());
		try {
			return (Move.isValidMoveInput(playerInput, theMove, controller.getDemension())
					&& controller.isValidMove(theMove));
		} catch (Exception e) {
			logger.error("Exception caught inside processInput", e);
			return false;
		}
	}

	public void refresh(Controller controller) throws Exception {

		// update the moves
		controller.updateTheMoves();

		this.show(controller);
	}

	private void clearConsole() {
		try {
			ConsoleReader r = new ConsoleReader();
			r.clearScreen();
			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void askingForInput(String guiSay) {
		System.out.println(guiSay);
		System.out.println();
	}

	private void printTheBoard(int demension) {
		System.out.println("````text");
		for (int x = 0; x < demension; x++) {
			System.out.print(x + 1);
			for (int y = 0; y < demension; y++) {
				System.out.print(board[y][x]);
			}
			System.out.print("\n");
		}
		System.out.println(" abcdefgh");
	}

	public void askingForInput() {
		askingForInput(String.format("Now '%s' plays, please input your move (e.g. c6) and press Enter",
				this.controller.getCurrentPlayer().name()));
	}

}
