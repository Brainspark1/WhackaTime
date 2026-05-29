/*
Nihaal Garud, April 5th 2026
Modelling the counter frame displaying the remaining number of moles to show to users as their game runs
 */

package whackatime;

// importing all necessary GUI components
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MolesCounter extends JFrame
{
  // declaring the font used in the rest of the program
  public final Font BIG_FONT = new Font("Chalkboard", Font.BOLD, 25);
  public final Font BIGGER_FONT = new Font("Chalkboard", Font.BOLD, 60);

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel molesLabel;
  private JLabel counterLabel;

  // initializing panels
  private JPanel textPanel;

  public MolesCounter()
  {
    // initializing the frame
    super("Moles Left");
    this.setBounds(980, 50, 215, 175);
    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // not allowing users to close the counter window on their own while the game is still running
    this.getContentPane().setBackground(Color.BLACK);
    this.setLayout(new BorderLayout());

    // constructing panels
    // constructing the panel used to hold all the labels of the frame
    this.textPanel = new JPanel(new GridLayout(3, 1)); // using a grid layout on the panel to lay out all the components properly
    textPanel.setBackground(Color.RED); // setting the background colour to red

    // constructing labels
    // constructing the label used to indiccate to users where to look to see how many moles they have left
    this.molesLabel = new JLabel("Moles Left:", SwingConstants.CENTER);
    molesLabel.setFont(BIG_FONT); // setting the font of the label to the larger font declared above
    molesLabel.setForeground(Color.WHITE); // setting the colour of the text to white
    
    // constructing the label used to actually display to users the number of moles they have left
    counterLabel = new JLabel("30", SwingConstants.CENTER);
    counterLabel.setFont(BIGGER_FONT); // setting the font of the label to the very large font declared above for readability
    counterLabel.setForeground(Color.WHITE); // setting the colour of the text to white

    // adding labels to the panel
    textPanel.add(molesLabel);
    textPanel.add(counterLabel);

    // adding the panel to the center of the frame
    this.add(textPanel, BorderLayout.CENTER);

    // prevents the game window from disappearing behind other open windows in case user clicks off the window and onto the background - from testing
    this.setAlwaysOnTop(true);
    // prevents users from changing the size of the window, as already at a good set window size
    this.setResizable(false);
    
    // making the frame visible when opened
    this.setVisible(true);
  }

  // method accessed by other classes (PlayGame window in particular) to update the number of moles displayed in the label on this frame
  public void setMolesRemaining(int molesRemaining)
  {
    // setting the text of the label showing the number of moles left to the the String value of the calculated number of moles remaining
    counterLabel.setText(String.valueOf(molesRemaining));
  }
  
  // main method to test MolesCounter GUI
  public static void main(String[] args)
  {
    // new MolesCounter window/frame
    new MolesCounter();
  }
}
