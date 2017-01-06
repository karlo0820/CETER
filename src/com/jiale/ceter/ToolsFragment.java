package com.jiale.ceter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiale.adapter.ToolsViewAdapter;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ToolsFragment extends Fragment {

	private ListView toolView;
	private List<Map<String, Object>> data;
	private ToolsViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_layout_tools, container, false);
		Log.i("ToolsActivity", "����toolsҳ��");
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		initView();
		initEvent();
		super.onActivityCreated(savedInstanceState);
	}

	private void initView() {
		Log.i("tool", "view��ʼ��");
		toolView = (ListView) getActivity().findViewById(R.id.tool_listview);
		adapter = new ToolsViewAdapter(getActivity(), data);
		toolView.setAdapter(adapter);
		Log.i("tool", "view��ʼ������");
	}

	private void initData() {
		Log.i("tool", "data��ʼ��");
		data = new ArrayList<Map<String, Object>>();
		String[] title = new String[] { "���ѧУ���԰���", "��ѯͼ��������", "��ѯ�����õ����", "ѧ���ɼ�����ѯ", "ѧ���α�", "4��6����ѯ" };
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", title[i]);
			data.add(map);
		}
		Log.i("tool", "data��ʼ������");
	}

	private void initEvent() {
		Log.i("tool", "event��ʼ��");
		toolView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectEvent(position);
			}
		});
	}

	private void selectEvent(int position) {
		switch (position) {
		case 0: {
			Log.i("tool", "���԰���");
			Intent intent = new Intent(getActivity(), WyuExamArrangement.class);
			startActivity(intent);
		}
			break;
		case 1: {
			Log.i("tool", "ͼ�����");
			Intent intent = new Intent(getActivity(), LibsActivity.class);
			startActivity(intent);
			break;
		}
		case 2: {
			
			Log.i("tool", "�õ�");
			Intent intent = new Intent(getActivity(), CheckBattery.class);
			startActivity(intent);
			break;
		}
		case 3: {
			Log.i("tool", "�ɼ�");
			Intent intent = new Intent(getActivity(), StudentCJ.class);
			startActivity(intent);
			break;
		}
		case 4: {
			Log.i("tool", "�α�");
			Intent intent = new Intent(getActivity(), StudentSyllabus.class);
			startActivity(intent);
			break;
		}
		case 5: {
			Log.i("tool", "4��6��");
			Intent intent = new Intent(getActivity(), CetFourSix.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
}
