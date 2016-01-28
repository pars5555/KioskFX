/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

/**
 *
 * @author default
 */
public class CoinAcceptorManager {

    private static CoinAcceptorManager instance = null;

    public CoinAcceptorManager() {
        init();
    }

    public static CoinAcceptorManager getInstance() {
        if (instance == null) {
            instance = new CoinAcceptorManager();
        }
        return instance;
    }

    public void turnOn() {

    }

    public void turnIOff() {

    }

    private void init() {
    }
}
