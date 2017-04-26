package com.medavox.util.download;

/**base class for code implementing download functionality.
 * If it downloads something, it should extend this.*/
public abstract class DownloadAction
{
	public String src;
	public DownloadAction(String src)
	{
		this.src = src;
	}
	public String getSource()
	{
		return src;
	}
	public abstract String download() throws Exception;
	
	public void errorCallback(HttpCodeAction hca)
	{
		
	}
	public int getRetryLimit()
	{
		return 3;
	}
}
