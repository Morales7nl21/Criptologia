import java.io.*;

public class MultiTable {
    
    public String cadenadeBits(int n, int tam)
    {
        String binario = Integer.toBinaryString(n);
        while(binario.length()!= tam)
        {
            binario = "0"+binario;
        }
        return binario;
    }

    public static void main(String[] args) {

        PolynomialProduct p = new PolynomialProduct();        
        MultiTable m = new MultiTable();
        int[][] inverseMultiplicative = new int[64][64]; 
        /*-------THE VALUES MODIFY DEPEND OF THE MULTIPLICATION TABLE THAT WE WANT--------------*/
        
        int n = 6; //Valor de n
        String mx = "10000011"; //Valor m(x)

        /*--------------------------------------------------------------------------------------*/
        
        int base = 2;//Base
        int cuadrado = (int)Math.pow( (double)base , (double)n );
        
        //We create a string array
        String[] valores = new String[cuadrado-1];

        //We save all the numbers that are element of 2^n in the array valores
        for(int i = 1; i<cuadrado; i++)
        {
            String c = m.cadenadeBits(i, n);
            valores[i-1] = c;
        }
        
        //Now we do the multplication table
        try
        {
            //We create the file that we save all the information
            BufferedWriter ficheroSalida = new BufferedWriter(new FileWriter(new File("multiplicationTable.txt")));
            ficheroSalida.write("Multiplication table of 2^"+n+"\n\n*\t");//Write the title of the multiplication table

            for(int i = 0; i<cuadrado-1; i++)
            {
                String hexa = Integer.toHexString(i+1);
                ficheroSalida.write(""+hexa+"\t");//We write the line from 1 to 2^n in he filetext
            }

            ficheroSalida.write("\n");

            for(int i = 0; i<cuadrado-1; i++)
            {
                String hexa = Integer.toHexString(i+1);
                ficheroSalida.write(""+hexa+"\t");
                for(int j = 0; j<cuadrado-1; j++)
                {
                    //We call the function that we create before with the values of the array valores
                    int res = p.getModularPolynomialProduct(n, valores[i], valores[j], mx);
                    hexa = Integer.toHexString(res);
                    ficheroSalida.write(""+hexa+"\t");                    
                    inverseMultiplicative[i][j] = res;
                }
                ficheroSalida.write("\n");
            }

            ficheroSalida.close();

        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        String[] invmult = new String[32];
        int posInvmult = 0;
        for (int i = 0; i < inverseMultiplicative[0].length-1; i++) {
            for (int j = i; j < inverseMultiplicative[0].length-1; j++) {
                //System.out.print(inverseMultiplicative[i][j]);
                if(inverseMultiplicative[i][j] == 1){  
                    String hexa = Integer.toHexString(i+1);
                    String hexa1 = Integer.toHexString(j+1);           
                    invmult[posInvmult] = "the inverse multiplicative of "+ hexa + " is -> " + hexa1;
                    posInvmult++;                                        
                }
            }            
        }

        try {
            BufferedWriter ficheroSalida = new BufferedWriter(new FileWriter(new File("inverseM.txt")));
            ficheroSalida.write("Inverse multiplicative\n\n");
            for (int i = 0; i < 31; i++) {
                ficheroSalida.write(String.valueOf(invmult[i]));
                ficheroSalida.write("\n");
            }                
            ficheroSalida.close();
        } catch (Exception e) {
            System.out.println(e);
        }                   
    }



}
