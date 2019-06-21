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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author avila
 */
@Named(value = "changePasswordBean")
@RequestScoped
public class changePasswordBean implements Serializable{

    @EJB
    private UserFacade userFacade;

    @Inject 
    protected LoginBean loginBean;
    
    protected User userLogin;
    protected String oldPassword;
    protected String newPassword1;
    protected String newPassword2;
    protected boolean oldPasswordOK;
    protected boolean newPasswordsEquals;
    protected boolean newPasswordDiffCurrent;
    protected boolean allOk;
    
    /**
     * Creates a new instance of changePasswordBean
     */
    public changePasswordBean() {
    }

    @PostConstruct
    public void init()
    {
        this.userLogin = this.loginBean.getUser();
        this.oldPassword = "";
        this.newPassword1 = "";
        this.newPassword2 = "";
        this.oldPasswordOK = true;
        this.newPasswordsEquals = true;
        this.newPasswordDiffCurrent = true;
        this.allOk = false;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword1() {
        return this.newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return this.newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public boolean isOldPasswordOK() {
        return oldPasswordOK;
    }

    public void setOldPasswordOK(boolean oldPasswordOK) {
        this.oldPasswordOK = oldPasswordOK;
    }

    public boolean isNewPasswordsEquals() {
        return newPasswordsEquals;
    }

    public void setNewPasswordsEquals(boolean newPasswordsEquals) {
        this.newPasswordsEquals = newPasswordsEquals;
    }

    public boolean isNewPasswordDiffCurrent() {
        return newPasswordDiffCurrent;
    }

    public void setNewPasswordDiffCurrent(boolean newPasswordDiffCurrent) {
        this.newPasswordDiffCurrent = newPasswordDiffCurrent;
    }

    public boolean isAllOk() {
        return allOk;
    }

    public void setAllOk(boolean allOk) {
        this.allOk = allOk;
    }

    
    public String doSave()
    {
        this.oldPassword = getSHA512(oldPassword);
        this.newPassword1 = getSHA512(newPassword1);
        this.newPassword2 = getSHA512(newPassword2);
        this.oldPasswordOK = this.userLogin.getPassword().equals(this.oldPassword);
        this.newPasswordDiffCurrent = !this.userLogin.getPassword().equals(this.newPassword1);
        this.newPasswordsEquals = this.newPassword1.equals(this.newPassword2);
        this.allOk = this.oldPasswordOK && this.newPasswordDiffCurrent && this.newPasswordsEquals;
        
        if(this.allOk)
        {
            this.userLogin.setPassword(this.newPassword1);
            this.userFacade.edit(userLogin);
        }   
        
        return "changePassword";
    }
    
}
