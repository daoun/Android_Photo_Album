package cs213.androidproject38;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

public class ViewImage extends AppCompatActivity {
    //Test
    ImageView iv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        String f = getIntent().getStringExtra("img");
        iv3 = (ImageView) findViewById(R.id.imageView3);
        iv3.setImageURI(Uri.parse(f));

    }
}
