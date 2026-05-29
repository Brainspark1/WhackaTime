/*
Nihaal Garud, April 1st 2026
Modelling a sign up page for users to create a new account in the app
 */

package whackatime;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SignUp extends JFrame implements ActionListener
{
  // declaring fonts that will be used in the rest of the program file
  public final Font BIG_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 45);
  public final Font SIDE_FONT = new Font("Chalkboard", Font.BOLD | Font.ITALIC, 20);
  
  // declaring the image path and actual image icon for the peeking mole image
  public final URL MOLE_PATH = getClass().getResource("moleSign.png");
  public final ImageIcon MOLE_IMAGE = new ImageIcon(new ImageIcon(
    MOLE_PATH).getImage().getScaledInstance(
      175, 160, Image.SCALE_DEFAULT));

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel titleLabel;
  private JLabel usernameLabel;
  private JLabel moleImgLabel;

  // initializing textfields
  private JTextField usernameField;

  // initializing buttons
  private JButton signUpButton;
  private JButton cancelButton;

  // initializing panels
  private JPanel titlePanel;
  private JPanel signUpPanel;
  private JPanel buttonPanel;

  // initializing new Home frame to pass the torch to from the constructor in order to execute methods belonging to the frame already open
  private Home homePage;

  public SignUp(Home homeFrame)
  {
    // initializing the frame
    super("Sign Up");
    this.setBounds(100, 50, 390, 300);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.getContentPane().setBackground(Color.GREEN);
    this.setLayout(new BorderLayout());

    // passing the torch from the Home frame passed into the constructor to the encapsulated variable above to be used in the rest of the program
    this.homePage = homeFrame;

    // constructing labels
    // constructing label for title
    this.titleLabel = new JLabel("Sign Up", SwingConstants.CENTER);
    titleLabel.setFont(BIG_FONT); // setting font to larger font declared above
    titleLabel.setForeground(Color.WHITE); // setting colour to white
    
    // constructing label pointing to username textfield
    this.usernameLabel = new JLabel("Username:");
    usernameLabel.setFont(SIDE_FONT); // setting font to smaller font declared above
    usernameLabel.setForeground(Color.WHITE); // setting text colour to white
    
    // constructing label to hold mole image
    this.moleImgLabel = new JLabel(MOLE_IMAGE);

    // constructing panels
    // constructing panel to hold title text
    this.titlePanel = new JPanel();
    titlePanel.setBackground(Color.BLUE);
    
    // constructing panel to hold textfield for user to enter their new username
    this.signUpPanel = new JPanel();
    signUpPanel.setBackground(Color.BLUE); // setting background colour to blue
    
    // constructing panel to hold sign upp button
    this.buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.RED); // setting background colour to red

    // constructing textfields
    // constructing textfield used to allow users to enter their new username in order to sign up
    this.usernameField = new JTextField(15);

    // constructing buttons
    // constructing button used to allow users to sign up
    this.signUpButton = new JButton("Sign Up");
    signUpButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing button used to allow users to cancel signing up if needed
    this.cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // adding components to panels
    titlePanel.add(titleLabel);
    titlePanel.add(moleImgLabel);
    
    signUpPanel.add(usernameLabel);
    signUpPanel.add(usernameField);

    buttonPanel.add(signUpButton);
    buttonPanel.add(cancelButton);

    // adding panels to different parts of the frame
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(signUpPanel, BorderLayout.CENTER);
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

    // query to check if the entered username already exists
    String dbQuery = "SELECT * FROM UserBase WHERE userName = ?";
    // query to insert a new username into the UserBase data table
    String createQuery = "INSERT INTO UserBase (userName) VALUES (?)";

    // reset all users lastId to 0
    String resetQuery = "UPDATE UserBase SET lastId = 0";
    // set currently logged in user lastId to 1
    String setLastQuery = "UPDATE UserBase SET lastId = 1 WHERE userName = ?";

    // if the user clicked the button to sign up/creating an account ...
    if (command == signUpButton)
    {
      // read username of user entered in sign up textfield
      readUserName = usernameField.getText();

      // the following try-catch block tries to execute the above statements to sign up and login the user, and then set the auto login status of a logged in user, catching any errors that may have been thrown in the 
      try
      {
        // preparing the select statement for execution
        PreparedStatement ps = myConn.prepareStatement(dbQuery);
        // setting the username in the query to the username read from user input in the textfield
        ps.setString(1, readUserName);

        // executes query and stores it into a resultset to determine if after the execution, the username already exists
        ResultSet rs = ps.executeQuery();

        // if the pointer can move to a new row/the username already exists...
        if (rs.next())
        {
          // let the user know that the username already exists
          new Warning("Username already exists!");
          this.toFront(); // bring sign up window to front

          // letting the use know that this user already exists in the database
          System.out.println("User already exists!");
        }
        // if the pointer cannot move to a new row/the username doesn't already exists ...
        else
        {
          // the following try-catch block tries to create a new user account in the user base data table, catching any errors that may have been thrown in the process
          try
          {
            // preparing the update statement for execution
            PreparedStatement createPs = myConn.prepareStatement(createQuery);
            // setting the username in the query to the username read from user input in the textfield
            createPs.setString(1, readUserName);

            // executing the update statement
            createPs.executeUpdate();

            // letting the programmers know that a new user has been successfully created/signed up
            System.out.println("New user created successfully!");

            // automatically logging in the new user account after they created their account into the UserManager
            UserManager.loginUser(readUserName);
            // creating a new past user history table for the newly created account
            objDb.createUserTable(readUserName);

            // reset all users query 
            PreparedStatement resetPs = myConn.prepareStatement(resetQuery);
            resetPs.executeUpdate();

            // set this currently loggedin user as last logged in query
            PreparedStatement lastPs = myConn.prepareStatement(setLastQuery);
            lastPs.setString(1, readUserName);
            lastPs.executeUpdate();

            homePage.updateForLogin(); // unlocking game buttons
            this.dispose(); // closing the sign up window
          }
          // catching any SQL errors that may have been thrown in the process
          catch (SQLException se)
          {
            // letting the programmers know that an error was thrown in creating a new account/user
            System.out.println("Error in creating user.");
          }
        }
      }
      // catching any SQL errors that may have been thrown in the process
      catch (SQLException se)
      {
        // letting the programmers know that an error was thrown
        System.out.println("Error in SQL.");
      }
    }
    // if the user wants to cancel their login ...
    else if (command == cancelButton)
    {
      // ... close this sign up frame/window
      this.dispose();
    }
  }

  // main method to test SignUp GUI
  public static void main(String[] args)
  {
    // new SignUp frame/window with a new Home frame/window passed inside as declared above in the constructor
    new SignUp(new Home());
  }
}
