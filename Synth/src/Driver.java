import java.io.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import ddf.minim.*;
import ddf.minim.spi.*; // for AudioRecordingStream
import ddf.minim.ugens.*;
import processing.core.*;

public class Driver extends PApplet {

	
	static final float[] modulationFrequencyMultipliers = {0.25f, 0.5f, 0.75f, 1f, 2f, 3f, 4f, 6f, 8f};
	static final float[] modulationAmplitudes = {0f, 0.01f, 0.1f, 0.2f, 0.5f, 1f, 2f, 4f, 8f};
	
	static final boolean MUTE_ALL = false;
	static final int MAX_NUM_MODULATORS = 5;
	
	// create all of the variables that will need to be accessed in
	// more than one methods (setup(), draw(), stop()).
	Minim minim;
	AudioOutput out;
	
	ToneInstrument instr;
	Button button;
	Label modLabel;
	
	//which modulator should be altered by moving the mouse?
	int modulatorToChange = 0;
	static int numModulators = 2;	//this needs to be set by hand at the moment
	
	//each modulator parameter is passed in as an array of [mod1Frequency, mod1Amplitude, mod2Frequency, ...]
	float[] initialModulatorParameters = {440f, 0.2f, 360f, 0.2f};
	
	//fft = new FFT()

	// setup is run once at the beginning
	public void setup()
	{

	  // initialize the drawing window
	  size( 512, 400, P2D );

	  // initialize the minim and out objects
	  minim = new Minim( this );
	  out = minim.getLineOut( Minim.MONO, 2048 );
	  
	  if(MUTE_ALL)
		  out.mute();
  
	  
	  instr = new ToneInstrument(440f, 1f, initialModulatorParameters, out);
	  
	  
	  //for making the sweep of the space
	  Oscil sineOsc = new Oscil( 200f, 1.0f, Waves.SINE);
	  
	  sineOsc.patch(instr.carrier.frequency);
	  
	  out.playNote(0f, 10000f, instr);
	  
	  modLabel = new Label("Changing modulator #" + modulatorToChange);
	  modLabel.setBounds(25, 210, 200, 30);
	  modLabel.setBackground(Color.BLACK);
	  modLabel.setForeground(Color.WHITE);
	  add(modLabel);
	  
	  //add(new JLabel("Hi you"));
	  
	  
	  
	  button = new Button("Harmonic");
	  button.setBounds(250, 300, 80, 30);
	  add(button);
	  
	 /*
	  
	  TODO
	  The top commented line works but the next 4 don't
	  It's something to do with ofPitch "c4" etc not working
	 //out.playNote(0f, 20f, new ToneInstrument( 250, 0.5f, out ));
	  * 
	  out.playNote(0f, 1f, new ToneInstrument( Frequency.ofPitch("C4").asHz(), 1f, out ));
	  out.playNote(1f, 1f, new ToneInstrument( Frequency.ofPitch("E4").asHz(), 1f, out ));
	  out.playNote(2f, 1f, new ToneInstrument( Frequency.ofPitch("G4").asHz(), 1f, out ));
	  out.playNote(3f, 1f, new ToneInstrument( Frequency.ofPitch("C5").asHz(), 1f, out ));
	  
	  out.playNote(0f, 1f, new ToneInstrument( 261f, 1f, out ));
	  out.playNote(1f, 1f, new ToneInstrument( 329f, 1f, out ));
	  out.playNote(2f, 1f, new ToneInstrument( 391f, 1f, out ));
	  out.playNote(3f, 1f, new ToneInstrument( 523f, 1f, out ));
	  
	  
	  System.out.println(Frequency.ofPitch("C4").asHz());
	  System.out.println(Frequency.ofPitch("E4").asHz());
	  System.out.println(Frequency.ofPitch("G4").asHz());
	  System.out.println(Frequency.ofPitch("C5").asHz());
*/
	  /*
	  out.playNote(0f, 5f, new ToneInstrument( Frequency.ofPitch("C4").asHz(), 1f, out ));
	  out.playNote(1f, 5f, new ToneInstrument( Frequency.ofPitch("E4").asHz(), 1f, out ));
	  out.playNote(2f, 5f, new ToneInstrument( Frequency.ofPitch("G4").asHz(), 1f, out ));
	  out.playNote(3f, 5f, new ToneInstrument( Frequency.ofPitch("C5").asHz(), 1f, out ));
	  */
	  
	  /*
	  out.pauseNotes();
	  // make four repetitions of the same pattern
	  for( int i = 0; i < 4; i++ )
	  {
	    // add some low notes
	    out.playNote( (float) (1.25 + i*2.0), 0.3f, new ToneInstrument( 50, 0.8f, out ) );
	    out.playNote((float) ( 2.50 + i*2.0), 0.3f, new ToneInstrument( 50, 0.8f, out ) );
	    out.playNote( (float) (1.25 + i*2.0), 0.3f, new ToneInstrument( 60, 0.8f, out ) );
	    out.playNote((float) ( 2.50 + i*2.0), 0.3f, new ToneInstrument( 60, 0.8f, out ) );
	    out.playNote( (float) (1.25 + i*2.0), 0.3f, new ToneInstrument( 70, 0.8f, out ) );
	    out.playNote((float) ( 2.50 + i*2.0), 0.3f, new ToneInstrument( 70, 0.8f, out ) );
	    out.playNote( (float) (1.25 + i*2.0), 0.3f, new ToneInstrument( 80, 0.8f, out ) );
	    out.playNote((float) ( 2.50 + i*2.0), 0.3f, new ToneInstrument( 80, 0.8f, out ) );
	    
	    // add some middle notes
	    out.playNote((float) ( 1.75 + i*2.0), 0.3f, new ToneInstrument( 175, 0.4f, out ) );
	    out.playNote((float) ( 2.75 + i*2.0), 0.3f, new ToneInstrument( 175, 0.4f, out ) );
	    
	    // add some high notes
	    out.playNote((float) ( 1.25 + i*2.0), 0.3f, new ToneInstrument( 3750, 0.07f, out ) );
	    out.playNote((float) ( 1.5 + i*2.0), 0.3f, new ToneInstrument( 1750, 0.02f, out ) );
	    out.playNote((float) ( 1.75 + i*2.0), 0.3f, new ToneInstrument( 3750, 0.07f, out ) );
	    out.playNote((float) ( 2.0 + i*2.0), 0.3f, new ToneInstrument( 1750, 0.02f, out ) );
	    out.playNote( (float) (2.25 + i*2.0), 0.3f, new ToneInstrument( 3750, 0.07f, out ) );
	    out.playNote( (float) (2.5 + i*2.0), 0.3f, new ToneInstrument( 5550, 0.09f, out ) );
	    out.playNote( (float) (2.75 + i*2.0), 0.3f, new ToneInstrument( 3750, 0.07f, out ) );
	  }
	  // resume time after a bunch of notes are added at once
	  out.resumeNotes();
	  */
	  
	  
	}

