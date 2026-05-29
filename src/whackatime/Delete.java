/*
Nihaal Garud, April 29th 2026
Modelling a delete class to be called when users choose to delete rows/times from their past history table
 */

package whackatime;

// importing all necessary GUI components
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// importing all necessary SQL components
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete extends JFrame implements ActionListener
{
  // initializing elements/components to be declared and constructed later in the constructor
  // initializing labels
  private JLabel idLabel;

  // initializing textfields
  private JTextField idField;

  // initializing buttons
  private JButton deleteButton;
  private JButton doneButton;

  // initializing panels
  private JPanel deletePanel;
  private JPanel dataPanel;
  private JPanel donePanel;

  // initializes a new data table window to store the MassCalculationsDisplay class passed via pFrame into the Delete class constructor to put the MassCalculationsDisplay window in scope to be easily called later
  private PastHistoryDisplay gFrame;

  public Delete(PastHistoryDisplay pFrame)
  {
    // initializing the frame
    super("Delete");
    gFrame = pFrame; // pass the torch from the parameter PastHistoryDisplay of the constructor into the initialized gFrame above to be accessed locally in the entire Delete class
    this.setBounds(925, 50, 400, 150);
    this.getContentPane().setBackground(Color.CYAN);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // constructing labels
    // label pointing to textfield where user is prompted to enter the ID of the row they want to delete
    this.idLabel = new JLabel("Game Number: ");
    // setting the font of the id label
    this.idLabel.setFont(new Font("Arial", Font.BOLD, 20));

    // constructing textfields
    // textfield where user is told to enter the ID of the row they want to delete
    this.idField = new JTextField(15);

    // constructing buttons
    // button used by user to delete the row with the id number that they entered in the id textfield
    this.deleteButton = new JButton("Delete");
    deleteButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    // button used by user to close the delete window when they are done deleting the row they wished to delete
    this.doneButton = new JButton("Done");
    doneButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing panels
    // panel placed at the top of the frame to store textfields for the user to interact with and enter the row number which they wish to delete
    this.dataPanel = new JPanel(new FlowLayout());
    dataPanel.setBackground(Color.CYAN); // setting the background of this panel to the color cyan
    // panel placed in the middle of the frame to hold the delete button for the user to press when they choose to delete the row with the corresponding entered id number 
    this.deletePanel = new JPanel();
    deletePanel.setBackground(Color.CYAN); // setting the background of this panel to the color cyan
    // panel placed at the bottom of the frame to hold the done button for the user to press to close the frame
    this.donePanel = new JPanel();
    donePanel.setBackground(Color.RED); // setting the background of this panel to the color red

    // adding components to panels
    // adding components for the user to be able to enter the id of the row number which they wish to delete
    dataPanel.add(idLabel);
    dataPanel.add(idField);

    // adding components for the user to be able to successfully delete the row from the data table with the corresponding id number by clicking the delete button
    deletePanel.add(deleteButton);

    // adding components for the user to be able to close the delete window by clicking the done button
    donePanel.add(doneButton);

    // adding components to the frame
    this.add(dataPanel, BorderLayout.NORTH); // adding the panel holding the components needed for the user to enter their data to the top of the frame
    this.add(deletePanel, BorderLayout.CENTER); // adding the panel holding the components needed for the user to successfully delete the row from the data table with the corresponding id number to the middle of the frame
    this.add(donePanel, BorderLayout.SOUTH); // adding the panel holding the components needed for the user to close the frame when they are done to the bottom of the frame
    this.setVisible(true); // setting the frame visible for the user to be able to see the frame pop-up
  }

  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the text on the name of the button that was clicked when the button press was registered
    String command = e.getActionCommand();

    // variable to store the number of rows in the data table affected by a SQL query, checking if any rows in the database were affected by the query to determine if the id entered by the user above exists in the data table
    int rows = 0;

    // db info
    String dbName = "WhackATimeHistory";
    String tableName = "TimingHistory_" + UserManager.getCurrentUser();
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    // variable to store the entered id number that is read from the id textfield above to allow the program to search for and delete the row with that corresponding id number from the table
    int readId;

    // connect to db
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myConn = objDb.getDbConn();

    // if the button with the text name of "Delete" is pressed ...
    if (command.equals("Delete"))
    {
      /*
      the following try-catch block is run when the user decides to delete a row from the 
      data table with the corresponding game number inputted in the textfield in the GUI frame
       */
      try
      {
        // read data from textfields
        readId = Integer.parseInt(idField.getText());

        // delete query to delete row from TimingHistory_(currently logged in user) table with given game number
        String dbQuery = "DELETE FROM " + tableName + " WHERE gameNumber=?";

        // insert into database
        // prepare statement
        PreparedStatement ps = myConn.prepareStatement(dbQuery);
        // enter data into query
        ps.setInt(1, readId);

        // execute the query, storing the number of rows affected by the update to determine if any rows were affected by the query/the inputted game number exists in the data table
        rows = ps.executeUpdate();

        // if the number of rows affected by the SQL query was greater than 0/the game number entered by users must exist in the data table ...
        if (rows > 0)
        {
          // printing a message for the programmers to let them know that the processes have been successfully executed
          System.out.println("Data deleted successfully.");

          // clearing the textfield
          idField.setText("");

          // refresh the display frame by closing and reopening the frame showing the current data table to the users
          gFrame.dispose(); // disposing/closing the window holding and showing the current data table to the user
          gFrame = new PastHistoryDisplay(dbName, tableName, columnNames); // creating another window holding and showing the current data table for the user
          this.toFront(); // bring this current Delete window/frame to the front of the newly created data table window in case the user wishes to delete another row again
        }
        else // if the number of rows affected by the SQL query was not greater than 0 and thus equal to 0/the id number entered by users must not exist in the data table ...
        {
          // printing a message for the programmers to let them know that no data was deleted/the processes have been unsuccessfully executed
          System.out.println("No data deleted.");
          new Warning("Game number not found!");
        }
      }
      // catching exceptions thrown above involving ID entered by user that is not in the database
      catch (SQLException se)
      {
        System.out.println("Error deleting data.");
      }
      // catching exceptions thrown above involving format of input into idField not being an integer
      catch (NumberFormatException nfe)
      {
        // creating and showing a new Warning frame displaying the error that the value entered by the user into the id textfield wasn't of the proper type (an integer)
        new Warning("Enter a valid number");
      }
    }
    // if the user decides to click the done button ...
    else if (command.equals("Done"))
    {
      // closing the db connection and closing the created Delete window
      objDb.closeDbConn();
      this.dispose();
    }
  }
  
  // main method to test Delete GUI and connection to database to pass changed information into database
  public static void main(String[] args)
  {
    // db info
    String dbName = "WhackATimeHistory";
    String tableName = "TimingHistory_" + UserManager.getCurrentUser();
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    // creating a new Delete class to open up a new window for users to test the class by passing information about the database into a newly created PastHistoryDisplay window holding the user's logged in data table as well
    new Delete(new PastHistoryDisplay(dbName, tableName, columnNames));
  }
}
