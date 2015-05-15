import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.*;


class keyAction extends AbstractAction {
	private String action;
	private ConcurrentLinkedQueue<Direction> queue;
    public keyAction(String a,ConcurrentLinkedQueue<Direction> q) {
    	action = a;
    	queue = q;
    }
    
    public void actionPerformed(ActionEvent e) {
        switch(action)	{
        	case "RIGHT":
    			queue.add(Direction.EAST);
        		break;
        	case "UP":
    			queue.add(Direction.NORTH);
        		break;
        	case "DOWN":
    			queue.add(Direction.SOUTH);
        		break;
        	case "LEFT":
    			queue.add(Direction.WEST);
        		break;
        	default:
        		break;
        }
    }
}
