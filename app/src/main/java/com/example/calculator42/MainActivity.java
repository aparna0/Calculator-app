package com.example.calculator42;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    String pre; //to get previous expression when backspace button is used to delete current input text
    Stack<Float> values = new Stack<Float>(); //to hold values from given expression
    Stack<Character> ops = new Stack<Character>(); //to hold operators from given expression
    TextView input;// reference of input text field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setviewId();
    }
    void setviewId(){
        input=(TextView)findViewById(R.id.input);
    }

    //when number or operator button is clicked , append that number or operator to given expression
    public void getInput(View v){
        String str = ((Button)v).getText().toString();
        pre = input.getText().toString();
        str =  pre + str ;
        input.setText(str);
    }

    //when AC button is clicked, clear all expression or result from textView
    public void allClear(View v){
        input.setText(" ");
    }

    //when Del button is clicked, delete last input like backspace
    public void backspace(View v){
        input.setText(pre);
    }

    //when equal button is clicked, calculate result for given expression
    //using expression exalation with stack
    public void calculate(View v){
        String expression = input.getText().toString();
        char[] exp = expression.toCharArray();
        int i = 0;
        while(i < exp.length){

            if(exp[i] == ' ') {                          //if input is nothing
                i++;
                continue;
            }

            else if(exp[i]>='0' && exp[i]<='9'){         //if input is number, push number to values stack
                StringBuffer val = new  StringBuffer();
                while(i < exp.length && ( (exp[i] >= '0' && exp[i] <= '9') || exp[i] == '.')) {
                    val.append(exp[i++]);
                }
                String val2 = val.toString();
                Float pval = Float.parseFloat(val2);
                values.push(pval);
                i--;
            }

            else {                                       //if input is operator, push operator to operator stack
                System.out.println("it is operator");
                if(ops.empty()) {
                    ops.push(exp[i]);
                }
                else {
                    char preop;
                    preop  = ops.pop();
                    //to execute higher priority operator first then
                    while( priority(preop) >= priority(exp[i]) ) {
                        // if priority of previous operator is greater or equal to current operator then
                        //pop previous operator and get result of that operator and push into values stack
                        values.push(getResult(preop));
                        //if no operator is left in operator stack to evaluate then break;
                        if(ops.empty())  break;
                        preop  = ops.pop();
                    }
                    // if priority of previous operator is small than current operator then
                    //push operators into operator stack
                    if( (priority(preop) < priority(exp[i]))  )
                        ops.push(preop);
                    ops.push(exp[i]);
                }

            }
            i++;
        }
        //get result for remaining operators in operator stack
        // and push into values stack
        while(!ops.empty()) {
            values.push(getResult(ops.pop()));
        }
        //return top value of values stack as final result
        String out = values.pop().toString();
        input.setText(out);
    }

    //to return priority according to presidence
    public int priority(char op){
        int res = 0;
        if(op == '*' || op == '/' || op == '%')
            res = 2;
        else
            res = 1;
        return res;
    }

    //to calculate result for each passed operator
    public float getResult(char op){
        float b = values.pop();
        float a = values.pop();
        float res = 0;
        switch(op) {
            case '%':
                res = a % b;
                break;
            case '/':
                res = a / b;
                break;
            case '*':
                res = a * b;
                break;
            case '+':
                res = a + b;
                break;
            case '-':
                res = a - b;
                break;
        }
        return res;
    }
}