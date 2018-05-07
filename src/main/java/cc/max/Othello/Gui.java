package cc.max.Othello;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import cc.max.Othello.pojo.Move;
import jline.console.ConsoleReader;

public class Gui {

	public static final String EXIT = "exit";
	public static final String NEXT = "next";

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

	}

	public void show(Controller controller) {
		// update board
		controller.updateBoard(board);

		// clear console
		clearConsole();

		// print board
		printTheBoard(controller.getDemension());

		// asking for input
		askingForInput(String.format("Now '%s' plays, please input your move (e.g. c6) and press Enter",
				controller.getCurrentPlayer().name()));
	}

	public String waitForInput(Controller controller) throws Exception {

		Scanner sc = new Scanner(new InputStreamReader(System.in));

		try {

			if (controller.teminationCheck()) {
				System.out.println("Game end...");
				return EXIT;
			}

			if (!controller.hasAvailableMove()) {
				controller.swapPlayers();
				System.out.println(String.format("swap player since there is no available move"));
				return NEXT;
			}

			StringBuffer inputStr = new StringBuffer();
			do {
				String playerInput;

				playerInput = sc.nextLine();

				if (StringUtils.isEmpty(playerInput)) {
					continue;
				} else {
					System.out.println(String.format("input is %s", playerInput));
				}

				if (playerInput.trim().equalsIgnoreCase(EXIT)) {
					System.out.println("Quit the game...");
					return EXIT;
				}
				Move theMove = new Move();
				theMove.setSide(controller.getCurrentPlayer());
				if (!Move.isValidMoveInput(playerInput.trim(), theMove, controller.getDemension())
						|| !controller.isValidMove(theMove)) {
					System.out.println(
							String.format("%s is not a valid move, please try other move ..", playerInput.trim()));
				} else {
					controller.swapPlayers();
					System.out.println(String.format("%s is a valid move", playerInput.trim()));
					break;
				}

				inputStr.append(playerInput + "\n");
			} while (true);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
		System.out.println("next ...");
		return NEXT;
	}

	public void refresh(Controller rule) throws Exception {

		// update the moves
		rule.updateTheMoves();

		this.show(rule);
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

}
