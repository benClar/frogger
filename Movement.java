import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Movement  {

	private int rowMovement;
	private int columnMovement;

	public Movement(int rowM, int colM)	{
		rowMovement = rowM;
		columnMovement = colM;
	}

	public int getRowMovement()	{
		return rowMovement;
	}

	public int getColumnMovement()	{
		return columnMovement;
	}

	public void setRowMovement(int rowM)	{
		rowMovement = rowM;
	}

	public void setColMovement(int colM)	{
		columnMovement = colM;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Movement.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Movement Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

