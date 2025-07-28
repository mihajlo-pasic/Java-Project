package fact.it.epj2.izvjestaj;

import fact.it.epj2.pomoc.Konfiguracija;
import fact.it.epj2.racun.Racun;
import fact.it.epj2.vozila.Vozilo;
import fact.it.epj2.vozila.VozniPark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Klasa Izvjestaj generise razlicite izvestaje na osnovu podataka o transakcijama iznajmljivanja vozila.
 */
public class Izvjestaj {
    private final List<Racun> racuni;
    /**
     * Konstruktor za klasu Izvjestaj.
     * Inicijalizuje prazan spisak racuna.
     */
    public Izvjestaj() {
        this.racuni = new ArrayList<>();
    }
    /**
     * Dodaje novi racun u spisak racuna.
     *
     * @param racun racun koji se dodaje
     */
    public void dodajRacun(Racun racun) {
        this.racuni.add(racun);
    }
    /**
     * Generise sumarni izveštaj koji sadrzi ukupne podatke o prihodima, troskovima i porezima.
     *
     * @return string koji sadrzi sumarni izvestaj
     */
    public String generisiSumarniIzvjestaj() {
        double ukupanPrihod = racuni.stream().mapToDouble(Racun::getUkupnaCijena).sum();
        double ukupanPopust = racuni.stream().mapToDouble(Racun::getPopust).sum();
        double ukupnoPromocije = racuni.stream().mapToDouble(Racun::getVrijednostPromocije).sum();
        double ukupanIznosUzih = racuni.stream()
                .filter(r -> !r.isSirePodrucje())
                .mapToDouble(Racun::getUkupnaCijena)
                .sum();
        double ukupanIznosSirih = racuni.stream()
                .filter(Racun::isSirePodrucje)
                .mapToDouble(Racun::getUkupnaCijena)
                .sum();
        double ukupanIznosZaOdrzavanje = ukupanPrihod * 0.2;
        double ukupanIznosZaPopravke = racuni.stream()
                .filter(Racun::isKvar)
                .mapToDouble(this::izracunajTrosakKvara)
                .sum();
        double ukupniTroskovi = ukupanPrihod * 0.2;
        double ukupanPorez = (ukupanPrihod - ukupanIznosZaOdrzavanje - ukupanIznosZaPopravke - ukupniTroskovi) * 0.1;

        StringBuilder sb = new StringBuilder();
        sb.append("Sumarni izvještaj:\n");
        sb.append("Ukupan prihod: ").append(ukupanPrihod).append("\n");
        sb.append("Ukupan popust: ").append(ukupanPopust).append("\n");
        sb.append("Ukupno promocije: ").append(ukupnoPromocije).append("\n");
        sb.append("Ukupan iznos u užem dijelu grada: ").append(ukupanIznosUzih).append("\n");
        sb.append("Ukupan iznos u širem dijelu grada: ").append(ukupanIznosSirih).append("\n");
        sb.append("Ukupan iznos za održavanje: ").append(ukupanIznosZaOdrzavanje).append("\n");
        sb.append("Ukupan iznos za popravke: ").append(ukupanIznosZaPopravke).append("\n");
        sb.append("Ukupni troškovi kompanije: ").append(ukupniTroskovi).append("\n");
        sb.append("Ukupan porez: ").append(ukupanPorez).append("\n");

        return sb.toString();
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    /**
     * Generise dnevni izvestaj za odredjeni datum.
     *
     * @param datum datum za koji se generise izvestaj
     * @return string koji sadrzi dnevni izvestaj
     */
    public String generisiDnevniIzvjestaj(LocalDate datum) {
        List<Racun> racuniZaDan = racuni.stream()
                .filter(racun -> racun.getDatumVrijemeIznajmljivanja().toLocalDate().equals(datum))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("Dnevni izvještaj za datum: ").append(datum.format(dateFormatter)).append("\n");

        double ukupanPrihod = racuniZaDan.stream().mapToDouble(Racun::getUkupnaCijena).sum();
        double ukupanPopust = racuniZaDan.stream().mapToDouble(Racun::getPopust).sum();
        double ukupnoPromocije = racuniZaDan.stream().mapToDouble(Racun::getVrijednostPromocije).sum();
        double ukupanIznosUzih = racuniZaDan.stream()
                .filter(r -> !r.isSirePodrucje())
                .mapToDouble(Racun::getUkupnaCijena)
                .sum();
        double ukupanIznosSirih = racuniZaDan.stream()
                .filter(Racun::isSirePodrucje)
                .mapToDouble(Racun::getUkupnaCijena)
                .sum();
        double ukupanIznosZaOdrzavanje = ukupanPrihod * 0.2;
        double ukupanIznosZaPopravke = racuniZaDan.stream()
                .filter(Racun::isKvar)
                .mapToDouble(this::izracunajTrosakKvara)
                .sum();

        sb.append("Ukupan prihod: ").append(ukupanPrihod).append("\n");
        sb.append("Ukupan popust: ").append(ukupanPopust).append("\n");
        sb.append("Ukupno promocije: ").append(ukupnoPromocije).append("\n");
        sb.append("Ukupan iznos u užem dijelu grada: ").append(ukupanIznosUzih).append("\n");
        sb.append("Ukupan iznos u širem dijelu grada: ").append(ukupanIznosSirih).append("\n");
        sb.append("Ukupan iznos za održavanje: ").append(ukupanIznosZaOdrzavanje).append("\n");
        sb.append("Ukupan iznos za popravke: ").append(ukupanIznosZaPopravke).append("\n");

        return sb.toString();
    }

    private final Properties properties = Konfiguracija.getProperties();
    /**
     * Izracunava trosak kvara na osnovu koeficijenta i cene nabavke vozila.
     *
     * @param racun racun za koji se racuna trosak kvara
     * @return izracunati trosak kvara
     */
    private double izracunajTrosakKvara(Racun racun) {
        double koeficijent = 0;
        String tipVozila = racun.getIdVozila().substring(0, 1).toUpperCase();

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

        return koeficijent * racun.getIznajmljivanje().getVozniPark().pronadjiVozilo(racun.getIdVozila()).getCijenaNabavke();
    }

    /**
     * Pronalazi vozila koja su prouzrokovala najvece gubitke po vrsti.
     *
     * @param vozniPark vozni park koji sadrzi sva vozila
     * @return mapa koja sadrzi vrstu vozila kao kljuc i vozilo sa najvecim gubicima kao vrednost
     */
    public Map<String, Vozilo> pronadjiVozilaSaNajviseGubitaka(VozniPark vozniPark) {
        Map<String, Double> gubiciPoVozilu = new HashMap<>();

        for (Racun racun : racuni) {
            if (racun.isKvar()) {
                String idVozila = racun.getIdVozila();
                double trosakKvara = izracunajTrosakKvara(racun);
                gubiciPoVozilu.put(idVozila, gubiciPoVozilu.getOrDefault(idVozila, 0.0) + trosakKvara);
            }
        }

        Map<String, Vozilo> najvecigubiciPoVrsti = new HashMap<>();
        Map<String, Double> maxGubiciPoVrsti = new HashMap<>();

        for (Map.Entry<String, Double> entry : gubiciPoVozilu.entrySet()) {
            String idVozila = entry.getKey();
            double gubitak = entry.getValue();

            Vozilo vozilo = vozniPark.pronadjiVozilo(idVozila);

            if (vozilo != null) {
                String vrstaVozila = vozilo.getClass().getSimpleName();
                if (!maxGubiciPoVrsti.containsKey(vrstaVozila) || gubitak > maxGubiciPoVrsti.get(vrstaVozila)) {
                    maxGubiciPoVrsti.put(vrstaVozila, gubitak);
                    najvecigubiciPoVrsti.put(vrstaVozila, vozilo);
                }
            }
        }

        return najvecigubiciPoVrsti;
    }
    /**
     * Serijalizuje vozila sa najvecim gubicima po vrsti u binarne fajlove.
     *
     * @param vozniPark vozni park koji sadrzi sva vozila
     */
    public void serijalizujVozilaSaNajviseGubitaka(VozniPark vozniPark) {
        Map<String, Vozilo> vozilaSaNajviseGubitaka = pronadjiVozilaSaNajviseGubitaka(vozniPark);

        for (Map.Entry<String, Vozilo> entry : vozilaSaNajviseGubitaka.entrySet()) {
            String vrstaVozila = entry.getKey();
            Vozilo vozilo = entry.getValue();

            String filePath = properties.getProperty("DODATNA_FUNKCIONALNOST_FILE_PATH") + File.separator + vrstaVozila + "_najvise_gubitaka.dat";
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(vozilo);
                System.out.println("Podaci o vozilu sa najvise gubitaka za " + vrstaVozila + " su sacuvani.");
            } catch (IOException e) {
                System.err.println("Greska prilikom serijalizacije: " + e.getMessage());
            }
        }
    }

}
