package com.example.TextViewLinkExample.util;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.example.TextViewLinkExample.MainActivity;
import com.example.TextViewLinkExample.R;
import com.example.TextViewLinkExample.model.Topic;
import com.example.TextViewLinkExample.model.User;

/**
 * Created by 青松 on 2016/10/10.
 */
public class BasicClickableSpan extends ClickableSpan {

    private Context context;
    private String url;
    private boolean pressed;

    public BasicClickableSpan(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void onClick(View widget) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String text = "";
        if (url.startsWith("http")) {
            text = "Website: " + url;
        }
        if (url.startsWith("tel")) {
            text = "Telephone Number: " + url.substring(4);
        }
        if (url.startsWith("mailto")) {
            text = "Email Address: " + url.substring(7);
        }
        if (url.startsWith("mention")) {
            text = url.substring(9);
        }
        if (url.startsWith("topic")) {
            Object tag = widget.getTag(MainActivity.TAG_TOPIC);
            if (tag != null && tag instanceof Topic) {
                Topic topic = (Topic) tag;
                text = "Topic: id->" + topic.getTopicId() + ", name->" + topic.getTopicName();
            } else {
                text = "Topic: " + url.substring(7);
            }
        }
        if (url.startsWith("user")) {
            Object tag = widget.getTag(MainActivity.TAG_USER);
            if (tag != null && tag instanceof User) {
                User user = (User) tag;
                text = "User: id->" + user.getId() + ", name->" + user.getName();
            } else {
                text = "Something was wrong!";
            }
        }

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

//        Uri uri = Uri.parse(url);
//        Context context = widget.getContext();
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
//        try {
//            context.startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Log.w("BasicClickableSpan", "Activity was not found for intent, " + intent.toString());
//        }
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(getTextColor(context, url));
        ds.bgColor = getBackgroundColor(context, url, pressed);
    }

    private int getBackgroundColor(Context context, String url, boolean pressed) {
        int color;
        if (pressed) {
            if (!TextUtils.isEmpty(url) && url.startsWith("topic")) {
                color = R.color.main_color;
            } else {
                color = R.color.color_dfdfdf;
            }
        } else {
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("topic")) {
                    color = R.color.main_color;
                } else {
                    color = R.color.color_fafafa;
                }
            } else {
                color = R.color.color_eeeeee;
            }
        }
        return context.getResources().getColor(color);
    }

    private int getTextColor(Context context, String url) {
        int color;
        if (!TextUtils.isEmpty(url) && url.startsWith("topic")) {
            color = R.color.color_white;
        } else {
            color = R.color.color_586B94;
        }
        return context.getResources().getColor(color);
    }
}
