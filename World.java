import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.concurrent.*;
import java.util.*;

public class World  {

	private Board board;
	private Frog frogger;
	private int cellWidth;
	private int cellHeight;
	private Window window;
	private Movement moveCalculator;
	private Queue<CreateInstruction> createQueue;
	private ObjectGenerator objectGen;
	private FrogView view;
	private Timer resetTimer;
	private Sound sound;
	private int furthestRow;
	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
	private final int FROG_INIT_ROW = 0;
	private final int FROG_INIT_COLUMN = WORLD_WIDTH/2;
	private final int CELL_WIDTH = 30;
	private final int CELL_HEIGHT = 30;


	public World(ConcurrentLinkedQueue<UserCommand> queue, Queue<CreateInstruction> cQueue)	{
		board = new Board(WORLD_HEIGHT,WORLD_WIDTH);
		view = new FrogView(WORLD_HEIGHT);
		window = new Window(queue,view);
		resetTimer = new Timer(1.0);

		frogger = new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN);
		createQueue = cQueue;
		moveCalculator = new Movement(0,0);
		addToWorld(frogger.getRow(), frogger.getCol(), frogger);
		objectGen = new ObjectGenerator(view,cQueue);
		sound = new Sound();
		furthestRow = 0;
	}

	public void tick()	{
            checkForInstructions();
            objectGen.generate();
            checkFrog();
            moveWorld();	
	}

	private void resetWorld()	{
		board = new Board(WORLD_HEIGHT,WORLD_WIDTH);
		view = new FrogView(WORLD_HEIGHT);
		frogger = new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN);
		window.setView(view);
		addToWorld(frogger.getRow(), frogger.getCol(), frogger);
		objectGen = new ObjectGenerator(view,createQueue);
	}

	public void interpret(UserCommand cmd)	{
		switch(cmd)	{
			case MOVE_NORTH:
				moveFrog(Direction.NORTH);
				break;
			case MOVE_SOUTH:
				moveFrog(Direction.SOUTH);
				break;
			case MOVE_EAST:
				moveFrog(Direction.EAST);
				break;
			case MOVE_WEST:
				moveFrog(Direction.WEST);
				break;
			case RESET_WORLD:
				if(resetTimer.ready())	{
					resetWorld();
				}
				break;
			default:
				break;
		}
	}

	public void checkForInstructions()	{
    	try {
    		interpretCreateInstruction(createQueue.remove()); 
    	} catch (Exception err) { 
        	
        }
	}

	private void interpretCreateInstruction(CreateInstruction i)	{
		try	{
			int waterIndex;
			switch(i.getType())	{
				case CAR:
					addToWorld(i.getRow(),i.getStartingColumn(),new Car(i.getDirection(),i.getMovementInterval()));
					break;
				case ROAD:
					addToWorld(i.getRow(),i.getStartingColumn(),new Road());
					break;
				case WATER:
					double moveInterval = Timer.generateInterval(Water.getLowerBound(),Water.getUpperBound());
					for(int col = 0 ; col < board.getWidth(); col++)	{
						addToWorld(i.getRow(),col,new Water(i.getDirection(),moveInterval));
					}
					break;
				case LOG:
					waterIndex = board.getCell(i.getRow(),0).getGameObjectIndex(GameObjectType.WATER);
					int logCol = ObjectGenerator.getStartingColumn(board.getCell(i.getRow(),0).getGameObject(waterIndex).getDirection());
					addToWorld(i.getRow(),logCol,new Log(board.getCell(i.getRow(),0).getGameObject(waterIndex).getMoveInterval()));
					break;
				case COIN:
					if(board.getCell(i.getRow(),i.getStartingColumn()).objectPresent(GameObjectType.WATER))	{
						waterIndex = board.getCell(i.getRow(),i.getStartingColumn()).getGameObjectIndex(GameObjectType.WATER);
						addToWorld(i.getRow(),i.getStartingColumn(),new Coin(50,board.getCell(i.getRow(),i.getStartingColumn()).getGameObject(waterIndex).getMoveInterval()));
					}else {
						addToWorld(i.getRow(),i.getStartingColumn(),new Coin(10));
					}
					break;
				case HEART:
					if(board.getCell(i.getRow(),i.getStartingColumn()).objectPresent(GameObjectType.WATER))	{
						waterIndex = board.getCell(i.getRow(),i.getStartingColumn()).getGameObjectIndex(GameObjectType.WATER);
						addToWorld(i.getRow(),i.getStartingColumn(),new Heart(board.getCell(i.getRow(),i.getStartingColumn()).getGameObject(waterIndex).getMoveInterval()));
					} else {
						addToWorld(i.getRow(),i.getStartingColumn(),new Heart());
					}
					break;
				default:
					throw new IllegalArgumentException();
			}
		}catch (IllegalArgumentException e)	{
			WhiteBoxTesting.catchFatalException(e,"Wrong object in queue " + i.getType());
		}
	}

	public void moveWorld()	{
		int lower = view.getLowerView();
		int upper =  view.getUpperView();
		for(int row = lower; row < upper; row++)	{
			for(int col = 0; col < board.getWidth(); col++)	{
				moveCell(row,col);
			}
		}
	}

	private void moveCell(int row, int col)	{
		int height = board.getCell(row,col).getCellHeight();
		for(int z = 0; z < height; z++)	{
			GameObject current = board.getCell(row,col).getGameObject(z);
			GameObject above = null;
			try	{
				above = board.getCell(row,col).getGameObject(z + 1);
			} catch (IndexOutOfBoundsException e)	{
				above = null;
			}
			current.move(board.getCell(row,col).getGameObject(z).getDirection(),moveCalculator);
			moveObjectPosition(row,col,z,moveCalculator);
			if(height > board.getCell(row,col).getCellHeight())	{
				--height;
				--z;
			}
			if(current.inheritable() && above != null && current.ready() && above.justBeenMoved() == false)	{
				above.makeReady();
				inheritAttributes(current, above);
			}
			current.removeInheritanceSpeed();
			current.setInheritanceStatus(false);
		}	
	}

	public Window getWindow()	{
		return window;
	}

	public void addToWorld(int row, int col, GameObject newGameObj)	{
		board.addToWorld(row,col,newGameObj);
		window.update(board);
	}



	public void inheritAttributes(GameObject parent, GameObject child)	{
			child.addToSpeed(parent.inheritSpeed());
			child.setDirection(parent.getDirection());
			child.setInheritanceStatus(true);
	}

	public void moveFrog(Direction m)	{
		Movement frogMovement;
		int frogCol, frogRow;
		if(m != Direction.NONE)	{
			frogCol = frogger.getCol();
			frogRow = frogger.getRow();
			frogger.playerMove(m,moveCalculator);
			if(checkConfines(frogger.getRow(),frogger.getCol()))	{
				expandFrogView(m);
				if(checkFrogDeath(board.getCell(frogger.getRow(),frogger.getCol())))	{
					killFrog(frogRow,frogCol);
				} else	{
					checkForItems(frogRow,frogCol);
					moveObjectPosition(frogRow,frogCol,getFrogZAxis(frogRow,frogCol),moveCalculator);
					if(m == Direction.NORTH)	{
						if(furthestRow < frogRow) {
							window.update("score",1);
							furthestRow++;
						}
					}
				}
			} else {
				frogger.setRow((frogger.getRow() - moveCalculator.getRowMovement()));
				frogger.setCol((frogger.getCol() - moveCalculator.getColumnMovement()));
			}
		}
	}

	public void checkForItems(int row, int col)	{
		if(board.getCell(row,col).objectPresent(GameObjectType.COIN))	{
			int coinIndex = board.getCell(row,col).getGameObjectIndex(GameObjectType.COIN);
			Coin c = (Coin) board.getCell(row,col).removeGameObject(coinIndex);
			window.update("score", c.getPoints());
			sound.playClip("coin");
		}else if(board.getCell(row,col).objectPresent(GameObjectType.HEART))	{
			int heartIndex = board.getCell(row,col).getGameObjectIndex(GameObjectType.HEART);
			board.getCell(row,col).removeGameObject(heartIndex);
			frogger.setInvincibility(true);
			sound.playClip("heart");
			window.setIcon("heart",true);
		}
	}

	public Boolean checkConfines(int row, int col)	{
		if((row >= 0 && row < view.getUpperView()) && (col >= 0 && col < board.getWidth()))	{
			return true;
		}
		return false;
	}

	public boolean checkFrogDeath(Cell c)	{
		try{
			if(checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.CAR))	{
				return true;
			} else if (c.getGameObjectType(c.getCellHeight() - 1) == GameObjectType.WATER) {
				return true;
			} else if(c.getGameObjectType(c.getCellHeight() - 2) == GameObjectType.WATER &&  c.getGameObjectType(c.getCellHeight() - 1) != GameObjectType.LOG)	{
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e)	{
			return false;
		}

		return false;
	}

	public void expandFrogView(Direction d)	{
		if(d == Direction.NORTH)	{
			board.addRow();
			for(int i = 0; i < board.getWidth(); i++)	{
				addToWorld(board.getHeight() - 1,i,new Ground());
			}
			view.increaseView();
		} else if(d == Direction.SOUTH)	{
			view.decreaseView();
		}
	}

	public int getFrogView()	{
		return frogger.getRow() + WORLD_HEIGHT;
	}

	public void checkFrog()	{
		try	{
			checkForItems(frogger.getRow(),frogger.getCol());
			if(((checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.FROG))) && (checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.CAR)))	{
			 	killFrog(frogger.getRow(),frogger.getCol());	
		 	}
		} catch (IndexOutOfBoundsException e){
			adjustFrogPosition();
			killFrog(frogger.getRow(),frogger.getCol());
		}
	}

	private int adjustFrogPosition()	{
		int correctCol;
		if(frogger.getCol() >= WORLD_WIDTH)	{
			correctCol = WORLD_WIDTH -1;
		} else {
			correctCol = 0;
		}

		frogger.setCol(correctCol);
		addToWorld(frogger.getRow(),correctCol,frogger);
		
		return correctCol;
	}

	private void killFrog(int frogRow, int frogCol)	{
		if(frogger.getInvincibility())	{
			moveToSafety(frogRow,frogCol);
			frogger.setInvincibility(false);
			window.setIcon("heart",false);
		} else {
			furthestRow = 0;
			sound.playClip("death");
			removeGameObject(frogRow,frogCol,getFrogZAxis(frogRow,frogCol));
			if(!checkForObject(frogRow,frogCol,GameObjectType.BLOOD))	{
				addToWorld(frogRow,frogCol, new BloodSplatter());
			}
			spawnFrog();
			window.update("death",1);
		}
	}

	private void moveToSafety(int frogRow, int frogCol)	{
		int targetRow = board.findNearestType(GameObjectType.GROUND, frogRow, frogCol);
		moveCalculator.getMove(Direction.SOUTH,frogRow - targetRow);
		moveObjectPosition(frogRow, frogCol,board.getCell(frogRow,frogCol).getGameObjectIndex(GameObjectType.FROG),moveCalculator);
		for(int i = frogRow; i > targetRow; i--)	{
			expandFrogView(Direction.SOUTH);
		}
		frogger.setRow(targetRow);
		frogger.setInvincibility(false);
	}

	private void spawnFrog()	{
		frogger = new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN);
		addToWorld(frogger.getRow(),frogger.getCol(),frogger);	
		view.resetView();	
		window.setScore(0);	
	}



	private Boolean checkForObject(int row,int col,GameObjectType g)	{
		if(board.getCell(row,col).objectPresent(g))	{
			return true;
		}
		return false;
	}

	public Frog getFrog()	{
		return frogger;
	}

	public int getFrogZAxis(int row, int col)	{
		for(int i = 0 ; i < board.getCell(row,col).getCellHeight(); i++)	{
			if(board.getCell(row,col).getGameObjectType(i) == GameObjectType.FROG)	{
				return i;
			}
		}
		try	{
			throw new Exception("Frog not Found");
		} catch (Exception e)	{
			WhiteBoxTesting.catchFatalException(e,"Lost Frog");
			return -1;
		}
	}

	public GameObject removeGameObject(int row, int col, int zAxis)	{
		return board.getCell(row,col).removeGameObject(zAxis);
	}
	
	public void moveObjectPosition(int oldRow, int oldCol, int zAxis, Movement newMove)	{

		int targetRow = oldRow + newMove.getRowMovement();
		int targetCol = oldCol + newMove.getColumnMovement();

		if(newMove.getRowMovement() == 0 && newMove.getColumnMovement() == 0)	{
			return;
		}

		GameObject o = removeGameObject(oldRow,oldCol,zAxis);

		if(checkConfines(targetRow,targetCol))	{
			addToWorld((targetRow),(targetCol),o);
		} else {
			o = null;
			window.update(board);
		}
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			World.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests");
		unitTest_MovingObjects(t);
		unitTest_worldMechanisms(t);
		unitTest_inheriting(t);
		t.exitSuite();
		return t;
	}

	public static Testing unitTest_worldMechanisms(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests: Testing World mechanisms");
		ConcurrentLinkedQueue<UserCommand> worldQueue = new ConcurrentLinkedQueue<UserCommand>();
		World w = new World(worldQueue,new LinkedList<CreateInstruction>());	
		t.compare(w.checkConfines(-1,0),"==",false,"out of bounds");
		t.compare(w.checkConfines(0,-1),"==",false,"out of bounds");
		t.compare(w.checkConfines(10,0),"==",false,"out of bounds");
		t.compare(w.checkConfines(4,10),"==",false,"out of bounds");
		t.compare(w.checkConfines(8,0),"==",false,"out of bounds");
		t.compare(w.checkConfines(0,9),"==",false,"out of bounds");
		t.compare(w.checkConfines(0,0),"==",true,"in bounds");
		t.compare(w.checkConfines(7,8),"==",true,"in bounds");
		t.compare(w.board.getCell(0,0).getCellHeight(),"==",1,"cell height one");
		return t;
	}

	public static Testing unitTest_inheriting(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests: Objects inheriting");
		ConcurrentLinkedQueue<UserCommand> worldQueue = new ConcurrentLinkedQueue<UserCommand>();
		World w = new World(worldQueue,new LinkedList<CreateInstruction>());
		double moveInterval = Timer.generateInterval(Water.getLowerBound(),Water.getUpperBound());
		w.addToWorld(1,4,new Water(Direction.EAST,moveInterval));
		w.addToWorld(1,4,new Log(moveInterval));
		w.addToWorld(1,5,new Water(Direction.EAST,moveInterval));
		w.addToWorld(1,6,new Water(Direction.EAST,moveInterval));
		w.addToWorld(1,7,new Water(Direction.EAST,moveInterval));
		t.compare(w.board.getCell(1,4).getGameObject(1).inheritable(),"==",true,"Water is inhertable");
		t.compare(w.frogger.inheriting(),"==",false,"Frog not inheriting");
		w.moveFrog(Direction.NORTH);
		t.compare(w.board.getCell(1,4).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog In correct position");
		w.moveCell(1,4);
		w.moveCell(1,5);
		t.compare(w.board.getCell(1,5).getGameObject(2).getGameObjectType(),"==",GameObjectType.LOG,"Log Carried by water to the east one");
		t.compare(w.board.getCell(1,5).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog Carried by water to the east one");
		try{
		 	Thread.sleep(2000);
		} catch (InterruptedException e)	{

		}
		w.moveCell(1,5);
		t.compare(w.board.getCell(1,6).getGameObject(2).getGameObjectType(),"==",GameObjectType.LOG,"Log Carried by water to the east one");
		t.compare(w.board.getCell(1,6).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog Carried by water to the east one");
		w.moveToSafety(w.frogger.getRow(),w.frogger.getCol());
		t.compare(w.board.getCell(0,6).getGameObject(1).getGameObjectType(),"==",GameObjectType.FROG,"Frog moved back to safety");
		w.addToWorld(2,6,new Water(Direction.NORTH,moveInterval));
		w.addToWorld(2,6,new Log(moveInterval));
		w.addToWorld(3,6,new Water(Direction.NORTH,moveInterval));
		w.addToWorld(3,6,new Log(moveInterval));
		w.addToWorld(4,6,new Water(Direction.NORTH,moveInterval));
		w.addToWorld(4,6,new Log(moveInterval));
		w.moveFrog(Direction.NORTH);
		w.moveFrog(Direction.NORTH);
		w.moveFrog(Direction.NORTH);
		w.moveFrog(Direction.NORTH);
		w.moveToSafety(w.frogger.getRow(),w.frogger.getCol());
		t.compare(w.board.getCell(0,6).getGameObject(1).getGameObjectType(),"==",GameObjectType.FROG,"Frog moved back to safety");
		w.moveFrog(Direction.NORTH);
		t.compare(w.board.getCell(1,6).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog Can be moved");
		return t;
	}

	public static Testing unitTest_MovingObjects(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests: Moving Objects");
		ConcurrentLinkedQueue<UserCommand> worldQueue = new ConcurrentLinkedQueue<UserCommand>();
		World w = new World(worldQueue,new LinkedList<CreateInstruction>());
		t.compare(w.getFrogZAxis(w.getFrog().getRow(),w.getFrog().getCol()),"==",1,"Frog's zaxis is 1");
		t.compare(w.board.getCell(0,4).getGameObjectType(1),"==",GameObjectType.FROG,"Frog is at correct place");
		w.moveFrog(Direction.NORTH);
		t.compare(w.frogger.getRow(),"==",1,"frog moved one cell forward");
		t.compare(w.frogger.getCol(),"==",4,"No Horizontal movement");
		t.compare(w.board.getCell(1,4).getGameObjectType(1),"==",GameObjectType.FROG,"Frog found");
		w.moveFrog(Direction.WEST);
		t.compare(w.frogger.getRow(),"==",1,"No Vertical Movement");
		t.compare(w.frogger.getCol(),"==",3,"Frog moved to the left");
		w.moveFrog(Direction.EAST);
		t.compare(w.frogger.getRow(),"==",1,"No Vertical Movement");
		t.compare(w.frogger.getCol(),"==",4,"Frog moved to the right");
		w.moveFrog(Direction.SOUTH);
		t.compare(w.frogger.getRow(),"==",0,"Frog moved back a cell");
		t.compare(w.frogger.getCol(),"==",4,"No Horizontal movement");
		w.addToWorld(1,0,new Car(Direction.EAST));
		w.moveWorld();
		t.compare(w.frogger.getRow(),"==",0,"Frog Hasn't Moved");
		t.compare(w.frogger.getCol(),"==",4,"Frog Hasn't Moved");
		t.compare(w.board.getCell(1,1).getGameObject(1).getGameObjectType(),"==",GameObjectType.CAR,"Car moved right by one");


		return t;
	}
}

