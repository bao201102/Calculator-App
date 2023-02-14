package bao201102.gmail.com.calculator2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result,solution;
    Button btn_history;
    MaterialButton btn_clear,btn_open,btn_close,btn_allclear,btn_dot;
    MaterialButton btn_add,btn_subtract,btn_multiply,btn_divide,btn_equal;
    MaterialButton btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9;
    List<String> resultList = new ArrayList<String>();
    List<String> solutiontList = new ArrayList<String>();

    String[] resultArray, solutionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        solution = findViewById(R.id.solution);

        assignId(btn_0,R.id.btn_0);
        assignId(btn_1,R.id.btn_1);
        assignId(btn_2,R.id.btn_2);
        assignId(btn_3,R.id.btn_3);
        assignId(btn_4,R.id.btn_4);
        assignId(btn_5,R.id.btn_5);
        assignId(btn_6,R.id.btn_6);
        assignId(btn_7,R.id.btn_7);
        assignId(btn_8,R.id.btn_8);
        assignId(btn_9,R.id.btn_9);
        assignId(btn_clear,R.id.btn_clear);
        assignId(btn_open,R.id.btn_open);
        assignId(btn_close,R.id.btn_close);
        assignId(btn_allclear,R.id.btn_allclear);
        assignId(btn_dot,R.id.btn_dot);
        assignId(btn_add,R.id.btn_add);
        assignId(btn_subtract,R.id.btn_subtract);
        assignId(btn_multiply,R.id.btn_multiply);
        assignId(btn_divide,R.id.btn_divide);
        assignId(btn_equal,R.id.btn_equal);

        btn_history = findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), HistoryActivity.class);
                i.putExtra("result", resultArray);
                i.putExtra("solution", solutionArray);
                startActivity(i);
            }
        });
    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (result.getText().toString()!=null)
            outState.putString("result", result.getText().toString());
        if (solution.getText().toString()!=null)
            outState.putString("solution", solution.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.get("result")!=null)
            result.setText(savedInstanceState.get("result").toString());
        if (savedInstanceState.get("solution")!=null)
            solution.setText(savedInstanceState.get("solution").toString());
    }

    void assignId(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataCalculate = solution.getText().toString();

        if (buttonText.equals("C")){
            solution.setText("");
            result.setText("0");
            return;
        }

        if (buttonText.equals("=")){
            double val = eval(dataCalculate);
            result.setText(String.valueOf(val));
            resultList.add(String.valueOf(val));
            solutiontList.add(solution.getText().toString());

            resultArray = resultList.toArray(new String[0]);
            solutionArray = solutiontList.toArray(new String[0]);
            return;
        }

        if (buttonText.equals("DEL")){
            dataCalculate = dataCalculate.substring(0,dataCalculate.length()-1);
        }else {
            dataCalculate = dataCalculate + buttonText;
        }

        solution.setText(dataCalculate);
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
//                } else if (ch >= 'a' && ch <= 'z') { // functions
//                    while (ch >= 'a' && ch <= 'z') nextChar();
//                    String func = str.substring(startPos, this.pos);
//                    x = parseFactor();
//                    if (func.equals("sqrt")) x = Math.sqrt(x);
//                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
//                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
//                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
//                    else if (func.equals("log")) x = Math.log10(x);
//                    else if (func.equals("ln")) x = Math.log(x);
//                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

//                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}