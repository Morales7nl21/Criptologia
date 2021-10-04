import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;

public class App {

    static class Permutation {
        Permutation(){}
        public static boolean GetBit(byte b, int bitNumber) {
            return (b & (1 << bitNumber)) != 0;
        }

        public static byte generaPermutacion(Byte b, int[] s) {

            Byte rs = b;
            StringBuilder string_permutacion = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                if (GetBit(rs, 7 - s[i]))
                    string_permutacion.append('1');
                else
                    string_permutacion.append('0');
            }            
            int decimal = Integer.parseInt(string_permutacion.toString(), 2);
            byte result = (byte) decimal;
            return Byte.valueOf(result);
        }

    }
    static class logger{
        public void err(Exception s){
            System.err.println(s);
        }
        public void log(String s){
            System.out.println(s);
        }
        public void logN(int i){
            System.out.print(i);
        }
    }
    static class TrippleDes {

        private static final String UNICODE_FORMAT = "UTF8";
        public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
        private KeySpec ks;
        private SecretKeyFactory skf;
        public Cipher cipher;
        byte[] arrayBytes;
        private String myEncryptionKey;
        private String myEncryptionScheme;
        SecretKey key;
        private static File Arch = null;

        public TrippleDes() {
            try {
                SecureRandom random = new SecureRandom();
                random.setSeed(random.generateSeed(112));
                KeyGenerator keygenerator = KeyGenerator.getInstance(DESEDE_ENCRYPTION_SCHEME); // DES EDE EDE variante a usar
                /**
                 * Keysize must be equal to 112 or 168.
                        A keysize of 112 will generate a Triple DES key with 2 intermediate keys, and a keysize of 168 will generate a Triple DES key with 3 intermediate keys.
                    Due to the "Meet-In-The-Middle" problem, even though 112 or 168 bits of key material are used, the effective keysize is 80 or 112 bits respectively.
                 * 
                 */
                keygenerator.init(112, random); // PAra cambiar de 3 llaves ocupamos 
                myEncryptionKey = keygenerator.toString();
                myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME; // DES EDE Equema a usar
                arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT); // UTF8
                ks = new DESedeKeySpec(arrayBytes); // DES EDE se especifica aqui las especificaciones de la llave
                skf = SecretKeyFactory.getInstance(myEncryptionScheme); // Se obtiene sched de llaves skf
                cipher = Cipher.getInstance(myEncryptionScheme);
                key = skf.generateSecret(ks); // Se genera la llave secreta a partir de la key random junto con las
                                            // especificaciones a usar
                String key_a_guardar = new String(Base64.encodeBase64(key.toString().getBytes()));
                // System.out.println(key_a_guardar); // Imprimimos la random key secure
                mandarKeyTxt(key_a_guardar, "keyBase64", 1);    
            } catch (Exception e) {
                new logger().err(e);
            }
            
        }

        public void mandarKeyTxt(String key, String name, int modo) throws IOException {
            FileWriter fichero = null;
            try {
                if(modo == 1){
                    fichero = new FileWriter(name + ".txt");
                    fichero.write(key);
                    fichero.write("\n");
                    fichero.close();
                }else if (modo == 2){
                    fichero = new FileWriter(name + ".des");
                    fichero.write(key);
                    fichero.write("\n");
                    fichero.close();
                }else if(modo == 3){
                    fichero = new FileWriter("Decrypted_"+name + ".txt");
                    fichero.write(key);
                    fichero.write("\n");
                    fichero.close();
                }                                              
            } catch (Exception e) {
                new logger().err(e);
            }
        }

        public static BufferedReader getBuffered(String link) {

            FileReader lector = null;
            BufferedReader br = null;
            
            try {
                Arch = new File(link);
                if (!Arch.exists()) {
                    new logger().log("No existe el archivo");
                } else {
                    lector = new FileReader(link);
                    br = new BufferedReader(lector);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return br;
        }

        public String encrypt(String unencryptedString, String nombre_archivo_key ) {
          
            String encryptedString = null;     
            try {                
            String ruta = "./" + nombre_archivo_key + ".txt";
            BufferedReader br = getBuffered(ruta);
            final SecretKey key = getSecretKey(br.readLine());      
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
            final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decipher.init(Cipher.ENCRYPT_MODE, key, iv);    
            final byte[] plainText = decipher.doFinal(unencryptedString.getBytes(UNICODE_FORMAT));
            encryptedString = new String(Base64.encodeBase64(plainText), "UTF-8");            
            } catch (Exception e) {
                e.printStackTrace();
            }

            return  encryptedString;

        }

        public void encryptArchivo(String narch, String narchkey) throws IOException {
          
            String texto = "";
            String textoF = "";
            try {
                
                File archivo = new File("./" + narch + ".txt");
                FileReader entrada = new FileReader(archivo);
                BufferedReader br = new BufferedReader(entrada);
                while((texto=br.readLine())!=null){                    
                    textoF+=texto;                    
                }
                entrada.close();
                } 
            catch (IOException e) {
                System.out.println("No se ha encontrado el archivo:" + e.getMessage());
            }
            String textEncrypted = encrypt(textoF, narchkey);
             
            mandarKeyTxt(textEncrypted,narch,2);

        }
        public void decryptArchivo(String narch, String narchkey) throws IOException {
          
            String texto = "";
            String textoF = "";
            try {                
                File archivo = new File("./" + narch + ".des");
                FileReader entrada = new FileReader(archivo);
                BufferedReader br = new BufferedReader(entrada);               
                while((texto=br.readLine())!=null){                    
                    textoF+=texto;
                }
                entrada.close();
                } 
            catch (IOException e) {
                System.out.println("No se ha encontrado el archivo:" + e.getMessage());
            }
            String textDecrypted = decrypt(textoF, narchkey);
            mandarKeyTxt(textDecrypted,narch,3);
        }


        public SecretKey getSecretKey(String skstring) throws InvalidKeyException, InvalidKeySpecException {
            String nkstring = skstring;
            nkstring = nkstring.replace("\n", "");
            nkstring = nkstring.replace(" ", "");
            byte[] arrbk = Base64.decodeBase64(nkstring);
            return skf.generateSecret(new DESedeKeySpec(arrbk));
        }

        public String decrypt(String encryptedString, String nombre_archivo_key) throws UnsupportedEncodingException {
           
            byte[] plainText = null;
            try {
                String ruta = "./" + nombre_archivo_key + ".txt";
                BufferedReader br = getBuffered(ruta);
                final SecretKey key = getSecretKey(br.readLine());      
                final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
                final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
                decipher.init(Cipher.DECRYPT_MODE, key, iv);        
                final byte[] encData = Base64.decodeBase64(encryptedString);
                plainText = decipher.doFinal(encData);    
            } catch (Exception e) {
                new logger().err(e);
            }               
            return new String(plainText, "UTF-8");
        }
    }

    public static void main(String[] args) throws Exception {
        Byte b = 0b01110000;
        String s;
        int[] perm = { 0, 1, 2, 3, 4, 5, 6, 7 };
        int[] perm2 = { 7, 6, 5, 4, 3, 2, 1, 0 };
        int[] perm3 = { 7, 2, 6, 1, 5, 4, 0, 3 };
        logger logg = new App.logger();
        s = String.format("%8s", Integer.toBinaryString(b.byteValue() & 0xFF)).replace(' ', '0');
        logg.log(("Antes de la permutacion: " + s));
        logg.log("Permutaciones a usar: ");
        for (int i = 0; i < perm.length; i++) {
            logg.logN(perm[i]);
        }
        logg.log("");
        for (int i = 0; i < perm2.length; i++) {
            logg.logN(perm2[i]);
        }
        logg.log("");
        for (int i = 0; i < perm3.length; i++) {
            logg.logN(perm3[i]);
        }
        logg.log("");
        try {
            Byte nb = App.Permutation.generaPermutacion(b, perm);
            Byte nb2 = App.Permutation.generaPermutacion(b, perm2);
            Byte nb3 = App.Permutation.generaPermutacion(b, perm3);
            s = String.format("%8s", Integer.toBinaryString(nb.byteValue() & 0xFF)).replace(' ', '0');
            logg.log("Despues de la permutacion 1:  " + s);
            s = String.format("%8s", Integer.toBinaryString(nb2.byteValue() & 0xFF)).replace(' ', '0');
            logg.log("Despues de la permutacion 2:  " + s);
            s = String.format("%8s", Integer.toBinaryString(nb3.byteValue() & 0xFF)).replace(' ', '0');
            logg.log("Despues de la permutacion 3:  " + s);
        } catch (Exception e) {
            logg.err(e);
        }
        // TrippleDes
        TrippleDes td = new TrippleDes();
        String target = "imparator";
        String kb64 = "keyBase64";
        String encrypted = td.encrypt(target, kb64);
        String decrypted = td.decrypt(encrypted, kb64);
        logg.log("String To Encrypt:" + target);
        logg.log("Encrypted String:" + encrypted);
        logg.log("Decrypted String:" + decrypted);
        td.encryptArchivo("txtAEncriptarTest", kb64);
        td.decryptArchivo("txtAEncriptarTest", kb64);        
    }
}
