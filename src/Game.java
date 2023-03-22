import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//the vast majority of the comments are in this game class as the code in the guis is relatively simple and self explanatory.

public class Game {
	private int x;
	private int y;
	private int z;
	private int delay = 100;
	private boolean isQuit;
	private IntroGUI introGUI;
	private MainGUI mainGUI;
	private Board board;
	private boolean paused = true;
	private String state = "new";
	private int[] parameters;
	private boolean step = false;
	private boolean reset = false;
	private String filename;
	private NewGameGUI newGameGUI;
	private LoadGameGUI loadGameGUI;

	public static void main(String[] args) {
		Game game = new Game();
		game.introGUI = new IntroGUI(game);
	}

	public void setNewGameGUI(NewGameGUI newGameGUI) {
		this.newGameGUI = newGameGUI;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	public void setLoadGameGUI(LoadGameGUI loadGameGUI) {
		this.loadGameGUI = loadGameGUI;
	}

	// this is called by either NewGameGUI or LoadGameGUI. To avoid repeat code and
	// two separate methods, we use a "state" of new or load to indicate how exactly
	// to make the game
	public void newGame() {
		this.x = this.parameters[2];
		this.y = this.parameters[3];
		this.z = this.parameters[4];
		if (this.getState().equals("new")) {
			this.board = new Board(this.parameters[0], this.parameters[1]);
			this.mainGUI = new MainGUI(board.getWidth(), board.getHeight(), board, this);
		} else if (this.getState().equals("load")) {
			try {
				this.loadGame(this.filename);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return;
			}
		}
	}

	// this method loads the board and distinguishes between files with and without
	// a .gol suffix to determine which loading algorithm to use.
	// notice both boards use the same load method to save on code as the decode
	// methods will produce the same output
	public void loadGame(String file) {
		Pattern pattern = Pattern.compile("(.*\\.gol)");
		Matcher match = pattern.matcher(this.filename);
		File givenFile = new File(file);
		try {
			if (match.matches()) {
				this.loadBoard(this.decodeGOL(givenFile));
			} else {
				this.loadBoard(this.decodeLoad(givenFile));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	// the main game loop. Uses the 'paused' attribute to determine when it should
	// stop. Always runs on a separate thread when it is running (it is not always
	// running)
	public void gameLoop() {
		while (!this.paused) {
			try {
				this.updateBoard(this.board);
				Thread.sleep(this.delay);
			} catch (InterruptedException i) {
				System.out.println(i.getMessage());
				return;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return;
			}
		}
	}

	// this is our 'compressed' save method as outlined in the report
	public void saveGameBoard(String input, String comment, Board board) {
		try {
			this.paused = true;
			String name = input;
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			writer.write(board.getHeight() + "," + board.getWidth());
			writer.newLine();
			String line = "";
			for (int i = 0; i < board.getHeight(); i++) {
				for (int j = 0; j < board.getWidth(); j++) {
					if (board.getIndex(i, j)) {
						line += "1";
					} else {
						line += "0";
					}
				}
			}
			boolean full;
			String character = String.valueOf(line.charAt(0));
			if (character.equals("0")) {
				writer.append("e");
				full = false;
			} else {
				writer.append("f");
				full = true;
			}
			int counter = 1;
			for (int i = 1; i < line.length(); i++) {
				String specificCharacter = String.valueOf(line.charAt(i));
				boolean tempBool = full;
				if (specificCharacter.equals("0")) {
					full = false;
					counter++;
				} else {
					full = true;
					counter++;
				}
				if (tempBool != full && i != 0) {
					String value = Integer.toString(counter - 1);
					writer.write(value);
					if (full) {
						writer.append("f");
					} else {
						writer.append("e");
					}
					counter = 1;
				}
				if (i == line.length() - 1) {
					String finalValue = Integer.toString(counter);
					writer.write(finalValue);
				}
			}
			writer.newLine();
			writer.write(comment);
			writer.close();
		} catch (IOException i) {
			System.out.println(i.getMessage());
			return;
		}
	}

	// generic scan method which is useful in other methods to save code
	public List<String> scanFile(File file) throws FileNotFoundException {
		Scanner scan = new Scanner(file);
		List<String> scanned = new ArrayList<>();
		while (scan.hasNextLine()) {
			scanned.add(scan.nextLine());
		}
		scan.close();
		return scanned;
	}

	// will return a list of coordinates to be swapped, decodes a compressed save
	public List<Integer> decodeLoad(File file) throws FileNotFoundException {
		// returns array with height and width as first two elements, then y and x (in
		// that order) of all live cells to be loaded
		// This code is a bit big but it has to be very general to accommodate all
		// potential board dimensions
		List<Integer> decoded = new ArrayList<>();
		List<String> contents = this.scanFile(file);
		String[] dimensions = contents.get(0).split(",");
		int height = Integer.valueOf(dimensions[0]);
		int width = Integer.valueOf(dimensions[1]);
		String code = contents.get(1);
		boolean full = false;
		String number = "";
		int currentRow = 0;
		int currentColumn = 0;
		int total = 0;
		decoded.add(height);
		decoded.add(width);
		for (int i = 0; i < code.length(); i++) {
			char charac = code.charAt(i);
			String character = Character.toString(charac);
			if (Character.isLetter(charac)) {
				if (i != 0) {
					int value = Integer.valueOf(number);
					number = "";
					if (full) {
						for (int j = 0; j < value; j++) {
							decoded.add(currentRow);
							decoded.add(currentColumn);
							currentColumn++;
							if (currentColumn == width) {
								currentColumn = 0;
								currentRow++;
							}
						}
						total += value;
					} else {
						total += value;
						currentRow = total / width;
						currentColumn = total % width;
					}
				}
				if (character.equals("f")) {
					full = true;
				} else {
					full = false;
				}
			} else {
				number += character;
			}
			if (i == code.length() - 1) {
				int finalValue = Integer.valueOf(number);
				if (full) {
					for (int j = 0; j < finalValue; j++) {
						decoded.add(currentRow);
						decoded.add(currentColumn);
						if (currentColumn == width + 1) {
							currentColumn = 0;
							currentRow++;
						}
					}
				}
			}
		}
		return decoded;
	}

	// method for saving as the basic gol file.
	public void saveGOL(String input, String comment, Board board) {
		try {
			String name = (input + ".gol");
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			writer.write(board.getHeight() + "," + board.getWidth());
			writer.newLine();
			for (int i = 0; i < board.getHeight(); i++) {
				String line = "";
				for (int j = 0; j < board.getWidth(); j++) {
					if (board.getIndex(i, j)) {
						line += "1";
					} else {
						line += "0";
					}
				}
				writer.write(line);
				writer.newLine();
			}
			writer.write(comment);
			writer.close();
		} catch (IOException i) {
			System.out.println(i.getMessage());
		}
	}

	// method for decoding the basic gol file.
	public List<Integer> decodeGOL(File file) throws FileNotFoundException {
		List<Integer> decoded = new ArrayList<>();
		List<String> contents = this.scanFile(file);
		String[] dimensions = contents.get(0).split(",");
		int height = Integer.valueOf(dimensions[0]);
		int width = Integer.valueOf(dimensions[1]);
		contents.remove(contents.size() - 1);
		contents.remove(0);
		decoded.add(height);
		decoded.add(width);
		for (int i = 0; i < contents.size(); i++) {
			for (int j = 0; j < contents.get(i).length(); j++) {
				String character = String.valueOf(contents.get(i).charAt(j));
				if (character.equals("1")) {
					decoded.add(i);
					decoded.add(j);
				}
			}
		}
		return decoded;
	}

	// takes a list of live cell coordinates and swaps them. It can therefore
	// receive output from either load method.
	public void loadBoard(List<Integer> input) {
		List<Integer> decoded = new ArrayList<>(input);
		this.board = new Board(decoded.get(0), decoded.get(1));
		this.mainGUI = new MainGUI(this.board.getWidth(), this.board.getHeight(), this.board, this);
		for (int i = 2; i < decoded.size(); i = i + 2) {
			this.board.swapIndex(decoded.get(i), decoded.get(i + 1));
			this.mainGUI.setBoxWhite(decoded.get(i), decoded.get(i + 1));
			this.board.addToLiveSet(decoded.get(i + 1), decoded.get(i));
		}
	}

	// the fundamental method for getting the next generation
	public void updateBoard(Board board) {
		try {
			// the hash map data is has a coordinates as the key and the number of adjacent
			// live squares as the value
			HashSet<List<Integer>> liveSet = this.board.getLiveSet();
			HashMap<List<Integer>, Integer> data = new HashMap<>();
			HashSet<List<Integer>> checked = new HashSet<>();
			Iterator<List<Integer>> it = liveSet.iterator();
			int counter = 0;
			/*
			 * Iterator<List<Integer>> its = liveSet.iterator();
			 * int counters = 0;
			 * while (its.hasNext()){
			 * List<Integer> coordsa = its.next();
			 * //System.out.println(coordsa.get(0) + " " + coordsa.get(1) + " " + counters);
			 * counters++;
			 * }
			 */
			// iterates through the live set of coordinates and their adjacent squares to
			// save on resources
			while (it.hasNext()) {
				List<Integer> element = it.next();
				for (int x = -1; x <= 1; x++) {
					for (int y = -1; y <= 1; y++) {
						int xcoord = element.get(0) + x;
						int ycoord = element.get(1) + y;
						if (xcoord == -1) {
							xcoord = board.getWidth() - 1;
						}
						if (ycoord == -1) {
							ycoord = board.getHeight() - 1;
						}
						if (xcoord == board.getWidth()) {
							xcoord = 0;
						}
						if (ycoord == board.getHeight()) {
							ycoord = 0;
						}
						List<Integer> coords = new ArrayList<>();
						coords.add(xcoord);
						coords.add(ycoord);
						if (!checked.contains(coords)) {
							int filled = this.scanBoard(board, coords.get(1), coords.get(0));
							data.put(coords, filled);
							checked.add(coords);
						}
					}
				}
			}
			// iterating through data hash map as described above to change cells in
			// accordance with x y z
			Iterator<List<Integer>> iter = data.keySet().iterator();
			while (iter.hasNext()) {
				List<Integer> ij = iter.next();
				int y = ij.get(1);
				int x = ij.get(0);
				if (board.getIndex(y, x) == true && data.get(ij) < this.x) {
					this.swapCell(y, x);
					counter++;
				} else if (board.getIndex(y, x) == true && data.get(ij) >= this.x && data.get(ij) <= this.y) {
					// nothing happens
				} else if (board.getIndex(y, x) == true && data.get(ij) > this.y) {
					this.swapCell(y, x);
					counter++;
				} else if (board.getIndex(y, x) == false && data.get(ij) == this.z) {
					this.swapCell(y, x);
					counter++;
				}
			}
			if (counter == 0) {
				this.paused = true;
				this.setGUIPauseButtons();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	// basic swap cell method to swap cells of the board
	public void swapCell(int y, int x) {
		this.mainGUI.swapBoxColour(y, x);
		board.swapIndex(y, x);
		this.board.swapSet(x, y);
	}

	// scans a cell of the board's adjacnet cells to see how many are full
	public int scanBoard(Board board, int y, int x) {
		int number = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				// don't want to check the centre square
				if (i == 0 && j == 0) {
				} else {
					int yCo = y + i;
					int xCo = x + j;
					// toroidality
					if (xCo == -1) {
						xCo = board.getWidth() - 1;
					}
					if (yCo == -1) {
						yCo = board.getHeight() - 1;
					}
					if (xCo == board.getWidth()) {
						xCo = 0;
					}
					if (yCo == board.getHeight()) {
						yCo = 0;
					}
					if (board.getIndex(yCo, xCo)) {
						number++;
					}
				}
			}
		}
		return number;
	}

	public boolean getPauseState() {
		return this.paused;
	}

	public void setPauseState(boolean paused) {
		this.paused = paused;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void reset() {
		this.paused = true;
		for (int i = 0; i < this.board.getHeight(); i++) {
			for (int j = 0; j < this.board.getWidth(); j++) {
				this.board.setIndex(i, j, false);
				this.mainGUI.setBoxBlack(i, j);
				this.board.removeFromLiveSet(j, i);
			}
		}
		this.setGUIPauseButtons();
	}

	public void randomise() {
		this.paused = true;
		Random random = new Random();
		for (int i = 0; i < this.board.getHeight(); i++) {
			for (int j = 0; j < this.board.getWidth(); j++) {
				int rand = random.nextInt(2);
				if (rand == 0) {
					this.mainGUI.swapBoxColour(i, j);
					this.board.swapIndex(i, j);
					this.board.swapSet(j, i);
				} else {

				}
			}
		}
		this.setGUIPauseButtons();
	}

	public void swapPaused() {
		if (this.paused) {
			this.paused = false;
		} else {
			this.paused = true;
		}
	}

	public void setGUIPauseButtons() {
		this.mainGUI.pauseButton.setText("Play");
		this.mainGUI.saveButton.setVisible(true);
		this.mainGUI.stepButton.setVisible(true);
	}

	public void setDelay(int input) {
		// RHS of a normal distribution curve: higher control at higher speeds
		this.delay = (int) Math.round(1000 * Math.pow(2.718, -((Math.pow(input * 0.003, 2)))));
	}

	public void requestStep() {
		this.updateBoard(this.board);
	}

	public String getState() {
		return this.state;
	}

	public void setParameters(int[] parameters) {
		this.parameters = parameters;
	}

	public void setState(String state) {
		this.state = state;
	}

	// method for updating board settings.
	public void update(int[] params) {
		HashSet<List<Integer>> live = this.board.getLiveSet();
		/*
		 * Iterator<List<Integer>> it = live.iterator();
		 * int counter = 0;
		 * while (it.hasNext()){
		 * List<Integer> coordsa = it.next();
		 * System.out.println(coordsa.get(0) + " " + coordsa.get(1) + " " + counter);
		 * counter++;
		 * }
		 */
		// System.out.println("");
		this.board = new Board(params[1], params[0]);
		this.board.setLiveSet(live);
		this.x = params[2];
		this.y = params[3];
		this.z = params[4];
		this.mainGUI = new MainGUI(this.board.getWidth(), this.board.getHeight(), this.board, this);
		Iterator<List<Integer>> iterate = live.iterator();
		// chops off cells from the edges if the board is made smaller
		List<List<Integer>> deads = new ArrayList<>();
		while (iterate.hasNext()) {
			List<Integer> element = iterate.next();
			int i = element.get(0);
			int j = element.get(1);
			if (i < this.board.getWidth() && j < this.board.getHeight()) {
				this.board.swapIndex(j, i);
				this.mainGUI.setBoxWhite(j, i);
			} else {
				deads.add(element);
				// this.board.removeFromLiveSet(i, j);
			}
		}
		for (int i = 0; i < deads.size(); i++) {
			this.board.removeFromLiveSet(deads.get(i).get(0), deads.get(i).get(1));
		}

	}

	public Board getBoard() {
		return this.board;
	}

	// gets the comment on the loaded file
	public String getComment(String file) throws FileNotFoundException {
		File f = new File(file);
		List<String> scanned = this.scanFile(f);
		int length = scanned.size() - 1;
		String comment = scanned.get(length);
		return comment;
	}
}
