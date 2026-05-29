/*
Nihaal Garud, March 31st 2026
Modelling the home page for the application when the app opens
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

// using box layout to stack components/buttons on top of each other
import javax.swing.Box;
import javax.swing.BoxLayout;

public class Home extends JFrame implements ActionListener
{
  // declaring the colors used in the rest of the program for different components on the frame
  public final Color BACK_COLOR = new Color(138, 113, 88);
  public final Color TOP_GREEN = new Color(71, 110, 74);

  // declaring the font used in the rest of the program
  public final Font BIG_FONT = new Font("Chalkboard", Font.BOLD, 40);

  // declaring the image path and actual image icon for the home beware mole image
  public final URL BEWARE_PATH = getClass().getResource("bewareMole.png");
  public final ImageIcon BEWARE_IMAGE = new ImageIcon(new ImageIcon(
    BEWARE_PATH).getImage().getScaledInstance(
    290, 290, Image.SCALE_DEFAULT));

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel welcomeMessage;
  private JLabel highScoreMessage;
  private JLabel bewareImgLabel;

  // initializing buttons
  private JButton loginButton;
  private JButton signUpButton;
  private JButton logoutButton;
  private JButton helpButton;
  private JButton resultsButton;
  private JButton newGameButton;
  private JButton leaderboardButton;

  // initializing panels
  private JPanel loginPanel;
  private JPanel buttonPanel;

  // double variable holding the shortest time gathered to be displayed in a label within the constructor
  private double shortestTime;

  // initializing an instance of JavaDatabase to access its database methods within the program
  private JavaDatabase objDb;

  public Home()
  {
    // initializing the frame
    super("Whack-a-Time");
    this.setBounds(100, 50, 850, 750);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.getContentPane().setBackground(BACK_COLOR);
    this.setLayout(new BorderLayout());

    // constructing panels
    // constructing the panel holding the buttons needed for the user to sign in/login
    this.loginPanel = new JPanel();
    loginPanel.setBackground(TOP_GREEN); // setting the background color of the panel to the green declared above

    // constructing the panel holding the buttons for users to start a new game, view their past history or leaderboard
    this.buttonPanel = new JPanel();
    // setting the layout of the panel to a vertical box layout in order to stack the buttons and labels on top of each other
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
    buttonPanel.setBackground(BACK_COLOR); // setting the background color of the panel to the background brown declared above

    // constructing buttons
    // constructing the button for users to login into an existing account
    this.loginButton = new JButton("Login");
    loginButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing the button for users to create a new account
    this.signUpButton = new JButton("Sign Up");
    signUpButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing the button for users to logout from a logged in account
    this.logoutButton = new JButton("Logout");
    logoutButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing the button for users to navigate to the help menu
    this.helpButton = new JButton("Help");
    helpButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing the button for users to play a new game
    this.newGameButton = new JButton("New Game");
    newGameButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing the button for users to navigate to their own past history display
    this.resultsButton = new JButton("View History");
    resultsButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing the button for users to navigate to the running leaderboard
    this.leaderboardButton = new JButton("View Leaderboard");
    leaderboardButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing labels
    // constructing the label used to hold the image of beware of the mole declared above
    this.bewareImgLabel = new JLabel(BEWARE_IMAGE);

    // constructing the label to show the welcome text to users
    this.welcomeMessage = new JLabel("Welcome to Whack-a-Time!");
    welcomeMessage.setFont(BIG_FONT); // setting the font of the title text to the larger font declared above
    welcomeMessage.setForeground(Color.WHITE); // setting the text colour of the title text to white

    // constructing the label to show the best score that the logged in user has received
    this.highScoreMessage = new JLabel(""); // initially has no text when constructed, as will be added in later in the constructor
    highScoreMessage.setFont(BIG_FONT); // setting the font of the high score text to the larger font declared above
    highScoreMessage.setForeground(Color.WHITE); // setting the text colour of the high score text to white

    // constructing the instance of JavaDatabase by connecting to the new database of WhackATimeHistory
    this.objDb = new JavaDatabase("WhackATimeHistory");

    // auto login last user
    objDb.autoLogin("in");

    // if a user is logged in/has been logged in with the auto login feature ...
    if (UserManager.isUserLoggedIn())
    {
      // change the title on the home page to display the user's name
      welcomeMessage.setText("Welcome, " + UserManager.getCurrentUser() + "!");
      // get the shortest/best time the logged in user has received using methods from the JavaDatabase instance declared above
      this.shortestTime = objDb.getShortestTime(UserManager.getCurrentUser());
      // set the text of the previously empty high score label to the found best time
      highScoreMessage.setText("Best Reaction Time: " + shortestTime + " sec");
    }

    // setting center alignment for components in box layout along the x-axis when added to the buttonPanel
    welcomeMessage.setAlignmentX(CENTER_ALIGNMENT);
    newGameButton.setAlignmentX(CENTER_ALIGNMENT);
    resultsButton.setAlignmentX(CENTER_ALIGNMENT);
    leaderboardButton.setAlignmentX(CENTER_ALIGNMENT);
    highScoreMessage.setAlignmentX(CENTER_ALIGNMENT);
    bewareImgLabel.setAlignmentX(CENTER_ALIGNMENT);

    // adding components/buttons to the login panel initially 
    loginPanel.add(loginButton);
    loginPanel.add(signUpButton);

    // update home page UI based on whether or not a user a logged in
    updateForLogin();

    // adding both panels to the frame
    this.add(loginPanel, BorderLayout.NORTH);
    this.add(buttonPanel, BorderLayout.CENTER);

    // making the frame visible when opened
    this.setVisible(true);
  }

  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the identity/name of button that was clicked when the button press was registered
    Object command = e.getSource();

    // db info
    String dbName = "WhackATimeHistory";
    String tableName = "TimingHistory_" + UserManager.getCurrentUser(); // following the system where the table name of a particular user is TimingHistory followed with their unique username, separated by an underscore
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    // if the user pressed the button to login ...
    if (command == loginButton)
    {
      // ... open a new Login window/frame
      new Login(this);
    }
    // if the user pressed the button to sign up ...
    else if (command == signUpButton)
    {
      // ... open a new SignUp window/frame
      new SignUp(this);
    }
    // if the user pressed the button to play a new game ...
    else if (command == newGameButton)
    {
      // ... close the current home window
      this.dispose();
      // open a new PlayGame window/frame
      new PlayGame();
    }
    // if the user pressed the button to view their past results ...
    else if (command == resultsButton)
    {
      // ... close the current home window
      this.dispose();
      // open a new PastHistoryDisplay window/frame, passing in the database information declared above
      new PastHistoryDisplay(dbName, tableName, columnNames);
    }
    // if the user pressed the button to logout of their account ...
    else if (command == logoutButton)
    {
      // ... logout user from the UserManager
      UserManager.logoutUser();

      // re-update the home page UI for these changes
      updateForLogin();
      // auto logout the user from the auto login system
      objDb.autoLogin("out");
    }
    // if the user pressed the button to view the game's leaderboard ...
    else if (command == leaderboardButton)
    {
      // ... open a new Leaderboard window/frame, passing the instance of the database used within this program inside
      new Leaderboard();
    }
    // if the user pressed the button to view the game's help page ...
    else if (command == helpButton)
    {
      // ... open a new Help window/frame
      new Help();
    }
  }

  // method to update the home page UI based on who is logged in
  public void updateForLogin()
  {
    // remove everything from home page to reset UI for adding new components later in the method
    buttonPanel.removeAll();
    loginPanel.removeAll();

    // if a user is logged in ...
    if (UserManager.isUserLoggedIn())
    {
      // changing the welcome message text to add the username of the user currently logged in
      welcomeMessage.setText("Welcome, " + UserManager.getCurrentUser() + "!");

      // getting the shortest time of the currently logged in user
      shortestTime = objDb.getShortestTime(UserManager.getCurrentUser());

      // if no shortest time ...
      if (shortestTime == 0.0)
      {
        // user must never have played yet, so prompt user to play their first game
        highScoreMessage.setText("Play your first game!");
        // prevent the user from using the results button as no past results to actually show yet
        resultsButton.setEnabled(false);
      }
      // if there is a shortest time ...
      else
      {
        // user must have played, so use the shortest time variable found above to give and set its value as text for the user 
        highScoreMessage.setText("Best Reaction Time: " + shortestTime + " sec");
        // allow the user to use the results button as there is a past result to actually show
        resultsButton.setEnabled(true);
      }

      // re-adding components back to the button panel - using BoxLayout to stack components on top of each other
      buttonPanel.add(Box.createVerticalGlue()); // creating a filler component to manage extra space on the top of the panel while stacking the components in the panel on top of each other
      buttonPanel.add(welcomeMessage); // adding the welcome message to the top of the stack
      buttonPanel.add(Box.createVerticalStrut(40)); // creating 40 px of vertical space between components
      buttonPanel.add(bewareImgLabel); // adding the mole image to the middle of the stack
      buttonPanel.add(Box.createVerticalStrut(10));  // creating 10 px of vertical space between components
      buttonPanel.add(newGameButton); // adding the new game button to the middle of the stack
      buttonPanel.add(Box.createVerticalStrut(10));  // creating 10 px of vertical space between components
      buttonPanel.add(resultsButton); // adding the past results button to the middle of the stack

      // if there is a shortest time ...
      if (shortestTime != 0.0)
      {
        // user must have played, so show them leaderboard
        buttonPanel.add(Box.createVerticalStrut(10));  // creating 10 px of vertical space between components
        buttonPanel.add(leaderboardButton); // adding the leaderboard button to the middle of the stack
      }
      
      buttonPanel.add(Box.createVerticalStrut(40));  // creating 40 px of vertical space between components
      buttonPanel.add(highScoreMessage); // adding the label showing the logged in user's high score to the bottom of the stack
      buttonPanel.add(Box.createVerticalGlue());  // creating a filler component to manage extra space on the bottom of the panel while stacking the components in the panel on top of each other

      // adding the logout button and help button to the login panel, as no need for users to see the login and sign up buttons anymore now that they are logged in
      loginPanel.add(logoutButton);
      loginPanel.add(helpButton);
    }
    // if there is no user logged in ...
    else
    {
      // keeping the welcome message generic
      welcomeMessage.setText("Welcome to Whack-a-Time!");

      // prompting the user to login or sign up in order to see their high score
      highScoreMessage.setText("Login or sign up to play");

      // re-adding components back to the button panel - using BoxLayout to stack components on top of each other
      buttonPanel.add(Box.createVerticalGlue()); // creating a filler component to manage extra space on the top of the panel while stacking the components in the panel on top of each other
      buttonPanel.add(welcomeMessage); // adding the welcome message to the top of the stack
      buttonPanel.add(Box.createVerticalStrut(35)); // creating 35 px of vertical space between components
      buttonPanel.add(bewareImgLabel);  // adding the mole image to the middle of the stack
      buttonPanel.add(Box.createVerticalStrut(20)); // creating 20 px of vertical space between components
      buttonPanel.add(highScoreMessage); // adding the label showing the logged in user's high score to the bottom of the stack
      buttonPanel.add(Box.createVerticalGlue()); // creating a filler component to manage extra space on the bottom of the panel while stacking the components in the panel on top of each other

      // note: don't add or show any buttons for users to actually play until they are logged in
      // adding the login and sign up buttons to the login panel for users to use in order to see their scores
      loginPanel.add(loginButton);
      loginPanel.add(signUpButton);
    }

    // validating and repainting both affected panels in this method in order to show their updates on the frame to the user
    loginPanel.validate();
    loginPanel.repaint();
    buttonPanel.validate();
    buttonPanel.repaint();
  }

  // main method to test the Home GUI
  public static void main(String[] args)
  {
    // new Home window/frame
    new Home();
  }
}
