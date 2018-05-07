package cc.max.Othello;

import java.io.ByteArrayInputStream;

import org.junit.Assert;
import org.junit.Test;

import cc.max.Othello.pojo.Side;

public class OthelloTest {

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

		StringBuffer inputs = new StringBuffer();

		inputs.append("c1").append("\n");
		inputs.append("exit").append("\n");

		System.setIn(new ByteArrayInputStream(inputs.toString().getBytes()));

		gui.waitForInput(controller);

		Assert.assertTrue(true);
	}

	@Test
	public void playAnInvalidMove() throws Exception {

		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);

		StringBuffer inputs = new StringBuffer();

		inputs.append("a1").append("\n");
		inputs.append("c4").append("\n");
		inputs.append("exit").append("\n");

		System.setIn(new ByteArrayInputStream(inputs.toString().getBytes()));

		gui.waitForInput(controller);

		Assert.assertTrue(true);
	}

	@Test
	public void playAnInvalidMoveInput() throws Exception {

		Controller controller = new Controller(Side.O, 4);
		Gui gui = new Gui(controller.getDemension());
		gui.show(controller);

		StringBuffer inputs = new StringBuffer();

		inputs.append("c5").append("\n");
		inputs.append("e5").append("\n");
		inputs.append("e4").append("\n");
		inputs.append("exit").append("\n");

		System.setIn(new ByteArrayInputStream(inputs.toString().getBytes()));

		gui.waitForInput(controller);

		Assert.assertTrue(true);
	}

}
