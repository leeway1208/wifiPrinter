package com.printer.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btpdemo76.R;
import com.zkc.pinter.activity.ScanDeviceActivity;

public class DeviceListViewAdapter extends BaseAdapter {
	private Context context;

	private LayoutInflater inflater;
	private ScanDeviceActivity scanDeviceActivity;
	ArrayList<HashMap<String, Object>> device_item;

	private final class ViewHolder {
		public Button btnBeep;
		public Button btnLed;
		public ImageView image;
		public TextView title;
		public TextView text;

	}

	public DeviceListViewAdapter(Context context,
			ArrayList<HashMap<String, Object>> listItem,
			ScanDeviceActivity scanDeviceActivity) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.device_item = listItem;
		this.scanDeviceActivity = scanDeviceActivity;
	}

	@Override
	public int getCount() {
		return device_item.size();
	}

	@Override
	public Object getItem(int position) {
		return device_item.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.activity_scan_device_listview, parent, false);
			viewHolder = new ViewHolder();
			// viewHolder.image = (ImageView) convertView
			// .findViewById(R.id.ItemImage);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.ItemTitle);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.ItemText);
			viewHolder.btnBeep = (Button) convertView
					.findViewById(R.id.btn_beep);
			viewHolder.btnLed = (Button) convertView.findViewById(R.id.btn_led);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (device_item.size() > 0)
			setUpView(viewHolder, position);
		return convertView;
	}

	private void setUpView(ViewHolder viewHolder, final int position) {

		// viewHolder.image.setBackground(context.getResources().getDrawable(
		// R.drawable.ble_icon));

		if (device_item.get(position).get("title") != null)
			viewHolder.title.setText(device_item.get(position).get("title")
					.toString());
		if (device_item.get(position).get("text") != null)
			viewHolder.text.setText(device_item.get(position).get("text")
					.toString());

		viewHolder.btnBeep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scanDeviceActivity.beep(position);
			}
		});

		viewHolder.btnLed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scanDeviceActivity.led(position);
			}
		});
		// viewHolder.major_minor_btn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// final Intent intent = new Intent();
		// intent.setClass(context, WriteMajorAndMinorServicesActivity.class);
		// intent.putExtra(ServicesActivity.EXTRAS_DEVICE_NAME,
		// device_item.get(position).get("title").toString());
		// intent.putExtra(ServicesActivity.EXTRAS_DEVICE_ADDRESS,
		// device_item.get(position).get("address").toString());
		//
		// PeripheralActivity.adapter_stop_flag = true;
		// context.startActivity(intent);
		//
		// }
		// });
	}
}
