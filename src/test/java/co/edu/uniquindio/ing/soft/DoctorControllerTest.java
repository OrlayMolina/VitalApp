package co.edu.uniquindio.ing.soft;

import co.edu.uniquindio.ing.soft.controller.DoctorController;
import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.enums.Speciality;
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

        // Doctor de prueba con especialidad CARDIOLOGIA
        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@example.com");
        doctor.setPassword("password123");
        doctor.setRole(Role.DOCTOR);
        doctor.setFirstname("John");
        doctor.setLastname("Doe");
        doctor.setDocumentType(DocumentType.CC);
        doctor.setDocumentNumber("1234567890");
        doctor.setProfessionalNumber("MED12345");
        doctor.setSpeciality(Speciality.CARDIOLOGIA.name()); // Usando name() para obtener el String

        doctors.add(doctor);

        vitalApp = new VitalApp();
        vitalApp.setDoctors(doctors);

        doctorController = new DoctorController(vitalApp);
    }

    @Test
    public void testCrearDoctor() {
        Doctor nuevoDoctor = doctorController.crearDoctor(
                "newdoctor@example.com",
                "pass123",
                "Jane",
                "Smith",
                DocumentType.CC,
                "0987654321",
                "MED54321",
                Speciality.PEDIATRIA.name() // Especialidad PEDIATRIA
        );

        assertNotNull("Debería crearse un nuevo doctor", nuevoDoctor);
        assertEquals("Especialidad debería ser PEDIATRIA", Speciality.PEDIATRIA.name(), nuevoDoctor.getSpeciality());
    }

    @Test
    public void testCrearDoctorEspecialidadInvalida() {
        // Test con especialidad no existente en el enum
        Doctor doctorInvalido = doctorController.crearDoctor(
                "invalid@example.com",
                "pass123",
                "Invalid",
                "Doctor",
                DocumentType.CC,
                "1111111111",
                "MED11111",
                "ESPECIALIDAD_INEXISTENTE" // Especialidad no válida
        );

        assertNull("No debería crearse doctor con especialidad inválida", doctorInvalido);
    }

    @Test
    public void testActualizarEspecialidad() {
        boolean actualizado = doctorController.actualizarDoctor(
                "doctor@example.com",
                null, // No cambiar
                null, // No cambiar
                null, // No cambiar
                null, // No cambiar
                Speciality.PEDIATRIA.name() // Cambiar especialidad
        );

        assertTrue("Debería actualizarse la especialidad", actualizado);
        Doctor doctorActualizado = doctorController.buscarDoctorPorEmail("doctor@example.com");
        assertEquals("Especialidad debería ser PEDIATRIA", Speciality.PEDIATRIA.name(), doctorActualizado.getSpeciality());
    }

    @Test
    public void testBuscarPorEspecialidad() {
        // Crear un segundo doctor con PEDIATRIA
        doctorController.crearDoctor(
                "pediatra@example.com",
                "pass123",
                "Pedro",
                "Pediatra",
                DocumentType.CC,
                "2222222222",
                "MED22222",
                Speciality.PEDIATRIA.name()
        );

        List<Doctor> pediatras = new ArrayList<>();
        for (Doctor d : doctorController.obtenerTodosDoctores()) {
            if (Speciality.PEDIATRIA.name().equals(d.getSpeciality())) {
                pediatras.add(d);
            }
        }

        assertEquals("Debería haber 1 pediatra", 1, pediatras.size());
        assertEquals("Email del pediatra debería coincidir", "pediatra@example.com", pediatras.get(0).getEmail());
    }
}