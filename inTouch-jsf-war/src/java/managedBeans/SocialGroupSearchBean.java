/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.PendingFriendshipFacade;
import inTouch.ejb.PendingMembershipFacade;
import inTouch.ejb.SocialGroupFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.PendingFriendship;
import inTouch.entity.PendingMembership;
import inTouch.entity.Post;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import markdownj.Markdown;

/**
 *
 * @author darioarrebola
 */
@Named(value = "groupSearchBean")
@SessionScoped

public class SocialGroupSearchBean implements Serializable{
    @EJB
    UserFacade userFacade;
    @EJB
    SocialGroupFacade groupFacade;
    @EJB
    PendingFriendshipFacade pendingFriendshipFacade;
    @EJB
    private PendingMembershipFacade pendingMembershipFacade;

    @Inject
    LoginBean loginBean;
    @Inject
    SocialGroupBean socialGroupBean;
    
    protected String searchText;
    protected List<User> userList;
    protected Map<User, Object[]> userData;
    protected User loggedUser;
    protected SocialGroup socialGroup;
    protected List<SocialGroup> groupList;
    protected Map<SocialGroup, Object[]> groupData;
    
    protected User toBeAddedFriend;
    
    /**
     * Creates a new instance of searchBean
     */
    public SocialGroupSearchBean() {
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Map<User, Object[]> getUserData() {
        return userData;
    }

    public void setUserData(Map<User, Object[]> userData) {
        this.userData = userData;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public List<SocialGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<SocialGroup> groupList) {
        this.groupList = groupList;
    }

    public Map<SocialGroup, Object[]> getGroupData() {
        return groupData;
    }

    public void setGroupData(Map<SocialGroup, Object[]> groupData) {
        this.groupData = groupData;
    }

    public User getToBeAddedFriend() {
        return toBeAddedFriend;
    }

    public void setToBeAddedFriend(User toBeAddedFriend) {
        this.toBeAddedFriend = toBeAddedFriend;
    }
    
    public String search() {
        
        init();
        return "search";
    }
    
    @PostConstruct
    public void init() {
        socialGroup = socialGroupBean.getSocialGroup();
        
        List<User> findUser = this.userFacade.findByUsername(searchText);
        userData = new TreeMap<User, Object[]>();
        List<User> members = this.groupFacade.getMembers(socialGroup);
        List<User> pendingMembers = this.groupFacade.getPendingMembers(socialGroup);

        for (User u: findUser) {
            Object[] data = new Object[2];

            //Posts
            Iterator<Post> postIt = u.getPostCollection().iterator(); 
            if (postIt.hasNext())
                data[0] = postIt.next();
            else
                data[0] = null;

            //Membership
            if (members.contains(u))
                data[1] = User.memberStatus.member;
            else if (pendingMembers.contains(u))
                data[1] = User.memberStatus.pending;
            else
                data[1] = User.memberStatus.not_member;


            userData.put(u, data);
        }
        
        this.userList = new ArrayList<User>(userData.keySet());
        
        List<SocialGroup> findGroup = this.groupFacade.findByName(searchText);
        groupData = new TreeMap<SocialGroup, Object[]>();
        List<SocialGroup> groups = this.userFacade.findSocialGroups(loggedUser);
        List<SocialGroup> pendingGroups = this.userFacade.findPendingMemberships(loggedUser);
        
        for (SocialGroup g: findGroup) {
            Object[] data = new Object[2];

            //Posts
            Iterator<Post> postIt = g.getPostCollection().iterator(); 
            if (postIt.hasNext())
                data[0] = postIt.next();
            else
                data[0] = null;

            //Membership
            if (groups.contains(g))
                data[1] = SocialGroup.membershipStatus.member;
            else if (pendingGroups.contains(g))
                data[1] = SocialGroup.membershipStatus.pending;
            else
                data[1] = SocialGroup.membershipStatus.unrelated;


            groupData.put(g, data);
        }
        
        this.groupList = new ArrayList<SocialGroup>(groupData.keySet());
    }
    
    public String getFriendButtonText(User user) {
         User.memberStatus memberStatus = (User.memberStatus)userData.get(user)[1];
         String res;
         
        switch (memberStatus) {
            case member:
                res = "searchSG.alreadyMember";
                break;
            case pending:
                res = "searchSG.invitationSent";
                break;
            default:
                res = "search.inviteMember";
                break;
        }
        
        return res;
    }
    
    
            
    public boolean getFriendButtonDisabled(User user) {
        User.friendStatus friendStatus = (User.friendStatus)userData.get(user)[1];
       
        return friendStatus != User.friendStatus.unrelated;
    }
    

    
    public String getPost(User user) {
        Post post = (Post)userData.get(user)[0];
        if (post != null)
            return Markdown.toHtml(post.getBody());
        else
            return user.getUsername() + " has not posted yet";
    }
    

    
        public String inviteMember(User user) {
        PendingMembership pending = new PendingMembership(0);
        pending.setSocialGroup(socialGroup);
        pending.setUser(user);
        pending.setInvitation(true);
        this.pendingMembershipFacade.create(pending);
        
        init();
        return "search";
    }
    
}

