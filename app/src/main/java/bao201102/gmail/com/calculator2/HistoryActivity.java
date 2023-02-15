package bao201102.gmail.com.calculator2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    TextView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        history = findViewById(R.id.history);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            ArrayList<String> result = extras.getStringArrayList("result");
            ArrayList<String> solution = extras.getStringArrayList("solution");

            String his = "";
            for (int i = result.size() - 1; i >= 0; i--) {
                his = his + solution.get(i) + " = " + result.get(i) + "\n\n";
            }

            history.setText(his);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (history.getText().toString()!=null)
            outState.putString("result", history.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get("result")!=null)
            history.setText(savedInstanceState.get("result").toString());
    }

}