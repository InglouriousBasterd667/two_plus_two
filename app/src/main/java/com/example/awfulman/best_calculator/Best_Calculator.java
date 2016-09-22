package com.example.awfulman.best_calculator;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AwfulMan on 22.09.16.
 */

public class Best_Calculator extends Application{
    public List<String> equations_list;
    public String str_expression;
    public String result_str;
    public boolean last_num;
    public boolean has_dot;

    public Best_Calculator(){
        super();
        equations_list = new ArrayList<>();
        str_expression = "0";
        result_str = "";
        last_num = false;
        has_dot = false;
    }
}
