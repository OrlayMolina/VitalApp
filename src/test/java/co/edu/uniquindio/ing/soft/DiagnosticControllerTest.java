package co.edu.uniquindio.ing.soft;

import co.edu.uniquindio.ing.soft.controller.DiagnosticController;
import co.edu.uniquindio.ing.soft.model.Diagnostic;
import co.edu.uniquindio.ing.soft.model.VitalApp;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DiagnosticControllerTest {

    private DiagnosticController diagnosticController;
    private VitalApp vitalApp;
    private Diagnostic diagnostic1;
    private Diagnostic diagnostic2;

    @Before
    public void setUp() {
        // Configuración inicial sin persistencia
        vitalApp = new VitalApp();
        vitalApp.setDiagnostics(new ArrayList<>());

        diagnostic1 = new Diagnostic();
        diagnostic1.setCode("D001");
        diagnostic1.setDiagnostic("Hipertensión arterial");

        diagnostic2 = new Diagnostic();
        diagnostic2.setCode("D002");
        diagnostic2.setDiagnostic("Diabetes mellitus tipo 2");

        // Agregar diagnósticos iniciales directamente
        vitalApp.getDiagnostics().add(diagnostic1);
        vitalApp.getDiagnostics().add(diagnostic2);

        diagnosticController = new DiagnosticController(vitalApp);
    }

    @Test
    public void testCrearDiagnosticoExitoso() {
        Diagnostic nuevo = diagnosticController.crearDiagnostico("D003", "Asma");

        assertNotNull("Debería crear diagnóstico válido", nuevo);
        assertEquals("Debería haber 3 diagnósticos", 3, vitalApp.getDiagnostics().size());
    }

    @Test
    public void testCrearDiagnosticoConCodigoExistente() {
        Diagnostic resultado = diagnosticController.crearDiagnostico("D001", "Nueva descripción");

        assertNull("No debería permitir duplicados", resultado);
        assertEquals("Deberían mantenerse 2 diagnósticos", 2, vitalApp.getDiagnostics().size());
    }

    @Test
    public void testBuscarDiagnosticoPorId() {
        Diagnostic encontrado = diagnosticController.buscarDiagnosticoPorId("D001");

        assertNotNull("Debería encontrar diagnóstico existente", encontrado);
        assertEquals("Debería ser el diagnóstico correcto", diagnostic1, encontrado);
    }

    @Test
    public void testObtenerTodosDiagnosticos() {
        List<Diagnostic> diagnosticos = diagnosticController.obtenerTodosDiagnosticos();

        assertEquals("Deberían obtenerse todos los diagnósticos", 2, diagnosticos.size());
        assertTrue("Debería contener D001", diagnosticos.contains(diagnostic1));
    }

    @Test
    public void testActualizarDiagnosticoExitoso() {
        boolean resultado = diagnosticController.actualizarDiagnostico("D001", "Hipertensión severa");

        assertTrue("Debería actualizar correctamente", resultado);
        assertEquals("Descripción debería actualizarse",
                "Hipertensión severa",
                diagnosticController.buscarDiagnosticoPorId("D001").getDiagnostic());
    }

    @Test
    public void testEliminarDiagnosticoExitoso() {
        boolean resultado = diagnosticController.eliminarDiagnostico("D001");

        assertTrue("Debería eliminar correctamente", resultado);
        assertEquals("Debería quedar 1 diagnóstico", 1, vitalApp.getDiagnostics().size());
    }

    @Test
    public void testIntegridadDatos() {
        // Operaciones combinadas
        diagnosticController.crearDiagnostico("D003", "Asma");
        diagnosticController.actualizarDiagnostico("D002", "Diabetes actualizada");
        diagnosticController.eliminarDiagnostico("D001");

        List<Diagnostic> diagnosticos = diagnosticController.obtenerTodosDiagnosticos();

        assertEquals("Deberían quedar 2 diagnósticos", 2, diagnosticos.size());
        assertNotNull("D002 debería existir", diagnosticController.buscarDiagnosticoPorId("D002"));
        assertEquals("D002 debería estar actualizado",
                "Diabetes actualizada",
                diagnosticController.buscarDiagnosticoPorId("D002").getDiagnostic());
    }
}