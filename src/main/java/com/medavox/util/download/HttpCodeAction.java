package com.medavox.util.download;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Makes decisions about what to do with a failed HTTP request,
 * based on the Exception and/or HTTP status code received.
 * 
 * Options are: RETRY until request succeeds,
 * LIMITED_RETRY a number of times, then give up if HTTP request didn't succeed,
 * MOVE_ON (give up) from downloading,
 * or crash from something completely unexpected (PANIC).*/
public enum HttpCodeAction
{//http error code handling actions, in order of severity:
    SUCCESS, RETRY, LIMITED_RETRY, MOVE_ON, PANIC;

    public static HttpCodeAction fromCode(int code)
    {
        System.err.print("\nReceived HTTP error code "+code+"; ");
        if (code < 100) //codes < 100 are nonsensical
            throw new IllegalArgumentException("HTTP code must be >= 100, was "+code);
        else if (code <= 299)//codes <400 shouldn't be thrown (100=info, 200=success, 300=redirect)
            return SUCCESS;
        else if (code <= 399)//redirect
            return SUCCESS;
        else if (code == 404)//may be temporary, but usually not
            return LIMITED_RETRY;
        else if (code == 408  || code == 429)//request timed out || too many requests
            return RETRY;
        else if (code <= 499)//other 400 codes are usually unresolvable by retrying
            return MOVE_ON;
        else if (code <= 599)//server errors; should fix itself with a retry or 2
            return RETRY;
        else//otherwise, we have no idea. PANIC!
            System.err.println("ERROR: unknown HTTP error code: "+code+"; exiting...");
            //throw new IllegalArgumentException("HTTP code must be between 100 and 599, inclusive, was "+code);
            return PANIC;
    }
    
    public static HttpCodeAction fromException(Throwable e)
    {
		int errorCode;
		switch(e.getClass().getName()) {
            //yes, this is a failure to use OO techniques
		    //but it's shorter, simpler and more readable
			case "java.net.SocketTimeoutException":
			case "java.net.UnknownHostException":
			//HostNotFoundException is usually caused by ISP/router failure
            //(why would the domain suddenly not exist?)
				return RETRY;

			case "java.net.SocketException":
				return LIMITED_RETRY;
			
			case "java.io.FileNotFoundException":
				return MOVE_ON;
			
			case "java.io.IOException":
				errorCode = getHttpCodeFromIOException((IOException)e);
				if(errorCode < 100)
				{
					return null;
				}
				return fromCode(errorCode);
				
			default:
				return null;
		}
	}
	/**Get the actual HTTP error code int from the stupidly verbose IOException message
	@param ioe The exception we can't be bothered dealing with
	@return The HTTP status code we parsed, -2 on NumberFormatException, or -1 on all else.*/
    private static int getHttpCodeFromIOException(IOException ioe)
    {
        String msg = ioe.getMessage();
        Pattern patrick = Pattern.compile("Server returned HTTP response code: ([0-9]{3})");
		Matcher cherche = patrick.matcher(msg);
		if(cherche.find())//if we found something
		{
			String match = cherche.group(1);
            //System.err.println("match found: "+match);
            try
            {
                return Integer.parseInt(match);
            }
            catch(NumberFormatException nfe)
            {
                return -2;
            }
		}
        return -1;//default fail
    }
}
