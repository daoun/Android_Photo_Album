package cs213.androidproject38;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Album> albumlist = new ArrayList<Album>();


    public Album getAlbum(int index){
        return albumlist.get(index);
    }

    public List<Album> getAlbumlist() {
        return albumlist;
    }

}