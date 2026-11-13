package Menus;

public final class Consola {

    private Consola() {
    }

    public static void mostrarEncabezado(String titulo) {

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.printf("║ %-50s ║%n", titulo);
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    public static void mostrarSeparador() {

        System.out.println("──────────────────────────────────────────────────────");
    }

    public static void mostrarExito(String mensaje) {

        System.out.println("\n[✓] " + mensaje);
    }

    public static void mostrarError(String mensaje) {

        System.out.println("\n[!] " + mensaje);
    }

    public static void mostrarInfo(String mensaje) {

        System.out.println("\n[i] " + mensaje);
    }
}