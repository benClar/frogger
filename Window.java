import com.bclarke.testing.*;
import com.bclarke.general.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class Window implements Runnable {

	private int row, column;
	private GameObject gameObj;
	private int task;
	private Display drawing;
	private Board board;
	private ConcurrentLinkedQueue<Direction> queue;
	private FrogView frogView;

	private final int START = 0;
	private final int UPDATE = 1;

	public Window(ConcurrentLinkedQueue<Direction> q, FrogView view)	{
		task = START;
		queue = q;
		frogView = view;
		try	{
			SwingUtilities.invokeAndWait(this);
    	} catch (Exception err)	{
    		throw new Error(err);
    	}
	}

    public void run() {
        if (task == START) {
        	start();
        } else if (task == UPDATE) {
        	update();
        } 
    }

    public Display getDisplay()	{
    	return drawing;
    }

    public void update(Board b)	{
    	board = b;
    	task = UPDATE;
    	try	{
    		SwingUtilities.invokeAndWait(this);
    	} catch (Exception err)	{
    		throw new Error(err);
    	}
    }


    public void update()	{
    	drawing.update(board);
    }

	public void start()	{
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		drawing = new Display(queue,frogView);
		window.add(drawing);
		window.pack();
		window.setLocationByPlatform(true);
		window.setVisible(true);
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Window.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Window Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

