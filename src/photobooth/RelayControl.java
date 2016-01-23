/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

/**
 *
 * @author default
 */
public class RelayControl {

    private static RelayControl instance;

    private final GpioPinDigitalOutput waterOutputPin;
    private final GpioPinDigitalOutput foamOutputPin;
    private final GpioPinDigitalOutput additional1OutputPin;
    private final GpioPinDigitalOutput additional2OutputPin;
    private boolean foamRealyOn;
    private boolean waterRealyOn;
    private boolean additionalRealy1On;
    private boolean additionalRealy2On;

    public RelayControl() {
        int water_realy_pin = Config.getInstance().getInt("water_realy_pin");
        int foam_realy_pin = Config.getInstance().getInt("foam_realy_pin");
        int additional1_realy_pin = Config.getInstance().getInt("additional1_realy_pin");
        int additional2_realy_pin = Config.getInstance().getInt("additional2_realy_pin");
        waterOutputPin = GpioControl.getInstance().getOutputPin(water_realy_pin);
        foamOutputPin = GpioControl.getInstance().getOutputPin(foam_realy_pin);
        additional1OutputPin = GpioControl.getInstance().getOutputPin(additional1_realy_pin);
        additional2OutputPin = GpioControl.getInstance().getOutputPin(additional2_realy_pin);
        foamRealyOn = false;
        waterRealyOn = false;
        additionalRealy1On = false;
    }

    public static RelayControl getInstance() {
        if (instance == null) {
            instance = new RelayControl();
        }
        return instance;
    }

    public void setWaterRelayOn() {
        if (!waterRealyOn) {
            waterRealyOn = true;
            waterOutputPin.setState(PinState.HIGH);
        }
    }

    
    public void setAllRelaysOff() {
        setWaterRelayOff();
        setFoamRelayOff();
        setAdditional1RelayOff();
        setAdditional2RelayOff();
    }
    public void setWaterRelayOff() {
        if (waterRealyOn) {
            waterRealyOn = false;
            waterOutputPin.setState(PinState.LOW);
        }

    }

    public void setFoamRelayOn() {
        if (!foamRealyOn) {
            foamRealyOn = true;
            foamOutputPin.setState(PinState.HIGH);
        }
    }

    public void setFoamRelayOff() {
        if (foamRealyOn) {
            foamRealyOn = false;
            foamOutputPin.setState(PinState.LOW);
        }

    }

    public void setAdditional1RelayOn() {
        if (!additionalRealy1On) {
            additionalRealy1On = true;
            additional1OutputPin.setState(PinState.HIGH);
        }
    }

    public void setAdditional2RelayOn() {
        if (!additionalRealy2On) {
            additionalRealy2On = true;
            additional2OutputPin.setState(PinState.HIGH);
        }
    }

    public void setAdditional1RelayOff() {
        if (additionalRealy1On) {
            additionalRealy1On = false;
            additional1OutputPin.setState(PinState.LOW);
        }

    }

    public void setAdditional2RelayOff() {
        if (additionalRealy2On) {
            additionalRealy2On = false;
            additional2OutputPin.setState(PinState.LOW);
        }

    }
    
    
    
    public boolean isAnyRealyOn() {
        return foamRealyOn || waterRealyOn || additionalRealy1On || additionalRealy2On;
    }
    
    public boolean isFoamRealyOn() {
        return foamRealyOn;
    }

    public boolean isAdditional1RealyOn() {
        return additionalRealy1On;
    }

    public boolean isAdditional2RealyOn() {
        return additionalRealy2On;
    }

    public boolean isWaterRealyOn() {
        return waterRealyOn;
    }

}
