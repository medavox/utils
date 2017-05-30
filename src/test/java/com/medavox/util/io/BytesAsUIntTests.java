package com.medavox.util.io;

import com.medavox.util.io.BytesAsUInt;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static com.medavox.util.io.Bytes.bytesToHex;

/**
 * @author Adam Howard
 * @date 24/05/2017
 */

public class BytesAsUIntTests {
    /**The number of bytes input variables should have. Vastly affects testing time.*/
    private static final int bytesWidth = 2;
    private static int leng = 256;

    @BeforeClass
    public static void setup() {
        for(int i = 1; i < bytesWidth; i++) {
            leng *= 256;
        }
    }

    public byte[] byteArrayOf(int... literals) {
        byte[] out = new byte[literals.length];
        for(int i = 0; i < out.length; i++) {
            out[i] = (byte)literals[i];
        }
        return out;
    }

    public interface SingleArgumentTest {
        void test(int i);
    };

    public interface DoubleArgumentTest {
        void test(int i, int j);
    };

    public void allSingly(SingleArgumentTest test) {
        for(int i = 0; i < leng; i++) {
            test.test(i);
        }
    }

    public void allCombinations(DoubleArgumentTest turst) {
        for(int i = 0; i < leng; i++) {
            for(int j = 0; j < leng; j++) {
                turst.test(i, j);
            }
        }
    }

    public void uniquePairs(DoubleArgumentTest pairs) {
        for(int i = 0; i < leng-2; i++) {
            for(int j = i+1; j < leng-1; j++) {
                pairs.test(i, j);
            }
        }
    }

    //------------------ACTUAL TESTS---------------------------

    @Test
    public void test_increment()  {
        allSingly(new SingleArgumentTest() {
            @Override public void test(int i) {
                byte[] test = BytesAsUInt.genByteArray(i, bytesWidth);
                //byte[] manualZeroes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                //byte[] manualZeroOne = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                byte[] result = BytesAsUInt.increment(test);
                byte[] exp = BytesAsUInt.genByteArray(i+1, bytesWidth);
                Assert.assertTrue("expected:"+bytesToHex(exp)+"; result:"+bytesToHex(result),
                        Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void test_decrement()  {
        allSingly(new SingleArgumentTest() {
            @Override public void test(int i) {
                byte[] test = BytesAsUInt.genByteArray(i,bytesWidth);
                //byte[] manualZeroes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                //byte[] manualZeroOne = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00};
                byte[] result = BytesAsUInt.decrement(test);
                byte[] exp = BytesAsUInt.genByteArray(i-1, bytesWidth);
                Assert.assertTrue("expected:"+bytesToHex(exp)+"; actual:"+bytesToHex(result),
                        Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void testPassByValue() {
        byte[] a = BytesAsUInt.genByteArray(1, bytesWidth);
        byte[] b = BytesAsUInt.genByteArray(1, bytesWidth);
        byte[] aBefore = a;

        System.out.println("a :"+a+"; value:"+bytesToHex(a));
        System.out.println("b :"+a+"; value:"+bytesToHex(b));
        System.out.println("a2:"+aBefore+"; value:"+bytesToHex(aBefore));


        byte[] added = BytesAsUInt.add(a, b);

        System.out.println("a:"+a+"; value:"+bytesToHex(a));
        System.out.println("b:"+a+"; value:"+bytesToHex(b));
        System.out.println("a2:"+aBefore+"; value:"+bytesToHex(aBefore));
        System.out.println("added:"+added+"; value:"+bytesToHex(added));

    }

    @Test
    public void test_add_newImpl() {
        uniquePairs(new DoubleArgumentTest() {
            private int lastI = -1;
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                byte[] exp = BytesAsUInt.genByteArray(i+j, bytesWidth);
                if(i != lastI) {
                    lastI = i;
                    System.out.println("i:" + i + "; j:" + j);
                }
                byte[] result = BytesAsUInt.add_newImpl(a, b);
                Assert.assertTrue("expected: \""+bytesToHex(a).trim()+"\"+\""+bytesToHex(b).trim()+
                        "\"=\""+bytesToHex(exp).trim()+"\""
                                +"; actual:"+bytesToHex(result)+
                        "\ni:"+i+"; j:"+j, Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void test_add() {
        uniquePairs(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                byte[] exp = BytesAsUInt.genByteArray(i+j, bytesWidth);
                if((j % 1024) == 0) {
                    System.out.println("i:" + i + "; j:" + j);
                }
                byte[] result = BytesAsUInt.add(a, b);
                Assert.assertTrue("expected: \""+bytesToHex(a).trim()+"\"+\""+bytesToHex(b).trim()+
                        "\"=\""+bytesToHex(exp).trim()+"\""
                        +"; actual:"+bytesToHex(result)+
                        "\ni:"+i+"; j:"+j, Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void test_subtract() {
        allCombinations(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                byte[] expected = BytesAsUInt.genByteArray(Math.max(0, i-j), bytesWidth);

                byte[] result = BytesAsUInt.subtract(a, b);
                Assert.assertTrue("expected:"+bytesToHex(expected)+"; result:"+bytesToHex(result),
                        Arrays.equals(expected, result));
            }
        });
    }

    @Test
    public void test_multiply() {
        uniquePairs(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                byte[] exp = BytesAsUInt.genByteArray(i*j, bytesWidth);

                byte[] result = BytesAsUInt.multiply(a, b);
                Assert.assertTrue("expected:"+bytesToHex(exp)+"; result:"+bytesToHex(result),
                        Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void test_divide() {
        allCombinations(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                byte[] exp = BytesAsUInt.genByteArray(i / j, bytesWidth);

                byte[] result = BytesAsUInt.divide(a, b);
                Assert.assertTrue("expected:" + bytesToHex(exp) + "; result:" + bytesToHex(result),
                        Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void test_mod() {
        uniquePairs(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                byte[] exp = BytesAsUInt.genByteArray(i % j, bytesWidth);

                byte[] result = BytesAsUInt.mod(a, b);
                Assert.assertTrue("expected:"+bytesToHex(exp)+"; result:"+bytesToHex(result),
                        Arrays.equals(exp, result));
            }
        });
    }

    @Test
    public void test_greaterThan() {
        allCombinations(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                boolean expected = i > j;

                boolean result = BytesAsUInt.greaterThan(a, b);

            }
        });
    }

    @Test
    public void test_lessThan() {
        allCombinations(new DoubleArgumentTest() {
            @Override public void test(int i, int j) {
                byte[] a = BytesAsUInt.genByteArray(i, bytesWidth);
                byte[] b = BytesAsUInt.genByteArray(j, bytesWidth);
                boolean expected = i < j;

                boolean result = BytesAsUInt.lessThan(a, b);

            }
        });
    }
}
