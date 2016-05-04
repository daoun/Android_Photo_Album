package cs213.androidproject38;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christine on 5/4/16.
 */
public class MovePhotoAlbumList extends AppCompatActivity {

    ListView listLV;
    private Context context = this;
    ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<>();

    public static int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_photo_albumlist);
        setTitle("Choose Album");

        listLV = (ListView) findViewById(R.id.movePhotoAlbumList);

        for(int i = 0; i < Home.user.getAlbumlist().size(); i++){
            list.add(Home.user.getAlbum(i).getName());
        }





        listLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;

                System.out.println("ALBUM CHOSEN TO MOVE = " + selected);
                Photo movePhoto = Home.user.getAlbum(Photos.currAlbum).getPhoto(Photos.selected);
                Home.user.getAlbum(Photos.currAlbum).remove(Photos.selected);
                Home.user.getAlbum(selected).addPhoto(movePhoto);

                store();

                startActivity(new Intent(getApplicationContext(), Photos.class));

            }
        });


        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listLV.setAdapter(adapter);

    }

    public void store(){
        try {
            FileOutputStream fos = context.openFileOutput(Home.FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(Home.user);
            os.close();
            fos.close();

            File f = new File(getFilesDir(),Home.FILENAME);
            if(f.exists()){
                System.out.println("STORED FILE");
            }else{
                System.out.println("DID NOT STORE FILE");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
