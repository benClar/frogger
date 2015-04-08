import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Frog  implements GameObject {

	private Direction direction;
	private Movement frogMovement;

	private final int MOVE_NORTH = -1;
	private final int MOVE_SOUTH = 1;
	private final int MOVE_EAST = 1;
	private final int MOVE_WEST = -1;
	private final int NO_MOVE = 0;
	private final GameObjectType type = GameObjectType.FROG;

	public Frog()	{
		direction = Direction.NORTH; 
		frogMovement = new Movement(0,0);
	}

	public GameObjectType getGameObjectType()	{
		return type;
	}

	private Direction getFrogDirection()	{
		return direction;
	}

	private void setFrogDirection(Direction d)	{
		direction = d;
	}

	public Movement move()	{
		switch(direction)	{
			case NORTH:
				frogMovement.setRowMovement(MOVE_NORTH);
				frogMovement.setColMovement(NO_MOVE);
				break;
			case SOUTH:
				frogMovement.setRowMovement(MOVE_SOUTH);
				frogMovement.setColMovement(NO_MOVE);
				break;
			case EAST:
				frogMovement.setRowMovement(NO_MOVE);
				frogMovement.setColMovement(MOVE_EAST);
				break;
			case WEST:
				frogMovement.setRowMovement(NO_MOVE);
				frogMovement.setColMovement(MOVE_WEST);
				break;
		}
		return frogMovement;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Frog.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		Frog.unitTest_frogMoving(t);
		
		return t;
	}

	public static Testing unitTest_frogMoving(Testing t)	{
		t.enterSuite("Frog Unit Tests: Frog Movement");
		Frog f = new Frog();
		t.compare(Direction.NORTH,"==",f.getFrogDirection(),"Frog Direction initialized to foward");
		t.exitSuite();
		return t;
	}
}

