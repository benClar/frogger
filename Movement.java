import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Movement  {

	private int rowMovement;
	private int columnMovement;

	private final int NO_MOVE = 0;


	public Movement(int rowM, int colM)	{
		rowMovement = rowM;
		columnMovement = colM;
	}

	public Movement getMove(Direction direction, int speed)	{
		switch(direction)	{
			case NORTH:
					moveNorth(speed);
				break;
			case SOUTH:
					moveSouth(speed);
				break;
			case EAST:
					moveEast(speed);
				break;
			case WEST:
					moveWest(speed);
				break;
		}
		return this;
	}

	public int getRowMovement()	{
		return rowMovement;
	}

	public int getColumnMovement()	{
		return columnMovement;
	}

	private void setRowMovement(int rowM)	{
		rowMovement = rowM;
	}

	private void setColumnMovement(int colM)	{
		columnMovement = colM;
	}

	public void moveNorth(int n)	{
		setRowMovement(-n);
		setColumnMovement(NO_MOVE);
	}

	public void moveSouth(int n)	{
		setRowMovement(n);
		setColumnMovement(NO_MOVE);
	}

	public void moveEast(int n)	{
		setRowMovement(NO_MOVE);
		setColumnMovement(n);
	}

	public void moveWest(int n)	{
		setRowMovement(NO_MOVE);
		setColumnMovement(-n);
	}

	public void dontMove()	{
		setRowMovement(NO_MOVE);
		setColumnMovement(NO_MOVE);
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Movement.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		Movement m = new Movement(0,0);
		t.enterSuite("Movement Unit Tests");
		m.moveNorth(10);
		t.compare(-10,"==",m.getRowMovement(),"Row Movement is -10 to move North");
		t.compare(0,"==",m.getColumnMovement(),"Column Movement is 0 to move North");
		m.moveSouth(10);
		t.compare(10,"==",m.getRowMovement(),"Row Movement is 10 to move South");
		t.compare(0,"==",m.getColumnMovement(),"Column Movement is 0 to move South");
		m.moveEast(10);
		t.compare(0,"==",m.getRowMovement(),"Row Movement is 0 to move East");
		t.compare(10,"==",m.getColumnMovement(),"Column Movement is 10 to move East");
		t.exitSuite();
		return t;
	}
}

