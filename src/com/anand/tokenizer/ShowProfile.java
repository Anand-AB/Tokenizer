package com.anand.tokenizer;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowProfile extends ActionBarActivity {
	public static String user="";
	public static String passw="";
	public static String mail="";
	public static long phone_saved;
	public static String name="";
	




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.exc12);

		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		user=sp.getString("username", "");
		passw=sp.getString("password", "");
		name=sp.getString("name", "");
		mail=sp.getString("email", "");
		phone_saved=sp.getLong("phone", 0l);

	
		try {
			((ImageView)findViewById(R.id.registration_userImage)).setImageBitmap(BitmapFactory.decodeFile(sp.getString("userimagepath","")));;	
		} catch (Exception e) {
			System.out.println("Unable to set the image from preferences");
			e.printStackTrace();

		}

		String username_saved=sp.getString("username", "");
		String password_saved=sp.getString("password", "");
		String name_saved=sp.getString("name", "");
		String email_saved=sp.getString("email", "");
		long phone_saved=sp.getLong("phone", 0l);

		((TextView)findViewById(R.id.editText3)).setText(username_saved);
		((TextView)findViewById(R.id.editText4)).setText(password_saved);
		((TextView)findViewById(R.id.editText1)).setText(name_saved);
		((TextView)findViewById(R.id.editText2)).setText(email_saved);
		((TextView)findViewById(R.id.editText5)).setText(phone_saved+"");
	}






}
