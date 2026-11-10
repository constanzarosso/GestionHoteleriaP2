package ConexionBD;

import Excepciones.ElementoNoEncontradoException;
import Excepciones.PersistenciaException;
import enums.MetodoDePago;
import gestoras.GestorReservas;
import models.Consumo;
import models.Ocupacion;
import models.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

public final class GestorOcupacionesBD {
    private GestorOcupacionesBD() {
    }

    private static void crearOcupaciones() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS ocupaciones ("
                    + "id INT NOT NULL, "
                    + "id_reserva INT NOT NULL, "
                    + "fecha_check_in DATE NOT NULL, "
                    + "fecha_check_out DATE NULL, "
                    + "total DOUBLE NOT NULL DEFAULT 0, "
                    + "metodo_pago ENUM('EFECTIVO', 'DEBITO', 'CREDITO', 'TRANSFERENCIA') NULL, "
                    + "PRIMARY KEY (id), "
                    + "UNIQUE (id_reserva), "
                    + "FOREIGN KEY (id_reserva) REFERENCES reservas(id))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla ocupaciones: " + e.getMessage());
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

    private static void crearConsumos() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS consumos ("
                    + "id INT NOT NULL, "
                    + "id_ocupacion INT NOT NULL, "
                    + "descripcion VARCHAR(100) NOT NULL, "
                    + "monto DOUBLE NOT NULL, "
                    + "fecha DATE NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "FOREIGN KEY (id_ocupacion) REFERENCES ocupaciones(id))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla consumos: " + e.getMessage());
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
        crearOcupaciones();
        crearConsumos();
    }

    public List<Ocupacion> listar(GestorReservas gestorReservas)
            throws PersistenciaException, ElementoNoEncontradoException {

        List<Ocupacion> ocupaciones = new ArrayList<>();

        String sql = "SELECT * FROM ocupaciones";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("id");
                int idReserva = rs.getInt("id_reserva");

                Reserva reserva = gestorReservas.buscarElemento(idReserva);

                LocalDate fechaCheckIn =
                        rs.getDate("fecha_check_in").toLocalDate();

                Ocupacion ocupacion =
                        new Ocupacion(id, reserva, fechaCheckIn);

                listarConsumos(conn, ocupacion);

                Date fechaCheckOut = rs.getDate("fecha_check_out");

                if (fechaCheckOut != null) {
                    MetodoDePago pago = MetodoDePago.valueOf(
                            rs.getString("metodo_pago"));

                    ocupacion.finalizar(
                            fechaCheckOut.toLocalDate(),
                            pago);


                }
                ocupaciones.add(ocupacion);
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar ocupaciones: " + e.getMessage());
        }

        return ocupaciones;
    }


        private void listarConsumos(Connection conn, Ocupacion ocupacion)
            throws PersistenciaException {

        String sql = "SELECT * FROM consumos WHERE id_ocupacion = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ocupacion.getId());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Consumo consumo = new Consumo(
                            rs.getInt("id"),
                            rs.getString("descripcion"),
                            rs.getDouble("monto"),
                            rs.getDate("fecha").toLocalDate()
                    );

                    ocupacion.agregarConsumo(consumo);
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar consumos: " + e.getMessage());
        }
    }

    public void insertar(Ocupacion ocupacion)
            throws PersistenciaException {

        String sql = "INSERT INTO ocupaciones "
                + "(id, id_reserva, fecha_check_in, fecha_check_out, total, metodo_pago) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ocupacion.getId());
            ps.setInt(2, ocupacion.getReserva().getId());
            ps.setDate(3, Date.valueOf(ocupacion.getFechaCheckIn()));

            if (ocupacion.getFechaCheckOut() != null) {
                ps.setDate(4, Date.valueOf(ocupacion.getFechaCheckOut()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            ps.setDouble(5, ocupacion.calcularTotal());

            if (ocupacion.getMetodoPago() != null) {
                ps.setString(6, ocupacion.getMetodoPago().name());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al insertar ocupación: " + e.getMessage());
        }
    }

    public void modificar(Ocupacion ocupacion)
            throws PersistenciaException {

        String sql = "UPDATE ocupaciones SET "
                + "fecha_check_out = ?, total = ?, metodo_pago = ? "
                + "WHERE id = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (ocupacion.getFechaCheckOut() != null) {
                ps.setDate(1, Date.valueOf(ocupacion.getFechaCheckOut()));
            } else {
                ps.setNull(1, java.sql.Types.DATE);
            }

            ps.setDouble(2, ocupacion.calcularTotal());

            if (ocupacion.getMetodoPago() != null) {
                ps.setString(3, ocupacion.getMetodoPago().name());
            } else {
                ps.setNull(3, java.sql.Types.VARCHAR);
            }

            ps.setInt(4, ocupacion.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al modificar ocupación: " + e.getMessage());
        }
    }

    public void insertarConsumo(int idOcupacion, Consumo consumo)
            throws PersistenciaException {

        String sql = "INSERT INTO consumos "
                + "(id, id_ocupacion, descripcion, monto, fecha) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, obtenerSiguienteIdConsumo(conn));
            ps.setInt(2, idOcupacion);
            ps.setString(3, consumo.getDescripcion());
            ps.setDouble(4, consumo.getMonto());
            ps.setDate(5, Date.valueOf(consumo.getFecha()));

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al insertar consumo: " + e.getMessage());
        }
    }

    private int obtenerSiguienteIdConsumo(Connection conn)
            throws SQLException {

        String sql = "SELECT MAX(id) FROM consumos";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }

        return 1;
    }
}
