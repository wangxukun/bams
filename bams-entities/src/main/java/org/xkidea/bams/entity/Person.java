package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PERSON")
// TODO @NamedQueries
@NamedQueries({
        @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email")
})
public class Person implements Serializable {

    private static final long serialVersionUID = 7728610399338717149L;
    @JoinTable(
            name = "PERSON_GROUPZ",
            joinColumns = {@JoinColumn(name = "EMAIL",referencedColumnName = "EMAIL")},
            inverseJoinColumns = {@JoinColumn(name = "GROUP_ID",referencedColumnName = "NAME")})
    @ManyToMany
    protected List<Groups> groupsList;

    @JoinTable(
            name="PERSON_GENERALACCOUNT",
            joinColumns=
            @JoinColumn(name="EMAIL", referencedColumnName="EMAIL"),
            inverseJoinColumns=
            @JoinColumn(name="ACCOUNT_ID", referencedColumnName="ID")
    )
    @ManyToMany
    protected List<GeneralAccount> accountList;

    @JoinTable(
            name="PERSON_AREA",
            joinColumns=
            @JoinColumn(name="EMAIL", referencedColumnName="EMAIL"),
            inverseJoinColumns=
            @JoinColumn(name="AREA_ID", referencedColumnName="ID")
    )
    @ManyToMany(fetch=FetchType.EAGER)
    protected List<Area> areaList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    protected Integer id;
    @Basic(optional = false)
    @Size(min=2, max=50, message="{person.name}")
    @Column(name = "NAME")
    protected String name;
    @Basic(optional = false)
    @Column(name = "PHONE")
    protected String phone;
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "{person.email}")
    @Size(min = 3, max = 45, message = "{person.email}")
    @Basic(optional = false)
    @Column(name = "EMAIL", unique = true)
    protected String email;
    @Basic(optional = false)
    @Size(min = 6, max = 100, message = "{person.password}")
    @Column(name = "PASSWORD")
    protected String password;
    @Basic(optional = false)
    @Size(min = 2, max = 100, message = "{person.organization")
    @Column(name = "ORGANIZATION")
    protected String organization;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED")
    protected Date dateCreated;

    public Person() {
        groupsList = new ArrayList<>();
        accountList = new ArrayList<>();
        areaList = new ArrayList<>();
    }

    public Person(Integer id) {
        this.id = id;
        groupsList = new ArrayList<>();
        accountList = new ArrayList<>();
        areaList = new ArrayList<>();
    }

    public Person(Integer id, @Size(min = 2, max = 50, message = "{person.name}") String name, String phone, @Pattern(regexp = ".+@.+\\.[a-z]+", message = "{person.email}") @Size(min = 3, max = 45, message = "{person.email}") String email, @Size(min = 6, max = 100, message = "{person.password}") String password, @Size(min = 2, max = 100, message = "{person.organization") String organization, @NotNull Date dateCreated) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.organization = organization;
        this.dateCreated = dateCreated;
        groupsList = new ArrayList<>();
        accountList = new ArrayList<>();
        areaList = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Add XmlTransient annotation to this field for security reasons.
     * @return the password
     */
    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Groups> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }

    public List<GeneralAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<GeneralAccount> accountList) {
        this.accountList = accountList;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public void addArea(Area area) {
        this.getAreaList().add(area);
    }

    public void dropArea(Area area) {
        this.getAreaList().remove(area);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
