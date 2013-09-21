import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class AePlayWave extends Thread { 
 
    private String filename;
 
    private Position curPosition;
 
    int frameCounter;
    int skippedFrame = 10;
    int direction = 1;
    int frame;
 
    enum Position { 
        LEFT, RIGHT, NORMAL
    };
 
    public AePlayWave(String wavfile) { 
        filename = wavfile;
        curPosition = Position.NORMAL;
    } 
 
    public void run() { 
 
        File soundFile = new File(filename);
        if (!soundFile.exists()) { 
            System.err.println("Wave file not found: " + filename);
            return;
        } 
 
        AudioInputStream audioInputStream = null;
        try { 
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e1) { 
            e1.printStackTrace();
            return;
        } 
 
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
 
        try { 
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) { 
            e.printStackTrace();
            return;
        } 
 
        if (auline.isControlSupported(FloatControl.Type.PAN)) { 
            FloatControl pan = (FloatControl) auline
                    .getControl(FloatControl.Type.PAN);
            if (curPosition == Position.RIGHT) 
                pan.setValue(1.0f);
            else if (curPosition == Position.LEFT) 
                pan.setValue(-1.0f);
        } 
 
        auline.start();
        boolean moveForward = true;
        frameCounter = 0;
        
    	try{
	        FrameBuffer frameStream = new FrameBuffer(audioInputStream); 
	        System.out.println("Number of frames: " + frameStream.numberFrames());
	        int frameNumber = frameStream.numberFrames();
	        frame = 0;
	        while (frame < frameNumber) {
	             auline.write(frameStream.getFrame(frame), 0, frameStream.frameSize());
	             
	             if(frameCounter > skippedFrame){
	            	 moveForward = false;
	            	 frameCounter = 0;
	             }else{
	            	 moveForward = true;
	            	 frameCounter++;
	             }
	        
	             if(moveForward){
	            	 if(direction == 1)
	            		 frame = (frame + 1) % (frameNumber - 1);
	            	 else
	            		 frame = (frame - 1);
	             }
	        }
        }catch(Exception e){
        	System.out.println("fail");
        }
        
    } 
    
    public void setSpeed(int height){
    	skippedFrame = height;
    }
    
    public void setDirection(int depth){
    	this.direction = depth > 0 ? 1 : -1;
    }
    
    public void jumpBack(){
    	this.frame = this.frame - 10000;
    }
} 
 