package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.utils.Menu;

import java.util.List;
import java.util.Scanner;

public class ModelFactoryController {

    private static class SingletonHolder {
        private static final ModelFactoryController INSTANCE = new ModelFactoryController();
    }

    public static ModelFactoryController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Scanner scanner;
    private DiagnosticController diagnosticController;

    public ModelFactoryController() {
        scanner = new Scanner(System.in);

        diagnosticController = new DiagnosticController();
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
                    registrarDiagnostico();
                    break;
                case 2:
                    consultarDiagnosticos();
                    break;
                case 3:
                    actualizarDiagnostico();
                    break;
                case 4:
                    eliminarDiagnostico();
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
     * Registra un nuevo diagnóstico
     */
    private void registrarDiagnostico() {
        System.out.println("\n=== REGISTRAR NUEVO DIAGNÓSTICO ===");

        System.out.print("Código del diagnóstico (según resolución/ley): ");
        String codigo = scanner.nextLine();

        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("El código del diagnóstico no puede estar vacío.");
            esperarTecla();
            return;
        }

        // Verificar si el código ya existe
        if (diagnosticController.buscarDiagnosticoPorId(codigo) != null) {
            System.out.println("Error: Ya existe un diagnóstico con el código " + codigo);
            esperarTecla();
            return;
        }

        System.out.print("Diagnóstico: ");
        String diagnosticText = scanner.nextLine();

        if (diagnosticText == null || diagnosticText.trim().isEmpty()) {
            System.out.println("El diagnóstico no puede estar vacío.");
            esperarTecla();
            return;
        }

        Menu.mostrarProcesando();

        Diagnostic nuevoDiagnostico = diagnosticController.crearDiagnostico(codigo, diagnosticText);

        if (nuevoDiagnostico != null) {
            System.out.println("\n¡Diagnóstico registrado exitosamente!");
            System.out.println("Código del diagnóstico: " + nuevoDiagnostico.getCode());
        } else {
            System.out.println("\nError al registrar el diagnóstico. Intente nuevamente.");
        }

        esperarTecla();
    }

    /**
     * Consulta todos los diagnósticos
     */
    private void consultarDiagnosticos() {
        System.out.println("\n=== CONSULTAR DIAGNÓSTICOS ===");

        Menu.mostrarProcesando();
        List<Diagnostic> diagnosticos = diagnosticController.obtenerTodosDiagnosticos();

        if (diagnosticos.isEmpty()) {
            System.out.println("No se encontraron diagnósticos registrados.");
        } else {
            System.out.println("\n=== LISTA DE DIAGNÓSTICOS ===");
            for (Diagnostic diag : diagnosticos) {
                System.out.println("\nCódigo: " + diag.getCode());
                System.out.println("Diagnóstico: " + diag.getDiagnostic());
                System.out.println("------------------------------------------");
            }
        }

        esperarTecla();
    }

    /**
     * Actualiza un diagnóstico existente
     */
    private void actualizarDiagnostico() {
        System.out.println("\n=== ACTUALIZAR DIAGNÓSTICO ===");

        System.out.print("Ingrese el código del diagnóstico a actualizar: ");
        String code = scanner.nextLine();

        Diagnostic diagnostico = diagnosticController.buscarDiagnosticoPorId(code);
        if (diagnostico == null) {
            System.out.println("No se encontró un diagnóstico con el código proporcionado.");
            esperarTecla();
            return;
        }

        System.out.println("\nDatos actuales del diagnóstico:");
        System.out.println("Diagnóstico: " + diagnostico.getDiagnostic());

        System.out.print("\nNuevo diagnóstico: ");
        String nuevoDiagnostico = scanner.nextLine();

        if (nuevoDiagnostico == null || nuevoDiagnostico.trim().isEmpty()) {
            System.out.println("El diagnóstico no puede estar vacío.");
            esperarTecla();
            return;
        }

        Menu.mostrarProcesando();

        boolean exito = diagnosticController.actualizarDiagnostico(code, nuevoDiagnostico);

        if (exito) {
            System.out.println("\n¡Diagnóstico actualizado exitosamente!");
        } else {
            System.out.println("\nError al actualizar el diagnóstico.");
        }

        esperarTecla();
    }

    /**
     * Elimina un diagnóstico
     */
    private void eliminarDiagnostico() {
        System.out.println("\n=== ELIMINAR DIAGNÓSTICO ===");

        System.out.print("Ingrese el código del diagnóstico a eliminar: ");
        String code = scanner.nextLine();

        Diagnostic diagnostico = diagnosticController.buscarDiagnosticoPorId(code);
        if (diagnostico == null) {
            System.out.println("No se encontró un diagnóstico con el código proporcionado.");
            esperarTecla();
            return;
        }

        System.out.println("\nDetalle del diagnóstico a eliminar:");
        System.out.println("Código: " + diagnostico.getCode());
        System.out.println("Diagnóstico: " + diagnostico.getDiagnostic());

        System.out.print("\n¿Está seguro que desea eliminar este diagnóstico? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            Menu.mostrarProcesando();
            boolean exito = diagnosticController.eliminarDiagnostico(code);

            if (exito) {
                System.out.println("\n¡Diagnóstico eliminado exitosamente!");
            } else {
                System.out.println("\nError al eliminar el diagnóstico.");
            }
        } else {
            System.out.println("\nOperación cancelada.");
        }

        esperarTecla();
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