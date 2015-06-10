package qiwei.fpd;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class create_account extends ActionBarActivity {

    private static EditText username;
    private static EditText password;
    private static EditText zip;
    private static Button create_account_btn;
    private static String mastername;
    private static String masterpassword;
    private static String masterzip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        CreateAccButton();
    }


    public void CreateAccButton(){
        username = (EditText)findViewById(R.id.editText_c_username);
        password = (EditText)findViewById(R.id.editText_c_password);
        zip = (EditText)findViewById(R.id.editText_c_zip);
        create_account_btn = (Button)findViewById(R.id.button_create_my_acc);

        create_account_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        mastername = username.getText().toString();
                        masterpassword = password.getText().toString();
                        masterzip = zip.getText().toString();
                        new FetchSQLcreateaccount().execute(mastername, masterpassword, masterzip);
                    }
                }
        );

    }

    private class FetchSQLcreateaccount extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            int rows;
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String url = "jdbc:postgresql://10.0.2.2/postgres?user=postgres&password=1234";
            Connection conn;
            try{
                DriverManager.setLoginTimeout(5);
                conn = DriverManager.getConnection(url);
                PreparedStatement st = conn.prepareStatement("INSERT INTO \"userTable\" (username, password, zip) VALUES ('" + mastername + "', '" + masterpassword +"','"+masterzip+"');");
                rows = st.executeUpdate();
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                rows = -1;
            }
            return rows;
        }
        @Override
        protected void onPostExecute(Integer value) {
            if(value == -1){
                Toast.makeText(create_account.this, "The username is already used. Retry please.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(create_account.this, "Success! Logging in!", Toast.LENGTH_LONG).show();
                final Global globalVariable = (Global)getApplicationContext();
                globalVariable.setMastername(mastername);
                Intent intent = new Intent(getBaseContext(), user.class);
                startActivity(intent);
            }


        }
    }
}
