package com.project.testnn;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper {

    private static Map<String,float[]> vocabMap = null;
    private static int maxLength = 0;
    private final int batchSize = 1;


    public static Helper initialize(AssetManager assetManager){
        Helper helper = new Helper();

        String vocabJson = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("job_desc_vocab.json")));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            vocabJson = sb.toString();
            br.close();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<Map<String, float[]>>(){}.getType();
            vocabMap = gson.fromJson(vocabJson, type);
            Log.e("Initializing", "Finished initialize");
        }
        catch(Exception e){
            Log.e("Helper Error",e.toString());
        }

        return helper;
    }

    public float[] getInput(String text){
        Log.e("text", text);

        float[] realInput = buildInput(TextPreprocess.preprocess(text));
        return realInput;
    }

    private float[] buildInput(List<String> words){
        maxLength = words.size();
        List<float[]> allInput = new ArrayList<float[]>();
        float[] input = new float[batchSize * maxLength];
            if (vocabMap == null) {
                Log.e("Error", "Empty Vocab");
            } else {
                int i = 0;
                for (String word : words) {
                    if (vocabMap.get(word) != null) {
                        DecimalFormat df = new DecimalFormat("#.0000");
                        float[] index = vocabMap.get(word);
                        for (int ii = 0; ii < index.length; ii++){
                            index[ii] = Float.valueOf(df.format(index[ii]));
                        }
                        allInput.add(index);
                        Log.e("i", Float.toString(input[i]));

                        for (float f : index){
                            Log.e("index", Float.toString(f));
                        }

                        i++;
                        if (i == maxLength)
                            break;
                    }

                }
            }
            for (float[] f : allInput){
                input = ArrayUtils.addAll(input, f);
            }
        return input;
    }
}
