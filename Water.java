import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Water implements GameObject {

	private Direction direction;
	private Timer timer;
	private final GameObjectType type = GameObjectType.WATER;
	private double moveInterval;
	private int speed;
	private final int WATER_BASE_SPEED = 1;

	private static final double MOVE_LOWER_BOUND = 0.5;
	private static final double MOVE_UPPER_BOUND = 0.7;
	private boolean inheritable = true;

	public Water(Direction d, double mInterval)	{
		direction = d;
		moveInterval = mInterval;
		timer = new Timer(moveInterval);
		speed = WATER_BASE_SPEED;
	}

	public void addToSpeed(int s)	{

	}

	public double getMoveInterval()	{
		return moveInterval;
	}

	public boolean justBeenMoved()	{
		return true;
	}

	public static double getUpperBound()	{
		return MOVE_UPPER_BOUND;
	}

	public void makeReady()	{
		
	}

	public static double getLowerBound()	{
		return MOVE_LOWER_BOUND;
	}

	public boolean ready()	{
		return timer.ready();
	}

	public void removeInheritanceSpeed()	{

	}

	public boolean inheritable()	{
		return inheritable;
	}

	public void setInheritanceStatus(boolean b)	{
		
	}

	public boolean inheriting()	{
		return false;
	}

	public Movement move(Direction d, Movement moveCalculator)	{
	 	moveCalculator.dontMove();
	 	return moveCalculator;
	}

	public void setDirection(Direction d)	{

	}

	public int inheritSpeed()	{
		return getSpeed();
	}

	public int getSpeed()	{
		return speed;
	}

	public Direction inheritDirection()	{
		return getDirection();
	}

	public GameObjectType getGameObjectType(){
		return type;
	}

	public Direction getDirection()	{
		return direction;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Water.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Water Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

