package co.edu.uniquindio.ing.soft.controller;

import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import co.edu.uniquindio.ing.soft.utils.Persistencia;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DiagnosticController {

    private final VitalApp vitalApp;

    public DiagnosticController(VitalApp vitalApp) {
        this.vitalApp = vitalApp;

        // Inicialización segura
        if (this.vitalApp.getDiagnostics() == null) {
            this.vitalApp.setDiagnostics(new ArrayList<>());
        }
    }

    public Diagnostic crearDiagnostico(String code, String diagnostic) {
        if (code == null || code.trim().isEmpty() ||
                diagnostic == null || diagnostic.trim().isEmpty()) {
            return null;
        }

        // Verificación más estricta de código existente
        if (vitalApp.getDiagnostics().stream()
                .anyMatch(d -> d.getCode().equalsIgnoreCase(code.trim()))) {
            return null;
        }

        Diagnostic nuevoDiagnostico = new Diagnostic();
        nuevoDiagnostico.setCode(code.trim());
        nuevoDiagnostico.setDiagnostic(diagnostic.trim());

        vitalApp.getDiagnostics().add(nuevoDiagnostico);
        guardarCambios();

        return nuevoDiagnostico;
    }

    public Diagnostic buscarDiagnosticoPorId(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return vitalApp.getDiagnostics().stream()
                .filter(d -> d.getCode().equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(null);
    }

    public List<Diagnostic> obtenerTodosDiagnosticos() {
        return new ArrayList<>(vitalApp.getDiagnostics());
    }

    public boolean actualizarDiagnostico(String code, String diagnostic) {
        if (code == null || code.trim().isEmpty() ||
                diagnostic == null || diagnostic.trim().isEmpty()) {
            return false;
        }

        Diagnostic existente = buscarDiagnosticoPorId(code);
        if (existente == null) {
            return false;
        }

        existente.setDiagnostic(diagnostic.trim());
        guardarCambios();
        return true;
    }

    public boolean eliminarDiagnostico(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        Diagnostic aEliminar = buscarDiagnosticoPorId(code);
        if (aEliminar == null) {
            return false;
        }

        boolean eliminado = vitalApp.getDiagnostics().remove(aEliminar);
        if (eliminado) {
            guardarCambios();
            return true;
        }
        return false;
    }

    private void guardarCambios() {
        Persistencia.guardarRecursoVitalAppXML(vitalApp);
    }
}