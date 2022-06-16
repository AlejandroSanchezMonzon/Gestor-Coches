module es.dam.repaso04 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires lombok;
    requires java.sql;


    opens es.dam.repaso04 to javafx.fxml;
    exports es.dam.repaso04;


    opens es.dam.repaso04.controllers to javafx.fxml;
    exports es.dam.repaso04.controllers;

    opens es.dam.repaso04.models to com.google.gson;
    exports es.dam.repaso04.models;

    opens es.dam.repaso04.dto to com.google.gson;
    exports es.dam.repaso04.dto;
}