/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author default
 */
public class GpioControlManager {

    private static GpioControlManager instance = null;

    private final GpioController gpio;
    private final Map<Integer, GpioPinDigitalOutput> pins;

    private GpioControlManager() {

        gpio = GpioFactory.getInstance();
        pins = new HashMap<>();
    }

    private GpioPinDigitalOutput getOutputPin(Integer pinNumber) {
        if (pins.containsKey(pinNumber)) {
            return pins.get(pinNumber);
        }
        switch (pinNumber) {
            case 0:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00));
                break;
            case 1:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01));
                break;
            case 2:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02));
                break;
            case 3:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03));
                break;
            case 4:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04));
                break;
            case 5:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05));
                break;
            case 6:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06));
                break;
            case 7:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07));
                break;
            case 8:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08));
                break;
            case 9:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09));
                break;
            case 10:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10));
                break;
            case 11:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11));
                break;
            case 12:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12));
                break;
            case 13:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13));
                break;
            case 14:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14));
                break;
            case 15:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15));
                break;
            case 16:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16));
                break;
            case 17:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17));
                break;
            case 18:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18));
                break;
            case 19:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19));
                break;
            case 20:
                pins.put(pinNumber, gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20));
                break;
        }
        return pins.get(pinNumber);
    }

    public static GpioControlManager getInstance() {
        if (instance == null) {
            instance = new GpioControlManager();
        }
        return instance;
    }

    public void setPinState(Integer pinNumber, boolean state) {
        if (state) {
            getOutputPin(pinNumber).high();
        } else {
            getOutputPin(pinNumber).low();
        }
    }

    public void setPinToHigh(Integer pinNumber) {
        getOutputPin(pinNumber).high();
    }

    public void setPinToLow(Integer pinNumber) {
        getOutputPin(pinNumber).low();
    }

    public void togglePinState(Integer pinNumber) {
        getOutputPin(pinNumber).toggle();
    }

    public void pulsePin(Integer pinNumber, Integer milliseconds) {
        getOutputPin(pinNumber).pulse(milliseconds);
    }

}
