import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Coin extends Item {


	private int points;

	private final GameObjectType type = GameObjectType.COIN;

	public Coin(int p)	{
		super();
		points = p;
	}
	
	public Coin(int p,double interval)	{
		super(interval);
		points = p;
	}

	public int getPoints()	{
		return points;
	}

	@Override
	public GameObjectType getGameObjectType(){
		return type;
	}


/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Coin.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Coin Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

