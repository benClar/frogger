import com.bclarke.testing.*;
import com.bclarke.general.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import javax.swing.border.*;
import java.net.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Window implements Runnable {

	private int row, column;
	private GameObject gameObj;
	private int task;
	private Display drawing;
	private Board board;
	private ConcurrentLinkedQueue<UserCommand> queue;
	private FrogView frogView;
	private JLabel scoreLabel;
	private JLabel deathLabel;
	private JLabel invincabilityIcon;

	private final int START = 0;
	private final int UPDATE = 1;

	public Window(ConcurrentLinkedQueue<UserCommand> q, FrogView view)	{
		task = START;
		queue = q;
		frogView = view;
		Timer resetTimer = new Timer(1.0);
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

    public void setView(FrogView v)	{
    	frogView = v;
    	drawing.setView(v);
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

    public void update(String label, int i)	{
    	String s[];
    	switch(label)	{
    		case "score":
    			scoreLabel.setText(scoreLabel.getText().split(":")[0] + ":" + (Integer.parseInt(scoreLabel.getText().split(":")[1]) + i));
    			break;
    		case "death":
    			deathLabel.setText(deathLabel.getText().split(":")[0] + ":" +  (Integer.parseInt(deathLabel.getText().split(":")[1]) + 1));
    			break;
    		default:
    			break;
    	}
    }

    public void setScore(int i)	{
    	scoreLabel.setText(scoreLabel.getText().split(":")[0] + ":" + "0");
    }

    private JPanel display()	{
    	JPanel allBox = new JPanel();
    	Box userBox = Box.createVerticalBox();
    	Box gameBox = Box.createVerticalBox();
    	GridLayout grid = new GridLayout(1, 2);
    	scoreLabel = new JLabel("Current Score:0");
    	deathLabel = new JLabel("Number of deaths:0");
        // URL u = this.getClass().getResource("images/heart.png");
        ImageIcon icon = new ImageIcon(getImage("images/heart.png",40,40));
        invincabilityIcon = new JLabel(icon);
        invincabilityIcon.setVisible(false);
    	drawing = new Display(queue,frogView);
    	gameBox.add(drawing);
    	userBox.add(invincabilityIcon);
    	userBox.add(scoreLabel);
    	userBox.add(deathLabel);
    	buttons(userBox);
    	allBox.setLayout(grid);
    	allBox.add(gameBox);
    	allBox.add(userBox);
    	return allBox;
    }

    public void setIcon(String icon,boolean status)	{
    	switch(icon)	{
    		case "heart":
    			invincabilityIcon.setVisible(status);
    			break;
    		default:
    			break;
    	}
    }

    // http://stackoverflow.com/questions/16343098/resize-a-picture-to-fit-a-jlabel
    public Image getImage(String location, int width, int height)	{
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(location));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dImg = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
		return dImg;
    }

    public Box buttons(Box b)	{
    	JButton resetButton = new JButton("Reset");
    	resetButton.addActionListener(new ActionListener()	{
	        public void actionPerformed(ActionEvent e) {
	           	queue.add(UserCommand.RESET_WORLD);
	        }
    	});
    	b.add(resetButton);

    	return b;
    }

	public void start()	{
		JFrame window = new JFrame();
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Frogger");
		window.add(display());
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

