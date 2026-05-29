/*
Nihaal Garud, February 28th 2026
Connecting Java to database with various set, get and conversion methods
 */

package whackatime;

// importing all necessary SQL components
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

// importing ArrayList to store and hold the 2D array of data that will be returned by this program
import java.util.ArrayList;

public class JavaDatabase
{
  // initializing variables to be used later on the respective database methods
  private String dbName; // variable that holds the name of the newly created database
  private Connection dbConn; // variable that holds the connection to be created between the java program and the newly created database
  private ArrayList<ArrayList<String>> data; // variable that holds the 2D array of data entered by users in the program

  // constructor used to install/initialize new database without any set values
  public JavaDatabase()
  {
    this.dbName = ""; // initializing no database name
    this.dbConn = null; // initializing no database connection
    this.data = null; // initializing no data stored within the database
  }

  // constructor used to set the name, database connection and initialize data to null of a new database
  public JavaDatabase(String dbName)
  {
    setDbName(dbName); // setting the database name to the name passed into the constructor
    setDbConn(); // setting the database connection
    this.data = null; // setting the data of the database to empty/null to allow the program to add data whenever necessary to the database
  }

  // set method to set the database name of the recently created database
  public void setDbName(String dbName)
  {
    this.dbName = dbName;
  }

  // get method to return the database name of the recently created database
  public String getDbName()
  {
    return this.dbName;
  }

  // set method to set database connection between the recently created database and the JDBC driver/connector added into this project's library - the method contains no parameter
  public void setDbConn()
  {
    // creating mysql connection
    String connectionURL = "jdbc:mysql://localhost:3306/" + this.dbName;
    this.dbConn = null;

    // the following try-catch block aims to determine if the JDBC driver has been successfully added to the library of the project
    try
    {
      // checks if the JDBC driver is found in the project
      Class.forName("com.mysql.cj.jdbc.Driver");
      // sets the database connection to the found JDBC driver if it was found and no errors were thrown
      this.dbConn = DriverManager.getConnection(connectionURL,
        "root", "mysql1");

      // end of mysql connection
    }
    // if the driver was not found ...
    catch (ClassNotFoundException ex)
    {
      // prompts the programmer to check the project's library to make sure it is there
      System.out.println("Driver not found, check library.");
    }
    // if any errors are thrown in the SQL setting database connection stage ...
    catch (SQLException se)
    {
      // tells the programmer that the database was not successfully created, proving why the errors may have been thrown
      System.out.println("SQL Connection error, DB was not created!");
    }
  }

  // this get method returns the database connection of the recently created database
  public Connection getDbConn()
  {
    return dbConn;
  }

  // this set method sets the data of the recently created database to a 2D array of data passed into it
  public void setData(ArrayList<ArrayList<String>> data)
  {
    this.data = data;
  }

  /* 
  this get method returns the 2D array of data that was stored and passed into the data tables of the database
  by receiving and taking in the table name and column names of the table whose data needs to be fetched
   */
  public ArrayList<ArrayList<String>> getData(String tableName,
                                              String[] tableHeaders)
  {
    int columnCount = tableHeaders.length; // holds the number of columns in the table
    Statement s = null; // holds a statement to be created below by the connection of the recently created database in order to help execute the database query
    ResultSet rs = null; // stores the result of executing a database query to deetermine if there is a next row in the data table, and if so to create and move a pointer down to that next row

    // this query uses SQL to select all information from a given table name
    String dbQuery = "SELECT * FROM " + tableName;
    // creating a new empty ArrayList to hold various other ArrayLists with data passed into it 
    this.data = new ArrayList<>();

    /*
    the following try catch block tries to execute the SQL statement above
    and insert all data within the fetched data into a new ArrayList before adding
    the new ArrayList to the overarching data ArrayList to make it 2D, and throws
    any exceptions/errors found in this process
     */
    try
    {
      s = this.dbConn.createStatement();
      rs = s.executeQuery(dbQuery);

      // if next row, move rs down to next row
      while (rs.next())
      {
        // row object to hold one row data
        ArrayList<String> row = new ArrayList<>();
        // go through the row and read each cell
        for (int i = 0; i < columnCount; i++)
        {
          // read cell i 
          // example: String cell = rs.getString("atomicMass");
          // reads the cell in column make
          // tableHeader={"id", "atomicMass", "protons", "neutrons"}
          String cell = rs.getString(tableHeaders[i]);
          // add cell into row
          // example: row.add(18);
          row.add(cell);
        }

        // add row to data
        // example: this.data.add(18, 13, 5);
        this.data.add(row);
      }
    }
    catch (SQLException se)
    {
      // printing message for programmers to let them know that data was unsuccessfully returned
      System.out.println("SQL Error: Not able to get data.");
    }

    return data;
  }

