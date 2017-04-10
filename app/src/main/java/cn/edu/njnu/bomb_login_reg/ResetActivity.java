package cn.edu.njnu.bomb_login_reg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.UpdateListener;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText phoneNum;
    private EditText newPass;
    private EditText identify_code;
    private Button Message_btn;
    private Button Reset_btn;

    private Context ctx;
    private Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        initBomb();
        initView();



        initEvent();
    }
    private void initBomb(){
        ctx=ResetActivity.this;
        act=ResetActivity.this;
        Bmob.initialize(ResetActivity.this,"77b64adfa8fd9f860ed96ae1c9203dec");
        BmobSMS.initialize(ResetActivity.this, "77b64adfa8fd9f860ed96ae1c9203dec");
    }
    private void initView(){

        phoneNum=(EditText)findViewById(R.id.userName_et);
        newPass= (EditText) findViewById(R.id.passWord_et);
        newPass.setInputType(InputType.TYPE_NULL);
        newPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputback = newPass.getInputType();
                new KeyboardUtil(act, ctx, newPass).showKeyboard();
                newPass.setInputType(inputback);
                return false;
            }
        });
        identify_code= (EditText) findViewById(R.id.Verity_et);
        Message_btn= (Button) findViewById(R.id.Message_btn);
        Reset_btn=(Button)findViewById(R.id.Reset_btn);
    }
    private void initEvent() {
        Message_btn.setOnClickListener(this);
        Reset_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String pNum = phoneNum.getText().toString();
        String pWd = newPass.getText().toString();
        String ify_code = identify_code.getText().toString();
        switch (v.getId()) {
            case R.id.Message_btn:
                if (phoneNum.getText().toString().length() != 11) {
                    Toast.makeText(ResetActivity.this, "请输入11位有效手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    //进行获取验证码操作和倒计时1分钟操作
                    BmobSMS.requestSMSCode(ResetActivity.this, pNum, "短信模板", new RequestSMSCodeListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                //发送成功时，让获取验证码按钮不可点击，且为灰色
                                Message_btn.setClickable(false);
                                Message_btn.setBackgroundColor(Color.GRAY);
                                Toast.makeText(ResetActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
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
                            } else {
                                Toast.makeText(ResetActivity.this, "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.Reset_btn:
                final MyUser user = new MyUser();
                if (phoneNum.getText().toString().length() == 0 || ify_code.length() == 0 || phoneNum.getText().toString().length() != 11) {
                    Toast.makeText(this, "手机号或验证码输入不合法", Toast.LENGTH_SHORT).show();
                } else {
//                    user.setMobilePhoneNumber(pNum);
//                    user.setPassword(pWd);
                    user.resetPasswordBySMSCode(ify_code, pWd, new UpdateListener() {
                        @Override
                        public void done(cn.bmob.v3.exception.BmobException e) {
                            if (e == null) {
                                Toast.makeText(ResetActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetActivity.this, SuccessActivity.class);
                                //intent.putExtra("user",user);
                                startActivity(intent);
                            }else{
                                Toast.makeText(ResetActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
