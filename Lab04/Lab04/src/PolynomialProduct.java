import java.lang.*;

public class PolynomialProduct
{
    public int binaryToInt(String binaryString)
    {
        int decimal = 0;

        for(int i = 0; i < binaryString.length(); i++)
            decimal += Math.pow(2, i) * (binaryString.charAt(binaryString.length() - 1 - i) - 48);

        return decimal;
    }

    public String intToBinary(int decimal, int bitsLength)
    {
        String binaryString = "";

        for(int shift = bitsLength - 1; shift >= 0; shift--)
		    binaryString += (decimal >> shift) & 0x01;  
            
        return binaryString;
    }

    public String multiplyFunctions(String fx, String gx)
    {
        int product = 0;
        int []individualProducts = new int[fx.length()];

        for(int i = 0; i < individualProducts.length; i++)
            individualProducts[i] = 0;

        for(int i = 0; i < fx.length(); i++)
        {
            for(int j = 0; j < gx.length(); j++)
            {
                individualProducts[i] += Math.pow(2, gx.length() - 1 - j) * (fx.charAt(fx.length() - 1 - i) - 48) * (gx.charAt(gx.length() - 1 - j) - 48); 
            }

            individualProducts[i] = individualProducts[i] << fx.length() - 1 - i;
        }

        product = individualProducts[0];

        for(int i = 0; i < individualProducts.length - 1; i++)
            product = product ^ individualProducts[i + 1];

        return intToBinary(product, (fx.length() - 1) * 2 + 1);
    }

    public int getPolynomialDegree(String fx)
    {
        int i;

        for(i = 0; i < fx.length(); i++)
            if(fx.charAt(i) == '1')
                break;

        return fx.length() - 1 - i;
    }

    public int getModularPolynomialProduct(int n, String fx, String gx, String mx)
    {
        //Performing the polynomial product
        String binaryProduct = multiplyFunctions(fx, gx);
        int decimalProduct = binaryToInt(binaryProduct);
        
        //Getting the product's degree
        int productDegree = getPolynomialDegree(binaryProduct);
    
        if(productDegree >= n - 1)
        {
            //Getting the most and less significant bits
            int mostSignificantBits = decimalProduct >> n - 1;
            int lessSignificantBits = decimalProduct & ((1 << n - 1) - 1);

            //Getting the modular value
            int module = binaryToInt(mx) & ((1 << n) - 1);

            //Performing left shifts
            for(int i = 0; i < n - 1; i++)
            {
                //If most significant bit is 1
                if((mostSignificantBits & (1 << n - 1)) == (1 << n - 1))
                {
                    mostSignificantBits = (mostSignificantBits << 1) & ((1 << n) - 1);
                    mostSignificantBits = mostSignificantBits ^ module;
                }
                //If most significant bit is 0
                else
                    mostSignificantBits = (mostSignificantBits << 1) & ((1 << n) - 1);
            }

            //Performing last XOR between most and less significant bits
            return mostSignificantBits ^ lessSignificantBits;
        }
        else
            //Returnning the less signifcant bits
            return decimalProduct & ((1 << n - 1) - 1);
    }

    public static void main(String[] args)
    {
        PolynomialProduct p = new PolynomialProduct();

        System.out.println(p.getModularPolynomialProduct(8, "11111111", "11111111", "100011011"));
    }
}
