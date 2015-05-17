import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.*;

public class Cell {

	private int cellRow;
	private int cellCol;
	private ArrayList<GameObject> gameCell;

	public Cell(int r, int c)	{
		gameCell = new ArrayList<GameObject>();
		cellRow = r;
		cellCol = c;
	}

	public void addToCell(GameObject newGameObj)	{
		gameCell.add(newGameObj);
	}

	public int getCellHeight()	{
		return gameCell.size();
	}


	public Boolean objectPresent(GameObjectType go)	{
		for (GameObject g: gameCell)	{
			if(g.getGameObjectType() == go)	{
				return true;
			}
		}
		return false;
	}

	public GameObjectType getGameObjectType(int index)	{
		try {
		return gameCell.get(index).getGameObjectType();
		} catch (NullPointerException e)	{
			return GameObjectType.NONE;
		} catch (IndexOutOfBoundsException e)	{
			return GameObjectType.NONE;
		}
	}

	public int getGameObjectIndex(GameObjectType go)	{
		int i = 0;
		for (GameObject g: gameCell)	{
			if(g.getGameObjectType() == go)	{
				return i;
			}
			i++;
		}
		try	{
			throw new Exception();
		} catch (Exception e)	{
			WhiteBoxTesting.catchFatalException(e,"Object type does not exits in this cell");
			return 0;
		}
	}

	public GameObject getGameObject(int index)	{
		return gameCell.get(index);
	}

	public GameObject removeGameObject(int index)	{
		return gameCell.remove(index);
	}


/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Cell.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("CellUnit Tests");
		Cell c = new Cell(0,0);
		c.addToCell(new Ground());
		t.compare(c.getCellHeight(),"==",1,"Cell has one object");
		c.addToCell(new Car(Direction.EAST));
		t.compare(c.getCellHeight(),"==",2,"Cell has two objects");
		t.exitSuite();
		return t;
	}

}

