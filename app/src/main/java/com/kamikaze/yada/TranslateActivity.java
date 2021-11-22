package com.kamikaze.yada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Button translateButton=(Button) findViewById(R.id.translate_button);
        EditText inputText=(EditText) findViewById(R.id.input_lang_text);
        TextView outputText=(TextView) findViewById(R.id.output_lang_text);
        TranslatorOptions options=new TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH).setTargetLanguage(TranslateLanguage.HINDI).build();
        Translator translator= Translation.getClient(options);
        DownloadConditions conditions=new DownloadConditions.Builder().requireWifi().build();
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translator.downloadModelIfNeeded(conditions).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            translator.translate(inputText.getText().toString()).addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    if(task.isSuccessful())
                                    {
                                        String translatedText=task.getResult();
                                        outputText.setText(translatedText);
                                        Toast.makeText(getApplicationContext(), translatedText, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}