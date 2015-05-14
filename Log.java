import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Log implements GameObject {

	
	private int speed;
	private Direction direction;
	private double moveInterval;
	private boolean inheriting; //! Set to true if object is currently inheriting movement attributes from other objects.
	private boolean justBeenMoved;
	private Timer timer;

	private final GameObjectType type = GameObjectType.LOG;
	private final boolean inheritable = true; 
	private final int LOG_BASE_SPEED = 0;

	public Log(double interval)	{
		justBeenMoved = false;
		inheriting = false;
		speed = LOG_BASE_SPEED;
		timer = new Timer(interval);
		moveInterval = interval;
		direction = Direction.NONE;
	}


	public boolean ready()	{
		return timer.ready();

	}

	public void makeReady()	{
		timer.makeReady();
	}

	public double getMoveInterval()	{
		return moveInterval;
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

	public int inheritSpeed()	{
		return getSpeed();
	}

	public boolean inheritable()	{
		return inheritable;
	}

	public void setInheritanceStatus(boolean i)	{
		inheriting = i;
	}

	public void addToSpeed(int s)	{
		speed += s;
	}

	public Direction inheritDirection()	{
		return getDirection();
	}

	public Movement move(Direction d, Movement logMovement)	{
		logMovement.getMove(direction,speed);
		return logMovement;
	}

	public void setDirection(Direction d)	{
		direction = d;
	}

	public Direction getDirection()	{
		return direction;
	}

	public boolean inheriting()	{
		return inheriting;
	}

	public int getSpeed()	{
		return speed;
	}


	public void removeInheritanceSpeed()	{		
		speed = LOG_BASE_SPEED;
	}

	public GameObjectType getGameObjectType(){
		return type;
	}


/*----------Testing----------*/


	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Log.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Log Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

