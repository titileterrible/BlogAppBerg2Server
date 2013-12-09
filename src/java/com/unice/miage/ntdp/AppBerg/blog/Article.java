package com.unice.miage.ntdp.AppBerg.blog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author GLYSE581B
 */
@NamedQueries({
    @NamedQuery(name = "Art.findAll", query = "SELECT a FROM Article a ORDER BY a.id"),
    @NamedQuery(name = "Art.findAllByUser", query = "SELECT a FROM Article a where a.blogUser.id = :userId"),
    @NamedQuery(name = "Art.findAllByStatus", query = "SELECT a FROM Article a where a.status  = :status"),
    @NamedQuery(name = "Art.delete", query = "delete from Article a where a.id = :aId"),
    
    @NamedQuery(name = "Art.countAll", query = "SELECT COUNT(a) FROM Article a"),
   
    @NamedQuery(name = "Art.countByStatus", query = "SELECT COUNT(a) FROM Article a where a.status  = :status"),
    @NamedQuery(name = "Art.countByUser", query = "SELECT COUNT(a) FROM Article a where a.blogUser.id = :userId"),
    @NamedQuery(name = "Art.countReportedAbused", query = "SELECT COUNT(a) FROM Article a where a.status = 1"),
    @NamedQuery(name = "Art.countWaitForValidation", query = "SELECT COUNT(a) FROM Article a where a.status = 2"),
    @NamedQuery(name = "Art.countPublished", query = "SELECT COUNT(a) FROM Article a where a.status = 0"),
    @NamedQuery(name = "Art.countByUser", query = "SELECT COUNT(a) FROM Article a where a.blogUser.id = :userId")
})
@Entity
@XmlRootElement
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String keywords;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date published_on;
    @Lob
    private String content;
    //private String photo;
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true) @Lob
    private byte[] photo;
    private String position_longitude;
    private String position_latitude;
    private String position_name;
    private Status status;
    /**
     * bidirectional An article has one to many comments. CascadeType.ALL : all
     * operations on parent will be performed on the child
     */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne
    private BlogUser blogUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Date getPublished_on() {
        return published_on;
    }

    public void setPublished_on(Date published_on) {
        this.published_on = published_on;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPosition_longitude() {
        return position_longitude;
    }

    public void setPosition_longitude(String position_longitude) {
        this.position_longitude = position_longitude;
    }

    public String getPosition_latitude() {
        return position_latitude;
    }

    public void setPosition_latitude(String position_latitude) {
        this.position_latitude = position_latitude;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @XmlTransient
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
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
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Article[ id=" + id + " ]";
    }
}
