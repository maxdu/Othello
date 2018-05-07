package cc.max.Othello;

public class App {

	public static void main(String[] args) throws Exception {
		Gui gui = new Gui(Side.O);
		Rule rule = new Rule();
		gui.show(rule);
		String moveInput = null;
		do {
			moveInput = gui.waitForInput(rule);
			if (!Gui.EXIT.equals(moveInput))
				gui.refresh(rule);
		} while (!Gui.EXIT.equals(moveInput));
	}
}
