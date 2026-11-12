package ConexionBD;


import Excepciones.ExcepcionesBD.PersistenciaException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import enums.EstadoReserva;
import gestoras.GestorHabitaciones;
import gestoras.GestorPasajeros;
import models.Habitacion;
import models.Pasajero;
import models.Reserva;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class GestorReservasBD {
    private GestorReservasBD() {
    }

    private static void crearReservas() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS reservas ("
                    + "id INT NOT NULL, "
                    + "dni_pasajero INT NOT NULL, "
                    + "numero_habitacion INT NOT NULL, "
                    + "fecha_desde DATE NOT NULL, "
                    + "fecha_hasta DATE NOT NULL, "
                    + "cantidad_personas INT NOT NULL, "
                    + "estado ENUM('PENDIENTE', 'EN_CURSO', 'FINALIZADA', 'CANCELADA') NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "FOREIGN KEY (dni_pasajero) REFERENCES pasajeros(dni), "
                    + "FOREIGN KEY (numero_habitacion) REFERENCES habitaciones(numero))");
            ps.execute();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla reservas: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new PersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    public static void createTable() throws PersistenciaException {
        crearReservas();
    }

    public List<Reserva> listar(GestorHabitaciones gestorHabitaciones,
                                GestorPasajeros gestorPasajeros)
            throws PersistenciaException, ElementoNoEncontradoException {

        List<Reserva> reservas = new ArrayList<>();

        String sql = "SELECT * FROM reservas";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("id");
                int dniPasajero = rs.getInt("dni_pasajero");
                int numeroHabitacion = rs.getInt("numero_habitacion");

                Pasajero pasajero =
                        gestorPasajeros.buscarElemento(dniPasajero);

                Habitacion habitacion =
                        gestorHabitaciones.buscarElemento(numeroHabitacion);

                LocalDate fechaDesde =
                        rs.getDate("fecha_desde").toLocalDate();

                LocalDate fechaHasta =
                        rs.getDate("fecha_hasta").toLocalDate();

                int cantidadPersonas =
                        rs.getInt("cantidad_personas");

                EstadoReserva estado =
                        EstadoReserva.valueOf(rs.getString("estado"));

                Reserva reserva = new Reserva(id, pasajero, fechaDesde, habitacion, fechaHasta, cantidadPersonas, estado);


                if (estado == EstadoReserva.CANCELADA) {
                    reserva.cancelar();
                } else if (estado == EstadoReserva.EN_CURSO) {
                    reserva.pasarAEnCurso();
                } else if (estado == EstadoReserva.FINALIZADA) {
                    reserva.pasarAEnCurso();
                    reserva.finalizar();
                }

                reservas.add(reserva);
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar reservas: " + e.getMessage());
        }

        return reservas;
    }

    public void insertar(Reserva reserva)
            throws PersistenciaException {

        String sql = "INSERT INTO reservas "
                + "(id, dni_pasajero, numero_habitacion, fecha_desde, "
                + "fecha_hasta, cantidad_personas, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reserva.getId());
            ps.setInt(2, reserva.getPasajero().getId());
            ps.setInt(3, reserva.getHabitacion().getId());
            ps.setDate(4, Date.valueOf(reserva.getFechaDesde()));
            ps.setDate(5, Date.valueOf(reserva.getFechaHasta()));
            ps.setInt(6, reserva.getCantidadPersonas());
            ps.setString(7, reserva.getEstado().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al insertar reserva: " + e.getMessage());
        }
    }

    public void modificar(Reserva reserva)
            throws PersistenciaException {

        String sql = "UPDATE reservas SET "
                + "dni_pasajero = ?, numero_habitacion = ?, "
                + "fecha_desde = ?, fecha_hasta = ?, "
                + "cantidad_personas = ?, estado = ? "
                + "WHERE id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reserva.getPasajero().getId());
            ps.setInt(2, reserva.getHabitacion().getId());
            ps.setDate(3, Date.valueOf(reserva.getFechaDesde()));
            ps.setDate(4, Date.valueOf(reserva.getFechaHasta()));
            ps.setInt(5, reserva.getCantidadPersonas());
            ps.setString(6, reserva.getEstado().name());
            ps.setInt(7, reserva.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al modificar reserva: " + e.getMessage());
        }
    }
}
