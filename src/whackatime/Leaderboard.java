/*
Nihaal Garud, April 17th 2026
Modelling the user leaderboard page for the application
 */

package whackatime;

// importing all necessary GUI components
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

// using box layout to stack components/buttons on top of each other
import javax.swing.Box;
import javax.swing.BoxLayout;

public class Leaderboard extends JFrame
{
  // declaring the fonts used in the rest of the program
  public final Font TITLE_FONT = new Font("Chalkboard", Font.BOLD, 38);
  public final Font TEXT_FONT = new Font("Chalkboard", Font.BOLD, 22);

  // declaring the image path and actual image icon for the leaderboard image
  public final URL LEADERBOARD_PATH = getClass().getResource("leaderboard.png");
  public final ImageIcon LEADERBOARD_IMAGE = new ImageIcon(new ImageIcon(
    LEADERBOARD_PATH).getImage().getScaledInstance(
    360, 180, Image.SCALE_DEFAULT));

  // declaring the image path and actual image icon for the three white dots to extend the leaderboard
  public final URL CIRCLE_PATH = getClass().getResource("whiteCircles.png");
  public final ImageIcon CIRCLE_IMAGE = new ImageIcon(new ImageIcon(
    CIRCLE_PATH).getImage().getScaledInstance(
    65, 65, Image.SCALE_DEFAULT));

  // initializing the ArrayList to store/hold leaderboard data fetched from database
  private ArrayList<String[]> leaderboardList;

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing panels
  private JPanel mainPanel;

  // initializing labels to display title and top 3 scores
  private JLabel titleLabel;
  private JLabel firstLabel;
  private JLabel secondLabel;
  private JLabel thirdLabel;
  private JLabel leaderboardImgLabel;
  private JLabel circlesImgLabel;
  private JLabel userLabel;

  // variable to hold the current position of the current user to be displayed in the leaderboard
  private int currentPosition;
  // variable to hold the name of the currently logged in user to be displayed in the leaderboard
  private String currentUser = UserManager.getCurrentUser();
  
  private JavaDatabase objDb;

  public Leaderboard()
  {
    // initializing the frame
    super("Leaderboard");
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Color.PINK);
    this.setLayout(new BorderLayout());
    
    this.objDb = new JavaDatabase("WhackATimeHistory");

    // retrieving leaderboard data from the database
    this.leaderboardList = objDb.getLeaderboardData();

    // constructing panels
    // constructing the main panel with vertical layout to stack components
    this.mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // allowing components to be added to the panel and be stacked vertically
    mainPanel.setBackground(Color.PINK); // setting the background colour to pink

    // constructing labels
    // constructing title label
    this.titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
    titleLabel.setFont(TITLE_FONT); // setting the font to the larger title font declared above
    titleLabel.setForeground(Color.WHITE); // setting the text colour to white

    // constructing label to hold leaderboard image 
    this.leaderboardImgLabel = new JLabel(LEADERBOARD_IMAGE, SwingConstants.CENTER);

    // constructing the label used to show the first place user/user with the shortest best time
    this.firstLabel = new JLabel("", SwingConstants.CENTER);
    firstLabel.setFont(TEXT_FONT); // setting the font to the smaller paragraph font declared above
    firstLabel.setForeground(Color.WHITE); // setting the text colour to white

    // checking if data exists for 1st place, and if so displaying it
    if (leaderboardList.size() > 0)
    {
      // setting the username text of the first place label to the first column of the first row in the gathered leaderboard data, and the best time to the second column of the first row
      firstLabel.setText("1. " + leaderboardList.get(0)[0] + "  -  " + leaderboardList.get(0)[1] + " sec");
    }
    // if no data exists for 1st place ...
    else
    {
      // ... set the text of the first place label to blanks
      firstLabel.setText("1 ---");
    }

    // constructing the label used to show the second place user/user with the second shortest best time
    this.secondLabel = new JLabel("", SwingConstants.CENTER);
    secondLabel.setFont(TEXT_FONT); // setting the font to the smaller paragraph font declared above
    secondLabel.setForeground(Color.WHITE); // setting the text colour to white

    // checking if data exists for 2nd place, and if so displaying it
    if (leaderboardList.size() > 1)
    {
      // setting the username text of the second place label to the first column of the second row in the gathered leaderboard data, and the best time to the second column of the second row
      secondLabel.setText("2. " + leaderboardList.get(1)[0] + "  -  " + leaderboardList.get(1)[1] + " sec");
    }
    // if no data exists for 2nd place ...
    else
    {
      // ... set the text of the second place label to blanks
      secondLabel.setText("2 ---");
    }

