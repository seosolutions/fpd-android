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


public class rate extends ActionBarActivity {
    public static EditText rate_input;
    public static Button rate_btn;
    public static String id_input;
    public static String mastername;
    public static String score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        final Global globalVariable = (Global)getApplicationContext();
        id_input = globalVariable.getTmpString();
        mastername = globalVariable.getMastername();
        //Toast.makeText(rate.this, id_input+mastername, Toast.LENGTH_LONG).show();
        rate_btn = (Button) findViewById(R.id.button_rate);
        rate_input = (EditText) findViewById(R.id.editText_rate_input);
        rate_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        score = rate_input.getText().toString();
                        //Toast.makeText(rate.this, mastername+id_input+score, Toast.LENGTH_LONG).show();
                        new FetchSQLrate().execute(mastername, id_input, score);
                    }
                }
        );
    }

    private class FetchSQLrate extends AsyncTask<String, Void, String> {
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
                sql = "SELECT * FROM \"deliveryTable\" WHERE id = " + id_input +";";
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval = rs.getString("receiver");
                }
                rs.close();
                st.close();
                conn.close();

                if(retval.equals("")){
                    return "Invalid delivery ID";
                }

                if(!retval.equals(mastername)) {
                    return "You cannot rate this delivery because you are not the receiver";
                }

                //can delete
                int rows;
                try{
                    DriverManager.setLoginTimeout(5);
                    conn = DriverManager.getConnection(url);
                    sql = "UPDATE \"deliveryTable\" SET rate = "+score+" WHERE id = "+id_input+";";
                    PreparedStatement st2 = conn.prepareStatement(sql);
                    rows = st2.executeUpdate();
                    st2.close();
                    conn.close();
                    return ("Rated successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                    return "Database error! Make sure to have valid connections";
                }

            } catch (SQLException e) {
                e.printStackTrace();
                retval = e.toString();
                return retval;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(rate.this, result, Toast.LENGTH_LONG).show();
            /*if(result.equals("")){
                Toast.makeText(SetDE.this, "yes", Toast.LENGTH_LONG).show();
            }*/
        }
    }

}
