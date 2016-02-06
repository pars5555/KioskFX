/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth;


import photobooth.views.ExplorerPane;
import photobooth.views.HomePane;
import photobooth.views.NotWorkingPane;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 *
 * @author default
 */
public class Core {

    private static Core instance = null;
    private static int countdown;
    private final GpioPinDigitalInput coinAcceptorInputPin;
   
    private long lastCoinAcceptorHighPulseMilis = 0;
    private int insertedCoinPulseCount = 0;

    private enum Cointypes {

        AMD100, AMD200, AMD500
    }

    private Core() {
        countdown = 0;        
        int coinAcceptopPinNumber = Config.getInstance().getInt("coin_acceptor_pin");
        coinAcceptorInputPin = GpioControl.getInstance().getInputPin(coinAcceptopPinNumber, PinPullResistance.PULL_DOWN);
        
    }

    public static Core getInstance() {
        if (instance == null) {
            instance = new Core();
        }
        return instance;
    }

    public void init() {
        //CoinCounter.getInstance();        

        coinAcceptorInputPin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if (event.getState() == PinState.HIGH) {
                    lastCoinAcceptorHighPulseMilis = System.currentTimeMillis();
                    insertedCoinPulseCount++;
                }
            }
        });
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    Global.delay(100);
                    if (insertedCoinPulseCount > 0 && System.currentTimeMillis() - lastCoinAcceptorHighPulseMilis > 200) {
                        if (insertedCoinPulseCount >= 1 && insertedCoinPulseCount <= 2) {
                            coinInserted(Cointypes.AMD100);
                        } else if (insertedCoinPulseCount >= 3 && insertedCoinPulseCount <= 4) {
                            coinInserted(Cointypes.AMD200);
                        } else if (insertedCoinPulseCount > 4) {
                            coinInserted(Cointypes.AMD500);
                        }
                        lastCoinAcceptorHighPulseMilis = 0;
                        insertedCoinPulseCount = 0;
                    }
                }
            }
        }).start();
    }

    public int getCountdownSeconds() {
        return countdown;
    }

    public boolean isEnable() {
        return countdown > 1;
    }

    public void coinInserted(Cointypes coin) {
        if (Config.getInstance().getInt("not_working") == 1) {
            return;
        }
        switch (coin) {
            case AMD100:
                Global.print("coin inserted: 100Amd");
                //CoinCounter.getInstance().addAmd100Coin();
                
                break;
            case AMD200:
                Global.print("coin inserted: 200Amd");
               // CoinCounter.getInstance().addAmd200Coin();
                
                break;
            case AMD500:
                Global.print("coin inserted: 500Amd");
                //CoinCounter.getInstance().addAmd500Coin();
               
                break;
        }
    }

}
