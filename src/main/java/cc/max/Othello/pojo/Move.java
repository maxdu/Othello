package cc.max.Othello.pojo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Move {

	public static final Logger logger = (Logger) LogManager.getLogger(Move.class);

	private Side side; // O or X
	private int axisX;
	private int axisY;

	public void setAxisX(int axisX) {
		this.axisX = axisX;
	}

	public void setAxisY(int axisY) {
		this.axisY = axisY;
	}

	private char axisXc;
	private char axisYc;

	public static void takeSlot(Move move, boolean[][] slotsTaken) {
		slotsTaken[move.getAxisX()][move.getAxisY()] = true;

	}

	public static boolean slotIsTaken(Move move, boolean[][] slotsTaken) {
		return slotsTaken[move.getAxisX()][move.getAxisY()];
	}

	public Move(Side side, char axisX, char axisY) throws Exception {
		super();
		this.side = side;
		this.setAxisX(axisX);
		this.setAxisY(axisY);
	}

	public Move(Side side, int axisX, int axisY) throws Exception {
		super();
		this.setAxisX((char) ('a' + axisX));
		this.setAxisY((char) ('1' + axisY));
	}

	public Move(int axisX, int axisY) throws Exception {
		super();
		this.setAxisX((char) ('a' + axisX));
		this.setAxisY((char) ('1' + axisY));
	}

	public Move() {
		super();
	}

	public static boolean isValidMoveInput(String input, Move move, int limit) throws Exception {
		if (input.length() != 2)
			return false;

		char[] vtc = input.toLowerCase().toCharArray();

		if ((vtc[0] >= 'a' && vtc[0] <= 'a' + limit - 1) && vtc[1] >= '1' && vtc[1] <= '1' + limit - 1) {
			move.setAxisX(vtc[0]);
			move.setAxisY(vtc[1]);
			return true;
		}

		if ((vtc[1] >= 'a' && vtc[1] <= 'a' + limit - 1) && vtc[0] >= '1' && vtc[0] <= '1' + limit - 1) {
			move.setAxisX(vtc[1]);
			move.setAxisY(vtc[0]);
			return true;
		}

		logger.error(String.format("%s is not a valid move input", input));

		return false;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public int getAxisX() {
		return axisX;
	}

	public void setAxisX(char axisX) throws Exception {
		if (axisX < 'a' || axisX > 'h')
			throw new Exception("Not a valid X axis value, should be value among 'a'-'h'");
		this.axisX = (int) axisX - 'a';
		this.axisXc = axisX;
	}

	public int getAxisY() {
		return axisY;
	}

	public void setAxisY(char axisY) throws Exception {
		if (axisY < '1' || axisY > '8')
			throw new Exception("Not a valid Y axis value, should be value among '1'-'8'");
		this.axisY = (int) axisY - '1';
		this.axisYc = axisY;
	}

	public String getAxisString() {
		return "" + this.axisX + this.axisY;

	}

	public char getAxisYc() {
		return axisYc;
	}

	public void setAxisYc(char axisYc) {
		this.axisYc = axisYc;
	}

	public char getAxisXc() {
		return axisXc;
	}

	public void setAxisXc(char axisXc) {
		this.axisXc = axisXc;
	}

}
