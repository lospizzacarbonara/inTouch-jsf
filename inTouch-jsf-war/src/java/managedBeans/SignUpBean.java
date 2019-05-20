/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Toshiba
 */
@Named(value = "signUpBean")
@Dependent

public class SignUpBean implements Serializable {
protected String userName;
protected String password;
    /**
     * Creates a new instance of SignUpBean
     */
    public SignUpBean() {
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String doSignUp(){
        
    return "index";
    }
}
