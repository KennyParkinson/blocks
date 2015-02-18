package pt314.blocks.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pt314.blocks.game.Block;
import pt314.blocks.game.Direction;
import pt314.blocks.game.GameBoard;
import pt314.blocks.game.HorizontalBlock;
import pt314.blocks.game.TargetBlock;
import pt314.blocks.game.VerticalBlock;

/**
 * Simple GUI test...
 */
public class SimpleGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int newRow = 5;
	private static int newCol = 5;

	private GameBoard board;

	// currently selected block
	private Block selectedBlock;
	private int selectedBlockRow;
	private int selectedBlockCol;

	private GridButton[][] buttonGrid;

	private JMenuBar menuBar;
	private JMenu gameMenu, helpMenu;
	private JMenuItem newGameMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem aboutMenuItem;

	public SimpleGUI() {
		super("Blocks");

		initMenus();

		initBoard("");

		pack();
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void initMenus() {
		menuBar = new JMenuBar();

		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);

		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		newGameMenuItem = new JMenuItem("New game");
		newGameMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				getContentPane().removeAll();
				String fileName = JOptionPane.showInputDialog(SimpleGUI.this,
						"Enter File Name");
				initBoard(fileName);
				pack();
				setVisible(true);
			}
		});
		gameMenu.add(newGameMenuItem);

		gameMenu.addSeparator();

		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		gameMenu.add(exitMenuItem);

		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(SimpleGUI.this, "Sliding blocks!");
			}
		});
		helpMenu.add(aboutMenuItem);

		setJMenuBar(menuBar);
	}

	private void initBoard(String gameFile) {

		if (gameFile == "") {
			board = new GameBoard(newCol, newRow);
			buttonGrid = new GridButton[newRow][newCol];

			setLayout(new GridLayout(newRow, newCol));
			for (int row = 0; row < newRow; row++) {
				for (int col = 0; col < newCol; col++) {
					GridButton cell = new GridButton(row, col);
					cell.setPreferredSize(new Dimension(64, 64));
					cell.addActionListener(this);
					buttonGrid[row][col] = cell;
					add(cell);
				}
			}

			// add some blocks for testing...
			board.placeBlockAt(new HorizontalBlock(), 0, 0);
			board.placeBlockAt(new HorizontalBlock(), 4, 4);
			board.placeBlockAt(new VerticalBlock(), 1, 3);
			board.placeBlockAt(new VerticalBlock(), 3, 1);
			board.placeBlockAt(new TargetBlock(), 2, 2);
		} else {
			String filePath = new File("").getAbsolutePath();
			String pathToFile = "\\res\\puzzles\\".concat(gameFile);
			filePath = filePath.concat(pathToFile);
			try (BufferedReader textReader = new BufferedReader(new FileReader(
					filePath))) {
				String rowCol = (textReader.readLine()).trim();
				String[] rowAndCol = rowCol.split("\\s");
				newRow = Integer.parseInt(rowAndCol[0]);
				newCol = Integer.parseInt(rowAndCol[1]);

				String[] boardLayout = new String[newRow];
				for (int r = 0; r < newRow; r++) {
					boardLayout[r] = textReader.readLine();
				}
//				System.out.println("got it");
				textReader.close();

				board = new GameBoard(newCol, newRow);
				buttonGrid = new GridButton[newRow][newCol];

				setLayout(new GridLayout(newRow, newCol));
				for (int r = 0; r < newRow; r++) {
					for (int c = 0; c < newCol; c++) {
						GridButton cell = new GridButton(r, c);
						cell.setPreferredSize(new Dimension(64, 64));
						cell.addActionListener(this);
						buttonGrid[r][c] = cell;
						add(cell);
//						System.out.println(boardLayout[r].charAt(c));
						switch ((boardLayout[r].charAt(c))) {
						case 'H':
							board.placeBlockAt(new HorizontalBlock(), r, c);
							break;
						case 'V':
							board.placeBlockAt(new VerticalBlock(), r, c);
							break;
						case 'T':
							board.placeBlockAt(new TargetBlock(), r, c);
							break;
						}
					}
				}

			} catch (FileNotFoundException e) {
				System.out.println("File not found error!");
				System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(SimpleGUI.this, "Error"+e.getMessage(), "File not found error!", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				System.out.println("Fatal error!");
			}
		}

		updateUI();
	}

	// Update display based on the state of the board...
	// TODO: make this more efficient
	private void updateUI() {
		for (int row = 0; row < newRow; row++) {
			for (int col = 0; col < newCol; col++) {
				Block block = board.getBlockAt(row, col);
				JButton cell = buttonGrid[row][col];
				String filePath = new File("").getAbsolutePath();
				if (block == null)
					cell.setIcon(new ImageIcon(filePath	+ "\\res\\images\\darkgray.png"));
				else if (block instanceof TargetBlock)
					cell.setIcon(new ImageIcon(filePath	+ "\\res\\images\\orange.png"));
				else if (block instanceof HorizontalBlock)
					cell.setIcon(new ImageIcon(filePath + "\\res\\images\\blue.png"));
				else if (block instanceof VerticalBlock)
					cell.setIcon(new ImageIcon(filePath	+ "\\res\\images\\red2.png"));
			}
		}
	}

	/**
	 * Handle board clicks.
	 * 
	 * Movement is done by first selecting a block, and then selecting the
	 * destination.
	 * 
	 * Whenever a block is clicked, it is selected, even if another block was
	 * selected before.
	 * 
	 * When an empty cell is clicked after a block is selected, the block is
	 * moved if the move is valid.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Handle grid button clicks...
		GridButton cell = (GridButton) e.getSource();
		int row = cell.getRow();
		int col = cell.getCol();
		System.out.println("Clicked (" + row + ", " + col + ")");

		if (selectedBlock == null || board.getBlockAt(row, col) != null) {
			selectBlockAt(row, col);
		} else {
			boolean win = false;
			if(selectedBlock.getType() && col == newCol-1){
				win = true;
			}
			moveSelectedBlockTo(row, col);
			if(win){
				JOptionPane.showMessageDialog(SimpleGUI.this, "You have won the game", "All your base are belong to me!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
	}

	/**
	 * Select block at a specific location.
	 * 
	 * If there is no block at the specified location, the previously selected
	 * block remains selected.
	 * 
	 * If there is a block at the specified location, the previous selection is
	 * replaced.
	 */
	private void selectBlockAt(int row, int col) {
		Block block = board.getBlockAt(row, col);
		if (block != null) {
			selectedBlock = block;
			selectedBlockRow = row;
			selectedBlockCol = col;
		}
	}

	/**
	 * Try to move the currently selected block to a specific location.
	 * 
	 * If the move is not possible, nothing happens.
	 * @return TODO
	 */
	private boolean moveSelectedBlockTo(int row, int col) {

		int vertDist = row - selectedBlockRow;
		int horzDist = col - selectedBlockCol;

		if (vertDist != 0 && horzDist != 0) {
			System.err.println("Invalid move!");
			return false;
		}

		Direction dir = getMoveDirection(selectedBlockRow, selectedBlockCol,
				row, col);
		int dist = Math.abs(vertDist + horzDist);

		if (!board.moveBlock(selectedBlockRow, selectedBlockCol, dir, dist)) {
			System.err.println("Invalid move!");
			return false;
		} else {
			selectedBlock = null;
			updateUI();
			return true;
		}
	}

	/**
	 * Determines the direction of a move based on the starting location and the
	 * destination.
	 * 
	 * @return <code>null</code> if both the horizontal distance and the
	 *         vertical distance are not zero.
	 */
	private Direction getMoveDirection(int startRow, int startCol, int destRow,
			int destCol) {
		int vertDist = destRow - startRow;
		int horzDist = destCol - startCol;
		if (vertDist < 0)
			return Direction.UP;
		if (vertDist > 0)
			return Direction.DOWN;
		if (horzDist < 0)
			return Direction.LEFT;
		if (horzDist > 0)
			return Direction.RIGHT;
		return null;
	}
}
