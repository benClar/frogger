import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.*;

public class Cell {

	private ArrayList<GameObject> gameCell;

	public Cell()	{
		gameCell = new ArrayList<GameObject>();
	}

	public void addToCell(GameObject newGameObj)	{
		gameCell.add(newGameObj);
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Cell.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("CellUnit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

