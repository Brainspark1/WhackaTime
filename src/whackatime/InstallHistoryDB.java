/*
Nihaal Garud, April 2nd 2026
Creating a new database and corresponding past history table as an abstraction of the the Whack-a-Time game to add history database functionality
*/

package whackatime;

public class InstallHistoryDB
{
  // main method to install/create a new database and data table with it using methods in the JavaDatabase class
  public static void main(String[] args)
  {
    // db info
    String dbName = "WhackATimeHistory"; // name of database to be created
    JavaDatabase objDb = new JavaDatabase(); // creating new object of JavaDatabase to access all the methods required to set up/create a new database from the class
    
    /* 
    the following line typically will stay commented out, to 
    allow this file to run without a problem and create a new table
    called TimingHistory when this file is run without producing
    the SQL error that the database is already created
    */
    // objDb.createDb(dbName);
    
    // creating table using SQL database query to do so
    String newTable = "CREATE TABLE TimingHistory " // name of new table created within newly created database called TimingHistory
      + "(gameNumber int NOT NULL AUTO_INCREMENT, " // creation of a table column storing the gameNumber of different rows entered into the data table - placing parameters on the game numbers to not be null/has to exist and starts to auto-increment from 1 whenever new rows are added into the table
      + "reactionTime decimal(4, 3), " // creation of a table column storing the decimal value of the reaction times of rows entered into the data table
      + "PRIMARY KEY (gameNumber) )" // setting the game number to the primary key/column value that uniquely identifies a particular row, meaning all game numbers values must be unique/distinct
      + " AUTO_INCREMENT=1"; // starting the increment of the game number of newly created rows in the data table from 1 and increasing by 1 each time for each new row
    
    // creating a new data table within the newly created database using the SQL create table query created above
    objDb.createTable(newTable, dbName);
  }
}
