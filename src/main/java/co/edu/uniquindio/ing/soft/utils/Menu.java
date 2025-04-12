package co.edu.uniquindio.ing.soft.utils;

public class Menu {

    public static void showLogin() {

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║      BIENVENIDO A VITAL APP      ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Iniciar sesión               ║");
        System.out.println("║  0. Salir                        ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarMenuPrincipal() {

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║          MENÚ PRINCIPAL          ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Administrar Diagnósticos     ║");
        System.out.println("║  2. Administrar Pacientes        ║");
        System.out.println("║  3. Administrar Citas            ║");
        System.out.println("║  4. Actualizar datos             ║");
        System.out.println("║  0. Salir                        ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarProcesando() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║           PROCESANDO...          ║");
        System.out.println("╚══════════════════════════════════╝");
    }
}
