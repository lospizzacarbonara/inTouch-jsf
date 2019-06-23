/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.PostFacade;
import inTouch.ejb.SocialGroupFacade;
import inTouch.ejb.UserFacade;
import inTouch.entity.Post;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author darioarrebola
 */
@Named(value = "socialGroupBean")
@RequestScoped
public class SocialGroupBean implements Serializable {

    @EJB
    private UserFacade userFacade;

    @EJB
    private SocialGroupFacade socialGroupFacade;
    
    @EJB
    private PostFacade postFacade;
    @Inject
    private LoginBean loginBean;
    
    protected SocialGroup socialGroup;
    protected List<User> userList;
    protected List<Post> groupPostList;
    protected String name;
    
    
    public String doSelectGroup(int id){
        
        SocialGroup sg = this.socialGroupFacade.find(id);
        //socialGroup=sg;
        loginBean.setCurrentSg(sg);
        groupPostList=this.postFacade.getGroupPost(loginBean.getCurrentSg());
        userList=this.userFacade.getUserList(loginBean.getCurrentSg());
        return "groupWall";
    }
    @PostConstruct
    public void init(){
        //coger la id del xhtml
        groupPostList=this.postFacade.getGroupPost(loginBean.getCurrentSg());
        userList=this.userFacade.getUserList(loginBean.getCurrentSg());
    }

    public void setSocialGroup(SocialGroup socialGroup) {
        this.socialGroup = socialGroup;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void setGroupPostList(List<Post> groupPostList) {
        this.groupPostList = groupPostList;
    }
    
    public SocialGroup getSocialGroup() {
        return socialGroup;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<Post> getGroupPostList() {
        return groupPostList;
    }
    
    public String getDescription(){
        return loginBean.getCurrentSg().getDescription();
    }
    
    public String getName(){
        return loginBean.getCurrentSg().getName();
    }
    
    
    

    /**
     * Creates a new instance of socialGroupBean
     */
    public SocialGroupBean() {
    }
    
}
