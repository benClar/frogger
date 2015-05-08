import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Ground implements GameObject {


	private final int GROUND_BASE_SPEED = 0;
	private final GameObjectType type = GameObjectType.GROUND;

	public Movement move(Direction d, Movement groundMovement)	{
		groundMovement.dontMove();
		return groundMovement;
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
			Ground.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Ground Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}

}

