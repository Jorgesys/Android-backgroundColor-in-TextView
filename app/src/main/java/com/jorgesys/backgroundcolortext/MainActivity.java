package com.jorgesys.backgroundcolortext;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String message =  "<font color='#FF0000'>Author <b>Mihai Eminescu</b></font><br>" +
                "Por la noche, perezoso y cárdeno, arde el <font color='#FF0000'>fuego</font> en la chimenea;\n" +
                "desde <u>un rincón en un sofá</u>  <font color='#FF0000'>rojo</font> yo lo miro de frente," +
                "<b>hasta que mi mente se duerme, hasta que mis pestañas se bajan</b>;" +
                "la vela está apagada en la casa... <i>el sueño es</i> <font color='#0000FF'>cálido</font>, lento, suave.<br>" +
                "and again <span class=\"highlighter\">Jorgesys</span><hr>" +
                "was here with the company of <span class=\"highlighter\">Elenasys</span> hehehe";

        String messageNoHtml =  "Author Mihai Eminescu\n" +
                "Por la noche, perezoso y cárdeno, arde el fuego en la chimenea;\n" +
                "desde un rincón en un sofá rojo yo lo miro de frente," +
                "hasta que mi mente se duerme, hasta que mis pestañas se bajan;" +
                "la vela está apagada en la casa... el sueño es cálido, lento, suave." +
                "and again Jorge\n" +
                "Jorgesys was here and Jorge hehehe";

        //First option using Htl.fromHtml()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((TextView) findViewById(R.id.myTextView)).setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        }else{
            ((TextView) findViewById(R.id.myTextView)).setText(Html.fromHtml(message));
        }

        //Second option using Spannable Text()
        ((TextView)findViewById(R.id.myTextView2)).setText( setBackgroundColor("noche", messageNoHtml, 0));

        //My option using the span highlighter to set a background color:
        HtmlStyler styler = new HtmlStyler(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((TextView) findViewById(R.id.myTextView)).setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        }else{
            ((TextView) findViewById(R.id.myTextView3)).setText(Html.fromHtml(message, null, styler));
        }

    }


    public static Spannable setBackgroundColor(String word, String message, int color) {
        if(color==0) {
            color = 0xFFFFFF42; //default color yellow.
        }
        Spannable raw=new SpannableString(message);
        BackgroundColorSpan[] spans=raw.getSpans(0,
                raw.length(),
                BackgroundColorSpan.class);

        for (BackgroundColorSpan span : spans) {
            raw.removeSpan(span);
        }

        int index= TextUtils.indexOf(raw, word);
        while (index >= 0) {
            raw.setSpan(new BackgroundColorSpan(color), index, index
                    + word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            index=TextUtils.indexOf(raw, word, index + word.length());
        }
        return raw;
    }



}
