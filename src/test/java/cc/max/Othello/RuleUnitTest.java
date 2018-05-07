package cc.max.Othello;

import org.junit.Assert;
import org.junit.Test;

public class RuleUnitTest {

	@Test
	public void itsJustATest() throws Exception {
		Rule rule = new Rule();
		Assert.assertTrue(rule != null);
		rule.getOccupiedMoves().forEach(x -> System.out.println(x.getAxisString()));
	}

}
