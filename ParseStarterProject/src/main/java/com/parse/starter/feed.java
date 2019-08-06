package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
      Intent intent=getIntent();
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linear);

        String activeUserName=intent.getStringExtra("username");
        setTitle(activeUserName+"'s Feed");
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("images");
        query.whereEqualTo("username",activeUserName);

        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for (ParseObject object:objects)
                        {
                            ParseFile file= (ParseFile)object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null && data!=null)
                                    {
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                        ImageView imageView=new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        imageView.setImageBitmap(bitmap); //adding image view in linear layout
                                        linearLayout.addView(imageView);
                                    }
                                }
                            });

                        }
                    }
                }
            }
        });

    }
}
