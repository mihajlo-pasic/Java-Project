package fact.it.epj2.pomoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * Klasa koja sluzi za ucitavanje konfiguracionih postavki aplikacije iz fajla.
 */
public class Konfiguracija {
    /**
     * Metoda koja ucitava konfiguracione postavke iz fajla `app.properties`.
     *
     * @return Objekat tipa {@link Properties} koji sadrži konfiguracione postavke aplikacije.
     * Ako dodje do greske pri ucitavanju, vraca prazan {@link Properties} objekat.
     */
    public static Properties getProperties() {
        //String appConfigPath = "src/main/resources/fact/it/epj2/app.properties";
        String appConfigPath = "src"+ File.separator +"main"+File.separator+"resources"+File.separator+"fact"+File.separator+"it"+File.separator+"epj2"+File.separator+"app.properties";
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(appConfigPath));
            //return properties;
        } catch (IOException e) {
            System.err.println("GRESKA: " + e.getMessage());
        }
        return properties;
    }
}
