import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.*;

public class Frogger {

	private ConcurrentLinkedQueue<UserCommand> moveQueue;
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
		moveQueue = new ConcurrentLinkedQueue<UserCommand>();
		createQueue = new LinkedList<CreateInstruction>();
		World w = new World(moveQueue,createQueue);
		while(true)	{
            w.tick();
            w.interpret(get());
		}
    }

    public UserCommand get()	{
    	try {
    		return moveQueue.remove(); 
    	} catch (Exception err) { 
        	return UserCommand.NO_COMMAND; 
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
		Frog.unitTest(t);
		Movement.unitTest(t);
		Display.unitTest(t);
		Cell.unitTest(t);
		Car.unitTest(t);
		Timer.unitTest(t);
		Board.unitTest(t);
		World.unitTest(t);
		return t;
	}

}

