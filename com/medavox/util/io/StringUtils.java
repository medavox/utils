package com.medavox.util.io;

import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.FileReader;

public class StringUtils
{
	
	public static String[]findURLsInDoc(String page, Pattern reggie, Set<String> postURLs)
	{
		Set<String> matches = new HashSet<String>();//store in a set to remove exact duplicates
		
		Matcher cherche = reggie.matcher(page);
		while(cherche.find())//if we found something
		{
			String match = page.substring(cherche.start(), cherche.end());
			
			//only add post if its URL hasn't been seen before
			if(postURLs != null && !postURLs.contains(match))
            {
                matches.add(match);
                postURLs.add(match);
            }
            
		}
		//cast the set to an array, and return
		String[] retval = new String[matches.size()];
		return matches.toArray(retval);
	}
	/**Find all substrings matching the supplied regex Pattern, in the supplied string.
	 * Finds one or more URLs in the supplied document, according to supplied regex.
	 * @param page the string to serach in
	 * @param reggie the regular expression to search for*/
	public static String[] findURLsInDoc(String page, Pattern reg)
	{
		return findURLsInDoc(page, reg, null);
	}
	
	/**Reads the supplied (plaintext) file as a string and returns it.
	 * @File f the supplied file. This MUST be a plaintext file. 
	 * @return the contents of the file, as a String.*/
	public static String fileToString(File f)
	{
		try
		{
			FileReader fr = new FileReader(f);
			char[] tmp = new char[(int)f.length()];
			char c;
			int j = 0;
			for(int i = fr.read(); i != -1; i = fr.read())
			{
				c = (char)i;
				tmp[j] = c;
				j++;
			}
			fr.close();
			String ret = new String(tmp);
			return ret;
		}
		catch(Exception e)
		{
			System.err.println("failed to read file: \""+f.getName()+"\"!");
			e.printStackTrace();
			return "";
		}
	}
	
}
