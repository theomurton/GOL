import java.util.*;
public class Board implements Cloneable {
	private int height;
	private int width;
	private boolean[][] board;
	private HashSet<List<Integer>> liveSet = new HashSet<List<Integer>>();
	public Board(int height, int width){
		this.height = height;
		this.width = width;
		this.setBoardSize(this.height, this.width);
	}
	public void setLiveSet(HashSet<List<Integer>> set){
		this.liveSet = set;
	}
	public void addToLiveSet(int x, int y){
		List<Integer> coords = new ArrayList<Integer>();
		coords.add(x);
		coords.add(y);
		if (!this.liveSet.contains(coords)){
			this.liveSet.add(coords);
		}
		/*Iterator<List<Integer>> it = this.liveSet.iterator();
		int counter = 0;
		while (it.hasNext()){
			List<Integer> coordsa = it.next();
			System.out.println(coordsa.get(0) + " " + coordsa.get(1) + " " + counter);
			counter++;
		}
		System.out.println("");*/
	}
	public void removeFromLiveSet(int x, int y){
		List<Integer> coords = new ArrayList<>();
		coords.add(x);
		coords.add(y);
		if (this.liveSet.contains(coords)){
			this.liveSet.remove(coords);
		}
		/*Iterator<List<Integer>> it = this.liveSet.iterator();
		int counter = 0;
		while (it.hasNext()){
			List<Integer> coordsa = it.next();
			System.out.println(coordsa.get(0) + " " + coordsa.get(1) + " " + counter);
			counter++;
		}
		System.out.println("");*/
	}
	public void swapSet(int x, int y){
		List<Integer> coords = new ArrayList<>();
		coords.add(x);
		coords.add(y);
		if (this.liveSet.contains(coords)){
			this.removeFromLiveSet(x,y);
		} else {
			this.addToLiveSet(x, y);
		}
	}
	public HashSet<List<Integer>> getLiveSet(){
		return this.liveSet;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public void setArray(boolean[][] array) {
		this.board = array;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public boolean[][] getArray() {
		return this.board;
	}

	public void setBoardSize(int height, int width) {
		this.board = new boolean[height][width];
	}

	public void printBoard() {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				if (this.board[i][j]) {
					System.out.print("O");
				} else {
					System.out.print(".");
				}
			}
			System.out.println("");
		}
	}

	public Board getBoard() {
		return this;
	}

	public boolean getIndex(int x, int y) {
		return this.board[x][y];
	}

	public void setIndex(int x, int y, boolean value) {
		this.board[x][y] = value;
	}

	public void swapIndex(int y, int x) {
		if (this.board[y][x]) {
			this.board[y][x] = false;
		} else {
			this.board[y][x] = true;
		}
	}
}
