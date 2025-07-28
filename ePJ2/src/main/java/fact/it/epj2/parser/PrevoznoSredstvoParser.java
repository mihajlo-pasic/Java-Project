package fact.it.epj2.parser;

import fact.it.epj2.vozila.Automobil;
import fact.it.epj2.vozila.Bicikl;
import fact.it.epj2.vozila.Trotinet;
import fact.it.epj2.vozila.Vozilo;
import javafx.util.converter.LocalDateStringConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
/**
 * Klasa PrevoznoSredstvoParser sluzi za parsiranje podataka o vozilima iz CSV fajla.
 */
public class PrevoznoSredstvoParser {
    /**
     * Ucitava vozila iz CSV fajla i vraca listu vozila.
     *
     * @param filePath putanja do CSV fajla.
     * @return lista vozila ucitanih iz fajla.
     */
    public static List<Vozilo> ucitajVozila(String filePath) {
        List<Vozilo> vozila = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); //za zaglavlje tabele
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 6) {
                    System.err.println("GRESKA: Nedovoljno podataka u redu - " + line);
                    continue;
                }

                try {
                    String id = values[0];
                    String proizvodjac = values[1];
                    String model = values[2];
                    int cijenaNabavke = Integer.parseInt(values[4]);

                    Random rand = new Random();
                    int trenutniNivoBaterije = rand.nextInt(50) + 50;
                    String tipVozila = values[8];

                    Vozilo vozilo = null;
                    switch (tipVozila.toLowerCase()) {
                        case "automobil":
                            if (values.length < 8) {
                                System.err.println("GRESKA: Nedovoljno podataka za automobil - " + line);
                                continue;
                            }
                            String datum = formatirajDatum(values[3]);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
                            Date datumNabavke1 = sdf.parse(datum);
                            // Korak 1: Konvertujte Date u Instant
                            Instant instant = datumNabavke1.toInstant();
                            // Korak 2: Koristite Instant za kreiranje LocalDateTime
                            LocalDateTime datumNabavke = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                            String opis = values[7];
                            vozilo = new Automobil(id, proizvodjac, model, cijenaNabavke, trenutniNivoBaterije, datumNabavke, opis);
                            break;
                        case "bicikl":
                            if (values.length < 7) {
                                System.err.println("GRESKA: Nedovoljno podataka za bicikl - " + line);
                                continue;
                            }
                            int autonomija = Integer.parseInt(values[5]);
                            vozilo = new Bicikl(id, proizvodjac, model, cijenaNabavke, trenutniNivoBaterije, autonomija);
                            break;
                        case "trotinet":
                            if (values.length < 7) {
                                System.err.println("GRESKA: Nedovoljno podataka za trotinet - " + line);
                                continue;
                            }
                            int maxBrzina = Integer.parseInt(values[6]);
                            vozilo = new Trotinet(id, proizvodjac, model, cijenaNabavke, trenutniNivoBaterije, maxBrzina);
                            break;
                        default:
                            System.err.println("GRESKA: Nepoznat tip vozila - " + line);
                            continue;
                    }

                    vozila.add(vozilo);
                } catch (Exception e) {
                    System.err.println("GRESKA: Nevalidni podaci u redu - " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("GRESKA: Problem sa citanjem fajla - " + e.getMessage());
        }

        return vozila;
    }
    /**
    * Formatira datum tako da dodaje nulu ispred jednocifrenih dana i mjeseci.
    *
    * @param datum string koji predstavlja datum u formatu "d.M.yyyy."
    *  @return formatiran datum u formatu "dd.MM.yyyy."
    */
    private static String formatirajDatum(String datum) {
        String[] delovi = datum.split("\\.");  // Razdvajamo string na dan, mjesec i godinu
        String dan = delovi[0].length() == 1 ? "0" + delovi[0] : delovi[0];
        String mesec = delovi[1].length() == 1 ? "0" + delovi[1] : delovi[1];
        String godina = delovi[2];

        return dan + "." + mesec + "." + godina + ".";
    }

}
