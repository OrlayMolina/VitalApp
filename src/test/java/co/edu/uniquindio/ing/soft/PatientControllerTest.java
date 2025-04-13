package co.edu.uniquindio.ing.soft;

import co.edu.uniquindio.ing.soft.controller.PatientController;
import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Gender;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.Patient;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PatientControllerTest {

    private PatientController patientController;
    private VitalApp vitalApp;

    @Before
    public void setUp() {
        List<Patient> patients = new ArrayList<>();

        Patient existingPatient = new Patient();
        existingPatient.setEmail("existing@example.com");
        existingPatient.setPassword("password123");
        existingPatient.setRole(Role.PATIENT);
        existingPatient.setFirstname("Nombre");
        existingPatient.setLastname("Existente");
        existingPatient.setDocumentType(DocumentType.CC);
        existingPatient.setDocumentNumber("1234567890");
        existingPatient.setAge(30);
        existingPatient.setGender(Gender.MALE);
        existingPatient.setAddress("Calle Principal 123");
        existingPatient.setDiagnostics(new ArrayList<>());
        patients.add(existingPatient);

        vitalApp = new VitalApp();
        vitalApp.setPatients(patients);

        patientController = new PatientController(vitalApp);
    }

    @Test
    public void testCrearPaciente() {
        Patient newPatient = patientController.crearPaciente(
                "nuevo@example.com",
                "password456",
                "Nuevo",
                "Paciente",
                DocumentType.TI,
                "0987654321",
                25,
                Gender.FEMALE,
                "Avenida Secundaria 456"
        );

        assertNotNull("El paciente debería crearse correctamente", newPatient);
        assertEquals("El email debería coincidir", "nuevo@example.com", newPatient.getEmail());
        assertEquals("El rol debería ser PATIENT", Role.PATIENT, newPatient.getRole());
        assertEquals("El documento debería coincidir", "0987654321", newPatient.getDocumentNumber());

        Patient retrieved = patientController.buscarPacientePorEmail("nuevo@example.com");
        assertNotNull("El paciente debería encontrarse después de crearlo", retrieved);
    }

    @Test
    public void testCrearPacienteEmailDuplicado() {
        Patient duplicatePatient = patientController.crearPaciente(
                "existing@example.com",
                "otherpassword",
                "Otro",
                "Nombre",
                DocumentType.CC,
                "1111111111",
                40,
                Gender.MALE,
                "Otra Dirección"
        );

        assertNull("No debería permitir crear un paciente con email duplicado", duplicatePatient);
    }

    @Test
    public void testCrearPacienteDocumentoDuplicado() {
        Patient duplicatePatient = patientController.crearPaciente(
                "otro@example.com",
                "otherpassword",
                "Otro",
                "Nombre",
                DocumentType.CC,
                "1234567890",
                40,
                Gender.MALE,
                "Otra Dirección"
        );

        assertNull("No debería permitir crear un paciente con documento duplicado", duplicatePatient);
    }

    @Test
    public void testCrearPacienteDatosInvalidos() {
        // Email vacío
        Patient invalidPatient = patientController.crearPaciente(
                "", "password", "Nombre", "Apellido",
                DocumentType.CC, "12345", 25, Gender.MALE, "Dirección"
        );
        assertNull("No debería permitir crear un paciente con email vacío", invalidPatient);

        // Contraseña vacía
        invalidPatient = patientController.crearPaciente(
                "valido@example.com", "", "Nombre", "Apellido",
                DocumentType.CC, "12345", 25, Gender.MALE, "Dirección"
        );
        assertNull("No debería permitir crear un paciente con contraseña vacía", invalidPatient);

        // Nombre vacío
        invalidPatient = patientController.crearPaciente(
                "valido@example.com", "password", "", "Apellido",
                DocumentType.CC, "12345", 25, Gender.MALE, "Dirección"
        );
        assertNull("No debería permitir crear un paciente con nombre vacío", invalidPatient);

        // DocumentType null
        invalidPatient = patientController.crearPaciente(
                "valido@example.com", "password", "Nombre", "Apellido",
                null, "12345", 25, Gender.MALE, "Dirección"
        );
        assertNull("No debería permitir crear un paciente con documentType null", invalidPatient);

        // Edad inválida
        invalidPatient = patientController.crearPaciente(
                "valido@example.com", "password", "Nombre", "Apellido",
                DocumentType.CC, "12345", -5, Gender.MALE, "Dirección"
        );
        assertNull("No debería permitir crear un paciente con edad inválida", invalidPatient);
    }

    @Test
    public void testBuscarPacientePorEmail() {
        Patient found = patientController.buscarPacientePorEmail("existing@example.com");
        assertNotNull("Debería encontrar el paciente existente", found);
        assertEquals("El email debería coincidir", "existing@example.com", found.getEmail());

        Patient notFound = patientController.buscarPacientePorEmail("nonexistent@example.com");
        assertNull("No debería encontrar un paciente que no existe", notFound);

        Patient nullEmail = patientController.buscarPacientePorEmail(null);
        assertNull("Debería manejar email null", nullEmail);

        Patient emptyEmail = patientController.buscarPacientePorEmail("");
        assertNull("Debería manejar email vacío", emptyEmail);
    }

    @Test
    public void testBuscarPacientePorDocumento() {
        Patient found = patientController.buscarPacientePorDocumento(DocumentType.CC, "1234567890");
        assertNotNull("Debería encontrar el paciente por documento", found);
        assertEquals("El número de documento debería coincidir", "1234567890", found.getDocumentNumber());

        Patient notFound = patientController.buscarPacientePorDocumento(DocumentType.CC, "9999999999");
        assertNull("No debería encontrar un paciente con documento inexistente", notFound);

        Patient wrongType = patientController.buscarPacientePorDocumento(DocumentType.TI, "1234567890");
        assertNull("No debería encontrar un paciente con tipo de documento incorrecto", wrongType);

        Patient nullDoc = patientController.buscarPacientePorDocumento(DocumentType.CC, null);
        assertNull("Debería manejar número de documento null", nullDoc);
    }

    @Test
    public void testBuscarPacientePorDocumentoNumero() {
        Patient found = patientController.buscarPacientePorDocumentoNumero("1234567890");
        assertNotNull("Debería encontrar el paciente por número de documento", found);
        assertEquals("El número de documento debería coincidir", "1234567890", found.getDocumentNumber());

        Patient notFound = patientController.buscarPacientePorDocumentoNumero("9999999999");
        assertNull("No debería encontrar un paciente con número de documento inexistente", notFound);

        Patient nullDoc = patientController.buscarPacientePorDocumentoNumero(null);
        assertNull("Debería manejar número de documento null", nullDoc);
    }

    @Test
    public void testObtenerTodosPacientes() {
        List<Patient> patients = patientController.obtenerTodosPacientes();
        assertNotNull("La lista de pacientes no debería ser null", patients);
        assertEquals("Debería haber un paciente inicialmente", 1, patients.size());

        // Agregar un nuevo paciente
        patientController.crearPaciente(
                "nuevo@example.com", "password456", "Nuevo", "Paciente",
                DocumentType.TI, "0987654321", 25, Gender.FEMALE, "Dirección nueva"
        );

        patients = patientController.obtenerTodosPacientes();
        assertEquals("Debería haber dos pacientes después de agregar uno", 2, patients.size());
    }

    @Test
    public void testActualizarPaciente() {
        boolean updated = patientController.actualizarPaciente(
                "existing@example.com", "newpassword", "NuevoNombre",
                "NuevoApellido", 35, "Nueva Dirección"
        );

        assertTrue("La actualización debería ser exitosa", updated);

        Patient patient = patientController.buscarPacientePorEmail("existing@example.com");
        assertEquals("La contraseña debería actualizarse", "newpassword", patient.getPassword());
        assertEquals("El nombre debería actualizarse", "NuevoNombre", patient.getFirstname());
        assertEquals("El apellido debería actualizarse", "NuevoApellido", patient.getLastname());
        assertEquals("La edad debería actualizarse", 35, patient.getAge());
        assertEquals("La dirección debería actualizarse", "Nueva Dirección", patient.getAddress());
    }

    @Test
    public void testActualizarPacienteNoExistente() {
        boolean updated = patientController.actualizarPaciente(
                "nonexistent@example.com", "newpassword", "NuevoNombre",
                "NuevoApellido", 35, "Nueva Dirección"
        );

        assertFalse("No debería actualizar un paciente inexistente", updated);
    }

    @Test
    public void testActualizarPacienteDatosParciales() {
        // Solo actualizar la contraseña
        boolean updated = patientController.actualizarPaciente(
                "existing@example.com", "newpassword", "", "", 0, ""
        );

        assertTrue("La actualización parcial debería ser exitosa", updated);

        Patient patient = patientController.buscarPacientePorEmail("existing@example.com");
        assertEquals("La contraseña debería actualizarse", "newpassword", patient.getPassword());
        assertEquals("El nombre no debería cambiar", "Nombre", patient.getFirstname());
        assertEquals("El apellido no debería cambiar", "Existente", patient.getLastname());
        assertEquals("La edad no debería cambiar", 30, patient.getAge());
        assertEquals("La dirección no debería cambiar", "Calle Principal 123", patient.getAddress());
    }

    @Test
    public void testEliminarPaciente() {
        boolean deleted = patientController.eliminarPaciente("existing@example.com");
        assertTrue("La eliminación debería ser exitosa", deleted);

        Patient patient = patientController.buscarPacientePorEmail("existing@example.com");
        assertNull("El paciente no debería existir después de eliminarlo", patient);

        List<Patient> patients = patientController.obtenerTodosPacientes();
        assertEquals("No debería haber pacientes después de eliminar el único existente", 0, patients.size());
    }

    @Test
    public void testEliminarPacienteNoExistente() {
        boolean deleted = patientController.eliminarPaciente("nonexistent@example.com");
        assertFalse("No debería eliminar un paciente inexistente", deleted);
    }

    @Test
    public void testAgregarDiagnosticoAPaciente() {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setCode("DIAG001");
        diagnostic.setDescription("Diagnóstico de prueba");

        patientController.agregarDiagnosticoAPaciente("1234567890", diagnostic);

        Patient patient = patientController.buscarPacientePorDocumentoNumero("1234567890");
        assertNotNull("El paciente debería existir", patient);
        assertEquals("El paciente debería tener un diagnóstico", 1, patient.getDiagnostics().size());
        assertEquals("El código del diagnóstico debería coincidir", "DIAG001", patient.getDiagnostics().get(0).getCode());
    }

    @Test
    public void testAgregarDiagnosticoDuplicado() {
        Diagnostic diagnostic1 = new Diagnostic();
        diagnostic1.setCode("DIAG001");
        diagnostic1.setDescription("Diagnóstico de prueba");

        patientController.agregarDiagnosticoAPaciente("1234567890", diagnostic1);

        // Intentar agregar el mismo diagnóstico nuevamente
        Diagnostic diagnostic2 = new Diagnostic();
        diagnostic2.setCode("DIAG001");
        diagnostic2.setDescription("Descripción diferente");

        patientController.agregarDiagnosticoAPaciente("1234567890", diagnostic2);

        Patient patient = patientController.buscarPacientePorDocumentoNumero("1234567890");
        assertEquals("No debería agregar diagnósticos duplicados", 1, patient.getDiagnostics().size());
    }

    @Test
    public void testAgregarDiagnosticoAPacienteInexistente() {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setCode("DIAG001");
        diagnostic.setDescription("Diagnóstico de prueba");

        // Esto no debería lanzar excepción, simplemente no hacer nada
        patientController.agregarDiagnosticoAPaciente("9999999999", diagnostic);

        // Verificar que el paciente existente no se vio afectado
        Patient patient = patientController.buscarPacientePorDocumentoNumero("1234567890");
        assertEquals("El paciente existente no debería tener diagnósticos", 0, patient.getDiagnostics().size());
    }
}