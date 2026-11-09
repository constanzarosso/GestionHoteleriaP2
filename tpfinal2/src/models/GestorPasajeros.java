package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorPasajeros extends GestorGenerico<Pasajero> {

    public GestorPasajeros() {
        super();
    }

    public GestorPasajeros(Map<Integer, Pasajero> elementos) {
        super(elementos);
    }

    public List<Pasajero> buscarPorApellido(String apellido) {
        List<Pasajero> encontrados = new ArrayList<>();

        for (Pasajero pasajero : obtenerElementos()) {
            if (pasajero.getApellido().equalsIgnoreCase(apellido)) {
                encontrados.add(pasajero);
            }
        }

        return encontrados;
    }
}


