package cs213.androidproject38;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christine on 4/25/16.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Album> albumlist = new ArrayList<>();

    public void addAlbum(Album album){
        this.albumlist.add(album);
    }
    public Album getAlbum(int index){
        return albumlist.get(index);
    }
    public int getAlbumlistSize(){
        return albumlist.size();
    }
    public void remove(int index){
        this.albumlist.remove(index);
    }

    public List<Album> getAlbumlist() {
        return albumlist;
    }

    public void setAlbumlist(List<Album> albumlist) {
        this.albumlist = albumlist;
    }

}