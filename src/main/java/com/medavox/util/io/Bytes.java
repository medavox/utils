package com.medavox.util.io;
/**Low-level bit-twiddling ops. bit numbers are 0 to 7.*/
import java.math.BigInteger;
public class Bytes
{
    private static void validate(int bitNum)
    {
        if(bitNum < 0
        || bitNum > 7)
        {
            throw new IllegalArgumentException("bitNum was <0 or >7");
        }
    }
    public static boolean testBit(int bitNum, byte testee)
    {
        validate(bitNum);
        byte tester = (byte)0x01;
        tester <<= bitNum;
        return (testee & tester) != 0;
    }
    
    public static byte setBit(int bitNum, byte settee)
    {
        validate(bitNum);
        byte setter = (byte)0x01;
        setter <<= bitNum;
        return (byte)(settee | setter);
    }
    
    public static byte unsetBit(int bitNum, byte settee)
    {
        validate(bitNum);        
        byte setter = (byte)0x01;
        setter <<= bitNum;
        setter = (byte)~setter;
        return (byte)(setter & settee);
    }
    
    /**Based on code from https://stackoverflow.com/questions/9655181*/
    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        if(bytes == null ) {
            return "<null>";
        }
        if(bytes.length == 0) {
            return "<empty>";
        }
        char[] hexChars = new char[bytes.length * 3];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }
    
    /**Calculates the number of full 8-bit bytes that can be stored in a sequence of symbols.
     * @param sequenceLength the length of the sequence 
     * @param uniqueSymbols the number of possible symbols each symbol could be
     * @return the number of bytes which could be stored in the sequence*/
    public static int computeBytesStorable(int sequenceLength, int uniqueSymbols) {
        //System.out.println("chars:"+CHARS);
        BigInteger combosOfChars = BigInteger.valueOf(uniqueSymbols).pow(sequenceLength);
        BigInteger n256 = BigInteger.valueOf(256);
        //combinationsOf140Bytes = combinationsOf140Bytes.pow(140);
        int bytesThatFit = -1;
        for(int i = 1; i < Integer.MAX_VALUE; i++) {
            BigInteger combosOfNBytes = n256.pow(i);
            if(combosOfNBytes.compareTo(combosOfChars) > 0) {
                bytesThatFit = i-1;
                //the previous power was the last & highest one to be less, therefore fit
                System.out.println("number of full bytes that can be expressed by "+sequenceLength
                        +" consecutive symbols that have "+uniqueSymbols+" possible values: "+bytesThatFit);
                System.out.println("256^"+bytesThatFit+": "+combosOfNBytes);
                System.out.println(uniqueSymbols+"^"+sequenceLength+": "+combosOfChars);

                System.out.println("diff:    "+combosOfChars.subtract(combosOfNBytes));
                break;
            }
        }
        return bytesThatFit;
    }
}
