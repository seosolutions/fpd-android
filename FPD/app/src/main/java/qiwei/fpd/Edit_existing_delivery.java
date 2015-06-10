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


public class Edit_existing_delivery extends ActionBarActivity {
    private static Button cancel_btn;
    private static Button arrive_btn;
    private static Button receive_btn;
    private static Button view_btn;
    private static EditText id_area;
    private static String id_input;
    public static String mastername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_existing_delivery);
        final Global globalVariable = (Global)getApplicationContext();
        mastername = globalVariable.getMastername();
        viewButton();
        cancelButton();
        arriveButton();
        receiveButton();
    }

    public void viewButton(){
        view_btn = (Button)findViewById(R.id.button_view_my);
        view_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new FetchSQLview().execute(mastername);
                    }
                }
        );

    }
    public void cancelButton(){
        cancel_btn = (Button)findViewById(R.id.button_cancel);
        id_area = (EditText)findViewById(R.id.editText_deIDinput);
        cancel_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id_input = id_area.getText().toString();
                        new FetchSQLcancel().execute(mastername, id_input);
                    }
                }
        );

    }

    public void arriveButton() {
        arrive_btn = (Button) findViewById(R.id.button_report_arrive);
        id_area = (EditText) findViewById(R.id.editText_deIDinput);
        arrive_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id_input = id_area.getText().toString();
                        Toast.makeText(Edit_existing_delivery.this, "A reminder notice will be sent to the receiver. ", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void receiveButton() {
        receive_btn = (Button) findViewById(R.id.button_delivery_receival);
        id_area = (EditText) findViewById(R.id.editText_deIDinput);
        receive_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        id_input = id_area.getText().toString();
                        final Global globalVariable = (Global)getApplicationContext();
                        globalVariable.setTmpString(String.valueOf(id_input));
                        Intent intent = new Intent(getBaseContext(), rate.class);
                        startActivity(intent);
                    }
                }

        );
    }

    private class FetchSQLview extends AsyncTask<String, Void, String> {
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

            retval = "";
            try {
                DriverManager.setLoginTimeout(5);
                conn = DriverManager.getConnection(url);
                Statement st = conn.createStatement();
                String sql;
                sql = "SELECT id, parcel FROM \"deliveryTable\" WHERE sender = '" + mastername +"';";
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval = retval + "("+ String.valueOf(rs.getInt("id"));
                    retval = retval + "," + rs.getString("parcel") + ")";
                }
                rs.close();
                st.close();
                conn.close();

                return(retval);

            } catch (SQLException e) {
                e.printStackTrace();
                retval = e.toString();
                return retval;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Edit_existing_delivery.this, result, Toast.LENGTH_LONG).show();
            /*if(result.equals("")){
                Toast.makeText(SetDE.this, "yes", Toast.LENGTH_LONG).show();
            }*/
        }
    }

    private class FetchSQLcancel extends AsyncTask<String, Void, String> {
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
                    retval = rs.getString("sender");
                }
                rs.close();
                st.close();
                conn.close();

                if(retval.equals("")){
                    return "This is an invalid delivery ID";
                }

                if(!retval.equals(mastername)) {
                    return "You cannot cancel this delivery because you are not the sender";
                }

                //can delete
                int rows;
                try{
                    DriverManager.setLoginTimeout(5);
                    conn = DriverManager.getConnection(url);
                    sql = "DELETE FROM \"deliveryTable\" WHERE id = "+id_input+";";
                    PreparedStatement st2 = conn.prepareStatement(sql);
                    rows = st2.executeUpdate();
                    st2.close();
                    conn.close();
                    return ("Canceled successfully");
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
            Toast.makeText(Edit_existing_delivery.this, result, Toast.LENGTH_LONG).show();
            /*if(result.equals("")){
                Toast.makeText(SetDE.this, "yes", Toast.LENGTH_LONG).show();
            }*/
        }
    }

}
