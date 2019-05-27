/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import static hash.SHA2.getSHA512;
import inTouch.ejb.UserFacade;
import inTouch.entity.User;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Toshiba
 */
@Named(value = "signUpBean")
@RequestScoped

public class SignUpBean implements Serializable {
    
    @EJB
    private UserFacade userFacade;
    
protected String userName;
protected String password;
protected String name;
protected String surname;
protected String email;
protected String fecha;
    /**
     * Creates a new instance of SignUpBean
     */
    public SignUpBean() {
        this.userName="";
        this.password="";
        this.email="";
        this.surname="";
        this.email="";
        this.fecha="";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public String doSignUp(){
     User usuario= new User(0, userName, getSHA512(password),"");
     usuario.setEmail(email);
     usuario.setName(name);
     usuario.setSurname(surname);
     SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
     if(!fecha.equals("")){
        try {
            Date date = format.parse(fecha);
            usuario.setBirthdate( new Date(date.getTime()+24*60*60*1000) );
        } catch (ParseException ex) {
            Logger.getLogger(SignUpBean.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     this.userFacade.create(usuario);
       
    return "login";
    }
}
