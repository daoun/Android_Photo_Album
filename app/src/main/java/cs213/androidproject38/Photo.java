package cs213.androidproject38;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Christine on 4/25/16.
 */
public class Photo implements Serializable {

    private static final long serialVersionUID = 2651057460302515220L;
    private List<String> taglist = new ArrayList<String>();;
    private Bitmap url;
    private String caption;
    private Date date;

    public Photo(Bitmap url){
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTag(int index) {
        return taglist.get(index);
    }
    public void addTag(String tag) {
        this.taglist.add(tag);
    }
    public int getTaglistSize(){
        return taglist.size();
    }

    public Bitmap getURL() {
        return url;
    }
    public void setURL(Bitmap url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString(){
        return caption;
    }

    public List<String> getTaglist() {
        return taglist;
    }

    public void setTaglist(List<String> taglist) {
        this.taglist = taglist;
    }
}
