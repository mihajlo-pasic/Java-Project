package fact.it.epj2.parser;

import fact.it.epj2.izvjestaj.Izvjestaj;
import fact.it.epj2.pomoc.Lokacija;
import fact.it.epj2.racun.Racun;
import fact.it.epj2.vozila.Vozilo;
import fact.it.epj2.vozila.VozniPark;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Klasa koja predstavlja iznajmljivanje vozila. Implementira interfejs Runnable kako bi se simulacija iznajmljivanja mogla
 * pokrenuti u zasebnoj niti.
 */
public class Iznajmljivanje implements Runnable {
    private String imeKorisnika;
    private String idVozila;
    private LocalDateTime datumVrijemeIznajmljivanja;
    private Lokacija lokacijaPreuzimanja;
    private Lokacija lokacijaOstavljanja;
    private long trajanjeKoriscenja; // u sekundama
    private boolean sirePodrucje;
    private boolean kvar;
    private boolean promocija;
    private final GridPane mapa;
    private final VozniPark vozniPark;
    private String vrstaVozila;
    private String opisKvara;
    /**
     * Vraca referencu na objekat klase VozniPark povezan sa ovim iznajmljivanjem.
     *
     * @return objekat klase VozniPark
     */
    public VozniPark getVozniPark() {
        return vozniPark;
    }
    /**
     * Vraca vrstu vozila koje je iznajmljeno.
     *
     * @return vrsta vozila
     */
    public String getVrstaVozila() {
        return vrstaVozila;
    }
    /**
     * Postavlja vrstu vozila koje je iznajmljeno.
     *
     * @param vrstaVozila vrsta vozila
     */
    public void setVrstaVozila(String vrstaVozila) {
        this.vrstaVozila = vrstaVozila;
    }
    /**
     * Vraća opis kvara koji se dogodio tokom iznajmljivanja.
     *
     * @return opis kvara
     */
    public String getOpisKvara() {
        return opisKvara;
    }
    /**
     * Postavlja opis kvara koji se dogodio tokom iznajmljivanja.
     *
     * @param opisKvara opis kvara
     */
    public void setOpisKvara(String opisKvara) {
        this.opisKvara = opisKvara;
    }

    private String dokument="";
    private String vozackaDozvola="";

