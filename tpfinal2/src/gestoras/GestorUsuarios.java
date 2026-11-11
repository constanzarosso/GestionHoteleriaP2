package gestoras;

import Excepciones.ExcepcionesBD.BDCredencialesInvalidasException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Excepciones.ExcepcionesBD.BDUsuarioInactivoException;
import enums.Permiso;
import models.Usuario;

import java.util.List;

public class GestorUsuarios extends GestorGenerico<Usuario> {

    private GestorUsuarioBD gestorUsuarioBD;

    public GestorUsuarios() {
        super();
        this.gestorUsuarioBD = new GestorUsuarioBD();
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

    public void darDeBajaUsuario(int id) throws ElementoNoEncontradoException, PersistenciaException {
        Usuario usuario = buscarElemento(id);
        usuario.darDeBaja();
        gestorUsuarioBD.actualizar(usuario);
    }

    public void asignarPermisos(int id, Permiso permiso) throws ElementoNoEncontradoException, PersistenciaException {
        Usuario usuario = buscarElemento(id);
        usuario.agregarPermiso(permiso);
        gestorUsuarioBD.actualizar(usuario);
    }

    public void quitarPermisos(int id, Permiso permiso) throws ElementoNoEncontradoException, PersistenciaException {
        Usuario usuario = buscarElemento(id);
        usuario.quitarPermiso(permiso);
        gestorUsuarioBD.actualizar(usuario);
    }

    public void cambiarContrasenia(int id, String nuevaContrasenia) throws ElementoNoEncontradoException, PersistenciaException {
        Usuario usuario = buscarElemento(id);
        usuario.cambiarContrasenia(nuevaContrasenia);
        gestorUsuarioBD.actualizar(usuario);
    }

    public void cargarUsuarios() throws PersistenciaException, ElementoDuplicadoException {
        List<Usuario> usuarios = gestorUsuarioBD.listar();

        for(Usuario usuario : usuarios){
            try {
                agregarElemento(usuario);
            }catch (ElementoDuplicadoException e){
                throw new PersistenciaException(e.getMessage() );
            }
        }
    }

    public void registrarUsuario(Usuario usuario) throws ElementoDuplicadoException, PersistenciaException {
        agregarElemento(usuario);
        gestorUsuarioBD.guardar(usuario);
    }
}
