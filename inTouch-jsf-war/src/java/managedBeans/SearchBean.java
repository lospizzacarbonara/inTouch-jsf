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
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import markdownj.Markdown;

/**
 *
 * @author jfaldanam
 */
@Named(value = "searchBean")
@SessionScoped

public class SearchBean implements Serializable{
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
    
    protected String searchText;
    protected List<User> userList;
    protected Map<User, Object[]> userData;
    protected User loggedUser;
    protected List<SocialGroup> groupList;
    protected Map<SocialGroup, Object[]> groupData;
    
    protected User toBeAddedFriend;
    
    /**
     * Creates a new instance of searchBean
     */
    public SearchBean() {
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
        loggedUser = loginBean.getUser();
        
        List<User> findUser = this.userFacade.findByUsername(searchText);
        userData = new TreeMap<User, Object[]>();
        List<User> friends = this.userFacade.findFriends(loggedUser);
        List<User> pendingFriends = this.userFacade.findPendingFriends(loggedUser);

        for (User u: findUser) {
            Object[] data = new Object[2];

            //Posts
            Iterator<Post> postIt = u.getPostCollection().iterator(); 
            if (postIt.hasNext())
                data[0] = postIt.next();
            else
                data[0] = null;

            //Friendship
            if (friends.contains(u))
                data[1] = User.friendStatus.friends;
            else if (pendingFriends.contains(u))
                data[1] = User.friendStatus.pending;
            else
                data[1] = User.friendStatus.unrelated;


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
         User.friendStatus friendStatus = (User.friendStatus)userData.get(user)[1];
         String res;
         
        switch (friendStatus) {
            case friends:
                res = "search.alreadyFriend";
                break;
            case pending:
                res = "search.petitionSent";
                break;
            default:
                res = "search.addFriend";
                break;
        }
        
        return res;
    }
    
    public String getGroupButtonText(SocialGroup group) {
        SocialGroup.membershipStatus memberStatus = (SocialGroup.membershipStatus)groupData.get(group)[1];
        String res;
         
        switch (memberStatus) {
            case member:
                res = "search.alreadyMember";
                break;
            case pending:
                res = "search.petitionSent";
                break;
            default:
                res = "search.joinGroup";
                break;
        }
        
        return res;
    }
            
    public boolean getFriendButtonDisabled(User user) {
        User.friendStatus friendStatus = (User.friendStatus)userData.get(user)[1];
       
        return friendStatus != User.friendStatus.unrelated;
    }
    
    public boolean getGroupButtonDisabled(SocialGroup group) {
        SocialGroup.membershipStatus memberStatus = (SocialGroup.membershipStatus)groupData.get(group)[1];
       
        return memberStatus != SocialGroup.membershipStatus.unrelated;
    }
    
    public String getPost(User user) {
        Post post = (Post)userData.get(user)[0];
        if (post != null)
            return Markdown.toHtml(post.getBody());
        else
            return user.getUsername() + " has not posted yet";
    }
    
    public String getPostGroup(SocialGroup group) {
        Post post = (Post)groupData.get(group)[0];
        if (post != null)
            return Markdown.toHtml(post.getBody());
        else
            return group.getName() + " has not posted yet";
    }
    
    public String addFriend(User user) {
        PendingFriendship pending = new PendingFriendship(0);
        pending.setSender(loggedUser);
        pending.setReceiver(user);
        this.pendingFriendshipFacade.create(pending);
        
        init();
        return "search";
    }
    
    public String joinGroup(SocialGroup group) {
        PendingMembership pending = new PendingMembership(0);
        pending.setInvitation(false);
        pending.setUser(loggedUser);
        pending.setSocialGroup(group);
        this.pendingMembershipFacade.create(pending);
        
        this.init();
        return "search";
    }
}
