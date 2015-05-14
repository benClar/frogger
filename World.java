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

	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
	private final int FROG_INIT_ROW = 0;
	private final int FROG_INIT_COLUMN = WORLD_WIDTH/2;
	private final int CELL_WIDTH = 30;
	private final int CELL_HEIGHT = 30;


	public World(ConcurrentLinkedQueue<Direction> queue, Queue<CreateInstruction> cQueue)	{
		board = new Board(WORLD_HEIGHT,WORLD_WIDTH);
		view = new FrogView(WORLD_HEIGHT);
		window = new Window(queue,view);
		// System.out.println("COL " +FROG_INIT_COLUMN );

		frogger = new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN);
		createQueue = cQueue;
		moveCalculator = new Movement(0,0);
		addToWorld(frogger.getRow(), frogger.getCol(), frogger);
		objectGen = new ObjectGenerator(view,cQueue);
	}

	public void tick()	{
            checkForInstructions();
            objectGen.generate();
            checkFrog();
            moveWorld();	
	}

	public void checkForInstructions()	{
    	try {
    		interpretCreateInstruction(createQueue.remove()); 
    	} catch (Exception err) { 
        	
        }
	}

	private void interpretCreateInstruction(CreateInstruction i)	{
		try	{
			switch(i.getType())	{
				case CAR:
					addToWorld(i.getRow(),i.getStartingColumn(),new Car(i.getDirection(),i.getMovementInterval()));
					break;
				case ROAD:
					// System.out.println(i.getRow());
					addToWorld(i.getRow(),i.getStartingColumn(),new Road());
					break;
				case WATER:
					double moveInterval = Timer.generateInterval(Water.getLowerBound(),Water.getUpperBound());
					for(int col = 0 ; col < board.getWidth(); col++)	{
						addToWorld(i.getRow(),col,new Water(i.getDirection(),moveInterval));
					}
					break;
				case LOG:
					int waterIndex = board.getCell(i.getRow(),0).getGameObjectIndex(GameObjectType.WATER);
					int logCol = ObjectGenerator.getStartingColumn(board.getCell(i.getRow(),0).getGameObject(waterIndex).getDirection());
					addToWorld(i.getRow(),logCol,new Log(board.getCell(i.getRow(),0).getGameObject(waterIndex).getMoveInterval()));
					break;
				default:
					throw new IllegalArgumentException();
			}
		}catch (IllegalArgumentException e)	{
			WhiteBoxTesting.catchFatalException(e,"Wrong object in queue " + i.getType());
		}
	}

	private void paveWithSameObject(int row, GameObject g)	{

	}

	public void moveWorld()	{
		int lower = view.getLowerView();
		int upper =  view.getUpperView();
		for(int row = lower; row < upper; row++)	{
			for(int col = 0; col < board.getWidth(); col++)	{
				// for(int z = 0; z < board.getCell(row,col).getCellHeight(); z++)	{
					moveCell(row,col);
				// }
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

			}

			// System.out.println(row + " " + col + " " + current.getGameObjectType() );
			current.move(board.getCell(row,col).getGameObject(z).getDirection(),moveCalculator);
			moveObjectPosition(row,col,z,moveCalculator);
			if(current.getGameObjectType() == GameObjectType.FROG)	{
				// System.out.println(row + " " + col + " " + board.getCell(row,col).getGameObject(z - 1).getGameObjectType() + " " + moveCalculator.getRowMovement() + " " + moveCalculator.getColumnMovement());
			}
			if(height > board.getCell(row,col).getCellHeight())	{
				--height;
				--z;
			}
			// System.out.println(current.inheritable());
			if(current.inheritable())	{
				if(above != null)	{
					if(current.ready())	{
						above.makeReady();
						if(above.justBeenMoved() == false)	{
							if(above.getGameObjectType() == GameObjectType.FROG)	{
								// System.out.println(current.getGameObjectType() + "==>" + above.getGameObjectType());
							}
							inheritAttributes(current, above);
						}
					} else	{
						// System.out.println(current.getGameObjectType() + " IS NOT READY");
					}
				} else {
					// System.out.println("NO MORE OBJECTS ABOVE! "  + current.getGameObjectType());
				}
			} else	{
				// System.out.println("NOT READY " + current.getGameObjectType()) ;
			}
			current.removeInheritanceSpeed();
			current.setInheritanceStatus(false);
		}	
	}

	private void moveCell(int row, int col, int z)	{

		try	{
			System.out.println("HERE_1 " + board.getCell(row,col).getGameObject(z).getGameObjectType());
			// if(board.getCell(row,col).getGameObject(z - 1).inheritable())	{
			// 	inheritAttributes(board.getCell(row,col).getGameObject(z - 1), board.getCell(row,col).getGameObject(z));
			// }

			if(board.getCell(row,col).getGameObject(z).inheritable())	{
				// System.out.println(board.getCell(row,col).getGameObject(z + 1).getGameObjectType());
				if(board.getCell(row,col).getGameObject(z).ready())	{
					if(board.getCell(row,col).getGameObject(z + 1).justBeenMoved() == false){
						inheritAttributes(board.getCell(row,col).getGameObject(z), board.getCell(row,col).getGameObject(z + 1));
						System.out.println("HERE_2 " + board.getCell(row,col).getGameObject(z).getGameObjectType());
						board.getCell(row,col).getGameObject(z).move(board.getCell(row,col).getGameObject(z).getDirection(),moveCalculator);
						board.getCell(row,col).getGameObject(z).removeInheritanceSpeed();
						board.getCell(row,col).getGameObject(z).setInheritanceStatus(false);
						System.out.println("HERE_3 " + board.getCell(row,col).getGameObject(z).getGameObjectType() + " " + board.getCell(row,col).getGameObject(z).getDirection() +  " " + moveCalculator.getRowMovement() + " " + moveCalculator.getColumnMovement());
						moveObjectPosition(row,col,z,moveCalculator);

						if(board.getCell(row,col).getGameObject(z).getGameObjectType() == GameObjectType.WATER)	{
							moveCell(row,col,z + 1);
						} else {
							moveCell(row,col,z);
						}
					}
				} else {
					// System.out.println("NOT READY TO MOVE ABOVE");
				}
			} else	{
				System.out.println("HERE_4 " + board.getCell(row,col).getGameObject(z).getGameObjectType());
				board.getCell(row,col).getGameObject(z).move(board.getCell(row,col).getGameObject(z).getDirection(),moveCalculator);
				System.out.println(board.getCell(row,col).getGameObject(z).getGameObjectType() + " " + board.getCell(row,col).getGameObject(z).getDirection() +  " " + moveCalculator.getRowMovement() + " " + moveCalculator.getColumnMovement() + " " +  " MOVE");
				board.getCell(row,col).getGameObject(z).removeInheritanceSpeed();
				board.getCell(row,col).getGameObject(z).setInheritanceStatus(false);
				moveObjectPosition(row,col,z,moveCalculator);
			}
			

			
		} catch (IndexOutOfBoundsException e)	{
			System.out.println(row + " " + col);
			System.out.println("NOTHING ABOVE");
			return;	
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
					moveObjectPosition(frogRow,frogCol,getFrogZAxis(frogRow,frogCol),moveCalculator);
				}
			} else {
				frogger.setRow((frogger.getRow() - moveCalculator.getRowMovement()));
				frogger.setCol((frogger.getCol() - moveCalculator.getColumnMovement()));
			}
		}
	}

	public Boolean checkConfines(int row, int col)	{
		if((row >= 0 && row < view.getUpperView()) && (col >= 0 && col < board.getWidth()))	{
			return true;
		}
		return false;
	}

	public boolean checkFrogDeath(Cell c)	{
		if(checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.CAR))	{
			return true;
		} else if (c.getGameObjectType(c.getCellHeight() - 1) == GameObjectType.WATER) {
			return true;
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
			// board.removeTop();
		}
	}

	public int getFrogView()	{
		return frogger.getRow() + WORLD_HEIGHT;
	}

	public void checkFrog()	{
		try	{
			if(((checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.FROG))) && (checkForObject(frogger.getRow(),frogger.getCol(),GameObjectType.CAR)))	{
			 	killFrog(frogger.getRow(),frogger.getCol());	
		 	}
		} catch (IndexOutOfBoundsException e){
			spawnFrog();
		}
	}

	private void killFrog(int frogRow, int frogCol)	{
		removeGameObject(frogRow,frogCol,getFrogZAxis(frogRow,frogCol));
		addToWorld(frogRow,frogCol, new BloodSplatter());
		spawnFrog();
	}

	private void spawnFrog()	{
		frogger = new Frog(FROG_INIT_ROW, FROG_INIT_COLUMN);
		addToWorld(frogger.getRow(),frogger.getCol(),frogger);	
		view.resetView();		
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
		ConcurrentLinkedQueue<Direction> worldQueue = new ConcurrentLinkedQueue<Direction>();
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
		ConcurrentLinkedQueue<Direction> worldQueue = new ConcurrentLinkedQueue<Direction>();
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
		// t.compare(w.frogger.inheriting(),"==",true,"Frog inheriting");
		t.compare(w.board.getCell(1,4).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog In correct position");
		// w.moveWorld();
		w.moveCell(1,4);
		w.moveCell(1,5);
		t.compare(w.board.getCell(1,5).getGameObject(2).getGameObjectType(),"==",GameObjectType.LOG,"Log Carried by water to the east one");
		t.compare(w.board.getCell(1,5).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog Carried by water to the east one");
		try{
		 	Thread.sleep(2000);
		} catch (InterruptedException e)	{

		}
		w.moveCell(1,5);
		// w.moveWorld();
		t.compare(w.board.getCell(1,6).getGameObject(2).getGameObjectType(),"==",GameObjectType.LOG,"Log Carried by water to the east one");
		t.compare(w.board.getCell(1,6).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog Carried by water to the east one");
		// System.out.println(w.frogger.getSpeed() + " " + w.frogger.getDirection());
		// System.out.println("CELL HEIGHT "+ w.board.getCell(1,4).getCellHeight());
		// w.moveCell(1,4,1);
		// System.out.println(w.frogger.getSpeed() + " " + w.frogger.getDirection());
		// System.out.println(" FROG " + w.frogger.getRow() + " " + w.frogger.getCol());
		// System.out.println(w.board.getCell(1,5).getGameObject(1).getGameObjectType());
		// w.moveCell(1,4,1);
		// t.compare(w.board.getCell(1,5).getGameObject(1).ready(),"==",false,"Water not ready to move");
		// w.moveCell(1,5,1);
		// w.moveWorld();
		// System.out.println(w.frogger.getRow() + " " + w.frogger.getCol());
		// t.compare(w.board.getCell(1,5).getGameObject(3).getGameObjectType(),"==",GameObjectType.FROG,"Frog Hasn't moved");

		// t.compare(w.board.getCell(1,5).getGameObject(1).ready(),"==",true,"Water now ready to move");
		// w.moveWorld();
		// w.moveCell(1,5,1);

		// System.out.println(w.frogger.getSpeed() + " " + w.frogger.getDirection());

		// w.moveWorld();
		// w.moveCell(1,5,1);
		// t.compare(w.board.getCell(1,6).getGameObject(1).getGameObjectType(),"==",GameObjectType.FROG,"Frog Carried by water to the east one");
		// System.out.println(w.board.getCell(1,6).getGameObject(w.board.getCell(1,6).getCellHeight() -1).getGameObjectType());
		// w.moveWorld();
		// System.out.println(w.board.getCell(1,6).getGameObject(w.board.getCell(1,6).getCellHeight() -1).getGameObjectType());
		// // w.frogger.inheriting
		return t;
	}

	public static Testing unitTest_MovingObjects(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("World Unit Tests: Moving Objects");
		ConcurrentLinkedQueue<Direction> worldQueue = new ConcurrentLinkedQueue<Direction>();
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

