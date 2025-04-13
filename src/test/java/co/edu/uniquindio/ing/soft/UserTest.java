package co.edu.uniquindio.ing.soft;

import co.edu.uniquindio.ing.soft.controller.UserController;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.Patient;
import co.edu.uniquindio.ing.soft.model.User;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {

    private UserController userController;

    @Before
    public void setUp() {
        List<Doctor> doctors = new ArrayList<>();
        List<Patient> patients = new ArrayList<>();

        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@example.com");
        doctor.setPassword("password123");
        doctor.setRole(Role.DOCTOR);
        doctors.add(doctor);

        Patient patient = new Patient();
        patient.setEmail("patient@example.com");
        patient.setPassword("password456");
        patient.setRole(Role.PATIENT);
        patients.add(patient);

        VitalApp vitalApp = new VitalApp();
        vitalApp.setDoctors(doctors);
        vitalApp.setPatients(patients);

        userController = new UserController(vitalApp);
    }

    @Test
    public void testAutenticarUsuarioDoctor() {
        User user = userController.autenticarUsuario("doctor@example.com", "password123");
        assertNotNull("El doctor debería autenticarse correctamente", user);
        assertTrue("El usuario debería ser un Doctor", user instanceof Doctor);

        Role role = userController.obtenerRolUsuario(user);
        assertEquals("El rol debería ser DOCTOR", Role.DOCTOR, role);
    }

    @Test
    public void testAutenticarUsuarioPatient() {
        User user = userController.autenticarUsuario("patient@example.com", "password456");
        assertNotNull("El paciente debería autenticarse correctamente", user);
        assertTrue("El usuario debería ser un Patient", user instanceof Patient);

        Role role = userController.obtenerRolUsuario(user);
        assertEquals("El rol debería ser PATIENT", Role.PATIENT, role);
    }

    @Test
    public void testCredencialesInvalidas() {
        User user = userController.autenticarUsuario("doctor@example.com", "wrongpassword");
        assertNull("La autenticación debería fallar con contraseña incorrecta", user);

        user = userController.autenticarUsuario("wrongemail@example.com", "password123");
        assertNull("La autenticación debería fallar con email incorrecto", user);
    }

    @Test
    public void testCredencialesVacias() {
        User user = userController.autenticarUsuario("", "password123");
        assertNull("La autenticación debería fallar con email vacío", user);

        user = userController.autenticarUsuario("doctor@example.com", "");
        assertNull("La autenticación debería fallar con contraseña vacía", user);

        user = userController.autenticarUsuario(null, "password123");
        assertNull("La autenticación debería fallar con email null", user);

        user = userController.autenticarUsuario("doctor@example.com", null);
        assertNull("La autenticación debería fallar con contraseña null", user);
    }

    @Test
    public void testObtenerRolUsuario() {
        Role role = userController.obtenerRolUsuario(null);
        assertNull("El rol debería ser null si el usuario es null", role);
    }
}