/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.facade;

import com.unice.miage.ntdp.AppBerg.blog.Comment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author GLYSE581B
 */
@Stateless
public class CommentFacade extends AbstractFacade<Comment> {
    @PersistenceContext(unitName = "BlogAppBerg2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommentFacade() {
        super(Comment.class);
    }
    
}
