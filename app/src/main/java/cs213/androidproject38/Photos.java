package cs213.androidproject38;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class Photos extends AppCompatActivity {

    GridView gv;
    ArrayList<File> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        System.out.println(Environment.getExternalStorageDirectory().toString());

        list = imageReader(Environment.getExternalStorageDirectory());

        gv = (GridView) findViewById(R.id.gridView);
        gv.setAdapter(new GridAdapter());

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ViewImage.class).putExtra("img",list.get(position)));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.addPhotoAction:
                addPhoto();
                return true;

            case R.id.movePhotoAction:
                return true;

            case R.id.deletePhotoAction:
                return true;

            case R.id.tagPhotoAction:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void addPhoto(){
        openGallery();
        System.out.println();
    }

    public void openGallery(){

    }

    class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.single_grid, parent, false);
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView2);

            //iv.setImageURI(Uri.parse(getItem(position).toString()));

            return convertView;
        }
    }

    ArrayList<File> imageReader(File root) {

        ArrayList<File> a = new ArrayList<>();

        File[] files = root.listFiles();

        if(files == null){
            return a;
        }

        for(int i = 0; i < files.length; i++){
            if(files[i].isDirectory()){
                a.addAll(imageReader(files[i]));
            }else{
                if(files[i].getName().endsWith(".jpg")){
                    a.add(files[i]);
                }
            }
        }

        return a;
    }


}
