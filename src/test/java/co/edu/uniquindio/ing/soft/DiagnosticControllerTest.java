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

    @Before
    public void setUp() {
        // Creamos algunos diagnósticos existentes para las pruebas
        List<Diagnostic> diagnostics = new ArrayList<>();

        Diagnostic diagnostic1 = new Diagnostic();
        diagnostic1.setCode("D001");
        diagnostic1.setDiagnostic("Hipertensión arterial");
        diagnostics.add(diagnostic1);

        Diagnostic diagnostic2 = new Diagnostic();
        diagnostic2.setCode("D002");
        diagnostic2.setDiagnostic("Diabetes mellitus tipo 2");
        diagnostics.add(diagnostic2);

        // Configuramos el objeto VitalApp con los diagnósticos
        vitalApp = new VitalApp();
        vitalApp.setDiagnostics(diagnostics);

        // Creamos el controlador con la aplicación configurada
        diagnosticController = new DiagnosticController(vitalApp);
    }

    @Test
    public void testCrearDiagnosticoExitoso() {
        Diagnostic diagnostic = diagnosticController.crearDiagnostico("D003", "Asma bronquial");

        assertNotNull("El diagnóstico debería crearse correctamente", diagnostic);
        assertEquals("El código debería ser D003", "D003", diagnostic.getCode());
        assertEquals("El diagnóstico debería ser Asma bronquial", "Asma bronquial", diagnostic.getDiagnostic());

        // Verificamos que se agregó a la lista
        Diagnostic foundDiagnostic = diagnosticController.buscarDiagnosticoPorId("D003");
        assertNotNull("El diagnóstico debería encontrarse en la lista", foundDiagnostic);
    }

    @Test
    public void testCrearDiagnosticoConCodigoExistente() {
        Diagnostic diagnostic = diagnosticController.crearDiagnostico("D001", "Otro diagnóstico");

        assertNull("No debería permitir crear un diagnóstico con código existente", diagnostic);
    }

    @Test
    public void testCrearDiagnosticoConDatosInvalidos() {
        Diagnostic diagnostic = diagnosticController.crearDiagnostico("", "Diagnóstico válido");
        assertNull("No debería crear diagnóstico con código vacío", diagnostic);

        diagnostic = diagnosticController.crearDiagnostico(null, "Diagnóstico válido");
        assertNull("No debería crear diagnóstico con código null", diagnostic);

        diagnostic = diagnosticController.crearDiagnostico("D003", "");
        assertNull("No debería crear diagnóstico con descripción vacía", diagnostic);

        diagnostic = diagnosticController.crearDiagnostico("D003", null);
        assertNull("No debería crear diagnóstico con descripción null", diagnostic);

        diagnostic = diagnosticController.crearDiagnostico("  ", "Diagnóstico válido");
        assertNull("No debería crear diagnóstico con código de espacios", diagnostic);

        diagnostic = diagnosticController.crearDiagnostico("D003", "  ");
        assertNull("No debería crear diagnóstico con descripción de espacios", diagnostic);
    }

    @Test
    public void testBuscarDiagnosticoPorId() {
        Diagnostic diagnostic = diagnosticController.buscarDiagnosticoPorId("D001");
        assertNotNull("Debería encontrar el diagnóstico existente", diagnostic);
        assertEquals("El diagnóstico encontrado debería tener código D001", "D001", diagnostic.getCode());

        diagnostic = diagnosticController.buscarDiagnosticoPorId("NOVALIDO");
        assertNull("No debería encontrar un diagnóstico con código inexistente", diagnostic);

        diagnostic = diagnosticController.buscarDiagnosticoPorId("");
        assertNull("No debería encontrar un diagnóstico con código vacío", diagnostic);

        diagnostic = diagnosticController.buscarDiagnosticoPorId(null);
        assertNull("No debería encontrar un diagnóstico con código null", diagnostic);
    }

    @Test
    public void testObtenerTodosDiagnosticos() {
        List<Diagnostic> diagnostics = diagnosticController.obtenerTodosDiagnosticos();

        assertNotNull("La lista de diagnósticos no debería ser null", diagnostics);
        assertEquals("Deberían haber 2 diagnósticos", 2, diagnostics.size());

        boolean encontradoD001 = false;
        boolean encontradoD002 = false;

        for (Diagnostic diagnostic : diagnostics) {
            if (diagnostic.getCode().equals("D001")) {
                encontradoD001 = true;
            } else if (diagnostic.getCode().equals("D002")) {
                encontradoD002 = true;
            }
        }

        assertTrue("Debería encontrar el diagnóstico D001", encontradoD001);
        assertTrue("Debería encontrar el diagnóstico D002", encontradoD002);
    }

    @Test
    public void testActualizarDiagnostico() {
        boolean resultado = diagnosticController.actualizarDiagnostico("D001", "Hipertensión arterial severa");

        assertTrue("La actualización debería ser exitosa", resultado);

        Diagnostic diagnostic = diagnosticController.buscarDiagnosticoPorId("D001");
        assertEquals("El diagnóstico debería estar actualizado", "Hipertensión arterial severa", diagnostic.getDiagnostic());
    }

    @Test
    public void testActualizarDiagnosticoInexistente() {
        boolean resultado = diagnosticController.actualizarDiagnostico("NOVALIDO", "Descripción");

        assertFalse("No debería actualizar un diagnóstico inexistente", resultado);
    }

    @Test
    public void testActualizarDiagnosticoConDatosInvalidos() {
        boolean resultado = diagnosticController.actualizarDiagnostico("D001", null);

        assertFalse("No debería actualizar con descripción null", resultado);

        resultado = diagnosticController.actualizarDiagnostico("D001", "");

        assertFalse("No debería actualizar con descripción vacía", resultado);

        resultado = diagnosticController.actualizarDiagnostico("D001", "  ");

        assertFalse("No debería actualizar con descripción de espacios", resultado);
    }

    @Test
    public void testEliminarDiagnostico() {
        boolean resultado = diagnosticController.eliminarDiagnostico("D001");

        assertTrue("La eliminación debería ser exitosa", resultado);

        Diagnostic diagnostic = diagnosticController.buscarDiagnosticoPorId("D001");
        assertNull("El diagnóstico debería haberse eliminado", diagnostic);

        List<Diagnostic> diagnostics = diagnosticController.obtenerTodosDiagnosticos();
        assertEquals("Debería quedar 1 diagnóstico", 1, diagnostics.size());
    }

    @Test
    public void testEliminarDiagnosticoInexistente() {
        boolean resultado = diagnosticController.eliminarDiagnostico("NOVALIDO");

        assertFalse("No debería eliminar un diagnóstico inexistente", resultado);
    }

    @Test
    public void testEliminarDiagnosticoConDatosInvalidos() {
        boolean resultado = diagnosticController.eliminarDiagnostico("");

        assertFalse("No debería eliminar con código vacío", resultado);

        resultado = diagnosticController.eliminarDiagnostico(null);

        assertFalse("No debería eliminar con código null", resultado);
    }
}
