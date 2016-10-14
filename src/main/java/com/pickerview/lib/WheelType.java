package com.pickerview.lib;

import java.util.ArrayList;

import android.view.View;

import com.pickerview.R;

public class WheelType {
	private View view;
	private WheelView wv_option1;

	private ArrayList<String> mOptions1Items;
	public int screenheight;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelType(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void setPicker(ArrayList<String> optionsItems) {
		setPicker(optionsItems, false);
	}

	public void setPicker(ArrayList<String> options1Items, boolean linkage) {
		this.mOptions1Items = options1Items;
		int len = ArrayWheelAdapter.DEFAULT_LENGTH;
		// 选项1
		wv_option1 = (WheelView) view.findViewById(R.id.options1);
		wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 设置显示数据
		wv_option1.setCurrentItem(0);// 初始化时显示的数据

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = (screenheight / 100) * 3;

		wv_option1.TEXT_SIZE = textSize;

		// 联动监听器
		OnWheelChangedListener wheelListener_option1 = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
			}
		};

	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		if (label1 != null)
			wv_option1.setLabel(label1);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
	}

	/**
	 * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
	 * 
	 * @return
	 */
	public int[] getCurrentItems() {
		int[] currentItems = new int[3];
		currentItems[0] = wv_option1.getCurrentItem();
		return currentItems;
	}

	public void setCurrentItems(int option1) {
		wv_option1.setCurrentItem(option1);
	}
}
