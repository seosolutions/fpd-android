package qiwei.fpd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.IntentFilter;
import android.os.AsyncTask;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


public class login extends ActionBarActivity {
    private static EditText username;
    private static EditText password;
    private static TextView attempts;
    private static Button login_btn;
    private static Button create_account_btn;
    int attempt_counter = 3;
    private static String mastername;
    private static String masterpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton();
        CreateAccButton();
    }


    private class FetchSQLgetpassword extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String retval = "";
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                retval = e.toString();
            }
            String url = "jdbc:postgresql://10.0.2.2/postgres?user=postgres&password=1234";
            Connection conn;
            try {
                DriverManager.setLoginTimeout(5);
                conn = DriverManager.getConnection(url);
                Statement st = conn.createStatement();
                String sql;
                sql = "SELECT * FROM \"userTable\" WHERE username = '" + mastername +"';";
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval = rs.getString(2);
                }
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                retval = e.toString();
            }
            return retval;
        }
        @Override
        protected void onPostExecute(String value) {
            if (masterpassword.equals(value)) {
                Toast.makeText(login.this, "Username and password are correct",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("qiwei.fpd.user");
                startActivity(intent);
            } else {
                Toast.makeText(login.this, "Username or password is not correct",
                        Toast.LENGTH_SHORT).show();
                attempt_counter--;
                attempts.setText(Integer.toString(attempt_counter));
                if(attempt_counter == 0){
                    login_btn.setEnabled(false);
                }
            }
        }
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
                PreparedStatement st = conn.prepareStatement("INSERT INTO \"userTable\" (username, password) VALUES ('" + mastername + "', '" + masterpassword +"');");
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
                Toast.makeText(login.this, "The username is already used. Retry please.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(login.this, "Success! Logging in!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent("qiwei.fpd.user");
                startActivity(intent);
            }


        }
    }
    public void LoginButton(){
        username = (EditText)findViewById(R.id.editText_username);
        password = (EditText)findViewById(R.id.editText_password);
        attempts = (TextView)findViewById(R.id.textView_attempts_counter);
        login_btn = (Button)findViewById(R.id.button_login);
        attempts.setText(Integer.toString(attempt_counter));

        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mastername = username.getText().toString();
                        masterpassword = password.getText().toString();
                        new FetchSQLgetpassword().execute(mastername, masterpassword);
                    }
                }
        );
    }

    public void CreateAccButton(){
        username = (EditText)findViewById(R.id.editText_username);
        password = (EditText)findViewById(R.id.editText_password);
        create_account_btn = (Button)findViewById(R.id.button_create_account);

        create_account_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        mastername = username.getText().toString();
                        masterpassword = password.getText().toString();
                        new FetchSQLcreateaccount().execute(mastername, masterpassword);
                    }
                }
        );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