  // method to install/create db
  public void createDb(String newDbName)
  {
    Connection newConn;
    setDbName(newDbName);

    // mysql connection
    String connectionURL = "jdbc:mysql://localhost:3306/";
    String query = "CREATE DATABASE " + this.dbName;

    try
    {
      // to check for driver (mysql jar file)
      Class.forName("com.mysql.cj.jdbc.Driver");
      // to make connection using DriverManager of mysql
      newConn = DriverManager.getConnection(connectionURL,
        "root", "mysql1");

      // creating a blank statement
      Statement s = newConn.createStatement();
      // a) fill the statement s with the query, b) execute it
      s.executeUpdate(query);
      System.out.println("New database created.");
      // close db connection
      newConn.close();
      // end of mysql connection
    }
    // if the driver is not found ...
    catch (ClassNotFoundException ex)
    {
      // ... prompt programmer to check for the JDBC driver in the project's library
      System.out.println("Driver not found, check library.");
    }
    // catching any SQL errors that could be thrown in the database set up processes
    catch (SQLException se)
    {
      // prompting programmer that something went wrong with the creation of their database
      System.out.println("SQL Connection error, DB was not created!");
    }
  }

  // method to create a table within the recently created database for data to be added and stored
  public void createTable(String newTable, String dbName)
  {
    // prints the name of the new table for programmers to determine if code is working correctly or not
    System.out.println(newTable);
    // assigns parameter dbName to attribute dbName of this class
    setDbName(dbName);
    // creates connection to db and assigns it to attribute dbConn of this class
    setDbConn();
    // blank statement
    Statement s;

    // the following try-catch block tries to create a table from a connection created from a blank statement, and throws any errors/exceptions that may come about
    try
    {
      // creating and assigning a statement based on the recently created database connection
      s = this.dbConn.createStatement();
      // executing the statement by passing in and thus creating a new table with the name of the name passed in for newTable into the method
      s.execute(newTable);
      // printing for programmers a message that a new table has been successfully created
      System.out.println("New table created.");
      // closing the now unnecessary database connection
      this.dbConn.close();
    }
    // if any SQL errors were thrown as a result of these processes ...
    catch (SQLException err)
    {
      // ... print them for programmers to prompt them as to why these errors are occurring to help them fix it
      System.out.println("Error creating table " + newTable);
    }
  }

  // method to close the database connection between the recently-created database and the JDBC Driver found in the project's library
  public void closeDbConn()
  {
    try
    {
      // tries to close the connection between the Java program and the SQL database
      this.dbConn.close();
    }
    // catches any errors/exceptions that may have come as a result of these processes
    catch (Exception err)
    {
      // printing message for programmer to know that errors were thrown in the closing of the database connection
      System.out.println("DB closing error.");
    }
  }

