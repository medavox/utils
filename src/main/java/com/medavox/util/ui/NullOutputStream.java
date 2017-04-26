package com.medavox.ui;

import java.io.OutputStream;

/**Does nothing; merely exists to provide something to pass to the super() constructor of GraphicalPrintout
 * (new PrintStream(OutputStream)),
     * without using file I/O*/
    public class NullOutputStream extends OutputStream
    {
        @Override
        public void close(){}
        
        @Override
        public void flush(){}
        
        @Override
        public void write(byte[] b){}
        
        @Override
        public void write(byte[] b, int off, int len){}
        
        public void write(int b){}

    }
