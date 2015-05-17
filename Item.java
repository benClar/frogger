import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Item implements GameObject{

	private Timer timer;
	private double moveInterval;
	private int speed;
	private Direction direction;
	boolean justBeenMoved;

	private final GameObjectType type = GameObjectType.ITEM;
	private final int ITEM_BASE_SPEED = 0;

	public Item()	{
		moveInterval = 0;
		timer = new Timer(moveInterval);
		speed = ITEM_BASE_SPEED;
		direction = Direction.NONE;
		justBeenMoved = false;
	}

	public Item(double interval)	{
		moveInterval = interval;
		timer = new Timer(moveInterval);
		speed = ITEM_BASE_SPEED;
		direction = Direction.NONE;
		justBeenMoved = false;
	}

	public Movement move(Direction d, Movement moveCalculator)	{
		moveCalculator.dontMove();
		return moveCalculator;
	}

	public GameObjectType getGameObjectType(){
		return type;
	}

	public Direction getDirection(){
		return direction;
	}

	public int inheritSpeed(){
		return speed;
	}

	public Direction inheritDirection(){
		return getDirection();
	}

	public boolean inheritable(){
		return true;
	}

	public void setDirection(Direction d){
		direction = d;
	}

	public void setInheritanceStatus(boolean i){

	}

	public void addToSpeed(int s){
		speed += s;
	}

	public void removeInheritanceSpeed(){
		speed = ITEM_BASE_SPEED;
	}

	public boolean ready(){
		return timer.ready();
	}

	public void setLastMoved(boolean f)	{

	}

	public boolean justBeenMoved(){
		boolean move;
		// if(justBeenMoved == false)	{
		// 	move = justBeenMoved;
		// 	justBeenMoved = true;
		// 	return move;
		// } else {
		// 	move = justBeenMoved;
		// 	justBeenMoved = false;
		// 	return move;
		// }
		return false;
	}

	public double getMoveInterval(){
		return moveInterval;
	}

	public void makeReady(){
		timer.makeReady();
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Item.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Item Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

