package com.medavox.util.io;
/**Low-level bit-twiddling ops. bit numbers are 0 to 7.*/
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
    
    /**based on code from https://stackoverflow.com/questions/9655181*/
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
}
