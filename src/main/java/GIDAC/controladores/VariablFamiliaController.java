package GIDAC.controladores;


import GIDAC.modelo.CatalogoEspoch;
import GIDAC.modelo.CatalogoOrganizacion;
import GIDAC.modelo.EquivalenciaVariable;
import GIDAC.modelo.VariableUnidadMedida;
import GIDAC.modelo.ValorPermitido;
import GIDAC.modelo.Variable;
import GIDAC.modelo.VariableFamilia;
import GIDAC.modelo.VariableFamiliaDifusion;
import GIDAC.servicios.UnidadMedidaVariableService;
import GIDAC.servicios.VariableFamiliaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/variable-familia")
@CrossOrigin("*")
public class VariablFamiliaController {

    
    @Autowired
    private VariableFamiliaService service;
    

    @PostMapping("/guardar")
    public Object guardar(@RequestBody VariableFamilia oC) throws JsonProcessingException{
//        oC.setIdVariable(oC.getVariable().getIdVariable());
//        oC.setIdUnidadMedida(oC.getUnidadMedida().getIdUnidadMedida());
        return service.guardar(oC);    
    }
    
    @PutMapping("/actualizar")
    public Object actualizar(@RequestBody VariableFamilia oC) throws JsonProcessingException{
//        oC.setIdVariable(oC.getVariable().getIdVariable());
//        oC.setIdUnidadMedida(oC.getUnidadMedida().getIdUnidadMedida());
        return service.guardar(oC);    
    }
    
    @GetMapping("/obtener/{idVariable}/{idFamilia}")
    public Object obtenerVigente(@PathVariable String idVariable,@PathVariable Integer idFamilia)
    {    
        return service.buscarPorId(idVariable, idFamilia, true);
    }
    
    @GetMapping("/listar-equivalencia-variable-vigentes")
    public List<VariableFamilia> listarVigentes()
    {    
        return service.buscarTodos(true);
    }
    
    @GetMapping("/listar-equivalencia-variable-eliminados")
    public List<VariableFamilia> listarEliminados()
    {    
        return service.buscarTodos(false);
    }
    
    
    @DeleteMapping("/eliminar/{idVariable}/{idFamilia}")
    public void eliminar(@PathVariable String idVariable,@PathVariable Integer idFamilia)
    {   
        VariableFamilia oC=(VariableFamilia) service.buscarPorId(idVariable, idFamilia, true);
        oC.setVigencia(false);
        service.guardar(oC);
    }
    
    
     @GetMapping("/listar-familias-difusion")
    public List<VariableFamiliaDifusion> listarFamiliasDifucion()
    {
        List<Object[]> listaObject= service.listarFamiliasDifusion();
        List<VariableFamiliaDifusion> listaFamilia = new ArrayList<>();
        for (Object[] objeto : listaObject) {
            VariableFamiliaDifusion variable = new VariableFamiliaDifusion();

            variable.setIdFamilia((Integer) objeto[0]);
            variable.setDescripcion((String) objeto[1]);
            listaFamilia.add(variable);
        }
        return listaFamilia;  
    }
    
    
    
    
}
