package io.github.spl.game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import io.github.spl.game.actions.*;
import io.github.spl.player.*;
import io.github.spl.ships.*;
import io.github.spl.protocol.*;

public class GUIGameView extends GameView {

    private JFrame frame;
    private JTextArea messageArea;
    private JPanel yourGridPanel;
    private JPanel opponentGridPanel;
    private JButton[][] yourGridButtons;
    private JButton[][] opponentGridButtons;
    private int gridWidth;
    private int gridHeight;

    public GUIGameView() {
        super();

        // Main Frame Setup
        frame = new JFrame("Battleships");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Message Area
        messageArea = new JTextArea(8, 50);
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Grids
        JPanel gridsContainer = new JPanel(new GridLayout(1, 2));
        yourGridPanel = new JPanel();
        opponentGridPanel = new JPanel();
        gridsContainer.add(yourGridPanel);
        gridsContainer.add(opponentGridPanel);
        frame.add(gridsContainer, BorderLayout.CENTER);

        // Set default dimensions for the grids
        gridWidth = 10;  // Default grid width
        gridHeight = 10; // Default grid height

        //initializeGrids(); // Initialize the grids immediately

        frame.setVisible(true);
    }

    // Initialize both grids
    public void initializeGrids() {
        yourGridPanel.removeAll();
        opponentGridPanel.removeAll();

        yourGridPanel.setLayout(new GridLayout(gridHeight, gridWidth));
        opponentGridPanel.setLayout(new GridLayout(gridHeight, gridWidth));

        yourGridButtons = new JButton[gridHeight][gridWidth];
        opponentGridButtons = new JButton[gridHeight][gridWidth];

        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                // Your Grid
                JButton yourButton = new JButton();
                yourButton.setBackground(Color.BLUE);
                yourButton.setEnabled(false); // Not clickable
                yourGridButtons[row][col] = yourButton;
                yourGridPanel.add(yourButton);

                // Opponent Grid
                JButton opponentButton = new JButton();
                opponentButton.setBackground(Color.BLUE);
                final int r = row, c = col;
                opponentButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        handleOpponentGridClick(r, c);
                    }
                });
                opponentGridButtons[row][col] = opponentButton;
                opponentGridPanel.add(opponentButton);
            }
        }

        yourGridPanel.revalidate();
        yourGridPanel.repaint();
        opponentGridPanel.revalidate();
        opponentGridPanel.repaint();
    }

    protected void processRequestCoordinates(RequestCoordinates action) {
        HumanPlayer humanPlayer = (HumanPlayer) action.getPlayer();

        gridWidth = game.getGameType().getDimension().getWidth();
        gridHeight = game.getGameType().getDimension().getHeight();

        initializeGrids();

        messageArea.append("Select a coordinate to hit.\n");
        enableOpponentGridButtons();
    }

    private void enableOpponentGridButtons() {
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                opponentGridButtons[row][col].setEnabled(true);
            }
        }
    }

    private void handleOpponentGridClick(int row, int col) {
        disableOpponentGridButtons();

        if (checkListHits(row, col, ((HumanPlayer) game.getPlayer1()).getGameGrid())) {
            ((HumanPlayer) game.getPlayer1()).getCommandQueue().add(new ResponseCoordinate(row, col));
            messageArea.append("Coordinate selected: (" + row + ", " + col + ").\n");
        } else {
            messageArea.append("Invalid selection. Try again.\n");
            enableOpponentGridButtons();
        }
    }

    private void disableOpponentGridButtons() {
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                opponentGridButtons[row][col].setEnabled(false);
            }
        }
    }

    protected void processHit(Hit action) {
        Coordinate coordinate = action.getCoordinate();
        opponentGridButtons[coordinate.getX()][coordinate.getY()].setBackground(Color.RED);
        opponentGridButtons[coordinate.getX()][coordinate.getY()].setText("X");
        messageArea.append("Hit at " + coordinate + "!\n");
    }

    protected void processMiss(Miss action) {
        Coordinate coordinate = action.getMissCoordinate();
        opponentGridButtons[coordinate.getX()][coordinate.getY()].setBackground(Color.WHITE);
        opponentGridButtons[coordinate.getX()][coordinate.getY()].setText("O");
        messageArea.append("Miss at " + coordinate + ".\n");
    }

    protected void processDamage(Damage action) {
        processHit(new Hit(action.getAttacker(), action.getHitCoordinate()));
        messageArea.append("Ship \"" + action.getShipName() + "\" was damaged.\n");
    }

    protected void processSinkage(Sinkage action) {
        processHit(new Hit(action.getAttacker(), action.getLastCoordinateHit()));
        messageArea.append("Ship \"" + action.getShipName() + "\" sank.\n");
    }

    protected void processGameWin(GameWin action) {
        messageArea.append("Player " + action.getPlayer().getName() + " won the game!\n");
        JOptionPane.showMessageDialog(frame, "Player " + action.getPlayer().getName() + " won the game!");
    }

    public boolean checkListHits(int x, int y, GameGrid gameGrid) {
        for (ShipCoordinate coord : gameGrid.getListOfCoordsHit()) {
            if (coord.getX() == x && coord.getY() == y) {
                return false;
            }
        }
        return true;
    }

    public void setupFleetFromUserInput(HumanPlayer player, List<ShipTemplate> shipTemplates) {
        messageArea.append("Place your fleet.\n");
        GameGrid gameGrid = player.getGameGrid();
        List<Ship> ships = new ArrayList<>();

        for (ShipTemplate template : shipTemplates) {
            boolean placed = false;

            while (!placed) {
                String input = JOptionPane.showInputDialog(frame,
                        "Place ship: " + template.getName() +
                                " (Length: " + template.getCoordinates().size() + ")\nEnter starting row, column, and rotation (0=Horizontal, 1=Vertical):");
                if (input == null) continue;

                String[] parts = input.split(",");
                if (parts.length < 3) {
                    messageArea.append("Invalid input. Try again.\n");
                    continue;
                }

                try {
                    int row = Integer.parseInt(parts[0].trim());
                    int col = Integer.parseInt(parts[1].trim());
                    int rotation = Integer.parseInt(parts[2].trim());

                    Coordinate startingCoordinate = new Coordinate(row, col);
                    Ship ship = new Ship(template, startingCoordinate, rotation);

                    if (GameView.canPlaceShip(ship, ships, gameGrid)) {
                        ships.add(ship);
                        player.addShip(template, startingCoordinate, rotation);
                        placed = true;

                        yourGridButtons[row][col].setBackground(Color.YELLOW);
                        messageArea.append("Ship placed successfully.\n");
                    } else {
                        messageArea.append("Invalid placement. Try again.\n");
                    }
                } catch (NumberFormatException e) {
                    messageArea.append("Invalid input. Try again.\n");
                }
            }
        }
    }
}
