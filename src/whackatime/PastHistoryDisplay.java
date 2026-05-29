/*
Nihaal Garud, April 8th 2026
Modelling the display page to show the past game history and average reaction times for a user with GUI
 */

package whackatime;

// importing all necessary GUI components 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

// importing ArrayList to store and hold the data returned from methods in the JavaDatabase class to display within the GUI
import java.util.ArrayList;

public class PastHistoryDisplay extends JFrame implements ActionListener
{
  // declaring the colors used in the rest of the program for different components on the frame
  public final Color BACK_COLOR = new Color(138, 113, 88);
  public final Color TOP_GREEN = new Color(71, 110, 74);
  
  // creating a 2D ArrayList to get and store data retrieved from the database/data table
  private ArrayList<ArrayList<String>> dataList;
  // creating a 2D Object array to hold data before converting it into a 2d array to be displayed in GUI
  // note - using type Object to hold the data as the data type of the retrieved values/data is unknown
  private Object[][] data;

  // initializing elements/components to be declared and constructed later in the constructor
  // initializing buttons
  private JButton homeButton;
  private JButton exitButton;
  private JButton deleteButton;

  // initializing panels
  private JPanel buttonPanel;
  private JPanel navPanel;

  // initializing table
  private JTable dbTable;
  // initializing scroll pane for the data table
  private JScrollPane scrollTable;
  // initializing table headers for the data table
  private JTableHeader header;
  // initializing table columns for the data table
  private TableColumn column;

  public PastHistoryDisplay(String dbName, String tableName, String[] tableHeaders)
  {
    // initializing the frame
    super("Current Report");
    this.setBounds(100, 50, 800, 500);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    // construct data
    JavaDatabase objDb = new JavaDatabase(dbName);
    dataList = objDb.getData(tableName, tableHeaders);
    data = objDb.to2dArray(dataList);
    objDb.closeDbConn();

    // construct table
    dbTable = new JTable(data, tableHeaders);
    dbTable.setGridColor(Color.BLACK);
    dbTable.setBackground(BACK_COLOR);
    dbTable.setFont(new Font("Arial", Font.BOLD, 20));
    dbTable.setForeground(Color.WHITE);
    dbTable.setRowHeight(30);

    // format header
    header = dbTable.getTableHeader();
    header.setBackground(TOP_GREEN);
    header.setForeground(Color.WHITE);
    header.setFont(new Font("Arial", Font.BOLD, 24));

    // format columns
    column = dbTable.getColumnModel().getColumn(0);
    column.setPreferredWidth(30);
    column = dbTable.getColumnModel().getColumn(1);
    column.setPreferredWidth(150);

    // put table into scrollPanel
    scrollTable = new JScrollPane();
    scrollTable.getViewport().add(dbTable);
    dbTable.setFillsViewportHeight(true);

    // button used by user to navigate back to the start window of the program from the data table display window
    this.homeButton = new JButton("Home");
    homeButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // button used by user to exit the program from the data table display window
    this.exitButton = new JButton("Exit");
    exitButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below
    
    // button used by user to delete past times
    this.deleteButton = new JButton("Delete Time");
    deleteButton.addActionListener(this); // adding actionListener to the button for the program to be able to register when the button is clicked in the actionPerformed() method below

    // constructing panels
    // panel placed at the bottom of the frame to store buttons for navigating home and exiting the program
    this.navPanel = new JPanel(new FlowLayout());
    navPanel.setBackground(Color.RED); // setting the background of this panel to the color red
    
    // adding components for the user to be able to delete past times, navigate back home and exit the program
    navPanel.add(deleteButton);
    navPanel.add(homeButton);
    navPanel.add(exitButton);

    // adding components to the frame
    this.add(scrollTable, BorderLayout.CENTER); // adding the scroll pane containing the data table to the center of the frame
    this.add(navPanel, BorderLayout.SOUTH); // adding the panel containing the buttons to navigate to other parts of the program to the bottom of the frame

    // making the frame visible when opened
    this.setVisible(true);
  }
  
  // the following method is used to register when different buttons are clicked in the GUI frame to perform actions with them accordingly
  @Override
  public void actionPerformed(ActionEvent e)
  {
    // receiving the text on the name of the button that was clicked when the button press was registered
    String command = e.getActionCommand();

    // if the user chose to navigate back to the home frame ...
    if (command.equals("Home"))
    {
      // deleting this frame and creating a new home page frame instead
      this.dispose();
      new Home();
    }
    // if the user chose to click the button to exit the program ...
    else if (command.equals("Exit"))
    {
      // ... exit the program
      System.exit(0);
    }
    // if the user chose to click the button to delete a time ...
    else if (command.equals("Delete Time"))
    {
      // ... create a new Delete window/frame for users to do so
      new Delete(this);
    }
  }
  
  // main method to test PastHistoryDisplay GUI
  public static void main(String[] args)
  {
    // db info
    String dbName = "WhackATimeHistory";
    String tableName = "TimingHistory_" + UserManager.getCurrentUser();
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    // new PastHistoryDisplay window/frame, passing this database info into the frame
    new PastHistoryDisplay(dbName, tableName, columnNames);
  }
}
