package fact.it.epj2.vozila;


import java.time.LocalDateTime;

/**
 * Klasa koja predstavlja bicikl kao specificnu vrstu vozila.
 * Nasledjuje klasu {@link Vozilo} i dodaje dodatne atribute specificne za bicikle.
 */
public class Bicikl extends Vozilo{
    private static final long serialVersionUID = 1L;
    private int autonomija;
    /**
     * Konstruktor za kreiranje novog objekta klase Bicikl.
     *
     * @param id                Jedinstveni identifikator vozila.
     * @param proizvodjac       Proizvodjac bicikla.
     * @param model             Model bicikla.
     * @param cijenaNabavke     Cijena nabavke bicikla.
     * @param trenutniNivoBaterije Trenutni nivo baterije bicikla.
     * @param autonomija        Autonomija bicikla u kilometrima.
     */
    public Bicikl(String id, String proizvodjac, String model, int cijenaNabavke, int trenutniNivoBaterije, int autonomija) {
        super(id, proizvodjac, model, cijenaNabavke, trenutniNivoBaterije);
        this.autonomija = autonomija;
    }
    /**
     * Vraca domet bicikla.
     *
     * @return Autonomija bicikla.
     */
    public int getAutonomija() {
        return autonomija;
    }
    /**
     * Postavlja domet bicikla.
     *
     * @param autonomija Nova autonomija bicikla.
     */
    public void setAutonomija(int autonomija) {
        this.autonomija = autonomija;
    }
    /**
     * Vraca string reprezentaciju objekta klase Bicikl.
     *
     * @return String koji sadrzi informacije o biciklu.
     */
    @Override
    public String toString() {
        return "Bicikl{" +
                "id='" + getId() + '\'' +
                ", proizvodjac='" + getProizvodjac() + '\'' +
                ", model='" + getModel() + '\'' +
                ", cijenaNabavke=" + getCijenaNabavke() +
                ", trenutniNivoBaterije=" + getTrenutniNivoBaterije() +
                ", autonomija=" + autonomija +
                '}';
    }

}
