package cc.max.Othello.pojo;

public class Direction {

	private int offSetX;
	private int OffSetY;
	private String name;

	public Direction(int offSetX, int offSetY, String name) {
		super();
		this.offSetX = offSetX;
		OffSetY = offSetY;
		this.name = name;
	}

	public int getOffSetX() {
		return offSetX;
	}

	public void setOffSetX(int offSetX) {
		this.offSetX = offSetX;
	}

	public int getOffSetY() {
		return OffSetY;
	}

	public void setOffSetY(int offSetY) {
		OffSetY = offSetY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
