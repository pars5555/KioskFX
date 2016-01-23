/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author default
 */
public class GpioControl {

    private static GpioControl instance = null;
    private final GpioController gpio;

    private GpioControl() {
        gpio = GpioFactory.getInstance();
    }

    public GpioPinDigitalOutput getOutputPin(Integer pinNumber) {
        switch (pinNumber) {
            case 0:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
            case 1:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
            case 2:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
            case 3:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
            case 4:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
            case 5:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05);
            case 6:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
            case 7:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07);
            case 8:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08);
            case 9:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09);
            case 10:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10);
            case 11:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11);
            case 12:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12);
            case 13:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13);
            case 14:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14);
            case 15:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15);
            case 16:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16);
            case 17:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17);
            case 18:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_18);
            case 19:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_19);
            case 20:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_20);
            case 21:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21);
            case 22:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22);
            case 23:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23);
            case 24:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24);
            case 25:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);
            case 26:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26);
            case 27:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27);
            case 28:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28);
            case 29:
                return gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29);
            default:
                return null;
        }
    }

    public GpioPinDigitalInput getInputPin(Integer pinNumber, PinPullResistance ppr) {
        switch (pinNumber) {
            case 0:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_00, ppr);
            case 1:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, ppr);
            case 2:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, ppr);
            case 3:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, ppr);
            case 4:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, ppr);
            case 5:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, ppr);
            case 6:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, ppr);
            case 7:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, ppr);
            case 8:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_08, ppr);
            case 9:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_09, ppr);
            case 10:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_10, ppr);
            case 11:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, ppr);
            case 12:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, ppr);
            case 13:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, ppr);
            case 14:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_14, ppr);
            case 15:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, ppr);
            case 16:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_16, ppr);
            case 17:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, ppr);
            case 18:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_18, ppr);
            case 19:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_19, ppr);
            case 20:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_20, ppr);
            case 21:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, ppr);
            case 22:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, ppr);
            case 23:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_23, ppr);
            case 24:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_24, ppr);
            case 25:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, ppr);
            case 26:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, ppr);
            case 27:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, ppr);
            case 28:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, ppr);
            case 29:
                return gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, ppr);
            default:
                return null;
        }
    }

    public static GpioControl getInstance() {
        if (instance == null) {
            instance = new GpioControl();
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

}
