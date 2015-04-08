import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Frogger  {

	public Frogger()	{

	}

/*----------Testing----------*/

	public static void main( String[] args )    {
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Frogger.unitTest(new Testing()).endTesting();
		} else if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.COMPONENT_TEST)) {
			Frogger.componentTest(new Testing()).endTesting();
		} else if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.NOP)) {
			Frogger main = new Frogger();
			main.run(args);
		}
	}

	public void run(String[] args)  {
		/*main Program*/
    }

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Frogger Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}

	public static Testing componentTest(Testing t) {
		Frogger.unitTest(t);
		Frog.unitTest(t);
		return t;
	}
}

