import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Frog implements GameObject{

	private Direction direction;
	// private Movement frogMovement;
	private int row;
	private int col;
	private int speed;
	private boolean inheriting; //! Set to true if object is currently inheriting movement attributes from other objects.
	private boolean justBeenMoved;
	private final int FROG_BASE_SPEED = 0;
	private final int FROG_MOVE_SPEED = 1;
	private boolean invincible;
	private final GameObjectType type = GameObjectType.FROG;
	private final boolean inheritable = false;

	public Frog(int r, int c)	{
		justBeenMoved = false;
		invincible = false;
		inheriting = false;
		direction = Direction.NORTH; 
		// frogMovement = new Movement(0,0);
		row = r;
		col = c;
		speed = FROG_BASE_SPEED;
	}

	public boolean justBeenMoved()	{
		// boolean move;
		// if(justBeenMoved == false)	{
		// 	System.out.println("FROG HAVE NOT JUST BEEN MOVED");
		// 	move = justBeenMoved;
		// 	justBeenMoved = true;
		// 	return move;
		// } else {
		// 	System.out.println("FROG HAVE JUST BEEN MOVED");
		// 	move = justBeenMoved;
		// 	justBeenMoved = false;
		// 	return move;
		// }
		return false;
	}

	public void makeReady()	{

	}

	public void setLastMoved(boolean f)	{

	}

	public void setInvincibility(boolean i)	{
		invincible = i;
	}

	public boolean getInvincibility()	{
		return invincible;
	}

	public int getRow()	{
		return row;
	}

	public int getCol()	{
		return col;
	}

	public boolean ready()	{
		return false;
	}

	public boolean inheritable()	{
		return inheritable;
	}

	public void setInheritanceStatus(boolean i)	{
		inheriting = i;
	}

	public void setRow(int r)	{
		row = r;

	}

	public double getMoveInterval()	{
		return 0;
	}

	public boolean inheriting()	{
		return inheriting;
	}

	public int inheritSpeed()	{
		return 0;
	}

	public void setDirection(Direction d)	{
		direction = d;
	}

	public void addToSpeed(int s)	{
		speed += s;
	}

	public Direction inheritDirection()	{
		return Direction.NONE;
	}

	public void setCol(int c)	{
		col = c;
	}

	public GameObjectType getGameObjectType()	{
		return type;
	}

	public Direction getDirection()	{
		return direction;
	}

	public void setFrogDirection(Direction d)	{
		direction = d;
	}

	public Movement move(Direction d, Movement frogMovement)	{
		setFrogDirection(d);
		frogMovement.getMove(direction,speed);
		row += frogMovement.getRowMovement();
		col += frogMovement.getColumnMovement(); 
		return frogMovement;
	}

	public int getSpeed()	{
		return speed;
	}

	public void removeInheritanceSpeed()	{		
		speed = FROG_BASE_SPEED;
	}

	public Movement playerMove(Direction d, Movement frogMovement)	{
		setFrogDirection(d);
		frogMovement.getMove(direction,FROG_MOVE_SPEED);
		row += frogMovement.getRowMovement();
		col += frogMovement.getColumnMovement(); 
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
		Frog f = new Frog(5,3);
		Movement moveCalc = new Movement(0,0);
		t.compare(Direction.NORTH,"==",f.getDirection(),"Frog Direction initialized to foward");
		f.playerMove(Direction.NORTH,moveCalc);
		t.compare(f.getCol(),"==",3,"Frog hasn't moved horizontally");
		t.compare(f.getRow(),"==",6,"Frog moved one space vertically");
		f.playerMove(Direction.WEST,moveCalc);
		t.compare(f.getCol(),"==",2,"Frog moved left one space horizontally");
		t.compare(f.getRow(),"==",6,"Frog hasn't moved vertically");
		f.playerMove(Direction.SOUTH,moveCalc);
		t.compare(f.getCol(),"==",2,"Frog hasn't moved horizontally");
		t.compare(f.getRow(),"==",5,"Frog moved down one space");
		f.playerMove(Direction.EAST,moveCalc);
		t.compare(f.getCol(),"==",3,"Frog moved right once space horizontally");
		t.compare(f.getRow(),"==",5,"Frog hasn't moved vertically");			
		t.exitSuite();
		return t;
	}
}

