package com.kamikaze.yada;

import static com.google.mlkit.nl.translate.TranslateLanguage.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.IdentifiedLanguage;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.List;

public class TranslateActivity extends AppCompatActivity {
    String[] inputLangs={"Detect language","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Danish","German","Greek","English","Esperanto","Spanish","Estonian","Persian","Finnish","French","Irish","Galician","Gujarati","Hebrew","Hindi","Croatian","Haitian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukrainian","Urdu","Vietnamese","Chinese"};
    String[] outputLangs={"Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Danish","German","Greek","English","Esperanto","Spanish","Estonian","Persian","Finnish","French","Irish","Galician","Gujarati","Hebrew","Hindi","Croatian","Haitian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Georgian","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Dutch","Norwegian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Albanian","Swedish","Swahili","Tamil","Telugu","Thai","Tagalog","Turkish","Ukrainian","Urdu","Vietnamese","Chinese"};
    String[] langCodes={AFRIKAANS,ARABIC,BELARUSIAN,BULGARIAN,BENGALI,CATALAN,CZECH,WELSH,DANISH,GERMAN,GREEK,ENGLISH,ESPERANTO,SPANISH,ESTONIAN,PERSIAN,FINNISH,FRENCH,IRISH,GALICIAN,GUJARATI,HEBREW,HINDI,CROATIAN,HAITIAN_CREOLE,HUNGARIAN,INDONESIAN,ICELANDIC,ITALIAN,JAPANESE,GEORGIAN,KANNADA,KOREAN,LITHUANIAN,LATVIAN,MACEDONIAN,MARATHI,MALAY,MALTESE,DUTCH,NORWEGIAN,POLISH,PORTUGUESE,ROMANIAN,RUSSIAN,SLOVAK,SLOVENIAN,ALBANIAN,SWEDISH,SWAHILI,TAMIL,TELUGU,THAI,TAGALOG,TURKISH,UKRAINIAN,URDU,VIETNAMESE,CHINESE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Button translateButton=(Button) findViewById(R.id.translate_button);
        EditText inputText=(EditText) findViewById(R.id.input_lang_text);
        TextView outputText=(TextView) findViewById(R.id.output_lang_text);
        Spinner inputLang=(Spinner) findViewById(R.id.input_langs);
        Spinner outputLang=(Spinner) findViewById(R.id.output_langs);
        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.spinner_item,inputLangs);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        inputLang.setAdapter(adapter);
        adapter=new ArrayAdapter(this,R.layout.spinner_item,outputLangs);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        outputLang.setAdapter(adapter);
        outputLang.setSelection(11);
        ImageButton swapButton=(ImageButton) findViewById(R.id.swap_button);
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputLang.getSelectedItemPosition()==0)
                {
                    Toast.makeText(TranslateActivity.this, "Please select a particular language", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int input=inputLang.getSelectedItemPosition();
                    int output=outputLang.getSelectedItemPosition();
                    inputLang.setSelection(output+1);
                    outputLang.setSelection(input-1);
                }
            }
        });
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(TranslateActivity.this, inputLang.getSelectedItemPosition()+" "+outputLang.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();
                switch(inputLang.getSelectedItemPosition())
                {
                    case 0:
//                        Toast.makeText(TranslateActivity.this, "Hello vai", Toast.LENGTH_SHORT).show();
                        LanguageIdentificationOptions options1=new LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.5f).build();
                        LanguageIdentifier languageIdentifier= LanguageIdentification.getClient(options1);

                        languageIdentifier.identifyPossibleLanguages(inputText.getText().toString()).addOnCompleteListener(new OnCompleteListener<List<IdentifiedLanguage>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<IdentifiedLanguage>> task) {
                                if(task.isSuccessful())
                                {
                                    int i=0;
                                    boolean found =false;
                                    for(int ii=0;ii<task.getResult().size();ii++)
                                    {
                                        for(int j=0;j<langCodes.length;j++)
                                        {
                                            Log.d(task.getResult().get(ii).getLanguageTag(),langCodes[j]);
                                            if(task.getResult().get(ii).getLanguageTag().equals(langCodes[j]))
                                            {
                                                i=ii;
                                                found=true;
                                                break;
                                            }
                                        }
                                        if(found) break;
                                        Log.d("sadge",task.getResult().get(i).getLanguageTag());
                                    }
                                    if(!found)
                                    {
                                        Toast.makeText(TranslateActivity.this, "No language found", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    TranslatorOptions options=new TranslatorOptions.Builder().setSourceLanguage(task.getResult().get(0).getLanguageTag()).setTargetLanguage(langCodes[outputLang.getSelectedItemPosition()]).build();
                                    Translator translator= Translation.getClient(options);
                                    DownloadConditions conditions=new DownloadConditions.Builder().requireWifi().build();
                                    ProgressDialog progressDialog=new ProgressDialog(TranslateActivity.this);
                                    progressDialog.setTitle("Downloading required models");
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
                                    progressDialog.setProgressNumberFormat(null);
                                    progressDialog.setProgressPercentFormat(null);
                                    progressDialog.show();
                                    translator.downloadModelIfNeeded(conditions).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.cancel();
                                            if(task.isSuccessful())
                                            {
                                                translator.translate(inputText.getText().toString()).addOnCompleteListener(new OnCompleteListener<String>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<String> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            String translatedText=task.getResult();
                                                            outputText.setText(translatedText);
                                                        }
                                                    }
                                                });
                                            }
                                            else Toast.makeText(TranslateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else Toast.makeText(TranslateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    default:
                        TranslatorOptions options=new TranslatorOptions.Builder().setSourceLanguage(langCodes[inputLang.getSelectedItemPosition()-1]).setTargetLanguage(langCodes[outputLang.getSelectedItemPosition()]).build();
                        Translator translator= Translation.getClient(options);
                        DownloadConditions conditions=new DownloadConditions.Builder().requireWifi().build();
                        ProgressDialog progressDialog=new ProgressDialog(TranslateActivity.this);
                        progressDialog.setTitle("Downloading required models");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
                        progressDialog.setProgressNumberFormat(null);
                        progressDialog.setProgressPercentFormat(null);
                        progressDialog.show();
                        translator.downloadModelIfNeeded(conditions).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.cancel();
                                if(task.isSuccessful())
                                {
                                    translator.translate(inputText.getText().toString()).addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if(task.isSuccessful())
                                            {
                                                String translatedText=task.getResult();
                                                outputText.setText(translatedText);
                                            }
                                        }
                                    });
                                }
                                else Toast.makeText(TranslateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                }


            }
        });
    }
}