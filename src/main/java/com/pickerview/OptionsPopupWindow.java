package com.pickerview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.pickerview.lib.ScreenInfo;
import com.pickerview.lib.WheelOptions;

/**
 * 选项选择器，可支持一二三级联动选择
 * 
 * @author Sai
 *
 */
public class OptionsPopupWindow extends PopupWindow implements OnClickListener, OnTouchListener {
	private View rootView; // 总的布局
	WheelOptions wheelOptions;
	private View btnSubmit, btnCancel;
	private OnOptionsSelectListener optionsSelectListener;
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCEL = "cancel";

	public OptionsPopupWindow(Context context) {
		super(context);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
		this.setOutsideTouchable(false);
		this.setAnimationStyle(R.style.timepopwindow_anim_style);

		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		rootView = mLayoutInflater.inflate(R.layout.pw_options, null);
		// -----确定和取消按钮
		btnSubmit = rootView.findViewById(R.id.btnSubmit);
		btnSubmit.setTag(TAG_SUBMIT);
		btnCancel = rootView.findViewById(R.id.btnCancel);
		btnCancel.setTag(TAG_CANCEL);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		rootView.setOnTouchListener(this);
		// ----转轮
		final View optionspicker = rootView.findViewById(R.id.optionspicker);
		ScreenInfo screenInfo = new ScreenInfo((Activity) context);
		wheelOptions = new WheelOptions(optionspicker);

		wheelOptions.screenheight = screenInfo.getHeight();
		setContentView(rootView);
	}

	public void setPicker(ArrayList<String> optionsItems) {
		wheelOptions.setPicker(optionsItems, null, null, false);
	}

	public void setPicker(ArrayList<String> options1Items,
			ArrayList<ArrayList<String>> options2Items, boolean linkage) {
		wheelOptions.setPicker(options1Items, options2Items, null, linkage);
	}

	/**
	 * 设置选中的item位置
	 * 
	 * @param option1
	 */
	public void setSelectOptions(int option1) {
		wheelOptions.setCurrentItems(option1, 0);
	}

	/**
	 * 设置选中的item位置
	 * 
	 * @param option1
	 * @param option2
	 */
	public void setSelectOptions(int option1, int option2) {
		wheelOptions.setCurrentItems(option1, option2);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 */
	public void setLabels(String label1) {
		wheelOptions.setLabels(label1, null);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 */
	public void setLabels(String label1, String label2) {
		wheelOptions.setLabels(label1, label2);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wheelOptions.setCyclic(cyclic);
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag.equals(TAG_CANCEL)) {
			dismiss();
			return;
		} else {
			if (optionsSelectListener != null) {
				int[] optionsCurrentItems = wheelOptions.getCurrentItems();
				optionsSelectListener.onOptionsSelect(optionsCurrentItems[0],
						optionsCurrentItems[1]);
			}
			dismiss();
			return;
		}
	}

	public interface OnOptionsSelectListener {
		public void onOptionsSelect(int options1, int option2);
	}

	public void setOnoptionsSelectListener(
			OnOptionsSelectListener optionsSelectListener) {
		this.optionsSelectListener = optionsSelectListener;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int height = rootView.findViewById(R.id.rl_dimiss).getTop();
		int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (y < height) {
				dismiss();
			}
		}
		return true;
	}
}