    // constructing the label used to show the third place user/user with the third shortest best time
    this.thirdLabel = new JLabel("", SwingConstants.CENTER);
    thirdLabel.setFont(TEXT_FONT); // setting the font to the smaller paragraph font declared above
    thirdLabel.setForeground(Color.WHITE); // setting the text colour to white

    // checking if data exists for 3rd place, and if so displaying it
    if (leaderboardList.size() > 2)
    {
      // setting the username text of the third place label to the first column of the third row in the gathered leaderboard data, and the best time to the second column of the third row
      thirdLabel.setText("3. " + leaderboardList.get(2)[0] + "  -  " + leaderboardList.get(2)[1] + " sec");
    }
    // if no data exists for 3rd place ...
    else
    {
      // ... set the text of the third place label to blanks
      thirdLabel.setText("3 ---");
    }

    // constructing label hold three dots image
    this.circlesImgLabel = new JLabel(CIRCLE_IMAGE);

    // constructing label to hold currently logged in user if not part of the top three
    this.userLabel = new JLabel("");
    userLabel.setFont(TEXT_FONT); // setting the font to the text font declared above
    userLabel.setForeground(Color.WHITE); // setting the text colour to white

    // setting center alignment for components in box layout along the x-axis when added to the buttonPanel
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firstLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    secondLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    thirdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    leaderboardImgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    circlesImgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    // adding components to the panel - using BoxLayout to stack components on top of each other
    mainPanel.add(leaderboardImgLabel); // adding the leaderboard image at the top of the stack
    mainPanel.add(Box.createVerticalStrut(2)); // creating 15 px of vertical space between components
    mainPanel.add(titleLabel); // adding the title to the middle of the stack
    mainPanel.add(Box.createVerticalStrut(20)); // creating 20 px of vertical space between components
    mainPanel.add(firstLabel); // adding the first place results to the middle of the stack
    mainPanel.add(Box.createVerticalStrut(10)); // creating 10 px of vertical space between components
    mainPanel.add(secondLabel); // adding the second place results to the middle of the stack
    mainPanel.add(Box.createVerticalStrut(10)); // creating 10 px of vertical space between components
    mainPanel.add(thirdLabel); // adding the third place results to the bottom of the stack

    // calling method to update GUI based on whether or not the currently logged in user is in the top three
    notTopThree();

    mainPanel.add(Box.createVerticalGlue()); // creating a filler component to manage extra space on the bottom of the panel while stacking the components in the panel on top of each other
    
    // adding the panel to the center of the frame
    this.add(mainPanel, BorderLayout.CENTER);

    // preventing the users from changing the size of the frame, as already at a good size
    this.setResizable(false);

    // making the frame visible when opened
    this.setVisible(true);
  }
  
  // method to update the leaderboard window GUI if the logged in user isn't in the top three
  private void notTopThree()
  {
    // if there is a currently logged in user ...
    if (currentUser != null)
    {
      // if the logged in user isn't one of the top three on the leaderboard ...
      if (!currentUser.equals(leaderboardList.get(0)[0]) // aren't first place
        && !currentUser.equals(leaderboardList.get(1)[0]) // aren't second place
        && !currentUser.equals(leaderboardList.get(2)[0])) // aren't third place
      {
        // ... extend the leaderboard frame height to include the position of the logged in user
        this.setBounds(420, 150, 350, 515);
        
        // get the current position of the logged in user
        this.currentPosition = objDb.getPositionOfUser(UserManager.getCurrentUser());
        // set the text of the label holding the logged in user's position on the leaderboard
        userLabel.setText(currentPosition + ". " + currentUser + "  -  " + leaderboardList.get(currentPosition - 1)[1] + " sec");
        
        // adding extra components to the panel
        mainPanel.add(Box.createVerticalStrut(5)); // creating 5 px of vertical space between components
        mainPanel.add(circlesImgLabel); // adding the circles image label to the middle of the stack if needed
        mainPanel.add(Box.createVerticalStrut(5)); // creating 5 px of vertical space between components
        mainPanel.add(userLabel); // adding label showing logged in user in the leaderboard at the end of the stack
      }
      // if the logged in user is one of the top three on the leaderboard ...
      else
      {
        // ... no need to extend the leaderboard frame height to include the position of the logged in user
        this.setBounds(420, 150, 350, 406);
      }
    }
    // if there is no currently logged in user ...
    else
    {
      // ... keep the leaderboard window at its normal size
      this.setBounds(420, 150, 350, 406);
    }
  }

  // main method to test the Leaderboard GUI
  public static void main(String[] args)
  {
    // new Leaderboard window/frame with a new JavaDatabase object passed within the constructor as declared above
    new Leaderboard();
  }
}
