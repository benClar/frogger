import com.bclarke.testing.*;
import com.bclarke.general.*;

public class BloodSplatter implements GameObject {

	private final int GROUND_BASE_SPEED = 0;
	private final GameObjectType type = GameObjectType.BLOOD;

	public Movement move(Direction d, Movement bloodMovement)	{
		bloodMovement.dontMove();
		return bloodMovement;
	}

	public double getMoveInterval()	{
		return 0;
	}

	public void makeReady()	{
		
	}

	public GameObjectType getGameObjectType()	{
		return type;
	}

	public void addToSpeed(int s)	{

	}

	public void removeInheritanceSpeed()	{

	}

	public boolean justBeenMoved()	{
		return true;
	}

	public boolean ready()	{
		return false;
	}

	public Direction getDirection()	{
		return Direction.NORTH;
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

	public void setDirection(Direction d)	{

	}

	public boolean inheritable()	{
		return false;
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

