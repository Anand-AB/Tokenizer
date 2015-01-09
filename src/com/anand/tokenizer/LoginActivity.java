package com.anand.tokenizer;

import java.util.StringTokenizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}
	public void sign(View v)
	{
		EditText edit_user=(EditText)findViewById(R.id.username_edit);
		EditText edit_pass=(EditText)findViewById(R.id.password_edit);

		String username_entered=edit_user.getText().toString();
		String password_entered=edit_pass.getText().toString();

		boolean loginStatus=false;

		String alluser=(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())).getString("previous_registrations", "");
		if(alluser.contains(username_entered)){
			StringTokenizer stk=new StringTokenizer(alluser, "|");
			StringTokenizer stk2=null;
			int no_of_users = stk.countTokens();
			String usename="";
			String passw="";
			String name="";
			String mail="";
			String phone="";
			String photo="";
			for (int i = 0; i < no_of_users; i++) {
				stk2=new StringTokenizer(stk.nextToken(), ",");
				usename=(stk2.nextToken());
				passw=(stk2.nextToken());
//				name=(stk2.nextToken());
//				mail=(stk2.nextToken());
//				phone=(stk2.nextToken());
//				photo=(stk2.nextToken());
				if(usename.equalsIgnoreCase(username_entered) && passw.equals(password_entered)){
					loginStatus=true;
					Editor e=(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())).edit();
					e.putString("username", username_entered);
					e.putString("password", password_entered);
					e.putString("name", stk2.nextToken());
					e.putString("email", stk2.nextToken());
					e.putLong("phone",Long.parseLong(stk2.nextToken()));
					e.putString("userimagepath", stk2.nextToken());
					e.commit();
					break;
				}

			}
		}else{
			Toast.makeText(getApplicationContext(), "User doesn\'t Exist  Register First.", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, RegisterActivity.class));
			finish();
			return;
		}
		if(loginStatus){
			Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, ShowProfile.class));
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Login Failed\n Enter correct Username and Password.", Toast.LENGTH_LONG).show();
		}

	}
}
