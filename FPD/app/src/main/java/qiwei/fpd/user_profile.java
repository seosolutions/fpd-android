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



public class user_profile extends ActionBarActivity {
    private static EditText friendinput;
    private static Button addhim_btn;
    private static Button removehim_btn;
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
    public static String friendname;
    public static String mastername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final Global globalVariable = (Global)getApplicationContext();
        mastername = globalVariable.getMastername();
        //globalVariable is visable here
        //Toast.makeText(user_profile.this, mastername + globalVariable.getTmpString(), Toast.LENGTH_LONG).show();
        AddHimButton();
        //RemoveHimButton();

    }

    public void AddHimButton(){
        friendinput = (EditText)findViewById(R.id.editText_friendinput);
        addhim_btn = (Button)findViewById(R.id.button_addhim);
        removehim_btn = (Button)findViewById(R.id.button_removehim);

        addhim_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendname = friendinput.getText().toString();
                        //if you want to access global variables here, you have to do the next line
                        //final Global globalVariable = (Global)getApplicationContext();
                        new FetchSQLaddfriend().execute(mastername, friendname);
                    }
                }
        );
    }




    private class FetchSQLaddfriend extends AsyncTask<String, Void, String> {
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
                sql = "SELECT * FROM \"userTable\" WHERE username = '" + friendname +"';";
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval = rs.getString("username");
                }
                rs.close();
                st.close();
                conn.close();
                if(retval.equals("") || retval.equals(mastername)){
                    //the friend entered is not an user or is master himself
                    retval = "invalid";
                    return retval;
                } else {
                    //now check if they are friends already
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

                    String masterfriendlist = retval;

                    String[] splitArr = convertStringToArray(masterfriendlist);
                    //retval = String.valueOf(splitArr.length);
                    for(int i=0; i<splitArr.length; i++){
                        if(splitArr[i].equals(friendname)){
                            retval = "already";
                            return retval;
                        }
                    }
                    //now the two people are addable we update the database
                    //get friend's friendlist
                    DriverManager.setLoginTimeout(5);
                    conn = DriverManager.getConnection(url);
                    st = conn.createStatement();
                    sql = "SELECT * FROM \"userTable\" WHERE username = '" + friendname +"';";
                    rs = st.executeQuery(sql);
                    while(rs.next()) {
                        retval = rs.getString("friendlist");
                    }
                    rs.close();
                    st.close();
                    conn.close();
                    String friendfriendlist = retval;

                    String masteradded = masterfriendlist + "," + friendname;
                    String friendaded = friendfriendlist + "," + mastername;

                    int rows;
                    try{
                        DriverManager.setLoginTimeout(5);
                        conn = DriverManager.getConnection(url);
                        PreparedStatement st2 = conn.prepareStatement("UPDATE \"userTable\" SET friendlist = '"+masteradded+"'WHERE username = '"+mastername+"';");
                        PreparedStatement st3 = conn.prepareStatement("UPDATE \"userTable\" SET friendlist = '"+friendaded+"' WHERE username = '"+friendname+"';");
                    rows = st2.executeUpdate();
                    rows = st3.executeUpdate();
                    st.close();
                    conn.close();
                    } catch (SQLException e) {
                    e.printStackTrace();
                    rows = -1;
                    }
                    return "added";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                retval = e.toString();
                return retval;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(user_profile.this, result, Toast.LENGTH_LONG).show();

            //Toast.makeText(user_profile.this, result, Toast.LENGTH_LONG).show();

            //Toast.makeText(user_profile.this, (convertStringToArray(value))[1], Toast.LENGTH_LONG).show();
        }
    }


}
