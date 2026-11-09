package models;

import Excepciones.CredencialesInvalidasException;
import Excepciones.ElementoNoEncontradoException;
import Excepciones.UsuarioInactivoException;
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

    public Usuario login(String nombreUsuario, String contrasenia)throws CredencialesInvalidasException, UsuarioInactivoException {

        Usuario usuario;
        try{
            usuario = buscarPorNombre(nombreUsuario);

        }catch(ElementoNoEncontradoException e){
           throw new CredencialesInvalidasException();
        }

        if (!usuario.isActivo()) {
            throw new UsuarioInactivoException();
        }

        if(!usuario.validarContrasenia(contrasenia)){
            throw new CredencialesInvalidasException();
        }
        return usuario;
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
