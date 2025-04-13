package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.enums.AppointmentStatus;
import co.edu.uniquindio.ing.soft.enums.Day;
import co.edu.uniquindio.ing.soft.model.Appointment;
import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.Patient;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.utils.Persistencia;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class AppointmentController {

    /**
     * -- GETTER --
     * Obtiene la instancia de VitalApp
     *
     * @return La instancia de VitalApp
     */
    private VitalApp vitalApp;
    private final PatientController patientController;
    private final DiagnosticController diagnosticController;

    /**
     * Constructor que inicializa el controlador cargando los datos de la persistencia
     */
    public AppointmentController() {
        this.vitalApp = Persistencia.cargarRecursoVitalAppXML();

        if (this.vitalApp == null) {
            this.vitalApp = new VitalApp();
        }

        this.patientController = new PatientController(this.vitalApp);
        this.diagnosticController = new DiagnosticController(this.vitalApp);
    }

    /**
     * Constructor con parámetro para facilitar pruebas unitarias
     * @param vitalApp Instancia de VitalApp
     */
    public AppointmentController(VitalApp vitalApp) {
        this.vitalApp = vitalApp;
        this.patientController = new PatientController(vitalApp);
        this.diagnosticController = new DiagnosticController(vitalApp);
    }

    /**
     * Crea una nueva cita
     *
     * @param patientDocumentNumber Número de documento del paciente
     * @param doctorDocumentNumber Doctor asignado a la cita
     * @param day Día de la cita
     * @param horario Horario de la cita
     * @param diagnosticCodes Lista de códigos de diagnósticos asociados a la cita
     * @return La cita creada o null si ocurrió un error
     */
    public Appointment crearCita(String patientDocumentNumber, Doctor doctorDocumentNumber,
                                 Day day, String horario, List<String> diagnosticCodes) {

        if (patientDocumentNumber == null || patientDocumentNumber.trim().isEmpty() ||
                doctorDocumentNumber == null || day == null ||
                horario == null || horario.trim().isEmpty()) {
            return null;
        }

        Patient paciente = patientController.buscarPacientePorDocumentoNumero(patientDocumentNumber);
        if (paciente == null) {
            return null;
        }

        for (Appointment cita : vitalApp.getAppointments()) {
            if (cita.getDoctorDocumentNumber().equals(doctorDocumentNumber) &&
                    cita.getDay() == day &&
                    cita.getHorario().equals(horario) &&
                    cita.getStatus() != AppointmentStatus.CANCELLED) {
                return null;
            }
        }

        String appointmentId = UUID.randomUUID().toString();

        Appointment nuevaCita = new Appointment();
        nuevaCita.setAppointmentId(appointmentId);
        nuevaCita.setPatientDocumentNumber(patientDocumentNumber);
        nuevaCita.setDoctorDocumentNumber(doctorDocumentNumber);
        nuevaCita.setDay(day);
        nuevaCita.setHorario(horario);
        nuevaCita.setStatus(AppointmentStatus.SCHEDULED);

        vitalApp.getAppointments().add(nuevaCita);

        if (diagnosticCodes != null && !diagnosticCodes.isEmpty()) {
            for (String diagnosticCode : diagnosticCodes) {
                Diagnostic diagnostic = diagnosticController.buscarDiagnosticoPorId(diagnosticCode);
                if (diagnostic != null) {
                    patientController.agregarDiagnosticoAPaciente(patientDocumentNumber, diagnostic);
                }
            }
        }

        guardarCambios();

        return nuevaCita;
    }

    /**
     * Busca una cita por su ID
     *
     * @param appointmentId ID de la cita
     * @return La cita encontrada o null si no existe
     */
    public Appointment buscarCitaPorId(String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            return null;
        }

        for (Appointment cita : vitalApp.getAppointments()) {
            if (cita.getAppointmentId().equals(appointmentId)) {
                return cita;
            }
        }

        return null;
    }

    /**
     * Obtiene todas las citas de un paciente
     *
     * @param patientDocumentNumber Número de documento del paciente
     * @return Lista de citas del paciente
     */
    public List<Appointment> obtenerCitasPorPaciente(String patientDocumentNumber) {
        if (patientDocumentNumber == null || patientDocumentNumber.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Appointment> citasPaciente = new ArrayList<>();
        for (Appointment cita : vitalApp.getAppointments()) {
            if (cita.getPatientDocumentNumber().equals(patientDocumentNumber)) {
                citasPaciente.add(cita);
            }
        }

        return citasPaciente;
    }

    /**
     * Obtiene todas las citas de un doctor
     *
     * @param doctorDocumentNumber Doctor a consultar
     * @return Lista de citas del doctor
     */
    public List<Appointment> obtenerCitasPorDoctor(Doctor doctorDocumentNumber) {
        if (doctorDocumentNumber == null) {
            return new ArrayList<>();
        }

        List<Appointment> citasDoctor = new ArrayList<>();
        for (Appointment cita : vitalApp.getAppointments()) {
            if (cita.getDoctorDocumentNumber().equals(doctorDocumentNumber)) {
                citasDoctor.add(cita);
            }
        }

        return citasDoctor;
    }

    /**
     * Obtiene todas las citas para un día específico
     *
     * @param day Día a consultar
     * @return Lista de citas para ese día
     */
    public List<Appointment> obtenerCitasPorDia(Day day) {
        if (day == null) {
            return new ArrayList<>();
        }

        List<Appointment> citasDia = new ArrayList<>();
        for (Appointment cita : vitalApp.getAppointments()) {
            if (cita.getDay() == day) {
                citasDia.add(cita);
            }
        }

        return citasDia;
    }

    /**
     * Cambiar el estado de una cita
     *
     * @param appointmentId ID de la cita
     * @param nuevoStatus Nuevo estado de la cita
     * @param diagnosticCodes Lista de códigos de diagnósticos a agregar (para citas completadas)
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean cambiarEstadoCita(String appointmentId, AppointmentStatus nuevoStatus, List<String> diagnosticCodes) {
        Appointment cita = buscarCitaPorId(appointmentId);

        if (cita == null) {
            return false;
        }

        cita.setStatus(nuevoStatus);

        if (nuevoStatus == AppointmentStatus.COMPLETED && diagnosticCodes != null && !diagnosticCodes.isEmpty()) {
            for (String diagnosticCode : diagnosticCodes) {
                Diagnostic diagnostic = diagnosticController.buscarDiagnosticoPorId(diagnosticCode);
                if (diagnostic != null) {
                    patientController.agregarDiagnosticoAPaciente(cita.getPatientDocumentNumber(), diagnostic);
                }
            }
        }

        guardarCambios();

        return true;
    }

    /**
     * Reprogramar una cita (cambiar día y/o horario)
     *
     * @param appointmentId ID de la cita
     * @param newDay Nuevo día (null para no cambiar)
     * @param newHorario Nuevo horario (null o vacío para no cambiar)
     * @return true si se reprogramó correctamente, false en caso contrario
     */
    public boolean reprogramarCita(String appointmentId, Day newDay, String newHorario) {
        Appointment cita = buscarCitaPorId(appointmentId);

        if (cita == null || cita.getStatus() == AppointmentStatus.COMPLETED) {
            return false;
        }

        if ((newDay == null || newDay == cita.getDay()) &&
                (newHorario == null || newHorario.trim().isEmpty() || newHorario.equals(cita.getHorario()))) {
            return true;
        }

        Day diaActualizado = (newDay != null) ? newDay : cita.getDay();
        String horarioActualizado = (newHorario != null && !newHorario.trim().isEmpty()) ? newHorario : cita.getHorario();

        for (Appointment otraCita : vitalApp.getAppointments()) {
            if (!otraCita.getAppointmentId().equals(appointmentId) &&
                    otraCita.getDoctorDocumentNumber().equals(cita.getDoctorDocumentNumber()) &&
                    otraCita.getDay() == diaActualizado &&
                    otraCita.getHorario().equals(horarioActualizado) &&
                    otraCita.getStatus() != AppointmentStatus.CANCELLED) {
                return false;
            }
        }

        cita.setDay(diaActualizado);
        cita.setHorario(horarioActualizado);
        cita.setStatus(AppointmentStatus.RESCHEDULED);

        guardarCambios();

        return true;
    }

    /**
     * Cancelar una cita
     *
     * @param appointmentId ID de la cita a cancelar
     * @return true si se canceló correctamente, false en caso contrario
     */
    public boolean cancelarCita(String appointmentId) {
        return cambiarEstadoCita(appointmentId, AppointmentStatus.CANCELLED, null);
    }

    /**
     * Obtiene todas las citas
     *
     * @return Lista de todas las citas
     */
    public List<Appointment> obtenerTodasCitas() {
        return new ArrayList<>(vitalApp.getAppointments());
    }

    /**
     * Guarda los cambios en la persistencia
     */
    private void guardarCambios() {
        Persistencia.guardarRecursoVitalAppXML(vitalApp);
    }
}