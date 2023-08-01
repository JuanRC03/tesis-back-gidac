package GIDAC.servicios.impl;


import GIDAC.modelo.CatalogoEspoch;
import GIDAC.repositorios.CatalogoEspochRepository;
import GIDAC.servicios.CatalogoEspochService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CatalogoEspochServiceImpl implements CatalogoEspochService {

    @Autowired
    private CatalogoEspochRepository repository;

    @Override
    public CatalogoEspoch guardar(Object objeto) {
        CatalogoEspoch oA=(CatalogoEspoch) objeto;
        return repository.save(oA);
    }

    @Override
    public CatalogoEspoch buscarPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List buscarTodos() {
        return repository.findAll();
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List buscarPorVigencia(Boolean vigencia) {
        return repository.findByVigencia(vigencia);
    }

}
