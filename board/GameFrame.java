package board;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import common.WindowUtil;
import option.GameType;
import option.OptionDialog;
import main.AboutDialog;
import status.GameInfoBoard;
import status.HighScoreDialog;
import status.PlayerScore;
import status.PlayerScoreHistory;

public class GameFrame extends JFrame {

	public GameFrame() {
		setTitle("LINES");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		ImageIcon icon = new ImageIcon("resources/logo.png");
		setIconImage(icon.getImage());

		gamePanel = new GamePanel(this);
		add(gamePanel);

		GameBoard gameBoard = new GameBoard(gamePanel);
		gamePanel.setGameBoard(gameBoard);

		setJMenuBar(new JMenuBar());
		addGameMenu();
		addControlMenu();
		addHelpMenu();

		pack();
		setResizable(false);

		WindowUtil.centerOwner(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				saveHighScore();
			}
		});
	}

	public void endGame() {
		saveHighScore();
		gamePanel.getGameBoard().newGame();
	}

	private void addGameMenu() {
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');

		JMenuItem newMenuItem;
		gameMenu.add(newMenuItem = new JMenuItem("New", 'N'));
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		newMenuItem.addActionListener((e) -> {
			saveHighScore();
			gamePanel.getGameBoard().newGame();
		});

		JMenuItem newLinesMenuItem = new JMenuItem("New Lines Game", 'L');
		gameMenu.add(newLinesMenuItem);
		newLinesMenuItem.addActionListener((e) -> {
			saveHighScore();
			gamePanel.getGameBoard().newGame(GameType.LINE);
		});

		JMenuItem newSquaresMenuItem = new JMenuItem("New Squares Game", 'S');
		gameMenu.add(newSquaresMenuItem);
		newSquaresMenuItem.addActionListener((e) -> {
			saveHighScore();
			gamePanel.getGameBoard().newGame(GameType.SQUARE);
		});

		JMenuItem newBlocksMenuItem = new JMenuItem("New Blocks Game", 'B');
		gameMenu.add(newBlocksMenuItem);
		newBlocksMenuItem.addActionListener((e) -> {
			saveHighScore();
			gamePanel.getGameBoard().newGame(GameType.BLOCK);
		});

		gameMenu.addSeparator();

		JMenuItem optionsMenuItem = new JMenuItem("Options", 'O');
		gameMenu.add(optionsMenuItem);
		optionsMenuItem.addActionListener((e) -> {
			OptionDialog optionDialog = new OptionDialog(this);
			optionDialog.setVisible(true);
			gamePanel.repaint();
		});

		gameMenu.addSeparator();

		JMenuItem saveGameMenuItem = new JMenuItem("High Scores", 'H');
		gameMenu.add(saveGameMenuItem);
		saveGameMenuItem.addActionListener((e) -> {
			showHighScoreDialog();
		});

		getJMenuBar().add(gameMenu);
	}

	private void addControlMenu() {
		JMenu controlMenu = new JMenu("Control");
		controlMenu.setMnemonic('C');

		JMenuItem saveGameMenuItem = new JMenuItem("Save Game", 'S');
		controlMenu.add(saveGameMenuItem);
		saveGameMenuItem.addActionListener((e) -> {
			gamePanel.getGameBoard().saveGame();
		});

		JMenuItem loadGameMenuItem = new JMenuItem("Load Game", 'L');
		controlMenu.add(loadGameMenuItem);
		loadGameMenuItem.addActionListener((e) -> {
			gamePanel.getGameBoard().loadGame();
		});

		JMenuItem endGameMenuItem = new JMenuItem("End Game", 'E');
		controlMenu.add(endGameMenuItem);
		endGameMenuItem.addActionListener((e) -> {
			endGame();
		});

		controlMenu.addSeparator();

		JMenuItem stepBackMenuItem = new JMenuItem("Step back");
		stepBackMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		controlMenu.add(stepBackMenuItem);
		stepBackMenuItem.addActionListener((e) -> {
			gamePanel.getGameBoard().stepBack();
		});

		getJMenuBar().add(controlMenu);
	}

	private void addHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');

		JMenuItem aboutMenuItem = new JMenuItem("About", 'A');
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener((e) -> {
			AboutDialog aboutDialog = new AboutDialog(GameFrame.this);
			aboutDialog.setVisible(true);

		});

		getJMenuBar().add(helpMenu);
	}

	private void saveHighScore() {
		GameInfoBoard gameInfoBoard = gamePanel.getGameBoard().getGameInfoBoard();

		// Stop the playing clock
		gameInfoBoard.setClockState(false);

		PlayerScoreHistory playerScoreHistory = PlayerScoreHistory.getInstance();

		// Player gets a new high score
		if (playerScoreHistory.isNewRecord(gameInfoBoard.getScore().getScore())) {
			String playerName = JOptionPane.showInputDialog(GameFrame.this,
					"You've got a high score. Please input your name", "New high score", JOptionPane.QUESTION_MESSAGE);
			if (playerName != null && !"".equals(playerName)) {
				// Add a new record to high score history
				playerScoreHistory.addHighScore(new PlayerScore(playerName, gameInfoBoard.getScore().getScore(),
						gameInfoBoard.getClock().toString()));
				playerScoreHistory.save();

				showHighScoreDialog();
			}
		}

		// Update highest score on the game status board
		gameInfoBoard.getHighestScore().setScore(playerScoreHistory.getHighestScore());
	}

	private void showHighScoreDialog() {
		HighScoreDialog highScoreDialog = new HighScoreDialog(GameFrame.this);
		highScoreDialog.setVisible(true);
	}

	private GamePanel gamePanel;
	private static final long serialVersionUID = -8199515970527728642L;
}