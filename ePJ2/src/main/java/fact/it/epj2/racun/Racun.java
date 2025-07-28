package fact.it.epj2.racun;

import fact.it.epj2.parser.Iznajmljivanje;
import fact.it.epj2.pomoc.Konfiguracija;
import fact.it.epj2.pomoc.Lokacija;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Klasa koja predstavlja racun za iznajmljivanje vozila.
 * Generise racun na osnovu informacija o iznajmljivanju i parametara iz konfiguracije.
 */
public class Racun {
    private String imeKorisnika;
    private String idVozila;
    private LocalDateTime datumVrijemeIznajmljivanja;
    private Lokacija lokacijaPreuzimanja;
    private Lokacija lokacijaOstavljanja;
    private long trajanjeKoriscenja; // u sekundama
    private boolean sirePodrucje;
    private boolean kvar;
    private boolean promocija;
    private static int brojIznajmljivanja = 0;
    private final Properties properties = Konfiguracija.getProperties();
    private Iznajmljivanje iznajmljivanje;
    private double osnovnaCijena;
    private double cijenaSaUdaljenoscu;
    private double popust;
    private double cijenaSaPopustom;
    private double cijenaSaPromocijom;
    private double ukupnaCijena;
    /**
     * Konstruktor klase Racun.
     *
     * @param iznajmljivanje Objekt klase Iznajmljivanje koji sadrzi sve relevantne informacije o iznajmljivanju vozila.
     */
    public Racun(Iznajmljivanje iznajmljivanje){
        this.iznajmljivanje = iznajmljivanje;
        this.imeKorisnika = iznajmljivanje.getImeKorisnika();
        this.idVozila = iznajmljivanje.getIdVozila();
        this.datumVrijemeIznajmljivanja = iznajmljivanje.getDatumVrijemeIznajmljivanja();
        this.lokacijaPreuzimanja = iznajmljivanje.getLokacijaPreuzimanja();
        this.lokacijaOstavljanja = iznajmljivanje.getLokacijaOstavljanja();
        this.trajanjeKoriscenja = iznajmljivanje.getTrajanjeKoriscenja();
        this.sirePodrucje = iznajmljivanje.isSirePodrucje();
        this.kvar = iznajmljivanje.isKvar();
        this.promocija = iznajmljivanje.isPromocija();
        ++brojIznajmljivanja;
    }

