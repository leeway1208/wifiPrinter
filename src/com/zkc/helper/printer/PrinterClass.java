package com.zkc.helper.printer;

import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

public interface PrinterClass {
	
	/**
	 * open the device
	 * 
	 **/
	public boolean open(Context context);
	
	/**
	 * close the device
	 * 
	 **/
	public boolean close(Context context);
	
	/**
	 * scan printer
	 * 
	 **/
	public void scan();
	
	/**
	 * get device
	 * @return
	 */
	public List<Device> getDeviceList();
	
	/**
	 * stop scan
	 */
	public void stopScan();
	
	/**
	 * connect a printer
	 * 
	 **/
	public boolean connect(String device);
	
	/**
	 * disconnect a printer
	 * 
	 **/
	public boolean disconnect();
	/**
	 * get the connect state
	 * 
	 **/
	public int getState();
	
	/**
	 * Set state
	 * @param state
	 */
	public void setState(int state);
	
	public boolean IsOpen();
	/**
	 * send data
	 * 
	 **/
	public boolean write(byte[] bt);
	
	/**
	 * Print text
	 * @param textStr
	 * @return
	 */
	public boolean printText(String textStr);
	
	/**
	 * Print Image
	 * @param bitmap
	 * @return
	 */
	public boolean printImage(Bitmap bitmap);
	
	/**
	 * Print Unicode
	 * @param textStr
	 * @return
	 */
	public boolean printUnicode(String textStr);
	

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote
													// device
	public static final int LOSE_CONNECT = 4;
	public static final int FAILED_CONNECT = 5;
	public static final int SUCCESS_CONNECT = 6; // now connected to a remote


	public static final int STATE_SCANING = 7;// É¨Ãè×´Ì¬
	public static final int STATE_SCAN_STOP = 8;

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	
}
