import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.concurrent.*;
import java.util.*;

public class Creator  {

	private World world;

	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
	private final int FROG_INIT_ROW = WORLD_HEIGHT - 1;
	private final int FROG_INIT_COLUMN = WORLD_WIDTH/2;
	private final int CELL_WIDTH = 30;
	private final int CELL_HEIGHT = 30;

	public World createWorld(ConcurrentLinkedQueue<Direction> moveQueue,Queue<CreateInstruction> createQueue)	{
		world = new World(new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN), WORLD_HEIGHT, WORLD_WIDTH, CELL_WIDTH,CELL_HEIGHT,moveQueue,createQueue);
		return world;
	}

	public ObjectGenerator createObjectGenerator(Queue<CreateInstruction> createQueue)	{
		return new ObjectGenerator(WORLD_HEIGHT,createQueue);
	}

	public World getWorld()	{
		return world;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Creator.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Creator Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

