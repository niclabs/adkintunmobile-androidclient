package cl.niclabs.adkintunmobile.views.aboutus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cl.niclabs.adkintunmobile.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void openHomeWebPage(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://adkintunmobile.cl"));
        startActivity(browserIntent);
    }

    public void openPrivacyPolicy(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://adkintunmobile.cl/#privacy"));
        startActivity(browserIntent);
    }
}
