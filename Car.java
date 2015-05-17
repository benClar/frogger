import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Car implements GameObject  {



	private Direction direction;
	private Timer action;

	private final int CAR_BASE_SPEED = 1;
	private final double DEFAULT_CAR_MOVE_INTERVAL = 0.5;
	private final GameObjectType type = GameObjectType.CAR;
	private final boolean inheritable = false;

	public Car(Direction d, double interval)	{
		direction = d;
		action = new Timer(interval);
	}

	public void setDirection(Direction d)	{

	}

	public boolean justBeenMoved()	{
		return true;
	}

	public void addToSpeed(int s)	{

	}

	public void makeReady()	{

	}

	public void setLastMoved(boolean f)	{
		
	}

	public double getMoveInterval()	{
		return 0;
	}

	public void removeInheritanceSpeed()	{

	}

	public boolean ready()	{
		return false;
	}

	public Car(Direction d)	{
		direction = d;
		action = new Timer(DEFAULT_CAR_MOVE_INTERVAL);
	}

	public void setInheritanceStatus(boolean b)	{
		
	}

	public boolean inheriting()	{
		return false;
	}

	public Movement move(Direction d, Movement carMovement)	{
		if(action.ready())	{
			carMovement.getMove(d,CAR_BASE_SPEED);
		} else {
			carMovement.dontMove();
		}
		return carMovement;
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

	public GameObjectType getGameObjectType()	{
		return type;	
	}

	public Direction getDirection()	{
		return direction;
	}

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Car.unitTest(new Testing()).endTesting();
		} 
	}

/*----------Testing----------*/


	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Car Unit Tests");
		Movement m = new Movement(0,0);
		Car c = new Car(Direction.NORTH);
		t.compare(c.action.ready(),"==",true,"Car ready to move");
		c.move(Direction.NORTH,m);
		t.compare(c.action.ready(),"==",false,"Car not ready to move");
		t.exitSuite();
		return t;
	}
}

