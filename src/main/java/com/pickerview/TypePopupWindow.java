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
import com.pickerview.lib.WheelType;

/**
 * 选项选择器，可支持一二三级联动选择
 * 
 * @author Sai
 *
 */
public class TypePopupWindow extends PopupWindow implements OnClickListener, OnTouchListener {
	private View rootView; // 总的布局
	WheelType wheelOptions;
	private View btnSubmit, btnCancel;
	private OnTypeSelectListener typeSelectListener;
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCEL = "cancel";

	public TypePopupWindow(Context context) {
		super(context);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.timepopwindow_anim_style);

		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		rootView = mLayoutInflater.inflate(R.layout.pw_type, null);
		// -----确定和取消按钮
		btnSubmit = rootView.findViewById(R.id.btnSubmit);
		btnSubmit.setTag(TAG_SUBMIT);
		btnCancel = rootView.findViewById(R.id.btnCancel);
		btnCancel.setTag(TAG_CANCEL);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		rootView.setOnTouchListener(this);
		// ----转轮
		final View typepicker = rootView.findViewById(R.id.typepicker);
		ScreenInfo screenInfo = new ScreenInfo((Activity) context);
		wheelOptions = new WheelType(typepicker);

		wheelOptions.screenheight = screenInfo.getHeight();
		setContentView(rootView);	
	}

	public void setPicker(ArrayList<String> optionsItems, boolean linkage) {
		wheelOptions.setPicker(optionsItems, false);
	}

	/**
	 * 设置选中的item位置
	 * 
	 * @param option1
	 */
	public void setSelectOptions(int option1) {
		wheelOptions.setCurrentItems(option1);
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
			if (typeSelectListener != null) {
				int[] optionsCurrentItems = wheelOptions.getCurrentItems();
				typeSelectListener.onTypeSelected(optionsCurrentItems[0]);
			}
			dismiss();
			return;
		}
	}

	public interface OnTypeSelectListener {
		public void onTypeSelected(int options1);
	}

	public void setOntypeSelectListener(
			OnTypeSelectListener typeSelectListener) {
		this.typeSelectListener = typeSelectListener;
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
