package com.zkc.pinter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.btpdemo76.R;
import com.printer.adapter.DeviceListViewAdapter;
import com.printer.bluetooth.BluetoothUtils;
import com.printer.model.Device;

public class ScanDeviceActivity extends Activity implements
		BluetoothUtils.BleConnectCallback {
	private HashMap<String, Device> deviceMap = new HashMap<String, Device>();
	private ArrayList<HashMap<String, Object>> device_listitem;
	private DeviceListViewAdapter deviceAdapter;

	private Button bt_scan;
	private String device_address;
	private BluetoothUtils mBluetoothUtils;
	private BluetoothAdapter.LeScanCallback leScanCallback;
	private ListView myList;
	private boolean is_start_scan = false;
	private Dialog start_dialog;
	private static final int MSG_FAILURE = 1;
	private static final int MSG_SUCCESS = 0;

	private boolean isBeepFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_device);

		device_listitem = new ArrayList<HashMap<String, Object>>();
		deviceAdapter = new DeviceListViewAdapter(ScanDeviceActivity.this,
				device_listitem, ScanDeviceActivity.this);

		myList = (ListView) findViewById(R.id.device_listView);
		myList.setAdapter(deviceAdapter);

		// myList.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // mBluetoothUtils.stopLeScan();
		//
		// mBluetoothUtils.writeBeep(
		// device_listitem.get(arg2).get("address").toString(),
		// 10000, "01");
		// device_address = device_listitem.get(arg2).get("address")
		// .toString();
		// System.out.println(device_listitem.get(arg2).get("address")
		// .toString());
		// }
		// });

		bt_scan = (Button) findViewById(R.id.bt_scan);
		bt_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!is_start_scan) {
					mBluetoothUtils.startLeScan(leScanCallback, 500);
					bt_scan.setText("Stop scan device");
					is_start_scan = true;
				} else {
					mBluetoothUtils.stopLeScan();
					bt_scan.setText("Start scan device");
					is_start_scan = false;
					device_listitem.clear();
					deviceAdapter.notifyDataSetChanged();

					clearTheData();
					deviceAdapter.notifyDataSetChanged();
				}

			}
		});

		leScanCallback = new LeScanCallback() {

			@Override
			public void onLeScan(final BluetoothDevice device, final int rssi,
					final byte[] scanRecord) {
				// TODO Auto-generated method stub

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						Device newDevice = new Device();
						newDevice.setAddress(device.getAddress());
						newDevice.setMajor(scanRecord[25] * 256
								+ scanRecord[26]);
						newDevice.setMinor(scanRecord[27] * 256
								+ scanRecord[28]);
						newDevice.setName(device.getName());
						newDevice.setUuid(bytesToHex(scanRecord, 9, 16));
						newDevice.setRssi(rssi);

						if (deviceMap.put(device.getAddress(), newDevice) != null) {
							Iterator<Entry<String, Device>> it = deviceMap
									.entrySet().iterator();
							device_listitem.clear();

							// MissChildData.clear();
							while (it.hasNext()) {
								HashMap<String, Object> map = new HashMap<String, Object>();

								Map.Entry<String, Device> entry = it.next();

								map.put("image", R.drawable.ble_icon);
								map.put("title", entry.getValue().getName());
								map.put("text", entry.getValue().getAddress()
										+ " UUID:" + entry.getValue().getUuid()
										+ " MajorID:"
										+ entry.getValue().getMajor()
										+ " MinorID:"
										+ entry.getValue().getMinor()
										+ " RSSI:" + entry.getValue().getRssi());
								map.put("address", entry.getValue()
										.getAddress());
								device_listitem.add(map);

							}

							deviceAdapter.notifyDataSetChanged();
						}

					}

				});

			}
		};

		mBluetoothUtils = new BluetoothUtils(this, getFragmentManager(), this);

	}

	public void beep(int arg2) {
		mBluetoothUtils.stopLeScan();

		mBluetoothUtils.writeBeep(device_listitem.get(arg2).get("address")
				.toString(), 10000, "01");

		isBeepFlag = true;
		device_address = device_listitem.get(arg2).get("address").toString();
		System.out.println(device_listitem.get(arg2).get("address").toString());
	}

	public void led(int arg2) {
		mBluetoothUtils.stopLeScan();

		mBluetoothUtils.writeLed(device_listitem.get(arg2).get("address")
				.toString(), 10000, "08");

		isBeepFlag = false;
	}

	public static String bytesToHex(byte[] bytes, int begin, int length) {
		StringBuilder sbuf = new StringBuilder();
		for (int idx = begin; idx < begin + length; idx++) {
			int intVal = bytes[idx] & 0xff;
			if (intVal < 0x10)
				sbuf.append("0");
			sbuf.append(Integer.toHexString(intVal).toUpperCase());
		}
		return sbuf.toString();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mBluetoothUtils != null) {
			mBluetoothUtils.registerReceiver();
		}

		if (!is_start_scan) {
			mBluetoothUtils.startLeScan(leScanCallback, 500);
			bt_scan.setText("Stop scan device");
			is_start_scan = true;
		} else {
			mBluetoothUtils.stopLeScan();
			bt_scan.setText("Start scan device");
			is_start_scan = false;
			device_listitem.clear();
			deviceAdapter.notifyDataSetChanged();

			clearTheData();
			deviceAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onPreConnect() {
		// TODO Auto-generated method stub
		start_dialog = LoadingDialog.createLoadingDialogCanCancel(
				ScanDeviceActivity.this, "loading...");
		start_dialog.show();
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub

	}

	private void clearTheData() {
		if (device_listitem != null) {
			Iterator<HashMap<String, Object>> it2 = device_listitem.iterator();
			for (; it2.hasNext();) {
				it2.next();
				it2.remove();
			}
		}

		if (deviceMap != null) {
			deviceMap.clear();
		}

	}

	@Override
	public void onDisConnected() {
		// TODO Auto-generated method stub
		if (start_dialog.isShowing() && start_dialog != null) {
			start_dialog.dismiss();
		}

	}

	@Override
	public void onDiscovered() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataAvailable(String value) {
		System.out.println(Integer.parseInt(value, 16) + "%");

	}

	@Override
	public void onResult(boolean result) {
		// TODO Auto-generated method stub

		System.out.println("result---->" + result);
		if (result) {
			if (start_dialog.isShowing() && start_dialog != null) {
				start_dialog.dismiss();
			}
			if (isBeepFlag) {

				Intent intent = new Intent(ScanDeviceActivity.this,
						WriteSuccessActivity.class);
				intent.putExtra("address", device_address);

				mBluetoothUtils.stopLeScan();
				bt_scan.setText("Start scan device");
				is_start_scan = false;
				isBeepFlag = false;
				// mBluetoothUtils.disconnect();
				mBluetoothUtils.close();
				startActivity(intent);
			} else {
				Message ms = new Message();
				ms.what = MSG_SUCCESS;
				mHandler.sendMessage(ms);
				mBluetoothUtils.close();
			}

		} else {
			Message ms = new Message();
			ms.what = MSG_FAILURE;
			mHandler.sendMessage(ms);
		}

	}

	@Override
	public void onConnectCanceled() {
		// TODO Auto-generated method stub
		if (mBluetoothUtils != null) {
			mBluetoothUtils.close();

		}
		if (start_dialog.isShowing() && start_dialog != null) {
			start_dialog.dismiss();
		}

		Message ms = new Message();
		ms.what = MSG_FAILURE;
		mHandler.sendMessage(ms);

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mBluetoothUtils != null) {
			mBluetoothUtils.unregisterReceiver();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBluetoothUtils != null) {
			mBluetoothUtils.disconnect();
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				Toast.makeText(ScanDeviceActivity.this, "寫入led成功!",
						Toast.LENGTH_LONG).show();
				break;

			case MSG_FAILURE:
				Toast.makeText(ScanDeviceActivity.this, "寫入失敗!",
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
}
