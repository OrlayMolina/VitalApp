package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.Patient;
import co.edu.uniquindio.ing.soft.model.User;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.enums.Role;

import java.util.List;

public class UserController {

    private final VitalApp vitalApp;

    public UserController(VitalApp vitalApp) {
        this.vitalApp = vitalApp;
    }

    /**
     * Autentica un usuario por su email y contraseña
     *
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return El usuario autenticado o null si las credenciales son inválidas
     */
    public User autenticarUsuario(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        // Buscar entre los doctores
        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getEmail() != null && doctor.getEmail().equals(email) &&
                    doctor.getPassword() != null && doctor.getPassword().equals(password)) {
                return doctor;
            }
        }

        // Buscar entre los pacientes
        for (Patient patient : vitalApp.getPatients()) {
            if (patient.getEmail() != null && patient.getEmail().equals(email) &&
                    patient.getPassword() != null && patient.getPassword().equals(password)) {
                return patient;
            }
        }

        return null; // No se encontró ningún usuario con esas credenciales
    }

    /**
     * Obtiene el rol del usuario autenticado
     *
     * @param usuario Usuario autenticado
     * @return El rol del usuario o null si el usuario es null
     */
    public Role obtenerRolUsuario(User usuario) {
        if (usuario == null) {
            return null;
        }

        if (usuario instanceof Doctor) {
            return ((Doctor) usuario).getRole();
        } else if (usuario instanceof Patient) {
            return ((Patient) usuario).getRole();
        }

        return null;
    }

    public User buscarUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        List<Doctor> doctorList = vitalApp.getDoctors();
        List<Patient> patientList = vitalApp.getPatients();
        for (Doctor doctor : doctorList) {
            if (doctor.getEmail() != null && doctor.getEmail().equals(email)) {
                return doctor;
            }
        }

        for (Patient patient : patientList) {
            if(patient.getEmail() != null && patient.getEmail().equals(email)) {
                return patient;
            }
        }

        return null;
    }
}