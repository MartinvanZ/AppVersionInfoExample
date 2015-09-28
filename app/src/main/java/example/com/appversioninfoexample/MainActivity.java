package example.com.appversioninfoexample;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_get_version_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVersionInfo();
            }
        });

        checkAppVersion();
    }

    private void checkAppVersion() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int currentAppVersionCode = getCurrentAppVersionCode();
        int oldAppVersion = prefs.getInt("app_version", 0);
        if (oldAppVersion < currentAppVersionCode) {
            try {
                if (oldAppVersion > 0)
                    Toast.makeText(this, String.format("App updated from version %d", oldAppVersion), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, String.format("App started for the first time", oldAppVersion), Toast.LENGTH_SHORT).show();
            } finally {
                SharedPreferences.Editor preferencesEditor = prefs.edit();
                preferencesEditor.putInt("app_version", currentAppVersionCode);
                preferencesEditor.commit();
            }
        }
    }

    private int getCurrentAppVersionCode() {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //get the current version number and name
    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
        textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));

        //Use the following code to get the version info from the Gradle build file.
//        versionName = BuildConfig.VERSION_NAME;
//        versionCode = BuildConfig.VERSION_CODE;
//        textViewVersionInfo.setText(textViewVersionInfo.getText() +
//                String.format("\nGradle build config version name = %s \nGradle build config version code = %d", versionName, versionCode));
    }

}
