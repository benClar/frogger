import com.bclarke.testing.*;
import com.bclarke.general.*;

public class BloodSplatter implements GameObject {

	private int speed;
	private boolean justBeenMoved;
	private Direction direction;
	private boolean readiness;
	private final int WATER_BASE_SPEED = 0;
	private final GameObjectType type = GameObjectType.BLOOD;

	public BloodSplatter()	{
		justBeenMoved = false;
		readiness = false;
		speed = WATER_BASE_SPEED;
		direction = Direction.NONE;

	}

	public Movement move(Direction d, Movement bloodMovement)	{
		bloodMovement.getMove(d,speed);
		return bloodMovement;
	}

	public double getMoveInterval()	{
		return 0;
	}

	public void makeReady()	{
		readiness = true;
	}

	public GameObjectType getGameObjectType()	{
		return type;
	}

	public void addToSpeed(int s)	{
		speed += s;
	}

	public void removeInheritanceSpeed()	{
		speed = WATER_BASE_SPEED;
	}

	public boolean justBeenMoved()	{
		boolean move;
		if(justBeenMoved == false)	{
			// System.out.println("LOG HAVE NOT JUST BEEN MOVED");
			move = justBeenMoved;
			justBeenMoved = true;
			return move;
		} else {
			// System.out.println("LOG HAVE JUST BEEN MOVED");
			move = justBeenMoved;
			justBeenMoved = false;
			return move;
		}
	}

	public boolean ready()	{
		boolean readinessTemp = readiness;
		readiness = false;
		return readinessTemp;
	}

	public Direction getDirection()	{
		return Direction.NORTH;
	}

	public int inheritSpeed()	{
		return speed;
	}

	public Direction inheritDirection()	{
		return direction;
	}

	public void setInheritanceStatus(boolean b)	{

	}

	public boolean inheriting()	{
		return false;
	}

	public void setDirection(Direction d)	{
		direction =d;
	}

	public boolean inheritable()	{
		return true;
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

