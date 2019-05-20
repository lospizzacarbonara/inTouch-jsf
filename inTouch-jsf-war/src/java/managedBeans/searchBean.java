/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.UserFacade;
import inTouch.entity.Post;
import inTouch.entity.User;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author jfaldanam
 */
@Named(value = "searchBean")
@RequestScoped
public class searchBean {
    @EJB
    UserFacade userFacade;

    protected String searchText;
    protected Set<User> userSet;
    /**
     * Creates a new instance of searchBean
     */
    public searchBean() {
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
    
    public String search() {
        
        
        return "search";
    }
    
    @PostConstruct
    public void init() {
        List<User> userList = null;
        Map<User, Object[]> userData = new TreeMap<User, Object[]>();
        List<User> friends = this.userFacade.findFriends(new User(99));
        List<User> pendingFriends = this.userFacade.findPendingFriends(new User(99));
        
        if (searchText != null) {
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
            
            userSet = userData.keySet();
        } else {
            userData = null;
        }
        
        
    }
}
