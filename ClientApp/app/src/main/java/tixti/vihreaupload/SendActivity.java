package tixti.vihreaupload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class SendActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        final String viesti = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        final TextView tv = new TextView(this);
        tv.setTextSize(20);
        tv.setText(viesti);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(tv);

        Button postaa = (Button) findViewById(R.id.posta);
        postaa.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("nimi", "andoird");
                params.put("kuvateksti", "andoirdlähetäää");
                try {
                    params.put("image", new File((viesti)));
                } catch (Exception e) {

                    e.printStackTrace();
                }
                //192.168.43.59:3000/post
                client.post("http://192.168.43.59:3000/post", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        tv.setText("Kysyppä joku toinen");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        tv.setText("lolootparaseividdutäätoimi?");
                    }
                });
            }
        });
    }


}
