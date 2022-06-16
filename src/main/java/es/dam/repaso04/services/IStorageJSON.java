package es.dam.repaso04.services;

import es.dam.repaso04.dto.CocheDTO;

import java.util.List;

public interface IStorageJSON{
    void backUp(List<CocheDTO> cochesDTO);
}