	// draw is run many times
	public void draw()
	{
	  // erase the window to black
	  background( 0 );
	  // draw using a white stroke
	  stroke( 255 );
	  // draw the waveforms
	  for( int i = 0; i < out.bufferSize() - 1; i++ )
	  {
	    // find the x position of each buffer value
	    float x1  =  map( i, 0, out.bufferSize(), 0, width );
	    float x2  =  map( i+1, 0, out.bufferSize(), 0, width );
	    // draw a line from one buffer position to the next for both channels
	    line( x1, 50 + out.left.get(i)*50, x2, 50 + out.left.get(i+1)*50);
	    line( x1, 150 + out.right.get(i)*50, x2, 150 + out.right.get(i+1)*50);
	  }  
	}

	// stop is run when the user presses stop
	public void stop()
	{
	  // close the AudioOutput
	  out.close();
	  // stop the minim object
	  minim.stop();
	  // stop the processing object
	  super.stop();
	}
	
	
	// when the mouse is moved, change the delay parameters
	public void mouseMoved()
	{
		//map(float value, float inLow, float inHigh, float outLow, float outHigh)
		//the horizontal mouse location changes the frequency of the modulator
		//by selecting a value from the array of frequency multipliers
		/*
		instr.modulator.setFrequency(instr.carrier.frequency.getLastValue() * modulationFrequencyMultipliers[(int) (map((float) mouseX, 0f, (float) width, 0f, (float) modulationFrequencyMultipliers.length))]);
		
		//instr.modulator.setAmplitude(modulationAmplitudes[modulationAmplitudes.length - 1 - (int) (map((float) mouseY, 0f, (float) height, 0f, (float) modulationAmplitudes.length))]);
		*/
	}
	
	boolean[] keys = new boolean[526];
	
	public void keyPressed()
	{
		keys[keyCode] = true;
		System.out.println(KeyEvent.getKeyText(keyCode));
	}
	
	public void mousePressed()
	{
		if (mouseButton == LEFT)
		{
			modulatorToChange++;
			modulatorToChange %= numModulators;
			modLabel.setText("Changing modulator #" + modulatorToChange);
			
			System.out.println(modulatorToChange);
		}
		else
		{
			System.out.println("Mouse pressed at (" + mouseEvent.getX() + ", " + mouseEvent.getY() + ")");
		}
			
	}
}