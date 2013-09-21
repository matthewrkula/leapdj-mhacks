import interfaces.DepthListener;
import interfaces.HeightListener;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

class LeapListener extends Listener {
	
	HeightListener l;
	DepthListener d;
	
	public LeapListener(HeightListener l, DepthListener d){
		this.l = l;
		this.d = d;
	}

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        
        d.onDepthChange(1);

        if (!frame.hands().empty()) {
            // Get the first hand
            Hand hand = frame.hands().get(0);
            if(frame.hands().count() == 1){
	        	if(hand.fingers().count() == 5)
	        		l.onHandInListener((int)(hand.palmPosition().getY()/20));
            }else{
            	d.onDepthChange(-1);
            }
            	
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    
                    if(swipe.state() == State.STATE_STOP){
                    	if(swipe.direction().getZ() > 0.8)
                    		System.out.println("Backwards");
                    	else if(swipe.direction().getZ() < -0.8)
                    		System.out.println("Forwards");
                    	else 
                    		System.out.println("IDK");
                    }
                    break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }
        }
    }
}