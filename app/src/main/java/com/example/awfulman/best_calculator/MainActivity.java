package com.example.awfulman.best_calculator;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awfulman.best_calculator.expressionSolver.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.R.layout.simple_list_item_1;
import static com.example.awfulman.best_calculator.expressionSolver.eval;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Some Tag" ;
    private static final int CM_DELETE_ID = 1;

    private TextView expression;
    private ListView equations;

//    List<String> equations_list = new ArrayList<>();
//    private String str_expression = "0";
//    private String result_str = "";
//    private boolean last_num = false;
//    private boolean has_dot = false;

    private Best_Calculator app;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.app = (Best_Calculator) this.getApplication();
        expression = (TextView) findViewById(R.id.expression);
        expression.setText(app.str_expression);
        expression.setMovementMethod(new ScrollingMovementMethod());
        expression.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                app.str_expression = "";
                UpdateExpression();
                return true;
            }
        });


        equations = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, equations_list);
        equations.setAdapter(adapter);
        registerForContextMenu(equations);


        Button solve = (Button) findViewById(R.id.solve);
        solve.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                onClickSolve(v);
                if (!app.result_str.equals("")) sendTextToList(app.result_str);
                return true;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Delete");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo menu_info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            app.equations_list.remove(menu_info.position);
            adapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Deleted!", Toast.LENGTH_SHORT);
            toast.show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void sendTextToList(String text){
        equations_list.add(text);
        adapter.notifyDataSetChanged();

    }

    private void appendTextFromView(View v) {
        if ( v instanceof Button ){
            Button but = (Button) v;
            if (str_expression.equals("0"))
                str_expression = (String) but.getText();
            else
                str_expression += but.getText();
            UpdateExpression();
        }
    }

    private void UpdateExpression(){
        expression.setText(str_expression);
    }

    public void onClickNumber(View v){
        last_num = true;
        appendTextFromView(v);
    }

    public void onClickOperator(View v){
        if (last_num && !str_expression.equals("0")){
            appendTextFromView(v);
            last_num = false;
            has_dot = false;
        }
    }

    public void onClickDot(View v){
        if (last_num && !has_dot && !str_expression.equals("0")){
            has_dot = true;
            Button but = (Button) v;
            str_expression += but.getText();
            UpdateExpression();
        }
    }


    private void validateString(String exp){
        int len = str_expression.length();
        if (len > 0) {
            char last_char = str_expression.charAt(len - 1);
            last_num = ('0' < last_char) &&
                    ('9' > last_char);
        }

    }
    public void onClickClear(View v){
        int len = str_expression.length();
        if ( len != 0) {
            char deleted_char = str_expression.charAt(len - 1);
            if (deleted_char == '.')
                has_dot = false;
            str_expression = str_expression.substring(0, len - 1);
            validateString(str_expression);
            UpdateExpression();
        }
    }

    public void onClickSolve(View v){
        if(last_num){
            result_str = str_expression;
            double result = eval(str_expression);
            if (result == Math.round(result))
                str_expression = Integer.toString(Double.valueOf(result).intValue());
            else
                str_expression = Double.toString(result);
            result_str += (" = " + str_expression);
            UpdateExpression();
        }

    }
}
