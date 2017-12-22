package ca.bart.travailpratique1;

import android.os.Bundle;

import android.app.Activity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.bart.travailpratique1.views.Grid;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = (TextView) findViewById(R.id.textView);
        GameEngine.getInstance().SetMineText(text);
        Button nouvPartie = (Button) findViewById(R.id.button);
        final MainActivity act = this;
        nouvPartie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameEngine.getInstance().Reset();
            }
        });

    }
}
