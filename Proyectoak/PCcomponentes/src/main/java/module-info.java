module pccomp.PCcomponentes {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;

    opens PCcomponentes to javafx.fxml;
    exports PCcomponentes;
    exports PCcomponentes.Login;
    opens PCcomponentes.Login to javafx.fxml;
    exports PCcomponentes.Productos;
    opens PCcomponentes.Productos to javafx.fxml;



}