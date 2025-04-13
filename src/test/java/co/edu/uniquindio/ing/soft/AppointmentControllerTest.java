package co.edu.uniquindio.ing.soft;

import co.edu.uniquindio.ing.soft.controller.AppointmentController;
import co.edu.uniquindio.ing.soft.enums.AppointmentStatus;
import co.edu.uniquindio.ing.soft.enums.Day;
import co.edu.uniquindio.ing.soft.model.Appointment;
import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.Patient;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class AppointmentControllerTest {

    private AppointmentController appointmentController;
    private VitalApp vitalApp;
    private Patient patient;
    private Doctor doctor;
    private Diagnostic diagnostic;

    @Before
    public void setUp() {
        // Inicializar VitalApp con datos de prueba
        vitalApp = new VitalApp();

        // Crear paciente de prueba
        patient = new Patient();
        patient.setDocumentNumber("123456789");
        patient.setFirstname("Juan");
        patient.setLastname("Pérez");
        patient.setEmail("juan@example.com");

        List<Patient> patients = new ArrayList<>();
        patients.add(patient);
        vitalApp.setPatients(patients);

        // Crear doctor de prueba
        doctor = new Doctor();
        doctor.setDocumentNumber("987654321");
        doctor.setFirstname("María");
        doctor.setLastname("Gómez");
        doctor.setSpeciality("Cardiología");

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);
        vitalApp.setDoctors(doctors);

        // Crear diagnóstico de prueba
        diagnostic = new Diagnostic();
        diagnostic.setCode("D001");
        diagnostic.setDiagnostic("Hipertensión");
        diagnostic.setDescription("Presión arterial alta");

        List<Diagnostic> diagnostics = new ArrayList<>();
        diagnostics.add(diagnostic);
        vitalApp.setDiagnostics(diagnostics);

        // Inicializar lista de citas
        vitalApp.setAppointments(new ArrayList<>());

        // Crear controlador con los datos de prueba
        appointmentController = new AppointmentController(vitalApp);
    }

    @Test
    public void testCrearCita() {
        // Datos para la cita
        String patientDocument = "123456789";
        Day day = Day.MARTES;
        String horario = "10:00";
        List<String> diagnosticCodes = Arrays.asList("D001");

        // Crear la cita
        Appointment cita = appointmentController.crearCita(patientDocument, doctor, day, horario, diagnosticCodes);

        // Verificar que la cita se creó correctamente
        assertNotNull("La cita debería haberse creado", cita);
        assertEquals("El paciente debería ser el correcto", patientDocument, cita.getPatientDocumentNumber());
        assertEquals("El doctor debería ser el correcto", doctor, cita.getDoctorDocumentNumber());
        assertEquals("El día debería ser el correcto", day, cita.getDay());
        assertEquals("El horario debería ser el correcto", horario, cita.getHorario());
        assertEquals("El estado inicial debería ser SCHEDULED", AppointmentStatus.SCHEDULED, cita.getStatus());

        // Verificar que la cita se agregó a la lista en VitalApp
        assertEquals("Debería haber una cita en la lista", 1, vitalApp.getAppointments().size());
    }

    @Test
    public void testCrearCitaConDatosInvalidos() {
        // Probar con paciente null
        Appointment cita = appointmentController.crearCita(null, doctor, Day.LUNES, "10:00", null);
        assertNull("No debería crearse cita con paciente null", cita);

        // Probar con doctor null
        cita = appointmentController.crearCita("123456789", null, Day.LUNES, "10:00", null);
        assertNull("No debería crearse cita con doctor null", cita);

        // Probar con día null
        cita = appointmentController.crearCita("123456789", doctor, null, "10:00", null);
        assertNull("No debería crearse cita con día null", cita);

        // Probar con horario null o vacío
        cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, null, null);
        assertNull("No debería crearse cita con horario null", cita);

        cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "", null);
        assertNull("No debería crearse cita con horario vacío", cita);
    }

    @Test
    public void testCrearCitaConPacienteInexistente() {
        // Intentar crear cita con un paciente que no existe
        Appointment cita = appointmentController.crearCita("999999999", doctor, Day.LUNES, "10:00", null);
        assertNull("No debería crearse cita con paciente inexistente", cita);
    }

    @Test
    public void testCrearCitaHorarioOcupado() {
        // Crear primera cita
        appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);

        // Intentar crear otra cita con el mismo doctor, día y horario
        Appointment segundaCita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        assertNull("No debería crearse cita con horario ocupado", segundaCita);
    }

    @Test
    public void testBuscarCitaPorId() {
        // Crear una cita
        Appointment cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        assertNotNull(cita);

        // Buscar la cita por su ID
        Appointment citaEncontrada = appointmentController.buscarCitaPorId(cita.getAppointmentId());
        assertNotNull("Debería encontrarse la cita", citaEncontrada);
        assertEquals("Debería ser la misma cita", cita.getAppointmentId(), citaEncontrada.getAppointmentId());

        // Buscar una cita con ID inexistente
        Appointment citaInexistente = appointmentController.buscarCitaPorId("ID_INEXISTENTE");
        assertNull("No debería encontrarse la cita inexistente", citaInexistente);

        // Buscar con ID null o vacío
        assertNull("No debería encontrarse cita con ID null", appointmentController.buscarCitaPorId(null));
        assertNull("No debería encontrarse cita con ID vacío", appointmentController.buscarCitaPorId(""));
    }

    @Test
    public void testObtenerCitasPorPaciente() {
        // Crear varias citas para el mismo paciente
        appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        appointmentController.crearCita("123456789", doctor, Day.MARTES, "11:00", null);

        // Obtener citas del paciente
        List<Appointment> citasPaciente = appointmentController.obtenerCitasPorPaciente("123456789");
        assertEquals("Deberían haber 2 citas para el paciente", 2, citasPaciente.size());

        // Verificar con paciente sin citas
        List<Appointment> citasPacienteInexistente = appointmentController.obtenerCitasPorPaciente("999999999");
        assertTrue("La lista de citas debería estar vacía", citasPacienteInexistente.isEmpty());

        // Verificar con paciente null o vacío
        assertTrue("La lista debería estar vacía con paciente null", appointmentController.obtenerCitasPorPaciente(null).isEmpty());
        assertTrue("La lista debería estar vacía con paciente vacío", appointmentController.obtenerCitasPorPaciente("").isEmpty());
    }

    @Test
    public void testObtenerCitasPorDoctor() {
        // Crear citas para un doctor
        appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        appointmentController.crearCita("123456789", doctor, Day.MARTES, "11:00", null);

        // Obtener citas del doctor
        List<Appointment> citasDoctor = appointmentController.obtenerCitasPorDoctor(doctor);
        assertEquals("Deberían haber 2 citas para el doctor", 2, citasDoctor.size());

        // Crear otro doctor sin citas
        Doctor otroDoctor = new Doctor();
        otroDoctor.setDocumentNumber("111222333");
        List<Appointment> citasOtroDoctor = appointmentController.obtenerCitasPorDoctor(otroDoctor);
        assertTrue("La lista de citas debería estar vacía", citasOtroDoctor.isEmpty());

        // Verificar con doctor null
        assertTrue("La lista debería estar vacía con doctor null", appointmentController.obtenerCitasPorDoctor(null).isEmpty());
    }

    @Test
    public void testObtenerCitasPorDia() {
        // Crear citas para diferentes días
        appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        appointmentController.crearCita("123456789", doctor, Day.LUNES, "11:00", null);
        appointmentController.crearCita("123456789", doctor, Day.MARTES, "10:00", null);

        // Obtener citas para el lunes
        List<Appointment> citasLunes = appointmentController.obtenerCitasPorDia(Day.LUNES);
        assertEquals("Deberían haber 2 citas para el lunes", 2, citasLunes.size());

        // Obtener citas para el martes
        List<Appointment> citasMartes = appointmentController.obtenerCitasPorDia(Day.MARTES);
        assertEquals("Debería haber 1 cita para el martes", 1, citasMartes.size());

        // Verificar con día sin citas
        List<Appointment> citasMiercoles = appointmentController.obtenerCitasPorDia(Day.MIERCOLES);
        assertTrue("La lista de citas debería estar vacía", citasMiercoles.isEmpty());

        // Verificar con día null
        assertTrue("La lista debería estar vacía con día null", appointmentController.obtenerCitasPorDia(null).isEmpty());
    }

    @Test
    public void testCambiarEstadoCita() {
        // Crear una cita
        Appointment cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        assertNotNull(cita);

        // Cambiar estado a COMPLETED
        boolean resultado = appointmentController.cambiarEstadoCita(cita.getAppointmentId(), AppointmentStatus.COMPLETED, Arrays.asList("D001"));
        assertTrue("Debería cambiar el estado correctamente", resultado);

        // Verificar que el estado cambió
        Appointment citaActualizada = appointmentController.buscarCitaPorId(cita.getAppointmentId());
        assertEquals("El estado debería ser COMPLETED", AppointmentStatus.COMPLETED, citaActualizada.getStatus());

        // Intentar cambiar estado de una cita inexistente
        resultado = appointmentController.cambiarEstadoCita("ID_INEXISTENTE", AppointmentStatus.CANCELLED, null);
        assertFalse("No debería cambiar el estado de una cita inexistente", resultado);
    }

    @Test
    public void testReprogramarCita() {
        // Crear una cita
        Appointment cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        assertNotNull(cita);

        // Reprogramar la cita
        boolean resultado = appointmentController.reprogramarCita(cita.getAppointmentId(), Day.MIERCOLES, "14:00");
        assertTrue("Debería reprogramar la cita correctamente", resultado);

        // Verificar que la cita fue reprogramada
        Appointment citaReprogramada = appointmentController.buscarCitaPorId(cita.getAppointmentId());
        assertEquals("El día debería ser WEDNESDAY", Day.MIERCOLES, citaReprogramada.getDay());
        assertEquals("El horario debería ser 14:00", "14:00", citaReprogramada.getHorario());
        assertEquals("El estado debería ser RESCHEDULED", AppointmentStatus.RESCHEDULED, citaReprogramada.getStatus());

        // Intentar reprogramar a un horario ocupado
        appointmentController.crearCita("123456789", doctor, Day.JUEVES, "15:00", null);
        resultado = appointmentController.reprogramarCita(cita.getAppointmentId(), Day.JUEVES, "15:00");
        assertFalse("No debería reprogramar a un horario ocupado", resultado);

        // Verificar que no se realizaron cambios
        citaReprogramada = appointmentController.buscarCitaPorId(cita.getAppointmentId());
        assertEquals("El día debería seguir siendo WEDNESDAY", Day.MIERCOLES, citaReprogramada.getDay());

        // Reprogramar una cita inexistente
        resultado = appointmentController.reprogramarCita("ID_INEXISTENTE", Day.VIERNES, "16:00");
        assertFalse("No debería reprogramar una cita inexistente", resultado);
    }

    @Test
    public void testReprogramarCitaCompletada() {
        // Crear una cita
        Appointment cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        assertNotNull(cita);

        // Marcar la cita como completada
        appointmentController.cambiarEstadoCita(cita.getAppointmentId(), AppointmentStatus.COMPLETED, null);

        // Intentar reprogramar una cita completada
        boolean resultado = appointmentController.reprogramarCita(cita.getAppointmentId(), Day.MIERCOLES, "14:00");
        assertFalse("No debería reprogramar una cita completada", resultado);
    }

    @Test
    public void testCancelarCita() {
        // Crear una cita
        Appointment cita = appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        assertNotNull(cita);

        // Cancelar la cita
        boolean resultado = appointmentController.cancelarCita(cita.getAppointmentId());
        assertTrue("Debería cancelar la cita correctamente", resultado);

        // Verificar que la cita fue cancelada
        Appointment citaCancelada = appointmentController.buscarCitaPorId(cita.getAppointmentId());
        assertEquals("El estado debería ser CANCELLED", AppointmentStatus.CANCELLED, citaCancelada.getStatus());

        // Intentar cancelar una cita inexistente
        resultado = appointmentController.cancelarCita("ID_INEXISTENTE");
        assertFalse("No debería cancelar una cita inexistente", resultado);
    }

    @Test
    public void testObtenerTodasCitas() {
        // Inicialmente no hay citas
        List<Appointment> todasCitas = appointmentController.obtenerTodasCitas();
        assertTrue("La lista inicial de citas debería estar vacía", todasCitas.isEmpty());

        // Crear varias citas
        appointmentController.crearCita("123456789", doctor, Day.LUNES, "10:00", null);
        appointmentController.crearCita("123456789", doctor, Day.MARTES, "11:00", null);
        appointmentController.crearCita("123456789", doctor, Day.MIERCOLES, "12:00", null);

        // Obtener todas las citas
        todasCitas = appointmentController.obtenerTodasCitas();
        assertEquals("Deberían haber 3 citas en total", 3, todasCitas.size());
    }
}