/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import inTouch.ejb.PostFacade;
import inTouch.entity.Post;
import inTouch.entity.User;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import markdownj.Markdown;

/**
 *
 * @author Nellogy
 */
@Named(value = "postBean")
@RequestScoped
public class PostBean {

    @EJB
    private PostFacade postFacade;
    
    @Inject
    private LoginBean loginBean;
    
    protected User user;
    protected List<Post> publicPostList, privatePostList;

    /**
     * Creates a new instance of PostBean
     */
    public PostBean() {}
    
    @PostConstruct
    public void setUp() {
        user = loginBean.getUser();
        publicPostList = postFacade.getPublicPost();
        privatePostList = postFacade.getPrivatePost(user);
    }
    
    public String parsePost(String body) {
        if (body != null)
            return Markdown.toHtml(body);
        else
            return null;
    }
    
    /*
    * --- SETTERS ---
    */
    public void setUser(User user) {
        this.user = user;
    }
         
    public void setPublicPostList(List<Post> publicPostList) {
        this.publicPostList = publicPostList;
    }   

    public void setPrivatePostList(List<Post> privatePostList) {
        this.privatePostList = privatePostList;
    }
    
    /*
    * --- SETTERS ---
    */
    public User getUser() {
        return user;
    }
    
    public List<Post> getPublicPostList() {
        return publicPostList;
    }

     public List<Post> getPrivatePostList() {
        return privatePostList;
    }
    
}
