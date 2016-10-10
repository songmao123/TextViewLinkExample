package com.example.TextViewLinkExample.util;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;

import org.xml.sax.XMLReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 青松 on 2016/10/10.
 */

public class TagHelper implements Html.TagHandler {

    public static String TAG_USER = "user";
    public static int KEY_TAG_START = 1;

    private Context context;
    private Map<Integer, Integer> map = new HashMap<>();
    private BasicClickableSpan basicClickableSpan;

    public TagHelper(Context context) {
        this.context = context;
        this.basicClickableSpan = new BasicClickableSpan(context, "user");
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (!tag.equalsIgnoreCase(TAG_USER)) {
            return;
        }
        if (opening) {
            int start = output.length();
            if (tag.equalsIgnoreCase(TAG_USER)) {
                map.put(KEY_TAG_START, start);
            }
        } else {
            int end = output.length();
            if (tag.equalsIgnoreCase(TAG_USER)) {
                int start = map.get(KEY_TAG_START);
                output.setSpan(basicClickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
