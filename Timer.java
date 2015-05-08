import com.bclarke.testing.*;
import com.bclarke.general.*;

public class Timer  {

	private long lastAction;
	private long interval;

	public Timer(double i)	{
		setInterval(i);
		lastAction = 0;
	}

	private long convert(double i)	{
		
		try	{
			int decimalPt = Integer.valueOf(String.valueOf(i).indexOf('.'));
			return (long) (Double.valueOf(String.valueOf(i).substring(0,decimalPt + 4)) * 1000);
		} catch (StringIndexOutOfBoundsException e)	{
			return (long) (i * 1000);
		}
	}

	public boolean ready()	{
		if(getCurrentTime() - getLastAction() >= interval)	{
			logAction();
			return true;
		}
		return false;
	}

	public void setInterval(double i)	{
		interval = convert(i);
	}

	private long getCurrentTime()	{
		return System.currentTimeMillis();
	}

	private long getLastAction()	{
		return lastAction;
	}

	private void logAction()	{
		lastAction = System.currentTimeMillis();
	}


/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Timer.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Timer Unit Tests");
		Timer ti = new Timer(1);
		t.compare(ti.convert(100.2),"==",(long)100200,"Converting interval");
		t.compare(ti.convert(101.244443),"==",(long)101244,"Converting interval");
		t.compare(ti.ready(),"==",true,"Action Ready");
		t.compare(ti.ready(),"==",false,"Action Not ready Ready");
		t.exitSuite();
		return t;
	}
}

