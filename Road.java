import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Road implements GameObject  {

	private final int ROAD_BASE_SPEED = 0;
	private final GameObjectType type = GameObjectType.ROAD;
	private final boolean inheritable = false;
	public Movement move(Direction d, Movement roadMovement)	{
		roadMovement.dontMove();
		return roadMovement;
	}

	public GameObjectType getGameObjectType()	{
		return type;
	}

	public double getMoveInterval()	{
		return 0;
	}

	public Direction getDirection()	{
		return Direction.NORTH;
	}

	public int inheritSpeed()	{
		return 0;
	}

	public void setLastMoved(boolean f)	{

	}


	public void makeReady()	{

	}

	public void addToSpeed(int s)	{

	}

	public boolean justBeenMoved()	{
		return true;
	}

	public boolean ready()	{
		return false;
	}

	public void removeInheritanceSpeed()	{

	}

	public boolean inheritable()	{
		return inheritable;
	}

	public Direction inheritDirection()	{
		return Direction.NONE;
	}

	public void setInheritanceStatus(boolean b)	{
		
	}

	public boolean inheriting()	{
		return false;
	}

	public void setDirection(Direction d)	{

	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Road.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Road Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

