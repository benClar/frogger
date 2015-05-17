import com.bclarke.testing.*;
import com.bclarke.general.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

public class Sound extends JFrame {

	Clip coin;
	Clip death;
	Clip heart;
	public Sound()	{
		loadSounds();
	}

	private void loadSounds()	{
		try	{
			URL url = this.getClass().getClassLoader().getResource("Sound/coin.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			coin = AudioSystem.getClip();
			coin.open(audioIn);

			url = this.getClass().getClassLoader().getResource("Sound/death.wav");
			audioIn = AudioSystem.getAudioInputStream(url);
			death = AudioSystem.getClip();
			death.open(audioIn);

			url = this.getClass().getClassLoader().getResource("Sound/heart.wav");
			audioIn = AudioSystem.getAudioInputStream(url);
			heart = AudioSystem.getClip();
			heart.open(audioIn);


		} 
		catch (UnsupportedAudioFileException e)	{ }
		catch(LineUnavailableException e){	}
		catch(IOException e){ }	
	}

	private void playClip(Clip c)	{
		if (c.isRunning()) { 
			c.stop(); 
		}
		c.setFramePosition(0);
		c.start();
	}

	public void playClip(String clip)	{
		switch(clip)	{
			case "coin":
				playClip(coin);
				break;
			case "death":
				playClip(death);
				break;
			case "heart":
				playClip(heart);
				break;
			default:
				break;
		}
	}

/*----------Testing----------*/

	public static void main( String[] args )    {
	 
		if(WhiteBoxTesting.checkMode(args).equals(OperatingMode.UNIT_TEST)) {
			Sound.unitTest(new Testing()).endTesting();
		} 
	}

	public static Testing unitTest(Testing t)	{
		WhiteBoxTesting.startTesting();
		t.enterSuite("Sound Unit Tests");
		/*Unit Tests Here*/
		t.exitSuite();
		return t;
	}
}

