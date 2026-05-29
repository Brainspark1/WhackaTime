/*
Nihaal Garud, March 30th 2026
Modelling the page for users to start and play a new Whack-a-Time game
 */

package whackatime;

// importing all necessary GUI components 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// importing all necessary components to play audio/sound
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

// importing all necessary SQL components
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayGame extends JFrame implements ActionListener
{
  // declaring the font used in the rest of the program
  public final Font BIG_FONT = new Font("Chalkboard", Font.BOLD, 25);
  public final Font BIGGER_FONT = new Font("Chalkboard", Font.BOLD, 100);
  
  // declaring the image path and actual image icon for the mole button image
  public final URL MOLE_PATH = getClass().getResource("mole.png");
  public final ImageIcon MOLE_IMAGE = new ImageIcon(new ImageIcon(
    MOLE_PATH).getImage().getScaledInstance(
    165, 125, Image.SCALE_DEFAULT));

  // declaring the image path and actual image icon for the gaming controller image
  public final URL GAMING_PATH = getClass().getResource("gaming.jpeg");
  public final ImageIcon GAMING_IMAGE = new ImageIcon(new ImageIcon(
    GAMING_PATH).getImage().getScaledInstance(
    400, 225, Image.SCALE_DEFAULT));

  // declaring the color used in the rest of the program for the menu bar on the frame
  public final Color TOP_BLUE = new Color(145, 162, 217);

  // declaring constants used in the rest of the program
  public final int ROWS = 6; // number of rows of GridLayout grid
  public final int COLUMNS = 6; // number of columns of GridLayout grid
  private final double LINE_WIDTH = 0.0001; // width of lines separating grids in GridLayout

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing buttons
  private JButton startButton;
  private JButton homeButton;
  private JButton moleButton;

  // initializing panels
  private JPanel startPanel;
  // panel overlaid with a grid to place moles randomly in it 
  private JPanel gridPanel;

  // initializing labels
  private JLabel instructionsLabel;
  private JLabel gamingImgLabel;
  private JLabel countLabel;

  // declaring new moles counter window to update the remaining number of moles text on it once the user clicks a mole
  private MolesCounter counterWindow;

  // initializing timer
  private Timer countdownTimer;
  
  // variable holding the amount of time left for the countdown timer
  private int countLeft;

  // initializing object of the logic/computation class
  private WhackTime objTime;

  // initializing audio streams to play music and sound effects
  private AudioInputStream musicStream; // audio stream for background music
  private AudioInputStream effectsStream; // audio stream for sound effects
  
  // initializing clips to play music and sound effects
  private Clip musicClip; // clip for background music
  private Clip effectsClip; // clip for sound effects

  public PlayGame()
  {
    // initializing the frame
    super("Play Game");
    this.setBounds(100, 50, 850, 700);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Color.BLACK);
    this.setLayout(new BorderLayout());

    // constructing buttons
    // constructing button for users to start a new game
    this.startButton = new JButton("Start Game");
    startButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // constructing button for users to navigate home
    this.homeButton = new JButton("Home");
    homeButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // constructing button for the actual moving mole along the grid for users to click
    this.moleButton = new JButton(MOLE_IMAGE);
    moleButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // constructing panels
    // constructing panel to contain buttons
    this.startPanel = new JPanel();
    startPanel.setBackground(TOP_BLUE); // setting the background colour to the blue colour declared above
    
    // constructing panel with an overlaid grid layout of 6 rows, 6 columns and type-casted very small widths for the mail to be randomly moved around in
    this.gridPanel = new JPanel(new GridLayout(ROWS, COLUMNS, (int) LINE_WIDTH, (int) LINE_WIDTH)); // note - typecasting the line width constant declared above, as grid layout requires integer widths
    gridPanel.setBackground(Color.BLACK); // setting the background colour to black

    // constructing labels
    // constructing label to display to users how much time they have left in the countdown timer
    this.countLabel = new JLabel(String.valueOf(countLeft), SwingConstants.CENTER);
    countLabel.setFont(BIGGER_FONT); // setting the font of the countdown text to the larger font declared above
    countLabel.setForeground(Color.WHITE); // setting the colour to white
    
    // constructing label showing the instructions for users to play the game
    this.instructionsLabel = new JLabel(
      "<html><center>"
      + "Click Start Game above to begin.<br><br>"
      + "A mole will appear randomly in one of the grid cells.<br>"
      + "Click the mole as quickly as possible each time it appears.<br><br>"
      + "The mole will move to a new location after every click.<br>"
      + "The game ends after 30 moles have been clicked.<br><br>"
      + "Your average reaction time will be calculated as your score.<br><br>"
      + "Good Luck!<br>"
      + "</center></html>", SwingConstants.CENTER);
    instructionsLabel.setForeground(Color.WHITE); // setting the colour to white
    instructionsLabel.setFont(BIG_FONT); // setting the font to the big font declared above
    
    // constructing label to display the image of the gaming controller to users
    this.gamingImgLabel = new JLabel(GAMING_IMAGE);

    // adding buttons to the start panel placed at the top of the frame
    startPanel.add(startButton);
    startPanel.add(homeButton);

    // adding components to different parts of the frame
    this.add(startPanel, BorderLayout.NORTH);
    this.add(instructionsLabel, BorderLayout.CENTER);
    this.add(gamingImgLabel, BorderLayout.SOUTH);

    // prevents users from changing the size of the window, as already at a good set window size
    this.setResizable(false);
    
    // making the frame visible when opened
    this.setVisible(true);
  }

  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the identity/name of button that was clicked when the button press was registered
    Object command = e.getSource();

    // boolean used to see if the user should continue to play their game if they haven't yet reached the 30 moles limit
    boolean continueGame;
    
    // double value holding the calculated average reaction time to display to users at the end of their game
    double averageTime;

    // db info
    String dbName = "WhackATimeHistory";
    String currentUser = UserManager.getCurrentUser();
    String tableName = "TimingHistory_" + currentUser;
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    // insert query to insert row into MassCalculations table with given/calculated atomicMass, number of protons and number of neutrons
    String dbQuery = "INSERT INTO " + tableName + " (reactionTime) VALUES (?)";
    // connect to db
    JavaDatabase objDb = new JavaDatabase(dbName);
    objDb.createUserTable(currentUser);
    Connection myConn = objDb.getDbConn();

    // if the user chose to start a new game ...
    if (command == startButton)
    {
      // ... remove everything initially existing on the window
      this.remove(startPanel);
      this.remove(instructionsLabel);
      this.remove(gamingImgLabel);
      
      // add the label displaying the countdown timer before the game starts
      this.add(countLabel, BorderLayout.CENTER);

      // constructing new object of the computation/logic class
      objTime = new WhackTime();

      // starting countdown at 3
      countLeft = 3;

      // setting the label displaying the countdown timer to the amount of time left on the countdown
      countLabel.setText(String.valueOf(countLeft));

      // initializing and starting countdown timer
      countdownTimer = new Timer(1000, this); // delay 1000 milliseconds = 1 second
      countdownTimer.start();

      // opening and playing the count sound effect when the countdown timer initially starts
      openSoundEffect("countSound.wav");
      playSoundEffect();
    }
    // using countdown timer
    else if (command == countdownTimer)
    {
      // lower count left by 1 after 1 second has passed 
      countLeft--;

      // if still time left
      if (countLeft > 0)
      {
        // opening and playing the count sound effect when the countdown timer changes
        openSoundEffect("countSound.wav");
        playSoundEffect();

        // set label equal to how many seconds is left
        countLabel.setText(String.valueOf(countLeft));
      }
      // if time in the countdown has run out
      else
      {
        // stop timer
        countdownTimer.stop();

        // remove countdown timer label, and add grid for the actual mole game functionality to the center
        this.remove(countLabel);
        this.add(gridPanel, BorderLayout.CENTER);

        // randomly moving the mole button along the grid
        objTime.moveMole(moleButton, gridPanel);
        
        // creating new instance of the MolesCounter window to display the number of moles left for the user
        counterWindow = new MolesCounter();

        // prevents the game window from disappearing behind other open windows in case user clicks off the window and onto the background - from testing
        this.setAlwaysOnTop(true);

        // opening and playing the background music once the new game starts
        openMusic("clownAround.wav");
        playMusic();

        // setting default close operation to nothing to allow program to show warning to users on close
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        // adding a listener to the window for when it closes
        this.addWindowListener(new WindowAdapter()
        {
          // when the game window starts to close/the user tries to close the window ...
          @Override
          public void windowClosing(WindowEvent e)
          {
            // show a new close warning frame to warn the player that they are about to quit their game in the middle of their game
            new CloseWarning(PlayGame.this); // passing this PlayGame frame into the new warning frame
          }
        });

        // validating and repainting the frame to allow the changes made to show up
        validate();
        repaint();
      }
    }
    // if the user chose to press the mole button during the game ...
    else if (command == moleButton)
    {
      // use the computation class to record that a click has occurred in order to move the button
      objTime.recordClick();

      // opening and playing the coin sound effect when a mole button is pressed
      openSoundEffect("coinSound.wav");
      playSoundEffect();

      // set the text on the moles counter window to the number of moles remaining
      counterWindow.setMolesRemaining(objTime.getRemainingMoles());

      // move the mole to its new random location in the grid, recording its boolean return value
      continueGame = objTime.moveMole(moleButton, gridPanel);

      // if the user cannot continue their game/they have reached 30 moles ...
      if (!continueGame)
      {
        // if a user is logged in ...
        if (currentUser != null)
        {
          // get their average reaction time
          averageTime = objTime.getAverageTime();

          // stopping all music, including background and sound effects, from playing as the game has finished
          stopMusic();
          closeMusic();

          // the following try-catch block tries to insert the recorded average reaction time for the user into their own respective past history table, catching any errors that may have been thrown in the process
          try
          {
            // insert data into database
            // creating a PreparedStatement from SQL insert query to insert resulting data into running database
            PreparedStatement ps = myConn.prepareStatement(dbQuery);
            // enter data into query
            ps.setDouble(1, averageTime);
            // execute the query
            ps.executeUpdate();

            // letting the programmers know that these processes have been executed successfully
            System.out.println("Inserted data for user: " + currentUser);
          }
          // catching any SQL errors that may have been thrown in these processes
          catch (SQLException se)
          {
            // printing a message for the programmers to let them know that the processes have been unsuccessfully executed
            System.out.println("Error inserting data for user " + currentUser);
          }

          // closing this window and the open MolesCounter window at the end of the game
          this.setAlwaysOnTop(false);
          this.dispose();
          counterWindow.setAlwaysOnTop(false);
          counterWindow.dispose();
          
          // creating a new Output window/frame showing the average reaction time for the user from this round, rounding the value to 3 decimal places
          new Output("<html><center>Game Over!<br><i>Average Reaction Time: " + String.format("%.3f", averageTime) + " sec</i></center></html>");
        }
        // if there is no user logged in ...
        else
        {
          // ... let the programmers know about this
          System.out.println("Username not set.");
        }
      }
    }
    // if the user wants to navigate home ...
    else if (command == homeButton)
    {
      // ... close this current window
      this.dispose();
      // open a new Home window/frame
      new Home();
    }
  }

  // method accessed by other classes to close the also open MolesCounter window
  public void closeMolesCounterWindow()
  {
    // disposing the counter window
    counterWindow.dispose();
  }

  // method to open music with a particular file name
  public void openMusic(String fileName)
  {
    // the following try-catch block tries to open a music file, catching any errors that may have been thrown in the process
    try
    {
      musicStream = AudioSystem.getAudioInputStream(
        getClass().getResource(fileName));
      musicClip = AudioSystem.getClip();
      musicClip.open(musicStream);
    }
    // catching any errors that may have been thrown in the process
    catch (Exception e)
    {
      // creating a new Warning window/frame letting the users know
      new Warning("Error turning your music on!");
    }
  }

  // method to start and continuously loop the background music
  public void playMusic()
  {
    // the following try-catch block tries to start and loop the background music, catching any errors that may have been thrown in the process
    try
    {
      musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    // catching any errors that may have been thrown in the process
    catch (Exception e)
    {
      // creating a new Warning window/frame letting the users know
      new Warning("Error playing your music!");
    }
  }

  // method to stop the looping background music
  public void stopMusic()
  {
    // the following try-catch block tries to stop the looping background music, catching any errors that may have been thrown in the process
    try
    {
      // stopping the music clip
      musicClip.stop();
      // moving the playhead all the way to the front of the music clip to reset the music
      musicClip.setFramePosition(0);
    }
    // catching any errors that may have been thrown in the process
    catch (Exception e)
    {
      // creating a new Warning window/frame letting the users know
      new Warning("Error stopping your music!");
    }
  }

  // method to close the music
  public void closeMusic()
  {
    // the following try-catch block tries to close the background music clip, catching any errors that may have been thrown in the process
    try
    {
      // closing the music clip
      musicClip.close();
    }
    // catching any errors that may have been thrown in the process
    catch (Exception e)
    {
      // creating a new Warning window/frame letting the users know
      new Warning("Error closing your music!");
    }
  }

  // method to open sound effects with a particular file name
  public void openSoundEffect(String fileName)
  {
    // the following try-catch block tries to open a sound effect file, catching any errors that may have been thrown in the process
    try
    {
      effectsStream = AudioSystem.getAudioInputStream(
        getClass().getResource(fileName));
      effectsClip = AudioSystem.getClip();
      effectsClip.open(effectsStream);
    }
    // catching any errors that may have been thrown in the process
    catch (Exception e)
    {
      // creating a new Warning window/frame letting the users know
      new Warning("Error starting sound effect!");
    }
  }

  // method to play a sound effect
  public void playSoundEffect()
  {
    // the following try-catch block tries to play a sound effect, catching any errors that may have been thrown in the process
    try
    {
      // starting the sound effect loaded into the clip
      effectsClip.start();
    }
    // catching any errors that may have been thrown in the process
    catch (Exception e)
    {
      // creating a new Warning window/frame letting the users know
      new Warning("Error playing sound effect!");
    }
  }
 
  // main method to test PlayGame GUI
  public static void main(String[] args)
  {
    // new PlayGame window/frame
    new PlayGame();
  }
}
