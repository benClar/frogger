import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Heart extends Item {


	private final GameObjectType type = GameObjectType.HEART;

	public Heart(double interval)	{
		super(interval);
	}

	public Heart()	{
		super();
	}

	@Override
	public GameObjectType getGameObjectType(){
		return type;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Heart.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Heart Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

