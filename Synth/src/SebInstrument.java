import ddf.minim.*;
import ddf.minim.ugens.*;

// Every instrument must implement the Instrument interface so 
// playNote() can call the instrument's methods.
class SebInstrument implements Instrument
{
  // create all variables that must be used througout the class
  Oscil sineOsc;
  ADSR  adsr;
  AudioOutput out;
  UGen cons;
  
  // constructor for this instrument
  SebInstrument( float frequency, float amplitude, AudioOutput output )
  {
    // equate class variables to constructor variables as necessary
    out = output;
    
    // create new instances of any UGen objects as necessary
    sineOsc = new Oscil( frequency, amplitude, Waves.TRIANGLE );
    adsr = new ADSR( 0.5f, 0.01f, 0.05f, 0.5f, 0.5f);
    //cons = new Constant(440);
    
    // patch everything together up to the final output
    //cons.patch( adsr );
    
  }
  
  // every instrument must have a noteOn( float ) method
 public void noteOn( float dur )
  {
    // turn on the ADSR
    adsr.noteOn();
    // patch to the output
    adsr.patch( out );
   }
  
  // every instrument must have a noteOff() method
 public  void noteOff()
  {
    // tell the ADSR to unpatch after the release is finished
    adsr.unpatchAfterRelease( out );
    // call the noteOff 
    adsr.noteOff();
  }
}