package fact.it.epj2;

import fact.it.epj2.izvjestaj.Izvjestaj;
import fact.it.epj2.pomoc.Konfiguracija;
import fact.it.epj2.racun.Racun;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import fact.it.epj2.parser.Iznajmljivanje;
import fact.it.epj2.parser.IznajmljivanjeParser;
import fact.it.epj2.parser.PrevoznoSredstvoParser;
import fact.it.epj2.vozila.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Kontroler za FXML datoteku aplikacije. Ova klasa upravlja interakcijama izmedju korisnickog interfejsa i
 * logike aplikacije za simulaciju iznajmljivanja vozila i generisanje izvestaja.
 * <p>
 * Klasa sadrzi metode za inicijalizaciju komponenti, rukovanje dogadjajima, generisanje izvestaja,
 * i upravljanje simulacijom kretanja vozila.
 * </p>
 *
 * @author Mihajlo Pasic
 * @version 1.0
 * @since 2024
 */
public class HelloController {
    private VozniPark vozniPark;
    private List<Iznajmljivanje> iznajmljivanja;

    @FXML
    private GridPane mapa;

    @FXML
    private Button startDugme;

    @FXML
    private Label naslov;

    @FXML
    private TableColumn<Automobil, Integer> autoCijenaNabavke;

    @FXML
    private TableColumn<Automobil, LocalDateTime> autoDatumNabavke;

    @FXML
    private TableColumn<Automobil, String> autoId;

    @FXML
    private TableColumn<Automobil, String> autoModel;

    @FXML
    private TableColumn<Automobil, Integer> autoNivoBaterije;

    @FXML
    private TableColumn<Automobil, String> autoOpis;

    @FXML
    private TableColumn<Automobil, String> autoProizvodjac;

    @FXML
    private TableView<Automobil> autoTabela;

    @FXML
    private TableColumn<Bicikl, Integer> bikeCijenaNabavke;

    @FXML
    private TableColumn<Bicikl, Integer> bikeDomet;

    @FXML
    private TableColumn<Bicikl, String> bikeId;

    @FXML
    private TableColumn<Bicikl, String> bikeModel;

    @FXML
    private TableColumn<Bicikl, Integer> bikeNivoBaterije;

    @FXML
    private TableColumn<Bicikl, String> bikeProizvodjac;

    @FXML
    private TableView<Bicikl> bikeTabela;

    @FXML
    private TableColumn<Trotinet, Integer> trotCijenaNabavke;

    @FXML
    private TableColumn<Trotinet, String> trotId;

    @FXML
    private TableColumn<Trotinet, Integer> trotMaxBrzina;

    @FXML
    private TableColumn<Trotinet, String> trotModel;

    @FXML
    private TableColumn<Trotinet, Integer> trotNivoBaterije;

    @FXML
    private TableColumn<Trotinet, String> trotProizvodjac;

    @FXML
    private TableView<Trotinet> trotTabela;

    @FXML
    private TableColumn<Iznajmljivanje, String> kvarId;

    @FXML
    private TableColumn<Iznajmljivanje, String> kvarOpis;

    @FXML
    private TableView<Iznajmljivanje> kvarTabela;

    @FXML
    private TableColumn<Iznajmljivanje, LocalDateTime> kvarVrijeme;

    @FXML
    private TableColumn<Iznajmljivanje, String> kvarVrsta;

    private final Properties properties = Konfiguracija.getProperties();

    private Izvjestaj izvjestaj;

    @FXML
    private MenuButton izbornikDatumaMeni;

    @FXML
    private Button sumarniIzvjestajButton;

    @FXML
    private Button dnevniIzvjestajButton;

