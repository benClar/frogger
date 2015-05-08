import com.bclarke.testing.*;
import com.bclarke.general.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.net.*;
import java.util.*;


public class Display extends JPanel implements MouseListener, KeyListener{

	private Cell[][] board;
	private Image frogImage;
	private Image carImage;
	private Image bloodImage;

	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
 	private ConcurrentLinkedQueue<Direction> queue;

 	private final int KEY_UP = 38;
 	private final int KEY_DOWN = 40;
 	private final int KEY_RIGHT = 37;
 	private final int KEY_LEFT = 39;

	public Display(ConcurrentLinkedQueue<Direction> q)	{
		board = new Cell[WORLD_HEIGHT][WORLD_WIDTH];
		createCells();
		queue = q;
		setPreferredSize(new Dimension(500,500));
		setBackground(Color.WHITE);
		// addMouseListener(this);
		addKeyListener(this);
		this.setFocusable(true);
		initImages();

	}

	private void initImages()	{
        URL u = this.getClass().getResource("images/Frog.png");
        ImageIcon icon = new ImageIcon(u);
        frogImage = icon.getImage();

        u = this.getClass().getResource("images/car.png");
        icon = new ImageIcon(u);
        carImage = icon.getImage();	

        u = this.getClass().getResource("images/blood.png");
        icon = new ImageIcon(u);
        bloodImage = icon.getImage();	 	
	}

    void update(Cell[][] b) {
        board = b;
        repaint();
    }

	private void createCells()	{
		for(int row = 0; row < board.length; row ++)	{
			for(int col = 0; col < board[row].length; col++)	{
				board[row][col] = new Cell(row, col);
			}
		}
	}

    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        for (int r = 0; r < WORLD_HEIGHT; r++) {
        	for (int c = 0; c < WORLD_WIDTH; c++) {
        		try	{
	        		for(int cellObj = 0; cellObj < board[r][c].getCellHeight(); cellObj++ )	{
	        			switch(board[r][c].getGameObjectType(cellObj))	{
	        				case FROG:
	        					frog(g, 50*c, 50*r);
	        					break;
	        				case GROUND:
	        					ground(g, 50*c, 50*r);
	        					break;
	        				case CAR:
	        					car(g,50*c,50*r);
	        					break;
	        				case BLOOD:
	        					displayImage(g,50*c,50*r,bloodImage);
	        					break;
	        				default:

	        					break;
	        			}
	        		}
        		} catch (NullPointerException e)	{

        		}
            }
        }
    }

    private void frog(Graphics2D g, int x, int y)	{
    	g.drawImage(frogImage, x, y,50,50, null);
    }

    private void displayImage(Graphics2D g, int x, int y, Image i)	{
    	g.drawImage(i, x, y,50,50, null);
    }

    private void ground(Graphics2D g, int x, int y)	{
    	g.setColor(Color.GRAY);
    	g.fillRect(x, y, 50, 50);
    }

    private void car(Graphics2D g, int x, int y)	{
    	g.drawImage(carImage, x, y,50,50, null);
    }

	public void mouseReleased(MouseEvent e) {
	        // queue.add(e.getX() / 100);
	        // queue.add(e.getY() / 100);
	}
    
    public void mousePressed(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent e) {
    	switch(e.getExtendedKeyCode())	{
    		case KEY_UP:
    			queue.add(Direction.NORTH);
    			break;
    		case KEY_DOWN:
    			queue.add(Direction.SOUTH);
    			break;
    		case KEY_LEFT:
    			queue.add(Direction.EAST);
    			break;
    		case KEY_RIGHT:
    			queue.add(Direction.WEST);
    			break;
    		default:
    			break;
    	}
    }
    public void keyReleased(KeyEvent e) {}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Display.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Display Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}

	public static Testing componentTest_frogMove(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Display Component Test: Move Frog");
		ConcurrentLinkedQueue<Direction> worldQueue = new ConcurrentLinkedQueue<Direction>();
		Creator c = new Creator();
		World w = c.createWorld(worldQueue,new LinkedList<CreateInstruction>());
		w.getWindow().getDisplay().queue.add(Direction.NORTH);
		try	{
			t.compare(Direction.NORTH,"==",worldQueue.remove(),"North direction added and taken from the queue");
			w.moveFrog(Direction.NORTH);
			

		} catch(Exception e)	{

		}

		return t;
	}
}

