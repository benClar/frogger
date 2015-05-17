import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.*;


class keyAction extends AbstractAction {
	private String action;
	private ConcurrentLinkedQueue<UserCommand> queue;
    public keyAction(String a,ConcurrentLinkedQueue<UserCommand> q) {
    	action = a;
    	queue = q;
    }
    
    public void actionPerformed(ActionEvent e) {
        switch(action)	{
        	case "RIGHT":
    			queue.add(UserCommand.MOVE_EAST);
        		break;
        	case "UP":
    			queue.add(UserCommand.MOVE_NORTH);
        		break;
        	case "DOWN":
    			queue.add(UserCommand.MOVE_SOUTH);
        		break;
        	case "LEFT":
    			queue.add(UserCommand.MOVE_WEST);
        		break;
        	default:
        		break;
        }
    }
}
