/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.PostFacade;
import inTouch.entity.Post;
import inTouch.entity.SocialGroup;
import inTouch.entity.User;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author darioarrebola
 */
@Named(value = "groupPostBean")
@RequestScoped
public class GroupPostBean implements Serializable{

    @EJB
    private PostFacade postFacade;
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private SocialGroupBean socialGroupBean;
    
    @Inject
    private PostBean postBean;   
    
    protected String body;
    protected boolean isPublic=false;
    protected User user;
    protected SocialGroup sg;
    
    
    @PostConstruct
    public void setUp(){
        user = loginBean.getUser();
        sg = socialGroupBean.getSocialGroup();
    }
    
        
    public void doPostCreation(){
        Date date = new Date();
        Post post = new Post(0, date, !isPublic); //Negation of isPublic, because the parameter is isPrivate
        post.setAuthor(user);
        post.setBody(body);
        post.setSocialGroup(sg);
        postFacade.create(post);
        
        //recharge posts List
        if(isPublic){
            postBean.setPublicPostList(postFacade.getPublicPost());
        } else {
            postBean.setPrivatePostList(postFacade.getPrivatePost(user));
        }
    }
    
    public void doPostDeletion(Post post){
        postFacade.remove(post);
        
        //recharge posts List
        if(post.getPrivate1()){
            postBean.setPrivatePostList(postFacade.getPrivatePost(user));
        } else {
            postBean.setPublicPostList(postFacade.getPublicPost());
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SocialGroup getSocialGroup() {
        return sg;
    }

    public void setSocialGroup(SocialGroup sg) {
        this.sg = sg;
    }
    
    
    
}
