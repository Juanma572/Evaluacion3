package ordenamientodatos;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Documento {

    private String ape1;
    private String ape2;
    private String nom;
    private String doc;

    public Documento(String apellido1, String apellido2, String nombre, String documento) {
        this.ape1 = apellido1;
        this.ape2 = apellido2;
        this.nom = nombre;
        this.doc = documento;
    }

    public String getDocumento() {
        return doc;
    }

    public String getNombreCompleto() {
        return ape1 + " " + ape2 + " " + nom;
    }

    public static List<Documento> documentos = new ArrayList();
    public static String[] encabezados;

    public static void obtenerDatosDesdeArchivo(String nombreArchivo) {
        documentos.clear();
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine();
                encabezados = linea.split(";");
                linea = br.readLine();
                while (linea != null) {
                    String[] textos = linea.split(";");
                    if (textos.length >= 4) {
                        Documento d = new Documento(textos[0], textos[1], textos[2], textos[3]);
                        documentos.add(d);
                    }
                    linea = br.readLine();
                }
            } catch (Exception ex) {
            }
        }
    }

    public static void mostrarDatos(JTable tbl) {
        String[][] datos = null;
        if (documentos.size() > 0) {
            datos = new String[documentos.size()][encabezados.length];
            for (int i = 0; i < documentos.size(); i++) {
                datos[i][0] = documentos.get(i).ape1;
                datos[i][1] = documentos.get(i).ape2;
                datos[i][2] = documentos.get(i).nom;
                datos[i][3] = documentos.get(i).doc;
            }
        }
        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
    }

    private static void intercambiar(int origen, int destino) {
        Documento temporal = documentos.get(origen);
        documentos.set(origen, documentos.get(destino));
        documentos.set(destino, temporal);
    }

    private static boolean esMayor(Documento d1, Documento d2, int criterio) {
        if (criterio == 0) {
            return ((d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0)
                    || (d1.getNombreCompleto().equals(d2.getNombreCompleto())
                    && d1.getDocumento().compareTo(d2.getDocumento()) > 0));
        } else {
            return ((d1.getDocumento().compareTo(d2.getDocumento()) > 0)
                    || (d1.getDocumento().equals(d2.getDocumento())
                    && d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0));
        }
    }

    public static void ordenarBurbujaRecursivo(int n, int criterio) {
        if (n == documentos.size() - 1) {
            return;
        } else {
            for (int i = n + 1; i < documentos.size(); i++) {
                if (esMayor(documentos.get(n), documentos.get(i), criterio)) {
                    intercambiar(n, i);
                }
            }
            ordenarBurbujaRecursivo(n + 1, criterio);
        }
    }

    public static void ordenarBurbuja(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {

            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(i), documentos.get(j), criterio)) {
                    intercambiar(i, j);
                }
            }
        }
    }

    private static int localizarPivote(int inicio, int fin, int criterio) {
        int pivote = inicio;
        Documento dP = documentos.get(pivote);

        for (int i = inicio + 1; i <= fin; i++) {
            if (esMayor(dP, documentos.get(i), criterio)) {
                pivote++;
                if (i != pivote) {
                    intercambiar(i, pivote);
                }
            }
        }
        if (inicio != pivote) {
            intercambiar(inicio, pivote);
        }
        return pivote;
    }

    public static void ordenarRapido(int ini, int fin, int cri) {
        //punto de finalización
        if (ini >= fin) {
            return;
        }
        int pivote = localizarPivote(ini, fin, cri);
        ordenarRapido(ini, pivote - 1, cri);
        ordenarRapido(pivote + 1, fin, cri);
    }
    public static void ordenarInsercion(int cri) {
        for (int i = 1; i < documentos.size(); i++) {
            Documento act = documentos.get(i);
            int j = i;
            while (j > 0 && esMayor(documentos.get(j - 1), act, cri)) {
                documentos.set(j, documentos.get(j - 1));
                j--;
            }
            documentos.set(j, act);
        }
    }
    

}
