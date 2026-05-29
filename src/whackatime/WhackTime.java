/*
Nihaal Garud, March 28th 2026
Modelling a computation and logic class with corresponding methods to add functionality to and time the game
 */

package whackatime;

// importing all necessary GUI components
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// importing Random in order to generate a random index value in the grid to move the mole to
import java.util.Random;

public class WhackTime
{
  // JLabel representing each empty cell to be placed in the overlaid grid
  private JLabel emptyCell;

  // constants for grid and number of moles that will appear
  private final int ROWS = 6; // 6 rows
  private final int COLUMNS = 6; // 6 columns
  private final int NUM_MOLES = 30; // 30 moles total to be clicked by the user

  // holding the randomly chosen index that the mole button must be randomly placed inside of
  private int targetIndex;
  // holding the number of clicks made by the user during a round
  private int clickCount = 0;

  // holding the time that the mole button was last clicked by the user
  private double lastClickTime;
  // holding the difference between times when the mole button was last clicked vs most recently clicked to find the reaction time for that mole movement
  private double reactionTime;
  // holding the sum of all the reaction times from the users
  private double totalTime = 0;

  // holds the index that the mole was previously at to prevent it from appearing at that same index consecutively
  private int previousIndex = 0;

  // constructor with parameters to construct a new computation class with values that are already known if ever called
  public WhackTime(double totalTime, int clickCount)
  {
    this.lastClickTime = 0; // new computation class, so no clicks before/no last click time
    this.totalTime = totalTime;
    this.clickCount = clickCount;
  }

  // constructor without parameters to construct a new computation class with default values if ever called
  public WhackTime()
  {
    this.lastClickTime = 0; // new computation class, so no clicks before/no last click time
    this.totalTime = 0;
    this.clickCount = 0;
  }

  // get method to return the total reaction time taken by the users in the round
  public double getTotalReactionTime()
  {
    // returns the total reaction time
    return this.totalTime;
  }

  // get method to return clickCount to make sure at 30 clicks
  public int getClickCount()
  {
    // returns the total number of clicks
    return this.clickCount;
  }

  // get method to return number of remaining moles left in the game
  public int getRemainingMoles()
  {
    return NUM_MOLES - clickCount;
  }

  // get method to return average reaction time of users
  public double getAverageTime()
  {
    // returning the average time (total time divided by number of clicks made)
    return (totalTime / (clickCount - 1)) / 1000; // converting to seconds from milliseconds by dividing by 1000, first click doesn't count, only intervals between clicks - why subtract one from clickCount in calculation
  }

  // set method to set the total reaction time taken by users in a round
  public void setTotalReactionTime(double totalTime)
  {
    this.totalTime = totalTime;
  }

  // set method to set the total number of clicks made by the user in a round
  public void setClickCount(int clickCount)
  {
    this.clickCount = clickCount;
  }

  // method to produce the index of a random cell to randomly add the mole button to
  public int randomCell()
  {
    // creating a new random object
    Random random = new Random();
    // randomly choosing an integer value between 0 (cell 1) and index of final cell in grid, or rows x columns - 1 = 6 x 6 = 36 - 1 = 35 (cell 36)
    int newIndex = random.nextInt(ROWS * COLUMNS);

    // the following while loop implements the functionality which prevents the mole from appearing in the same cell consecutively
    // if the newly generated index number is still equal to the last stored index/where the mole was last ...
    while (newIndex == previousIndex)
    {
      // ... keep randomly generating new index values until it is new
      newIndex = random.nextInt(ROWS * COLUMNS);
    }

    // assign the previously stored index value to the newly generated index value, to prevent that particular index from having the mole button straight after
    previousIndex = newIndex;

    // returning the new randomly generated index value
    return newIndex;
  }

  // method to randomly move the mole button in a random cell along the overlaid grid once the previous one had been clicked until the game has finished
  public boolean moveMole(JButton randomButton, JPanel gridPanel)
  {
    // stopping game after 30 moles have been clicked
    if (clickCount >= NUM_MOLES)
    {
      return false; // stop playing
    }

    gridPanel.removeAll(); // remove all existing mole buttons when they are clicked

    // randomly generate a new index along the overlaid grid panel to place the new mole button in
    targetIndex = randomCell();

    // the following for loop goes through every cell in the overlaid grid on the game frame, placing empty JLabels in each of the other cells until it comes across the randomly generate cell index 
    for (int i = 0; i < ROWS * COLUMNS; i++)
    {
      // if the for loop has reached the randomly generated target index value ...
      if (i == targetIndex)
      {
        // ... add to that particular cell the new mole button, setting a border for the button for the grid lines to appear along the grid
        randomButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        gridPanel.add(randomButton);
      }
      // if the for loop still hasn't reached or has already gone through the randomly generated target index value ...
      else
      {
        // ... create a new empty JLabel to fill this empty cell
        emptyCell = new JLabel("");
        // setting a border for this JLabel in order for the grid lines to appear along the grid
        emptyCell.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        // adding the empty cell to the grid panel
        gridPanel.add(emptyCell);
      }
    }

    // validating and repainting the grid panel to update its changes for users to see
    gridPanel.validate();
    gridPanel.repaint();

    return true; // continue playing
  }

  // method to execute different methods and functions once a mole button has been clicked by the user
  public void recordClick()
  {
    // if total recorded number of clicks reaches the total number of moles that are supposed to appear (30) ...
    if (clickCount == NUM_MOLES)
    {
      return; // ... stop counting clicks further once game is over
    }

    double currentTime = System.currentTimeMillis(); // gets current time of click

    if (clickCount > 0) // if user still has more clicks to do
    {
      // reaction time/time difference is the same as finding the difference between the current time clicked and the time that the mole button was last clicked
      reactionTime = currentTime - lastClickTime;
      // adding this new calculated reaction time to the total amount of time to be averaged out at the end of the round
      totalTime += reactionTime;
    }

    lastClickTime = currentTime; // sets current time to time of button last clicked
    clickCount++; // increases total number of clicks
  }

  // toString method to return assigned attributes of the current object of the WhackTime class when asked to give/print its memory address
  @Override
  public String toString()
  {
    return "Total Reaction Time: " + this.totalTime
      + ", Click Count: " + this.clickCount;
  }

  // main method to test WhackTime class logic and edge cases
  public static void main(String[] args)
  {
    // initializing new WhackTime computation class object
    WhackTime objTime = new WhackTime();
    
    // variables to hold randomly generated first and second indices to test cell generation
    int firstIndex;
    int secondIndex;

    // will work properly (recording 3 clicks)
    objTime.recordClick();
    objTime.recordClick();
    objTime.recordClick();

    System.out.println(objTime.getClickCount());
    System.out.println(objTime.getTotalReactionTime());
    System.out.println(objTime.getAverageTime());

    // will test remaining moles logic
    System.out.println(objTime.getRemainingMoles());

    // will test random cell generation - should not repeat consecutively
    firstIndex = objTime.randomCell();
    secondIndex = objTime.randomCell();

    System.out.println(firstIndex);
    System.out.println(secondIndex);

    // will test stopping condition at 30 clicks
    WhackTime objTimeTest = new WhackTime();

    // the following for loop loops 31 times to simulated 30 moles being clicked (30 intervals for 31 clicks)
    for (int i = 0; i < 30; i++)
    {
      objTimeTest.recordClick();
    }

    System.out.println(objTimeTest.getClickCount()); // should be at 30
    System.out.println(objTimeTest.getRemainingMoles()); // should be 0

    // printing object to test toString method
    System.out.println(objTime);
  }
}
