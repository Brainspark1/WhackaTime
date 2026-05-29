/*
Nihaal Garud, April 3rd 2026
Modelling the help frame to help users use the application
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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Help extends JFrame implements ActionListener
{
  // declaring the fonts used in the rest of the program
  public static final Font MY_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 25);
  public static final Font BIG_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 40);
  
  // declaring the image path and actual image icon for the help hand image
  public final URL HELP_PATH = getClass().getResource("helpImage.png");
  public final ImageIcon HELP_IMAGE = new ImageIcon(new ImageIcon(
    HELP_PATH).getImage().getScaledInstance(
    350, 360, Image.SCALE_DEFAULT));
  
  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel titleText;
  private JLabel helpText;
  private JLabel helpImgLabel;
  
  // initializing buttons
  private JButton homeButton;
  
  // initializing panels
  private JPanel buttonPanel;
  private JPanel leftPanel;
  private JPanel rightPanel;
  
  // initializing help message to be displayed in a JLabel when constructed below
  private String helpMessage = "<html><center>"
    + "<br>Start a new game by pressing [New Game]<br>"
    + "Check your past times by pressing [View History]<br>"
    + "Find the leaderboard by pressing [View Leaderboard]<br><br>"
    + "Your goal is to press moles as quickly as they appear.<br>"
    + "After 30 moles have been pressed, you will get<br> "
    + "your average reaction time for the round.<br>"
    + "Questions, reach out to brainsparkteam@gmail.com.<br>"
    + "Good luck, try your best and explore around!"
    + "<br><br></center></html>";

  public Help()
  {
    // initializing the frame
    super("Help");
    this.setBounds(150, 150, 1040, 438);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Color.BLUE);
    
    // not allowing the user to resize the help window, as already at a good size
    this.setResizable(false);
    
    // constructing labels
    // constructing label to show the title of the help page
    this.titleText = new JLabel("<html>Whack-a-Time Help<br></html>", SwingConstants.CENTER); // using html for the <br> element to create space between the title and main text
    titleText.setForeground(Color.WHITE); // setting text color to white
    titleText.setFont(BIG_FONT); // setting font of the text to larger font
    
    // constructing label to show the message of the help page initialized above
    this.helpText = new JLabel(helpMessage, SwingConstants.CENTER);
    helpText.setForeground(Color.WHITE); // setting text color to white
    helpText.setFont(MY_FONT); // setting font of the text to the smaller font
    
    // constructing label to display the image of the helping hand
    this.helpImgLabel = new JLabel(HELP_IMAGE, SwingConstants.CENTER);
    
    // constructing buttons
    // constructing button which takes the user home when clicked
    this.homeButton = new JButton("Home");
    homeButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // constructing panels
    // constructing panel to hold the home button
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.RED); // setting background color of panel to red
    
    // constructing panel to hold text on the left side of the frame
    this.leftPanel = new JPanel(new BorderLayout());
    leftPanel.setBackground(Color.BLUE); // setting background colour to blue
    
    // constructing panel to hold the helping hand image on the right side of the frame
    this.rightPanel = new JPanel();
    rightPanel.setBackground(Color.BLUE); // setting background colour to blue
    
    // adding components to panels
    buttonPanel.add(homeButton);
    
    rightPanel.add(helpImgLabel);
    
    leftPanel.add(titleText, BorderLayout.NORTH);
    leftPanel.add(helpText, BorderLayout.CENTER);
    
    // adding panels to the frame
    this.add(buttonPanel, BorderLayout.NORTH);
    this.add(leftPanel, BorderLayout.WEST);
    this.add(rightPanel, BorderLayout.EAST);
    
    // making the frame visible when opened
    this.setVisible(true);
  }
 
  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the identity/name of button that was clicked when the button press was registered
    Object command = e.getSource();
    
    // if the user pressed the button to navigate home ...
    if (command == homeButton)
    {
      // ... close this pop-up help frame to show the home page again
      this.dispose();
    }
  }
  
  // main method to test the Help GUI
  public static void main(String[] args)
  {
    // new Help window/frame
    new Help();
  }
}