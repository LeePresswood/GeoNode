package com.leepresswood.geonode.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leepresswood.geonode.R;
import com.leepresswood.geonode.db.DBManager;

import org.w3c.dom.Text;

public class LoginActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	 {// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	 {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_settings)
		  {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view)
	 {
		 //Gather the username and password
		 TextView usernameBox = (TextView) this.findViewById(R.id.textfield_username);
		 TextView passwordBox = (TextView) this.findViewById(R.id.textfield_password);

		 String username = usernameBox.getText().toString();
		 String password = passwordBox.getText().toString();

		 //Protect from SQL injection
		 DBManager dbmanager = new DBManager();
		 boolean isSafeFromSQL = checkSqlInjection(username, password);

		 //Get the strings for the query
		 String url = "http://babbage.cs.missouri.edu/~lmp6yb/GeoNode/services/login.php";
		 String query = "SELECT COUNT(*) FROM GeoNode.login WHERE username = " + username + " AND password = " + password + ";";

		 //Query the login service
		 dbmanager.queryGetData(url, query);
    }

    public void loginWithoutLogin(View view)
	 {
		 //Intent i = new Intent();
    }

	private boolean checkSqlInjection(String username, String password)
	{
		String newUsername = DBManager.htmlspecialchars(username);
		String newPassword = DBManager.htmlspecialchars(password);

		//If anything changed, there was a bad character
		if(!username.equalsIgnoreCase(newUsername) || !password.equalsIgnoreCase(newPassword))
		{
			//Create a tooltip over the bad box. If both are bad, just do it over the username.
			if(!username.equalsIgnoreCase(newUsername))
			{
				//Tooltip create here

			}
			else
			{
				//Tooltip create here

			}

			return false;
		}

		//Otherwise, there was nothing wrong
		return true;
	}
}
