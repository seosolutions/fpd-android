package qiwei.fpd;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private static Button start_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onClickToLogin();
    }

    public void onClickToLogin(){
        start_btn = (Button)findViewById(R.id.button_startapp);
        start_btn.setOnClickListener(
                new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(getBaseContext(), login.class);
                    startActivity(intent);
                }
            }
        );
    }
}
