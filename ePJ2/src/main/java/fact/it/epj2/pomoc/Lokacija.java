package fact.it.epj2.pomoc;
/**
 * Klasa koja predstavlja lokaciju sa koordinatama x i y u opsegu od 0 do 19.
 */
public class Lokacija {
    private int x;
    private int y;
    /**
     * Podrazumevani konstruktor koji kreira praznu lokaciju.
     */
    public Lokacija() {
    }
    /**
     * Konstruktor koji kreira lokaciju na osnovu stringa u formatu "x,y".
     *
     * @param lokacija String koji sadrži koordinate u formatu "x,y".
     * @throws IllegalArgumentException ako su koordinate van opsega (0-19).
     */
    public Lokacija(String lokacija) {
        // Split the string by comma
        String[] parts = lokacija.split(",");
        // Parse the parts to integers
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());

        // Check if coordinates are within the allowed range (0-19)
        if (x < 0 || x > 19 || y < 0 || y > 19) {
            throw new IllegalArgumentException("Koordinate moraju biti u opsegu 0-19. Dobijene koordinate: (" + x + ", " + y + ")");
        }

        // Assign to instance variables if valid
        this.x = x;
        this.y = y;
    }
    /**
     * Konstruktor koji kreira lokaciju na osnovu unetih x i y koordinata.
     *
     * @param x koordinata x.
     * @param y koordinata y.
     * @throws IllegalArgumentException ako su koordinate van opsega (0-19).
     */
    public Lokacija(int x, int y) {
        if (x < 0 || x > 19 || y < 0 || y > 19) {
            throw new IllegalArgumentException("Koordinate moraju biti u opsegu 0-19. Dobijene koordinate: (" + x + ", " + y + ")");
        }
        this.x = x;
        this.y = y;
    }
    /**
     * Vraca x koordinatu lokacije.
     *
     * @return x koordinata.
     */
    public int getX() {
        return x;
    }
    /**
     * Postavlja x koordinatu lokacije.
     *
     * @param x nova x koordinata.
     * @throws IllegalArgumentException ako je x van opsega (0-19).
     */
    public void setX(int x) {
        if (x < 0 || x > 19) {
            throw new IllegalArgumentException("Koordinate moraju biti u opsegu 0-19. Dobijena koordinata x: " + x);
        }
        this.x = x;
    }
    /**
     * Vraca y koordinatu lokacije.
     *
     * @return y koordinata.
     */
    public int getY() {
        return y;
    }
    /**
     * Postavlja y koordinatu lokacije.
     *
     * @param y nova y koordinata.
     * @throws IllegalArgumentException ako je y van opsega (0-19).
     */
    public void setY(int y) {
        if (y < 0 || y > 19) {
            throw new IllegalArgumentException("Koordinate moraju biti u opsegu 0-19. Dobijena koordinata y: " + y);
        }
        this.y = y;
    }
    /**
     * Vraca string reprezentaciju objekta Lokacija.
     *
     * @return string u formatu "{x=x, y=y}".
     */
    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
