package cs213.androidproject38;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Photos extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static int currAlbum;
    private GridView thumbnailGV;
    private Context context = this;

    int option; // 1-move; 2-delete

    ArrayList<Photo> photolist;
    private ImageAdapter<Photo> adapter;
    public static int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        thumbnailGV = (GridView) findViewById(R.id.thumbnails);
        setTitle(Home.user.getAlbumlist().get(currAlbum).getName());
        photolist = (ArrayList<Photo>) Home.user.getAlbumlist().get(currAlbum).getPhotolist();

        adapter = new ImageAdapter<>(this, 1, photolist);
        thumbnailGV.setAdapter(adapter);
        photoOpenListener();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchMenuItem = menu.findItem(R.id.search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchResult.class)));
        mSearchView.setIconifiedByDefault(false);

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
                movePhotoAction();
                return true;

            case R.id.deletePhotoAction:
                deletePhotoAction();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void photoOpenListener() {
        thumbnailGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                ViewImage.currPhoto = position;
                //System.out.println("POSITION = " + position);
                startActivity(new Intent(getApplicationContext(), ViewImage.class));
            }
        });
    }
    public void movePhotoAction(){
        thumbnailGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
                startActivity(new Intent(getApplicationContext(), MovePhotoAlbumList.class));



            }
        });



    }

    public void deletePhotoAction(){
        thumbnailGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                        photolist.remove(selected);
                        store();
                        adapter.notifyDataSetChanged();
                        photoOpenListener();
                    }
                });

                editPrompt.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        photoOpenListener();
                    }
                });

                editPrompt.show();
            }
        });
    }


    /**
     * called when add photo action button is clicked
     */
    public void addPhotoAction() {
        openGallery();
        System.out.println();
    }

    /**
     * opens the photo gallery
     */
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pdirpath = pdir.getPath();
        Uri data = Uri.parse(pdirpath);

        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { //if we are here, everything processed successfully
            if (requestCode == IMAGE_GALLERY_REQUEST) {//if we are here, we are hearing back from the image gallery

                Uri imageUri = data.getData(); //address of image in sdcard
                //System.out.println("photo Uri = " + imageUri);
                //System.out.println("photo Uri (to string) = " + imageUri.toString());
                //Uri imagestrinuri = (Uri) Uri.parse(imageUri.toString());
                Photo pic = new Photo(imageUri.toString());
                Home.user.getAlbumlist().get(currAlbum).addPhoto(pic);
                store();
                adapter.notifyDataSetChanged();


                /*   InputStream inputStream; // declare a stream to read the image data from sdcard



                //java.lang.SecurityException: Permission Denial: reading com.android.providers.media.MediaProvider uri content://media/external/images/media/30 from pid=3274, uid=10058 requires android.permission.READ_EXTERNAL_STORAGE, or grantUriPermission()

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    // add photo to array list
                    Photo pic = new Photo(image);
                    Home.user.getAlbumlist().get(currAlbum).addPhoto(pic);
                    store();
                    adapter.notifyDataSetChanged();
                    //addPhotoToGV(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image.", Toast.LENGTH_LONG).show();
                }

                */
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public class ImageAdapter<T> extends ArrayAdapter<Photo> {
        private Context mContext;
        private List<Photo> list;

        public ImageAdapter(Context c, int res, ArrayList<Photo> list) {
            super(c, res, list);
            mContext = c;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Photo getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = new ImageView(mContext);

            iv.setImageURI(Uri.parse(list.get(position).getURL()));
            //iv.setImageBitmap(list.get(position).getURL());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(265, 265);
            iv.setLayoutParams(lp);
            iv.setPadding(15, 15, 15, 15);

            return iv;
        }

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

    /**
     * adds photo to the grid layout
     * @param image
     */
 /*   public void addPhotoToGV(Bitmap image) {
        ImageView iv = new ImageView(this);

        iv.setImageBitmap(image);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(265, 265);
        // iv.setLayoutParams(lp);
        iv.setPadding(15, 15, 15, 15);


        thumbnailGV.addView(iv, lp);
        // thumbnailGV.addView(iv);
    }
*/
}