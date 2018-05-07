package cc.max.Othello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jline.console.ConsoleReader;

public class Gui {

	public static final String EXIT = "exit";
	public static final String NEXT = "next";
	public static Side currentSide = null;

	// the board
	private static String[][] board = new String[8][8];

	public Gui(Side startSide) {
		System.out.println("Welcome to Othello game, have fun!");

		// Initialize board
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = "-";
			}
		}

		// Set startSide
		currentSide = startSide;
	}

	public void show(Rule rule) {
		// update board
		rule.updateBoard(board);

		// clear console
		clearConsole();

		// print board
		printTheBoard();

		// asking for input
		askingForInput(
				String.format("Now '%s' plays, please input your move (e.g. c6) and press Enter", currentSide.name()));
	}

	public String waitForInput(Rule rule) throws Exception {
		try {
			if (rule.teminationCheck()) {
				System.out.println("Game end...");
				return EXIT;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			StringBuffer inputStr = new StringBuffer();
			do {
				String line;

				line = reader.readLine();
				// System.out.println(line);

				if (line.trim().equalsIgnoreCase(EXIT)) {
					System.out.println("Quit the game...");
					return EXIT;
				}
				Move theMove = new Move();
				theMove.setSide(currentSide);
				if (!Move.isValidMoveInput(line.trim(), theMove) || !rule.isValidMove(theMove, currentSide)) {
					System.out.println(String.format("%s is not a valid move, please input again ..", line.trim()));
				} else {
					currentSide = rule.flip(currentSide);
					System.out.println(String.format("%s is a valid move", line.trim()));
					break;
				}

				inputStr.append(line + "\n");
			} while (true);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("next ...");
		return NEXT;
	}

	public void refresh(Rule rule) {

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

	private void printTheBoard() {
		System.out.println("````text");
		for (int x = 0; x < 8; x++) {
			System.out.print(x + 1);
			for (int y = 0; y < 8; y++) {
				System.out.print(board[y][x]);
			}
			System.out.print("\n");
		}
		System.out.println(" abcdefgh");
	}

}
