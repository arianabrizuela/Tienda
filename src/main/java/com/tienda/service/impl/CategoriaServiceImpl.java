package com.tienda.service.impl;

import com.tienda.dao.CategoriaDao;
import com.tienda.domain.Categoria;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tienda.service.CategoriaService;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    
    //La anotacion autowired crea un unico objeto mientras se ejecuta la app
    @Autowired
    private CategoriaDao categoriaDao;
    
    public List<Categoria> getCategorias(boolean activos){
        var lista=categoriaDao.findAll();
        if(activos){ //se deben eliminar los que no estan activos
            lista.removeIf(e -> !e.isActivo());
                       //de la lista elimine los que no estan activos
        }
        return lista;
    }
}
