package es.dam.repaso04.controllers;

import es.dam.repaso04.models.Coche;
import es.dam.repaso04.repositories.CochesRepository;
import es.dam.repaso04.services.StorageCSV;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AppController {
    CochesRepository cochesRepository = CochesRepository.getInstance();
    List<Coche> coches;

    @FXML
    public MenuBar menubar;
    @FXML
    public Menu application;
    @FXML
    public MenuItem cerrar;
    @FXML
    public Menu archivos;
    @FXML
    public MenuItem csvbutton;
    @FXML
    public MenuItem jsonbutton;
    @FXML
    public Menu informe;
    @FXML
    public MenuItem txtbutton;
    @FXML
    public TableView<Coche> cochestabla;
    @FXML
    public TableColumn<Coche, String> modelocolumn;
    @FXML
    public TableColumn<Coche, String> matriculacolumn;
    @FXML
    public TextField modelofield;
    @FXML
    public TextField matriculafield;
    @FXML
    public TextField colorfield;
    @FXML
    public Button nuevobutton;
    @FXML
    public Button editarbutton;
    @FXML
    public Button eliminarbutton;
    @FXML
    public Button limpiarbutton;

    public void initialize() {
        try {
            cochestabla.setItems(cochesRepository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        modelocolumn.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        matriculacolumn.setCellValueFactory(cellData -> cellData.getValue().matriculaProperty());

        cochestabla.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> onSelectedCoche(newValue));

        setVacio();
    }

    private void onSelectedCoche(Coche coche) {
        if(coche != null) {
            setDatos(coche);
        } else {
            setVacio();
        }
    }

    private void setVacio() {
        modelofield.setEditable(true);
        modelofield.setText("");

        matriculafield.setEditable(true);
        matriculafield.setText("");

        colorfield.setEditable(true);
        colorfield.setText("");
    }

    private void setDatos(Coche coche) {
        modelofield.setEditable(true);
        modelofield.setText(coche.getModelo());

        matriculafield.setEditable(true);
        matriculafield.setText(coche.getMatricula());

        colorfield.setEditable(true);
        colorfield.setText(coche.getColor());
    }


    public void onActionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Saliendo...");
        alert.setContentText("¿Estas seguro de que quieres salir?");

        Optional<ButtonType> boton = alert.showAndWait();
        if(boton.get() == ButtonType.OK) {
            cochesRepository.autoSave();
            Platform.exit();
        } else {
            alert.close();
        }
    }

    public void onActionCSVImport(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Apartado CSV");
        alert.setHeaderText("Importando CSV...");
        alert.setContentText("A continuación, eliga el archivo .csv del cuál exportar los datos.");
        alert.showAndWait();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Eliga el fichero CSV.");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documentos", "*.csv"));
        Path path = fileChooser.showOpenDialog(colorfield.getScene().getWindow()).toPath();

        try {
            cochesRepository.restore(path);
            cochestabla.setItems(cochesRepository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionJSONExport(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Apartado JSON");
        alert.setHeaderText("Exportando JSON...");
        alert.setContentText("A continuación, se creará una copia de seguridad en formato .json");
        alert.showAndWait();

        try {
            cochesRepository.backup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActionTXTInform(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Apartado API-STREAM");
        alert.setHeaderText("Creando informe...");
        alert.setContentText("Informe creado con éxito.");
        alert.showAndWait();

        FileOutputStream fos;
        ObjectOutputStream oos;

        fos = new FileOutputStream(System.getProperty("user.dir") + File.separator + "data" + File.separator + "txt" + File.separator + "informe.txt");
        oos = new ObjectOutputStream(fos);

        try {
            coches = cochesRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        var res1 = coches.stream()
                .filter(a -> a.getColor().equalsIgnoreCase("Negro"))
                .count();

        var res2 = coches.stream()
                .filter(a -> a.getColor().equalsIgnoreCase("Blanco"))
                .count();

        var res3 = coches.stream()
                .filter(a -> a.getModelo().equalsIgnoreCase("Mercedes"))
                .count();

        var res4 = coches.stream()
                .filter(a -> a.getColor().equalsIgnoreCase("Audi"))
                .count();

        String h = "=== ESTADÍSTICAS: COCHES ===" + "\n"
                + "Número de coches blancos: " + res2 + '.' + "\n"
                + "Número de coches negros: " + res1 + '.' + "\n"
                + "Número de coches Mercedes: " + res3 + '.' + "\n"
                + "Número de coches Audi: " + res4 + '.' + "\n";

        try {
            oos.writeBytes(h);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionNuevoCoche(ActionEvent actionEvent) {
        modelofield.setEditable(true);
        matriculafield.setEditable(true);
        colorfield.setEditable(true);

        Coche coche = new Coche(
                UUID.randomUUID().toString(),
                modelofield.getText(),
                matriculafield.getText(),
                colorfield.getText()
        );

        try {
            cochesRepository.save(coche);
            cochestabla.setItems(cochesRepository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionUpdateCoche(ActionEvent actionEvent) {
        modelofield.setEditable(true);
        matriculafield.setEditable(true);
        colorfield.setEditable(true);

        Coche coche = cochestabla.getSelectionModel().getSelectedItem();
        coche.setModelo(modelofield.getText());
        coche.setMatricula(matriculafield.getText());
        coche.setColor(colorfield.getText());

        try {
            cochesRepository.update(coche);
            cochestabla.setItems(cochesRepository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onActionDeleteCoche(ActionEvent actionEvent) {
        Coche coche = cochestabla.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminando alumno...");
        alert.setHeaderText("Esta a punto de borrar a " + coche.getModelo());
        alert.setContentText("¿Está seguro de que desea eliminar el alumno?");
        Optional<ButtonType> button = alert.showAndWait();
        if(button.get() == ButtonType.OK) {
            try {
                cochesRepository.delete(coche);
                cochestabla.setItems(cochesRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            alert.close();
        }
    }

    public void onActionClearCoche(ActionEvent actionEvent) {
        Coche coche = cochestabla.getSelectionModel().getSelectedItem();

        modelofield.clear();
        matriculafield.clear();
        colorfield.clear();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Limpiando datos del coche...");
        alert.setHeaderText("Esta a punto de resetear a " + coche.getModelo());
        alert.setContentText("¿Está seguro de que desea resetear el coche?");
        Optional<ButtonType> button = alert.showAndWait();
        if(button.get() == ButtonType.OK) {
            try {
                cochesRepository.clear(coche);
                cochestabla.setItems(cochesRepository.findAll());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            alert.close();
        }
    }
}