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
        int n = 3; //Valor de n
        int base = 2;
        int cuadrado = (int)Math.pow( (double)base , (double)n );

        //We create a string array
        String valores[] = new String[cuadrado-1];

        //We input all the numbers that are element of 2^6
        for(int i = 1; i<cuadrado; i++)
        {
            String c = m.cadenadeBits(i, n);
            valores[i-1] = c;
        }
        
        //Now we do the multplication table
        try
        {
            BufferedWriter ficheroSalida = new BufferedWriter(new FileWriter(new File("multiplicationTable.txt")));
            ficheroSalida.write("Multiplication table of 2^"+n+"\n\n*\t");

            for(int i = 0; i<cuadrado-1; i++)
            {
                ficheroSalida.write(""+(i+1)+"\t");
            }

            ficheroSalida.write("\n");

            for(int i = 0; i<cuadrado-1; i++)
            {
                ficheroSalida.write(""+(i+1)+"\t");
                for(int j = 0; j<cuadrado-1; j++)
                {
                    int res = p.getModularPolynomialProduct(3, valores[i], valores[j], "1011");
                    ficheroSalida.write(""+res+"\t");
                }
                ficheroSalida.write("\n");
            }

            ficheroSalida.close();

        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

       
    }



}
