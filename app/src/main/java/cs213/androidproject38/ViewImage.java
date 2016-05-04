package cs213.androidproject38;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewImage extends AppCompatActivity {
    //Test
    ImageView photoIV;

    public static int currPhoto;
    
    private Photo photo;
    String personTagString;
    List<String> personTagList = new ArrayList<>();
    String locationTagString;
    List<String> locationTagList = new ArrayList<>();

    TextView locationTV;
    TextView personTV;


    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        photo = Home.user.getAlbumlist().get(Photos.currAlbum).getPhoto(currPhoto);

        photoIV = (ImageView) findViewById(R.id.photoIV);
        locationTV = (TextView) findViewById(R.id.locationTagTV);
        personTV = (TextView) findViewById(R.id.personTagTV);

        uploadPhotoAndInfo();


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

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {

                    if (x2 > x1) { // Right swipe action
                        if(currPhoto == 0) {
                            Toast.makeText(this, "No more pictures to the left", Toast.LENGTH_SHORT).show ();
                            return false;
                        } else {
                            currPhoto--;
                        }
                        photo = Home.user.getAlbumlist().get(Photos.currAlbum).getPhoto(currPhoto);
                        uploadPhotoAndInfo();

                    }
                    else { // Left swipe action
                        if(currPhoto == (Home.user.getAlbumlist().get(Photos.currAlbum).getPhotolistSize() - 1)) {
                            Toast.makeText(this, "No more pictures to the right", Toast.LENGTH_SHORT).show ();
                            return false;
                        } else {
                            currPhoto++;
                        }
                        photo = Home.user.getAlbumlist().get(Photos.currAlbum).getPhoto(currPhoto);
                        uploadPhotoAndInfo();
                    }
                }
                else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void uploadPhotoAndInfo(){
        photoIV.setImageURI(Uri.parse(photo.getURL()));
        //photoIV.setImageBitmap(photo.getURL());
        locationTV.setText( combineTags(photo.getLocationTaglist()) );
        personTV.setText(combineTags(photo.getPersonTaglist()));

    }

    public void addPersonTagAction(){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Person Tag");
        prompt.setMessage("Add and modify PERSON tags (separate each tag with #)");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 25, 40, 25);
        input.setText(combineTags(photo.getPersonTaglist()));

        prompt.setView(input);

        // Set up the buttons
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                personTagString = input.getText().toString();
                personTagList = parseTags(personTagString);
                Home.user.getAlbumlist().get(Photos.currAlbum).getPhoto(currPhoto).setPersonTaglist(personTagList);
                personTV.setText(combineTags(personTagList));
            }
        });
        prompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        prompt.show();
    }
    public void addLocationTagAction(){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Location Tag");
        prompt.setMessage("Add and modify LOCATION tags (separate each tag with #)");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 25, 40, 25);
        input.setText(combineTags(photo.getLocationTaglist()));

        prompt.setView(input);

        // Set up the buttons
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                locationTagString = input.getText().toString();
                locationTagList = parseTags(locationTagString);
                Home.user.getAlbumlist().get(Photos.currAlbum).getPhoto(currPhoto).setLocationTaglist(locationTagList);
                locationTV.setText(combineTags(locationTagList));
            }
        });
        prompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        prompt.show();
    }

    public List<String> parseTags(String input){
        List<String> result = new ArrayList<>();

        input=input.replaceAll("\\s+","");
        input=input.replaceAll("\\s","");

        for (String retval: input.split("#")){
            result.add(retval);
        }
        for(int i = result.size()-1; i >= 0; i--){
            if(result.get(i).equals("") || result.get(i).equals(" ")){
                result.remove(i);
            }
        }
        return result;
    }

    public String combineTags(List<String> taglist){
        String result = "";

        for(int i = 0; i <taglist.size(); i++){
            result = result + "#" + taglist.get(i) + " ";
        }

        return result;
    }
}
