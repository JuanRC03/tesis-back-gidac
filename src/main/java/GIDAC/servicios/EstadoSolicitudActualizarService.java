/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package GIDAC.servicios;


import GIDAC.modelo.EstadoSolicitudActualizar;
import java.util.List;

/**
 *
 * @author My Notebook
 */
public interface EstadoSolicitudActualizarService<T>{
    public T guardar(T objeto);
    public T buscarPorId(Integer id);
    public List<T> buscarTodos();
    public void eliminar(Integer id);
}
