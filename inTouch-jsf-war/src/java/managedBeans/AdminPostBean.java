/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.PostFacade;
import inTouch.entity.Post;
import inTouch.entity.User;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Nellogy
 */
@Named(value = "adminPostBean")
@RequestScoped
public class AdminPostBean {

    @EJB
    private PostFacade postFacade;
    
    @Inject
    private LoginBean loginBean;
    
    @Inject
    private PostBean postBean;
    
    protected String body;
    protected boolean isPublic;
    protected User user;
    
    /**
     * Creates a new instance of createPostBean
     */
    public AdminPostBean() {}
    
    @PostConstruct
    public void setUp(){
        user = loginBean.getUser();
    }
    
    public void doPostCreation(){
        Date date = new Date();
        Post post = new Post(0, date, !isPublic); //Negation of isPublic, because the parameter is isPrivate
        post.setAuthor(user);
        post.setBody(body);
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
    }
    
    /*
    * --- SETTERS ---
    */
    public void setUser(User user) {
        this.user = user;
    }
         
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }   

    public void setBody(String body) {
        this.body = body;
    }
    
    /*
    * --- SETTERS ---
    */
    public User getUser() {
        return user;
    }
    
    public boolean getIsPublic() {
        return isPublic;
    }

     public String getBody() {
        return body;
    }
}
