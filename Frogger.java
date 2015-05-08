import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.*;

public class Frogger {

	private ConcurrentLinkedQueue<Direction> moveQueue;
	private Queue<CreateInstruction> createQueue;

	public static void main( String[] args )    {
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Frogger.unitTest(new Testing()).endTesting();
		} else if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.COMPONENT_TEST)) {
			Frogger.componentTest(new Testing()).endTesting();
		} else if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.NOP)) {
			Frogger program = new Frogger();
			program.run();
		}
	}

	public void run()  {
		moveQueue = new ConcurrentLinkedQueue<Direction>();
		createQueue = new LinkedList<CreateInstruction>();
		Creator c = new Creator();
		World w = c.createWorld(moveQueue,createQueue);
		ObjectGenerator og = c.createObjectGenerator(createQueue);
		while(true)	{
            w.moveFrog(get());
            og.generate();
            w.checkForInstructions();
            w.checkFrog();
            w.moveWorld();
		}
    }

    public Direction get()	{
    	try {
    		return moveQueue.remove(); 
    	} catch (Exception err) { 
        	return Direction.NONE; 
        }
    }



/*----------Testing----------*/



	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Frogger Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}

	public static Testing componentTest(Testing t) {
		Frogger.unitTest(t);
		Frog.unitTest(t);
		Movement.unitTest(t);
		World.unitTest(t);
		Display.componentTest_frogMove(t);
		Cell.unitTest(t);
		Car.unitTest(t);
		ObjectGenerator.unitTest(t);
		Timer.unitTest(t);
		return t;
	}

}

