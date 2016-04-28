package cs213.androidproject38;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christine on 4/25/16.
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 6843591266296171581L;
    private String name;
    private List<Photo> photolist = new ArrayList<Photo>();
    private String coverPhoto;
    private String description;

    public Album(String name){
        this.name = name;
        //String base = System.getProperty("user.dir");

        coverPhoto = "../../../res/drawable/no_photo.png";

        //String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

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

    public String toString(){
        String ret = name +": ";

        for(int i = 0; i<photolist.size(); i++){
            ret = ret +  photolist.get(i) + ", ";
        }
        return ret;
    }

    public List<Photo> getPhotolist() {
        return photolist;
    }

    public void setPhotolist(List<Photo> photolist) {
        this.photolist = photolist;
    }

    public void setCoverPhoto(String url){
        this.coverPhoto = url;
    }
    public String getCoverPhoto(){
        return this.coverPhoto;
    }

    public void setDescription(String desc){
        this.description = desc;
    }
    public String getDescription(){
        return this.description;
    }

}

