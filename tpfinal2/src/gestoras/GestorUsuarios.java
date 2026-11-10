package gestoras;

import Excepciones.ExcepcionesBD.BDCredencialesInvalidasException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Excepciones.ExcepcionesBD.BDUsuarioInactivoException;
import enums.Permiso;
import models.Usuario;

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

    public Usuario login(String nombreUsuario, String contrasenia)throws BDCredencialesInvalidasException, BDUsuarioInactivoException {

        Usuario usuario;
        try{
            usuario = buscarPorNombre(nombreUsuario);

        }catch(ElementoNoEncontradoException e){
           throw new BDCredencialesInvalidasException();
        }

        if (!usuario.isActivo()) {
            throw new BDUsuarioInactivoException();
        }

        if(!usuario.validarContrasenia(contrasenia)){
            throw new BDCredencialesInvalidasException();
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
