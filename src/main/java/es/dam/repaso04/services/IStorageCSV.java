package es.dam.repaso04.services;

import java.nio.file.Path;
import java.util.List;

public interface IStorageCSV<T> {
    List<T> restore(Path path);

    void autoSave(List<T> lista, boolean append);
}
