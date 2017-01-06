package com.jiale.ceter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class BatteryDataFragment extends Fragment {
	private TextView buildNum;
	private TextView apartNum;
	private TextView usedNum;
	private TextView leaseNum;
	private TextView recode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.tools_of_battery_show, container, false);
		buildNum = (TextView) view.findViewById(R.id.battery_show_buildID);
		apartNum = (TextView) view.findViewById(R.id.battery_show_apartID);
		usedNum = (TextView) view.findViewById(R.id.battery_show_usedID);
		leaseNum = (TextView) view.findViewById(R.id.battery_show_leaseID);
		recode = (TextView) view.findViewById(R.id.battery_show_recode);
		Bundle bundle = getArguments();
		List<String> data = bundle.getStringArrayList("data");
		buildNum.setText(data.get(0));
		apartNum.setText(data.get(1));
		usedNum.setText(data.get(2));
		leaseNum.setText(data.get(3));
		recode.setText(data.get(4));
		return view;
	}

}
