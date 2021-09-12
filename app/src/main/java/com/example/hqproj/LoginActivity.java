package com.example.hqproj;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private MySQLiteOpenHelper helper;
    private Button btn_login;
    private Button btn_register;
    private EditText et_name;
    private EditText et_pwd;
    private String userName;
    private String userPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        helper = new MySQLiteOpenHelper(this,"HQdb",null,1);
        //获取按钮对象
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        //获取输入框对象
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);

        //登录按钮监听事件
        btn_login.setOnClickListener(v -> {
            SQLiteDatabase db = helper.getReadableDatabase();//创建数据库对象

            //获取文本框中的内容
            userName = et_name.getText().toString();
            userPwd = et_pwd.getText().toString();

            //读取数据库中的密码与账号
            String str1="";
            String str2="";
            try {
                Cursor cur = db.rawQuery("select * from User_info",null);
                //比较文本框所填写的内容是否能在数据库查找到
                while (cur.moveToNext()) {
                    str1 = cur.getString(cur.getColumnIndex("user_name"));
                    str2 = cur.getString(cur.getColumnIndex("user_password")) ;
                    if (userName.equals(str1) && userPwd.equals(str2)) {
                        Intent intent =new Intent(LoginActivity.this,PageActivity.class);
//                        Log.d("xxxxxxxxxxxxxxx", "11111111111111 ");
                        Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        return;//不加 return，登陆时会先弹出"用户名或密码不正确！"
//                        Log.d("xxxxxxxxxxxxxxx", "222222222222 ");
                    }else if(userName.equals("") && userPwd.equals("")){
                        Toast.makeText(this, "请输入用户名或密码！", Toast.LENGTH_SHORT).show();
                    }else if(!userName.equals(str1) || !userPwd.equals(str2)){
                        Toast.makeText(this, "用户名或密码不正确！", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (SQLException e){
                setTitle("登录失败");
            }
        });

        //注册按钮监听事件
        btn_register.setOnClickListener(v -> {
            Intent intent =new Intent(LoginActivity.this,RegActivity.class);
            startActivityForResult(intent,1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String name = data.getStringExtra("reg_userName");
                    String pwd = data.getStringExtra("reg_userPwd");
                    //添加数据
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("insert into User_info (user_name,user_password) values(?,?)",new String[]{name,pwd});
                }
        }
    }
}