/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unice.miage.ntdp.AppBerg.blog;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author GLYSE581B
 */
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM BlogUser u ORDER BY u.id"),
    @NamedQuery(name = "User.delete", query = "delete from BlogUser u where u.id = :uId"),
    
    
    
    @NamedQuery(name = "User.findByLoginPass", query = "SELECT u FROM BlogUser u  where u.username = :username and  u.password = :password "),
    @NamedQuery(name = "User.getUserByUUid", query = "SELECT u FROM BlogUser u  where u.sessionUniqueUserID = :suuid "),
    @NamedQuery(name = "User.getAdminByUUid", query = "SELECT u FROM BlogUser u  where u.sessionUniqueUserID = :suuid and  u.role.name = 'Admin' "),
    @NamedQuery(name = "User.getUserByUUidAndRoleName", query = "SELECT u FROM BlogUser u  where u.sessionUniqueUserID = :suuid and  u.role.name = :roleName "),
    
    @NamedQuery(name = "User.Count", query = "SELECT COUNT(u) FROM BlogUser u")
})
@Entity
@XmlRootElement
public class BlogUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sessionUniqueUserID; //IdGenerator.generateId( length)
    //UUID uuid;
    private String firstname;
    private String lastname;
    private String about;
    //private String photo;
    
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true) @Lob
    private byte[] photo;
    
    @Column(name = "BLOGUSER_USERNAME", nullable = false, unique = true)
    private String username;
    @Column(name = "BLOGUSER_PASSWORD", nullable = false, unique = true)
    private String password;
    private String email;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date last_connected;
    private UserStatus userStatus;
   
    @OneToOne
    @JoinColumn(name = "ROLE_ID", nullable = false)
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionUniqueUserID() {
        return sessionUniqueUserID;
    }

    public void setSessionUniqueUserID(String sessionUniqueUserID) {
        this.sessionUniqueUserID = sessionUniqueUserID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLast_connected() {
        return last_connected;
    }

    public void setLast_connected(Date last_connected) {
        this.last_connected = last_connected;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAdmin() {
        boolean toReturn = false;
        if ((this!=null&&this.role.getName().equals("Admin"))) {
            toReturn = true;
        }
        return toReturn;

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
        if (!(object instanceof BlogUser)) {
            return false;
        }
        BlogUser other = (BlogUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BlogUser[ id=" + id + " ]";
    }
}
