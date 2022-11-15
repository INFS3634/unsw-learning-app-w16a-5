package au.edu.unsw.infs3634.unswlearning.Model;

import com.google.firebase.firestore.DocumentId;

public class ModuleListModel {

    //Firebase query
    @DocumentId
    private String module_id;
    private String desc, image, title, completed;

    //empty constructor for Firebase
    public ModuleListModel() {

    }

    public ModuleListModel(String module_id, String desc, String image, String title, String completed) {
        this.module_id = module_id;
        this.desc = desc;
        this.image = image;
        this.title = title;
        this.completed = completed;
    }


    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}
