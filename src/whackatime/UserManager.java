/*
Nihaal Garud, April 2nd 2026
Managing users who are logged in to the game, passing information via static methods and variables to other classes to keep the user logged in and log their results to their particular data table
 */

package whackatime;

public class UserManager
{

  // variable that stores the current user logged in - needs to be static to be referenced in the static methods below - needs to be static as in the program, there can only be one current user that is currently logged in
  private static String currentUser = null;

  // set method that logs in a current user after they press the login button in login frame, fetching or creating their particular database of past results and adding new game entries/results into their corresponding past results db - needs to be static as only one user can be logged in in the entire program, not multiple if objects of the UserManager class were instead created
  public static void loginUser(String user)
  {
    // set current user equal to logged in user
    currentUser = user;
  }

  // method that logs out a current user after they press the logout button in home frame, resetting the components that can be found on the home frame to allow other users to sign up/login - needs to be static as only one user can be logged out at a time in the entire program, so the method must belong to the UserManager class, not to separate object instances
  public static void logoutUser()
  {
    // no current users logged in
    currentUser = null;
  }

  // method that returns the statically encapsulated current user when creating or fetching new tables in the db for that particular user's past results/history page
  public static String getCurrentUser()
  {
    // returning who is the current user
    return currentUser;
  }

  // method that checks if the user is logged in
  public static boolean isUserLoggedIn()
  {
    // if the user stored as current user is not nothing (null)
    if (currentUser != null)
    {
      // return that a user is logged in (true)
      return true;
    }
    // if the user stored as current user is nothing (null)
    else
    {
      // return that a user is not logged in (false)
      return false;
    }
  }

  // main method to test UserManager functionality
  public static void main(String[] args)
  {
    // testing no user logged in initially
    System.out.println("User: " + getCurrentUser() + ", " + isUserLoggedIn());
    System.out.println();
    // testing logging in user
    loginUser("Brainspark1");
    System.out.println("User: " + getCurrentUser() + ", " + isUserLoggedIn());
    System.out.println();
    // testing changing logged in user
    loginUser("sebastian");
    System.out.println("User: " + getCurrentUser() + ", " + isUserLoggedIn());
    System.out.println();
    // testing logging out user
    logoutUser();
    System.out.println("User: " + getCurrentUser() + ", " + isUserLoggedIn());
  }
}
