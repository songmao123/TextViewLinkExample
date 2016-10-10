package com.example.TextViewLinkExample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TextViewLinkExample.model.Topic;
import com.example.TextViewLinkExample.model.User;
import com.example.TextViewLinkExample.util.BasicClickableSpan;
import com.example.TextViewLinkExample.util.LinkTouchMovementMethod;
import com.example.TextViewLinkExample.util.TagHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ImageView like_iv;
    private TextView content_tv;
    private TextView like_name_tv;

    private User mUser;
    private boolean isLike = false;
    private List<User> mUserList = new ArrayList<>();

    public static final Pattern PATTERN_MENTION = Pattern.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final Pattern PATTERN_TOPIC = Pattern.compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final String SCHEME_MENTION = "mention:";
    public static final String SCHEME_TOPIC = "topic:";


    public static final int TAG_TOPIC = -23351;
    public static final int TAG_USER = -45214;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatas();
        setLikeUsers();
        initEvents();
    }

    private void initEvents() {
        processUserName();
        processLinkUrl();

        like_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLike) {
                    isLike = false;
                    User u = null;
                    for (User user : mUserList) {
                        if (user.getId() == mUser.getId()) {
                            u = user;
                            break;
                        }
                    }
                    if (u != null) {
                        mUserList.remove(u);
                    }
                    like_iv.setImageResource(R.drawable.ic_dynamic_like);
                } else {
                    isLike = true;
                    mUserList.add(0, mUser);
                    like_iv.setImageResource(R.drawable.ic_dynamic_like_red);
                }
                setLikeUsers();
            }
        });
    }

    private void setLikeUsers() {
        if (mUserList != null && mUserList.size() > 0) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(getLikeIconSpan()).append(" ");
            for (int i = 0; i < mUserList.size(); i++) {
                User user = mUserList.get(i);
                builder.append(getUserNameClickableSpan(getApplicationContext(), user.getName(), i));
                if (i != mUserList.size() - 1) {
                    builder.append(", ");
                }
            }
            like_name_tv.setText(builder);
        }
    }

    private SpannableString getUserNameClickableSpan(Context context, String name, final int position) {
        SpannableString spannText = new SpannableString(name);
        BasicClickableSpan clickSpan = new BasicClickableSpan(context, null) {
            @Override
            public void onClick(View widget) {
                User user = mUserList.get(position);
                Toast.makeText(MainActivity.this, user.getName() + " & " + user.getId(), Toast.LENGTH_SHORT).show();
            }
        };
        spannText.setSpan(clickSpan, 0, spannText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannText;
    }

    /** 添加心形图标*/
    private SpannableString getLikeIconSpan() {
        SpannableString spannableString = new SpannableString(" ");
        Bitmap bitmap = zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_like), dip2px(17));
        ImageSpan imageSpan = new ImageSpan(getApplicationContext(), bitmap, DynamicDrawableSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private void initView() {
        content_tv = (TextView) findViewById(R.id.content_tv);
        like_iv = (ImageView) findViewById(R.id.like_iv);
        like_name_tv = (TextView) findViewById(R.id.like_name_tv);

        Topic topic = new Topic(1000, "欢度国庆");
        content_tv.setTag(TAG_TOPIC, topic);
        String text = "#" + topic.getTopicName() + "#" + getString(R.string.test_string);
        content_tv.setText(text);

        like_name_tv.setMovementMethod(new LinkTouchMovementMethod());
    }

    private void initDatas() {
        for (int i = 1; i < 6; i++) {
            User user = new User(i, "SQSong0" + i);
            mUserList.add(user);
        }
        mUser = new User(100, "Lily");
    }

    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public Bitmap zoomBitmap(Bitmap source, int size) {
        Matrix matrix = new Matrix();
        float scale = (float) size * 1.0F / (float) source.getWidth();
        matrix.setScale(scale, scale);
        Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return result;
    }

    private void processUserName() {
        content_tv.setTag(TAG_USER, mUser);

        String formatText = String.format("<html><%s>%s</%s>: %s</html>", TagHelper.TAG_USER, mUser.getName(),
                    TagHelper.TAG_USER, content_tv.getText().toString());
        TagHelper tagHelper = new TagHelper(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content_tv.setText(Html.fromHtml(formatText, Html.FROM_HTML_MODE_LEGACY, null, tagHelper));
        } else {
            content_tv.setText(Html.fromHtml(formatText, null, tagHelper));
        }
        content_tv.setMovementMethod(new LinkTouchMovementMethod());
    }

    private void processLinkUrl() {
        SpannableString spannabelText = (SpannableString) content_tv.getText();
        Linkify.addLinks(spannabelText, PATTERN_MENTION, SCHEME_MENTION);
        Linkify.addLinks(spannabelText, PATTERN_TOPIC, SCHEME_TOPIC);

        URLSpan[] urlSpans = spannabelText.getSpans(0, spannabelText.length(), URLSpan.class);
        for (URLSpan urlSpan : urlSpans) {
            BasicClickableSpan myUrlSpan = new BasicClickableSpan(getApplicationContext(), urlSpan.getURL());
            int start = spannabelText.getSpanStart(urlSpan);
            int end = spannabelText.getSpanEnd(urlSpan);
            spannabelText.removeSpan(urlSpan);
            spannabelText.setSpan(myUrlSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
    }

}
