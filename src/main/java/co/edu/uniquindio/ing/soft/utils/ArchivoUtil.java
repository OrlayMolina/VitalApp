package co.edu.uniquindio.ing.soft.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ArchivoUtil {

    //------------------------------------ XML
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
