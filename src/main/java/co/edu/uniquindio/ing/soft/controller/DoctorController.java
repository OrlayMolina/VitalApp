package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.enums.Speciality;
import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.utils.Persistencia;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DoctorController {

    /**
     * Instancia de VitalApp que maneja el controlador
     */
    private VitalApp vitalApp;

    /**
     * Constructor que inicializa el controlador cargando los datos de la persistencia.
     * Si no existe datos previos, crea una nueva instancia de VitalApp.
     */
    public DoctorController() {
        this.vitalApp = Persistencia.cargarRecursoVitalAppXML();

        if (this.vitalApp == null) {
            this.vitalApp = new VitalApp();
        }
    }

    /**
     * Constructor para pruebas unitarias que recibe una instancia de VitalApp
     * @param vitalApp Instancia de VitalApp a utilizar
     */
    public DoctorController(VitalApp vitalApp) {
        this.vitalApp = vitalApp;
    }

    /**
     * Crea un nuevo doctor y lo añade al sistema después de validar todos los campos
     * @param email Email del doctor (debe ser único)
     * @param password Contraseña del doctor
     * @param firstname Nombre del doctor
     * @param lastname Apellido del doctor
     * @param documentType Tipo de documento (enum DocumentType)
     * @param documentNumber Número de documento (debe ser único para el tipo)
     * @param professionalNumber Número profesional (debe ser único)
     * @param speciality Especialidad médica (debe ser un valor válido del enum Speciality)
     * @return El doctor creado o null si hay algún error de validación
     */
    public Doctor crearDoctor(String email, String password, String firstname, String lastname,
                              DocumentType documentType, String documentNumber,
                              String professionalNumber, String speciality) {

        // Validación básica de campos obligatorios
        if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                firstname == null || firstname.trim().isEmpty() ||
                lastname == null || lastname.trim().isEmpty() ||
                documentType == null ||
                documentNumber == null || documentNumber.trim().isEmpty() ||
                professionalNumber == null || professionalNumber.trim().isEmpty() ||
                speciality == null || speciality.trim().isEmpty()) {
            return null;
        }

        // Validación de especialidad médica
        try {
            Speciality.valueOf(speciality.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Especialidad no existe en el enum
        }

        // Inicialización segura de lista de doctores
        if (vitalApp.getDoctors() == null) {
            vitalApp.setDoctors(new ArrayList<>());
        }

        // Validación de datos únicos
        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getEmail().equalsIgnoreCase(email)) {
                return null; // Email ya existe
            }
            if (doctor.getDocumentNumber().equals(documentNumber) &&
                    doctor.getDocumentType() == documentType) {
                return null; // Documento ya existe
            }
            if (doctor.getProfessionalNumber().equalsIgnoreCase(professionalNumber)) {
                return null; // Número profesional ya existe
            }
        }

        // Creación del nuevo doctor
        Doctor nuevoDoctor = new Doctor(email, password, Role.DOCTOR, firstname, lastname,
                documentType, documentNumber, professionalNumber, speciality);

        vitalApp.getDoctors().add(nuevoDoctor);
        guardarCambios();

        return nuevoDoctor;
    }

    /**
     * Busca un doctor por su dirección de email
     * @param email Email a buscar
     * @return El doctor encontrado o null si no existe
     */
    public Doctor buscarDoctorPorEmail(String email) {
        if (email == null || email.trim().isEmpty() || vitalApp.getDoctors() == null) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getEmail().equalsIgnoreCase(email)) {
                return doctor;
            }
        }
        return null;
    }

    /**
     * Busca un doctor por su documento de identidad
     * @param documentType Tipo de documento (CC, CE, PASAPORTE)
     * @param documentNumber Número del documento
     * @return El doctor encontrado o null si no existe
     */
    public Doctor buscarDoctorPorDocumento(DocumentType documentType, String documentNumber) {
        if (documentType == null || documentNumber == null ||
                documentNumber.trim().isEmpty() || vitalApp.getDoctors() == null) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getDocumentType() == documentType &&
                    doctor.getDocumentNumber().equals(documentNumber)) {
                return doctor;
            }
        }
        return null;
    }

    /**
     * Busca un doctor por su número de registro profesional
     * @param professionalNumber Número profesional a buscar
     * @return El doctor encontrado o null si no existe
     */
    public Doctor buscarDoctorPorNumeroProfesional(String professionalNumber) {
        if (professionalNumber == null || professionalNumber.trim().isEmpty() ||
                vitalApp.getDoctors() == null) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getProfessionalNumber().equalsIgnoreCase(professionalNumber)) {
                return doctor;
            }
        }
        return null;
    }

    /**
     * Obtiene una copia de la lista de todos los doctores registrados
     * @return Lista de doctores (nunca null)
     */
    public List<Doctor> obtenerTodosDoctores() {
        if (vitalApp.getDoctors() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(vitalApp.getDoctors());
    }

    /**
     * Actualiza los datos de un doctor existente
     * @param email Email del doctor a actualizar (identificador)
     * @param password Nueva contraseña (ignorado si null o vacío)
     * @param firstname Nuevo nombre (ignorado si null o vacío)
     * @param lastname Nuevo apellido (ignorado si null o vacío)
     * @param professionalNumber Nuevo número profesional (debe ser único)
     * @param speciality Nueva especialidad (debe ser válida)
     * @return true si la actualización fue exitosa, false si hubo error
     */
    public boolean actualizarDoctor(String email, String password, String firstname,
                                    String lastname, String professionalNumber,
                                    String speciality) {
        Doctor doctor = buscarDoctorPorEmail(email);

        if (doctor == null) {
            return false;
        }

        // Actualización de campos opcionales
        if (password != null && !password.trim().isEmpty()) {
            doctor.setPassword(password);
        }

        if (firstname != null && !firstname.trim().isEmpty()) {
            doctor.setFirstname(firstname);
        }

        if (lastname != null && !lastname.trim().isEmpty()) {
            doctor.setLastname(lastname);
        }

        // Validación y actualización de número profesional
        if (professionalNumber != null && !professionalNumber.trim().isEmpty()) {
            Doctor existente = buscarDoctorPorNumeroProfesional(professionalNumber);
            if (existente != null && !existente.getEmail().equalsIgnoreCase(email)) {
                return false; // Número profesional ya está en uso
            }
            doctor.setProfessionalNumber(professionalNumber);
        }

        // Validación y actualización de especialidad
        if (speciality != null && !speciality.trim().isEmpty()) {
            try {
                Speciality.valueOf(speciality.toUpperCase());
                doctor.setSpeciality(speciality);
            } catch (IllegalArgumentException e) {
                return false; // Especialidad no válida
            }
        }

        guardarCambios();
        return true;
    }

    /**
     * Elimina un doctor del sistema
     * @param email Email del doctor a eliminar
     * @return true si la eliminación fue exitosa, false si el doctor no existe
     */
    public boolean eliminarDoctor(String email) {
        Doctor doctor = buscarDoctorPorEmail(email);

        if (doctor == null || vitalApp.getDoctors() == null) {
            return false;
        }

        boolean eliminado = vitalApp.getDoctors().remove(doctor);
        if (eliminado) {
            guardarCambios();
            return true;
        }
        return false;
    }

    /**
     * Guarda los cambios en el sistema de persistencia
     */
    private void guardarCambios() {
        Persistencia.guardarRecursoVitalAppXML(vitalApp);
    }
}