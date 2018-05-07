package cc.max.Othello;

import cc.max.Othello.pojo.Side;

public class App {

	public static void main(String[] args) throws Exception {
		Controller rule = new Controller(Side.O, 8);
		Gui gui = new Gui(rule.getDemension());
		gui.show(rule);		
		String moveInput = null;
		do {
			moveInput = gui.waitForInput(rule);
			if (!Gui.EXIT.equals(moveInput)) {
				gui.refresh(rule);
			}
		} while (!Gui.EXIT.equals(moveInput));
	}
}
