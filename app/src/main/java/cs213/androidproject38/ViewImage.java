package cs213.androidproject38;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        photo = Home.albumList.get(Photos.currAlbum).getPhoto(currPhoto);

        photoIV = (ImageView) findViewById(R.id.photoIV);
        photoIV.setImageBitmap(photo.getURL());

        locationTV = (TextView) findViewById(R.id.locationTagTV);
        locationTV.setText( combineTags(photo.getLocationTaglist()) );

        personTV = (TextView) findViewById(R.id.personTagTV);
        personTV.setText( combineTags(photo.getPersonTaglist()) );


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
                Home.albumList.get(Photos.currAlbum).getPhoto(currPhoto).setPersonTaglist(personTagList);
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
                Home.albumList.get(Photos.currAlbum).getPhoto(currPhoto).setLocationTaglist(locationTagList);
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
