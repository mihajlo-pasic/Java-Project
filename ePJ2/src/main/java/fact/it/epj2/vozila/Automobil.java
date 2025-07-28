package fact.it.epj2.vozila;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Klasa koja predstavlja automobil kao specificnu vrstu vozila.
 * Nasledjuje klasu {@link Vozilo} i dodaje dodatne atribute specificne za automobile.
 */
public class Automobil extends Vozilo {
    private static final long serialVersionUID = 1L;
    private LocalDateTime datumNabavke;
    private String opis;
    private String dokument;
    private String vozackaDozvola;

    /**
     * Konstruktor za kreiranje novog objekta klase Automobil.
     *
     * @param id                Jedinstveni identifikator vozila.
     * @param proizvodjac       Proizvodjac automobila.
     * @param model             Model automobila.
     * @param cijenaNabavke     Cijena nabavke automobila.
     * @param trenutniNivoBaterije Trenutni nivo baterije automobila.
     * @param datumNabavke      Datum nabavke automobila.
     * @param opis              Opis automobila.
     */
    public Automobil(String id, String proizvodjac, String model, int cijenaNabavke, int trenutniNivoBaterije, LocalDateTime datumNabavke, String opis) {
        super(id, proizvodjac, model, cijenaNabavke, trenutniNivoBaterije);
        this.datumNabavke = datumNabavke;
        this.opis = opis;
    }
    /**
     * Vraca datum nabavke automobila.
     *
     * @return Datum nabavke automobila.
     */
    public LocalDateTime getDatumNabavke() { return datumNabavke; }
    /**
     * Postavlja datum nabavke automobila.
     *
     * @param datumNabavke Novi datum nabavke automobila.
     */
    public void setDatumNabavke(LocalDateTime datumNabavke) {
        this.datumNabavke = datumNabavke;
    }
    /**
     * Vraca opis automobila.
     *
     * @return Opis automobila.
     */
    public String getOpis() {
        return opis;
    }
    /**
     * Postavlja opis automobila.
     *
     * @param opis Novi opis automobila.
     */
    public void setOpis(String opis) {
        this.opis = opis;
    }
    /**
     * Vraca dokument automobila (npr. pasos ili licna karta).
     *
     * @return Dokument automobila.
     */
    public String getDokument() {
        return dokument;
    }
    /**
     * Postavlja dokument automobila (npr. pasos ili licna karta).
     *
     * @param dokument Novi dokument automobila.
     */
    public void setDokument(String dokument) {
        this.dokument = dokument;
    }
    /**
     * Vraca vozacku dozvolu automobila.
     *
     * @return Vozacka dozvola automobila.
     */
    public String getVozackaDozvola() {
        return vozackaDozvola;
    }
    /**
     * Postavlja vozacku dozvolu automobila.
     *
     * @param vozackaDozvola Nova vozacka dozvola automobila.
     */
    public void setVozackaDozvola(String vozackaDozvola) {
        this.vozackaDozvola = vozackaDozvola;
    }

    /**
     * Vraca string reprezentaciju objekta klase Automobil.
     *
     * @return String koji sadrzi informacije o automobilu.
     */
    @Override
    public String toString() {
        return "Automobil{" +
                "id='" + getId() + '\'' +
                ", proizvodjac='" + getProizvodjac() + '\'' +
                ", model='" + getModel() + '\'' +
                ", cijenaNabavke=" + getCijenaNabavke() +
                ", trenutniNivoBaterije=" + getTrenutniNivoBaterije() +
                ", datumNabavke=" + datumNabavke +
                ", opis='" + opis + '\'' +
                '}';
    }

}
