package fact.it.epj2.parser;

import fact.it.epj2.vozila.VozniPark;
import fact.it.epj2.pomoc.Lokacija;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Klasa za parsiranje podataka o iznajmljivanjima iz CSV fajla.
 */
public class IznajmljivanjeParser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    /**
     * Formatira datum i vrijeme iz formata sa jednocifrenim danom ili mjesecom u ispravan format.
     *
     * @param datumIVreme datum i vrijeme u formatu "d.M.yyyy HH:mm"
     * @return formatirani datum i vrijeme u formatu "dd.MM.yyyy HH:mm"
     */
    private static String formatirajDatumIVreme(String datumIVreme) {
        // Prvo razdvajamo datum i vrijeme
        String[] delovi = datumIVreme.split(" ");
        String datum = delovi[0];
        String vreme = delovi[1];

        // Razdvajamo dan, mjesec i godinu
        String[] datumDelovi = datum.split("\\.");
        String dan = datumDelovi[0].length() == 1 ? "0" + datumDelovi[0] : datumDelovi[0];
        String mesec = datumDelovi[1].length() == 1 ? "0" + datumDelovi[1] : datumDelovi[1];
        String godina = datumDelovi[2];

        // Vraćamo formatirani datum i vrijeme
        return dan + "." + mesec + "." + godina + " " + vreme;
    }

    /**
     * Ucitava podatke o iznajmljivanjima iz CSV fajla.
     *
     * @param filePath   putanja do CSV fajla
     * @param vozniPark  objekat klase VozniPark koji sadrži podatke o vozilima
     * @param mapa       GridPane mapa koja se koristi za prikaz kretanja vozila
     * @return lista objekata klase Iznajmljivanje koji predstavljaju podatke o iznajmljivanjima
     */
    public static List<Iznajmljivanje> ucitajIznajmljivanja(String filePath, VozniPark vozniPark, GridPane mapa) {
        List<Iznajmljivanje> iznajmljivanja = new ArrayList<>();
        Set<String> jedinstveniZapisi = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); //za zaglavlje tabele
            while ((line = br.readLine()) != null) {
                int expectedCommas = 10; // 9 zareza za 10 kolona
                int actualCommas = line.length() - line.replace(",", "").length();
                if (actualCommas != expectedCommas) {
                    System.err.println("GRESKA: Neispravan format zapisa - " + line);
                    continue;
                }
                String[] values = line.split(",");
                if (values.length != 10) {
                    System.err.println("GRESKA: Neispravan broj podataka u redu - " + line);
                    continue;
                }

                try {
                    String datumIVrijeme = formatirajDatumIVreme(values[0]);
                    LocalDateTime datumVrijemeIznajmljivanja = LocalDateTime.parse(datumIVrijeme, formatter);
                    String imeKorisnika = values[1];
                    String idVozila = values[2];

                    // Provjera da li vozilo postoji u voznom parku
                    if (vozniPark.pronadjiVozilo(idVozila) == null) {
                        System.err.println("GRESKA: Vozilo sa ID " + idVozila + " ne postoji u voznom parku - " + line);
                        continue;
                    }

                    // Provjera da li se vec koristi isto vozilo u istom trenutku
                    String jedinstveniKljuc = idVozila + "_" + datumVrijemeIznajmljivanja.format(formatter);
                    if (jedinstveniZapisi.contains(jedinstveniKljuc)) {
                        System.err.println("GRESKA: Vozilo sa ID " + idVozila + " je već zabilježeno za datum i vrijeme " + datumVrijemeIznajmljivanja.format(formatter) + " - " + line);
                        continue;
                    }
                    jedinstveniZapisi.add(jedinstveniKljuc);

                    int x1 = Integer.parseInt(values[3].replace("\"","")); // da se izbaci " viska
                    int y1 = Integer.parseInt(values[4].replace("\"",""));
                    int x2 = Integer.parseInt(values[5].replace("\"",""));
                    int y2 = Integer.parseInt(values[6].replace("\"",""));
                    Lokacija lokacijaPreuzimanja = new Lokacija(x1,y1);
                    Lokacija lokacijaOstavljanja = new Lokacija(x2,y2);

                    long trajanjeKoriscenja = Long.parseLong(values[7]);
                    boolean sirePodrucje = isOutsideCentralArea(lokacijaPreuzimanja) || isOutsideCentralArea(lokacijaOstavljanja);

                    boolean kvar;
                    if (values[8].equalsIgnoreCase("da")) {
                        kvar = true;
                    } else if (values[8].equalsIgnoreCase("ne")) {
                        kvar = false;
                    } else {
                        throw new IllegalArgumentException("GRESKA: Nevalidna vrednost za kvar - " + values[8]);
                    }

                    boolean promocija;
                    if (values[9].equalsIgnoreCase("da")) {
                        promocija = true;
                    } else if (values[9].equalsIgnoreCase("ne")) {
                        promocija = false;
                    } else {
                        throw new IllegalArgumentException("GRESKA: Nevalidna vrednost za promociju - " + values[9]);
                    }

                    Iznajmljivanje iznajmljivanje = new Iznajmljivanje(imeKorisnika, idVozila, datumVrijemeIznajmljivanja,
                            lokacijaPreuzimanja, lokacijaOstavljanja, trajanjeKoriscenja, sirePodrucje, kvar, promocija, mapa, vozniPark);

                    iznajmljivanja.add(iznajmljivanje);
                } catch (Exception e) {
                    System.err.println("GRESKA: Nevalidni podaci u redu - " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("GRESKA: Problem sa citanjem fajla - " + e.getMessage());
        }

        return iznajmljivanja;
    }
    /**
     * Proverava da li se lokacija nalazi izvan centralne oblasti grada.
     *
     * @param lokacija objekat klase Lokacija
     * @return true ako je lokacija izvan centralne oblasti, false ako nije
     */
    private static boolean isOutsideCentralArea(Lokacija lokacija) {
        int x = lokacija.getX();
        int y = lokacija.getY();
        return !(x > 4 && x < 15 && y > 4 && y < 15);
    }
}
