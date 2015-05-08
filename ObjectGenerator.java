import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.Random;
import java.util.*;

public class ObjectGenerator  {

	ArrayList <CreateInstruction> rows;
	Random randomGenerator;
	private Queue<CreateInstruction> createQueue;

	private final int CAR_BOUNDARY = 40;
	private final int EAST_DIRECTION_BOUNDARY = 50;
	private final int UPPER_BOUND = 101;
	private final double CREATION_INTERVAL_LOWER_BOUND = .5;
	private final double CREATION_INTERVAL_UPPER_BOUND = 1.7;
	private final double CAR_MOVEMENT_INTERVAL = .1;

	public ObjectGenerator(int height, Queue<CreateInstruction> q)	{
		createQueue = q;
		rows = new ArrayList<CreateInstruction>();
		randomGenerator = new Random();
		populateIndex(height);
	}

	public void generate()	{
		for(int i = 0; i < rows.size(); i++)	{
			if(rows.get(i).ready() && rows.get(i).getType() != GameObjectType.GROUND)	{
				rows.get(i).setInterval(generateInterval(CREATION_INTERVAL_LOWER_BOUND,CREATION_INTERVAL_UPPER_BOUND));
				createQueue.add(rows.get(i));
			}
		}
	}

	private void populateIndex(int height)	{
		for(int i = 0; i < height; i++)	{
			rows.add(generateObjectInstruction(i));
		}
	}

	private CreateInstruction generateObjectInstruction(int r)	{
		int rndm = randomGenerator.nextInt(UPPER_BOUND);

		if(rndm <= CAR_BOUNDARY)	{
			return new CreateInstruction(GameObjectType.CAR,generateInterval(CREATION_INTERVAL_LOWER_BOUND,CREATION_INTERVAL_UPPER_BOUND),CAR_MOVEMENT_INTERVAL,generateDirection(),r);
		} else {
			return new CreateInstruction(GameObjectType.GROUND,0,0,generateDirection(),r);
		}
	}

	private Double generateInterval(double lowerBound, double upperBound)	{
		Random r = new Random();
		return lowerBound + (upperBound  - lowerBound) * r.nextDouble();
	}

	private Direction generateDirection()	{
		int rndm = randomGenerator.nextInt(UPPER_BOUND);
		if(rndm <= EAST_DIRECTION_BOUNDARY)	{
			return Direction.EAST;
		}
		return Direction.WEST;

	}

	private int generateRow(int limit)	{
		return 0;
	}


	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			ObjectGenerator.unitTest(new Testing()).endTesting();
		} 
	}



/*----------Testing----------*/


	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("ObjectGenerator Unit Tests");
		ObjectGenerator og = new ObjectGenerator(10, new LinkedList<CreateInstruction>());
		System.out.println(og.generateInterval(0,3));
		t.exitSuite();
		return t;
	}
}

