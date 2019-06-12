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
import javax.faces.context.FacesContext;

/**
 *
 * @author jfaldanam
 */
@Named(value = "langBean")
@SessionScoped
public class langBean implements Serializable {
    
private static final long serialVersionUID = 1L;
	
  private final static Locale ENGLISH = new Locale("en");
  private final static Locale SPANISH = new Locale("es");
  private Locale currentLocale; 
  private String language;
  
  public Locale getCurrentLocale() {
    return(currentLocale);
  }

    public String getLanguage() {
        this.changeLocale();
        return language;
        
    }

    public void setLanguage(String language) {
        this.language = language;
    }

  public String setEnglish() {
    currentLocale=ENGLISH;
    changeLocale();
    return null;
  }
  
  public String setSpanish() {
    currentLocale=SPANISH;
    changeLocale();    
    return null;
  }
  
  private void changeLocale () {
      FacesContext.getCurrentInstance().getViewRoot().setLocale(currentLocale);
  }
  
  public String changeLanguage() {
    switch(language) {
        case "en":
            setEnglish();
            break;
        case "es":
            setSpanish();
            break;
    }
    return null;
  }
  
}
