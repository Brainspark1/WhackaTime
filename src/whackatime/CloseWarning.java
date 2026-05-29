/*
Nihaal Garud, April 7th 2026
Modelling a warning frame if users try to exit their game before finishing
 */

package whackatime;

// importing all necessary GUI components
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CloseWarning extends JFrame implements ActionListener
{
  // declaring the font used in the rest of the program
  public final Font BIG_FONT = new Font("Chalkboard", Font.BOLD, 30);
  
  // declaring the path and image of the warning triangle image loaded into this project to be displayed along with the warning frame below
  public final URL WARNING_PATH = getClass().getResource("warningTriangle.png");
  public final ImageIcon WARNING_IMAGE = new ImageIcon(new ImageIcon(
    WARNING_PATH).getImage().getScaledInstance(
    200, 200, Image.SCALE_DEFAULT));

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel messageLabel;
  private JLabel warningImgLabel;

  // initializing panels
  private JPanel buttonPanel;
  
  // initializing buttons
  private JButton yesButton;
  private JButton cancelButton;

  // initializing new PlayGame frame to pass the torch to from the constructor in order to execute methods belonging to the frame already open
  private PlayGame playGame;

  public CloseWarning(PlayGame gameFrame)
  {
    // initializing the frame
    super("Whack-a-Time");
    this.setBounds(100, 50, 650, 315);
    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.getContentPane().setBackground(Color.RED);
    this.setLayout(new BorderLayout());
    this.setAlwaysOnTop(true);

    // passing the torch from the PlayGame frame passed into the constructor to the encapsulated variable above to be used in the rest of the program
    this.playGame = gameFrame;

    // constructing labels
    // constructing the label to ask users if they really want to exit their game mid-way
    this.messageLabel = new JLabel("Are you sure you want to quit your game?", SwingConstants.CENTER);
    messageLabel.setForeground(Color.WHITE); // setting the text color of the label to white
    messageLabel.setFont(BIG_FONT); // setting the font of the label to the larger font declared above
    
    // constructing the label to display warning triangle image
    this.warningImgLabel = new JLabel(WARNING_IMAGE);

    // constructing buttons
    // constructing the button to close the PlayGame window when clicked
    this.yesButton = new JButton("Confirm");
    yesButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    // constructing the button to cancel going through with closing the PlayGame window when clicked
    this.cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing panels
    // constructing the panel to hold both buttons at the bottom of the frame
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.RED); // setting the background of the panel to red
    
    // adding buttons to the panel
    buttonPanel.add(yesButton);
    buttonPanel.add(cancelButton);

    // adding components to the frame
    this.add(messageLabel, BorderLayout.NORTH);
    this.add(warningImgLabel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // preventing the user from resizing the window, as already at a good size
    this.setResizable(true);
    
    // making the frame visible when opened
    this.setVisible(true);
  }

  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the identity/name of button that was clicked when the button press was registered
    Object command = e.getSource();

    // if the button to confirm is clicked ...
    if (command == yesButton)
    {
      // ... close the passed PlayGame window
      playGame.dispose();
      // and close the other MolesCounter window connected to the open PlayGame window by running the method closeMolesCounterWindow() in the playGame instance
      playGame.closeMolesCounterWindow();
      
      // stopping the music running when the game is playing
      playGame.stopMusic();
      playGame.closeMusic();
      
      // closing this warning frame
      this.dispose();
      
      // creating new Home page window once everything is closed
      new Home();
    }
    // if the button to cancel closing everything is clicked ...
    else if (command == cancelButton)
    {
      // ... just close this warning window, keeping everything else open
      this.dispose();
    }
  }

  // main method to test the CloseWarning GUI
  public static void main(String[] args)
  {
    // new CloseWarning frame, passing in a new PlayGame frame as stated above in the constructor
    new CloseWarning(new PlayGame());
  }
}
