package cn.edu.njnu.bomb_login_reg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText phoneNum;
    private EditText password;
    private EditText identify_code;
    private Button Message_btn;
    private Button Register_btn;

    private Context ctx;
    private Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initBomb();
        initView();

        initEvent();
    }

    private void initBomb(){
        ctx=RegisterActivity.this;
        act=RegisterActivity.this;
        Bmob.initialize(RegisterActivity.this,"77b64adfa8fd9f860ed96ae1c9203dec");
        BmobSMS.initialize(RegisterActivity.this, "77b64adfa8fd9f860ed96ae1c9203dec");
    }

    private void initView(){

        phoneNum=(EditText)findViewById(R.id.userName_et);
        password= (EditText) findViewById(R.id.passWord_et);
        password.setInputType(InputType.TYPE_NULL);
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputback = password.getInputType();
                new KeyboardUtil(act, ctx, password).showKeyboard();
                password.setInputType(inputback);
                return false;
            }
        });
        identify_code= (EditText) findViewById(R.id.Verity_et);
        Message_btn= (Button) findViewById(R.id.Message_btn);
        Register_btn=(Button)findViewById(R.id.Register_btn);
    }

    private void initEvent() {
        Message_btn.setOnClickListener(this);
        Register_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        Log.e("MESSAGE:", "1");
        String pNum = phoneNum.getText().toString();
        String pWd=password.getText().toString();
        String ify_code = identify_code.getText().toString();
        switch (v.getId()) {
            case R.id.Message_btn:
                if (phoneNum.getText().toString().length() != 11) {
                    Toast.makeText(RegisterActivity.this, "请输入11位有效手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    //进行获取验证码操作和倒计时1分钟操作
                    BmobSMS.requestSMSCode(RegisterActivity.this, phoneNum.getText().toString(), "短信模板", new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                //发送成功时，让获取验证码按钮不可点击，且为灰色
                                Message_btn.setClickable(false);
                                Message_btn.setBackgroundColor(Color.GRAY);
                                Toast.makeText(RegisterActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                                new CountDownTimer(60000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        Message_btn.setBackgroundResource(R.drawable.button_shape02);
                                        Message_btn.setText(millisUntilFinished / 1000 + "秒");
                                    }

                                    @Override
                                    public void onFinish() {
                                        Message_btn.setClickable(true);
                                        Message_btn.setBackgroundResource(R.drawable.button_shape);
                                        Message_btn.setText("重新发送");
                                    }
                                }.start();
                                Log.e("MESSAGE:", "4");
                            } else {
                                Toast.makeText(RegisterActivity.this, "验证码发送失败，服务器正忙", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.Register_btn:
                final MyUser user=new MyUser();
                if (phoneNum.getText().toString().length() == 0 || ify_code.length() == 0 || phoneNum.getText().toString().length() != 11) {
                    Toast.makeText(this, "手机号或验证码输入不合法", Toast.LENGTH_SHORT).show();
                } else {
                    user.setMobilePhoneNumber(pNum);
                    user.setPassword(pWd);
                    user.signOrLogin(ify_code, new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, cn.bmob.v3.exception.BmobException e) {
                            if(e==null){
                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, SuccessActivity.class);
                                //intent.putExtra("user",user);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
 /*                   BmobSMS.verifySmsCode(this, pNum, ify_code, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.e("MESSAGE:", "7");
                                Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, SuccessActivity.class);
                                startActivity(intent);
                            } else {
                                Log.e("MESSAGE:", "8");
                                Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                }
                break;
        }
    }
}
