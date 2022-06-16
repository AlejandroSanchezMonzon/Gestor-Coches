package es.dam.repaso04.repositories;

import es.dam.repaso04.models.Coche;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public interface ICochesRepository extends ICRUDRepository<Coche> {
    void backup();

    void restore(Path path) throws SQLException;

    void autoSave();
}
