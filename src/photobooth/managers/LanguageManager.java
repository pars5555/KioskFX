/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package photobooth.managers;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author default
 */
public class LanguageManager {

    private static LanguageManager instance = null;
    private final Locale locale;
    private final ResourceBundle bundle;

    public LanguageManager() {
        this.locale = new Locale("hy", "AM");
        bundle = ResourceBundle.getBundle("photobooth/translations/lang", this.locale);
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String getPhrase(String key) {
        return bundle.getString(key);
    }

}
