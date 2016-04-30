package cs213.androidproject38;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    private ListView albumLV;
    private String name;
    private List<Album> albumList = new ArrayList<>();

    private List<String> albumNameList = new ArrayList<>();
    private ArrayAdapter<String> adapter ;
    private Context context = this;
    public int selected = -1;

    private MenuInflater menuInflater;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.addAlbumAction:
                promptAdd();
                return true;

            case R.id.editAlbumAction:
                onEdit();
                return true;

            case R.id.deleteAlbumAction:
                onDelete();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Albums");
        albumLV = (ListView) findViewById(R.id.AlbumListView);
        albumLV.setItemsCanFocus(false);

        albumOpen();

        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.album_row_simple, R.id.albumNameTV, albumNameList);
        albumLV.setAdapter(adapter);

        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), Photos.class));
            }
        });


    }


    public void promptAdd(){
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

    public void onEdit(){
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String s = view.toString();
                //System.out.println(s);
                changeAlbumName();
                albumOpen();

            }
        });
    }


    public void onDelete(){
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
                        albumOpen();
                    }
                });

                editPrompt.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        albumOpen();
                    }
                });

                editPrompt.show();

            }

        });
    }

    public void albumOpen() {
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                Intent myIntent = new Intent(Home.this, Thumbnail.class);
                Home.this.startActivity(myIntent);
            }
        });
    }

    public void changeAlbumName(){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Edit Album Name");
        prompt.setMessage("Modify the name of the album.");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(40, 25, 40, 25);
        //input.setText(oldName);


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


}
