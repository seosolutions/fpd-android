package qiwei.fpd;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class user extends ActionBarActivity {

    private static Button setup_btn;
    private static Button edit_btn;
    private static Button profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        onClickToSetup();
        onClickToEdit();
        onClickToProfile();
    }

    public void onClickToSetup(){
        setup_btn = (Button)findViewById(R.id.button_setup);
        setup_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getBaseContext(), Set_up_a_delivery.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickToEdit(){
        edit_btn = (Button)findViewById(R.id.button_editDelivery);
        edit_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getBaseContext(), Edit_existing_delivery.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickToProfile(){
        profile_btn = (Button)findViewById(R.id.button_profile);
        profile_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getBaseContext(),user_profile.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
