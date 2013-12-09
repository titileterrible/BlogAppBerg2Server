/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.service;

import com.unice.miage.ntdp.AppBerg.blog.BlogUser;
import com.unice.miage.ntdp.AppBerg.blog.Comment;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author GLYSE581B
 */
@Stateless
@Path("/comment")
public class CommentFacadeREST extends AbstractFacade<Comment> {

    @PersistenceContext(unitName = "BlogAppBerg2PU")
    private EntityManager em;

    public CommentFacadeREST() {
        super(Comment.class);
    }

    @POST //Test OK
    @Consumes({"application/xml", "application/json"})
    public String createComment(Comment entity) {
        String createResult = CodeErreurEnum.Failure.toString();
        try {
            super.create(entity);
            createResult = CodeErreurEnum.Succes.toString();
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return createResult;
    }

    /**
     *
     * @param Comment
     * @param suuid
     * @return
     */
    @DELETE //Test OK
    @Path("/delete/{id}/{suuid}")
    public String deleteComment(@PathParam("id") Long id, @PathParam("suuid") String suuid) {
        String deleteResult = CodeErreurEnum.Failure.toString();
        Comment toDelete = super.find(id);
        BlogUser theUserInSession;
        if (toDelete != null) {
            try {

                try {
                    theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
                } catch (Exception e) {
                     return deleteResult+" No such uuid"+suuid;//Ou tout simpement Failure : au choix ?????
                }

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
    //mais ???????? N'accepte pas de renvoyer un String pour un put avec param
    //On renvoi plutot le comment modifié , avec le pssw et uuid de user vide
    //ou void si tu veux
    @PUT
    @Path("edit/user/{suuid}")
    @Consumes({"application/xml", "application/json"})
    //@Produces({MediaType.TEXT_PLAIN})
    public Comment editComment(Comment comment, @PathParam("suuid") String suuid) {
        Comment toReturn = new Comment();
        //String editResult = CodeErreurEnum.Failure.toString();
        try {
            BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
            if (theUserInSession != null && (theUserInSession.isAdmin() || comment.getBlogUser().getId() == theUserInSession.getId())) {
                super.edit(comment);
                toReturn = comment;
                //editResult = CodeErreurEnum.Succes.toString();
            }
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return toReturn;
    }

    //Test Ok
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Comment find(@PathParam("id") Long id) {
        return super.find(id);
    }

    //Test Ok
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Comment> findAll() {
        return super.findAll();
    }

    //Test Ok
    @GET
    @Path("/article/{artId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Comment> getListeCommentairesByArticle(@PathParam("artId") int artId) {
        List<Comment> returnComments = new ArrayList<>();
        try {
            returnComments = em.createNamedQuery("findByArticle", Comment.class).setParameter("artId", artId).getResultList();
        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return returnComments;
    }

    //Test Ok
    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Comment> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    //Test Ok
    @GET
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public String countCommentaire() {
        return String.valueOf(super.count());
    }

    //test Ok
    @GET
    @Path("count/{userId}")
    @Produces({MediaType.TEXT_PLAIN})
    public String countCommentairesByUser(@PathParam("userId") Long userId) {
        Query query;
        try {
            query = em.createNamedQuery("countByUser", Comment.class).setParameter("userId", userId);

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
