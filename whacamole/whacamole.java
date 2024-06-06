import java.awt.*;
import java.awt.event.*;
import java.util.Random;//To randomise the chicken's position
import java.util.ArrayList;
import java.util.List;


import javax.swing.*;//We got Jrame from here

public class whacamole {
    int boardWidth = 600;
    int boardHeight = 650;//Took 50px extra for the text on top 
    
    JFrame frame = new JFrame("whacamole!");//Title of the game
    JLabel textLabel = new JLabel();//For the text
    JPanel textPanel = new JPanel();//Panel for the text
    JPanel boardPanel = new JPanel();//Panel for the game

    JButton[] board = new JButton[9];//To keep track of 9 buttons
    ImageIcon ChickenIcon;//Image 1 for the chicken
    ImageIcon tntIcon;//Image 2 for the deadly tnt
    ImageIcon specialItemIcon;//Image 3 for the special item

    JButton currChickenTile;//Stores current chicken tile
    List<JButton> currTntTiles = new ArrayList<>(); // Store current TNT tiles

    Random random = new Random();
    Timer setChickenTimer;//Sets the timer after which the chicken tile is changed
    Timer setTntTimer;//Sets the timer after which the tnt tile is changed
    int score = 0;//Keep track of the score of game

    whacamole () {
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

        Image tntImg = new ImageIcon(getClass().getResource("./tnt.png")).getImage();//Gets image from the source
        tntIcon = new ImageIcon(tntImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));//Scales the image down so that it fits in the tile

        Image chickenImg = new ImageIcon(getClass().getResource("./chicken.png")).getImage();
        ChickenIcon = new ImageIcon(chickenImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image specialItemImg = new ImageIcon(getClass().getResource("./gapple.png")).getImage();
        specialItemIcon = new ImageIcon(specialItemImg.getScaledInstance(150, 150, Image.SCALE_SMOOTH));


        //For loop to create 9 buttons
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;//Tile is a Jbutton and we add it to the boardPanel
            boardPanel.add(tile);
            tile.setFocusable(false);//Removes the weird box around the tile we clicked
    

            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();
                    if (tile == currChickenTile) {
                        if (tile.getIcon() == specialItemIcon) {
                            score += 50; // Increase score by 50 for special item
                        } else {
                            score += 5; // Increase score by 10 for chicken
                        }
                        textLabel.setText("Score: " + score);
                        tile.setIcon(null);
                        currChickenTile = null;
                    }
                    else if (currTntTiles.contains(tile)) {
                        textLabel.setText("Game Over: " + score);
                        setChickenTimer.stop();
                        setTntTimer.stop();
                        for (JButton button : board) {
                            button.setEnabled(false);
                        }
                    }
                }
            });
	}
    //sets the button's background to be green
    Color bgColor = new Color(91,135,49);
    for (int i = 0 ; i < board.length ; i++) {
        board[i].setBackground(bgColor);
    }

    setChickenTimer = new Timer(600, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (currChickenTile != null) {
                currChickenTile.setIcon(null);
                currChickenTile = null;
            }

            // Find available tiles
            List<JButton> availableTiles = new ArrayList<>();
            for (JButton tile : board) {
                if (!currTntTiles.contains(tile)) {
                    availableTiles.add(tile);
                }
            }

            // Place chicken on a random available tile
            if (!availableTiles.isEmpty()) {
                int num = random.nextInt(availableTiles.size());
                currChickenTile = availableTiles.get(num);

                // Determine if a special item should appear
                if (random.nextInt(50) == 0) {
                    currChickenTile.setIcon(specialItemIcon);
                } else {
                    currChickenTile.setIcon(ChickenIcon);
                }
            }
        }
    });
    

            //Setting tnt timer here
            setTntTimer = new Timer(750, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (JButton tile : currTntTiles) {
                        tile.setIcon(null);
                    }
                    currTntTiles.clear();
    
                    int numTnts = random.nextInt(3) + 1; // Randomly choose 1 to 3 TNTs
                    for (int i = 0; i < numTnts; i++) {
                        int num = random.nextInt(9);
                        JButton tile = board[num];
    
                        if (tile != currChickenTile && !currTntTiles.contains(tile)) {
                            currTntTiles.add(tile);
                            tile.setIcon(tntIcon);
                        }
                    }
                }
            });


            setChickenTimer.start();
            setTntTimer.start();
            frame.setVisible(true);//Ensures all the components are loaded first, before actually displaying them -bug v1 fix
    }
}