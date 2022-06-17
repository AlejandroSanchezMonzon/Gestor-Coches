package es.dam.repaso04.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Coche {
    private String uuid = UUID.randomUUID().toString();
    private StringProperty modelo;
    private StringProperty matricula;
    private StringProperty color;

    public Coche(String modelo, String matricula, String color) {
        this.modelo = new SimpleStringProperty(modelo);
        this.matricula = new SimpleStringProperty(matricula);
        this.color = new SimpleStringProperty(color);
    }

    public Coche(String uuid, String modelo, String matricula, String color) {
        this.uuid = uuid;
        this.modelo = new SimpleStringProperty(modelo);
        this.matricula = new SimpleStringProperty(matricula);
        this.color = new SimpleStringProperty(color);
    }

    public Coche() {
    }

    public String getId() {
        return uuid;
    }

    public String getModelo() {
        return modelo.get();
    }

    public StringProperty modeloProperty() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo.set(modelo);
    }

    public String getMatricula() {
        return matricula.get();
    }

    public StringProperty matriculaProperty() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula.set(matricula);
    }

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    @Override
    public String toString() {
        return "Coche{" +
                "modelo=" + modelo.getValue() +
                ", matricula=" + matricula.getValue() +
                ", color=" + color.getValue() +
                '}';
    }
}
