package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.utils.Menu;
import java.util.Scanner;

public class ModelFactoryController {

    private static class SingletonHolder {
        private static final ModelFactoryController INSTANCE = new ModelFactoryController();
    }

    public static ModelFactoryController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Scanner scanner;

    public ModelFactoryController() {
        scanner = new Scanner(System.in);
        iniciarAplicacion();
    }

    public void iniciarAplicacion() {
        gestionarMenuLogin();
    }

    /**
     * Gestiona el menú de login, permitiendo al usuario iniciar sesión o salir
     */
    private void gestionarMenuLogin() {
        int opcion;

        do {
            Menu.showLogin();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.println("Iniciando sesión...");
                    Menu.mostrarProcesando();
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    gestionarMenuPrincipal();
                    break;
                case 0:
                    System.out.println("¡Gracias por usar VITAL APP! Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Gestiona el menú principal con todas sus opciones
     */
    private void gestionarMenuPrincipal() {
        int opcion;

        do {
            Menu.mostrarMenuPrincipal();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    gestionarMenuDiagnosticos();
                    break;
                case 2:
                    gestionarMenuPacientes();
                    break;
                case 3:
                    gestionarMenuCitas();
                    break;
                case 4:
                    gestionarActualizacionDatos();
                    break;
                case 0:
                    System.out.println("Volviendo al menú de login...");
                    return; // Vuelve al menú de login
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Gestiona el menú de diagnósticos
     */
    private void gestionarMenuDiagnosticos() {
        int opcion;

        do {
            mostrarMenuDiagnosticos();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.println("Registrando nuevo diagnóstico...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 2:
                    System.out.println("Consultando diagnósticos...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 3:
                    System.out.println("Actualizando diagnóstico...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    return; // Vuelve al menú principal
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú de diagnósticos
     */
    private void mostrarMenuDiagnosticos() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    ADMINISTRAR DIAGNÓSTICOS      ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Registrar diagnóstico        ║");
        System.out.println("║  2. Consultar diagnósticos       ║");
        System.out.println("║  3. Actualizar diagnóstico       ║");
        System.out.println("║  0. Volver al menú principal     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Gestiona el menú de pacientes
     */
    private void gestionarMenuPacientes() {
        int opcion;

        do {
            mostrarMenuPacientes();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.println("Registrando nuevo paciente...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 2:
                    System.out.println("Consultando pacientes...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 3:
                    System.out.println("Actualizando datos de paciente...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    return; // Vuelve al menú principal
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú de pacientes
     */
    private void mostrarMenuPacientes() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║      ADMINISTRAR PACIENTES       ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Registrar paciente           ║");
        System.out.println("║  2. Consultar pacientes          ║");
        System.out.println("║  3. Actualizar datos de paciente ║");
        System.out.println("║  0. Volver al menú principal     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Gestiona el menú de citas
     */
    private void gestionarMenuCitas() {
        int opcion;

        do {
            mostrarMenuCitas();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.println("Agendando nueva cita...");
                    Menu.mostrarProcesando();

                    esperarTecla();
                    break;
                case 2:
                    System.out.println("Consultando citas...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 3:
                    System.out.println("Cancelando cita...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    return; // Vuelve al menú principal
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú de citas
     */
    private void mostrarMenuCitas() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        ADMINISTRAR CITAS         ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Agendar cita                 ║");
        System.out.println("║  2. Consultar citas              ║");
        System.out.println("║  3. Cancelar cita                ║");
        System.out.println("║  0. Volver al menú principal     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Gestiona la actualización de datos
     */
    private void gestionarActualizacionDatos() {
        int opcion;

        do {
            mostrarMenuActualizarDatos();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    System.out.println("Actualizando datos personales...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 2:
                    System.out.println("Actualizando contraseña...");
                    Menu.mostrarProcesando();
                    esperarTecla();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    return; // Vuelve al menú principal
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú de actualización de datos
     */
    private void mostrarMenuActualizarDatos() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║       ACTUALIZAR DATOS           ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Datos personales             ║");
        System.out.println("║  2. Contraseña                   ║");
        System.out.println("║  0. Volver al menú principal     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee la opción ingresada por el usuario con validación básica
     */
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Opción inválida si no es un número
        }
    }

    /**
     * Espera que el usuario presione Enter para continuar
     */
    private void esperarTecla() {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}