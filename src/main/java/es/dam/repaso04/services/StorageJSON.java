package es.dam.repaso04.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.dam.repaso04.dto.CocheDTO;
import es.dam.repaso04.models.Coche;
import es.dam.repaso04.utils.LocalDateAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class StorageJSON implements IStorageJSON {

    public final String backupFile = System.getProperty("user.dir") + File.separator + "data" + File.separator + "json" + File.separator +"coches.json";

    public static StorageJSON instance;

    //SINGLETON
    public static StorageJSON getInstance() {
        if(instance == null) {
            instance = new StorageJSON();
        }
        return instance;
    }

    @Override
    public void backUp(List<CocheDTO> cochesDTO) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();

        try (PrintWriter f = new PrintWriter(new FileWriter(backupFile))) {
            f.println(gson.toJson(cochesDTO));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
