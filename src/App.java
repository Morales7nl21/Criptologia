import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import java.security.SecureRandom;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
public class App {

    static class Permutation{
        public static boolean GetBit(byte b, int bitNumber){
            return (b & (1 << bitNumber)) != 0;
        }
        public static byte generaPermutacion(Byte b, int[] s){
                                  
            Byte  rs = b;                                               
            StringBuilder  string_permutacion = new StringBuilder();                              
            for(int i = 0; i < s.length; i++){
               if(GetBit(rs,7-s[i]))
                   string_permutacion.append('1');
               else
                    string_permutacion.append('0');
            }            
            //System.out.println(string_permutacion);
            int decimal = Integer.parseInt(string_permutacion.toString(),2);
            byte result = (byte) decimal;
            return  Byte.valueOf(result);
        }
      
        
    }
    static class TrippleDes {

        private static final String UNICODE_FORMAT = "UTF8";
        public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
        private KeySpec ks;
        private SecretKeyFactory skf;
        private Cipher cipher;
        byte[] arrayBytes;
        private String myEncryptionKey;
        private String myEncryptionScheme;
        SecretKey key;
    
        public TrippleDes() throws Exception {
            
            SecureRandom random = new SecureRandom();    
            random.setSeed(random.generateSeed(168));
            KeyGenerator keygenerator = KeyGenerator.getInstance(DESEDE_ENCRYPTION_SCHEME); // DES EDE EDE  variante a usar
            keygenerator.init(168,random);      
            myEncryptionKey = keygenerator.toString();      
            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME; //DES EDE Equema a usar
            arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT); // UTF8                                               
            ks = new DESedeKeySpec(arrayBytes); // DES EDE se especifica aqui las especificaciones de la llave
            skf = SecretKeyFactory.getInstance(myEncryptionScheme);   // Se obtiene sched de llaves skf     
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks); // Se genera la llave secreta a partir de la key random junto con las especificaciones a usar 
            String key_a_guardar = new String(Base64.encodeBase64(key.toString().getBytes()));
            //System.out.println(key_a_guardar); // Imprimimos la random key secure  
            mandarKeyTxt(key_a_guardar,"keyBase64");
        }
        public void mandarKeyTxt(String key, String name)  throws IOException{
            FileWriter fichero = null;
            try {
                fichero = new FileWriter(name+".txt");
                fichero.write(key);                                       
                fichero.write("\n");                             
                fichero.close();
            } catch (Exception ex) {
                System.out.println("Mensaje de la excepción: " + ex.getMessage());
            }    
        }
        public static BufferedReader getBuffered(String link){

            FileReader lector  = null;
            BufferedReader br = null;
            try {
                 File Arch=new File(link);
                if(!Arch.exists()){
                   System.out.println("No existe el archivo");
                }else{
                   lector = new FileReader(link);
                   br = new BufferedReader(lector);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return br;
        }

    
        public String encrypt(String unencryptedString, String nombre_archivo_key) {
            String encryptedString = null;
            try {
                String ruta = "./"+nombre_archivo_key+".txt";
                BufferedReader br = getBuffered(ruta);  
                String lecturaKey=br.readLine();
                lecturaKey = lecturaKey.replace("\n", "");
                lecturaKey = lecturaKey.replace(" ", "");
                //System.out.println(lecturaKey);                
                byte[] arrbk = Base64.decodeBase64(lecturaKey);                                                
                SecretKey ska=skf.generateSecret(new DESedeKeySpec(arrbk));                
                cipher.init(Cipher.ENCRYPT_MODE, ska);
                byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
                byte[] encryptedText = cipher.doFinal(plainText);
                encryptedString = new String(Base64.encodeBase64(encryptedText));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return encryptedString;
        }
    
    
        public String decrypt(String encryptedString,  String nombre_archivo_key) {
            String decryptedText=null;
            try {
                String ruta = "./"+nombre_archivo_key+".txt";
                BufferedReader br = getBuffered(ruta);  
                String lecturaKey=br.readLine();
                lecturaKey = lecturaKey.replace("\n", "");
                lecturaKey = lecturaKey.replace(" ", "");
                //System.out.println(lecturaKey);                
                byte[] arrbk = Base64.decodeBase64(lecturaKey);                                                
                SecretKey ska=skf.generateSecret(new DESedeKeySpec(arrbk));               
                cipher.init(Cipher.DECRYPT_MODE, ska);
                byte[] encryptedText = Base64.decodeBase64(encryptedString);
                byte[] plainText = cipher.doFinal(encryptedText);
                decryptedText= new String(plainText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decryptedText;
        }
    }
    public static void main(String[] args) throws Exception {
        Byte b = 0b01110000; 
        String s;       
        int[] perm = {0, 1, 2, 3, 4, 5, 6, 7};        
        int[] perm2 = {7, 6, 5, 4, 3, 2, 1, 0};        
        int[] perm3 = {7, 3, 2, 4, 5, 1, 6, 0};    
        s = String.format("%8s", Integer.toBinaryString(b.byteValue() & 0xFF)).replace(' ', '0');
        System.out.println("Antes de la permutacion: "+  s);
        System.out.println("Permutaciones a usar: ");
        for(int i=0; i<perm.length; i++){
            System.out.print(perm[i]);
        }
        System.out.println();
        for(int i=0; i<perm2.length; i++){
            System.out.print(perm2[i]);
        }
        System.out.println();
        for(int i=0; i<perm3.length; i++){
            System.out.print(perm2[i]);
        }
        System.out.println();
        try {
            Byte nb= App.Permutation.generaPermutacion(b, perm);   
            Byte nb2= App.Permutation.generaPermutacion(b, perm2); 
            Byte nb3= App.Permutation.generaPermutacion(b, perm3); 
            s = String.format("%8s", Integer.toBinaryString(nb.byteValue() & 0xFF)).replace(' ', '0');                    
            System.out.println("Despues de la permutacion 1:  "+ s); 
            s = String.format("%8s", Integer.toBinaryString(nb2.byteValue() & 0xFF)).replace(' ', '0');        
            System.out.println("Despues de la permutacion 2:  "+ s); 
            s = String.format("%8s", Integer.toBinaryString(nb3.byteValue() & 0xFF)).replace(' ', '0');        
            System.out.println("Despues de la permutacion 3:  "+ s);
        } catch (Exception e) {
            System.err.println(e);
        }  
    //TrippleDes
    TrippleDes td= new TrippleDes();
    String target="imparator";
    String encrypted=td.encrypt(target, "keyBase64");
    String decrypted=td.decrypt(encrypted, "keyBase64");
    System.out.println("String To Encrypt:"+ target);
    System.out.println("Encrypted String:" + encrypted);
    System.out.println("Decrypted String:" + decrypted);
            
    }
}
