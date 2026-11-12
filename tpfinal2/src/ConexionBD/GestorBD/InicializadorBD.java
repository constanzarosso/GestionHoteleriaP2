package ConexionBD.GestorBD;

import ConexionBD.*;
import Excepciones.ExcepcionesBD.BDPersistenciaException;
import gestoras.GestorPasajeros;

public class InicializadorBD {

    public static void inicializar() throws BDPersistenciaException {
        GestorUsuarioBD.createTable();
        GestorPasajerosBD.createTable();
        GestorHabitacionesBD.createTable();
        GestorReservasBD.createTable();
        GestorOcupacionesBD.createTable();
        GestorBackupBD.createTable();

        System.out.println("Base de datos creada.");
    }
}