    private double vrijednostPromocije=0;
    /**
     * Vraca vrijednost promocije.
     *
     * @return Vrijednost promocije u dvostrukom formatu.
     */
    public double getVrijednostPromocije() {
        return vrijednostPromocije;
    }
    /**
     * Postavlja vrijednost promocije.
     *
     * @param vrijednostPromocije Vrijednost promocije koju treba postaviti.
     */
    public void setVrijednostPromocije(double vrijednostPromocije) {
        this.vrijednostPromocije = vrijednostPromocije;
    }
    /**
     * Generise racun na osnovu podataka o iznajmljivanju.
     * Racun se zapisuje u tekstualni fajl na putanji definisanoj u konfiguracionim fajlovima.
     */
    public void generisiRacun()  {

        String fileName = properties.getProperty("RACUNI_FILE_PATH") + File.separator +"R" + brojIznajmljivanja + "_" + idVozila + "_" + imeKorisnika + "_" + datumVrijemeIznajmljivanja.format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm")) + ".txt";
        osnovnaCijena = izracunajOsnovnuCijenu();
        cijenaSaUdaljenoscu = osnovnaCijena * (sirePodrucje ? Double.parseDouble(properties.getProperty("DISTANCE_WIDE")) : Double.parseDouble(properties.getProperty("DISTANCE_NARROW")));
        popust = (brojIznajmljivanja % 10 == 0) ? cijenaSaUdaljenoscu * Double.parseDouble(properties.getProperty("DISCOUNT")) / 100 : 0;
        cijenaSaPopustom = cijenaSaUdaljenoscu - popust;
        vrijednostPromocije = promocija ? cijenaSaUdaljenoscu * Double.parseDouble(properties.getProperty("DISCOUNT_PROM")) / 100 : 0;
        cijenaSaPromocijom = promocija ? cijenaSaPopustom - vrijednostPromocije : cijenaSaPopustom;
        ukupnaCijena = kvar ? 0 : cijenaSaPromocijom; //cijenaSaPopustom - cijenaSaPromocijom

        if(kvar){
            osnovnaCijena=0;
            cijenaSaUdaljenoscu=0;
            popust=0;
            cijenaSaPopustom=0;
            vrijednostPromocije=0;
            cijenaSaPromocijom=0;
            ukupnaCijena=0;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("R" + brojIznajmljivanja + "\n");
            writer.write("Racun za iznajmljivanje\n");
            writer.write("==================================\n");
            if(kvar) writer.write("DESIO SE KVAR! - cijena za plaćanje se nulira.\n");
            writer.write("Ime korisnika: " + imeKorisnika + "\n");
            if(iznajmljivanje.getVrstaVozila().equals("automobil")) writer.write("Dokumentacija korisnika: "+iznajmljivanje.getDokument()+" "+iznajmljivanje.getVozackaDozvola()+"\n");
            writer.write("ID vozila: " + idVozila + "\n");
            writer.write("Datum i vrijeme iznajmljivanja: " + datumVrijemeIznajmljivanja.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "\n");
            writer.write("Lokacija preuzimanja: " + lokacijaPreuzimanja + "\n");
            writer.write("Lokacija ostavljanja: " + lokacijaOstavljanja + "\n");
            writer.write("Trajanje korišćenja (sekunde): " + trajanjeKoriscenja + "\n");
            writer.write("Osnovna cijena: " + osnovnaCijena + "\n");
            writer.write("Cijena sa udaljenošću: " + cijenaSaUdaljenoscu + "\n");
            writer.write("Popust: " + popust + "\n");
            writer.write("Cijena sa popustom: " + cijenaSaPopustom + "\n");
            writer.write("Vrijednost promocije: " + vrijednostPromocije + "\n");
            writer.write("Cijena sa promocijom: " + cijenaSaPromocijom + "\n");
            writer.write("Ukupna cijena za plaćanje: " + ukupnaCijena + "\n");
        } catch (IOException e) {
            System.err.println("Greška prilikom pisanja racuna: " + e.getMessage());
        }
    }
    /**
     * Racuna osnovnu cijenu na osnovu vrste vozila i trajanja koriscenja.
     *
     * @return Osnovna cijena iznajmljivanja.
     */
    private double izracunajOsnovnuCijenu() {
        String tipVozila = idVozila.substring(0, 1).toUpperCase();
        double unitPrice = 0;

        switch (tipVozila) {
            case "A":
                unitPrice = Double.parseDouble(properties.getProperty("CAR_UNIT_PRICE"));
                break;
            case "B":
                unitPrice = Double.parseDouble(properties.getProperty("BIKE_UNIT_PRICE"));
                break;
            case "T":
                unitPrice = Double.parseDouble(properties.getProperty("SCOOTER_UNIT_PRICE"));
                break;
            default:
                throw new IllegalArgumentException("Nepoznat tip vozila: " + tipVozila);
        }

        return unitPrice * trajanjeKoriscenja;
    }
    /**
     * Vraca instancu objekta Iznajmljivanje povezanu sa ovim racunom.
     *
     * @return instanca objekta Iznajmljivanje.
     */
    public Iznajmljivanje getIznajmljivanje() {
        return iznajmljivanje;
    }
    /**
     * Postavlja instancu objekta Iznajmljivanje za ovaj racun.
     *
     * @param iznajmljivanje nova instanca objekta Iznajmljivanje.
     */
    public void setIznajmljivanje(Iznajmljivanje iznajmljivanje) {
        this.iznajmljivanje = iznajmljivanje;
    }
    /**
     * Vraca iznos popusta primijenjenog na ovaj racun.
     *
     * @return iznos popusta.
     */
    public double getPopust() {
        return popust;
    }
    /**
     * Vraca cijenu sa popustom.
     *
     * @return cijena sa popustom.
     */
    public double getCijenaSaPopustom() {
        return cijenaSaPopustom;
    }
    /**
     * Vraca cijenu sa primijenjenom promocijom.
     *
     * @return cijena sa promocijom.
     */
    public double getCijenaSaPromocijom() {
        return cijenaSaPromocijom;
    }
    /**
     * Vraca ukupnu cijenu za placanje na ovom racunu.
     *
     * @return ukupna cijena za placanje.
     */
    public double getUkupnaCijena() {
        return ukupnaCijena;
    }
    /**
     * Vraca cijenu sa uracunatom udaljenoscu.
     *
     * @return cijena sa udaljenoscu.
     */
    public double getCijenaSaUdaljenoscu() {
        return cijenaSaUdaljenoscu;
    }
    /**
     * Vraca ID vozila povezanog sa ovim racunom.
     *
     * @return ID vozila.
     */
    public String getIdVozila() {
        return idVozila;
    }
    /**
     * Provjerava da li je iznajmljivanje bilo u sirem podrucju grada.
     *
     * @return true ako je iznajmljivanje bilo u sirem podrucju, inace false.
     */
    public boolean isSirePodrucje() {
        return sirePodrucje;
    }
    /**
     * Provjerava da li je doslo do kvara tokom iznajmljivanja.
     *
     * @return true ako je doslo do kvara, inace false.
     */
    public boolean isKvar() {
        return kvar;
    }
    /**
     * Vraca datum i vrijeme iznajmljivanja.
     *
     * @return datum i vrijeme iznajmljivanja.
     */
    public LocalDateTime getDatumVrijemeIznajmljivanja() {
        return datumVrijemeIznajmljivanja;
    }
}
//public Racun(String imeKorisnika, String idVozila, LocalDateTime datumVrijemeIznajmljivanja,
//                 Lokacija lokacijaPreuzimanja, Lokacija lokacijaOstavljanja, long trajanjeKoriscenja,
//                 boolean sirePodrucje, boolean kvar, boolean promocija) {
//        this.imeKorisnika = imeKorisnika;
//        this.idVozila = idVozila;
//        this.datumVrijemeIznajmljivanja = datumVrijemeIznajmljivanja;
//        this.lokacijaPreuzimanja = lokacijaPreuzimanja;
//        this.lokacijaOstavljanja = lokacijaOstavljanja;
//        this.trajanjeKoriscenja = trajanjeKoriscenja;
//        this.sirePodrucje = sirePodrucje;
//        this.kvar = kvar;
//        this.promocija = promocija;
//        ++brojIznajmljivanja;
//    }