    /**
     * Inicijalizuje aplikaciju, postavlja pocetne vrednosti i popunjava tabele
     * sa podacima iz fajlova.
     */
    @FXML
    public void initialize() {
        vrstaVozilaComboBox.getItems().addAll("Automobil", "Bicikl", "Trotinet");
        izvjestaj = new Izvjestaj();
        int rows = 20; // Number of rows
        int cols = 20; // Number of columns

        // Add labels to each cell of the GridPane
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Label label = new Label("");
                label.setId("" + row +","+ col);
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Make label fill the cell
                label.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-background-color: white"); // Center text and add border
                mapa.add(label, col, row);
            }
        }

        String vozilaFilePath = properties.getProperty("VOZILA_FILE_PATH");
        String iznajmljivanjaFilePath = properties.getProperty("IZNAJMLJIVANJA_FILE_PATH");

        List<Vozilo> vozila = PrevoznoSredstvoParser.ucitajVozila(vozilaFilePath);
        vozniPark = new VozniPark();
        vozniPark.dodajVozila(vozila);

        iznajmljivanja = IznajmljivanjeParser.ucitajIznajmljivanja(iznajmljivanjaFilePath, vozniPark, mapa);
        Collections.sort(iznajmljivanja, Comparator.comparing(Iznajmljivanje::getDatumVrijemeIznajmljivanja));

        popuniTabelePrevoznihSredstava(vozniPark);
        popuniMenuButtonSaDatumima();
    }
    /**
     * Popunjava MenuButton sa jedinstvenim datumima iz liste iznajmljivanja.
     */
    private void popuniMenuButtonSaDatumima() {
        // Prikupljanje jedinstvenih datuma iz svih iznajmljivanja
        Set<LocalDate> jedinstveniDatumi = iznajmljivanja.stream()
                .map(iznajmljivanje -> iznajmljivanje.getDatumVrijemeIznajmljivanja().toLocalDate())
                .collect(Collectors.toCollection(TreeSet::new)); // Koristimo TreeSet za sortiranje datuma

        // Formatter za formatiranje datuma u format dd.MM.yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Dodavanje datuma kao opcija u MenuButton
        for (LocalDate datum : jedinstveniDatumi) {
            String formattedDate = datum.format(formatter);
            MenuItem item = new MenuItem(formattedDate);
            item.setOnAction(e -> izbornikDatumaMeni.setText(formattedDate));
            izbornikDatumaMeni.getItems().add(item);
        }
    }
    /**
     * Pokrece simulaciju kada se klikne na dugme "Start".
     */
    @FXML
    protected void onStartButtonClick(){
        startDugme.setVisible(false);
        new Thread(this::pokreniSimulaciju).start();
    }
    private static final DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    /**
     * Pokrece simulaciju kretanja vozila po mapi, ukljucujuci promene stanja vozila i azuriranje mape.
     */
    private void pokreniSimulaciju() {
        Map<LocalDateTime, List<Iznajmljivanje>> groupedByDateTime = iznajmljivanja.stream()
                .collect(Collectors.groupingBy(Iznajmljivanje::getDatumVrijemeIznajmljivanja, TreeMap::new, Collectors.toList()));

        for (Map.Entry<LocalDateTime, List<Iznajmljivanje>> entry : groupedByDateTime.entrySet()) {
            List<Thread> threads = new ArrayList<>();
            Platform.runLater(() -> naslov.setText(entry.getKey().format(dateFormatter2)));

            for (Iznajmljivanje iznajmljivanje : entry.getValue()) {
                iznajmljivanje.setIzvjestaj(izvjestaj);
                if(iznajmljivanje.isKvar()){
                    popuniTabeluKvarova(iznajmljivanje);
                    //continue;
                }
                Thread thread = new Thread(iznajmljivanje);
                threads.add(thread);
                thread.start();
            }

            // Čekanje da se sve niti završe
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }

            // Pauza od 5 sekundi između razlicitih vremena iznajmljivanja
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }

            Platform.runLater(() -> {
                for (Iznajmljivanje iznajmljivanje : entry.getValue()) {
                    iznajmljivanje.resetLastUpdatedCells();
                }
            });
        }
        Platform.runLater(() -> naslov.setText("KRAJ"));
        izvjestaj.serijalizujVozilaSaNajviseGubitaka(vozniPark);
    }
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    /**
     * Popunjava tabele u interfejsu sa podacima o vozilima iz voznog parka.
     *
     * @param vozniPark Objekat koji sadrzi listu svih vozila.
     */
    private void popuniTabelePrevoznihSredstava(VozniPark vozniPark){
        autoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        autoProizvodjac.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        autoModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        autoDatumNabavke.setCellValueFactory(new PropertyValueFactory<>("datumNabavke"));
        autoDatumNabavke.setCellFactory(new Callback<TableColumn<Automobil, LocalDateTime>, TableCell<Automobil, LocalDateTime>>() {
            @Override
            public TableCell<Automobil, LocalDateTime> call(TableColumn<Automobil, LocalDateTime> column) {
                return new TableCell<Automobil, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(dateFormatter.format(item));
                        }
                    }
                };
            }
        });
        autoCijenaNabavke.setCellValueFactory(new PropertyValueFactory<>("cijenaNabavke"));
        autoOpis.setCellValueFactory(new PropertyValueFactory<>("opis"));
        autoNivoBaterije.setCellValueFactory(new PropertyValueFactory<>("trenutniNivoBaterije"));

        bikeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        bikeProizvodjac.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        bikeModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        bikeCijenaNabavke.setCellValueFactory(new PropertyValueFactory<>("cijenaNabavke"));
        bikeDomet.setCellValueFactory(new PropertyValueFactory<>("autonomija"));
        bikeNivoBaterije.setCellValueFactory(new PropertyValueFactory<>("trenutniNivoBaterije"));

        trotId.setCellValueFactory(new PropertyValueFactory<>("id"));
        trotProizvodjac.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        trotModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        trotCijenaNabavke.setCellValueFactory(new PropertyValueFactory<>("cijenaNabavke"));
        trotMaxBrzina.setCellValueFactory(new PropertyValueFactory<>("maxBrzina"));
        trotNivoBaterije.setCellValueFactory(new PropertyValueFactory<>("trenutniNivoBaterije"));

        for (Map.Entry<String, Vozilo> entry : vozniPark.getVozila().entrySet()) {
            String key = entry.getKey();
            Vozilo vozilo = entry.getValue();
            key = key.substring(0, 1).toUpperCase();
            switch (key) {
                case "A":
                    autoTabela.getItems().add((Automobil) vozilo);
                    break;
                case "B":
                    bikeTabela.getItems().add((Bicikl) vozilo);
                    break;
                case "T":
                    trotTabela.getItems().add((Trotinet) vozilo);
                    break;
            }
        }
    }
    /**
     * Popunjava tabelu sa podacima o kvarovima vozila tokom simulacije.
     *
     * @param iznajmljivanje Objekat koji sadrži podatke o iznajmljivanju vozila.
     */
    private void popuniTabeluKvarova(Iznajmljivanje iznajmljivanje){
        kvarId.setCellValueFactory(new PropertyValueFactory<>("idVozila"));
        kvarVrijeme.setCellValueFactory(new PropertyValueFactory<>("datumVrijemeIznajmljivanja"));
        kvarVrijeme.setCellFactory(new Callback<TableColumn<Iznajmljivanje, LocalDateTime>, TableCell<Iznajmljivanje, LocalDateTime>>() {
            @Override
            public TableCell<Iznajmljivanje, LocalDateTime> call(TableColumn<Iznajmljivanje, LocalDateTime> column) {
                return new TableCell<Iznajmljivanje, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(dateFormatter2.format(item));
                        }
                    }
                };
            }
        });
        kvarVrsta.setCellValueFactory(new PropertyValueFactory<>("vrstaVozila"));
        kvarOpis.setCellValueFactory(new PropertyValueFactory<>("opisKvara"));

        kvarTabela.getItems().add(iznajmljivanje);

    }
    /**
     * Rukuje klikom na dugme za generisanje sumarnog izveštaja.
     * Omogucava korisniku da sacuva izvestaj u tekstualni fajl.
     */
    @FXML
    public void onSumarniIzvjestajButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sacuvaj sumarni izvjestaj");
        fileChooser.setInitialFileName("sumarni_izvjestaj.txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(sumarniIzvjestajButton.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(izvjestaj.generisiSumarniIzvjestaj());
                System.out.println("Sumarni izvjestaj je uspjesno sacuvan!");
            } catch (IOException e) {
                System.err.println("Greska prilikom cuvanja sumarnog izvjestaja: " + e.getMessage());
            }
        }
    }

    private final DateTimeFormatter datumFormat = DateTimeFormatter.ofPattern("dd_MM_yyyy");
    /**
     * Rukuje klikom na dugme za generisanje dnevnog izvestaja.
     * Omogucava korisniku da sacuva izvestaj za izabrani datum u tekstualni fajl.
     */
    @FXML
    public void onDnevniIzvjestajButtonClick() {
        String izabraniDatum = izbornikDatumaMeni.getText();
        if (izabraniDatum.equals("DATUM?")) {
            System.err.println("Molimo izaberite datum iz menija.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sacuvaj dnevni izvjestaj");
        fileChooser.setInitialFileName("dnevni_izvjestaj_" + izabraniDatum.formatted(datumFormat) + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(dnevniIzvjestajButton.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                LocalDate datum = LocalDate.parse(izabraniDatum, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                writer.write(izvjestaj.generisiDnevniIzvjestaj(datum));
                System.out.println("Dnevni izvjestaj je uspjesno sacuvan!");
            } catch (IOException e) {
                System.err.println("Greska prilikom cuvanja dnevnog izvjestaja: " + e.getMessage());
            }
        }
    }

    @FXML
    private ComboBox<String> vrstaVozilaComboBox;

    @FXML
    private Label voziloDetailsLabel;
    /**
     * Rukuje prikazom detalja o vozilu koje je donelo najvece gubitke po kategoriji.
     * Prikazuje podatke o vozilu i iznosu gubitka na interfejsu.
     */
    @FXML
    public void onPrikaziVoziloClick() {
        String vrstaVozila = vrstaVozilaComboBox.getValue();
        if (vrstaVozila != null) {
            String filePath = properties.getProperty("DODATNA_FUNKCIONALNOST_FILE_PATH") + File.separator + vrstaVozila + "_najvise_gubitaka.dat";
            try (FileInputStream fis = new FileInputStream(filePath);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Vozilo vozilo = (Vozilo) ois.readObject();
                voziloDetailsLabel.setText(vozilo.toString()+"\n" +
                        "Iznos gubitka: " + izracunajTrosakKvara(vozilo));

            } catch (IOException | ClassNotFoundException e) {
                //voziloDetailsLabel.setText("Greška prilikom deserijalizacije: " + e.getMessage());
                voziloDetailsLabel.setText("Odabrana vrsta vozila nije donijela nikakav gubitak!");
            }
        }
    }
    /**
     * Izracunava trosak kvara vozila na osnovu koeficijenta koji zavisi od vrste vozila.
     *
     * @param vozilo Objekat tipa Vozilo za koje se racuna trosak kvara.
     * @return Izracunati trosak kvara.
     */
    private double izracunajTrosakKvara(Vozilo vozilo) {
        double koeficijent = 0;
        String tipVozila = vozilo.getId().substring(0, 1).toUpperCase();

        switch (tipVozila) {
            case "A":
                koeficijent = Double.parseDouble(properties.getProperty("KOEF_KVARA_AUTO"));
                break;
            case "B":
                koeficijent = Double.parseDouble(properties.getProperty("KOEF_KVARA_BICIKL"));
                break;
            case "T":
                koeficijent = Double.parseDouble(properties.getProperty("KOEF_KVARA_TROTINET"));
                break;
        }

        return koeficijent * vozilo.getCijenaNabavke();
    }


}