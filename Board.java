import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.util.*;


public class Board  {

	private ArrayList<Cell[]> board;
	private int rowSize;
	private int colSize;

	public Board(int r,int c)	{
		board = new ArrayList<Cell[]>();
		rowSize = r;
		colSize = c;
		createCells();
	}

	private void createCells()	{
		for(int r = 0; r < rowSize; r++)	{
			board.add(new Cell[getWidth()]);
			for(int c = 0; c < getWidth(); c++)	{
				setCell(r,c,new Cell(r, c));
				addToWorld(r,c,new Ground());
			}
		}
	}

	public void addRow()	{
		board.add(new Cell[getWidth()]);
		for(int c = 0; c < getWidth(); c++)	{
			setCell(getHeight() -1, c,new Cell(getHeight() -1 , c));
		}
	}

	public void addToWorld(int row, int col, GameObject newGameObj)	{ 	
		getCell(row,col).addToCell(newGameObj);
	}

	public Cell getCell(int r,int c)	{
		return board.get(r)[c];
	}

	public void removeTop()	{
		board.remove(getHeight() -1);
	}

	private void setCell(int r, int c, Cell cell)	{
		board.get(r)[c] = cell;
	}

	public int getHeight()	{
		return board.size();
	}

	public int getWidth()	{
		return colSize;
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Board.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Board Unit Tests");
		Board b = new Board(7,8);
		t.compare(b.getCell(0,0).getCellHeight(),"==",1,"Cell height is one");
		b.addToWorld(0,0,new Car(Direction.NORTH,2));
		t.compare(b.getCell(0,0).getCellHeight(),"==",2,"Cell height is two");
		t.compare(b.getWidth(),"==",8,"Board width is 8");
		t.compare(b.getHeight(),"==",7,"Board Height is 7");
		b.addRow();
		t.compare(b.getHeight(),"==",8, "Board height increased");
		t.compare(b.getCell(7,0).getCellHeight(),"==",0,"Cell height is Zero");
		t.exitSuite();
		return t;
	}
}

