package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.enums.DocumentType;
import co.edu.uniquindio.ing.soft.enums.Gender;
import co.edu.uniquindio.ing.soft.enums.Role;
import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.Patient;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.utils.Persistencia;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PatientController {

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
    public PatientController() {
        this.vitalApp = Persistencia.cargarRecursoVitalAppXML();

        if (this.vitalApp == null) {
            this.vitalApp = new VitalApp();
        }
    }

    /**
     * Constructor con parámetro para facilitar pruebas unitarias
     * @param vitalApp Instancia de VitalApp
     */
    public PatientController(VitalApp vitalApp) {
        this.vitalApp = vitalApp;
    }

    /**
     * Crea un nuevo paciente y lo añade a la lista de pacientes
     *
     * @param email Email del paciente
     * @param password Contraseña del paciente
     * @param firstname Nombre del paciente
     * @param lastname Apellido del paciente
     * @param documentType Tipo de documento del paciente
     * @param documentNumber Número de documento del paciente
     * @param age Edad del paciente
     * @param gender Género del paciente
     * @param address Dirección del paciente
     * @return El paciente creado o null si ocurrió un error
     */
    public Patient crearPaciente(String email, String password, String firstname, String lastname,
                                 DocumentType documentType, String documentNumber, int age, Gender gender, String address) {

        // Validación de campos obligatorios
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty() ||
                firstname == null || firstname.trim().isEmpty() || lastname == null || lastname.trim().isEmpty() ||
                documentType == null || documentNumber == null || documentNumber.trim().isEmpty() ||
                age <= 0 || gender == null || address == null || address.trim().isEmpty()) {
            return null;
        }

        // Verificar si ya existe un paciente con el mismo email
        for (Patient patient : vitalApp.getPatients()) {
            if (patient.getEmail().equals(email)) {
                return null; // El email ya está registrado
            }
        }

        // Verificar si ya existe un paciente con el mismo número de documento
        for (Patient patient : vitalApp.getPatients()) {
            if (patient.getDocumentNumber().equals(documentNumber) && patient.getDocumentType() == documentType) {
                return null; // Ya existe un paciente con el mismo documento
            }
        }

        // Crear y añadir el nuevo paciente
        Patient nuevoPaciente = new Patient();
        nuevoPaciente.setEmail(email);
        nuevoPaciente.setPassword(password);
        nuevoPaciente.setRole(Role.PATIENT);
        nuevoPaciente.setFirstname(firstname);
        nuevoPaciente.setLastname(lastname);
        nuevoPaciente.setDocumentType(documentType);
        nuevoPaciente.setDocumentNumber(documentNumber);
        nuevoPaciente.setAge(age);
        nuevoPaciente.setGender(gender);
        nuevoPaciente.setAddress(address);
        nuevoPaciente.setDiagnostics(new ArrayList<>());

        vitalApp.getPatients().add(nuevoPaciente);

        guardarCambios();

        return nuevoPaciente;
    }

    /**
     * Busca un paciente por su email
     *
     * @param email Email del paciente
     * @return El paciente encontrado o null si no existe
     */
    public Patient buscarPacientePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        for (Patient patient : vitalApp.getPatients()) {
            if (patient.getEmail().equals(email)) {
                return patient;
            }
        }

        return null;
    }

    /**
     * Busca un paciente por su número de documento
     *
     * @param documentType Tipo de documento
     * @param documentNumber Número de documento
     * @return El paciente encontrado o null si no existe
     */
    public Patient buscarPacientePorDocumento(DocumentType documentType, String documentNumber) {
        if (documentType == null || documentNumber == null || documentNumber.trim().isEmpty()) {
            return null;
        }

        for (Patient patient : vitalApp.getPatients()) {
            if (patient.getDocumentType() == documentType && patient.getDocumentNumber().equals(documentNumber)) {
                return patient;
            }
        }

        return null;
    }

    /**
     * Busca un paciente por su número de documento
     *
     * @param documentNumber Número de documento
     * @return El paciente encontrado o null si no existe
     */
    public Patient buscarPacientePorDocumentoNumero(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return null;
        }

        for (Patient patient : vitalApp.getPatients()) {
            if (patient.getDocumentNumber().equals(documentNumber)) {
                return patient;
            }
        }

        return null;
    }

    /**
     * Obtiene todos los pacientes
     *
     * @return Lista de todos los pacientes
     */
    public List<Patient> obtenerTodosPacientes() {
        return new ArrayList<>(vitalApp.getPatients());
    }

    /**
     * Actualiza un paciente existente
     *
     * @param email Email del paciente a actualizar (no se puede cambiar)
     * @param password Nueva contraseña (null o vacío para no cambiar)
     * @param firstname Nuevo nombre (null o vacío para no cambiar)
     * @param lastname Nuevo apellido (null o vacío para no cambiar)
     * @param age Nueva edad (0 o negativo para no cambiar)
     * @param address Nueva dirección (null o vacío para no cambiar)
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarPaciente(String email, String password, String firstname,
                                      String lastname, int age, String address) {
        Patient patient = buscarPacientePorEmail(email);

        if (patient == null) {
            return false;
        }

        if (password != null && !password.trim().isEmpty()) {
            patient.setPassword(password);
        }

        if (firstname != null && !firstname.trim().isEmpty()) {
            patient.setFirstname(firstname);
        }

        if (lastname != null && !lastname.trim().isEmpty()) {
            patient.setLastname(lastname);
        }

        if (age > 0) {
            patient.setAge(age);
        }

        if (address != null && !address.trim().isEmpty()) {
            patient.setAddress(address);
        }

        guardarCambios();

        return true;
    }

    /**
     * Elimina un paciente
     *
     * @param email Email del paciente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarPaciente(String email) {
        Patient patient = buscarPacientePorEmail(email);

        if (patient == null) {
            return false;
        }

        vitalApp.getPatients().remove(patient);

        guardarCambios();

        return true;
    }

    /**
     * Agrega un diagnóstico a un paciente
     *
     * @param documentNumber Número de documento del paciente
     * @param diagnostic Diagnóstico a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean agregarDiagnosticoAPaciente(String documentNumber, Diagnostic diagnostic) {
        if (documentNumber == null || documentNumber.trim().isEmpty() || diagnostic == null) {
            return false;
        }

        Patient patient = buscarPacientePorDocumentoNumero(documentNumber);

        if (patient == null) {
            return false;
        }

        // Verificar si ya tiene este diagnóstico
        for (Diagnostic existingDiagnostic : patient.getDiagnostics()) {
            if (existingDiagnostic.getCode().equals(diagnostic.getCode())) {
                return true; // Ya tiene este diagnóstico, consideramos éxito
            }
        }

        patient.getDiagnostics().add(diagnostic);

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