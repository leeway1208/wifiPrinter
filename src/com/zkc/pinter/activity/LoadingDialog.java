package com.zkc.pinter.activity;

import com.example.btpdemo76.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



public class LoadingDialog {
	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	private static LayoutInflater inflater;
	private static View v;
	private static Dialog loadingDialog;
	private static TextView content;
	
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		((TextView) v.findViewById(R.id.msg)).setText(msg);

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}

	public static Dialog createLoadingDialogCanCancel(Context context,
			String msg) {

		inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		content = ((TextView) v.findViewById(R.id.msg));
		content.setText(msg);
		content.setGravity(Gravity.CENTER_HORIZONTAL);
		loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
	
		return loadingDialog;

	}

	public static void createLoadingDialogCanCancelForMsg(String beepAlli) {
		content.setText(beepAlli + "");
	}
	
	
}
