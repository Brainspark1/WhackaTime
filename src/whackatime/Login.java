/*
Nihaal Garud, April 1st 2026
Modelling the login page for the application
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// importing all necessary SQL components
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class Login extends JFrame implements ActionListener
{
  // declaring the fonts used in the rest of the program
  public final Font BIG_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 45);
  public final Font SIDE_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 20);
  
  // declaring the image path and actual image icon for the peeking mole image
  public final URL MOLE_PATH = getClass().getResource("moleLog.png");
  public final ImageIcon MOLE_IMAGE = new ImageIcon(new ImageIcon(
    MOLE_PATH).getImage().getScaledInstance(
      150, 150, Image.SCALE_DEFAULT));

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel titleLabel;
  private JLabel usernameLabel;
  private JLabel moleImgLabel;

  // initializing text fields
  private JTextField usernameField;

  // initializing buttons 
  private JButton loginButton;
  private JButton cancelButton;

  // initializing panels
  private JPanel titlePanel;
  private JPanel loginPanel;
  private JPanel buttonPanel;

  // initializing new Home frame to pass the torch to from the constructor in order to execute methods belonging to the frame already open
  private Home homePage;

  public Login(Home homeFrame)
  {
    // initializing the frame
    super("Login");
    this.setBounds(100, 50, 360, 300);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Color.GREEN);
    this.setLayout(new BorderLayout());

    // passing the torch from the Home frame passed into the constructor to the encapsulated variable above to be used in the rest of the program
    this.homePage = homeFrame;

    // constructing labels
    // constructing label for title
    this.titleLabel = new JLabel("Login", SwingConstants.CENTER);
    titleLabel.setFont(BIG_FONT); // setting font to larger font declared above
    titleLabel.setForeground(Color.WHITE); // setting colour to white

    // constructing label pointing to username textfield
    this.usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(SIDE_FONT); // setting font to smaller font declared above
    usernameLabel.setForeground(Color.WHITE); // setting colour to white
    
    // constructing label to hold mole image
    this.moleImgLabel = new JLabel(MOLE_IMAGE);

    // constructing panels
    // constructing panel to hold title text
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Color.BLUE); // setting background colour to blue

    // constructing panel to hold textfield for user to enter their username
    this.loginPanel = new JPanel();
    loginPanel.setBackground(Color.BLUE); // setting background colour to blue

    // constructing panel to hold login button
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.RED); // setting background colour to red

    // constructing textfields
    // constructing textfield used to allow users to enter their username in order to login
    this.usernameField = new JTextField(15);

    // constructing buttons
    // constructing button used to allow users to login
    this.loginButton = new JButton("Login");
    loginButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing button used to allow users to cancel logging in if needed
    this.cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // adding components to panels
    titlePanel.add(titleLabel);
    titlePanel.add(moleImgLabel);

    loginPanel.add(usernameLabel);
    loginPanel.add(usernameField);

    buttonPanel.add(loginButton);
    buttonPanel.add(cancelButton);

    // adding panels to frame
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(loginPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);

    // preventing users from changing size of window, as already at good size
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
    
    // String variable used to hold the username entered by the user into the textfield
    String readUserName;

    // db info
    String dbName = "WhackATimeHistory";
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myConn = objDb.getDbConn();

    // object/instance of logic/computation class
    WhackTime objTime = new WhackTime();

    // if the user chose to login ...
    if (command == loginButton)
    {
      // read username of user entered in login textfield
      readUserName = usernameField.getText();

      // gets all users with an inputted username - makes sure that the username actually exists in the db
      String dbQuery = "SELECT * FROM UserBase WHERE userName = ?";

      // reset all users lastId to 0
      String resetQuery = "UPDATE UserBase SET lastId = 0";
      // set currently logged in user lastId to 1
      String setLastQuery = "UPDATE UserBase SET lastId = 1 WHERE userName = ?";

      // the following try-catch block tries to execute the above statements to login, and then set the auto login status of a logged in user, catching any errors that may have been thrown in the processes
      try
      {
        // preparing the select statement for execution
        PreparedStatement ps = myConn.prepareStatement(dbQuery);
        // setting the username in the query to the username read from user input in the textfield
        ps.setString(1, readUserName);

        // using a resultset when executing the select query to determine if the username entered in when logging in actually exists in the database
        ResultSet rs = ps.executeQuery();

        // if the pointer in the result set can move to a new row in the table/the username entered in the query does exist ...
        if (rs.next())
        {
          // letting the programmers know that the login has been successful
          System.out.println("Login successful!");

          // login current user into UserManager
          UserManager.loginUser(readUserName);

          // reset all users query - do first to prevent any potential duplicate lastId = 1 in db, for no confusion on who was the last user logged in
          PreparedStatement resetPs = myConn.prepareStatement(resetQuery);
          // executing this update query
          resetPs.executeUpdate();

          // set this currently logged in user as last logged in query - now that every lastId is 0, this user's lastId will be set to 1 to make them the official last user right now when the app reopens and auto logs them in again
          PreparedStatement lastPs = myConn.prepareStatement(setLastQuery);
          // adding username of currently logged in user to setLastQuery to update the table for that user's row
          lastPs.setString(1, readUserName);
          // executing this update query
          lastPs.executeUpdate();

          homePage.updateForLogin(); // unlock game buttons
          this.dispose(); // close login window
        }
        // if the pointer in the result set cannot move to a new row in the table/the username entered in the query doesn't exist ...
        else
        {
          // ... show warning frame telling them to sign up first instead
          new Warning("<html><center>User is not found! <br>Try creating a new account by clicking Sign Up.</center></html>");
          System.out.println("User not found.");
        }
      }
      // if any SQL errors are thrown in the above processes ...
      catch (SQLException se)
      {
        // ... let the programmers know that an error was thrown in the login
        System.out.println("Error in logging in user.");
      }
    }
    // if the user wants to cancel their login ...
    else if (command == cancelButton)
    {
      // ... close this login frame/window
      this.dispose();
    }
  }
  
  // main method to test Login GUI
  public static void main(String[] args)
  {
    // new Login frame/window with a new Home frame/window passed inside as declared above in the constructor
    new Login(new Home());
  }
}
