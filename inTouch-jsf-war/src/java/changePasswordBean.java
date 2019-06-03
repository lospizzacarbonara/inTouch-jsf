/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static hash.SHA2.getSHA512;
import inTouch.ejb.UserFacade;
import inTouch.entity.User;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import managedBeans.LoginBean;

/**
 *
 * @author avila
 */
@Named(value = "changePasswordBean")
@RequestScoped
public class changePasswordBean {

    @EJB
    private UserFacade userFacade;

    @Inject 
    protected LoginBean loginBean;
    
    protected User userLogin;
    protected String oldPassword;
    protected String newPassword1;
    protected String newPassword2;
    
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
    }
    
    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }
    
    public String doSave()
    {
        return null;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) 
    {
        this.newPassword1 = getSHA512(newPassword1);
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) 
    {
        this.newPassword2 = getSHA512(newPassword2);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = getSHA512(oldPassword);
    }
    
}
