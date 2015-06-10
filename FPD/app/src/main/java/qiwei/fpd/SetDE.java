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


public class SetDE extends ActionBarActivity {
    private static EditText parcel;
    private static EditText receiver;
    private static EditText payment;
    private static EditText zip;
    private static EditText date;
    private static EditText time;
    private static Button setaction;

    public static String mastername;
    private static String parcelV;
    private static String receiverV;
    private static String paymentV;
    private static String zipV;
    private static String dateV;
    private static String timeV;

    public static String strSeparator = ",";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_de);
        final Global globalVariable = (Global)getApplicationContext();
        mastername = globalVariable.getMastername();
        GoButton();
    }

    public void GoButton(){
        parcel = (EditText)findViewById(R.id.editText_parcel_input);
        receiver = (EditText)findViewById(R.id.editText_receiver_input);
        payment = (EditText)findViewById(R.id.editText_payment_input);
        zip = (EditText)findViewById(R.id.editText_zip_input);
        date = (EditText)findViewById(R.id.editText_date_input);
        time = (EditText)findViewById(R.id.editText_time_input);
        setaction = (Button)findViewById(R.id.button_gosetde);

        setaction.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parcelV = parcel.getText().toString();
                        receiverV = receiver.getText().toString();
                        paymentV = payment.getText().toString();
                        zipV = zip.getText().toString();
                        dateV = date.getText().toString();
                        timeV = time.getText().toString();
                        //the following are all correct in the current scope
                        //Toast.makeText(SetDE.this, mastername+parcelV+receiverV+paymentV+zipV+dateV+timeV, Toast.LENGTH_LONG).show();
                        new FetchSQLadddirectde().execute(mastername, parcelV, receiverV, paymentV, zipV, dateV, timeV);
                    }
                }
        );
    }

    private class FetchSQLadddirectde extends AsyncTask<String, Void, String> {
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
                sql = "SELECT * FROM \"userTable\" WHERE username = '" + receiverV +"';";
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval = rs.getString("username");
                }
                rs.close();
                st.close();
                conn.close();

                if(retval.equals("")){
                    return "Receiver has to be an valid user";
                }

                if(retval.equals(mastername)) {
                    return "Receiver cannot be yourself";
                }

                DriverManager.setLoginTimeout(5);
                conn = DriverManager.getConnection(url);
                st = conn.createStatement();
                sql = "SELECT * FROM \"userTable\" WHERE username = '" + mastername +"';";
                rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval = rs.getString("friendlist");
                }
                rs.close();
                st.close();
                conn.close();

                if(retval==null){
                    return "You do not have any friends yet. Try to add receiver as friend";
                }

                String tmp = "not friend";

                String[] splitArr = convertStringToArray(retval);
                //retval = String.valueOf(splitArr.length);
                for(int i=0; i<splitArr.length; i++){
                    if(splitArr[i].equals(receiverV)){
                        tmp = "already";
                    }
                }
                if(tmp=="not friend"){
                    return "Receiver is not your friend";
                }

                //now, addable
                int rows = -1;
                DriverManager.setLoginTimeout(5);
                conn = DriverManager.getConnection(url);
                st = conn.createStatement();
                sql = "SELECT MAX(id) FROM \"deliveryTable\";";
                rs = st.executeQuery(sql);
                while(rs.next()) {
                    rows = rs.getInt(1);
                }
                rs.close();
                st.close();
                conn.close();

                //return(String.valueOf(rows));
                int rows2 = rows+1;

                try{
                    DriverManager.setLoginTimeout(5);
                    conn = DriverManager.getConnection(url);
                    sql = "INSERT INTO \"deliveryTable\"(id, parcel, sender, receiver, type, through, zip, date, meettime, payment) VALUES ("+rows2+", '"+parcelV+"', '"+mastername+"', '"+receiverV+"', 'D', '', '"+zipV+"', '"+dateV+"', '"+timeV+"','"+paymentV+"');";
                    PreparedStatement st2 = conn.prepareStatement(sql);
                    rows = st2.executeUpdate();
                    st2.close();
                    conn.close();
                    return ("The ID for this delivery is "+rows2);
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
            Toast.makeText(SetDE.this, result, Toast.LENGTH_LONG).show();
            /*if(result.equals("")){
                Toast.makeText(SetDE.this, "yes", Toast.LENGTH_LONG).show();
            }*/
        }
    }


}
