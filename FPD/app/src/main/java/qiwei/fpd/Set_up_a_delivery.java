package qiwei.fpd;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Set_up_a_delivery extends ActionBarActivity {
    private static Button de_btn;
    private static Button ind_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_a_delivery);
        SetDeButton();
        SetIndButton();
    }

    public void SetDeButton(){
        de_btn = (Button)findViewById(R.id.button_setde);
        de_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getBaseContext(), SetDE.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void SetIndButton(){
        ind_btn = (Button)findViewById(R.id.button_setind);
        ind_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getBaseContext(), SetIND.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
