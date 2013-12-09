/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.facade;

import com.unice.miage.ntdp.AppBerg.blog.Article;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author GLYSE581B
 */
@Stateless
public class ArticleFacade extends AbstractFacade<Article> {
    @PersistenceContext(unitName = "BlogAppBerg2PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArticleFacade() {
        super(Article.class);
    }
    
}
