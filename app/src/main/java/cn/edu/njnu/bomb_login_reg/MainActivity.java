package cn.edu.njnu.bomb_login_reg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText userName_et, passWord_et;
    private Button  login_btn,register_btn,Reset_btn;

    private Context ctx;
    private Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        act = this;

        initBomb();
        passWord_et = (EditText) this.findViewById(R.id.passWord_et);
        passWord_et.setInputType(InputType.TYPE_NULL);
        passWord_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passWord_et.setText("");
                int inputback = passWord_et.getInputType();
                new KeyboardUtil(act, ctx, passWord_et).showKeyboard();
                passWord_et.setInputType(inputback);
                return false;
            }
        });
        initView();

        initEvent();
    }

    private void initEvent() {
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);
        Reset_btn.setOnClickListener(this);
    }

    private void initBomb() {
        Bmob.initialize(MainActivity.this, "77b64adfa8fd9f860ed96ae1c9203dec");
        BmobSMS.initialize(MainActivity.this, "77b64adfa8fd9f860ed96ae1c9203dec");
    }


    @Override
    public void onClick(View v) {
        final String userName = userName_et.getText().toString();
        final String passWord = passWord_et.getText().toString();
        final BmobUser user=new BmobUser();
        switch (v.getId()) {
            case R.id.login_btn:
                if (userName_et.getText().toString().length() == 0 || passWord_et.getText().toString().length() == 0 || userName_et.getText().toString().length() != 11) {
                    Toast.makeText(MainActivity.this, "手机号或验证码输入不合法", Toast.LENGTH_SHORT).show();
                }
                else {
                    BmobUser.loginByAccount(userName, passWord, new LogInListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if(myUser!=null){
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.register_btn:
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.Reset_btn:
                Intent intent2=new Intent(MainActivity.this,ResetActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void initView() {

        userName_et = (EditText) findViewById(R.id.userName_et);

        login_btn = (Button) findViewById(R.id.login_btn);
        register_btn=(Button)findViewById(R.id.register_btn);
        Reset_btn=(Button)findViewById(R.id.Reset_btn);
    }


}