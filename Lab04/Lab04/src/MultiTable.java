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

        /*-------THE VALUES MODIFY DEPEND OF THE MULTIPLICATION TABLE THAT WE WANT--------------*/
        
        int n = 6; //Valor de n
        String mx = "1000011"; //Valor m(x)

        /*--------------------------------------------------------------------------------------*/
        
        int base = 2;//Base
        int cuadrado = (int)Math.pow( (double)base , (double)n );
        
        //We create a string array
        String valores[] = new String[cuadrado-1];

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
                ficheroSalida.write(""+(i+1)+"\t");//We write the line from 1 to 2^n in he filetext
            }

            ficheroSalida.write("\n");

            for(int i = 0; i<cuadrado-1; i++)
            {
                ficheroSalida.write(""+(i+1)+"\t");
                for(int j = 0; j<cuadrado-1; j++)
                {
                    //We call the function that we create before with the values of the array valores
                    int res = p.getModularPolynomialProduct(n, valores[i], valores[j], mx);
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
