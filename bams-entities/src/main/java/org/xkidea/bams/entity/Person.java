package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PERSON")
// TODO @NamedQueries
public class Person implements Serializable {

    private static final long serialVersionUID = 7728610399338717149L;
    @JoinTable(name = "PERSON_GROUPZ",
                joinColumns = {@JoinColumn(name = "EMAIL",referencedColumnName = "EMAIL")},
                inverseJoinColumns = {@JoinColumn(name = "GROUPZ_ID",referencedColumnName = "ID")})
    @ManyToMany
    protected List<Groups> groupsList;
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

    public Person() {
    }

    public Person(Integer id) {
        this.id = id;
    }

    public Person(Integer id, @Size(min = 3, max = 50, message = "{person.name}") String name, String phone, @Pattern(regexp = ".+@.+\\.[a-z]+", message = "{person.email}") @Size(min = 3, max = 45, message = "{person.email}") String email, @Size(min = 7, max = 100, message = "{person.password}") String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
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

    public List<Groups> getGroupsList() {
        return groupsList;
    }

    public void setGroupsList(List<Groups> groupsList) {
        this.groupsList = groupsList;
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
        return "Person{" +
                "id=" + id +
                '}';
    }
}
