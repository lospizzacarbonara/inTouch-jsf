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
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author avila
 */
@Named(value = "userProfileUserBean")
@SessionScoped
public class userProfileUserBean implements Serializable{
    
    @EJB
    private MembershipFacade membershipFacade;

    @EJB
    private FriendshipFacade friendshipFacade;

    @EJB
    UserFacade userFacade;

    @Inject
    protected LoginBean loginBean;
    
    @Inject
    protected userProfileLoginBean userProfileLoginBean;
    
    private User userProfile;
    private List<SocialGroup> userGroups;
    private boolean userFriend;
    /**
     * Creates a new instance of userProfileUserBean
     */
    public userProfileUserBean() {
    }

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }

    public List<SocialGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<SocialGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public boolean isUserFriend() {
        return userFriend;
    }

    public void setUserFriend(boolean userFriend) {
        this.userFriend = userFriend;
    }
    
    public String doUserProfileUser(User user)
    {
        String str;
        User userLogin = this.loginBean.getUser();
        if(userLogin.getId().equals(user.getId()))
        {
            str = this.userProfileLoginBean.doUserProfileLogin();
        }
        else
        {
            this.userProfile = user;
            this.userFriend = this.friendshipFacade.areFriends(userProfile, userLogin);
            this.userGroups = this.membershipFacade.findGroupsBetweenUsers(userProfile, userLogin);
            str = "userProfileUser";
        }
        return str;
    }    
}
