package ConexionBD;

import Excepciones.ExcepcionesBD.*;
import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;
import models.Habitacion;
import models.HabitacionDoble;
import models.HabitacionSimple;
import models.HabitacionSuite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class GestorHabitacionesBD {

    private GestorHabitacionesBD() {
    }

    private static void crearHabitaciones() throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS habitaciones ("
                    + "numero INT NOT NULL, "
                    + "tipo ENUM('SIMPLE', 'DOBLE', 'SUITE') NOT NULL, "
                    + "piso INT NOT NULL, "
                    + "capacidad INT NOT NULL, "
                    + "precio_base DOUBLE NOT NULL, "
                    + "estado ENUM('DISPONIBLE', 'OCUPADA', 'LIMPIEZA', 'REPARACION', 'DESINFECCION', 'FUERA_DE_SERVICIO') NOT NULL, "
                    + "motivo VARCHAR(100) NULL, "
                    + "PRIMARY KEY (numero))");
            ps.execute();
        } catch (SQLException e) {
            throw new BDCrearTablaException("Habitaciones: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar" + e.getMessage());
            }
        }
    }

    private static void crearHabitacionesDobles() throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS habitaciones_dobles ("
                    + "numero INT NOT NULL, "
                    + "camas_separadas BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (numero), "
                    + "FOREIGN KEY (numero) REFERENCES habitaciones(numero))");
            ps.execute();

        } catch (SQLException e) {
            throw new BDCrearTablaException("Habitaciones_dobles: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    private static void crearHabitacionesSuite() throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS habitaciones_suites ("
                    + "numero INT NOT NULL, "
                    + "tiene_jacuzzi BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (numero), "
                    + "FOREIGN KEY (numero) REFERENCES habitaciones(numero))");
            ps.execute();
        } catch (SQLException e) {
            throw new BDCrearTablaException("Habitaciones_suite: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    public static void createTable() throws BDPersistenciaException {
        crearHabitaciones();
        crearHabitacionesDobles();
        crearHabitacionesSuite();
    }

    private static void insertarHabitacion(Habitacion h) throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("INSERT INTO habitaciones "
                    + "(numero, tipo, piso, capacidad, precio_base, estado, motivo) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, h.getNumero());
            ps.setString(2, h.getTipoHabitacion().name());
            ps.setInt(3, h.getPiso());
            ps.setInt(4, h.getCapacidad());
            ps.setDouble(5, h.getPrecioBase());
            ps.setString(6, h.getEstadoHabitacion().name());
            ps.setString(7, h.getMotivoNoDisponible());
            ps.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BDRegistroDuplicadoException("Habitacion " + h.getNumero());
        } catch (SQLException e) {
            throw new BDPersistenciaException("Error al insertar una habitacion: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    private static void insertarHabitacionDoble(HabitacionDoble d) throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("INSERT INTO habitaciones_dobles "
                    + "(numero, camas_separadas) VALUES (?, ?)");
            ps.setInt(1, d.getNumero());
            ps.setBoolean(2, d.isCamasSeparadas());
            ps.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BDRegistroDuplicadoException("Habitacion_doble " + d.getNumero());
        } catch (SQLException e) {
            throw new BDPersistenciaException("Error al insertar una habitacion_doble: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    private static void insertarHabitacionSuite(HabitacionSuite s) throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("INSERT INTO habitaciones_suites "
                    + "(numero, tiene_jacuzzi) VALUES (?, ?)");
            ps.setInt(1, s.getNumero());
            ps.setBoolean(2, s.isTieneJacuzzi());
            ps.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new BDRegistroDuplicadoException("Habitacion_Suite " + s.getNumero());
        } catch (SQLException e) {
            throw new BDPersistenciaException("Error al insertar una habitacion_suite: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    public static void insertHabitacion(Habitacion h) throws BDPersistenciaException {
        insertarHabitacion(h);
        if (h.getTipoHabitacion() == ETipoHabitacion.DOBLE) {
            insertarHabitacionDoble((HabitacionDoble) h);
        } else if (h.getTipoHabitacion() == ETipoHabitacion.SUITE) {
            insertarHabitacionSuite((HabitacionSuite) h);
        }
    }

    public static List<Habitacion> listar() throws BDPersistenciaException {
        List<Habitacion> habitaciones = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("SELECT h.*, d.camas_separadas, s.tiene_jacuzzi "
                    + "FROM habitaciones h "
                    + "LEFT JOIN habitaciones_dobles d ON h.numero = d.numero "
                    + "LEFT JOIN habitaciones_suites s ON h.numero = s.numero");
            rs = ps.executeQuery();

            while (rs.next()) {
                int numero = rs.getInt("numero");
                int piso = rs.getInt("piso");
                int capacidad = rs.getInt("capacidad");
                double precio = rs.getDouble("precio_base");
                String motivo = rs.getString("motivo");

                EEstadoHabitacion estado = EEstadoHabitacion.valueOf(rs.getString("estado"));
                ETipoHabitacion tipo = ETipoHabitacion.valueOf(rs.getString("tipo"));

                Habitacion h;
                if (tipo == ETipoHabitacion.DOBLE) {
                    h = new HabitacionDoble(numero, piso, capacidad, precio, estado, motivo,
                            rs.getBoolean("camas_separadas"));
                } else if (tipo == ETipoHabitacion.SUITE) {
                    h = new HabitacionSuite(numero, piso, capacidad, precio, estado, motivo,
                            rs.getBoolean("tiene_jacuzzi"));
                } else {
                    h = new HabitacionSimple(numero, piso, capacidad, precio, estado, motivo);
                }
                habitaciones.add(h);
            }
        } catch (SQLException e) {
            throw new BDConsultaException(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
        return habitaciones;
    }

    public static void modificarHabitacion(Habitacion h) throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("UPDATE habitaciones "
                    + "SET capacidad = ?, precio_base = ?, estado = ?, motivo = ? "
                    + "WHERE numero = ?");
            ps.setInt(1, h.getCapacidad());
            ps.setDouble(2, h.getPrecioBase());
            ps.setString(3, h.getEstadoHabitacion().name());
            ps.setString(4, h.getMotivoNoDisponible());
            ps.setInt(5, h.getNumero());
            int filasModificadas = ps.executeUpdate();
            if (filasModificadas == 0) {
                throw new BDRegistroNoEncontradoException("Habitacion " + h.getNumero());
            }
        } catch (SQLException e) {
            throw new BDPersistenciaException("Error al modificar tabla habitacion: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }
}
