/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Nellogy
 */
@Named(value = "postBean")
@Dependent
public class PostBean {

    /**
     * Creates a new instance of PostBean
     */
    public PostBean() {
    }
    
}
