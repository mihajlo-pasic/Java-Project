module fact.it.epj2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens fact.it.epj2.vozila to javafx.base;
    opens fact.it.epj2 to javafx.fxml;
    opens fact.it.epj2.parser to javafx.base;

    exports fact.it.epj2;
    exports fact.it.epj2.vozila;
}