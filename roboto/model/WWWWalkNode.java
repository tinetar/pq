package dre.elfocrash.roboto.model;

public class WWWWalkNode {
	private int _x;
	private int _y;
	private int _z;
	private int _stayIterations;
	
	public WWWWalkNode(int x, int y, int z, int stayIterations) {
		_x = x;
		_y = y;
		_z = z;
		_stayIterations = stayIterations;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public int getZ() {
		return _z;
	}
	
	public int getStayIterations() {
		return _stayIterations;
	}
}
