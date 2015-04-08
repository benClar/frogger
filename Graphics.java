import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Graphics  {

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Graphics.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Graphics Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

