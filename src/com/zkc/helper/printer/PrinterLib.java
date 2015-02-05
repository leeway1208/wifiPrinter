package com.zkc.helper.printer;

public class PrinterLib {
	static
	{
		System.loadLibrary("printer");
	}
	public native static byte[] getBitmapData(int[] bitdata,int w,int h);
	
	public native static int[] imgToGray(int[] buf, int w, int h);
}
