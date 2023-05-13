package com.example.proyejemplo.model;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImprimirDocumento {
    private Context context;

    public ImprimirDocumento(Context context) {
        this.context = context;
    }

    public void guardarArrayListEnExcel(ArrayList<Object> arrayList, String nombreArchivo) {
        try {
            XSSFWorkbook libro = new XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet hoja = libro.createSheet("Datos");

            for (int indice = 0; indice < arrayList.size(); indice++) {
                Object item = arrayList.get(indice);
                org.apache.poi.ss.usermodel.Row fila = hoja.createRow(indice);
                org.apache.poi.ss.usermodel.Cell celda = fila.createCell(0);
                celda.setCellType(CellType.STRING);
                celda.setCellValue(item.toString());
            }

            String rutaAlmacenamientoExterno = Environment.getExternalStorageDirectory().toString();
            File directorio = new File(rutaAlmacenamientoExterno + "/MiApp");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            File archivoSalida = new File(directorio, nombreArchivo + ".xlsx");
            FileOutputStream flujoSalida = new FileOutputStream(archivoSalida);
            libro.write(flujoSalida);
            flujoSalida.close();

            Toast.makeText(context, "Archivo Excel guardado exitosamente.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error al guardar el archivo Excel.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

