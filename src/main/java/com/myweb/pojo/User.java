    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package com.myweb.pojo;

    import jakarta.persistence.Basic;
    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.NamedQueries;
    import jakarta.persistence.NamedQuery;
    import jakarta.persistence.OneToOne;
    import jakarta.persistence.Table;
    import jakarta.persistence.Temporal;
    import jakarta.persistence.TemporalType;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
    import java.io.Serializable;
    import java.util.Date;

    /**
     *
     * @author dat
     */
    @Entity
    @Table(name = "user")
    @NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
        @NamedQuery(name = "User.findByRegisterDate", query = "SELECT u FROM User u WHERE u.registerDate = :registerDate"),
        @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
        @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive")})
    public class User implements Serializable {

        private static final long serialVersionUID = 1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Basic(optional = false)
        @Column(name = "id")
        private Integer id;
        @Basic(optional = false)
        @NotNull
        @Size(min = 1, max = 50)
        @Column(name = "username")
        private String username;
        @Basic(optional = false)
        @NotNull
        @Size(min = 1, max = 200)
        @Column(name = "password")
        private String password;
        @Basic(optional = false)
        @NotNull
        @Column(name = "register_date")
        @Temporal(TemporalType.TIMESTAMP)
        private Date registerDate;
        @Basic(optional = false)
        @NotNull
        @Size(min = 1, max = 40)
        @Column(name = "role")
        private String role;
        @Basic(optional = false)
        @NotNull
        @Column(name = "is_active")
        private boolean isActive;
        @OneToOne(cascade = CascadeType.ALL, mappedBy = "userId")
        private Candidate candidate;
        @OneToOne(cascade = CascadeType.ALL, mappedBy = "userId")
        private Company company;

        public User() {
        }

        public User(Integer id) {
            this.id = id;
        }

        public User(Integer id, String username, String password, Date registerDate, String role, boolean isActive) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.registerDate = registerDate;
            this.role = role;
            this.isActive = isActive;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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

        public Date getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(Date registerDate) {
            this.registerDate = registerDate;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public Candidate getCandidate() {
            return candidate;
        }

        public void setCandidate(Candidate candidate) {
            this.candidate = candidate;
        }

        public Company getCompany() {
            return company;
        }

        public void setCompany(Company company) {
            this.company = company;
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
            if (!(object instanceof User)) {
                return false;
            }
            User other = (User) object;
            if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "com.myweb.pojo.User[ id=" + id + " ]";
        }

    }
