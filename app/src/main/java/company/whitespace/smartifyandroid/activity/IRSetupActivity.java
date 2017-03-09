package company.whitespace.smartifyandroid.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IRSetupActivity extends AppCompatActivity {
    TextView infoTest, buttonIndicator;
    SeekBar progress;
    Button complete;

    String currentButton = "0";

    final static String[] remoteButtons = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "ON"};

    private class Test implements Runnable {
        Handler handler;

        public Test(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            sendToServer("infraSetup", "IR_Start", currentButton);

            handler.postDelayed(this, 100);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ir_setup);

        infoTest = (TextView) findViewById(R.id.info_test);
        buttonIndicator = (TextView) findViewById(R.id.button_indicator);
        progress = (SeekBar) findViewById(R.id.progress);
        complete = (Button) findViewById(R.id.compelete);

        buttonIndicator.setText(this.currentButton);

        complete.setVisibility(View.INVISIBLE);

        sendToServer("infraSetup", "IR_Start", currentButton);

        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Handler handler = new Handler();

        Test test = new Test(handler);

        handler.postDelayed(test, 2500);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void updateStatus(final String currentButton, final int currentButtonStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("UPDATE", String.valueOf(currentButtonStatus));
                if (currentButtonStatus + 1 < 0) {
                    progress.setProgress(0);
                } else if (currentButtonStatus + 1 >= 3) {
                    progress.setProgress(0);
                    if (currentButton.equals("ON")) {
                        sendToServer("infraSetup", "IR_Complete", currentButton);
                        IRSetupActivity.super.onBackPressed();
                    } else {
                        IRSetupActivity.this.currentButton = remoteButtons[Integer.parseInt(currentButton) + 1];
                        buttonIndicator.setText(IRSetupActivity.this.currentButton);
                    }

                } else {
                    progress.setProgress(currentButtonStatus + 1);
                }
            }
        });
    }

    public void sendToServer(String header, String action, String currentButton) {
        JSONObject data = new JSONObject();

        try {
            data.put("Action", action);
            data.put("Button", currentButton);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("Header", header);
            obj.put("Data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray message = new JSONArray();
        message.put(obj);

        Pair<String, String>[] pairs = new Pair[1];
        pairs[0] = new Pair<>("message", message.toString());
        new GetUpdatesAsyncTask(IRSetupActivity.this).execute(pairs);

    }
}
