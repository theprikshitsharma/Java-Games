import java.awt.*;
import java.awt.event.*;
import java.util.Random; //To randomise the moles position

import javax.swing.*; //We got Jrame from here

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 650; //Took 50px extra for the text on top 
    
    JFrame frame = new JFrame("Mario: Whack A Mole");
    JLabel textLabel = new JLabel(); //For the text
    JPanel textPanel = new JPanel(); //Panel for the text
    JPanel boardPanel = new JPanel(); //Panel for the game

    JButton[] board = new JButton[9];//To keep track of 9 buttons
    ImageIcon moleIcon; //Image 1 for the mole
    ImageIcon plantIcon;//Image 2 for the deadly plant

    JButton currMoleTile;//Stores current mole tile
    JButton currPlantTile;//Stores current plant tile

    Random random = new Random();
    Timer setMoleTimer;//Sets the timer after which the mole tile is changed
    Timer setPlantTimer;//Sets the timer after which the plant tile is changed
    int score = 0;//Keep track of the score of game

    WhacAMole () {
        // frame.setVisible(true); //-bug v1 made window visible even if the components were not loaded (fixed)
        frame.setSize(boardWidth , boardHeight);
        frame.setLocationRelativeTo(null); //Will set the position of the screen in the centre
        frame.setResizable(false);//Stops user from resizing :(
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Will terminate the program when we click X button ;)
        frame.setLayout(new BorderLayout());

        //text label
        textLabel.setFont(new Font("Arial" , Font.PLAIN, 50));//Font Family
        textLabel.setHorizontalAlignment(JLabel.CENTER);//Center the text rather than having it start from left hand side
        textLabel.setText("Score: 0");
        textLabel.setOpaque(true);

        //text panel
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);//Adds the textlabel to textpanel
        frame.add(textPanel , BorderLayout.NORTH);//Adds the textpanel to frame and moves it to top

        //board panel
        boardPanel.setLayout(new GridLayout(3 , 3));//Adds a 3X3 panel 
        frame.add(boardPanel);

        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();//Gets image from the source
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));//Scales the image down so that it fits in the tile

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));


        //For loop to create 9 buttons
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;//Tile is a Jbutton and we add it to the boardPanel
            boardPanel.add(tile);
            tile.setFocusable(false);//Removes the weird box around the tile we clicked
    

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currMoleTile) {
                        score += 10;
                        textLabel.setText("Score: " + Integer.toString(score));
                    }
                    else if (tile == currPlantTile) {
		                textLabel.setText("Game Over: " + Integer.toString(score));
                        setMoleTimer.stop();
                        setPlantTimer.stop();
                        for (int i = 0; i < 9; i++) {
                            board[i].setEnabled(false);
                        }
                    }
                }
            });
	}

        //Setting mole timer here
            setMoleTimer = new Timer(1000, new ActionListener() { //1000ms = 1s
                public void actionPerformed(ActionEvent e) { //Every 1s we call actionPerfomed
                    //Remove mole from current tile
                    if (currMoleTile != null) { //There is already a Jbutton to current tile
                        currMoleTile.setIcon(null);//Make it invisible to the user
                        currMoleTile = null;//Remove the image from button
                    }

                    //Randomly select another tile
                    int num = random.nextInt(9);
                    JButton tile = board[num];

                    //If tile is occupied by plant, skip tile for this turn
                    if (currPlantTile == tile)
                    return;

                    //Set tile to mole
                    currMoleTile = tile;
                    currMoleTile.setIcon(moleIcon);
                }
            });

            //Setting plant timer here
            setPlantTimer = new Timer(1500, new ActionListener() { //1000ms = 1s
                public void actionPerformed(ActionEvent e) { //Every 1s we call actionPerfomed
                    //Remove mole from current tile
                    if (currPlantTile != null) { //There is already a Jbutton to current tile
                        currPlantTile.setIcon(null);//Make it invisible to the user
                        currPlantTile = null;//Remove the image from button
                    }

                    //Randomly select another tile
                    int num = random.nextInt(9);
                    JButton tile = board[num];

                    //If tile is occupied by mole, skip tile for this turn
                    if (currMoleTile == tile)
                    return;

                    //Set tile to mole
                    currPlantTile = tile;
                    currPlantTile.setIcon(plantIcon);
                }
            });

            setMoleTimer.start();
            setPlantTimer.start();
            frame.setVisible(true);//Ensures all the components are loaded first, before actually displaying them -bug v1 fix
    }
}