  // converting a 2D ArrayList of data into a 2D array to easily display data within the database with GUI in the MassCalculationsDisplay class
  public Object[][] to2dArray(ArrayList<ArrayList<String>> dataList)
  {
    // if there is no data ...
    if (data.size() == 0)
    {
      // ... create and return an empty data array to show to users in the GUI layer
      Object[][] dataArray = new Object[0][0];
      return dataArray;
    }
    // if there is data in the datalist
    else
    {
      // holds the number of columns within the gathered data by counting the number of columns at row 0/the first row
      int columnCount = dataList.get(0).size();
      // 2d array [row][column]
      Object[][] dataArray = new Object[dataList.size()][columnCount];

      // for every row in the given data list ...
      for (int r = 0; r < dataList.size(); r++)
      {
        // ... create a new row object holding the data in that particular row of the data table
        ArrayList<String> row = dataList.get(r);
        // for every column out of the total number of columns in the data/data table ...
        for (int c = 0; c < columnCount; c++)
        {
          // ... set the data in cell of row r and column c to the value found in the newly created row at index/column c in this case
          dataArray[r][c] = row.get(c);
        }
      }

      // returning the converted 2D array
      return dataArray;
    }
  }

  // method to create a new table for a new user with a particular userName
  public void createUserTable(String userName)
  {
    // the template/generic table name for a new user is TimingHistory followed by their username, separated with an underscore
    String tableName = "TimingHistory_" + userName;

    // SQL query to create a new table with the table name created above for the logged in user
    String dbQuery
      = "CREATE TABLE IF NOT EXISTS " + tableName + " (" // creating a new table if it doesn't already exist with the table name made above
      + "gameNumber INT NOT NULL AUTO_INCREMENT, " // creation of a table column storing the gameNumber of different rows entered into the data table - placing parameters on the game numbers to not be null/has to exist and starts to auto-increment from 1 whenever new rows are added into the table
      + "reactionTime DECIMAL(4,3), " // creation of a table column storing the decimal value of the reaction times of rows entered into the data table
      + "PRIMARY KEY (gameNumber)" // setting the game number to the primary key/column value that uniquely identifies a particular row, meaning all game numbers values must be unique/distinct
      + ") AUTO_INCREMENT=1"; // starting the increment of the game number of newly created rows in the data table from 1 and increasing by 1 each time for each new row

    // the following try-catch block tries to execute the above query and create a new table for the user, catching any errors that may have been thrown in the process
    try
    {
      // creating a statement from the db connection and executing the create table query
      Statement s = this.dbConn.createStatement();
      s.executeUpdate(dbQuery);

      // letting the programmer know that a new table was successfully created
      System.out.println("New table created for username: " + userName);
    }
    // if any SQL errors are thrown in the above processes ...
    catch (SQLException se)
    {
      // ... catch them and let the programmer know that there are errors
      System.out.println("Error creating table for username: " + userName);
    }
  }

  // method to get the best/shortest timed run for an entered user
  public double getShortestTime(String userName)
  {
    // db info
    String dbName = "WhackATimeHistory";
    String tableName = "TimingHistory_" + userName;
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myConn = objDb.getDbConn();

    // variable used to store and return the shortest time of the user
    double shortestTime = 0;

    // query that selects the smallest reaction time as the variable ShortestTime from the user's respective data table
    String dbQuery = "SELECT MIN(reactionTime) AS ShortestTime FROM " + tableName;

    // the following try-catch block tries to select the smallest reaction time from the user's data table, throwing any errors that may have been caught in these processes
    try
    {
      // creating a statement from the db connection and executing the find the smallest value query
      Statement s = myConn.createStatement();
      // storing the executed query results into a result set to check if the currently logged in user does have past history/a shortest time
      ResultSet rs = s.executeQuery(dbQuery);

      // if the rs pointer can move to the next row/there is a smallest time and the user has played their first game ...
      if (rs.next())
      {
        // ... get the double value of the shortest time variable returned from executing the db query
        shortestTime = rs.getDouble("ShortestTime");
        // letting the programmer know that the smallest time has been successfully found
        System.out.println("Found smallest time for: " + userName);
      }
    }
    // if any SQL errors are thrown in the above processes ...
    catch (SQLException se)
    {
      // printing a message for the programmers to let them know that the processes have been unsuccessfully executed
      System.out.println("Error finding smallest time. " + userName);
    }

    // returning the found shortest time - if still 0, caught in Home page to display message prompting users to play their first game
    return shortestTime;
  }

