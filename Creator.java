import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Creator  {

	private World world;

	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
	private final int FROG_INIT_ROW = WORLD_HEIGHT;
	private final int FROG_INIT_COLUMN = WORLD_WIDTH/2;

	public void createWorld()	{
		world = new World(new Frog(), WORLD_WIDTH, WORLD_HEIGHT,FROG_INIT_ROW, FROG_INIT_COLUMN);
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

