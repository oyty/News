package com.oyty.ui.widget.setting;


public class RowDescriptor extends BaseRowDescriptor {

	public int iconResId;
	public String label;
	
	public RowDescriptor() {
		super();
	}

	public RowDescriptor(int iconResId, String label, NormalRowView.RowActionEnum action) {
		super();
		this.iconResId = iconResId;
		this.label = label;
		this.action = action;
	}
	
	
}
