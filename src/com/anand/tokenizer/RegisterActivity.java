package com.anand.tokenizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity {

	ImageView profileImageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void photo(View v) {

		new AlertDialog.Builder(RegisterActivity.this)
		.setTitle("Camera Photo")
		.setMessage("Select This Image as Profile Photo?")
		.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// go to camera Activity
				startActivityForResult(new Intent(RegisterActivity.this, CameraActivity.class), 1);
			}
		}).setNegativeButton("Gallery", 	new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {	
				//Go to Gallery choose
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Choose Profile Image"), 2);
			}
		}).show();


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			File userImageFile=new File(Environment.getExternalStorageDirectory(),""+new Date().getSeconds()+".JPG");//CameraActivity.scaledData
			if(!userImageFile.exists()){
				try {
					userImageFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("userimagepath", userImageFile.getAbsolutePath()).commit();

			System.out.println("Result OK Got");
			profileImageView=(ImageView) findViewById(R.id.registration_userImage);
			//profileImageView.setPlaceholder(getResources().getDrawable(R.drawable.ic_launcher));

			if (requestCode == 1) {
				try {


					FileOutputStream fos = new FileOutputStream(userImageFile);
					fos.write(CameraActivity.scaledData);
					fos.flush();
					fos.close();

					profileImageView.setImageBitmap(new BitmapDrawable(userImageFile.getPath()).getBitmap());

					System.out.println("Image Set from camera");
				} catch (Exception e) {
					System.out.println("Exception from Register Activity Result - From Camera");
					e.printStackTrace();
				}
			}else if (requestCode == 2) {
				try {
					UserPicture usrPic=new UserPicture(data.getData(), getContentResolver());

					//write data to file
					FileOutputStream fos = new FileOutputStream(userImageFile);
					usrPic.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();

					profileImageView.setImageBitmap(usrPic.getBitmap());
					System.out.println("Image Set from Gallery");

				} catch (Exception e) {
					System.out.println("Exception from Register Activity Result - From Gallery");
					e.printStackTrace();
				}
			} else{
				System.out.println("Invalid request Code");
			}
		}
	}

	public void regBtn(View v) {
		boolean registrationOk=true;
		//User details entered by user in Regration page

		EditText e=((EditText)findViewById(R.id.editText1));
		String username_entered=e.getText().toString();

		EditText pass=((EditText)findViewById(R.id.editText2));
		String password_entered=pass.getText().toString();

		EditText name=((EditText)findViewById(R.id.editText3));
		String name_entered=name.getText().toString();

		EditText email=((EditText)findViewById(R.id.editText4));
		String email_entered=email.getText().toString();

		EditText phone=((EditText)findViewById(R.id.editText5));
		String phone_entered=phone.getText().toString();

		if(email_entered.contains("@") && email_entered.contains(".")){
			if(! email_entered.matches("[A-Z a-z _]+@*.*")){
				Toast.makeText(getApplicationContext(), "Invalid Email id", Toast.LENGTH_LONG).show();
				registrationOk = false;
				return;
			}
		}else{
			Toast.makeText(getApplicationContext(), "No proper Email id", Toast.LENGTH_LONG).show();
			registrationOk = false;
			return;

		}

		if(username_entered.equals("")){
			e.setError("Required");
			registrationOk = false;
			return;
		}else{
			String alluser=(PreferenceManager.getDefaultSharedPreferences(getApplicationContext())).getString("previous_registrations", "");
			if(alluser.contains(username_entered)){
				StringTokenizer stk=new StringTokenizer(alluser, "|");
				StringTokenizer stk2=null;
				int no_of_users = stk.countTokens();
				String usename="";
				for (int i = 0; i < no_of_users; i++) {
					stk2=new StringTokenizer(stk.nextToken(), ",");
					usename=(stk2.nextToken());
					if(usename.equalsIgnoreCase(username_entered)){
						Toast.makeText(getApplicationContext(), "Username Already exists! \n Please enter new username.", Toast.LENGTH_LONG).show();
						registrationOk = false;
						return;
					}
				}
			}
		}
		if(password_entered.equals("")){
			pass.setError("Required");
			registrationOk = false;
			return;

		}
		if(name_entered.equals("")){
			name.setError("Required");
			registrationOk = false;
			return;

		}

		if(phone_entered.equals("")||phone_entered.length()!=10){
			phone.setError("Required");
			registrationOk = false;
			return;

		}

		if(registrationOk){
			SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String photo=sp.getString("userimagepath","");
			Editor ed=sp.edit();
			ed.putString("username", username_entered);
			ed.putString("password", password_entered);
			ed.putString("name", name_entered);
			ed.putString("email", email_entered);
			ed.putLong("phone", Long.parseLong(phone_entered));
			ed.putString("photo","");
			ed.putBoolean("login_status", false);


			
			String currentuservalues=username_entered+","+password_entered+","+name_entered+","+email_entered+","+phone_entered+","+photo;

			if(! sp.getString("previous_registrations", "").equals("")){
				//already some registered
				currentuservalues=sp.getString("previous_registrations", "") + "|" +currentuservalues;
			}
			ed.putString("previous_registrations", currentuservalues);
			ed.commit();
			Toast.makeText(getApplicationContext(), "Registered Succesfully. \n Please login to continue", Toast.LENGTH_LONG).show();
			System.out.println("----------------------------------------\n"+sp.getString("previous_registrations", "ONNNUM ILLA")+"\n-------------------------------------------------");
			
			startActivity(new Intent(this, LoginActivity.class));
			finish();

		}

	}
}


