package com.example.zxy.logintest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by zxy on 2018/3/14.
 */

public class RegisActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tag","RegisActivity start-----------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis);

        Button button;
        final EditText userNameText = (EditText)findViewById(R.id.userName);
        final EditText passWordText = (EditText)findViewById(R.id.passWord);
        button = (Button)findViewById(R.id.regisButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db ;

                db = openOrCreateDatabase("user.db",MODE_PRIVATE,null);

                db.execSQL("create table if not exists usertb(_id integer primary key autoincrement,user string unique not null,password string not null)");
                String userName = userNameText.getText().toString();
                String passWord = passWordText.getText().toString();
//                Log.i("tag","" + userName+ " " + passWord);

                Cursor cursor = db.rawQuery("select * from usertb where user = ?", new String[]{userName});

                String userNameDb = null;
                if (cursor != null) {

                    while (cursor.moveToNext()) {

                        userNameDb = cursor.getString(cursor.getColumnIndex("user"));
//                        passWordDb = cursor.getString(cursor.getColumnIndex("password"));

//                        Log.i("tag", "username:" + userNameDb + "password" + passWordDb);
                    }
                }
                if(userName.equals(userNameDb)){
                    Toast.makeText(RegisActivity.this,"用户已注册，请直接登录",Toast.LENGTH_SHORT).show();
                    return;
                }
//                db.delete("usertb",null,null);
                ContentValues content = new ContentValues();
                content.put("user", userName);
                content.put("password",passWord);
                db.insert("usertb",null,content);
                Toast.makeText(RegisActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
//                Cursor c = db.rawQuery("select * from usertb", null);
//                if (c != null) {
//                    Log.i("tag","" + c.getCount());
//
//                    while (c.moveToNext()) {
//                        String[] colum = c.getColumnNames();
//                        for (String columName : colum) {
//                            Log.i("tag", "columName: " + c.getString(c.getColumnIndex(columName)));
//                            Log.i("tag", "--------------");
//
//                        }
//                    }
//                }
                db.close();
                finish();
            }
        });
    }
}
