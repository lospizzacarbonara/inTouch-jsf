/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.UserFacade;
import inTouch.entity.Post;
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

    @Inject
    LoginBean loginBean;
    
    protected String searchText;
    protected List<User> userList;
    protected Map<User, Object[]> userData;
    protected User user;
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

    public void setUserSet(List<User> userList) {
        this.userList = userList;
    }

    public Map<User, Object[]> getUserData() {
        return userData;
    }

    public void setUserData(Map<User, Object[]> userData) {
        this.userData = userData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String search() {
        
        init();
        return "search";
    }
    
    @PostConstruct
    public void init() {
        user = loginBean.getUser();
        
        List<User> userList = null;
        userData = new TreeMap<User, Object[]>();
        List<User> friends = this.userFacade.findFriends(user);
        List<User> pendingFriends = this.userFacade.findPendingFriends(user);

        userList = this.userFacade.findByUsername(searchText);
        for (User u: userList) {
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
    }
    
    public String getFriendButtonText(User user) {
         User.friendStatus friendStatus = (User.friendStatus)userData.get(user)[1];
         String res;
         
        if (friendStatus == User.friendStatus.friends) { 
            res = "alreadyFriend";
        } else if (friendStatus == User.friendStatus.pending) {
            res = "petitionSent";
        } else {
            res = "addFriend";
        }
        
        return res;
    }
            
    public String getFriendButtonDisabled(User user) {
         User.friendStatus friendStatus = (User.friendStatus)userData.get(user)[1];
         String res;
         
        if (friendStatus == User.friendStatus.unrelated) { 
            res = "";
        } else {
            res = "disabled";
        }
        
        return res;
    }
    
    public String getPost(User user) {
        Post post = (Post)userData.get(user)[0];
        if (post != null)
            return Markdown.toHtml(post.getBody());
        else
            return user.getUsername() + " has not posted yet";
    }
}
