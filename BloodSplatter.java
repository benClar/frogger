import com.bclarke.testing.*;
import com.bclarke.general.*;

public class BloodSplatter implements GameObject {

	private final int GROUND_BASE_SPEED = 0;
	private final GameObjectType type = GameObjectType.BLOOD;

	public Movement move(Direction d, Movement bloodMovement)	{
		bloodMovement.dontMove();
		return bloodMovement;
	}

	public GameObjectType getGameObjectType()	{
		return type;
	}

	public Direction getDirection()	{
		return Direction.NORTH;
	}

/*----------Testing----------*/



	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			BloodSplatter.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("BloodSplatter Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

