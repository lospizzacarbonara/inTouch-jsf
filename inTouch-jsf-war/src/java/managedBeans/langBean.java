/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

/**
 *
 * @author jfaldanam
 */
@Named(value = "langBean")
@SessionScoped
public class langBean implements Serializable {
    
    private String selected;
    /**
     * Creates a new instance of langBean
     */
    public langBean() {
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
    
    @PostConstruct
    private void init() {
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en"));
    }
    
    public void changeLocale() {
        if (selected != null && selected.equals("null"))
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(selected));
    }
}
