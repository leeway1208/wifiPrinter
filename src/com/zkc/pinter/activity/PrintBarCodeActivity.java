package com.zkc.pinter.activity;

import com.example.btpdemo76.R;
import com.zkc.helper.printer.BarcodeCreater;
import com.zkc.helper.printer.PrintService;
import com.zkc.helper.printer.PrinterClass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PrintBarCodeActivity extends Activity {
	private Bitmap btMap = null;
	private ImageView iv;
	private TextView et_input;
	private Button bt_bar;
	private Button bt_image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_barcode);
		
		iv = (ImageView) findViewById(R.id.iv_test);
		et_input=(EditText)findViewById(R.id.et_input);
		bt_bar = (Button) findViewById(R.id.bt_bar);
		bt_bar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (PrintActivity.pl.getState() != PrinterClass.STATE_CONNECTED) {
					Toast.makeText(
							PrintBarCodeActivity.this,
							PrintBarCodeActivity.this.getResources().getString(
									R.string.str_unconnected), 2000).show();
					return;
				}
				String message = et_input.getText().toString();

				if (message.getBytes().length > message.length()) {
					Toast.makeText(
							PrintBarCodeActivity.this,
							PrintBarCodeActivity.this.getResources().getString(
									R.string.str_cannotcreatebar), 2000).show();
					return;
				}
				if (message.length() > 0) {

					btMap = BarcodeCreater.creatBarcode(PrintBarCodeActivity.this,
							message, PrintService.imageWidth*8, 100, true, 1);
					iv.setImageBitmap(btMap);
				}

			}
		});
		

		bt_image = (Button) findViewById(R.id.bt_image);
		bt_image.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (btMap != null) {
					PrintActivity.pl.printImage(btMap);
					return;
				}
			}
		});
		
		btMap = BarcodeCreater.creatBarcode(PrintBarCodeActivity.this,
				"9787111291954", PrintService.imageWidth*8, 100, true, 1);
		iv.setImageBitmap(btMap);
	}
}
