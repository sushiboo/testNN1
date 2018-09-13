package com.project.testnn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.testnn.models.Classifier;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MODEL_FILE = "frozen_model.pb";
    private static final String INPUT_NODE = "input/X";
    private static final String OUTPUT_NODE = "FullyConnected/Softmax";
    private static final String[] OUTPUT_NODES = {"FullyConnected/Softmax"};
    private static final int[] INPUT_SIZE = {1, 5000};

    private TensorFlowInferenceInterface TFInference;
    private List<Classifier> mClassifiers = new ArrayList<>();

    private TextView tvResult;
    private EditText txtDescription;
    private Button btnPredict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        //loadModel();
        enablePredict();
    }

    private void bindView(){
        tvResult = (TextView)findViewById(R.id.tvResult);
        txtDescription = (EditText)findViewById(R.id.txtDescription);
        btnPredict = (Button)findViewById(R.id.btnPredict);
    }

    private void enablePredict(){
        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = txtDescription.getText().toString();
                Helper helper = Helper.initialize(getAssets());
             //   Helper2 helper2 = Helper2.initialize(getAssets());
                float[] input = helper.getInput(text);

              //  Log.e("input", Float.toString(input[0]));

                if (input.length != 0) {
                    tvResult.setText("Success");
                }

                classify(input);
                //classify2(input);

            }
        });
    }

    private void classify(float[] input){
        TFInference = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);

        TFInference.feed(INPUT_NODE, input, 1, input.length);
        TFInference.run(OUTPUT_NODES);
        float[] resu = new float[2];
        TFInference.fetch(OUTPUT_NODE, resu);
        tvResult.setText("Programmer: " + Float.toString(resu[0]) + "\n Construction" +  Float.toString(resu[1]));
        Log.e("Result: ", Float.toString(resu[0]));
    }

    private void classify2(float[] input){
        TFInference = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);

        TFInference.feed(INPUT_NODE, input, 1, input.length);
        TFInference.run(OUTPUT_NODES);
        float[] resu = new float[2];
        TFInference.fetch(OUTPUT_NODE, resu);
        tvResult.setText("Programmer: " + Float.toString(resu[0]) + "\n Construction" +  Float.toString(resu[1]));
        Log.e("Result: ", Float.toString(resu[0]));
    }
}


