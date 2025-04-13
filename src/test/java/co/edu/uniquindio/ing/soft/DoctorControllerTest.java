package co.edu.uniquindio.ing.soft;

import co.edu.uniquindio.ing.soft.controller.DoctorController;
import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DoctorControllerTest {

    private DoctorController doctorController;
    private VitalApp vitalApp;

    @Before
    public void setUp() {
        List<Doctor> doctors = new ArrayList<>();

        // Add a test doctor to the list
        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@example.com");
        doctor.setPassword("password123");
        doctor.setRole(Role.DOCTOR);
        doctor.setFirstname("John");
        doctor.setLastname("Doe");
        doctor.setDocumentType(DocumentType.CEDULA);
        doctor.setDocumentNumber("1234567890");
        doctor.setProfessionalNumber("MED12345");
        doctors.add(doctor);

        vitalApp = new VitalApp();
        vitalApp.setDoctors(doctors);

        doctorController = new DoctorController(vitalApp);
    }

    @Test
    public void testCrearDoctor() {
        // Test creating a valid doctor
        Doctor nuevoDoctor = doctorController.crearDoctor(
                "newdoctor@example.com",
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "0987654321",
                "MED54321"
        );

        assertNotNull("Debería crearse un nuevo doctor", nuevoDoctor);
        assertEquals("El email debería ser el mismo", "newdoctor@example.com", nuevoDoctor.getEmail());
        assertEquals("La contraseña debería ser la misma", "pass123", nuevoDoctor.getPassword());
        assertEquals("El nombre debería ser el mismo", "Jane", nuevoDoctor.getFirstname());
        assertEquals("El apellido debería ser el mismo", "Smith", nuevoDoctor.getLastname());
        assertEquals("El tipo de documento debería ser el mismo", DocumentType.CEDULA, nuevoDoctor.getDocumentType());
        assertEquals("El número de documento debería ser el mismo", "0987654321", nuevoDoctor.getDocumentNumber());
        assertEquals("El número profesional debería ser el mismo", "MED54321", nuevoDoctor.getProfessionalNumber());
        assertTrue("El doctor debería estar en la lista", doctorController.obtenerTodosDoctores().contains(nuevoDoctor));
    }

    @Test
    public void testCrearDoctorEmailDuplicado() {
        // Test creating a doctor with duplicate email
        Doctor doctorDuplicado = doctorController.crearDoctor(
                "doctor@example.com", // Email ya existente
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "0987654321",
                "MED54321"
        );

        assertNull("No debería crearse un doctor con email duplicado", doctorDuplicado);
    }

    @Test
    public void testCrearDoctorDocumentoDuplicado() {
        // Test creating a doctor with duplicate document
        Doctor doctorDuplicado = doctorController.crearDoctor(
                "newdoctor@example.com",
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "1234567890", // Documento ya existente
                "MED54321"
        );

        assertNull("No debería crearse un doctor con documento duplicado", doctorDuplicado);
    }

    @Test
    public void testCrearDoctorNumeroProfesionalDuplicado() {
        // Test creating a doctor with duplicate professional number
        Doctor doctorDuplicado = doctorController.crearDoctor(
                "newdoctor@example.com",
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "0987654321",
                "MED12345" // Número profesional ya existente
        );

        assertNull("No debería crearse un doctor con número profesional duplicado", doctorDuplicado);
    }

    @Test
    public void testCrearDoctorDatosInvalidos() {
        // Test with null values
        Doctor doctorNulo = doctorController.crearDoctor(
                null,
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "0987654321",
                "MED54321"
        );

        assertNull("No debería crearse un doctor con email nulo", doctorNulo);

        // Test with empty values
        Doctor doctorVacio = doctorController.crearDoctor(
                "newdoctor@example.com",
                "",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "0987654321",
                "MED54321"
        );

        assertNull("No debería crearse un doctor con contraseña vacía", doctorVacio);
    }

    @Test
    public void testBuscarDoctorPorEmail() {
        // Test finding an existing doctor by email
        Doctor doctorEncontrado = doctorController.buscarDoctorPorEmail("doctor@example.com");

        assertNotNull("Debería encontrar el doctor por email", doctorEncontrado);
        assertEquals("El email del doctor encontrado debería coincidir", "doctor@example.com", doctorEncontrado.getEmail());

        // Test with non-existing email
        Doctor doctorNoExistente = doctorController.buscarDoctorPorEmail("nonexistent@example.com");

        assertNull("No debería encontrar un doctor con email no existente", doctorNoExistente);

        // Test with null email
        Doctor doctorEmailNulo = doctorController.buscarDoctorPorEmail(null);

        assertNull("No debería encontrar un doctor con email nulo", doctorEmailNulo);
    }

    @Test
    public void testBuscarDoctorPorDocumento() {
        // Test finding an existing doctor by document
        Doctor doctorEncontrado = doctorController.buscarDoctorPorDocumento(DocumentType.CEDULA, "1234567890");

        assertNotNull("Debería encontrar el doctor por documento", doctorEncontrado);
        assertEquals("El documento del doctor encontrado debería coincidir", "1234567890", doctorEncontrado.getDocumentNumber());

        // Test with non-existing document
        Doctor doctorNoExistente = doctorController.buscarDoctorPorDocumento(DocumentType.CEDULA, "9999999999");

        assertNull("No debería encontrar un doctor con documento no existente", doctorNoExistente);

        // Test with null document
        Doctor doctorDocumentoNulo = doctorController.buscarDoctorPorDocumento(DocumentType.CEDULA, null);

        assertNull("No debería encontrar un doctor con documento nulo", doctorDocumentoNulo);

        // Test with null document type
        Doctor doctorTipoDocumentoNulo = doctorController.buscarDoctorPorDocumento(null, "1234567890");

        assertNull("No debería encontrar un doctor con tipo de documento nulo", doctorTipoDocumentoNulo);
    }

    @Test
    public void testBuscarDoctorPorNumeroProfesional() {
        // Test finding an existing doctor by professional number
        Doctor doctorEncontrado = doctorController.buscarDoctorPorNumeroProfesional("MED12345");

        assertNotNull("Debería encontrar el doctor por número profesional", doctorEncontrado);
        assertEquals("El número profesional del doctor encontrado debería coincidir", "MED12345", doctorEncontrado.getProfessionalNumber());

        // Test with non-existing professional number
        Doctor doctorNoExistente = doctorController.buscarDoctorPorNumeroProfesional("MED99999");

        assertNull("No debería encontrar un doctor con número profesional no existente", doctorNoExistente);

        // Test with null professional number
        Doctor doctorNumeroNulo = doctorController.buscarDoctorPorNumeroProfesional(null);

        assertNull("No debería encontrar un doctor con número profesional nulo", doctorNumeroNulo);
    }

    @Test
    public void testObtenerTodosDoctores() {
        List<Doctor> doctores = doctorController.obtenerTodosDoctores();

        assertNotNull("La lista de doctores no debería ser nula", doctores);
        assertEquals("La lista debería tener el doctor inicial", 1, doctores.size());
        assertEquals("El doctor en la lista debería ser el esperado", "doctor@example.com", doctores.get(0).getEmail());
    }

    @Test
    public void testActualizarDoctor() {
        // Test updating with valid values
        boolean actualizado = doctorController.actualizarDoctor(
                "doctor@example.com",
                "newpassword",
                "JohnUpdated",
                "DoeUpdated",
                "MED12345Updated"
        );

        assertTrue("La actualización debería ser exitosa", actualizado);

        Doctor doctorActualizado = doctorController.buscarDoctorPorEmail("doctor@example.com");
        assertEquals("La contraseña debería actualizarse", "newpassword", doctorActualizado.getPassword());
        assertEquals("El nombre debería actualizarse", "JohnUpdated", doctorActualizado.getFirstname());
        assertEquals("El apellido debería actualizarse", "DoeUpdated", doctorActualizado.getLastname());
        assertEquals("El número profesional debería actualizarse", "MED12345Updated", doctorActualizado.getProfessionalNumber());
    }

    @Test
    public void testActualizarDoctorNoExistente() {
        // Test updating a non-existing doctor
        boolean actualizado = doctorController.actualizarDoctor(
                "nonexistent@example.com",
                "newpassword",
                "JohnUpdated",
                "DoeUpdated",
                "MED12345Updated"
        );

        assertFalse("La actualización debería fallar con email no existente", actualizado);
    }

    @Test
    public void testActualizarDoctorCamposVacios() {
        // Test updating with empty fields (should not change those fields)
        boolean actualizado = doctorController.actualizarDoctor(
                "doctor@example.com",
                "",
                "",
                "",
                ""
        );

        assertTrue("La actualización debería ser exitosa", actualizado);

        Doctor doctorActualizado = doctorController.buscarDoctorPorEmail("doctor@example.com");
        assertEquals("La contraseña no debería cambiar", "password123", doctorActualizado.getPassword());
        assertEquals("El nombre no debería cambiar", "John", doctorActualizado.getFirstname());
        assertEquals("El apellido no debería cambiar", "Doe", doctorActualizado.getLastname());
        assertEquals("El número profesional no debería cambiar", "MED12345", doctorActualizado.getProfessionalNumber());
    }

    @Test
    public void testActualizarDoctorNumeroProfesionalDuplicado() {
        // First, create a new doctor
        doctorController.crearDoctor(
                "newdoctor@example.com",
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CEDULA,
                "0987654321",
                "MED54321"
        );

        // Try to update the first doctor with the professional number of the second doctor
        boolean actualizado = doctorController.actualizarDoctor(
                "doctor@example.com",
                "newpassword",
                "JohnUpdated",
                "DoeUpdated",
                "MED54321" // Número profesional del segundo doctor
        );

        assertFalse("La actualización debería fallar con número profesional duplicado", actualizado);
    }

    @Test
    public void testEliminarDoctor() {
        // Test deleting an existing doctor
        boolean eliminado = doctorController.eliminarDoctor("doctor@example.com");

        assertTrue("La eliminación debería ser exitosa", eliminado);
        assertNull("El doctor no debería encontrarse después de eliminarlo",
                doctorController.buscarDoctorPorEmail("doctor@example.com"));
        assertEquals("La lista de doctores debería estar vacía", 0, doctorController.obtenerTodosDoctores().size());
    }

    @Test
    public void testEliminarDoctorNoExistente() {
        // Test deleting a non-existing doctor
        boolean eliminado = doctorController.eliminarDoctor("nonexistent@example.com");

        assertFalse("La eliminación debería fallar con email no existente", eliminado);
        assertEquals("La lista de doctores no debería cambiar", 1, doctorController.obtenerTodosDoctores().size());
    }
}