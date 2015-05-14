import com.bclarke.testing.*;
import com.bclarke.general.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.net.*;
import java.util.*;


public class Display extends JPanel implements MouseListener, KeyListener{

	private Board board;
	private Image frogImage;
	private Image carImage;
	private Image bloodImage;
	private Image roadImage;
	private Image waterImage;
	private Image logImage;
	private int displayLowerBound;
	private int displayUpperBound;
	private FrogView frogView;

	private final int WORLD_WIDTH = 9;
	private final int WORLD_HEIGHT = 8;
 	private ConcurrentLinkedQueue<Direction> queue;

 	private final int KEY_UP = 38;
 	private final int KEY_DOWN = 40;
 	private final int KEY_RIGHT = 37;
 	private final int KEY_LEFT = 39;

	public Display(ConcurrentLinkedQueue<Direction> q, FrogView view)	{
		board = new Board(WORLD_WIDTH,WORLD_HEIGHT);
		queue = q;
		setPreferredSize(new Dimension(500,500));
		setBackground(Color.WHITE);
		// addMouseListener(this);
		addKeyListener(this);
		this.setFocusable(true);
		initImages();
		frogView = view;
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

        u = this.getClass().getResource("images/road.png");
        icon = new ImageIcon(u);
        roadImage = icon.getImage();

        u = this.getClass().getResource("images/water.jpg");
        icon = new ImageIcon(u);
        waterImage = icon.getImage();	

        u = this.getClass().getResource("images/log.png");
        icon = new ImageIcon(u);
        logImage = icon.getImage();
	}

    void update(Board b) {
        board = b;
        repaint();
    }

    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        int upper = frogView.getUpperView();
        int lower = frogView.getLowerView();
        int y = 0;
        for (int yPre = 0, r = lower; r < upper; r++, yPre++) {
        	// System.out.println("CHECKING ROW" + r);
        	for (int c = 0; c < board.getWidth(); c++) {
        		try	{
        			y = convertYAxis(yPre);
	        		for(int cellObj = 0; cellObj < board.getCell(r,c).getCellHeight(); cellObj++ )	{
	        			switch(board.getCell(r,c).getGameObjectType(cellObj))	{
	        				case FROG:
	        					// System.out.println("GRAPHIX" + y);
	        					// System.out.println("ROW" + r);
	        					frog(g, 50*c, 50*y);
	        					// System.out.println(r + " " + c + " " + cellObj);
	        					break;
	        				case GROUND:
	        					ground(g, 50*c, 50*y);
	        					break;
	        				case CAR:
	        					car(g,50*c,50*y);
	        					break;
	        				case BLOOD:
	        					displayImage(g,50*c,50*y,bloodImage);
	        					break;
	        				case ROAD:
	        					displayImage(g,50*c,50*y,roadImage);
	        					break;
	        				case WATER:
	        					displayImage(g,50*c,50*y,waterImage);
	        					break;
	        				case LOG:
	        					displayImage(g,50*c,50*y,logImage);
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

    private int convertYAxis(int y)	{
    		return (WORLD_HEIGHT - 1) - y;
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
		World w = new World(worldQueue,new LinkedList<CreateInstruction>());
		w.getWindow().getDisplay().queue.add(Direction.NORTH);
		try	{
			t.compare(Direction.NORTH,"==",worldQueue.remove(),"North direction added and taken from the queue");
			w.moveFrog(Direction.NORTH);
			

		} catch(Exception e)	{

		}

		return t;
	}
}

