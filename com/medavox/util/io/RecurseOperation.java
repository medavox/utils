package com.medavox.util.io;

import java.io.File;

public interface RecurseOperation
{
    /**Whatever goes in here is called on every file recursively,
     *  by FileRecurser*/
    public abstract void operate(File recursee);
    /**For complex filtering of recursed-over files*/
    public abstract boolean acceptFile(File recursee);
}
