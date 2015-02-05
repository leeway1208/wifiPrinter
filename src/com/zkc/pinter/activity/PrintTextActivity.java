package com.zkc.pinter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.btpdemo76.R;
import com.zkc.helper.printer.PrinterClass;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class PrintTextActivity extends Activity {
	List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
	private TextView et_input;
	private CheckBox checkBoxAuto;
	private Button bt_print, btnUnicode;
	private Thread autoprint_Thread;

	int times = 0;// Automatic print time interval
	boolean isPrint = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_text);
		isPrint = true;
		et_input = (EditText) findViewById(R.id.et_input);
		et_input.setText("让我们红尘作伴活的潇潇洒洒,策马奔腾共享人世繁华.");

		bt_print = (Button) findViewById(R.id.bt_print);
		bt_print.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String message = et_input.getText().toString();
				PrintActivity.pl.printText(message + "\r\n");
			}
		});

		btnUnicode = (Button) findViewById(R.id.btnUnicode);
		btnUnicode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String message = et_input.getText().toString();

				PrintActivity.pl.printUnicode(message);
				PrintActivity.pl.printText("\r\n");
			}
		});

		checkBoxAuto = (CheckBox) findViewById(R.id.checkBoxAuto);


		// Auto Print
		autoprint_Thread = new Thread() {
			public void run() {
				while (isPrint) {
					if (checkBoxAuto.isChecked()) {
						String message = et_input.getText().toString();
						PrintActivity.pl.printText(message);
						try {
							Thread.sleep(times);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		autoprint_Thread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		Resources res = getResources();
		String[] cmdStr = res.getStringArray(R.array.cmd);
		for (int i = 0; i < cmdStr.length; i++) {
			String[] cmdArray = cmdStr[i].split(",");
			if (cmdArray.length == 2) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", cmdArray[0]);
				map.put("description", cmdArray[1]);
				menu.add(0, i, i, cmdArray[0]);
				listData.add(map);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		Map map = listData.get(item.getItemId());
		String cmd = map.get("description").toString();
		byte[] bt = PrintCmdActivity.hexStringToBytes(cmd);
		PrintActivity.pl.write(bt);
		Toast toast = Toast.makeText(this, "���ͳɹ�", Toast.LENGTH_SHORT);
		toast.show();
		return false;
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		isPrint=false;
		super.onStop();
	}
}
