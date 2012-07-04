import ddf.minim.*;
import ddf.minim.ugens.*;

// Every instrument must implement the Instrument interface so 
// playNote() can call the instrument's methods.
class ToneInstrument implements Instrument
{
  // create all variables that must be used througout the class
  FMCarrier carrier;
  Oscil[] modulators;
  Oscil modulator1;
  Oscil modulator2;
  //UGenInput modulator;
  ADSR  carrierADSR, modulatorADSR;
  AudioOutput out;
  
  // constructor for this instrument
  ToneInstrument( float carrierFrequency, float carrierAmplitude, float[] initialModulatorParameters, AudioOutput output )
  {
    // equate class variables to constructor variables as necessary
    out = output;
    
    // create new instances of any UGen objects as necessary
    modulators = new Oscil[Driver.MAX_NUM_MODULATORS];
    
    for(int i = 0; i < Driver.numModulators; i++)
    	modulators[i] = new Oscil( initialModulatorParameters[2*i + 0], initialModulatorParameters[2*i + 1], Waves.SINE);
    
    carrier = new FMCarrier( carrierFrequency, carrierAmplitude, Waves.SINE);//, freqMod1);
    
    //freqMod2 = new Oscil( 50f, 0.5f, Waves.SINE);
    
    
    //sustain amplitude is 1
    //ADSR(float maxAmp, float attTime, float decTime, float susLvl, float relTime) 
    carrierADSR = new ADSR(1f, 0.05f, 0.1f, 0.4f, 0.01f);
    //modulatorADSR = new ADSR()

    //test should be removed
    for(int i = 0; i < Driver.numModulators; i++)
    	modulators[i].patch(carrier.modulators[i]);

    carrier.patch(carrierADSR);
    //freqMod1.patch(sineOsc.modulator);
    // patch everything together up to the final output
    
   // freqMod1.patch(sineOsc.)
   // freqMod1.patch(sineOsc).patch(adsr);
    
    //freqMod1.patch(freqMod2).patch(sineOsc).patch( adsr );
  }
  
  // every instrument must have a noteOn( float ) method
 public void noteOn( float dur )
  {
    // turn on the ADSR
    carrierADSR.noteOn();
    // patch to the output
    carrierADSR.patch( out );
   }
  
  // every instrument must have a noteOff() method
 public  void noteOff()
  {
    // tell the ADSR to unpatch after the release is finished
	 carrierADSR.unpatchAfterRelease( out );
    // call the noteOff 
	 carrierADSR.noteOff();
  }
}
