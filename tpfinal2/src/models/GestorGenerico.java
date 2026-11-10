package models;

import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import interfaces.Identificable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorGenerico<E extends Identificable> {
    private Map<Integer, E> elementos;

    public GestorGenerico() {
        this.elementos = new HashMap<>();
    }


    //PREGUNTAR AL PROFE//
    public GestorGenerico(Map<Integer, E> elementos) {
        this.elementos = elementos;
    }

    public void agregarElemento(E unElemento) throws ElementoDuplicadoException {
        if (elementos.containsKey(unElemento.getId())) {
            throw new ElementoDuplicadoException();
        }
        elementos.put(unElemento.getId(), unElemento);
    }

    public E buscarElemento(int id) throws ElementoNoEncontradoException {
        E unElemento = elementos.get(id);
        if (unElemento == null) {
            throw new ElementoNoEncontradoException();
        }
        return unElemento;
    }

    public boolean eliminarElemento(int id) throws ElementoNoEncontradoException{
        if(!elementos.containsKey(id)){
            throw new ElementoNoEncontradoException();
        }
        return elementos.remove(id) != null;
    }

    public List<E> obtenerElementos() {
        return new ArrayList<>(elementos.values());
    }

    public int cantidadElementos() {
        return elementos.size();
    }

    public int siguienteId() {
        int mayor = 0;
        for (Integer id : elementos.keySet()) {
            if (id > mayor) {
                mayor = id;
            }
        }
        return mayor + 1;
    }
}
