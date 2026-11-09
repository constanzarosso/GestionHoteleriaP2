package models;

import enums.Permiso;
import enums.TipoUsuario;

import java.util.HashSet;
import java.util.Set;

public class Recepcionista extends Usuario {

    public Recepcionista(int id, String nombreUsuario, String contrasenia){
        super(id, nombreUsuario, contrasenia);
        InicializarPermisos();
    }


    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.RECEPCIONISTA;
    }

    @Override
    public Set<Permiso> permisosIniciales() {
        return new HashSet<>(Set.of(
                Permiso.GESTIONAR_HABITACIONES,
                Permiso.GESTIONAR_RESERVAS,
                Permiso.HACER_CHECKIN_CHECKOUT,
                Permiso.VER_HABITACIONES,
                Permiso.CAMBIAR_ESTADO_HABITACION,
                Permiso.CARGAR_PASAJEROS,
                Permiso.REGISTRAR_CONSUMO,
                Permiso.SOLICITAR_RESERVAS
        ));
    }
}
