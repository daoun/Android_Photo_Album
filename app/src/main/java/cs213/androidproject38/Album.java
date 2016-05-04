package cs213.androidproject38;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {

    private static final long serialVersionUID = 6843591266296171581L;
    private String name;
    private List<Photo> photolist = new ArrayList<>();


    public Album(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void addPhoto(Photo pic){
        this.photolist.add(pic);
    }
    public Photo getPhoto(int index){
        return photolist.get(index);
    }
    public int getPhotolistSize(){
        return photolist.size();
    }
    public void remove(int index){
        this.photolist.remove(index);
    }

    public List<Photo> getPhotolist() {
        return photolist;
    }
    public void setPhotolist(List<Photo> photolist) {
        this.photolist = photolist;
    }

    public String toString(){
        String ret = name +": ";

        for(int i = 0; i<photolist.size(); i++){
            ret = ret +  photolist.get(i) + ", ";
        }
        return ret;
    }
}

