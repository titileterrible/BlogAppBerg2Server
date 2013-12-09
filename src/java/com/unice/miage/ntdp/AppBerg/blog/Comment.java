package com.unice.miage.ntdp.AppBerg.blog;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author GLYSE581B
 */
@NamedQueries({
    @NamedQuery(name = "findAll", query = "SELECT c FROM Comment c ORDER BY c.id"),
    @NamedQuery(name = "findByArticle", query = "SELECT c FROM Comment c where c.article.id = :artId"),
    @NamedQuery(name = "delete", query = "delete from Comment c where c.id = :cId"),
    @NamedQuery(name = "countByUser", query = "SELECT COUNT(c) FROM Comment c where c.blogUser.id = :userId"),
    @NamedQuery(name = "count", query = "SELECT COUNT(c) FROM Comment c")
})
@Entity
@XmlRootElement
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String comment_title;
    @Column(nullable = false)
    @Lob
    private String comment_body;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date comment_date;
    /*
     * Exple  
     * Many-To-One / One-To-Many Bidirectional
     * 
     newComment = new Comment();
     user->getComments()->add(newComment);
     newComment->setBlogUser(blogUser);
     em->persist(newComment);
     * 
     * 
     * Remove by Elements
     * user->getComments()->removeElement(comment);
     * comment->setBlogUser(null);
     */
    /**
     * Bidirectional - Many Comments are authored by one user (OWNING SIDE) An
     * BlogUser 'authors' zero to many Comments. An Comments is 'authored by'
     * only one BlogUser.
     */
    @ManyToOne
    @JoinColumn(name = "BLOGUSER_ID")
    private BlogUser blogUser;
    /*bidirectional 
     * Comment is now the owning side of the relationship
     * The comment will store what article it belongs to
     * comment.setArticle(article) before persist(comment)
     * 
     */
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment_title() {
        return comment_title;
    }

    public void setComment_title(String comment_title) {
        this.comment_title = comment_title;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Comment[ id=" + id + " ]";
    }
}