import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.concurrent.*;
import java.util.*;

public class World  {

	private Cell[][] board;
	private Frog frogger;
	private int cellWidth;
	private int cellHeight;
	private Window window;
	private Movement moveCalculator;
	private Queue<CreateInstruction> createQueue;

	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
	private final int FROG_INIT_ROW = WORLD_HEIGHT - 1;
	private final int FROG_INIT_COLUMN = WORLD_WIDTH/2;
	private final int CELL_WIDTH = 30;
	private final int CELL_HEIGHT = 30;


	public World(Frog newFrog, int rows, int cols, int cellW, int cellH, ConcurrentLinkedQueue<Direction> queue, Queue<CreateInstruction> cQueue)	{
		board = new Cell[rows][cols];
		window = new Window(queue);
		createCells();
		frogger = newFrog;
		createQueue = cQueue;
		// cellWidth = cellW;
		// cellHeight = cellH;
		moveCalculator = new Movement(0,0);
		addToWorld(frogger.getRow(), frogger.getCol(), frogger);
		// addToWorld(7, 7, new Frog(50,50));
	}


	public void checkForInstructions()	{
    	try {
    		interpretCreateInstruction(createQueue.remove()); 
    	} catch (Exception err) { 
        	
        }
	}

	private void interpretCreateInstruction(CreateInstruction i)	{
		int col = getStartingColumn(i.getDirection());
		// System.out.println(i.getDirection() +" "+  i.getType());
		try	{
			switch(i.getType())	{
				case CAR:
					addToWorld(i.getRow(),col,new Car(i.getDirection(),i.getMovementInterval()));
					break;
				default:
					throw new IllegalArgumentException();
			}
		}catch (IllegalArgumentException e)	{
			WhiteBoxTesting.catchFatalException(e,"Wrong object in queue " + i.getType());
		}
	}

	private Integer getStartingColumn(Direction d)	{
		try	{
			switch(d)	{
				case EAST:
					return 0;
				case WEST:
					return board[0].length - 1;
				default:
					throw new Exception("Object direction error");
			}
		} catch(Exception e)	{
			WhiteBoxTesting.catchFatalException(e,"Incorrect object direction");
			return null;
		}
	}

	private void createCells()	{
		for(int row = 0; row < board.length; row ++)	{
			for(int col = 0; col < board[row].length; col++)	{
				board[row][col] = new Cell(row, col);
				addToWorld(row,col,new Ground());
			}
		}
	}



	public void moveWorld()	{
		for(int row = 0; row < board.length; row++)	{
			for(int col = 0; col < board[0].length; col++)	{
				for(int z = 0; z < board[row][col].getCellHeight(); z++)	{
					board[row][col].getGameObject(z).move(board[row][col].getGameObject(z).getDirection(),moveCalculator);
					// System.out.println(row + " " + col + " " + z + " " + board[row][col].getGameObject(z).getGameObjectType());
					moveObjectPosition(row,col,z,moveCalculator);
				}
			}
		}
	}

	public Window getWindow()	{
		return window;
	}

	public void addToWorld(int row, int col, GameObject newGameObj)	{
		// System.out.println("ADDING " + newGameObj.getGameObjectType() + " ROW" + row + "col" + col);
		board[row][col].addToCell(newGameObj);
		window.update(board);
	}