    /**
     * Konstruktor klase Iznajmljivanje.
     *
     * @param imeKorisnika             ime korisnika koji iznajmljuje vozilo
     * @param idVozila                 identifikacioni broj vozila
     * @param datumVrijemeIznajmljivanja datum i vrijeme iznajmljivanja
     * @param lokacijaPreuzimanja      lokacija na kojoj je vozilo preuzeto
     * @param lokacijaOstavljanja      lokacija na kojoj je vozilo ostavljeno
     * @param trajanjeKoriscenja       trajanje koriscenja vozila u sekundama
     * @param sirePodrucje             da li se vozilo koristi u sirem podrucju
     * @param kvar                     da li je doslo do kvara
     * @param promocija                da li je iznajmljivanje u okviru promocije
     * @param mapa                     GridPane mapa na kojoj se prikazuje kretanje vozila
     * @param vozniPark                objekat klase VozniPark koji sadrzi sva vozila
     */
    public Iznajmljivanje(String imeKorisnika, String idVozila, LocalDateTime datumVrijemeIznajmljivanja,
                          Lokacija lokacijaPreuzimanja, Lokacija lokacijaOstavljanja, long trajanjeKoriscenja,
                          boolean sirePodrucje, boolean kvar, boolean promocija, GridPane mapa, VozniPark vozniPark) {
        this.imeKorisnika = imeKorisnika;
        this.idVozila = idVozila;
        this.datumVrijemeIznajmljivanja = datumVrijemeIznajmljivanja;
        this.lokacijaPreuzimanja = lokacijaPreuzimanja;
        this.lokacijaOstavljanja = lokacijaOstavljanja;
        this.trajanjeKoriscenja = trajanjeKoriscenja;
        this.sirePodrucje = sirePodrucje;
        this.kvar = kvar;
        this.promocija = promocija;
        this.mapa = mapa;
        this.vozniPark = vozniPark;
        String tipVozila = idVozila.substring(0, 1).toUpperCase();
        switch (tipVozila) {
            case "A":
                vrstaVozila = "automobil";
                Random random = new Random();
                boolean isStraniDrzavljanin = random.nextBoolean();
                generisiDokumentacijuVozaca(isStraniDrzavljanin);
                break;
            case "B":
                vrstaVozila = "bicikl";
                break;
            case "T":
                vrstaVozila = "trotinet";
                break;
        }
        if(kvar){
            // Lista mogucih kvarova
            String[] opisiKvara = {
                    "Problemi sa upravljanjem!",
                    "Neidentifikovan kvar!",
                    "Problemi sa kocnicama!",
                    "Elektronika se pokvarila!",
                    "Gume su izduvane!"
            };

            // Nasumicno biranje jednog od opisa kvara
            Random random = new Random();
            int index = random.nextInt(opisiKvara.length);

            opisKvara = opisiKvara[index]; // Postavljanje opisa kvara
        }
    }
    /**
     * Vraca dokument korisnika, kao što su licna karta ili pasos.
     *
     * @return dokument korisnika
     */
    public String getDokument() {
        return dokument;
    }
    /**
     * Postavlja dokument korisnika, kao što su licna karta ili pasos.
     *
     * @param dokument dokument korisnika
     */
    public void setDokument(String dokument) {
        this.dokument = dokument;
    }
    /**
     * Vraca vozacku dozvolu korisnika.
     *
     * @return vozacka dozvola
     */
    public String getVozackaDozvola() {
        return vozackaDozvola;
    }
    /**
     * Postavlja vozacku dozvolu korisnika.
     *
     * @param vozackaDozvola vozacka dozvola
     */
    public void setVozackaDozvola(String vozackaDozvola) {
        this.vozackaDozvola = vozackaDozvola;
    }
    /**
     * Generise dokumentaciju vozaca (licna karta ili pasos) i vozacku dozvolu sa nasumicnim nizom karaktera.
     *
     * @param isStraniDrzavljanin da li je korisnik strani drzavljanin
     */
    private void generisiDokumentacijuVozaca(boolean isStraniDrzavljanin) {
        String nasumicniString = generisiNasumicniString(8);

        if (isStraniDrzavljanin) {
            dokument = "PS:" + nasumicniString;
        } else {
            dokument = "LK:" + nasumicniString;
        }

        vozackaDozvola = "VD:" + generisiNasumicniString(8);
    }
    /**
     * Generise nasumican niz od zadatog broja karaktera.
     *
     * @param duzina duzina nasumicnog niza
     * @return nasumican niz karaktera
     */
    private String generisiNasumicniString(int duzina) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < duzina; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
    /**
     * Vraca nivo baterije vozila.
     *
     * @return trenutni nivo baterije
     */
    public int getVoziloNivoBaterije(){
        Vozilo vozilo = vozniPark.pronadjiVozilo(idVozila);
        return vozilo.getTrenutniNivoBaterije();
    }

