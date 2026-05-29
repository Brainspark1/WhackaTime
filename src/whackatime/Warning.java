/*
Nihaal Garud, April 6th 2026
Modelling a warning frame to display potential warnings in different aspects of the application to users
 */

package whackatime;

// importing all necessary GUI components
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFrame;

public class Warning extends JFrame
{
  // delcaring the font used in the rest of the program
  public static final Font MY_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 40);

  // declaring the path and image of the warning triangle image loaded into this project to be displayed along with the warning frame below
  public final URL WARNING_PATH = getClass().getResource("warningTriangle.png");
  public final ImageIcon WARNING_IMAGE = new ImageIcon(new ImageIcon(
    WARNING_PATH).getImage().getScaledInstance(
    280, 280, Image.SCALE_DEFAULT));

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel warningMessageLabel;
  private JLabel warningImage;

  public Warning(String message)
  {
    // initializing the frame
    super("Warning");
    this.setBounds(975, 625, 800, 500);
    // making warning frames pop-up by allowing users to close them without exiting the program
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Color.YELLOW);
    this.setResizable(false);

    // constructing labels
    // label holding the message that was passed into the Warning class constructor to be displayed to users inside of a new Warning frame 
    this.warningMessageLabel = new JLabel(message, SwingConstants.CENTER); // aligning the message to the center
    // setting the font of this message label
    warningMessageLabel.setFont(MY_FONT);

    // label holding the image of the warning triangle whose path was declared and defined above to show to users in a new Warning frame
    this.warningImage = new JLabel(WARNING_IMAGE);

    // adding components to the frame
    this.add(warningMessageLabel, BorderLayout.NORTH); // adding the label holding the warning message to be displayed to users to the top of the frame
    this.add(warningImage, BorderLayout.CENTER); // adding the label holding the image of the warning triangle to be displayed to users to the bottom of the frame
    this.setVisible(true); // setting the frame visible for the user to be able to see the frame pop-up
  }
  
  // main method to test Warning GUI
  public static void main(String[] args)
  {
    // sample warning message
    String message = "Enter a valid number.";
    
    // new Warning frame/window display sample warning message declared above
    new Warning(message);
  }
}
