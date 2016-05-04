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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    private ListView albumLV;
    private String name;
    public static User user = new User();
    private ArrayAdapter<Album> adapter;
    private Context context = this;
    public int selected = -1;
    public static String FILENAME = "user4.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        System.out.println(getFilesDir());
        File f = new File(getFilesDir(), FILENAME);
        if(f.exists()) {
            try {

                System.out.println("FILE EXISTS");
                FileInputStream fis = context.openFileInput(FILENAME);
                ObjectInputStream is = new ObjectInputStream(fis);
                user = (User) is.readObject();
                System.out.println(user.getAlbum(0).getName());

                is.close();
                fis.close();
            } catch (Exception i) {
                i.printStackTrace();
                return;
            }
        }

        setContentView(R.layout.activity_home);
        setTitle("Albums");
        albumLV = (ListView) findViewById(R.id.AlbumListView);
        albumLV.setItemsCanFocus(false);

        albumOpenListener();

        adapter = new AlbumAdapter<>(this, 1, (ArrayList<Album>) user.getAlbumlist());
        albumLV.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

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

    /**
     * Prompts the user for new album name and adds the new album
     */
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
                Album album = new Album(name);
                user.getAlbumlist().add(album);
                store();
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

    /**
     * Allows user to edit the selected album name
     */
    public void editAlbumAction(){
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                String s = user.getAlbumlist().get(position).getName();
                changeAlbumName(s);
                albumOpenListener();
                store();

            }
        });
    }

    /**
     * deletes the selected album from the list
     */
    public void deleteAlbumAction(){
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected = position;
                AlertDialog.Builder editPrompt = new AlertDialog.Builder(context);
                editPrompt.setTitle("Delete Album");
                editPrompt.setMessage("Do you want to delete album?");

                // Set up the buttons
                editPrompt.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.getAlbumlist().remove(selected);
                        store();
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

    /**
     * Sets up listener on the listview so that selected album opens the photos in the album
     */
    public void albumOpenListener() {
        albumLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                Photos.currAlbum = position;
                store();
                startActivity(new Intent(getApplicationContext(), Photos.class));
            }
        });
    }

    /**
     * Prompts the user for name change
     * @param oldName the original album name
     */
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
                user.getAlbumlist().get(selected).setName(name);

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

    /**
     * stores all albums information to serialized data file
     */
    public void store(){
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();

            File f = new File(getFilesDir(),FILENAME);
            if(f.exists()){
                System.out.println("STORED FILE");
            }else{
                System.out.println("DID NOT STORE FILE");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Album Adapter manages the albums listed in ListView
     * @param <T>
     */
    public class AlbumAdapter<T> extends ArrayAdapter<Album> {
        private Context mContext;
        private List<Album> list;

        public AlbumAdapter(Context c, int res, ArrayList<Album> list) {
            super(c, res, list);
            mContext = c;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Album getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(30);
            textView.setPadding(25,30,25,30);
            textView.setText(list.get(position).getName());
            return textView;
        }

    }

}
