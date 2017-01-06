package com.jiale.ceter;

import com.jiale.bean.MyAccount;
import com.jiale.util.CacheUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BindAccount extends Activity implements OnClickListener {
	private EditText accountET;
	private EditText pwdET;
	private EditText pwdRpET;
	private Button bind;
	private Button canel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main_tab_head_bindaccount);
		initView();
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		accountET = (EditText) findViewById(R.id.bindaccount_account);
		pwdET = (EditText) findViewById(R.id.bindaccount_pwd);
		pwdRpET = (EditText) findViewById(R.id.bindaccount_pwdRp);
		bind = (Button) findViewById(R.id.bindaccount_bindBtn);
		canel = (Button) findViewById(R.id.bindaccount_canelBtn);
		bind.setOnClickListener(this);
		canel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bindaccount_bindBtn:
			bindAccount();
			break;
		case R.id.bindaccount_canelBtn:
			finish();
			break;
		default:
			break;
		}
	}

	private void bindAccount() {
		String account = accountET.getText().toString();
		String pwd = pwdET.getText().toString();
		String pwdrp = pwdRpET.getText().toString();
		if (account.equals("") || pwd.equals("") || pwdrp.equals("")) {
			Toast.makeText(BindAccount.this, "输入不能为空", Toast.LENGTH_SHORT).show();
		} else if (!pwd.equals(pwdrp)) {
			Toast.makeText(BindAccount.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
		} else {
			MyAccount ma = new MyAccount(account, pwd);
			CacheUtil.setCache(this, "account", ma);
			if (CacheUtil.isFileExist(this, "account")) {
				Toast.makeText(BindAccount.this, "绑定成功", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(BindAccount.this, "绑定失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

}
