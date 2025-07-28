package fact.it.epj2.vozila;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Klasa koja predstavlja vozni park, cuvajuci kolekciju vozila i omogucavajuci razlicite operacije nad njima.
 */
public class VozniPark {
    private Map<String, Vozilo> vozila;
    /**
     * Konstruktor koji inicijalizuje prazan vozni park.
     */
    public VozniPark() {
        this.vozila = new HashMap<>();
    }
    /**
     * Dodaje listu vozila u vozni park.
     * Ako vozilo sa istim ID-jem vec postoji, ispisuje gresku u konzoli.
     *
     * @param vozila Lista vozila koja treba dodati u vozni park.
     */
    public void dodajVozila(List<Vozilo> vozila) {
        for (Vozilo vozilo : vozila) {
            if (!this.vozila.containsKey(vozilo.getId())) {
                this.vozila.put(vozilo.getId(), vozilo);
            } else {
                System.err.println("GRESKA: Dupli ID vozila - " + vozilo.getId());
            }
        }
    }
    /**
     * Pronalazi vozilo u voznom parku na osnovu ID-a.
     *
     * @param id ID vozila koje treba pronaci.
     * @return Vozilo sa datim ID-jem, ili null ako ne postoji.
     */
    public Vozilo pronadjiVozilo(String id) {
        return this.vozila.get(id);
    }
    /**
     * Vraca mapu svih vozila u voznom parku.
     *
     * @return Mapa vozila, gde je kljuc ID vozila, a vrednost je objekat tipa Vozilo.
     */
    public Map<String, Vozilo> getVozila() {
        return vozila;
    }
    /**
     * Postavlja mapu vozila u voznom parku.
     *
     * @param vozila Mapa vozila koju treba postaviti.
     */
    public void setVozila(Map<String, Vozilo> vozila) {
        this.vozila = vozila;
    }
}
