package managedBeans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static hash.SHA2.getSHA512;
import inTouch.ejb.UserFacade;
import inTouch.entity.User;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Toshiba
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    
    @EJB
    private UserFacade userFacade;

    protected User user;
    protected String userName;
    protected String password;
    protected boolean login;
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        login= true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
   
    public String doLogin(){  
        String sha512 = getSHA512(password);
        User usuario = this.userFacade.findByUsernameAndPassword(userName, sha512);
        if(usuario!=null && usuario.getUsername().equals(userName) && usuario.getPassword().equals(sha512)){
            login=true;
            user=usuario;
            return "wall";
        }
        login=false;
        return "index";
    }
    
    public String doSignUp(){
    return "signUp";
    }
    
}
