import com.bclarke.testing.*;
import com.bclarke.general.*;

public class CreateInstruction  {

	private GameObjectType type;
	private Direction direction;
	private int row;
	private int interval;

	public CreateInstruction(Direction d, GameObjectType t, int r, int i)	{
		type = t;
		direction = d;
		row = r;
		interval = i;
	}

	public GameObjectType getType()	{
		return type;
	}

	public Direction getDirection()	{
		return direction;
	}

	public int getRow()	{
		return row;
	}

	public int getInterval()	{
		return interval;
	}
/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			CreateInstruction.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("CreateInstruction Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