  // method to automatically log in or out a user
  public void autoLogin(String inOrOut) // takes in a String for the programmer to specify if the method is supposed to be acting as auto logging in or logging a user out of the application
  {
    // db info
    String dbName = "WhackATimeHistory";
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myConn = objDb.getDbConn();
    
    // query to get username of user who has last logged in/lastId = 1
    String inQuery = "SELECT userName FROM UserBase WHERE lastId = 1";
    
    // variable to hold the name of the user in the row where lastId = 1
    String lastUser;
    
    // set everyone's lastId equal to 0/no last logged in
    String outQuery = "UPDATE UserBase SET lastId = 0";

    // if the program needs to auto login a user (when the app is run and opens) ...
    if (inOrOut.equals("in"))
    {
      // the following try-catch block tries to find and auto login the user that has a lastId of 1/was previously logged in, throwing any errors that were caught in these processes
      try
      {
        // preparing the select query for execution using the db connection
        PreparedStatement ps = myConn.prepareStatement(inQuery);
        // storing result into result set to get attributes of result
        ResultSet rs = ps.executeQuery();

        // if pointer in result set can go down to next row/there is a row where lastId = 1 ...
        if (rs.next())
        {
          // get the name of the user in the row where lastId = 1
          lastUser = rs.getString("userName"); // gets the corresponding username whose lastId value equals 1
          // logging in the read user into UserManager to be accessed in different parts of the application at different points
          UserManager.loginUser(lastUser);
          // letting the programmer know that the user was successfully auto logged in
          System.out.println("Auto logged in last user named " + lastUser);
        }
        // if no lastId = 1/no one last logged in ...
        else
        {
          // do nothing - letting the programmer know that no lastId = 1, and so no user was auto logged in
          System.out.println("No lastId = 1");
        }
      }
      // if any SQL errors are thrown in the above processes ...
      catch (SQLException se)
      {
        // letting the programmer know that the user was not successfully auto logged in
        System.out.println("Error getting and auto logging last user");
      }
    }
    // if the program needs to log out a user (when logout button on home page is pressed) ...
    else if (inOrOut.equals("out"))
    {
      // the following try-catch block tries to find and reset every user, including the one that has a lastId of 1/was previously logged in, to 0/no one was previously logged in when logging out, throwing any errors that were caught in these processes
      try
      {
        // preparing the update query for execution using the db connection
        PreparedStatement outPs = myConn.prepareStatement(outQuery);
        // executing the update query above
        outPs.executeUpdate();
        // letting the programmer know that no user has a lastId of 1/all have been successfully auto logged out
        System.out.println("All lastId = 0");
      }
      // if any SQL errors are thrown in the above processes ...
      catch (SQLException se)
      {
        // letting the programmer know that the user was not successfully auto logged out in
        System.out.println("Error logging out last user");
      }

      // this way, when the application is closed and then re-opened again, there is no lastId = 1, as such no user will be auto logged in
    }
  }

  // method to get the top three shortest times out of all the users for the leaderboard frame
  public ArrayList<String[]> getLeaderboardData()
  {
    // initializing and creating an empty leaderboard ArrayList of String[] to hold the user name and their shortest time
    ArrayList<String[]> leaderboard = new ArrayList<>();

    // initializing the username of the fetched user
    String username;
    // initializing the String[] row used to hold the username and shortest time of a particular user
    String[] row;

    double shortestTime;

    // the following try-catch block tries to find the users with the shortest reaction times, catching any errors that may have been thrown in executing these processes
    try
    {
      // SQL query to get all users
      String query = "SELECT userName FROM UserBase";
      // preparing the select statement using the db connection
      PreparedStatement ps = this.dbConn.prepareStatement(query);
      // executing the select query/statement, storing the results in a result set table to loop through all the users
      ResultSet rs = ps.executeQuery();

      // this while loop loops through every user selected from the SQL query above
      while (rs.next())
      {
        // set the username of the currently looped over user to the corresponding username value held in the same row in the data table, doing so with the result set
        username = rs.getString("userName");

        // find the shortest/best time of the currently looped over user
        shortestTime = getShortestTime(username);

        // this if statement skips the users who haven't played yet/have a shortest reaction time of 0
        if (shortestTime > 0)
        {
          // creating/constructing a new row to add the leaderboard with ...
          row = new String[]
          {
            // ... the currently looped over user's username ...
            username,
            // ... and the best/shortest time of this particular user, converted to a String value to be added to the String[[ row
            String.valueOf(shortestTime),
          };

          // adding this newly constructed row to the leaderboard ArrayList
          leaderboard.add(row);
        }
      }

      // sort leaderboard in order of fastest/smallest time first
      leaderboard.sort((a, b)
        -> Double.compare( // sort by comparing both second values in each String[] row to determine which shortest time is smaller/faster
          Double.parseDouble(a[1]),
          Double.parseDouble(b[1])
        )
      );
    }
    // if any SQL errors are thrown in the above processes ...
    catch (SQLException se)
    {
      // letting the programmer know that leaderboard data was unsuccessfully retrieved
      System.out.println("Error getting leaderboard data");
    }

    // returning the final leaderboard ArrayList in sorted order from shortest to longest best time
    return leaderboard;
  }

