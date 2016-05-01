package cs213.androidproject38;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ViewImage extends AppCompatActivity {
    //Test
    ImageView iv3;

    public static int currPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        //String f = getIntent().getStringExtra("img");
        //iv3 = (ImageView) findViewById(R.id.imageView3);
        //iv3.setImageURI(Uri.parse(f));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.addPersonTagAction:
                addPersonTagAction();
                return true;

            case R.id.addLocationTagAction:
                addLocationTagAction();
                return true;

            case R.id.deleteTagAction:
                deleteTagAction();
                return true;

            case R.id.displayInfoAction:
                displayInfoAction();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addPersonTagAction(){

    }
    public void addLocationTagAction(){

    }
    public void deleteTagAction(){

    }
    public void displayInfoAction(){

    }
}
