package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.model.Doctor;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.utils.Persistencia;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DoctorController {

    /**
     * -- GETTER --
     * Obtiene la instancia de VitalApp
     *
     * @return La instancia de VitalApp
     */
    private VitalApp vitalApp;

    /**
     * Constructor que inicializa el controlador cargando los datos de la persistencia
     */
    public DoctorController() {
        this.vitalApp = Persistencia.cargarRecursoVitalAppXML();

        if (this.vitalApp == null) {
            this.vitalApp = new VitalApp();
        }
    }

    /**
     * Constructor con parámetro para facilitar pruebas unitarias
     * @param vitalApp Instancia de VitalApp
     */
    public DoctorController(VitalApp vitalApp) {
        this.vitalApp = vitalApp;
    }

    /**
     * Crea un nuevo doctor y lo añade a la lista de doctores
     *
     * @param email Email del doctor
     * @param password Contraseña del doctor
     * @param firstname Nombre del doctor
     * @param lastname Apellido del doctor
     * @param documentType Tipo de documento del doctor
     * @param documentNumber Número de documento del doctor
     * @param professionalNumber Número profesional del doctor
     * @return El doctor creado o null si ocurrió un error
     */
    public Doctor crearDoctor(String email, String password, String firstname, String lastname,
                              DocumentType documentType, String documentNumber, String professionalNumber) {

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty() ||
                firstname == null || firstname.trim().isEmpty() || lastname == null || lastname.trim().isEmpty() ||
                documentType == null || documentNumber == null || documentNumber.trim().isEmpty() ||
                professionalNumber == null || professionalNumber.trim().isEmpty()) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getEmail().equals(email)) {
                return null;
            }
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getDocumentNumber().equals(documentNumber) && doctor.getDocumentType() == documentType) {
                return null;
            }
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getProfessionalNumber().equals(professionalNumber)) {
                return null;
            }
        }

        Doctor nuevoDoctor = new Doctor(email, password, Role.DOCTOR, firstname, lastname,
                documentType, documentNumber, professionalNumber);

        vitalApp.getDoctors().add(nuevoDoctor);

        guardarCambios();

        return nuevoDoctor;
    }

    /**
     * Busca un doctor por su email
     *
     * @param email Email del doctor
     * @return El doctor encontrado o null si no existe
     */
    public Doctor buscarDoctorPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getEmail().equals(email)) {
                return doctor;
            }
        }

        return null;
    }

    /**
     * Busca un doctor por su número de documento
     *
     * @param documentType Tipo de documento
     * @param documentNumber Número de documento
     * @return El doctor encontrado o null si no existe
     */
    public Doctor buscarDoctorPorDocumento(DocumentType documentType, String documentNumber) {
        if (documentType == null || documentNumber == null || documentNumber.trim().isEmpty()) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getDocumentType() == documentType && doctor.getDocumentNumber().equals(documentNumber)) {
                return doctor;
            }
        }

        return null;
    }

    /**
     * Busca un doctor por su número profesional
     *
     * @param professionalNumber Número profesional del doctor
     * @return El doctor encontrado o null si no existe
     */
    public Doctor buscarDoctorPorNumeroProfesional(String professionalNumber) {
        if (professionalNumber == null || professionalNumber.trim().isEmpty()) {
            return null;
        }

        for (Doctor doctor : vitalApp.getDoctors()) {
            if (doctor.getProfessionalNumber().equals(professionalNumber)) {
                return doctor;
            }
        }

        return null;
    }

    /**
     * Obtiene todos los doctores
     *
     * @return Lista de todos los doctores
     */
    public List<Doctor> obtenerTodosDoctores() {
        return new ArrayList<>(vitalApp.getDoctors());
    }

    /**
     * Actualiza un doctor existente
     *
     * @param email Email del doctor a actualizar (no se puede cambiar)
     * @param password Nueva contraseña (null o vacío para no cambiar)
     * @param firstname Nuevo nombre (null o vacío para no cambiar)
     * @param lastname Nuevo apellido (null o vacío para no cambiar)
     * @param professionalNumber Nuevo número profesional (null o vacío para no cambiar)
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarDoctor(String email, String password, String firstname, String lastname, String professionalNumber) {
        Doctor doctor = buscarDoctorPorEmail(email);

        if (doctor == null) {
            return false;
        }

        if (password != null && !password.trim().isEmpty()) {
            doctor.setPassword(password);
        }

        if (firstname != null && !firstname.trim().isEmpty()) {
            doctor.setFirstname(firstname);
        }

        if (lastname != null && !lastname.trim().isEmpty()) {
            doctor.setLastname(lastname);
        }

        if (professionalNumber != null && !professionalNumber.trim().isEmpty()) {
            // Verificar que el nuevo número profesional no esté en uso por otro doctor
            Doctor doctorExistente = buscarDoctorPorNumeroProfesional(professionalNumber);
            if (doctorExistente != null && !doctorExistente.getEmail().equals(email)) {
                return false; // El número profesional ya está en uso por otro doctor
            }
            doctor.setProfessionalNumber(professionalNumber);
        }

        guardarCambios();

        return true;
    }

    /**
     * Elimina un doctor
     *
     * @param email Email del doctor a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarDoctor(String email) {
        Doctor doctor = buscarDoctorPorEmail(email);

        if (doctor == null) {
            return false;
        }

        vitalApp.getDoctors().remove(doctor);

        guardarCambios();

        return true;
    }

    /**
     * Guarda los cambios en la persistencia
     */
    private void guardarCambios() {
        Persistencia.guardarRecursoVitalAppXML(vitalApp);
    }
}