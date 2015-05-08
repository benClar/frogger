import com.bclarke.testing.*;
import com.bclarke.general.*;

public class CreateInstruction {

	private GameObjectType type;
	private int row;
	private Direction direction;
	private Timer action;
	private double objectMovementInterval;


	public CreateInstruction(GameObjectType t, double cInt, double obInterval, Direction d,int r)	{
		action = new Timer(cInt);
		type = t;
		objectMovementInterval = obInterval;
		direction = d;
		row = r;
	}

	public GameObjectType getType()	{
		return type;
	}

	public Boolean ready()	{
		return action.ready();
	}

	public void setInterval(double i)	{
		action.setInterval(i);
	}

	public Direction getDirection()	{
		return direction;
	}

	public int getRow()	{
		return row;
	}

	public double getMovementInterval()	{
		return objectMovementInterval;
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