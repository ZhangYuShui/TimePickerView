package com.pickerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pickerview.lib.ScreenInfo;
import com.pickerview.lib.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 * 
 * @author Sai
 * 
 */
public class TimePopupWindow extends PopupWindow implements OnClickListener, OnTouchListener {

	private LinearLayout ll_for_now;
	private TextView for_now;
	private OnSelectNowListener onSelectNowListener;

	public enum Type {
		ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN,YEAR_MONTH
	}// 四种选择模式，年月日时分，年月日，时分，月日时分  年月
	
	public enum checkBeforeORAfter {
		before,after
	}
	
	private View rootView; // 总的布局
	WheelTime wheelTime;
//	private View btnSubmit, btnCancel;
	private LinearLayout llSubmit;
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCEL = "cancel";
	private OnTimeSelectListener timeSelectListener;

	public TimePopupWindow(Context context, Type type) {
		super(context);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.timepopwindow_anim_style);

		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
		ll_for_now = (LinearLayout) rootView.findViewById(R.id.ll_for_now);
		for_now = (TextView) rootView.findViewById(R.id.for_now);

		// -----确定和取消按钮
//		btnSubmit = rootView.findViewById(R.id.btnSubmit);
//		btnSubmit.setTag(TAG_SUBMIT);
//		btnCancel = rootView.findViewById(R.id.btnCancel);
//		btnCancel.setTag(TAG_CANCEL);
//		btnSubmit.setOnClickListener(this);
//		btnCancel.setOnClickListener(this);
		
		llSubmit = (LinearLayout) rootView.findViewById(R.id.ll_sure);
		llSubmit.setTag(TAG_SUBMIT);
		llSubmit.setOnClickListener(this);
		// ----时间转轮
		final View timepickerview = rootView.findViewById(R.id.timepicker);
		ScreenInfo screenInfo = new ScreenInfo((Activity) context);
		wheelTime = new WheelTime(timepickerview, type);
		rootView.setOnTouchListener(this);
		wheelTime.screenheight = screenInfo.getHeight();

		// 默认选中当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);

		setContentView(rootView);
	}

	public void setNowShow(){
		ll_for_now.setVisibility(View.VISIBLE);
		for_now.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onSelectNowListener!=null){
					onSelectNowListener.onTimeSelectForNow();
				}
				dismiss();
			}
		});
	}

	public TimePopupWindow(Context context, Type type,checkBeforeORAfter checkbeforeORafter) {
		super(context);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.FILL_PARENT);
		this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.timepopwindow_anim_style);
		
		LayoutInflater mLayoutInflater = LayoutInflater.from(context);
		rootView = mLayoutInflater.inflate(R.layout.pw_time, null);
		ll_for_now = (LinearLayout) rootView.findViewById(R.id.ll_for_now);
		for_now = (TextView) rootView.findViewById(R.id.for_now);
		// -----确定和取消按钮
//		btnSubmit = rootView.findViewById(R.id.btnSubmit);
//		btnSubmit.setTag(TAG_SUBMIT);
//		btnCancel = rootView.findViewById(R.id.btnCancel);
//		btnCancel.setTag(TAG_CANCEL);
//		btnSubmit.setOnClickListener(this);
//		btnCancel.setOnClickListener(this);
		
		llSubmit = (LinearLayout) rootView.findViewById(R.id.ll_sure);
		llSubmit.setTag(TAG_SUBMIT);
		llSubmit.setOnClickListener(this);
		// ----时间转轮
		final View timepickerview = rootView.findViewById(R.id.timepicker);
		ScreenInfo screenInfo = new ScreenInfo((Activity) context);
		wheelTime = new WheelTime(timepickerview, type,checkbeforeORafter);
		rootView.setOnTouchListener(this);
		wheelTime.screenheight = screenInfo.getHeight();
		
		// 默认选中当前时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);
		
		setContentView(rootView);
	}

	/**
	 * 设置可以选择的时间范围
	 * 
	 * @param START_YEAR
	 * @param END_YEAR
	 */
	public void setRange(int START_YEAR, int END_YEAR) {
		WheelTime.setSTART_YEAR(START_YEAR);
		WheelTime.setEND_YEAR(END_YEAR);
	}

	/**
	 * 设置选中时间
	 * 
	 * @param date
	 */
	public void setTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			calendar.setTimeInMillis(System.currentTimeMillis());
		else
			calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);
	}

	/**
	 * 指定选中的时间，显示选择器
	 * 
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 * @param date
	 */
	public void showAtLocation(View parent, int gravity, int x, int y, Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date == null)
			calendar.setTimeInMillis(System.currentTimeMillis());
		else
			calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		wheelTime.setPicker(year, month, day, hours, minute);
		update();
		super.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wheelTime.setCyclic(cyclic);
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag.equals(TAG_CANCEL)) {
			dismiss();
			return;
		} else {
			if (timeSelectListener != null) {
				try {
					Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
					timeSelectListener.onTimeSelect(mType,date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			dismiss();
			return;
		}
	}

	private int mType = 0;
	public void setType(int type){
		this.mType = type;
	}

	public interface OnTimeSelectListener {
		public void onTimeSelect(int p , Date date);

	}

	public interface OnSelectNowListener{
		public void onTimeSelectForNow();//选择的是至今
	}

	public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
		this.timeSelectListener = timeSelectListener;
	}
	public void setOnSelectNowListener(OnSelectNowListener listener){

		this.onSelectNowListener = listener;
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
