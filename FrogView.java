import com.bclarke.testing.*;
import com.bclarke.general.*;

public class FrogView  {

	private int lowerView;
	private int upperView;
	private int relPosition;
	private int initialUpper;


	private final int VIEW_DISTANCE = 6;

	public FrogView(int upper)	{
		lowerView = 0;
		upperView = upper;
		initialUpper = upper;
		relPosition = 0;
	}

	public boolean increaseView()	{
		increaseRelativePosition();
		if(upperView - relPosition < getViewDistance())	{
			lowerView++;
			upperView++;
			return true;
		}

		return false;
	}

	public int getViewDistance()	{
		return VIEW_DISTANCE - 1;
	}

	private void increaseRelativePosition()	{
		relPosition++;
	}

	public void resetView()	{
		lowerView = relPosition = 0;
		upperView = initialUpper;
	}

	public void decreaseView()	{
		if(lowerView > 0)	{
			relPosition--;
			lowerView--;
			upperView--;
		}
	}

	public int getUpperView()	{
		return upperView;
	}

	public int getLowerView()	{
		return lowerView;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			FrogView.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("FrogView Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

