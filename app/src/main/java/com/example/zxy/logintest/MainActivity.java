package com.example.zxy.logintest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private String userNameDb;
    private String passWordDb;
    private Button regisButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regisButton = (Button) findViewById(R.id.regisButton);
        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisActivity.class);
                startActivity(intent);
                Log.i("tag", "-----------------------------");

            }
        });
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        final EditText userNameText = (EditText) findViewById(R.id.userName);
        final EditText passWordText = (EditText) findViewById(R.id.passWord);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Button loginButton = (Button) findViewById(R.id.loginButton);

        SharedPreferences preferences = getSharedPreferences("zzq", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        String userName = preferences.getString("name", null);
        if (userName == null) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
            userNameText.setText(userName);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameText.getText().toString();
                String passWord = passWordText.getText().toString();

                if (userName.isEmpty() || passWord.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Toast.makeText(MainActivity.this,"用户名："+ userName + "\n密码：" + passWord,Toast.LENGTH_SHORT).show();

                SQLiteDatabase db;

                db = openOrCreateDatabase("user.db", MODE_PRIVATE, null);

                db.execSQL("create table if not exists usertb(_id integer primary key autoincrement,user string not null,password string not null)");

                Cursor c = db.rawQuery("select * from usertb", null);
                if (c != null) {
                    Log.i("tag", "!!!!!!!!!!");

                    while (c.moveToNext()) {
                        String[] colum = c.getColumnNames();
                        for (String columName : colum) {
                            Log.i("tag", "columName: " + c.getString(c.getColumnIndex(columName)));
                            Log.i("tag", "--------------");

                        }
                    }
                }


                Cursor cursor = db.rawQuery("select * from usertb where user = ?", new String[]{userName});
//                Toast.makeText(MainActivity.this, "用户名：" + userName + "\n密码：" + passWord, Toast.LENGTH_SHORT).show();

                if (cursor == null) {
                    Log.i("tag", "登录失败");

                }
                if (cursor != null) {

                    while (cursor.moveToNext()) {

                        userNameDb = cursor.getString(cursor.getColumnIndex("user"));
                        passWordDb = cursor.getString(cursor.getColumnIndex("password"));

                        Log.i("tag", "username:" + userNameDb + "password" + passWordDb);
                    }
                }
//                cursor.close();
                Log.i("tag", "username:" + userNameDb + "password" + passWordDb);

                if(userName.equals(userNameDb) && (!passWord.equals(passWordDb))){
                    Toast.makeText(MainActivity.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userName.equals(userNameDb) && passWord.equals(passWordDb)) {

//                    builder.setTitle("");
                    builder.setMessage("登录成功");
                    builder.create().show();
//                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    if (checkBox.isChecked()) {
                        editor.putString("name", userName);
//                        editor.putString("password","123");
                        editor.commit();
                    } else {
                        editor.remove("name");
                        editor.commit();
                    }
                } else {
                    builder.setMessage("登录失败，请注册！");
                    builder.create().show();
//                    db.close();
//                    Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}


