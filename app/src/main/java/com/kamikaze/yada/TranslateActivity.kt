package com.kamikaze.yada

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslateActivity : AppCompatActivity() {
    var inputLangs = arrayOf<String?>("Detect language", "Afrikaans", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan", "Czech", "Welsh", "Danish", "German", "Greek", "English", "Esperanto", "Spanish", "Estonian", "Persian", "Finnish", "French", "Irish", "Galician", "Gujarati", "Hebrew", "Hindi", "Croatian", "Haitian", "Hungarian", "Indonesian", "Icelandic", "Italian", "Japanese", "Georgian", "Kannada", "Korean", "Lithuanian", "Latvian", "Macedonian", "Marathi", "Malay", "Maltese", "Dutch", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian", "Slovak", "Slovenian", "Albanian", "Swedish", "Swahili", "Tamil", "Telugu", "Thai", "Tagalog", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Chinese")
    var outputLangs = arrayOf<String?>("Afrikaans", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan", "Czech", "Welsh", "Danish", "German", "Greek", "English", "Esperanto", "Spanish", "Estonian", "Persian", "Finnish", "French", "Irish", "Galician", "Gujarati", "Hebrew", "Hindi", "Croatian", "Haitian", "Hungarian", "Indonesian", "Icelandic", "Italian", "Japanese", "Georgian", "Kannada", "Korean", "Lithuanian", "Latvian", "Macedonian", "Marathi", "Malay", "Maltese", "Dutch", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian", "Slovak", "Slovenian", "Albanian", "Swedish", "Swahili", "Tamil", "Telugu", "Thai", "Tagalog", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Chinese")
    var langCodes = arrayOf(TranslateLanguage.AFRIKAANS, TranslateLanguage.ARABIC, TranslateLanguage.BELARUSIAN, TranslateLanguage.BULGARIAN, TranslateLanguage.BENGALI, TranslateLanguage.CATALAN, TranslateLanguage.CZECH, TranslateLanguage.WELSH, TranslateLanguage.DANISH, TranslateLanguage.GERMAN, TranslateLanguage.GREEK, TranslateLanguage.ENGLISH, TranslateLanguage.ESPERANTO, TranslateLanguage.SPANISH, TranslateLanguage.ESTONIAN, TranslateLanguage.PERSIAN, TranslateLanguage.FINNISH, TranslateLanguage.FRENCH, TranslateLanguage.IRISH, TranslateLanguage.GALICIAN, TranslateLanguage.GUJARATI, TranslateLanguage.HEBREW, TranslateLanguage.HINDI, TranslateLanguage.CROATIAN, TranslateLanguage.HAITIAN_CREOLE, TranslateLanguage.HUNGARIAN, TranslateLanguage.INDONESIAN, TranslateLanguage.ICELANDIC, TranslateLanguage.ITALIAN, TranslateLanguage.JAPANESE, TranslateLanguage.GEORGIAN, TranslateLanguage.KANNADA, TranslateLanguage.KOREAN, TranslateLanguage.LITHUANIAN, TranslateLanguage.LATVIAN, TranslateLanguage.MACEDONIAN, TranslateLanguage.MARATHI, TranslateLanguage.MALAY, TranslateLanguage.MALTESE, TranslateLanguage.DUTCH, TranslateLanguage.NORWEGIAN, TranslateLanguage.POLISH, TranslateLanguage.PORTUGUESE, TranslateLanguage.ROMANIAN, TranslateLanguage.RUSSIAN, TranslateLanguage.SLOVAK, TranslateLanguage.SLOVENIAN, TranslateLanguage.ALBANIAN, TranslateLanguage.SWEDISH, TranslateLanguage.SWAHILI, TranslateLanguage.TAMIL, TranslateLanguage.TELUGU, TranslateLanguage.THAI, TranslateLanguage.TAGALOG, TranslateLanguage.TURKISH, TranslateLanguage.UKRAINIAN, TranslateLanguage.URDU, TranslateLanguage.VIETNAMESE, TranslateLanguage.CHINESE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        val translateButton = findViewById<View>(R.id.translate_button) as Button
        val inputText = findViewById<View>(R.id.input_lang_text) as EditText
        val outputText = findViewById<View>(R.id.output_lang_text) as TextView
        val inputLang = findViewById<View>(R.id.input_langs) as Spinner
        val outputLang = findViewById<View>(R.id.output_langs) as Spinner
        var adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.spinner_item, inputLangs)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        inputLang.adapter = adapter
        adapter = ArrayAdapter<Any?>(this, R.layout.spinner_item, outputLangs)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        outputLang.adapter = adapter
        outputLang.setSelection(11)
        val swapButton = findViewById<View>(R.id.swap_button) as ImageButton
        swapButton.setOnClickListener {
            if (inputLang.selectedItemPosition == 0) {
                Toast.makeText(this@TranslateActivity, "Please select a particular language", Toast.LENGTH_SHORT).show()
            } else {
                val input = inputLang.selectedItemPosition
                val output = outputLang.selectedItemPosition
                inputLang.setSelection(output + 1)
                outputLang.setSelection(input - 1)
            }
        }
        translateButton.setOnClickListener { view: View? ->
            when (inputLang.selectedItemPosition) {
                0 -> {
                    val options1 = LanguageIdentificationOptions.Builder().setConfidenceThreshold(0.5f).build()
                    val languageIdentifier = LanguageIdentification.getClient(options1)
                    languageIdentifier.identifyPossibleLanguages(inputText.text.toString()).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var found = false
                            var ii = 0
                            while (ii < task.result.size) {
                                for (langCode in langCodes) {
                                    if (task.result[ii].languageTag == langCode) {
                                        found = true
                                        break
                                    }
                                }
                                if (found) break
                                ii++
                            }
                            if (!found) {
                                Toast.makeText(this@TranslateActivity, "No language found", Toast.LENGTH_SHORT).show()
                                return@addOnCompleteListener
                            }
                            val options = TranslatorOptions.Builder().setSourceLanguage(task.result[0].languageTag).setTargetLanguage(langCodes[outputLang.selectedItemPosition]).build()
                            val translator = Translation.getClient(options)
                            val conditions = DownloadConditions.Builder().requireWifi().build()
                            val progressDialog = ProgressDialog(this@TranslateActivity)
                            progressDialog.setTitle("Downloading required models")
                            progressDialog.isIndeterminate = true
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                            progressDialog.setCanceledOnTouchOutside(false)
                            progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
                            progressDialog.setProgressNumberFormat(null)
                            progressDialog.setProgressPercentFormat(null)
                            progressDialog.show()
                            translator.downloadModelIfNeeded(conditions).addOnCompleteListener { task ->
                                progressDialog.cancel()
                                if (task.isSuccessful) {
                                    translator.translate(inputText.text.toString()).addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val translatedText = task.result
                                            outputText.text = translatedText
                                        }
                                    }
                                } else Toast.makeText(this@TranslateActivity, "Failed", Toast.LENGTH_SHORT).show()
                            }
                        } else Toast.makeText(this@TranslateActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {
                    val options = TranslatorOptions.Builder().setSourceLanguage(langCodes[inputLang.selectedItemPosition - 1]).setTargetLanguage(langCodes[outputLang.selectedItemPosition]).build()
                    val translator = Translation.getClient(options)
                    val conditions = DownloadConditions.Builder().requireWifi().build()
                    val progressDialog = ProgressDialog(this@TranslateActivity)
                    progressDialog.setTitle("Downloading required models")
                    progressDialog.isIndeterminate = true
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
                    progressDialog.setProgressNumberFormat(null)
                    progressDialog.setProgressPercentFormat(null)
                    progressDialog.show()
                    translator.downloadModelIfNeeded(conditions).addOnCompleteListener { task ->
                        progressDialog.cancel()
                        if (task.isSuccessful) {
                            translator.translate(inputText.text.toString()).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val translatedText = task.result
                                    outputText.text = translatedText
                                }
                            }
                        } else Toast.makeText(this@TranslateActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}