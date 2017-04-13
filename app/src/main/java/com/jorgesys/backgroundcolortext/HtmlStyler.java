package com.jorgesys.backgroundcolortext;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;

public class HtmlStyler  implements Html.TagHandler {

    private static final String TAG = "Articulo", SPAN = "span", CLASS = "class", HIGHLIGHTER = "highlighter",
            THENEWELEMENT = "theNewElement", THEATTS = "theAtts", DATA = "data", LENGTH = "length";
    private Context mContext;
    private boolean processEndSpan;
    private int highlightColor, width;

    public HtmlStyler(Context ctx) {
        mContext = ctx;
        width = getScreenWidth(ctx);
        highlightColor = ContextCompat.getColor(ctx, R.color.higlighter);
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if(SPAN.equalsIgnoreCase(tag) && (processAttributes(xmlReader) || processEndSpan)){
            processSpan(opening, output);
        }
    }

    private boolean processAttributes(final XMLReader xmlReader) {
        boolean response = false;
        try {
            Field elementField = xmlReader.getClass().getDeclaredField(THENEWELEMENT);
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField(THEATTS);
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField(DATA);
            dataField.setAccessible(true);
            String[] data = (String[])dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField(LENGTH);
            lengthField.setAccessible(true);
            int len = (Integer)lengthField.get(atts);
            for(int i = 0; i < len; i++){
                if(CLASS.equals(data[i * 5 + 1]) && HIGHLIGHTER.equals(data[i * 5 + 4])){
                    processEndSpan = true;
                    response = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "processAttributes() " + e.getMessage());
        }
        return response;
    }


    private void processSpan(boolean opening, Editable output) {
        int len = output.length();
        if(opening){
            output.setSpan(new BackgroundColorSpan(highlightColor), len, len, Spannable.SPAN_MARK_MARK);
        }else{
            Object obj = getLast(output);
            int where = output.getSpanStart(obj);
            output.removeSpan(obj);
            if(where != len){
                output.setSpan(new BackgroundColorSpan(highlightColor), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //disable highlither at the end of the span.
            processEndSpan = false;
        }
    }

    private Object getLast(Editable text) {
        Object response = null;
        BackgroundColorSpan[] objs = text.getSpans(0, text.length(), BackgroundColorSpan.class);
        for(int i = objs.length; i > 0; i--){
            if(text.getSpanFlags(objs[i - 1]) == Spannable.SPAN_MARK_MARK){
                response = objs[i - 1];
                break;
            }
        }
        return response;
    }

    public static int getScreenWidth(Context mContext){
        int width = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1){
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        }else{
            width = display.getWidth();
        }
        return width;
    }

}
