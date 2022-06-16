package es.dam.repaso04.repositories;

import es.dam.repaso04.dto.CocheDTO;
import es.dam.repaso04.managers.DataBaseManager;
import es.dam.repaso04.models.Coche;
import es.dam.repaso04.services.StorageCSV;
import es.dam.repaso04.services.StorageJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CochesRepository implements ICochesRepository{

    DataBaseManager db = DataBaseManager.getInstance();
    StorageJSON storageJSON = StorageJSON.getInstance();
    StorageCSV storageCSV = StorageCSV.getInstance();

    ObservableList<Coche> repository = FXCollections.observableArrayList();

    private static CochesRepository instance;

    //SINGLETON
    public static CochesRepository getInstance() {
        if (instance == null) {
            instance = new CochesRepository();
        }
        return instance;
    }

    public CochesRepository() {
    }

    @Override
    public ObservableList<Coche> findAll() throws SQLException {
        String sql = "SELECT * FROM coches";
        db.open();
        var res = db.select(sql).orElseThrow(() -> new SQLException("Error al compilar todos los coches."));

        repository.clear();

        while(res.next()) {
            repository.add(
                    new Coche(
                            res.getString("id"),
                            res.getString("modelo"),
                            res.getString("matricula"),
                            res.getString("color")
                    )
            );
        }
        db.close();
        if(repository.isEmpty()) {
            System.out.println("La lista está vacía.");
        }

        return repository;
    }

    @Override
    public void save(Coche coche) throws SQLException {
        String sql = "INSERT INTO coches(id, modelo, matricula, color) VALUES(?, ?, ?, ?)";
        db.open();
        var res = db.insert(sql, coche.getId(), coche.getModelo(), coche.getMatricula(), coche.getColor())
                .orElseThrow(() -> new SQLException("Imposible añadir un nuevo coche."));
        db.close();

        repository.add(coche);
    }

    @Override
    public void update(Coche coche) throws SQLException {
        String sql = "UPDATE coches SET modelo = ?, matricula = ?, color = ? WHERE id = ?";
        db.open();
        var res = db.update(sql, coche.getModelo(), coche.getMatricula(), coche.getColor(), coche.getId());
        db.close();

        repository.set(repository.indexOf(coche), coche);
    }

    @Override
    public void delete(Coche coche) throws SQLException {
        String sql = "DELETE FROM coches WHERE id = ?";
        db.open();
        var res = db.delete(sql, coche.getId());
        db.close();

        repository.remove(coche);
    }

    @Override
    public void clear(Coche coche) throws SQLException {
        String sql = "UPDATE coches SET modelo = ?, matricula = ?, color = ? WHERE id = ?";
        db.open();
        db.update(sql, "---", "---", "---", coche.getId());

        repository.set(repository.indexOf(coche), coche);
    }

    @Override
    public void backup() {
        List<CocheDTO> cochesDTO = repository.stream().map(CocheDTO::new).toList();
        storageJSON.backUp(cochesDTO);
    }

    @Override
    public void restore(Path path) throws SQLException {
        List<CocheDTO> cochesDTO = storageCSV.restore(path);
        repository.clear();

        String sql = "DELETE FROM coches";
        db.open();
        db.delete(sql);
        db.close();

        cochesDTO.forEach(c -> {
            try {
                save(c.fromDTO());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void autoSave() {
        List<CocheDTO> cochesDTO = repository.stream().map(CocheDTO::new).toList();
        storageCSV.autoSave(cochesDTO, false);
    }
}
