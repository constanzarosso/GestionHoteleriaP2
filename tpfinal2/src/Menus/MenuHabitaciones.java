package Menus;

import Excepciones.ExcepcionesBD.AccesoDenegadoException;
import Excepciones.ExcepcionesBD.PersistenciaException;
import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Validaciones.Teclado;
import enums.EEstadoHabitacion;
import models.Habitacion;
import models.HabitacionDoble;
import models.HabitacionSimple;
import models.HabitacionSuite;
import models.Hotel;

import java.util.List;

public final class MenuHabitaciones {

    private MenuHabitaciones() {
    }

    public static void menuAdministrador(Hotel hotel) {
        int opcion = 0;

        while (opcion != 6) {
            System.out.println("\n===== GESTION DE HABITACIONES =====");
            System.out.println("1. Agregar habitacion");
            System.out.println("2. Modificar precio");
            System.out.println("3. Marcar no disponible");
            System.out.println("4. Habilitar habitacion");
            System.out.println("5. Listar habitaciones");
            System.out.println("6. Volver");

            opcion = Teclado.leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    agregarHabitacion(hotel);
                    break;
                case 2:
                    modificarPrecio(hotel);
                    break;
                case 3:
                    marcarNoDisponible(hotel);
                    break;
                case 4:
                    habilitarHabitacion(hotel);
                    break;
                case 5:
                    listarHabitaciones(hotel);
                    break;
                case 6:
                    System.out.println("Volviendo al menu de administrador.");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }
        }
    }

    public static void menuRecepcionista(Hotel hotel) {
        listarHabitaciones(hotel);
    }

    private static void agregarHabitacion(Hotel hotel) {
        System.out.println("\n===== AGREGAR HABITACION =====");
        System.out.println("1. Simple");
        System.out.println("2. Doble");
        System.out.println("3. Suite");
        int tipo = Teclado.leerOpcion("Tipo: ", 1, 3);

        int numero = Teclado.leerEntero("Numero: ");
        int piso = Teclado.leerEntero("Piso: ");
        int capacidad = Teclado.leerEntero("Capacidad: ");
        double precio = Teclado.leerMonto("Precio base: ");

        Habitacion habitacion;
        if (tipo == 1) {
            habitacion = new HabitacionSimple(numero, piso, capacidad, precio);
        } else if (tipo == 2) {
            boolean camasSeparadas = Teclado.leerSiNo("¿Camas separadas? (S/N): ");
            habitacion = new HabitacionDoble(numero, piso, capacidad, precio, camasSeparadas);
        } else {
            boolean jacuzzi = Teclado.leerSiNo("¿Tiene jacuzzi? (S/N): ");
            habitacion = new HabitacionSuite(numero, piso, capacidad, precio, jacuzzi);
        }

        try {
            hotel.agregarHabitacion(habitacion);
            System.out.println("Habitacion agregada con exito.");
        } catch (AccesoDenegadoException | ElementoDuplicadoException | PersistenciaException e) {
            System.out.println("No se pudo agregar la habitacion: " + e.getMessage());
        }
    }

    private static void modificarPrecio(Hotel hotel) {
        System.out.println("\n===== MODIFICAR PRECIO =====");
        int numero = Teclado.leerEntero("Numero de habitacion: ");
        double precio = Teclado.leerMonto("Nuevo precio: ");

        try {
            hotel.modificarPrecio(numero, precio);
            System.out.println("Precio modificado con exito.");
        } catch (AccesoDenegadoException | ElementoNoEncontradoException | PersistenciaException e) {
            System.out.println("No se pudo modificar el precio: " + e.getMessage());
        }
    }

    private static void marcarNoDisponible(Hotel hotel) {
        System.out.println("\n===== MARCAR NO DISPONIBLE =====");
        int numero = Teclado.leerEntero("Numero de habitacion: ");

        System.out.println("1. Limpieza");
        System.out.println("2. Reparacion");
        System.out.println("3. Desinfeccion");
        System.out.println("4. Fuera de servicio");
        int opcionEstado = Teclado.leerOpcion("Estado: ", 1, 4);

        EEstadoHabitacion estado;
        if (opcionEstado == 1) {
            estado = EEstadoHabitacion.LIMPIEZA;
        } else if (opcionEstado == 2) {
            estado = EEstadoHabitacion.REPARACION;
        } else if (opcionEstado == 3) {
            estado = EEstadoHabitacion.DESINFECCION;
        } else {
            estado = EEstadoHabitacion.FUERA_DE_SERVICIO;
        }

        String motivo = Teclado.leerTexto("Motivo: ");

        try {
            hotel.marcarHabitacionNoDisponible(numero, motivo, estado);
            System.out.println("Habitacion marcada como no disponible.");
        } catch (AccesoDenegadoException | ElementoNoEncontradoException | PersistenciaException e) {
            System.out.println("No se pudo cambiar el estado: " + e.getMessage());
        }
    }

    private static void habilitarHabitacion(Hotel hotel) {
        System.out.println("\n===== HABILITAR HABITACION =====");
        int numero = Teclado.leerEntero("Numero de habitacion: ");

        try {
            hotel.liberarHabitacion(numero);
            System.out.println("Habitacion habilitada con exito.");
        } catch (AccesoDenegadoException | ElementoNoEncontradoException | PersistenciaException e) {
            System.out.println("No se pudo habilitar la habitacion: " + e.getMessage());
        }
    }

    private static void listarHabitaciones(Hotel hotel) {
        System.out.println("\n===== LISTAR HABITACIONES =====");
        System.out.println("1. Todas");
        System.out.println("2. Disponibles");
        System.out.println("3. Ocupadas");
        System.out.println("4. No disponibles");
        int opcion = Teclado.leerOpcion("Opcion: ", 1, 4);

        try {
            List<Habitacion> lista;
            if (opcion == 1) {
                lista = hotel.listarHabitaciones();
            } else if (opcion == 2) {
                lista = hotel.listarHabitacionesDisponibles();
            } else if (opcion == 3) {
                lista = hotel.listarHabitacionesOcupadas();
            } else {
                lista = hotel.listarHabitacionesNoDisponibles();
            }

            if (lista.isEmpty()) {
                System.out.println("No hay habitaciones para mostrar.");
            } else {
                for (Habitacion h : lista) {
                    System.out.println(h);
                }
            }
        } catch (AccesoDenegadoException e) {
            System.out.println(e.getMessage());
        }
    }
}
