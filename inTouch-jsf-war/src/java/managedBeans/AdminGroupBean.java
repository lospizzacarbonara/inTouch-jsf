/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.MembershipFacade;
import inTouch.ejb.SocialGroupFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.Membership;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.util.Date;
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
@Named(value = "adminGroupBean")
@RequestScoped
public class AdminGroupBean {

    @EJB
    private SocialGroupFacade socialGroupFacade;
    
    @EJB
    private UserFacade userFacade;
    
    @EJB
    private MembershipFacade membershipFacade;
    
    @Inject
    private LoginBean loginBean;  
    
    protected User user;
    protected String groupName;
    protected String groupDesc;
    protected List<SocialGroup> groupList;
    /**
     * Creates a new instance of AdminGroupBean
     */
    public AdminGroupBean() {}
    
    @PostConstruct
    public void setUp(){
        user = loginBean.getUser();
        groupList = userFacade.findSocialGroups(user);
    }
    
    public void doGroupCreation(){
        Date date = new Date();
        
        SocialGroup group = new SocialGroup(0, groupName, date);
        group.setDescription(groupDesc);
        socialGroupFacade.create(group);
        
        //Adding the user as admin
        Membership membership = new Membership(0, true);
        membership.setSocialGroup(group);
        membership.setMember1(user);
        membershipFacade.create(membership);
        
        //reload groupList
        groupList = userFacade.findSocialGroups(user);
    }
    
    public void doGroupDeletion(SocialGroup group){
        socialGroupFacade.remove(group);
        
        //reload group List
        groupList = userFacade.findSocialGroups(user);
    }
    
    /*
    * --- SETTERS ---
    */    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
    
    /*
    * --- GETTERS ---
    */    
    public User getUser() {
        return user;
    }
    
    public String getGroupName() {
        return groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }
}
