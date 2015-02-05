package com.zkc.pinter.activity;

import java.io.UnsupportedEncodingException;

import com.example.btpdemo76.R;
import com.zkc.helper.printer.BarcodeCreater;
import com.zkc.helper.printer.PrintService;
import com.zkc.helper.printer.PrinterClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WriteSuccessActivity extends Activity {

	private String address;
	private TextView tv_address;
	private TextView tv_hint;
	private Button btn_confirm;

	private String write_data;
	private boolean handle_flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_success);

		Intent intent = getIntent();
		address = intent.getStringExtra("address");

		tv_address = (TextView) findViewById(R.id.address);
		tv_hint = (TextView) findViewById(R.id.hint);

		tv_address.setText(address);

		btn_confirm = (Button) findViewById(R.id.btn_confirm);

		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("finish?");
				WriteSuccessActivity.this.finish();
			}
		});

		if (PrintActivity.pl.getState() != PrinterClass.STATE_CONNECTED) {
			Toast.makeText(
					WriteSuccessActivity.this,
					WriteSuccessActivity.this.getResources().getString(
							R.string.str_unconnected), 2000).show();
			return;
		}
		String message = address;
		if (message.length() > 0) {
			try {
				message = new String(message.getBytes("utf8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			BarcodeCreater.saveBitmap2file(BarcodeCreater.encode2dAsBitmap(
					message, PrintService.imageWidth * 8,
					PrintService.imageWidth * 8, 2), "mypic1.JPEG");
			// iv.setImageBitmap(btMap);

		}

		if (BarcodeCreater.encode2dAsBitmap(message,
				PrintService.imageWidth * 8, PrintService.imageWidth * 8, 2) != null) {
			PrintActivity.pl.printImage(BarcodeCreater.encode2dAsBitmap(
					message, PrintService.imageWidth * 8,
					PrintService.imageWidth * 8, 2));
//			if (handle_flag) {
//				PrintActivity.pl.printText("   設備識別碼" + "\r\n");
//				handle_flag = false;
//			}
//
//			PrintActivity.pl.printText("                  " + "\r\n");
//			PrintActivity.pl.printText("                  " + "\r\n");
			return;
		}

	}
}
