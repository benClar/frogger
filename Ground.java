import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Ground implements GameObject {


	private final int GROUND_BASE_SPEED = 0;
	private final GameObjectType type = GameObjectType.GROUND;
	private final boolean inheritable = false;

	public Movement move(Direction d, Movement groundMovement)	{
		groundMovement.dontMove();
		return groundMovement;
	}
	
	public GameObjectType getGameObjectType()	{
		return type;
	}

	public void setDirection(Direction d)	{

	}

	public double getMoveInterval()	{
		return 0;
	}

	public void addToSpeed(int s)	{

	}

	public void makeReady()	{
	}

	public boolean justBeenMoved()	{
		return true;
	}

	public boolean ready()	{
		return false;
	}

	public void removeInheritanceSpeed()	{

	}

	public Direction getDirection()	{
		return Direction.NORTH;
	}

	public boolean inheritable()	{
		return inheritable;
	}

	public int inheritSpeed()	{
		return 0;
	}

	public Direction inheritDirection()	{
		return Direction.NONE;
	}

	public void setInheritanceStatus(boolean b)	{
		
	}

	public boolean inheriting()	{
		return false;
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

