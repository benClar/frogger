import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.*;

public class World  {

	private Cell[][] board;

	private Frog frogger;
	private frogRow;
	private frogCol;

	public World(Frog newFrog, int height, int width, int frogInitrow, int frogInitcol)	{
		board = new Cell[height][width];
		frogger = newFrog;
		addToWorld(frog_row, frog_col, frogger);
		frogRow = frogInitrow;
		frogCol = frogInitcol;
	}

	public void addToWorld(int row, int col, GameObject newGameObj)	{
		board[row][col].addToCell(newGameObj);
	}

	public void moveFrog(Direction m)	{
		Movement frogMovement;
		frogger.setFrogDirection(m);
		frogMovement = frogger.move();
		changeWorldFrogPosition(frogMovement.getRowMovement(),frogMovement.getColMovement());
		moveObjectPosition(getFrogRow(),getFrogCol(),frogMovement);
	}

	private void changeWorldFrogPosition(int r, int c)	{
		frogRow += r;
		frogCol += c;
	}

	private int getFrogRow()	{
		return frogRow;
	}

	private int getFrogCol()	{
		return frogCol;
	}
	
	public void moveObjectPosition(int oldRow, int oldCol, Movement newMove)	{

	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			World.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

