package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.enums.*;
import co.edu.uniquindio.ing.soft.model.*;
import co.edu.uniquindio.ing.soft.utils.Menu;
import co.edu.uniquindio.ing.soft.utils.Persistencia;

import java.util.ArrayList;
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
    private final UserController userController;
    private AppointmentController appointmentController;
    private User usuarioActual;

    public ModelFactoryController() {
        scanner = new Scanner(System.in);
        VitalApp vitalApp = Persistencia.cargarRecursoVitalAppXML();
        if (vitalApp == null) {
            vitalApp = new VitalApp();
        }

        diagnosticController = new DiagnosticController(vitalApp);
        userController = new UserController(vitalApp);
        appointmentController = new AppointmentController(vitalApp);
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
                    iniciarSesion();
                    break;
                case 0:
                    String[] mensajes = {
                            "SALIENDO.",
                            "SALIENDO..",
                            "SALIENDO..",
                            "SALIENDO",
                            "SALIENDO.",
                            "SALIENDO..",
                            "SALIENDO..."
                    };

                    while (true) {  // Bucle infinito
                        for (String msg : mensajes) {
                            System.out.println(msg);
                            try {
                                Thread.sleep(500);  // Pausa de 500 milisegundos entre mensajes
                            } catch (InterruptedException e) {
                                // Si se interrumpe la espera, reestablecemos la interrupción
                                Thread.currentThread().interrupt();
                                System.out.println("El hilo fue interrumpido.");
                            }
                        }
                    }
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    esperarTecla();
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Maneja el proceso de inicio de sesión
     */
    private void iniciarSesion() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        System.out.println("Verificando credenciales...");
        Menu.mostrarProcesando();

        // Autenticar usuario
        usuarioActual = userController.autenticarUsuario(email, password);

        if (usuarioActual != null) {
            Role rol = userController.obtenerRolUsuario(usuarioActual);

            String nombre = "";
            if (usuarioActual instanceof Doctor) {
                Doctor doctor = (Doctor) usuarioActual;
                nombre = doctor.getFirstname() + " " + doctor.getLastname();
            } else if (usuarioActual instanceof Patient) {
                Patient patient = (Patient) usuarioActual;
                nombre = patient.getFirstname() + " " + patient.getLastname();
            }

            System.out.println("\n¡Inicio de sesión exitoso!");
            System.out.println("Bienvenido/a " + nombre);
            System.out.println("Rol: " + rol);

            esperarTecla();
            gestionarMenuPrincipal();
        } else {
            System.out.println("\nCredenciales incorrectas. Por favor intente nuevamente.");
            esperarTecla();
        }
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
                    System.out.println("Cerrando sesión...");
                    usuarioActual = null;
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
     * Registra un nuevo paciente
     */
    private void registrarPaciente() {
        System.out.println("\n=== REGISTRAR NUEVO PACIENTE ===");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (email == null || email.trim().isEmpty()) {
            System.out.println("El email no puede estar vacío.");
            esperarTecla();
            return;
        }

        if (userController.buscarUsuarioPorEmail(email) != null) {
            System.out.println("Error: Ya existe un usuario con el email " + email);
            esperarTecla();
            return;
        }

        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        if (password == null || password.trim().isEmpty()) {
            System.out.println("La contraseña no puede estar vacía.");
            esperarTecla();
            return;
        }

        System.out.print("Nombre: ");
        String firstname = scanner.nextLine();

        if (firstname == null || firstname.trim().isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            esperarTecla();
            return;
        }

        System.out.print("Apellido: ");
        String lastname = scanner.nextLine();

        if (lastname == null || lastname.trim().isEmpty()) {
            System.out.println("El apellido no puede estar vacío.");
            esperarTecla();
            return;
        }

        // Mostrar opciones de tipo de documento
        System.out.println("\nTipo de documento:");
        DocumentType[] tiposDocumento = DocumentType.values();
        for (int i = 0; i < tiposDocumento.length; i++) {
            System.out.println((i + 1) + ". " + tiposDocumento[i]);
        }

        System.out.print("Seleccione una opción: ");
        int opcionDocumento;
        try {
            opcionDocumento = Integer.parseInt(scanner.nextLine().trim());
            if (opcionDocumento < 1 || opcionDocumento > tiposDocumento.length) {
                System.out.println("Opción inválida. Se usará CC por defecto.");
                opcionDocumento = 1; // Usar CC por defecto
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido. Se usará CC por defecto.");
            opcionDocumento = 1; // Usar CC por defecto
        }

        DocumentType documentType = tiposDocumento[opcionDocumento - 1];

        System.out.print("Número de documento: ");
        String documentNumber = scanner.nextLine();

        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            System.out.println("El número de documento no puede estar vacío.");
            esperarTecla();
            return;
        }

        // Verificar si ya existe un paciente con el mismo documento
        PatientController patientController = new PatientController(diagnosticController.getVitalApp());
        if (patientController.buscarPacientePorDocumentoNumero(documentNumber) != null) {
            System.out.println("Error: Ya existe un paciente con el número de documento " + documentNumber);
            esperarTecla();
            return;
        }

        System.out.print("Edad: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
            if (age <= 0) {
                System.out.println("La edad debe ser un número positivo.");
                esperarTecla();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato de edad inválido.");
            esperarTecla();
            return;
        }

        // Mostrar opciones de género
        System.out.println("\nGénero:");
        Gender[] generos = Gender.values();
        for (int i = 0; i < generos.length; i++) {
            System.out.println((i + 1) + ". " + generos[i]);
        }

        System.out.print("Seleccione una opción: ");
        int opcionGenero;
        try {
            opcionGenero = Integer.parseInt(scanner.nextLine().trim());
            if (opcionGenero < 1 || opcionGenero > generos.length) {
                System.out.println("Opción inválida. Se usará MASCULINE por defecto.");
                opcionGenero = 1; // Usar MASCULINE por defecto
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido. Se usará MASCULINE por defecto.");
            opcionGenero = 1; // Usar MASCULINE por defecto
        }

        Gender gender = generos[opcionGenero - 1];

        System.out.print("Dirección: ");
        String address = scanner.nextLine();

        if (address == null || address.trim().isEmpty()) {
            System.out.println("La dirección no puede estar vacía.");
            esperarTecla();
            return;
        }

        Menu.mostrarProcesando();

        Patient nuevoPaciente = patientController.crearPaciente(email, password, firstname, lastname,
                documentType, documentNumber, age, gender, address);

        if (nuevoPaciente != null) {
            System.out.println("\n¡Paciente registrado exitosamente!");
            System.out.println("Email del paciente: " + nuevoPaciente.getEmail());
        } else {
            System.out.println("\nError al registrar el paciente. Verifique los datos e intente nuevamente.");
        }

        esperarTecla();
    }

    /**
     * Consulta todos los pacientes
     */
    private void consultarPacientes() {
        System.out.println("\n=== CONSULTAR PACIENTES ===");

        Menu.mostrarProcesando();
        PatientController patientController = new PatientController(diagnosticController.getVitalApp());
        List<Patient> pacientes = patientController.obtenerTodosPacientes();

        if (pacientes.isEmpty()) {
            System.out.println("No se encontraron pacientes registrados.");
        } else {
            System.out.println("\n=== LISTA DE PACIENTES ===");
            for (Patient paciente : pacientes) {
                System.out.println("\nNombre: " + paciente.getFirstname() + " " + paciente.getLastname());
                System.out.println("Documento: " + paciente.getDocumentType() + " " + paciente.getDocumentNumber());
                System.out.println("Email: " + paciente.getEmail());
                System.out.println("Edad: " + paciente.getAge());
                System.out.println("Género: " + paciente.getGender());
                System.out.println("Dirección: " + paciente.getAddress());

                // Mostrar diagnósticos si tiene
                if (paciente.getDiagnostics() != null && !paciente.getDiagnostics().isEmpty()) {
                    System.out.println("Diagnósticos:");
                    for (Diagnostic diagnostico : paciente.getDiagnostics()) {
                        System.out.println("  - " + diagnostico.getCode() + ": " + diagnostico.getDiagnostic());
                    }
                } else {
                    System.out.println("Diagnósticos: Ninguno");
                }
                System.out.println("------------------------------------------");
            }
        }

        esperarTecla();
    }

    /**
     * Actualiza los datos de un paciente
     */
    private void actualizarDatosPaciente() {
        System.out.println("\n=== ACTUALIZAR DATOS DE PACIENTE ===");

        System.out.print("Ingrese el email del paciente a actualizar: ");
        String email = scanner.nextLine();

        PatientController patientController = new PatientController(diagnosticController.getVitalApp());
        Patient paciente = patientController.buscarPacientePorEmail(email);

        if (paciente == null) {
            System.out.println("No se encontró un paciente con el email proporcionado.");
            esperarTecla();
            return;
        }

        System.out.println("\nDatos actuales del paciente:");
        System.out.println("Nombre: " + paciente.getFirstname() + " " + paciente.getLastname());
        System.out.println("Documento: " + paciente.getDocumentType() + " " + paciente.getDocumentNumber());
        System.out.println("Edad: " + paciente.getAge());
        System.out.println("Dirección: " + paciente.getAddress());

        System.out.println("\nIngrese los nuevos datos (deje en blanco para mantener el valor actual):");

        System.out.print("Nueva contraseña: ");
        String password = scanner.nextLine();

        System.out.print("Nuevo nombre: ");
        String firstname = scanner.nextLine();

        System.out.print("Nuevo apellido: ");
        String lastname = scanner.nextLine();

        System.out.print("Nueva edad (0 para mantener actual): ");
        int age;
        try {
            String ageStr = scanner.nextLine().trim();
            age = ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            System.out.println("Formato de edad inválido. Se mantendrá la edad actual.");
            age = 0;
        }

        System.out.print("Nueva dirección: ");
        String address = scanner.nextLine();

        Menu.mostrarProcesando();

        boolean exito = patientController.actualizarPaciente(email, password, firstname, lastname, age, address);

        if (exito) {
            System.out.println("\n¡Datos del paciente actualizados exitosamente!");
        } else {
            System.out.println("\nError al actualizar los datos del paciente.");
        }

        esperarTecla();
    }

    /**
     * Agenda una nueva cita
     */
    private void agendarCita() {
        System.out.println("\n=== AGENDAR NUEVA CITA ===");

        // Solicitar documento del paciente
        System.out.print("Número de documento del paciente: ");
        String patientDocumentNumber = scanner.nextLine();

        if (patientDocumentNumber == null || patientDocumentNumber.trim().isEmpty()) {
            System.out.println("El número de documento no puede estar vacío.");
            esperarTecla();
            return;
        }

        PatientController patientController = new PatientController(diagnosticController.getVitalApp());
        Patient paciente = patientController.buscarPacientePorDocumentoNumero(patientDocumentNumber);

        if (paciente == null) {
            System.out.println("No se encontró un paciente con el número de documento proporcionado.");
            esperarTecla();
            return;
        }

        // Buscar doctor (se asume que el usuario actual es el doctor)
        Doctor doctor = null;
        if (usuarioActual instanceof Doctor) {
            doctor = (Doctor) usuarioActual;
        } else {
            System.out.println("Error: Solo los doctores pueden agendar citas.");
            esperarTecla();
            return;
        }

        // Seleccionar día de la semana
        System.out.println("\nDía de la cita:");
        Day[] dias = Day.values();
        for (int i = 0; i < dias.length; i++) {
            System.out.println((i + 1) + ". " + dias[i]);
        }

        System.out.print("Seleccione una opción: ");
        int opcionDia;
        try {
            opcionDia = Integer.parseInt(scanner.nextLine().trim());
            if (opcionDia < 1 || opcionDia > dias.length) {
                System.out.println("Opción inválida. Se usará MONDAY por defecto.");
                opcionDia = 1; // Usar MONDAY por defecto
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido. Se usará MONDAY por defecto.");
            opcionDia = 1; // Usar MONDAY por defecto
        }

        Day day = dias[opcionDia - 1];

        // Ingresar horario
        System.out.print("Horario (HH:MM AM/PM): ");
        String horario = scanner.nextLine();

        if (horario == null || horario.trim().isEmpty()) {
            System.out.println("El horario no puede estar vacío.");
            esperarTecla();
            return;
        }

        // Preguntar si desea asociar diagnósticos
        System.out.print("¿Desea asociar diagnósticos a esta cita? (S/N): ");
        String respuesta = scanner.nextLine();

        List<String> diagnosticCodes = new ArrayList<>();

        if (respuesta.equalsIgnoreCase("S")) {
            // Obtener todos los diagnósticos para mostrarlos
            List<Diagnostic> diagnosticos = diagnosticController.obtenerTodosDiagnosticos();

            if (diagnosticos.isEmpty()) {
                System.out.println("No hay diagnósticos registrados en el sistema.");
            } else {
                System.out.println("\nDiagnósticos disponibles:");
                for (Diagnostic diagnostico : diagnosticos) {
                    System.out.println(diagnostico.getCode() + ": " + diagnostico.getDiagnostic());
                }

                System.out.println("\nIngrese los códigos de diagnóstico separados por coma (o deje en blanco para continuar):");
                String codigosStr = scanner.nextLine();

                if (codigosStr != null && !codigosStr.trim().isEmpty()) {
                    String[] codigos = codigosStr.split(",");
                    for (String codigo : codigos) {
                        diagnosticCodes.add(codigo.trim());
                    }
                }
            }
        }

        Menu.mostrarProcesando();

        // Crear la cita
        appointmentController = new AppointmentController(diagnosticController.getVitalApp());
        Appointment nuevaCita = appointmentController.crearCita(patientDocumentNumber, doctor, day, horario, diagnosticCodes);

        if (nuevaCita != null) {
            System.out.println("\n¡Cita agendada exitosamente!");
            System.out.println("ID de la cita: " + nuevaCita.getAppointmentId());
            System.out.println("Paciente: " + paciente.getFirstname() + " " + paciente.getLastname());
            System.out.println("Doctor: " + doctor.getFirstname() + " " + doctor.getLastname());
            System.out.println("Día: " + nuevaCita.getDay());
            System.out.println("Horario: " + nuevaCita.getHorario());
            System.out.println("Estado: " + nuevaCita.getStatus());
        } else {
            System.out.println("\nError al agendar la cita. Verifique que el horario esté disponible.");
        }

        esperarTecla();
    }

    /**
     * Consulta las citas según el rol del usuario
     */
    private void consultarCitas() {
        System.out.println("\n=== CONSULTAR CITAS ===");

        appointmentController = new AppointmentController(diagnosticController.getVitalApp());
        List<Appointment> citas = new ArrayList<>();

        // Determinar qué citas mostrar según el rol del usuario
        if (usuarioActual instanceof Doctor) {
            Doctor doctor = (Doctor) usuarioActual;
            citas = appointmentController.obtenerCitasPorDoctor(doctor);
            System.out.println("\nMostrando citas para el doctor: " + doctor.getFirstname() + " " + doctor.getLastname());
        } else if (usuarioActual instanceof Patient) {
            Patient patient = (Patient) usuarioActual;
            citas = appointmentController.obtenerCitasPorPaciente(patient.getDocumentNumber());
            System.out.println("\nMostrando citas para el paciente: " + patient.getFirstname() + " " + patient.getLastname());
        } else {
            // Administrador o rol sin definir
            citas = appointmentController.obtenerTodasCitas();
            System.out.println("\nMostrando todas las citas");
        }

        Menu.mostrarProcesando();

        if (citas.isEmpty()) {
            System.out.println("No se encontraron citas.");
        } else {
            PatientController patientController = new PatientController(diagnosticController.getVitalApp());

            System.out.println("\n=== LISTA DE CITAS ===");
            for (Appointment cita : citas) {
                System.out.println("\nID: " + cita.getAppointmentId());

                // Obtener información del paciente
                Patient paciente = patientController.buscarPacientePorDocumentoNumero(cita.getPatientDocumentNumber());
                if (paciente != null) {
                    System.out.println("Paciente: " + paciente.getFirstname() + " " + paciente.getLastname());
                } else {
                    System.out.println("Paciente: " + cita.getPatientDocumentNumber() + " (No encontrado)");
                }

                // Información del doctor (asumiendo que hay un método para buscar doctor por documento)
                Doctor doctor = (Doctor) cita.getDoctorDocumentNumber();
                if (doctor != null) {
                    System.out.println("Doctor: " + doctor.getFirstname() + " " + doctor.getLastname());
                } else {
                    System.out.println("Doctor: No disponible");
                }

                System.out.println("Día: " + cita.getDay());
                System.out.println("Horario: " + cita.getHorario());
                System.out.println("Estado: " + cita.getStatus());
                System.out.println("------------------------------------------");
            }
        }

        esperarTecla();
    }

    /**
     * Cancela una cita existente
     */
    private void cancelarCita() {
        System.out.println("\n=== CANCELAR CITA ===");

        System.out.print("Ingrese el ID de la cita a cancelar: ");
        String appointmentId = scanner.nextLine();

        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            System.out.println("El ID de la cita no puede estar vacío.");
            esperarTecla();
            return;
        }

        appointmentController = new AppointmentController(diagnosticController.getVitalApp());
        Appointment cita = appointmentController.buscarCitaPorId(appointmentId);

        if (cita == null) {
            System.out.println("No se encontró una cita con el ID proporcionado.");
            esperarTecla();
            return;
        }

        if (cita.getStatus() == AppointmentStatus.COMPLETED) {
            System.out.println("Error: No se puede cancelar una cita que ya fue completada.");
            esperarTecla();
            return;
        }

        if (cita.getStatus() == AppointmentStatus.CANCELLED) {
            System.out.println("Esta cita ya ha sido cancelada anteriormente.");
            esperarTecla();
            return;
        }

        // Verificar permisos según el rol
        if (usuarioActual instanceof Patient) {
            Patient patient = (Patient) usuarioActual;
            if (!cita.getPatientDocumentNumber().equals(patient.getDocumentNumber())) {
                System.out.println("Error: No tiene permisos para cancelar citas de otros pacientes.");
                esperarTecla();
                return;
            }
        } else if (!(usuarioActual instanceof Doctor)) {
            // Si no es doctor ni paciente, verificar si es administrador (no implementado)
            System.out.println("Error: No tiene permisos para cancelar citas.");
            esperarTecla();
            return;
        }

        // Mostrar detalles de la cita antes de cancelar
        PatientController patientController = new PatientController(diagnosticController.getVitalApp());
        Patient paciente = patientController.buscarPacientePorDocumentoNumero(cita.getPatientDocumentNumber());

        System.out.println("\nDetalles de la cita a cancelar:");
        if (paciente != null) {
            System.out.println("Paciente: " + paciente.getFirstname() + " " + paciente.getLastname());
        }
        System.out.println("Día: " + cita.getDay());
        System.out.println("Horario: " + cita.getHorario());
        System.out.println("Estado actual: " + cita.getStatus());

        System.out.print("\n¿Está seguro que desea cancelar esta cita? (S/N): ");
        String confirmacion = scanner.nextLine();

        if (confirmacion.equalsIgnoreCase("S")) {
            Menu.mostrarProcesando();
            boolean exito = appointmentController.cancelarCita(appointmentId);

            if (exito) {
                System.out.println("\n¡Cita cancelada exitosamente!");
            } else {
                System.out.println("\nError al cancelar la cita.");
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
                    registrarPaciente();
                    break;
                case 2:
                    consultarPacientes();
                    break;
                case 3:
                    actualizarDatosPaciente();
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
     * Gestiona el menú de citas
     */
    private void gestionarMenuCitas() {
        int opcion;

        do {
            mostrarMenuCitas();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    agendarCita();
                    break;
                case 2:
                    consultarCitas();
                    break;
                case 3:
                    cancelarCita();
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