  // method to get the position of the currently logged in user to display on the leaderboard 
  public int getPositionOfUser(String targetUsername) // takes in parameter of a target username in order to get their position
  {
    // ArrayList used to hold the full leaderboard retrieved from the method to do so
    ArrayList<String[]> leaderboard = getLeaderboardData();

    /* 
    the following for loop loops over every user in the gathered leaderboard list,
    and once the user is found it returns the current position of the user + 1 
    to prevent it from showing the index of the user's position, and instead their straight rank
    */
    for (int i = 0; i < leaderboard.size(); i++)
    {
      // if the username in the leaderboard at the currently looped over position equals the username of the currently logged in user ...
      if (leaderboard.get(i)[0].equals(targetUsername))
      {
        // ... return their corresponding index + 1, because rank starts at 1 instead of index 0
        return i + 1;
      }
    }

    // return -1 if user was not found in the leaderboard
    return -1;
  }

  // main method to test JavaDatabase functions
  public static void main(String[] args)
  {
    // db info
    String dbName = "WhackATimeHistory";
    String tableName = "TimingHistory";
    String[] columnNames =
    {
      "gameNumber", "reactionTime"
    };

    // Part 1: How to insert data
    // insert query
    String dbQuery = "INSERT INTO TimingHistory(reactionTime) "
      + "VALUES (?)";
    // connect to db
    JavaDatabase objDb = new JavaDatabase(dbName);
    Connection myConn = objDb.getDbConn();

    // to be read from the calculation class in the GUI program
    // for now we have manually assigned variables
    double readTime = 0.892;

    // insert into database
    try
    {
      // prepare statement
      PreparedStatement ps = myConn.prepareStatement(dbQuery);
      // enter data into query
      ps.setDouble(1, readTime);
      // execute the query
      ps.executeUpdate();

      // printing message for programmers to let them know that the user's data has been successfully inputted into the database
      System.out.println("Data inserted successfully.");
    }
    // catching any errors thrown in inserting data into the database
    catch (SQLException se)
    {
      // printing for message for programmer that an error was thrown in inserting data into the data table
      System.out.println("Error inserting data.");
    }

    // Part 2: How to read and display data
    // reading data from DB into 2d arraylist
    ArrayList<ArrayList<String>> data
      = objDb.getData(tableName, columnNames);
    // displaying the 2d arraylist
    System.out.println(data);

    // Part 3: Testing to2dArray method
    // concert 2d arraylist to 2d array
    Object[][] data2d = objDb.to2dArray(data);
    System.out.println();

    // display the 2d array
    // for every row in the data table ...
    for (int r = 0; r < data2d.length; r++)
    {
      // ... for every column in the data table ...
      for (int c = 0; c < data2d[0].length; c++)
      {
        // print the value in the data table held at the corresponding row and column number
        System.out.print(data2d[r][c] + " ");
      }
      // go to the next line to print the next row
      System.out.println();
    }
  }
}
