import interfaces.DepthListener;
import interfaces.HeightListener;
import interfaces.SpinnerListener;
import interfaces.TapListener;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;

class LeapListener extends Listener {
	
	HeightListener l;
	DepthListener d;
	TapListener t;
	SpinnerListener s;
	
	public LeapListener(HeightListener l, DepthListener d, TapListener t, SpinnerListener s){
		this.l = l;
		this.d = d;
		this.t = t;
		this.s = s;
	}
	
	

    @Override
	public void onInit(Controller c) {
		// TODO Auto-generated method stub
		super.onInit(c);
		c.enableGesture(Type.TYPE_SWIPE);
		c.enableGesture(Type.TYPE_CIRCLE);
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
                    	if(swipe.direction().getX() < -0.8)
                    		t.onKeyTap();
                    }
                    break;
                case TYPE_CIRCLE:
                	CircleGesture circle = new CircleGesture(gesture);
                	
                	if(circle.state() == State.STATE_START)
                		s.onStartSpin();
                	else if(circle.state() == State.STATE_STOP)
                		s.onStopSpin();
                	break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }
        }
    }
}