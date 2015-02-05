package com.zkc.pinter.activity;



import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.btpdemo76.R;
import com.zkc.helper.printer.Device;
import com.zkc.helper.printer.PrinterClass;
import com.zkc.helper.printer.PrinterClassFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

//zkc.bluetooth.api

public class PrintSettingActivity extends ListActivity {
	private String TAG = "BtSetting";
	private ArrayAdapter<String> mPairedDevicesArrayAdapter = null;
	public static ArrayAdapter<String> mNewDevicesArrayAdapter = null;
	public static List<Device> deviceList=new ArrayList<Device>();
	private Button bt_scan;
	public Handler mhandler;
	private LinearLayout layoutscan;
	private TextView tv_status;
	private Thread tv_update;
	private boolean tvFlag = true;
	Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_printsetting);
		// 允许主线程连接网络
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
				
		_context=this;
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.drawable.device_name);
		
		InitListView();		
		
		layoutscan = (LinearLayout) findViewById(R.id.layoutscan);
		layoutscan.setVisibility(View.GONE);

		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
				R.drawable.device_name);
		deviceList=new ArrayList<Device>();
		bt_scan = (Button) findViewById(R.id.bt_scan);
		bt_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(deviceList!=null)
				{
					deviceList.clear();
				}
				if (!PrintActivity.pl.IsOpen()) {
					PrintActivity.pl.open(_context);
				}
				layoutscan.setVisibility(View.VISIBLE);
				mNewDevicesArrayAdapter.clear();
				PrintActivity.pl.scan();				
				deviceList = PrintActivity.pl.getDeviceList();
				InitListView();
			}
		});

		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_update = new Thread() {
			public void run() {
				while (tvFlag) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tv_status.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (PrintActivity.pl != null) {
								if (PrintActivity.pl.getState() == PrinterClass.STATE_CONNECTED) {
									tv_status.setText(PrintSettingActivity.this
											.getResources().getString(
													R.string.str_connected));
									MainActivity.checkState=true;
									tvFlag=false;
									PrintSettingActivity.this.finish();
									
								} else if (PrintActivity.pl.getState() == PrinterClass.STATE_CONNECTING) {
									tv_status.setText(PrintSettingActivity.this
											.getResources().getString(
													R.string.str_connecting));
								} 
								else if(PrintActivity.pl.getState()==PrinterClass.STATE_SCAN_STOP)
								{
									tv_status.setText(PrintSettingActivity.this
										.getResources().getString(
												R.string.str_scanover));
									layoutscan.setVisibility(View.GONE);
									InitListView();
								}
								else if(PrintActivity.pl.getState()==PrinterClass.STATE_SCANING)
								{
									tv_status.setText(PrintSettingActivity.this
										.getResources().getString(
												R.string.str_scaning));
									InitListView();
								}
								else {
									int ss=PrintActivity.pl.getState();
									tv_status.setText(PrintSettingActivity.this
											.getResources().getString(
													R.string.str_disconnected));
								}
							}
						}
					});
				}
			}
		};
		tv_update.start();
		
	}
	
	private void InitListView()
	{
		SimpleAdapter simpleAdapter=new SimpleAdapter(this,getData("simple-list-item-2"),android.R.layout.simple_list_item_2,new String[]{"title", "description"},new int[]{android.R.id.text1, android.R.id.text2});
		setListAdapter(simpleAdapter);
	}
	
	/**
     * 当List的项被选中时触发
     */
    protected void onListItemClick(ListView listView, View v, int position, long id) {  
    	Map map = (Map)listView.getItemAtPosition(position);
    	String cmd=map.get("description").toString();
		PrintActivity.pl.connect(cmd);
    }
    
	
	/**
     * 构造SimpleAdapter的第二个参数，类型为List<Map<?,?>>
     * @param title
     * @return
     */
    private List<Map<String, String>> getData(String title) {
    	List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
    	if(deviceList!=null)
    	{
	    	for(int i=0;i<deviceList.size();i++)
	    	{
	    		Map<String, String> map = new HashMap<String, String>();
	    		map.put("title", deviceList.get(i).deviceName);
	    		map.put("description", deviceList.get(i).deviceAddress);
	    		listData.add(map);
	    	}
    	}
    	return listData;
    }

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
    @Override
    public void onBackPressed() {
	new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.str_exit))
		.setIcon(android.R.drawable.ic_dialog_info)
		.setPositiveButton("YES", new DialogInterface.OnClickListener() {
 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// 点击“确认”后的操作
				MainActivity.checkState=false;
				finish();
		    }
		})
		.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// 点击“返回”后的操作,这里不设置没有任何操作
		    }
		}).show();
	// super.onBackPressed();
    }


}
