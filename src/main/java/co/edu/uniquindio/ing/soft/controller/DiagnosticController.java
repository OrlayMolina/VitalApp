package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.utils.Persistencia;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticController {

    private VitalApp vitalApp;

    /**
     * Constructor que inicializa el controlador cargando los datos de la persistencia
     */
    public DiagnosticController() {
        this.vitalApp = Persistencia.cargarRecursoVitalAppXML();

        if (this.vitalApp == null) {
            this.vitalApp = new VitalApp();
        }
    }

    /**
     * Crea un nuevo diagnóstico y lo añade a la lista de diagnósticos
     *
     * @param code Código del diagnóstico (ingresado manualmente)
     * @param diagnostic Texto del diagnóstico
     * @return El diagnóstico creado o null si ocurrió un error
     */
    public Diagnostic crearDiagnostico(String code, String diagnostic) {
        if (code == null || code.trim().isEmpty() || diagnostic == null || diagnostic.trim().isEmpty()) {
            return null;
        }

        if (buscarDiagnosticoPorId(code) != null) {
            return null; // El código ya existe
        }

        Diagnostic nuevoDiagnostico = new Diagnostic();
        nuevoDiagnostico.setCode(code);
        nuevoDiagnostico.setDiagnostic(diagnostic);

        vitalApp.getDiagnostics().add(nuevoDiagnostico);

        guardarCambios();

        return nuevoDiagnostico;
    }

    /**
     * Busca un diagnóstico por su código
     *
     * @param code Código del diagnóstico
     * @return El diagnóstico encontrado o null si no existe
     */
    public Diagnostic buscarDiagnosticoPorId(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        for (Diagnostic diagnostico : vitalApp.getDiagnostics()) {
            if (diagnostico.getCode().equals(code)) {
                return diagnostico;
            }
        }

        return null;
    }

    /**
     * Obtiene todos los diagnósticos
     *
     * @return Lista de todos los diagnósticos
     */
    public List<Diagnostic> obtenerTodosDiagnosticos() {
        return new ArrayList<>(vitalApp.getDiagnostics());
    }

    /**
     * Actualiza un diagnóstico existente
     *
     * @param code Código del diagnóstico a actualizar
     * @param diagnostic Nuevo texto de diagnóstico
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarDiagnostico(String code, String diagnostic) {
        Diagnostic diagnostico = buscarDiagnosticoPorId(code);

        if (diagnostico == null) {
            return false;
        }

        if (diagnostic != null && !diagnostic.trim().isEmpty()) {
            diagnostico.setDiagnostic(diagnostic);
        }

        guardarCambios();

        return true;
    }

    /**
     * Elimina un diagnóstico
     *
     * @param code Código del diagnóstico a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarDiagnostico(String code) {
        Diagnostic diagnostico = buscarDiagnosticoPorId(code);

        if (diagnostico == null) {
            return false;
        }

        vitalApp.getDiagnostics().remove(diagnostico);

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