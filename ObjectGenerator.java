import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.Random;
import java.util.*;

public class ObjectGenerator  {

	private ArrayList <CreateInstruction> rows;
	private Random randomGenerator;
	private Queue<CreateInstruction> createQueue;
	private FrogView view;
	private Timer timer;
	private Direction toggle;
	private final int CAR_BOUNDARY = 60;
	private final int WATER_BOUNDARY = 30;
	private final int EAST_DIRECTION_BOUNDARY = 50;
	private final int UPPER_BOUND = 101;
	private final double CREATION_INTERVAL_LOWER_BOUND = .5;
	private final double CREATION_INTERVAL_UPPER_BOUND = 1.7;
	private final double LOG_CREATION_LOWER = 4;
	private final double LOG_CREATION_UPPER = 6;
	private final double CAR_MOVEMENT_INTERVAL = .1;
	private static final int WORLD_WIDTH = 9;
	public ObjectGenerator(FrogView v, Queue<CreateInstruction> q)	{
		createQueue = q;
		rows = new ArrayList<CreateInstruction>();
		randomGenerator = new Random();
		view = v;
		timer = new Timer();
		populateIndex(view.getUpperView());
		initialGenerate();
		toggle = generateRandomDirection();
	}

	public ArrayList<CreateInstruction> getCreationIndex()	{
		return rows;
	}

	private Timer getTimer()	{
		return timer;
	}

	public void initialGenerate()	{
		for(int i = view.getLowerView(); i < view.getUpperView(); i++)	{
			if(rows.get(i).ready() && rows.get(i).getType() != GameObjectType.GROUND)	{
				rows.get(i).setInterval(Timer.generateInterval(CREATION_INTERVAL_LOWER_BOUND,CREATION_INTERVAL_UPPER_BOUND));
				createQueue.add(rows.get(i));
				swap(rows.get(i),i);
			}
		}
	}

	private void swap(CreateInstruction oldInstruction, int r)	{
		switch(oldInstruction.getType())	{
			case WATER:
				// System.out.println("HERE");
				rows.set(r, new CreateInstruction(GameObjectType.LOG,Timer.generateInterval(LOG_CREATION_LOWER,LOG_CREATION_UPPER),0,Direction.NONE,r,0));
				// System.out.println(rows.get(r).getType());
				break;
			default:
				break;
		}
	}

	public void generate()	{
		for(int i = view.getLowerView(); i < view.getUpperView(); i++)	{
			if(incrementGeneration())	{
				staticGenerate();
			}
			if(rows.get(i).ready() && rows.get(i).getType() != GameObjectType.GROUND && rows.get(i).getType() != GameObjectType.WATER)	{
				switch(rows.get(i).getType())	{
					case CAR:
						rows.get(i).setInterval(Timer.generateInterval(CREATION_INTERVAL_LOWER_BOUND,CREATION_INTERVAL_UPPER_BOUND));
						break;
					case LOG:
						rows.get(i).setInterval(Timer.generateInterval(LOG_CREATION_LOWER,LOG_CREATION_UPPER));
						break;
					default:
						break;
				}
				createQueue.add(rows.get(i));
			}
		}
	}

	private void staticGenerate()	{
		switch(rows.get(getSize() - 1).getType())	{
			case WATER:
				createQueue.add(rows.get(getSize() -1));
				swap(rows.get(getSize() -1),getSize() -1);
				break;
			default:
		}
	}	

	private void paveRow(GameObjectType t, double lowerCreation, double upperCreation, Direction d, int row)	{
		for(int c = 0; c < WORLD_WIDTH; c++)	{
			createQueue.add(new CreateInstruction(t,lowerCreation,upperCreation,d,row,c));
		}
	}

	private Direction generateToggleDirection()	{
		if(toggle == Direction.EAST)	{
			toggle = Direction.WEST;
		} else {
			toggle = Direction.EAST;
		}
		return toggle;
	}

	public Boolean incrementGeneration()	{
		if(getSize() < view.getUpperView())	{
			System.out.println(view.getUpperView());
			rows.add(generateObjectInstruction(view.getUpperView() - 1));
			return true;
		}
		return false;
	}

	private int getSize()	{
		return rows.size();
	}


	private void populateIndex(int height)	{
		for(int i = 0; i < height; i++)	{
			rows.add(generateObjectInstruction(i));
		}
	}

	private CreateInstruction generateObjectInstruction(int r)	{
		int rndm = randomGenerator.nextInt(UPPER_BOUND);
		CreateInstruction c;
		Direction d = generateRandomDirection();
		if(r == 0)	{
			c = new CreateInstruction(GameObjectType.GROUND,0,0,generateRandomDirection(),r,getStartingColumn(d));
		} else if(rndm <= WATER_BOUNDARY)	{
			c = new CreateInstruction(GameObjectType.WATER, 0, 0, generateToggleDirection(), r, getStartingColumn(d));
			// paveRow(GameObjectType.WATER,0,0,d,r);
		} else if(rndm <= CAR_BOUNDARY)	{
			c = new CreateInstruction(GameObjectType.CAR,Timer.generateInterval(CREATION_INTERVAL_LOWER_BOUND,CREATION_INTERVAL_UPPER_BOUND),CAR_MOVEMENT_INTERVAL,d,r,getStartingColumn(d));
			paveRow(GameObjectType.ROAD,0,0,Direction.NORTH,rows.size());
		} else {
			c = new CreateInstruction(GameObjectType.GROUND,0,0,generateRandomDirection(),r,getStartingColumn(d));
		}
		return c;
	}


	public static Integer getStartingColumn(Direction d)	{
		try	{
			switch(d)	{
				case EAST:
					return 0;
				case WEST:
					return WORLD_WIDTH - 1;
				default:
					throw new Exception("Object direction error");
			}
		} catch(Exception e)	{
			WhiteBoxTesting.catchFatalException(e,"Incorrect object direction");
			return null;
		}
	}



	private Direction generateRandomDirection()	{
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
		ObjectGenerator og = new ObjectGenerator(new FrogView(10), new LinkedList<CreateInstruction>());
		t.exitSuite();
		return t;
	}
}

