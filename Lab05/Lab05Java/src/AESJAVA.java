import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program to Encrypt/Decrypt String Using AES 128 bit Encryption Algorithm
 */
public class AESJAVA {
    
    
    
    
    private static final String CIFRADO_MODO_PADDING    = "AES/CBC/NoPadding";
    private static final String CIFRADO_MODO_PADDING2   = "AES/CFB/NoPadding";
    private static final String CIFRADO_MODO_PADDING3   = "AES/CFB/PKCS5Padding";
    private static final String CIFRADO_MODO_PADDING4   = "AES/CBC/PKCS5Padding";
    private static final String ALGORITMO_DE_CIFRADO    = "AES";
    
    /**
     * 
     * @param s String que tiene elementos en hexadecimal
     * @return Un arreglo de bytes
     */
    public static byte[] cadBase16arregloBytes(String s) {
        int len = s.length();
        byte[] bytearr = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytearr[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return bytearr;
    }
    /**
     * 
     * @param a Arreglo de bytes de entrada
     * @return Un String que tendra en base hex la salida del arreglo de bytes
     */
    public static String arregloBytescadBase16(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a){
            sb.append(String.format("%02x", b));
        }            
        return sb.toString();
    }
    /**
     * 
     * @param plainText Texto en plano
     * @param tamKey Tamano de llave
     * @param modoOP Modo de operacion 0, CBC 1 CFB
     * @return Un texto cifrado de tipo String
     */
    public static String encrypt(String plainText, int tamKey, int modoOP, int salArch) {
        String encryptedText = "";
        try {
            Cipher cipher;
            if(modoOP==0)
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING);
            else if(modoOP==1)
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING2);
            else if(modoOP == 2)
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING3);
            else
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING4);
            byte[] key;
            if(salArch == 0){
                 key = new byte[tamKey];                                            
                 cipher.init(Cipher.ENCRYPT_MODE,  new SecretKeySpec(key, "AES"), new IvParameterSpec(new byte[16]));            
            }else{
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                
                keyGenerator.init(tamKey*8);
                SecretKey keyG = keyGenerator.generateKey();
                System.out.println("key: \n" + Arrays.toString(keyG.getEncoded()));
                
                Base64.Encoder encoder64 = Base64.getEncoder();
                String key64 = encoder64.encodeToString(keyG.getEncoded());
                mandarArchivo(key64, "key_aes", 4);
                cipher.init(Cipher.ENCRYPT_MODE,  keyG, new IvParameterSpec(new byte[16]));            
            }
            
            
            byte[] cipherText;
            if(salArch == 0){
                cipherText = cipher.doFinal(cadBase16arregloBytes(plainText));                        
                encryptedText = arregloBytescadBase16(cipherText);
            }else{
                cipherText = cipher.doFinal(plainText.getBytes("UTF8"));                        
                Base64.Encoder encoder = Base64.getEncoder();
                encryptedText = encoder.encodeToString(cipherText);
            }                        

        } catch (Exception E) {
             System.err.println("Encrypt Exception : "+E.getMessage());
        }
        return encryptedText;
    }
    /**
     * 
     * @param encryptedText String cifrado
     * @param tamKey Indica si será de 128, 192 o 256 la llave para AES
     * @param modoOP Indica el modo de operacion para la instancia del descifrado 0 CBC sin padding, 1 CFB sin padding, 2 CFB con Padding y 3 CBC con padding
     * @param salArch Indica si el resultado irá a un archivo o es un vector test, 0 de ser un vector de prueba y 1 si será parte del archivo
     * @return Un string descifrado en texto plano
     */
    public static String decrypt(String encryptedText, int tamKey, int modoOP, int salArch) {
        String decryptedText = "";
        try {
            Cipher cipher;
            if(modoOP==0)
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING);
            else if(modoOP ==1)
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING2);
            else if(modoOP == 2)
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING3);
            else
                cipher = Cipher.getInstance(CIFRADO_MODO_PADDING4);
            
            IvParameterSpec ivparameterspec = new IvParameterSpec(new byte[16]);
            if(salArch == 0){
                byte[] key = new byte[tamKey];
                SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITMO_DE_CIFRADO);                
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            }else{
                byte[] key = ObtenerKeyArchivo("key_aes");
                SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITMO_DE_CIFRADO);                
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            }
            
        
            byte[] txtplain;
            if(salArch==0){
                txtplain = cipher.doFinal(cadBase16arregloBytes(encryptedText));
                decryptedText = arregloBytescadBase16(txtplain);
            }else{
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
                txtplain = cipher.doFinal(cipherText);                
                decryptedText = new String(txtplain, "UTF-8");                            
            }                        

        } catch (Exception E) {
            System.err.println("decrypt Exception : "+E.getMessage());
        }
        return decryptedText;
    }
    /**
     * Se hace la prueba con vectores test para AES
     */
    public static void pruebaDeVectores(){


        String plainT = "f34481ec3cc627bacd5dc3fb08f273e6";

        String encyptStr = encrypt(plainT,16,0,0); //16,24,32
        String decryptStr  = decrypt(encyptStr,16,0,0);        
        System.out.println("Texto en plano   : "+plainT);
        System.out.println("Texto encriptado : "+encyptStr);
        System.out.println("Texto descifrado : "+decryptStr);

        // Prueba con 192 con los vectores oficales de prueba
        plainT = "1b077a6af4b7f98229de786d7516b639";
        encyptStr = encrypt(plainT,24,0,0); //16,24,32
        decryptStr  = decrypt(encyptStr,24,0,0);        
        System.out.println("Texto en plano  : "+plainT);
        System.out.println("Texto encriptado: "+encyptStr);
        System.out.println("Texto descifrado: "+decryptStr);
        // Prueba con 256 con los vectores oficales de prueba
        plainT = "014730f80ac625fe84f026c60bfd547d";
        encyptStr = encrypt(plainT,32,1,0); //16,24,32 
        decryptStr  = decrypt(encyptStr,32,1,0);        
        System.out.println("Texto en plano  : "+plainT);
        System.out.println("Texto encriptado: "+encyptStr);
        System.out.println("Texto descifrado: "+decryptStr);

    }
    /**
     * 
     * @param narch Indica el nombre del archivo
     * @param modo Indica el modo en que se hará el cifrado 1 CFB, 2 CBC para los archivos
     * @throws IOException
     */
    public static void encryptArchivo(String narch, int modo) throws IOException {
          
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
        String textEncrypted;
        if(modo==1)
            textEncrypted = encrypt(textoF, 24,2,1);
        else
            textEncrypted = encrypt(textoF, 24,3,1);
        mandarArchivo(textEncrypted,narch,2);

    }
    /**
     * 
     * @param narch Indica el nombre del archivo
     * @param modo Indica el modo en que el archivo será descifrado ya sea 1 CFB o 2 CBC
     * @throws IOException
     */
    public static void decryptArchivo(String narch, int modo) throws IOException {
          
        String texto = "";
        String textoF = "";
        try {                
            File archivo = new File("./" + narch + ".aes");
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
        String textDecrypted;
        if(modo==1)
            textDecrypted = decrypt(textoF, 24, 2,1);
        else
            textDecrypted = decrypt(textoF, 24, 3,1);
        mandarArchivo(textDecrypted,narch,3);
    }
    public static void mandarArchivo(String key, String name, int modo) throws IOException {
        FileWriter fichero = null;
        try {
            if(modo == 1){
                fichero = new FileWriter(name + ".txt");
                fichero.write(key);
                fichero.write("\n");
                fichero.close();
            }else if (modo == 2){
                fichero = new FileWriter(name + ".aes");
                fichero.write(key);
                fichero.write("\n");
                fichero.close();
            }else if(modo == 3){
                fichero = new FileWriter(name + ".dec");
                fichero.write(key);
                fichero.write("\n");
                fichero.close();
            }else if(modo == 4){
                fichero = new FileWriter(name + ".key");
                fichero.write(key);
                fichero.write("\n");
                fichero.close();
            }                                              
        } catch (Exception e) {
            System.out.println(e);;
        }
    }
    public static byte[] ObtenerKeyArchivo(String narch){

        String texto = "";
        String textoF = "";
        try {                
            File archivo = new File("./" + narch + ".key");
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
        Base64.Decoder decoder = Base64.getDecoder();        
        System.out.println(Arrays.toString(decoder.decode(textoF)));
        return decoder.decode(textoF);
    }
    public static void main(String[] args) throws IOException {                                
        pruebaDeVectores();       
        //encryptArchivo("prueba1",1); // CFB
        //decryptArchivo("prueba1",1);

        encryptArchivo("prueba1",2); // CBC
        decryptArchivo("prueba1",2);

    }   
}

