package fact.it.epj2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Glavna klasa aplikacije koja pokrece JavaFX aplikaciju.
 * Nasledjuje klasu {@link Application} i implementira metodu {@code start} za postavljanje glavne scene.
 * <p>
 * Ova klasa ucitava FXML datoteku koja definise izgled i ponasanje korisnickog interfejsa aplikacije.
 * </p>
 *
 * @author Mihajlo Pasic
 * @version 1.0
 * @since 2024
 */
public class HelloApplication extends Application {
    /**
     * Pokrece glavnu scenu aplikacije.
     * <p>
     * Ova metoda se poziva automatski kada se aplikacija pokrene. Ucitace FXML datoteku
     * koja sadrzi definiciju korisnickog interfejsa i postaviti je na glavnu pozornicu (Stage).
     * </p>
     *
     * @param stage Glavna pozornica aplikacije.
     * @throws IOException Ako dođe do greske prilikom ucitavanja FXML datoteke.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mapa.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 892, 826);
        stage.setTitle("ePJ2");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Glavna metoda koja pokrece aplikaciju.
     * <p>
     * Ova metoda koristi {@code launch} metodu iz klase {@link Application} da pokrene JavaFX aplikaciju.
     * </p>
     *
     * @param args Argumenti komandne linije.
     */
    public static void main(String[] args) {
        launch();
    }
}