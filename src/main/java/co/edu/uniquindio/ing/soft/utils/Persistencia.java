package co.edu.uniquindio.ing.soft.utils;

import co.edu.uniquindio.ing.soft.model.VitalApp;

public class Persistencia {
    public static final String RUTA_ARCHIVO_VITALAPP_XML = "src/main/resources/persistencia/model.xml";

    public static VitalApp cargarRecursoVitalAppXML() {

        VitalApp subasta = null;

        try {
            subasta = (VitalApp) ArchivoUtil.cargarRecursoSerializadoXML(RUTA_ARCHIVO_VITALAPP_XML);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subasta;

    }


    public static void guardarRecursoVitalAppXML(VitalApp subasta) {

        try {
            ArchivoUtil.salvarRecursoSerializadoXML(RUTA_ARCHIVO_VITALAPP_XML, subasta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
