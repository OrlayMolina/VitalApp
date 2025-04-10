package co.edu.uniquindio.ing.soft.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;

public class ArchivoUtil {

    static String fechaSistema = "";
    /**
     * Este método recibe una cadena con el contenido que se quiere guardar en el archivo
     * @param ruta es la ruta o path donde está ubicado el archivo
     */
    public static void guardarArchivo(String ruta,String contenido, Boolean flagAnexarContenido) throws IOException {

        FileWriter fw = new FileWriter(ruta,flagAnexarContenido);
        BufferedWriter bfw = new BufferedWriter(fw);
        bfw.write(contenido);
        bfw.close();
        fw.close();
    }


    /**
     * ESte metodo retorna el contendio del archivo ubicado en una ruta, con la lista de cadenas.
     */
    public static ArrayList<String> leerArchivo(String ruta) throws IOException {

        ArrayList<String>  contenido = new ArrayList<String>();
        FileReader fr=new FileReader(ruta);
        BufferedReader bfr=new BufferedReader(fr);
        String linea="";
        while((linea = bfr.readLine())!=null)
        {
            contenido.add(linea);
        }
        bfr.close();
        fr.close();
        return contenido;
    }

    private static void cargarFechaSistema() {

        String diaN = "";
        String mesN = "";

        Calendar cal1 = Calendar.getInstance();


        int dia = cal1.get(Calendar.DATE);
        int mes = cal1.get(Calendar.MONTH) + 1;
        int anio = cal1.get(Calendar.YEAR);


        if (dia < 10) {
            diaN += "0" + dia;
        } else {
            diaN += "" + dia;
        }
        if (mes < 10) {
            mesN += "0" + mes;
        } else {
            mesN += "" + mes;
        }

        fechaSistema = anio + "-" + mesN + "-" + diaN;

    }

    //------------------------------------SERIALIZACIÓN  y XML
    /**
     * Escribe en el fichero que se le pasa el objeto que se le envia
     *
     * @param rutaArchivo
     *            path del fichero que se quiere escribir
     * @throws IOException
     */

    public static Object cargarRecursoSerializado(String rutaArchivo)throws Exception
    {
        Object aux = null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(rutaArchivo)))) {

            aux = ois.readObject();

        }
        return aux;
    }


    public static void salvarRecursoSerializado(String rutaArchivo, Object object)	throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(rutaArchivo)))) {
            oos.writeObject(object);
        }
    }

    public static Object cargarRecursoSerializadoXML(String rutaArchivo) throws IOException {

        XMLDecoder decodificadorXML;
        Object objetoXML;

        decodificadorXML = new XMLDecoder(Files.newInputStream(Paths.get(rutaArchivo)));
        objetoXML = decodificadorXML.readObject();
        decodificadorXML.close();
        return objetoXML;

    }

    public static void salvarRecursoSerializadoXML(String rutaArchivo, Object objeto) throws IOException {

        XMLEncoder codificadorXML;

        codificadorXML = new XMLEncoder(Files.newOutputStream(Paths.get(rutaArchivo)));
        codificadorXML.writeObject(objeto);
        codificadorXML.close();

    }
}
