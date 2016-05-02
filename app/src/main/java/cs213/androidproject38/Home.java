package cs213.androidproject38;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    private ListView albumLV;
    private String name;
    public static List<Album> albumList = new ArrayList<>();

    private static List<String> albumNameList = new ArrayList<>();
    private ArrayAdapter<String> adapter ;
    private Context context = this;
    public int selected = -1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.addAlbumAction:
                addAlbumAction();
                return true;

            case R.id.editAlbumAction:
                editAlbumAction();
                return true;

            case R.id.deleteAlbumAction:
                deleteAlbumAction();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        File f = new File("users.ser");
        System.out.println("Current directory: " + System.getProperty("user.dir"));

        if(f.exists() && !f.isDirectory()) {
            try {
                FileInputStream fileIn = new FileInputStream("/sdcard/user.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                albumList = (List<Album>) in.readObject();
                in.close();
                fileIn.close();
            } catch (Exception i) {
                i.printStackTrace();
                return;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Albums");
        albumLV = (ListView) findViewById(R.id.AlbumListView);
        albumLV.setItemsCanFocus(false);

        albumOpenListener();

        for(Album a : albumList){
            albumNameList.add(a.getName());
        }

        adapter = new ArrayAdapter<>(getApplication(), R.layout.album_row_simple, R.id.albumNameTV, albumNameList);
        albumLV.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }

    public void addAlbumAction(){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Add New Album");
        prompt.setMessage("Enter the name of the new album.");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 25, 40, 25);


        prompt.setView(input);

        // Set up the buttons
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = input.getText().toString();

                System.out.println(name);
                Album album = new Album(name);
                albumList.add(album);
                albumNameList.add(name);
                adapter.notifyDataSetChanged();
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

    public void editAlbumAction(){
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                String s = albumLV.getItemAtPosition(position).toString();
                System.out.println(s);
                changeAlbumName(s);
                albumOpenListener();

            }
        });
    }

    public void deleteAlbumAction(){
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Clicked position" + position);
                selected = position;
                AlertDialog.Builder editPrompt = new AlertDialog.Builder(context);
                editPrompt.setTitle("Delete Album");
                editPrompt.setMessage("Do you want to delete album?");

                // Set up the buttons
                editPrompt.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        albumList.remove(selected);
                        albumNameList.remove(selected);
                        adapter.notifyDataSetChanged();
                        albumOpenListener();
                    }
                });

                editPrompt.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        albumOpenListener();
                    }
                });

                editPrompt.show();
            }
        });
    }

    public void albumOpenListener() {
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                Photos.currAlbum = position;
                System.out.println("POSITION = " + position);
                startActivity(new Intent(getApplicationContext(), Photos.class));
            }
        });
    }

    public void changeAlbumName(String oldName){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Edit Album Name");
        prompt.setMessage("Modify the name of the album.");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 25, 40, 25);
        input.setText(oldName);


        prompt.setView(input);

        // Set up the buttons
        prompt.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                name = input.getText().toString();
                albumList.get(selected).setName(name);
                albumNameList.set(selected, name);

                adapter.notifyDataSetChanged();
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

    @Override
    public void onDestroy(){

        super.onDestroy();

        try {

            FileOutputStream fileOut = new FileOutputStream("users.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new ArrayList<Album>(albumList));
            out.close();

            /*
            FileOutputStream fos = context.openFileOutput("user.ser", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(albumList);
            os.close();
            fos.close();
            */

        }catch(Exception i){
            i.printStackTrace();
        }

        /*
        try {
            FileOutputStream fileOut = new FileOutputStream("users.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(albumList);
            out.close();
            fileOut.close();
        }catch(IOException i){
            i.printStackTrace();
        }

        */
    }

}
