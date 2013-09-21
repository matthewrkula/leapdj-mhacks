import interfaces.DepthListener;
import interfaces.HeightListener;
import interfaces.SpinnerListener;
import interfaces.TapListener;

import java.io.File;
import java.io.IOException;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;

import com.leapmotion.leap.Controller;

public class LeapDJ{
	
	public static void main(String[] args) throws Exception{
		new MyPlayer().play();
	}
}

class MyPlayer implements HeightListener, DepthListener, TapListener, SpinnerListener{
	
	AePlayWave player;
	Player siren;
	
	public void play(){
		player = new AePlayWave("guacamole.wav");
//		player = new AePlayWave("shake1.wav");
		player.start();
		
        LeapListener listener = new LeapListener(this, this, this, this);
        Controller controller = new Controller();

        controller.addListener(listener);
        
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		
		PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder", 
				new Format[]{input1, input2}, 
				new Format[]{output}, 
				PlugInManager.CODEC);
		
		try{
			siren = Manager.createPlayer(new MediaLocator(new File("siren.mp3").toURI().toURL()));
	
	        // Keep this process running until Enter is pressed
	        System.out.println("Press Enter to quit...");
	        System.in.read();
		}catch(IOException e){
			System.out.println(e.toString());
		}catch(Exception e){
			System.out.println(e.toString());
		}

        // Remove the sample listener when done
        controller.removeListener(listener);
	}
	
	@Override
	public void onHandInListener(int height) {
		System.out.println(height);
		if(player != null)
			player.setSpeed(height);
	}
	
	@Override 
	public void onDepthChange(int depth){
		if(player != null)
			player.setDirection(depth);
	}

	@Override
	public void onKeyTap() {
		System.out.println("tap");
		if(player != null)
			player.jumpBack();
	}

	@Override
	public void onStartSpin() {
		siren.start();
	}

	@Override
	public void onStopSpin() {
		siren.stop();
	}
}


