/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.SocialGroupFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.Post;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import markdownj.Markdown;

/**
 *
 * @author jfaldanam
 */
@Named(value = "searchBean")
@RequestScoped
public class SearchBean {
    @EJB
    UserFacade userFacade;
    @EJB
    SocialGroupFacade groupFacade;

    @Inject
    LoginBean loginBean;
    
    protected String searchText;
    protected List<User> userList;
    protected Map<User, Object[]> userData;
    protected User loggedUser;
    protected List<SocialGroup> groupList;
    protected Map<SocialGroup, Object[]> groupData;
    
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
                res = "Already Friend";
                break;
            case pending:
                res = "Petition Sent";
                break;
            default:
                res = "Add Friend";
                break;
        }
        
        return res;
    }
    
    public String getGroupButtonText(SocialGroup group) {
        SocialGroup.membershipStatus memberStatus = (SocialGroup.membershipStatus)groupData.get(group)[1];
        String res;
         
        switch (memberStatus) {
            case member:
                res = "Already Member";
                break;
            case pending:
                res = "Petition Sent";
                break;
            default:
                res = "Join Group";
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
}
