package es.dam.repaso04.dto;

import es.dam.repaso04.models.Coche;

import java.io.Serializable;

public class CocheDTO implements Serializable {
    private String uuid;
    private String modelo;
    private String matricula;
    private String color;

    public CocheDTO(Coche coche) {
        this.uuid = coche.getId();
        this.modelo = coche.getModelo();
        this.matricula = coche.getMatricula();
        this.color = coche.getColor();
    }

    public CocheDTO(String uuid, String modelo, String matricula, String color) {
        this.uuid = uuid;
        this.modelo = modelo;
        this.matricula = matricula;
        this.color = color;
    }

    public Coche fromDTO() {
        return new Coche(uuid, modelo, matricula, color);
    }

    public String toFile() {
        return uuid + ';' + modelo + ';' + matricula + ';'+ color + "\n";
    }
}
