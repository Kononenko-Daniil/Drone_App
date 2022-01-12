package com.dji.importSDKDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FeaturesActivity extends AppCompatActivity {
    private String[] features;
    private TextView featuresText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        setFeaturesText();
    }

    public void setFeaturesText(){
        features = new String[]{
                "Сделать онлайн сервис для добавления, удаления, изменения имеющихся зон ограничений полетов.",
                "Получить файл со всеми зонами.",
                "Добавить просмотр положения других пользователей для избежания возможных столкновений.",
                "Добавить страницу с важной информацией о полетах."
        };
        featuresText = (TextView) findViewById(R.id.features);
        featuresText.setText("");
        for(int i = 0; i < features.length; i++){
            int featureNumber = i + 1;
            featuresText.append(featureNumber + ". " + features[i] + "\n");
        }
    }

    public void onBackClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}