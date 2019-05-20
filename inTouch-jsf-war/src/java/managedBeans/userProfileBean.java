/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.FriendshipFacade;
import inTouch.ejb.MembershipFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author avila
 */
@Named(value = "userProfileBean")
@RequestScoped
public class userProfileBean {

    @EJB
    private MembershipFacade membershipFacade;

    @EJB
    private FriendshipFacade friendshipFacade;

    @EJB
    UserFacade userFacade;

    @Inject
    protected LoginBean loginBean;
    
    
    private User userLogin;
    private User userOther;
    private List<SocialGroup> userGroups;
    private boolean userFriend;
    
    public userProfileBean() {
    }

    public User getUserLogin() {
        return userLogin;
    }    

    public List<SocialGroup> getUserGroups() {
        return userGroups;
    }

    public boolean isUserFriend() {
        return userFriend;
    }

    public User getUserOther() {
        return userOther;
    }    
    
    public void setUserOther(User userOther) {
        this.userOther = userOther;
    }
    
    @PostConstruct
    public void init()
    {
        this.userLogin = this.loginBean.user;
        this.userOther = new User();
        this.userFriend = false;
        this.userGroups = Collections.emptyList();
    }    
}