    private Izvjestaj izvjestaj=new Izvjestaj();
    /**
     * Postavlja izvestaj povezan sa ovim iznajmljivanjem.
     *
     * @param izvjestaj izvestaj
     */
    public void setIzvjestaj(Izvjestaj izvjestaj) {
        this.izvjestaj = izvjestaj;
    }
    /**
     * Vraca izvestaj povezan sa ovim iznajmljivanjem.
     *
     * @return izvestaj
     */
    public Izvjestaj getIzvjestaj() {
        return izvjestaj;
    }
    /**
     * Glavna metoda koja se izvrsava kada se pokrene nit. Simulira kretanje vozila na mapi,
     * praznjenje baterije i generisanje racuna nakon zavrsetka kretanja.
     */
@Override
public void run() {
    Vozilo vozilo = vozniPark.pronadjiVozilo(idVozila);
    Random rand = new Random();
    int trenutniNivoBaterije = rand.nextInt(50) + 50;
    vozilo.setTrenutniNivoBaterije(trenutniNivoBaterije);

    int startX = lokacijaPreuzimanja.getX();
    int startY = lokacijaPreuzimanja.getY();
    int endX = lokacijaOstavljanja.getX();
    int endY = lokacijaOstavljanja.getY();

    int currentX = startX;
    int currentY = startY;

    int steps = Math.abs(endX - startX) + Math.abs(endY - startY); // broj polja koji će vozilo preći
    long sleepTime = trajanjeKoriscenja * 1000 / steps; // vrijeme zadržavanja na jednom polju

    // Postavljanje vozila na pocetnu poziciju
    Platform.runLater(() -> postaviVoziloNaPocetnuLokaciju(startX, startY));
    try {
        Thread.sleep(sleepTime);
    } catch (InterruptedException e) {
        System.err.println(e.getMessage());
    }

    for (int i = 0; i <= steps; i++) {
        int previousX = currentX;
        int previousY = currentY;

        // Pomicanje korak po korak
        if (currentX != endX) {
            currentX += Integer.compare(endX, currentX);
        } else if (currentY != endY) {
            currentY += Integer.compare(endY, currentY);
        }

        final int fx = currentX;
        final int fy = currentY;
        final int fpx = previousX;
        final int fpy = previousY;

        // Pražnjenje baterije prilikom svakog koraka
        vozilo.prazniBateriju(1);

        // Ažuriranje mape
        Platform.runLater(() -> updateMap(fpx, fpy, fx, fy));

        try {
            if(fx == endX && fy == endY) {
                break; // Ako je vozilo stiglo na krajnju destinaciju, izlazi iz petlje - da se ne bi zadrzavalo na krajnem polju za sleepTime kao na ostalim
            }
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    Racun racun = new Racun(this);
    racun.generisiRacun();
    izvjestaj.dodajRacun(racun);
}

    /**
     * Postavlja vozilo na pocetnu lokaciju na mapi.
     *
     * @param startX pocetna X koordinata
     * @param startY pocetna Y koordinata
     */
    private void postaviVoziloNaPocetnuLokaciju(int startX, int startY){
        Label currentLabel = (Label) mapa.lookup("#" + startX + "," + startY);
        if (currentLabel != null) {
            int nivoBaterije = getVoziloNivoBaterije();
            currentLabel.setText(currentLabel.getText() + idVozila + ": " + nivoBaterije);
            String tipVozila = idVozila.substring(0, 1).toUpperCase();
            switch (tipVozila) {
                case "A":
                    currentLabel.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-alignment: center;");
                    break;
                case "B":
                    currentLabel.setStyle("-fx-background-color: red;-fx-text-fill: white; -fx-alignment: center;");
                    break;
                case "T":
                    currentLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-alignment: center;");
                    break;
            }
            lastUpdatedCells.add("#" + startX + "," + startY);
        }
    }

    /**
     * Azurira poziciju vozila na mapi i prazni bateriju pri svakom koraku.
     *
     * @param previousX prethodna X koordinata
     * @param previousY prethodna Y koordinata
     * @param currentX trenutna X koordinata
     * @param currentY trenutna Y koordinata
     */
    private void updateMap(int previousX, int previousY, int currentX, int currentY) {
        // Uklanjanje teksta iz prethodnog polja
        if (previousX >= 0 && previousY >= 0) {
            Label previousLabel = (Label) mapa.lookup("#" + previousX + "," + previousY);
            if (previousLabel != null) {
                previousLabel.setText(previousLabel.getText().replace(idVozila + ": " + (getVoziloNivoBaterije()+1), ""));
                //previousLabel.setText("");
                previousLabel.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-background-color: white");
            }
        }

        // Ažuriranje trenutnog polje
        Label currentLabel = (Label) mapa.lookup("#" + currentX + "," + currentY);
        if (currentLabel != null) {
            currentLabel.setText(currentLabel.getText() + idVozila + ": " + getVoziloNivoBaterije());
            String tipVozila = idVozila.substring(0, 1).toUpperCase();
            switch (tipVozila) {
                case "A":
                    currentLabel.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-alignment: center;");
                    break;
                case "B":
                    currentLabel.setStyle("-fx-background-color: red;-fx-text-fill: white; -fx-alignment: center;");
                    break;
                case "T":
                    currentLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-alignment: center;");
                    break;
            }
            lastUpdatedCells.add("#" + currentX + "," + currentY);
        }
    }

    private List<String> lastUpdatedCells = new ArrayList<>();
    /**
     * Resetuje sve celije koje su bile azurirane tokom simulacije na mapi.
     */
    public void resetLastUpdatedCells() {
        for (String cellId : lastUpdatedCells) {
            Label label = (Label) mapa.lookup(cellId);
            if (label != null) {
                label.setText("");
                label.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-background-color: white");
            }
        }
        lastUpdatedCells.clear();
    }

    // Getteri i Setteri
    /**
     * Vraca ime korisnika.
     *
     * @return ime korisnika
     */
    public String getImeKorisnika() {
        return imeKorisnika;
    }
    /**
     * Postavlja ime korisnika.
     *
     * @param imeKorisnika ime korisnika
     */
    public void setImeKorisnika(String imeKorisnika) {
        this.imeKorisnika = imeKorisnika;
    }
    /**
     * Vraca ID vozila.
     *
     * @return ID vozila
     */
    public String getIdVozila() {
        return idVozila;
    }
    /**
     * Postavlja ID vozila.
     *
     * @param idVozila ID vozila
     */
    public void setIdVozila(String idVozila) {
        this.idVozila = idVozila;
    }
    /**
     * Vraca datum i vrijeme iznajmljivanja.
     *
     * @return datum i vrijeme iznajmljivanja
     */
    public LocalDateTime getDatumVrijemeIznajmljivanja() {
        return datumVrijemeIznajmljivanja;
    }
    /**
     * Postavlja datum i vrijeme iznajmljivanja.
     *
     * @param datumVrijemeIznajmljivanja datum i vrijeme iznajmljivanja
     */
    public void setDatumVrijemeIznajmljivanja(LocalDateTime datumVrijemeIznajmljivanja) {
        this.datumVrijemeIznajmljivanja = datumVrijemeIznajmljivanja;
    }
    /**
     * Vraca lokaciju preuzimanja vozila.
     *
     * @return lokacija preuzimanja
     */
    public Lokacija getLokacijaPreuzimanja() {
        return lokacijaPreuzimanja;
    }
    /**
     * Postavlja lokaciju preuzimanja vozila.
     *
     * @param lokacijaPreuzimanja lokacija preuzimanja
     */
    public void setLokacijaPreuzimanja(Lokacija lokacijaPreuzimanja) {
        this.lokacijaPreuzimanja = lokacijaPreuzimanja;
    }
    /**
     * Vraca lokaciju ostavljanja vozila.
     *
     * @return lokacija ostavljanja
     */
    public Lokacija getLokacijaOstavljanja() {
        return lokacijaOstavljanja;
    }
    /**
     * Postavlja lokaciju ostavljanja vozila.
     *
     * @param lokacijaOstavljanja lokacija ostavljanja
     */
    public void setLokacijaOstavljanja(Lokacija lokacijaOstavljanja) {
        this.lokacijaOstavljanja = lokacijaOstavljanja;
    }
    /**
     * Vraca trajanje koriscenja vozila u sekundama.
     *
     * @return trajanje koriscenja u sekundama
     */
    public long getTrajanjeKoriscenja() {
        return trajanjeKoriscenja;
    }
    /**
     * Postavlja trajanje koriscenja vozila u sekundama.
     *
     * @param trajanjeKoriscenja trajanje koriscenja u sekundama
     */
    public void setTrajanjeKoriscenja(long trajanjeKoriscenja) {
        this.trajanjeKoriscenja = trajanjeKoriscenja;
    }
    /**
     * Vraca informaciju da li se vozilo koristi u sirem podrucju.
     *
     * @return true ako se koristi u sirem podrucju, inace false
     */
    public boolean isSirePodrucje() {
        return sirePodrucje;
    }
    /**
     * Postavlja informaciju da li se vozilo koristi u sirem podrucju.
     *
     * @param sirePodrucje true ako se koristi u sirem podrucju, inace false
     */
    public void setSirePodrucje(boolean sirePodrucje) {
        this.sirePodrucje = sirePodrucje;
    }
    /**
     * Vraca informaciju da li je doslo do kvara tokom iznajmljivanja.
     *
     * @return true ako je doslo do kvara, inace false
     */
    public boolean isKvar() {
        return kvar;
    }
    /**
     * Postavlja informaciju da li je doslo do kvara tokom iznajmljivanja.
     *
     * @param kvar true ako je doslo do kvara, inace false
     */
    public void setKvar(boolean kvar) {
        this.kvar = kvar;
    }
    /**
     * Vraca informaciju da li je iznajmljivanje u okviru promocije.
     *
     * @return true ako je u okviru promocije, inace false
     */
    public boolean isPromocija() {
        return promocija;
    }
    /**
     * Postavlja informaciju da li je iznajmljivanje u okviru promocije.
     *
     * @param promocija true ako je u okviru promocije, inace false
     */
    public void setPromocija(boolean promocija) {
        this.promocija = promocija;
    }
}

/*
    @Override
    public void run() {
        Vozilo vozilo = vozniPark.pronadjiVozilo(idVozila);
        Random rand = new Random();
        int trenutniNivoBaterije = rand.nextInt(50) + 50;
        vozilo.setTrenutniNivoBaterije(trenutniNivoBaterije);

        int startX = lokacijaPreuzimanja.getX();
        int startY = lokacijaPreuzimanja.getY();
        int endX = lokacijaOstavljanja.getX();
        int endY = lokacijaOstavljanja.getY();

        int currentX = startX;
        int currentY = startY;

        int steps = Math.abs(endX - startX) + Math.abs(endY - startY); //broj polja koji ce vozilo preci
        long sleepTime = trajanjeKoriscenja * 1000 / steps; // vrijeme zadržavanja na jednom polju

        //Platform.runLater(() -> postaviVoziloNaPocetnuLokaciju(startX, startY));
        for (int i = 0; i <= steps; i++) {
            int previousX = currentX;
            int previousY = currentY;

            if (currentX != endX) {
                currentX += Integer.compare(endX, currentX);
            } else if (currentY != endY) {
                currentY += Integer.compare(endY, currentY);
            }

            final int fx = currentX;
            final int fy = currentY;
            final int fpx = previousX;
            final int fpy = previousY;
            //System.out.println(idVozila+ ": (" + previousX + ", "+previousY+") -> (" + currentX + ", " + currentY + ")" );
            vozilo.prazniBateriju(1);
            Platform.runLater(() -> updateMap(fpx, fpy, fx, fy));
            try {
                if(fx==endX && fy==endY) continue;
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        Racun racun = new Racun(this);
        racun.generisiRacun();
        izvjestaj.dodajRacun(racun);
    }

    private List<String> lastUpdatedCells = new ArrayList<>();

    private void postaviVoziloNaPocetnuLokaciju(int startX, int startY){
        Label startLabel = (Label) mapa.lookup("#" + startX + startY);
        if (startLabel != null) {
            startLabel.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-background-color: white");
            startLabel.setText(startLabel.getText() + idVozila + ": " + getVoziloNivoBaterije());
        }
    }
    private void updateMap(int previousX, int previousY, int currentX, int currentY) {
        // Uklonite tekst iz prethodnog polja
        if (previousX >= 0 && previousY >= 0) {
            Label previousLabel = (Label) mapa.lookup("#" + previousX + previousY);
            if (previousLabel != null) {
                previousLabel.setStyle("-fx-alignment: center; -fx-border-color: black; -fx-background-color: white");
                previousLabel.setText(previousLabel.getText().replace(idVozila + ": " + (getVoziloNivoBaterije()+1), ""));
            }
        }

        // Ažurirajte trenutno polje
        Label currentLabel = (Label) mapa.lookup("#" + currentX + currentY);
        if (currentLabel != null) {
            currentLabel.setText(currentLabel.getText() + idVozila + ": " + getVoziloNivoBaterije());
            String tipVozila = idVozila.substring(0, 1).toUpperCase();
            switch (tipVozila) {
                case "A":
                    currentLabel.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-alignment: center;");
                    break;
                case "B":
                    currentLabel.setStyle("-fx-background-color: red;-fx-text-fill: white; -fx-alignment: center;");
                    break;
                case "T":
                    currentLabel.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-alignment: center;");
                    break;
            }
            lastUpdatedCells.add("#" + currentX + currentY);
        }
    }
*/
