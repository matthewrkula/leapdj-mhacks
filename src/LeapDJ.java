import interfaces.DepthListener;
import interfaces.HeightListener;

import java.io.IOException;

import com.leapmotion.leap.Controller;


public class LeapDJ{
	
	public static void main(String[] args){
		new MyPlayer().play();
	}
}

class MyPlayer  implements HeightListener, DepthListener{
	
	AePlayWave player;
	
	public void play(){
		player = new AePlayWave("guacamole.wav");
		player.start();
		
        LeapListener listener = new LeapListener(this, this);
        Controller controller = new Controller();

        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
	}
	
	@Override
	public void onHandInListener(int height) {
		System.out.println(height);
		player.setSpeed(height);
	}
	
	@Override 
	public void onDepthChange(int depth){
		player.setDirection(depth);
	}
}

