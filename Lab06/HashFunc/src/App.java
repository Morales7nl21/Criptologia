import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

public class App {

    /**
     * 
     * @param narch Indica el nombre del archivo
     * @param modo  Indica el modo en que el archivo será descifrado ya sea 1 CFB o
     *              2 CBC
     * @throws IOException
     */
    public static void leerArchivo(String narch, int n) throws IOException {

        String texto = "";
        String textoF = "";
        try {
            File archivo = new File("./" + narch + ".txt");
            FileReader entrada = new FileReader(archivo);
            BufferedReader br = new BufferedReader(entrada);
            while ((texto = br.readLine()) != null) {                
                textoF += texto;
            }
            entrada.close();
        } catch (IOException e) {
            System.out.println("No se ha encontrado el archivo:" + e.getMessage());
        }
        //System.out.println(textoF.length()*8);
      
        textoF = textoF.replace("\n", "");
        String binary = new BigInteger(textoF.getBytes()).toString(2);
        StringBuilder bb = new StringBuilder(binary);
        int r = 0;
        String t = "0";        
        while (binary.length() % n != 0) {
            binary = t+binary;
        }        
        StringBuilder bloque = new StringBuilder();
        ArrayList<String> array_bloques = new ArrayList<String>();
        for (char bits : binary.toCharArray()) {            
            if(r==n){
                array_bloques.add(bloque.toString());
                System.out.println();
                r=0;
                bloque = new StringBuilder();
            }
            bloque.append(bits);
            //System.out.print(bits);
            r++;
        }
        array_bloques.add(bloque.toString());
        for (String string : array_bloques) {
            System.out.println(string);
        }
        System.out.println("Blocks: " + String.valueOf(array_bloques.size()));
        StringBuilder sb = new StringBuilder();
        String dave= "";
        char init;
        char [] tt = binary.toCharArray();
        for (int i = 0; i < n; i++) {
                init = tt[0];
                for (int j = 1; j < array_bloques.size(); j++) {
                    init = (char) (init ^ tt[j*8]);
                }
                sb.append(init);
                 
        }

        
        System.out.println(binary+ " ---- " + sb.toString());

    }

    public static void main(String[] args) throws Exception {
        final int n = 8;
        leerArchivo("test", n);

    }
}
