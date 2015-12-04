package com.medavox.util.io;
import java.io.*;

public abstract class FileRecurser
{
    public static final int NO_FILTER = 0;
    public static final int STRING_FILTER = 1;
    public static final int METHOD_FILTER = 2;
    
    public static void recurse(File root, RecurseOperation fo)
    {
        recurse(root, NO_FILTER, "", fo);
    }
    
    public static void recurse(File root, String filter, RecurseOperation fo)
    {
        recurse(root, STRING_FILTER, filter, fo);
    }
    
    public static void recurse(File root, RecurseOperation fo, boolean placeHolder)
    {
        recurse(root, METHOD_FILTER, "", fo);
    }
    
    private static void recurse(File root, int mode, String filter, RecurseOperation fo)
    {
        if(!root.isDirectory()
        || !root.exists())
        {
            throw new IllegalArgumentException("supplied argument must be a directory which exists");
        }
        
        for(File f : root.listFiles())
        {
            if(f.isDirectory())
            {
                recurse(f, filter, fo);
            }
            else
            {
                switch(mode)
                {
                    case NO_FILTER:
                    fo.operate(f);
                    break;
                    
                    case STRING_FILTER:
                        if(f.getName().contains(filter))
                        {
                            fo.operate(f);  
                        }
                    break;
                    
                    case METHOD_FILTER:
                        if(fo.acceptFile(f))
                        {
                            fo.operate(f);
                        }
                    break;
                }
            }
            
        }
    }
    

}
