/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.UserFacade;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author Nellogy
 */
@Named(value = "adminGroupInvitesBean")
@Dependent
public class AdminGroupInvitesBean {

    @EJB
    private UserFacade userFacade;
    
    @Inject
    private LoginBean loginBean;
    
    protected User user;
    protected List<SocialGroup> groupInvites;
    protected boolean isEmpty;
    
    /**
     * Creates a new instance of AdminGroupInvitesBean
     */
    public AdminGroupInvitesBean() {
    }
    
    
    @PostConstruct
    public void setUp(){
        user = loginBean.getUser();
        groupInvites = userFacade.findPendingInvitationMemberships(user);
        isEmpty = groupInvites.isEmpty();
    }
    
    /*
    * --- SETTERS ---
    */ 
    public void setUser(User user) {
        this.user = user;
    }

    public void setGroupInvites(List<SocialGroup> groupInvites) {
        this.groupInvites = groupInvites;
    }
   
    /*
    * --- GETTERS ---
    */ 
    public User getUser() {
        return user;
    }
    
    public List<SocialGroup> getGroupInvites() {
        return groupInvites;
    }
    
    public boolean getIsEmpty() {
        return isEmpty;
    }
    
}
