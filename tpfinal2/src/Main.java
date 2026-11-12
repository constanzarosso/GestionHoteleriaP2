import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Validaciones.Teclado;
import enums.Permiso;
import models.Administrador;
import models.Hotel;
import models.Recepcionista;
import models.Usuario;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        //___________________________________________________ Inicio _____________________________________________//

        try {
            InicializadorBD.inicializar();


            Hotel hotel = new Hotel("Hotel UTN");
            int contador = 0;

            while (contador < 5) {
                try {
                    boolean cerrarSesion = false;
                    contador++;
                    System.out.println("\n============= LOGIN HOTEL UTN ===============");
                    String usuario = Teclado.leerNombreUsuario("Usuario: ");
                    String contrasenia = Teclado.leerContrasenia("Contraseña: ");

                    hotel.iniciarSesion(usuario, contrasenia);


                    while (!cerrarSesion) {
                        switch (hotel.getUsuarioActual().getTipoUsuario()) {
                            case ADMINISTRADOR:
                                System.out.println("\n===== MENU ADMINISTRADOR =====");
                                System.out.println("1. Gestionar usuarios");
                                System.out.println("2. Gestionar habitaciones");
                                System.out.println("3. Gestionar reservas");
                                System.out.println("4. Realizar backup");
                                System.out.println("5. Cambiar contraseña");
                                System.out.println("6. Cerrar sesión");


                                int opcionAdmin = Teclado.leerEntero("Opcion: ");

                                switch (opcionAdmin) {
                                    case 1:
                                            int opcionUsuario =0;

                                            while(opcionUsuario !=6) {
                                                System.out.println("\n===== GESTION DE USUARIOS ======");
                                                System.out.println("1. Crear Usuario");
                                                System.out.println("2. Listar Usuarios");
                                                System.out.println("3. Dar de Baja Usuario");
                                                System.out.println("4. Asignar Permisos");
                                                System.out.println("5. Quitar Permisos");
                                                System.out.println("6. Volver");

                                                opcionUsuario = Teclado.leerEntero("Opcion: ");
                                                switch (opcionUsuario) {
                                                    case 1:  //Crear Usuario

                                                        System.out.println("\n===== CREAR USUARIO ======");
                                                        System.out.println("1. Admininistrador");
                                                        System.out.println("2. Recepcionista");

                                                        int tipo = Teclado.leerEntero("Opcion: ");

                                                        if(tipo!=1 && tipo!=2){
                                                            System.out.println("Tipo de usuario incorrecto");
                                                            break;
                                                        }

                                                        String nombreUsuario = Teclado.leerNombreUsuario("Ingreso Nombre de Usuario: ");
                                                        String contraseniaUsuario = Teclado.leerContrasenia("Ingrese una Contraseña: ");

                                                        try{
                                                            Usuario nuevoUsuario;
                                                            int id = hotel.siguienteIdUsuario();

                                                            if(tipo==1){
                                                                nuevoUsuario = new Administrador(id, nombreUsuario, contraseniaUsuario);
                                                            }else{
                                                                nuevoUsuario = new Recepcionista(id, nombreUsuario,contraseniaUsuario);
                                                            }

                                                            hotel.crearUsuario(nuevoUsuario);
                                                            System.out.println("Usuario creado correctamente");
                                                        }catch (ElementoDuplicadoException | AccesoDenegadoException e){
                                                            System.out.println("No se pudo crear el usuario: "+e.getMessage());
                                                        } catch (PersistenciaException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                        break;

                                                    case 2:
                                                        System.out.println("\n ===== LISTADO DE USUARIOS =====");

                                                        try {
                                                            List<Usuario> listaUsuarios = hotel.listarUsuarios();

                                                            if(listaUsuarios.isEmpty()){
                                                                System.out.println("No hay usuarios registrados");

                                                            }else{
                                                                for(Usuario usu : listaUsuarios){
                                                                    System.out.println("--------------------");
                                                                    System.out.println("ID: "+usu.getId());
                                                                    System.out.println("Usuario: "+usu.getNombreUsuario());
                                                                    System.out.println("Tipo: "+usu.getTipoUsuario());
                                                                    System.out.println("Estado: "+(usu.isActivo() ? "Activo" : "Inactivo"));
                                                                    // Ternario ? si es true da activo : si es falso da Inactivo
                                                                }
                                                                System.out.println("------------------");
                                                            }

                                                        }catch (AccesoDenegadoException e){
                                                            System.out.println(e.getMessage());
                                                        }
                                                        break;

                                                    case 3:
                                                        System.out.println("\n====== DAR DE BAJA USUARIO ======");

                                                        try {
                                                            int id = Teclado.leerEntero("Ingrese ID del usuario: ");
                                                            hotel.darDeBajaUsuario(id);

                                                            System.out.println("Usuario se dio de baja con exito.");
                                                        }catch (ElementoNoEncontradoException |
                                                                AccesoDenegadoException | PersistenciaException e){
                                                            System.out.println("No se pudo dar de baja al usuario. "+e.getMessage() );
                                                        }
                                                        break;

                                                    case 4:
                                                        System.out.println("\n ===== ASIGNAR PERMISOS =====");
                                                        try {
                                                            int id = Teclado.leerEntero("Ingrese ID del usuario: ");
                                                            System.out.println("Permisos disponibles: ");
                                                            Permiso[] permisos = Permiso.values();

                                                            for(int i = 0 ; i<permisos.length; i++){    //Muestra los permisos enum
                                                                System.out.println((i+1)+". "+permisos[i]);
                                                            }

                                                            int opcionPermiso = Teclado.leerEntero("Opcion: ");

                                                            if(opcionPermiso<1 || opcionPermiso > permisos.length){
                                                                System.out.println("Opcion incorrecta");
                                                                break;
                                                            }

                                                            Permiso permiso = permisos[opcionPermiso-1];
                                                            hotel.asignarPermisos(id, permiso);
                                                            System.out.println("Permiso asignado con exito. ");

                                                        }catch (ElementoNoEncontradoException |
                                                                AccesoDenegadoException | PersistenciaException e){
                                                            System.out.println("No se pudo asignar los permisos. "+ e.getMessage() );
                                                        }
                                                        break;

                                                    case 5:
                                                        System.out.println("\n===== QUITAR PERMISOS =======");
                                                        try {
                                                            int id = Teclado.leerEntero("Ingrese ID del usuario: ");
                                                            System.out.println("\n Permisos disponibles: ");
                                                            Permiso[] permisos = Permiso.values();

                                                            for(int i=0 ; i<permisos.length; i++){
                                                                System.out.println((i+1)+". "+permisos[i]);
                                                            }

                                                            int opcionPermiso = Teclado.leerEntero("Opcion: ");
                                                            if(opcionPermiso<1 || opcionPermiso > permisos.length){
                                                                System.out.println("Opcion incorrecta");
                                                                break;
                                                            }

                                                            Permiso permiso = permisos[opcionPermiso-1];
                                                            hotel.quitarPermiso(id, permiso);
                                                            System.out.println("Permiso quitado con exito. ");
                                                        }catch (ElementoNoEncontradoException |
                                                                AccesoDenegadoException | PersistenciaException e){
                                                            System.out.println("No se pudo quitar los permisos. "+ e.getMessage() );
                                                        }
                                                        break;

                                                    case 6:
                                                        System.out.println("Volviendo al menu de administrador. ");
                                                        break;

                                                        default:
                                                            break;
                                                }
                                            }

                                        break;

                                    case 2:
                                        System.out.println("Gestionar habitaciones");
                                        break;

                                    case 3:
                                        System.out.println("Gestionar reservas");
                                        break;

                                    case 4:
                                        System.out.println("Realizar backup");
                                        break;

                                    case 5:
                                        System.out.println("\n ====== CAMBIAR CONTRASEÑA =====");
                                        try {
                                            String actual = Teclado.leerContrasenia("Ingrese la contraseña actual: ");
                                            String nueva = Teclado.leerContrasenia("Ingrese la nueva contraseña: ");
                                            String confirmar = Teclado.leerContrasenia("Confirme la nueva contraseña: ");

                                            if(!nueva.equals(confirmar)){
                                                System.out.println("Las contraseñas no coinciden");
                                                break;
                                            }

                                            hotel.cambiarContrasenia(actual, nueva);
                                            System.out.println("Contraseña cambiada con exito.");

                                        }catch (ElementoNoEncontradoException | ContraseniaInvalidaException |
                                                PersistenciaException |
                                                AccesoDenegadoException e){
                                            System.out.println("Error al cambiar la contraseña: "+e.getMessage());
                                        }

                                        break;
                                    case 6:
                                        cerrarSesion = true;
                                        contador = 0;
                                        break;

                                    default:
                                        System.out.println("Opción incorrecta");
                                }
                                break;


                            case RECEPCIONISTA:
                                System.out.println("Bienvenido recepcionista");


                                System.out.println("\n ====== MENU RECEPCIONISTA =====");
                                System.out.println("1. Ver Habitaciones");
                                System.out.println("2. Gestionar Reservas");
                                System.out.println("3. Registrar Pasajeros");
                                System.out.println("4. Check-in / Check-out");
                                System.out.println("5. Registrar Consumos");
                                System.out.println("6. Cerrar Sesion");

                                int opcionRecepcion = Teclado.leerEntero("Opcion: ");

                                switch (opcionRecepcion) {
                                    case 1:
                                        System.out.println("Ver Habitaciones");
                                        break;

                                    case 2:
                                        System.out.println("Registrar Pasajeros");
                                        break;

                                    case 3:
                                        System.out.println("Registrar Pasajeroi");
                                        break;

                                    case 4:
                                        System.out.println("Checkin Checkout");
                                        break;

                                    case 5:
                                        System.out.println("Registrar Consumos");
                                        break;

                                    case 6:
                                        System.out.println("Cerrar sesion");
                                        cerrarSesion = true;
                                        contador = 0;
                                        break;

                                    default:
                                        System.out.println("Opcion incorrecta");
                                        break;
                                }

                                break;
                        }

                    }

                    hotel.cerrarSesion();

                } catch (CredencialesInvalidasException e){
                    contador++;

                    System.out.println(e.getMessage());
                }catch (UsuarioInactivoException e){
                    contador++;

                    System.out.println(e.getMessage());
                } catch (ContraseniaInvalidaException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("\n========================================" +
                    "\nExceso de intentos permitidos para iniciar sesion, contacte al administrador." +
                    "\n========================================");

        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        }
    }

}