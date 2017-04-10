package cn.edu.njnu.bomb_login_reg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity implements View.OnClickListener{

   private Button return_main;
   private Button return_reset;
   private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        return_main=(Button)findViewById(R.id.return_main);
        return_reset=(Button)findViewById(R.id.return_reset);
        logout=(Button)findViewById(R.id.logout);
        return_main.setOnClickListener(this);
        return_reset.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.return_main:
                Intent intent=new Intent(SuccessActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.return_reset:
                Intent intent2=new Intent(SuccessActivity.this,ResetActivity.class);
                startActivity(intent2);
                break;
            case R.id.logout:
                System.exit(0);
                break;
        }
    }
}
