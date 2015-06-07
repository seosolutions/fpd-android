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
                        Intent intent = new Intent("qiwei.fpd.Set_up_a_delivery");
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
                        Intent intent = new Intent("qiwei.fpd.Edit_existing_delivery");
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
                        Intent intent = new Intent("qiwei.fpd.user_profile");
                        startActivity(intent);
                    }
                }
        );
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
