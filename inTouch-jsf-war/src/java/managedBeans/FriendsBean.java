/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.FriendshipFacade;
import inTouch.ejb.PendingFriendshipFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.Friendship;
import inTouch.entity.PendingFriendship;
import inTouch.entity.Post;
import inTouch.entity.User;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author jfaldanam
 */
@Named(value = "friendsBean")
@RequestScoped
public class FriendsBean {
    
    @Inject
    LoginBean loginBean;
    
    @EJB
    private UserFacade userFacade;
    @EJB
    private PendingFriendshipFacade pendingFriendshipFacade;
    @EJB
    private FriendshipFacade friendshipFacade;
    
    protected User loggedUser;
    protected List<User> friendList;
    protected List<User> pendingFriendList;
    protected List<User> pendingToAcceptFriendList;

    /**
     * Creates a new instance of FriendsBean
     */
    public FriendsBean() {
    }

    public List<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
    }

    public List<User> getPendingFriendList() {
        return pendingFriendList;
    }

    public void setPendingFriendList(List<User> pendingFriendList) {
        this.pendingFriendList = pendingFriendList;
    }

    public List<User> getPendingToAcceptFriendList() {
        return pendingToAcceptFriendList;
    }

    public void setPendingToAcceptFriendList(List<User> pendingToAcceptFriendList) {
        this.pendingToAcceptFriendList = pendingToAcceptFriendList;
    }
    
    @PostConstruct
    public void init() {
        loggedUser = loginBean.getUser();
        
        this.friendList = this.userFacade.findFriends(loggedUser);       
        this.pendingFriendList = this.userFacade.findPendingFriends(loggedUser);        
        this.pendingToAcceptFriendList = this.userFacade.findPendingToAcceptFriends(loggedUser);
    }
    
    public String accept(User user) {
        List<PendingFriendship> pendingFriendships = this.pendingFriendshipFacade.findPendingFriendship(user, loggedUser);
        for (PendingFriendship pendingFriendship: pendingFriendships)
            this.pendingFriendshipFacade.remove(pendingFriendship);
        
        Friendship friendship = new Friendship(0);
        friendship.setFriend1(loggedUser);
        friendship.setFriend2(user);
        this.friendshipFacade.create(friendship);
        
        Friendship friendship2 = new Friendship(0);
        friendship2.setFriend1(user);
        friendship2.setFriend2(loggedUser);
        this.friendshipFacade.create(friendship2);
        
        this.init();
        
        return "friends";
    }
    
    public String deny(User user) {
        this.cancel(user);
        
        return "friends";
    }
        
    public String cancel(User user) {
        List<PendingFriendship> pendingFriendships = this.pendingFriendshipFacade.findPendingFriendship(user, loggedUser);
        pendingFriendships.addAll(this.pendingFriendshipFacade.findPendingFriendship(loggedUser, user));
        for (PendingFriendship pendingFriendship: pendingFriendships)
            this.pendingFriendshipFacade.remove(pendingFriendship);
        
        this.init();
        
        return "friends";
    }
    
    public String remove(User user) {
        List<Friendship> friendships = this.friendshipFacade.findFriendshipBetweenFriends(loggedUser, user);
        for (Friendship friendship: friendships)
            this.friendshipFacade.remove(friendship);
        
        this.init();
        
        return "friends";
    }
    
}
