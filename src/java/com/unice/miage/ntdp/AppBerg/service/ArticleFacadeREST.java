/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.service;

import com.unice.miage.ntdp.AppBerg.blog.Article;
import com.unice.miage.ntdp.AppBerg.blog.BlogUser;
import com.unice.miage.ntdp.AppBerg.blog.Status;
import com.unice.miage.ntdp.AppBerg.util.CodeErreurEnum;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author GLYSE581B
 */
@Stateless
@Path("/article")
public class ArticleFacadeREST extends AbstractFacade<Article> {

    @PersistenceContext(unitName = "BlogAppBerg2PU")
    private EntityManager em;

    public ArticleFacadeREST() {
        super(Article.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Article entity) {
        super.create(entity);
    }

    //Test Ok 
    //mais ??????????? N'accepte pas de renvoyer un String pour un put avec param
    //On renvoi plutot l'article modifié , avec le pssw et uuid de user vide
    //ou void si tu veux
    //On ne peut pas faire un post avec  @Path
    @PUT
    @Consumes({"application/xml", "application/json"})
    @Path("put/create/{suuid}")
    //@Produces({MediaType.TEXT_PLAIN})
    public void createArticle(Article article, @PathParam("suuid") String suuid) {
        String createResult = CodeErreurEnum.Failure.toString();
        try {
            BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
            if (theUserInSession != null) {
                super.create(article);
                createResult = CodeErreurEnum.Succes.toString();
            }
        } catch (Exception e) {
            throw new WebApplicationException("createResult",e, Response.Status.INTERNAL_SERVER_ERROR);
            
        }
       // return createResult;
    }

    /**
     *
     * @param article
     * @param suuid
     * @return
     */
    @DELETE //Test OK
    @Path("/delete/{id}/{suuid}")
    //@Produces({MediaType.TEXT_PLAIN})
    public String deleteArticle(@PathParam("id") Long id, @PathParam("suuid") String suuid) {
        String deleteResult = CodeErreurEnum.Failure.toString();
        Article toDelete = super.find(id);
        if (toDelete != null) {
            try {
                BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
                if (theUserInSession != null && (theUserInSession.isAdmin() || toDelete.getBlogUser().getId() == theUserInSession.getId())) {
                    super.remove(toDelete);
                    deleteResult = CodeErreurEnum.Succes.toString();
                }
            } catch (Exception e) {
                throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return deleteResult;
    }

    //Test Ok 
    //mais ??????????????????????????????????? N'accepte pas de renvoyer un String pour un put avec param
    //On renvoi plutot l'article modifié , avec le pssw et uuid de user vide
    //ou void si tu veux
    @PUT 
    @Path("/put/edit/{suuid}")
    @Consumes({"application/xml", "application/json"})
    //@Produces({MediaType.TEXT_PLAIN})
    public Article editArticleArticle (Article article, @PathParam("suuid") String suuid){
        //String editResult = CodeErreurEnum.Failure.toString();
        Article toReturn =new Article();
        Long id;
        try {
            id = article.getId();
            if (id == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            try {
                BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
                if (theUserInSession != null && (theUserInSession.isAdmin() || id == theUserInSession.getId())) {
                    super.edit(article);
                    //editResult = CodeErreurEnum.Succes.toString();
                    toReturn=article;
                }
                em.flush();
                em.detach(article.getBlogUser());
                article.getBlogUser().setSessionUniqueUserID("");
                article.getBlogUser().setPassword("");
//                article.setKeywords(CodeErreurEnum.Succes.toString());
//                article.setTitle(CodeErreurEnum.Succes.toString());
//                article.setContent(CodeErreurEnum.Succes.toString());
                
            } catch (Exception e) {
                throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (WebApplicationException | IllegalArgumentException e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }   
        return toReturn;
    }    
    

    @GET //Test ok : entrez 0, 1 ou 2 avec TexPlain en sortie
    @Path("/article/{status}") 
    @Produces({"application/xml", "application/json"})
    public List<Article> getListeArticlesByStatus(@PathParam("status") int status) {
        List<Article> toReturn = new ArrayList<>();

        try {
            if (status == 0) {
                toReturn = em.createNamedQuery("Art.findAllByStatus", Article.class).setParameter("status", Status.Published).getResultList();
            } else {
                if (status == 1) {
                    toReturn = em.createNamedQuery("Art.findAllByStatus", Article.class).setParameter("status", Status.ReportAsAbused).getResultList();
                } else {
                    if (status == 2) {
                        toReturn = em.createNamedQuery("Art.findAllByStatus", Article.class).setParameter("status", Status.WaitForValidation).getResultList();
                    }
                }
            }

        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return toReturn;
    }

    @GET //Test ok
    @Path("/user/{userId}")
    @Produces({"application/xml", "application/json"})
    public List<Article> getListeArticlesByUser(@PathParam("userId") int userId) {
        Query query;
        try {
            query = em.createNamedQuery("Art.findAllByUser", Article.class).setParameter("userId", userId);

        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return query.getResultList();
    }

    @GET //Test OK 
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Article getArticle(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET //Test OK 
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Article> findAll() {
        return super.findAll();
    }

    @GET //Test OK 
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Article> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET  //Test OK 
    @Path("count")
    @Produces("text/plain")
    public String countAllArticles() {
        return String.valueOf(super.count());
    }

    //Test OK : entrez 1 ou 2 ou 3 pour status
    @GET
    @Path("count/status/{status}")
    @Produces("text/plain")
    public String countArticlesByStatus(@PathParam("status") int status) {
        Query query = null;
        try {
            if (status == 0) {
                query = em.createNamedQuery("Art.countByStatus", Article.class).setParameter("status", Status.Published);
            } else {
                if (status == 1) {
                    query = em.createNamedQuery("Art.countByStatus", Article.class).setParameter("status", Status.ReportAsAbused);
                } else {
                    if (status == 2) {
                        query = em.createNamedQuery("Art.countByStatus", Article.class).setParameter("status", Status.WaitForValidation);
                    }
                }
            }

        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        if (query.getSingleResult() != null) {
            return String.valueOf(query.getSingleResult());
        }
        return String.valueOf(0);
    }

    @GET //Test Ok
    @Path("count/user/{userId}")
    @Produces("text/plain")
    public String countArticlesByUser(@PathParam("userId") int userId) {
        Query query;
        try {
            query = em.createNamedQuery("Art.countByUser", Article.class).setParameter("userId", userId);

        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return String.valueOf(query.getSingleResult());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
