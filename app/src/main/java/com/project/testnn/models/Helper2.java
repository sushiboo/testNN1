package com.project.testnn.models;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.testnn.TextPreprocess;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helper2 {

    private static Map<String,Integer> vocabMap = null;
    private static int maxLength = 0;
    private final int batchSize = 1;


    public static Helper2 initialize(AssetManager assetManager){
        Helper2 helper = new Helper2();

        String vocabJson = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open("test_dict.json")));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            vocabJson = sb.toString();
            br.close();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<Map<String, Integer>>(){}.getType();
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

        int[] temp = buildInput(TextPreprocess.preprocess(text));
        float[] realInput = new float[]{};
        int i = 0;
        for (int i1 : temp){
            float f = (float)i1;
            realInput[i] = f;
            i++;

            if (i > temp.length){
                break;
            }
        }
        return realInput;
    }

    private int[] buildInput(List<String> words){
        maxLength = words.size();
        List<float[]> allInput = new ArrayList<float[]>();
        int[] input = new int[batchSize * maxLength];
            if (vocabMap == null) {
                Log.e("Error", "Empty Vocab");
            } else {
                int i = 0;
                for (String word : words) {
                    if (vocabMap.get(word) != null) {
                        int index = vocabMap.get(word);
                        Log.e("i", Float.toString(input[i]));
                        input[i] = index;
                        i++;
                        if (i == maxLength)
                            break;
                    }

                }
            }
        return input;
    }
}
