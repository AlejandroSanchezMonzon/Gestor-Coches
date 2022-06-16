package es.dam.repaso04.services;

import es.dam.repaso04.dto.CocheDTO;
import es.dam.repaso04.models.Coche;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StorageCSV implements IStorageCSV<CocheDTO>{
    public final String restoreField = System.getProperty("user.dir") + File.separator + "data" + File.separator + "csv" + File.separator +"coches.csv";
    public static StorageCSV instance;

    //SINGLETON
    public static StorageCSV getInstance() {
        if(instance == null) {
            instance = new StorageCSV();
        }

        return instance;
    }

    @Override
    public List<CocheDTO> restore(Path path) {
        File fichero = null;
        BufferedReader f = null;
        List<CocheDTO> coches = new ArrayList<>();

        try {
            fichero = new File(restoreField);
            f = new BufferedReader(new FileReader(restoreField));

            String linea;

            while((linea = f.readLine()) != null) {
                coches.add(getCoche(linea));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return coches;
    }

    private CocheDTO getCoche(String linea) {
        String[] campos = linea.split(";");
        String id = campos[0];
        String modelo = campos[1];
        String matricula = campos[2];
        String color = campos[3];

        return new CocheDTO(id, modelo, matricula, color);
    }

    @Override
    public void autoSave(List<CocheDTO> cochesDTO, boolean append) {
        File fichero = null;
        PrintWriter f = null;

        try {
            fichero = new File(restoreField);
            f = new PrintWriter(new FileWriter(restoreField, append));

            for(CocheDTO c : cochesDTO) {
                f.print(c.toFile());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (f != null) {
                f.close();
            }
        }
    }

}
