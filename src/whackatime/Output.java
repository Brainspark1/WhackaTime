/*
Nihaal Garud, March 30th 2025
Modelling the output page for users to see their average reaction time at the end of a round
 */

package whackatime;

// importing all necessary GUI components
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Output extends JFrame implements ActionListener
{
  // declaring the font used in the rest of the program
  public static final Font MY_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 40);
  
  // declaring the image path and actual image icon for the mole game over image
  public final URL OVER_PATH = getClass().getResource("gameOver.png");
  public final ImageIcon OVER_IMAGE = new ImageIcon(new ImageIcon(
    OVER_PATH).getImage().getScaledInstance(
      250, 250, Image.SCALE_DEFAULT));

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel resultLabel;
  private JLabel moleImgLabel;

  // initializing buttons
  private JButton homeButton;
  private JButton exitButton;
  private JButton newGameButton;

  // initializing panels
  private JPanel buttonPanel;

  public Output(String resultMessage)
  {
    // initializing the frame
    super("Game Over");
    this.setBounds(150, 150, 800, 560);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().setBackground(Color.BLUE);
    this.setResizable(false); // preventing users from resizing the frame, as already at a good size

    // constructing labels
    // constructing label used to display the resulting reaction time
    this.resultLabel = new JLabel(resultMessage, SwingConstants.CENTER);
    resultLabel.setFont(MY_FONT); // setting the font to the font declared above
    resultLabel.setForeground(Color.WHITE); // setting the text colour to white
    
    // constructing label used to display the image of the mole game over to the users
    this.moleImgLabel = new JLabel(OVER_IMAGE);

    // constructing buttons
    // constructing the button to allow users to navigate home
    this.homeButton = new JButton("Return Home");
    homeButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // constructing the button to allow users to exit the game
    this.exitButton = new JButton("Exit Game");
    exitButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // constructing the button to allow users to play again immediately 
    this.newGameButton = new JButton("Play Again");
    newGameButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing panels
    // using box layout to create vertical stack of buttons close together in the center of the frame
    this.buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // allowing components to be stacked in the panel vertically
    buttonPanel.setBackground(Color.BLUE); // setting background colour of the panel to blue

    // setting center alignment for components in box layout along the x-axis when added to the buttonPanel
    homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    // adding components to the button panel, vertically stacking them with BoxLayout
    buttonPanel.add(Box.createVerticalGlue()); // creating a filler component to manage extra space on the top of the panel while stacking the components in the panel on top of each other
    buttonPanel.add(Box.createVerticalStrut(40)); // creating 40 px of vertical space at the top
    buttonPanel.add(newGameButton); // adding the new game button to the top of the stack
    buttonPanel.add(Box.createVerticalStrut(10)); // creating 10 px of vertical space between components
    buttonPanel.add(homeButton); // adding the navigate home button to the middle of the stack
    buttonPanel.add(Box.createVerticalStrut(10)); // creating 10 px of vertical space between components
    buttonPanel.add(exitButton); // adding the exit button to the bottom of the stack
    buttonPanel.add(Box.createVerticalStrut(30)); // creating 30 px of vertical space at the bottom
    buttonPanel.add(Box.createVerticalGlue()); // creating a filler component to manage extra space on the bottom of the panel while stacking the components in the panel on top of each other

    // adding components to the frame
    this.add(moleImgLabel, BorderLayout.NORTH);
    this.add(resultLabel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH, SwingConstants.CENTER);
    
    // making the frame visible when opened
    this.setVisible(true);
  }

  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the identity/name of button that was clicked when the button press was registered
    Object command = e.getSource();
    
    // if the user chose the button to navigate home ...
    if (command == homeButton)
    {
      // ... close the current Output window, and open a new Home frame
      this.dispose();
      new Home();
    }
    // if the user chose the button to exit the application ...
    else if (command == exitButton)
    {
      // ... exit the program
      System.exit(0);
    }
    // if the user chose the button to start a new game ...
    else if (command == newGameButton)
    {
      // ... close the current Outupt window, and open a new game frame for users to play
      this.dispose();
      new PlayGame();
    }
  }
  
  // main method to test Output GUI
  public static void main(String[] args)
  {
    // sample reaction time value to be displayed
    double reactionTime = 0.694;
    
    // new Output window/frame displaying this reaction time as its message
    new Output("<html><center>Game Over!<br><i>Average Reaction Time: " + reactionTime + " sec</i></center></html>");
  }
}
