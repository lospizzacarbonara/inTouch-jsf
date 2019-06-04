/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.MembershipFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
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
@Named(value = "userProfileLoginBean")
@RequestScoped
public class userProfileLoginBean {

    @EJB
    private UserFacade userFacade;

    @EJB
    private MembershipFacade membershipFacade;

    
    @Inject
    protected LoginBean loginBean;
    
    
    private User userProfile;
    private List<SocialGroup> userGroups;
    /**
     * Creates a new instance of userProfileLoginBean
     */
    public userProfileLoginBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.userProfile = this.loginBean.getUser();
        this.userGroups = this.membershipFacade.findGroupsBetweenUsers(userProfile, userProfile);
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
    
    public String doUserProfileLogin()
    {
        return "userProfileLogin";
    }
    
    public String doUserProfileLoginSave()
    {
        this.userFacade.edit(this.userProfile);
        return "userProfileLogin";
    }
    
    public String doSaveEmail()
    {
        this.userFacade.edit(this.userProfile);
        return "userProfileLogin";
    }
}
