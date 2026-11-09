package models;

import Excepciones.ElementoNoEncontradoException;
import enums.Permiso;

public class GestorUsuarios extends GestorGenerico<Usuario> {

    public GestorUsuarios() {
        super();
    }

    public Usuario buscarPorNombre(String nombreUsuario) throws ElementoNoEncontradoException {
        for(Usuario usuario : obtenerElementos()){
            if(usuario.getNombreUsuario().equalsIgnoreCase(nombreUsuario)){
                return usuario;
            }
        }
        throw new ElementoNoEncontradoException();
    }

    public boolean login(String nombreUsuario, String contrasenia){
        try{
            Usuario usuario = buscarPorNombre(nombreUsuario);
            return usuario.isActivo() && usuario.validarContraenia(contrasenia);
        }catch(ElementoNoEncontradoException e){
            return false;
        }
    }

    public void darDeBajaUsuario(int id) throws ElementoNoEncontradoException{
        Usuario usuario = buscarElemento(id);

        usuario.darDeBaja();
    }

    public void asignarPermisos(int id, Permiso permiso) throws ElementoNoEncontradoException{
        Usuario usuario = buscarElemento(id);
        usuario.agregarPermiso(permiso);
    }

    public void quitarPermisos(int id, Permiso permiso) throws ElementoNoEncontradoException{
        Usuario usuario = buscarElemento(id);
        usuario.quitarPermiso(permiso);
    }

    public void cambiarContrasenia(int id, String nuevaContrasenia) throws ElementoNoEncontradoException{
        Usuario usuario = buscarElemento(id);
        usuario.cambiarContrasenia(nuevaContrasenia);
    }
}
