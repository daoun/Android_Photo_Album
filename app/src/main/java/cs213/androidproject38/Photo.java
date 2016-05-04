package cs213.androidproject38;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {

    private static final long serialVersionUID = 2651057460302515220L;
    private List<String> locationTagList = new ArrayList<>();
    private List<String> personTagList = new ArrayList<>();

    private String url;

    public Photo(String url){
        this.url = url;
    }

    public String getURL() {
        return url;
    }
    public void setURL(String url) {
        this.url = url;
    }

    public List<String> getLocationTaglist() {
        return locationTagList;
    }

    public void setLocationTaglist(List<String> taglist) {
        this.locationTagList = taglist;
    }

    public List<String> getPersonTaglist(){
        return personTagList;
    }

    public void setPersonTaglist(List<String> taglist){
        this.personTagList = taglist;
    }
}
