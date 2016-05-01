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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Photos extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static int currAlbum;
    private GridLayout thumbnailsGL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        thumbnailsGL = (GridLayout) findViewById(R.id.thumbnails);
        setTitle(Home.albumList.get(currAlbum).getName());
        int size = Home.albumList.get(currAlbum).getPhotolistSize();

        for(int i = 0; i < size; i++){
            addPhotoToGL(Home.albumList.get(currAlbum).getPhotolist().get(i).getURL());
        }

    }


    /**
     * adds photo to the grid layout
     * @param image
     */
    public void addPhotoToGL(Bitmap image){
        ImageView iv = new ImageView(this);

        iv.setImageBitmap(image);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(265,265);
        iv.setLayoutParams(lp);
        iv.setPadding(15, 15, 15, 15);
        thumbnailsGL.addView(iv);


    }


    /**
     * called when add photo action button is clicked
     */
    public void addPhotoAction(){
        openGallery();
        System.out.println();
    }

    /**
     * opens the photo gallery
     */
    public void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pdirpath = pdir.getPath();
        Uri data = Uri.parse(pdirpath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.addPhotoAction:
                addPhotoAction();
                return true;

            case R.id.movePhotoAction:
                return true;

            case R.id.deletePhotoAction:
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){ //if we are here, everything processed successfully
            if(requestCode == IMAGE_GALLERY_REQUEST){//if we are here, we are hearing back from the image gallery

                Uri imageUri = data.getData(); //address of image in sdcard
                InputStream inputStream; // declare a stream to read the image data from sdcard

                try {
                    //System.out.println("IMAGE URI" + imageUri);
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    addPhotoToGL(image);
                    // add photo to array list
                    Photo pic = new Photo(image);
                    Home.albumList.get(currAlbum).addPhoto(pic);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image.", Toast.LENGTH_LONG).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
