package fact.it.epj2.vozila;


import java.time.LocalDateTime;
/**
 * Klasa koja predstavlja trotinet kao specificnu vrstu vozila.
 * Nasledjuje klasu {@link Vozilo} i dodaje dodatne atribute specificne za trotinete.
 */
public class Trotinet extends Vozilo{
    private static final long serialVersionUID = 1L;
    private int maxBrzina;
    /**
     * Konstruktor za kreiranje novog objekta klase Trotinet.
     *
     * @param id                Jedinstveni identifikator vozila.
     * @param proizvodjac       Proizvodjac trotineta.
     * @param model             Model trotineta.
     * @param cijenaNabavke     Cijena nabavke trotineta.
     * @param trenutniNivoBaterije Trenutni nivo baterije trotineta.
     * @param maxBrzina         Maksimalna brzina trotineta u km/h.
     */
    public Trotinet(String id, String proizvodjac, String model, int cijenaNabavke, int trenutniNivoBaterije, int maxBrzina) {
        super(id, proizvodjac, model, cijenaNabavke, trenutniNivoBaterije);
        this.maxBrzina = maxBrzina;
    }
    /**
     * Vraca maksimalnu brzinu trotineta u km/h.
     *
     * @return Maksimalna brzina trotineta.
     */
    public int getMaxBrzina() {
        return maxBrzina;
    }
    /**
     * Postavlja maksimalnu brzinu trotineta u km/h.
     *
     * @param maxBrzina Nova maksimalna brzina trotineta.
     */
    public void setMaxBrzina(int maxBrzina) {
        this.maxBrzina = maxBrzina;
    }
    /**
     * Vraca string reprezentaciju objekta klase Trotinet.
     *
     * @return String koji sadrzi informacije o trotinetu.
     */
    @Override
    public String toString() {
        return "Trotinet{" +
                "id='" + getId() + '\'' +
                ", proizvodjac='" + getProizvodjac() + '\'' +
                ", model='" + getModel() + '\'' +
                ", cijenaNabavke=" + getCijenaNabavke() +
                ", trenutniNivoBaterije=" + getTrenutniNivoBaterije() +
                ", maxBrzina=" + maxBrzina +
                '}';
    }

}
