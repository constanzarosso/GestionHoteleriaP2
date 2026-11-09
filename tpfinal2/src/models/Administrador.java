package models;

import enums.Permiso;
import enums.TipoUsuario;

import java.util.HashSet;
import java.util.Set;

public class Administrador extends Usuario {

    public Administrador(int id, String nombreUsuario, String contrasenia) {
        super(id, nombreUsuario, contrasenia);

        //Asigna el conjunto de permisos
        InicializarPermisos();
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.ADMINISTRADOR;
    }

    @Override
    public Set<Permiso> permisosIniciales() {
        return new HashSet<>(Set.of(Permiso.values()));
    }

}