	public void moveFrog(Direction m)	{
		Movement frogMovement;
		int frogCol, frogRow;
		if(m != Direction.NONE)	{
			frogCol = frogger.getCol();
			frogRow = frogger.getRow();
			frogger.playerMove(m,moveCalculator);
			if(checkConfines(frogger.getRow(),frogger.getCol()))	{
				if(checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.CAR))	{
					killFrog(frogRow,frogCol);
				} else	{
					moveObjectPosition(frogRow,frogCol,getFrogZAxis(frogRow,frogCol),moveCalculator);
				}
			} else {
				frogger.setRow((frogger.getRow() - moveCalculator.getRowMovement()));
				frogger.setCol((frogger.getCol() - moveCalculator.getColumnMovement()));
			}
		}
	}

	public void checkFrog()	{
		if(((checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.FROG))) && (checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.CAR)))	{
		 	killFrog(frogger.getRow(),frogger.getCol());	
	 	}
	}

	private void killFrog(int frogRow, int frogCol)	{
		removeGameObject(frogRow,frogCol,getFrogZAxis(frogRow,frogCol));
		addToWorld(frogRow,frogCol, new BloodSplatter());
		frogger = new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN);
		addToWorld(frogger.getRow(),frogger.getCol(),frogger);	
	}



	private Boolean checkForObject(int row,int col,GameObjectType g)	{
		if(board[row][col].objectPresent(g))	{
			return true;
		}
		return false;
	}

	private Boolean checkConfines(int row, int col)	{
		if((row >= 0 && row < board.length) && (col >= 0 && col < board[0].length))	{
			return true;
		}
		return false;
	}

	public void moveFrog(int x, int y)	{
		System.out.println(x);
		System.out.println(y);
	}

	public Frog getFrog()	{
		return frogger;
	}

	public int getFrogZAxis(int row, int col)	{
		for(int i = 0 ; i < board[row][col].getCellHeight(); i++)	{
			if(board[row][col].getGameObjectType(i) == GameObjectType.FROG)	{
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
		return board[row][col].removeGameObject(zAxis);
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
		t.exitSuite();
		return t;
	}

	public static Testing unitTest_worldMechanisms(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests: Testing World mechanisms");
		ConcurrentLinkedQueue<Direction> worldQueue = new ConcurrentLinkedQueue<Direction>();
		Creator c = new Creator();
		World w = c.createWorld(worldQueue,new LinkedList<CreateInstruction>());	
		t.compare(w.checkConfines(-1,0),"==",false,"out of bounds");
		t.compare(w.checkConfines(0,-1),"==",false,"out of bounds");
		t.compare(w.checkConfines(10,0),"==",false,"out of bounds");
		t.compare(w.checkConfines(4,10),"==",false,"out of bounds");
		t.compare(w.checkConfines(8,0),"==",false,"out of bounds");
		t.compare(w.checkConfines(0,9),"==",false,"out of bounds");
		t.compare(w.checkConfines(0,0),"==",true,"in bounds");
		t.compare(w.checkConfines(7,8),"==",true,"in bounds");
		return t;
	}

	public static Testing unitTest_MovingObjects(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests: Moving Objects");
		ConcurrentLinkedQueue<Direction> worldQueue = new ConcurrentLinkedQueue<Direction>();
		Creator c = new Creator();
		World w = c.createWorld(worldQueue,new LinkedList<CreateInstruction>());
		t.compare(w.getFrogZAxis(w.getFrog().getRow(),w.getFrog().getCol()),"==",1,"Frog's zaxis is 1");
		t.compare(w.board[7][4].getGameObjectType(1),"==",GameObjectType.FROG,"Frog is at correct place");
		w.moveFrog(Direction.NORTH);
		t.compare(w.frogger.getRow(),"==",6,"frog moved one cell forward");
		t.compare(w.frogger.getCol(),"==",4,"No Horizontal movement");
		t.compare(w.board[6][4].getGameObjectType(1),"==",GameObjectType.FROG,"Frog found");
		w.moveFrog(Direction.WEST);
		t.compare(w.frogger.getRow(),"==",6,"No Vertical Movement");
		t.compare(w.frogger.getCol(),"==",3,"Frog moved to the left");
		w.moveFrog(Direction.EAST);
		t.compare(w.frogger.getRow(),"==",6,"No Vertical Movement");
		t.compare(w.frogger.getCol(),"==",4,"Frog moved to the right");
		w.moveFrog(Direction.SOUTH);
		t.compare(w.frogger.getRow(),"==",7,"Frog moved back a cell");
		t.compare(w.frogger.getCol(),"==",4,"No Horizontal movement");
		w.addToWorld(1,0,new Car(Direction.EAST));
		w.moveWorld();
		t.compare(w.frogger.getRow(),"==",7,"Frog Hasn't Moved");
		t.compare(w.frogger.getCol(),"==",4,"Frog Hasn't Moved");
		t.compare(w.board[1][1].getGameObject(1).getGameObjectType(),"==",GameObjectType.CAR,"Car moved right by one");


		return t;
	}
}

