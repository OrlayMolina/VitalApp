package co.edu.uniquindio.ing.soft.utils;

public class Menu {

    public static void showLogin() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║          VITAL APP               ║");
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
        System.out.println("║  1. Administrar diagnósticos     ║");
        System.out.println("║  2. Administrar pacientes        ║");
        System.out.println("║  3. Administrar citas            ║");
        System.out.println("║  4. Actualizar datos             ║");
        System.out.println("║  0. Cerrar sesión                ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    public static void mostrarProcesando() {
        System.out.print("Procesando");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}