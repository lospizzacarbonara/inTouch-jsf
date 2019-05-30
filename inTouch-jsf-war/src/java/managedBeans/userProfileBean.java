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
    
    
    private User userProfile;
    private List<SocialGroup> userGroups;
    private boolean userFriend;
    private boolean myProfile;
    
    public userProfileBean() {
    }

    public List<SocialGroup> getUserGroups() {
        return userGroups;
    }

    public boolean isUserFriend() {
        return userFriend;
    } 

    public User getUserProfile() {
        return userProfile;
    }

   
    
    public boolean isMyProfile() {
        return myProfile;
    }
    
    public String doProfileUserLogin()
    {
        userProfile = loginBean.getUser();
        userFriend = false;
        myProfile = true;
        userGroups = Collections.emptyList();
        return "userProfile";
    }
    
    public String doProfileUser(User user)
    {
        User userLogin = loginBean.getUser();
        this.userProfile = user;
        myProfile = false;
        userFriend = this.friendshipFacade.areFriends(userLogin, user);
        userGroups = this.membershipFacade.findGroupsBetweenUsers(userLogin, user);
        return "userProfile";
    }
    
    public String doSaveProfileUser()
    {
        
        return "userProfile";
    }
}
