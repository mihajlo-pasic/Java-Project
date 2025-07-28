package fact.it.epj2.vozila;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Apstraktna klasa koja predstavlja osnovne karakteristike vozila.
 * Implementira interfejs {@link Serializable} za serijalizaciju objekata.
 */
public abstract class Vozilo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String proizvodjac;
    private String model;
    private int cijenaNabavke;
    private int trenutniNivoBaterije;

    /**
     * Konstruktor za kreiranje novog objekta klase Vozilo.
     *
     * @param id                Jedinstveni identifikator vozila.
     * @param proizvodjac       Proizvodjac vozila.
     * @param model             Model vozila.
     * @param cijenaNabavke     Cijena nabavke vozila.
     * @param trenutniNivoBaterije Trenutni nivo baterije vozila.
     */
    public Vozilo(String id, String proizvodjac, String model, int cijenaNabavke, int trenutniNivoBaterije) {
        this.id = id;
        this.proizvodjac = proizvodjac;
        this.model = model;
        this.cijenaNabavke = cijenaNabavke;
        this.trenutniNivoBaterije = trenutniNivoBaterije;
    }
    /**
     * Puni bateriju vozila za zadati iznos.
     *
     * @param amount Iznos za koji se baterija puni.
     */
    public void puniBateriju(int amount){
        this.trenutniNivoBaterije = Math.min(100, this.trenutniNivoBaterije + amount);
    }
    /**
     * Prazni bateriju vozila za zadati iznos.
     *
     * @param amount Iznos za koji se baterija prazni.
     */
    public void prazniBateriju(int amount){
        this.trenutniNivoBaterije = Math.max(0, this.trenutniNivoBaterije - amount);
    }
    /**
     * Vraca jedinstveni identifikator vozila.
     *
     * @return ID vozila.
     */
    public String getId() {
        return id;
    }
    /**
     * Postavlja jedinstveni identifikator vozila.
     *
     * @param id Novi ID vozila.
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Vraca proizvodjaca vozila.
     *
     * @return Proizvodjac vozila.
     */
    public String getProizvodjac() {
        return proizvodjac;
    }
    /**
     * Postavlja proizvodjaca vozila.
     *
     * @param proizvodjac Novi proizvodjac vozila.
     */
    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }
    /**
     * Vraca model vozila.
     *
     * @return Model vozila.
     */
    public String getModel() {
        return model;
    }
    /**
     * Postavlja model vozila.
     *
     * @param model Novi model vozila.
     */
    public void setModel(String model) {
        this.model = model;
    }
    /**
     * Vraca cijenu nabavke vozila.
     *
     * @return Cijena nabavke vozila.
     */
    public int getCijenaNabavke() {
        return cijenaNabavke;
    }
    /**
     * Postavlja cijenu nabavke vozila.
     *
     * @param cijenaNabavke Nova cijena nabavke vozila.
     */
    public void setCijenaNabavke(int cijenaNabavke) {
        this.cijenaNabavke = cijenaNabavke;
    }
    /**
     * Vraca trenutni nivo baterije vozila.
     *
     * @return Trenutni nivo baterije vozila.
     */
    public int getTrenutniNivoBaterije() {
        return trenutniNivoBaterije;
    }
    /**
     * Postavlja trenutni nivo baterije vozila.
     *
     * @param trenutniNivoBaterije Novi nivo baterije vozila.
     */
    public void setTrenutniNivoBaterije(int trenutniNivoBaterije) {
        this.trenutniNivoBaterije = trenutniNivoBaterije;
    }
    /**
     * Vraca string reprezentaciju objekta klase Vozilo.
     *
     * @return String koji sadrži informacije o vozilu.
     */
    @Override
    public String toString() {
        return "Vozilo{" +
                "id='" + id + '\'' +
                ", proizvodjac='" + proizvodjac + '\'' +
                ", model='" + model + '\'' +
                ", cijenaNabavke=" + cijenaNabavke +
                ", trenutniNivoBaterije=" + trenutniNivoBaterije +
                '}';
    }
}
