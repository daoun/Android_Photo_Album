package cs213.androidproject38;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Photos extends AppCompatActivity {

    GridView gv;
    ArrayList<File> list;
    public static final int IMAGE_GALLERY_REQUEST = 20;

    private ImageView imgPicture;


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


        //imgPicture = (ImageView) findViewById(R.id.addPhotoAction);

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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pdirpath = pdir.getPath();
        Uri data = Uri.parse(pdirpath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            //if we are here, everything processed successfully
            if(requestCode == IMAGE_GALLERY_REQUEST){
                //if we are here, we are hearing back from the image gallery

                //address of image in sdcard
                Uri imageUri = data.getData();

                // declare a stream to read the image data from sdcard
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    //get a bitmap from the stream
                    Bitmap image = BitmapFactory.decodeStream(inputStream);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image.", Toast.LENGTH_LONG).show();
                }


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
            ImageView iv = (ImageView) convertView.findViewById(R.id.imageView);

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
