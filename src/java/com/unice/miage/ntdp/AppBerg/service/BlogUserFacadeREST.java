/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.service;

import com.unice.miage.ntdp.AppBerg.blog.BlogUser;
import com.unice.miage.ntdp.AppBerg.util.CodeErreurEnum;
import com.unice.miage.ntdp.AppBerg.util.IdGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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
@Path("/bloguser")
public class BlogUserFacadeREST extends AbstractFacade<BlogUser> {

    @PersistenceContext(unitName = "BlogAppBerg2PU")
    private EntityManager em;

    public BlogUserFacadeREST() {
        super(BlogUser.class);
    }

    /**
     * Une méthode permettant de retrouver un utilisateur authentifié
     *
     * @param login login de l'utilisateur
     * @param password password de l'utilisateur
     * @return String le sessioId ou um message d'erreur
     */
    @GET  //TestOK!!!!
    @Path("/authentication/{login}/{password}")
    @Produces({MediaType.TEXT_PLAIN})
    public String authentification(@PathParam("login") String login, @PathParam("password") String password) {
        BlogUser theUser = null;
        String toReturn = CodeErreurEnum.Unregistered.toString();
        try {
            theUser = em.createNamedQuery("User.findByLoginPass", BlogUser.class).setParameter("username", login).setParameter("password", password).getSingleResult();
            System.out.println("theUser found " + theUser.getUsername() + " ***** " + theUser.getPassword() + " ***** " + theUser.getRole().getName());
        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        theUser.setLast_connected(new Date()); // on modifie la date de dernière connection
        theUser.setSessionUniqueUserID(new IdGenerator().generateId(20));
        try {
            em.merge(theUser);// On sauvegarde la modif en base
            toReturn = theUser.getSessionUniqueUserID();
        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return toReturn; // On retourne le Token SUUID sous forme de string
    }

    //TestOK!!!!
    @GET
    @Path("/user/{suuid}")
    @Produces({MediaType.TEXT_PLAIN})
    public String isAdmin(@PathParam("suuid") String suuid) {
        String toReturn = CodeErreurEnum.Failure.toString();
        BlogUser admin =null;    
            try {
               admin = em.createNamedQuery("User.getAdminByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
            } catch (Exception e) {
                return toReturn;
            }         
        if (admin != null) {
            return CodeErreurEnum.Succes.toString();
        }
        return toReturn;
    }

    @GET //Test OK
    @Path("/user/{suuid}/{userId}")
    @Produces({"application/xml", "application/json"})
    public BlogUser getUserByIdForAppropriateRoleInSession(@PathParam("userId") int userId, @PathParam("suuid") String suuid) {
        BlogUser theUser = null;
        BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", suuid).getSingleResult();
        if (theUserInSession != null) {
            try {
                theUser = super.find(Long.valueOf(userId));
            } catch (Exception e) {
                throw new WebApplicationException(e,
                        Response.Status.INTERNAL_SERVER_ERROR);
            }

        } else {
            throw new NotFoundException("<error>No user with suuid: " + suuid + "</error>");
        }

        if(theUserInSession.getRole().getName().equals("Admin")){
            return theUser;
        }else{
                em.flush();
                em.detach(theUser);
                theUser.setPassword("");
                theUser.setSessionUniqueUserID("");
        }
//        switch (theUserInSession.getRole().getName()) {        
//            case "Admin":
//                return theUser;
//            case "User":
//                em.flush();
//                em.detach(theUser);
//                theUser.setPassword("");
//                theUser.setSessionUniqueUserID("");
//                break;
//        }

        return theUser;
    }

    @GET //Test OK : retour passw et uuid blanc
    @Produces({"application/xml", "application/json"})
    public List<BlogUser> getListeUsers() {
        List<BlogUser> ListBlogUserWithoutUuidAndPasswd = new ArrayList<>();
        List<BlogUser> ListBlogUserWithUuidAndPasswd = new ArrayList<>();
        try {
            ListBlogUserWithUuidAndPasswd = super.findAll();
        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        for (BlogUser user : ListBlogUserWithUuidAndPasswd) {
            em.flush();
            em.detach(user);
            user.setSessionUniqueUserID("");
            user.setPassword("");
            ListBlogUserWithoutUuidAndPasswd.add(user);
        }
        return ListBlogUserWithoutUuidAndPasswd;
    }

    //Test Ok  ??????????????? createAccount le meme que createUser 
    @POST
    @Consumes({"application/xml", "application/json"})
    public void createAccount(BlogUser user) {
        try {
            super.create(user);
        } catch (Exception e) {
            throw new WebApplicationException("Failure", e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT //Test ok
    @Consumes({"application/xml", "application/json"})
    @Produces({MediaType.TEXT_PLAIN})
    public String edtitUser(BlogUser user) {
        String editResult = CodeErreurEnum.Failure.toString();
        Long id;
        try {
            id = user.getId();
            if (id == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            try {
                BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", user.getSessionUniqueUserID()).getSingleResult();
                if (theUserInSession != null && (theUserInSession.isAdmin() || id == theUserInSession.getId())) {
                    super.edit(user);
                    editResult = CodeErreurEnum.Succes.toString();
                }

            } catch (Exception e) {
                throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (WebApplicationException | IllegalArgumentException e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return editResult;
    }

    @DELETE //Test OK
    @Path("{id}")
    @Produces({MediaType.TEXT_PLAIN})
    public String deleteUser(@PathParam("id") Long id) {
        String deleteResult = CodeErreurEnum.Failure.toString();
        BlogUser user;
        try {
            user = super.find(id);
            if (user == null) {
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            try {
                BlogUser theUserInSession = em.createNamedQuery("User.getUserByUUid", BlogUser.class).setParameter("suuid", user.getSessionUniqueUserID()).getSingleResult();
                if (theUserInSession != null && (theUserInSession.isAdmin() || id == theUserInSession.getId())) {
                    super.remove(user);
                    deleteResult = CodeErreurEnum.Succes.toString();
                }
            } catch (Exception e) {
                throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
            }

        } catch (WebApplicationException | IllegalArgumentException e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return deleteResult;

    }
    
    @GET //TestOK
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public BlogUser find(@PathParam("id") Long id) {
        BlogUser theUser;
        try {
            theUser = super.find(id);
            if (theUser == null) {
                return null;
            } else {
                em.flush();
                em.detach(theUser);
                theUser.setPassword("");//renvoie un Password vide
                theUser.setSessionUniqueUserID("");//renvoie un uuid vide
                return theUser;
            }

        } catch (Exception e) {
            throw new WebApplicationException("Failure", e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET //TestOK!!!!
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<BlogUser> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {

        List<BlogUser> ListBlogUserWithoutUuidAndPasswd = new ArrayList<>();
        List<BlogUser> ListBlogUserWithUuidAndPasswd = new ArrayList<>();
        try {
            ListBlogUserWithUuidAndPasswd = super.findRange(new int[]{from, to});
        } catch (Exception e) {
            throw new WebApplicationException(e,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        for (BlogUser user : ListBlogUserWithUuidAndPasswd) {
            em.flush();
            em.detach(user);
            user.setSessionUniqueUserID("");// vide
            user.setPassword("");//renvoie un Password vide
            ListBlogUserWithoutUuidAndPasswd.add(user);
        }
        return ListBlogUserWithoutUuidAndPasswd;
    }

    @GET //TestOK!!!!
    @Path("count")
    @Produces({MediaType.TEXT_PLAIN})
    public String countUsers() {
        int toReturn = 0;
        try {
            toReturn = super.count();
        } catch (Exception e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return String.valueOf(toReturn);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
