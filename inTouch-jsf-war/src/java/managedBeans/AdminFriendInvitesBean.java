/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.UserFacade;
import inTouch.entity.User;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Nellogy
 */
@Named(value = "adminFriendInvitesBean")
@RequestScoped
public class AdminFriendInvitesBean {

    @EJB
    private UserFacade userFacade;
    
    @Inject
    private LoginBean loginBean;
    
    protected User user;
    protected List<User> friendInvites;
    /**
     * Creates a new instance of AdminFriendInvitesBean
     */
    public AdminFriendInvitesBean() {
    }
    
    @PostConstruct
    public void setUp(){
        user = loginBean.getUser();
        friendInvites = userFacade.findPendingToAcceptFriends(user);
    }
    
    public boolean isEmpty() {
        return friendInvites.isEmpty();
    }

    /*
    * --- SETTERS ---
    */ 
    public void setUser(User user) {
        this.user = user;
    }

    public void setFriendInvites(List<User> friendInvites) {
        this.friendInvites = friendInvites;
    }
   
    /*
    * --- GETTERS ---
    */ 
    public User getUser() {
        return user;
    }
    
    public List<User> getFriendInvites() {
        return friendInvites;
    }
}
