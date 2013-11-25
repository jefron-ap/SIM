/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.demo.nb.sim.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kec
 */
@Entity
@Table(name = "PERSONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persons.findAll", query = "SELECT p FROM Persons p"),
    @NamedQuery(name = "Persons.findByPuuid", query = "SELECT p FROM Persons p WHERE p.puuid = :puuid"),
    @NamedQuery(name = "Persons.findByPnid", query = "SELECT p FROM Persons p WHERE p.pnid = :pnid"),
    @NamedQuery(name = "Persons.findByLastname", query = "SELECT p FROM Persons p WHERE p.familyName = :lastname"),
    @NamedQuery(name = "Persons.findByFirstname", query = "SELECT p FROM Persons p WHERE p.givenName = :firstname"),
    @NamedQuery(name = "Persons.findByDob", query = "SELECT p FROM Persons p WHERE p.dob = :dob")
    })
public class Persons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;
    
    @Basic(optional = false)
    @Column(name = "PUUID")
    private String puuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PNID")
    private Integer pnid;
    
    @Column(name = "PIEN")
    private String pInternalEntryNumber;
    
    @Column(name = "ID_IDENTITY")
    private String identity;
    
    @Column(name = "ID_ASSN_FAC")
    private String idAssigningFacility;
    
    @Column(name = "ID_ASSN_AUTH")
    private String idAssigningAuthority = "USVHA";
   
    @Column(name = "NAME_PREFIX")
    private String prefix;
    
    @Basic(optional = false)
    @Column(name = "NAME_GIVEN")
    private String givenName;
    
    @Column(name = "NAME_MIDDLE")
    private String middleName;
    
    @Basic(optional = false)
    @Column(name = "NAME_FAMILY")
    private String familyName;

    @Column(name = "NAME_SUFFIX")
    private String suffix;

    @Column(name = "NAME_TITLE")
    private String title;

    @OneToMany(mappedBy = "providernid")
    private Collection<Documents> documentsCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientnid")
    private Collection<Documents> documentsCollection1;

    public Persons() {
    }

    public Persons(Integer pnid) {
        this.pnid = pnid;
    }

    public Persons(Integer pnid, String puuid, String familyName, String givenName, Date dob) {
        this.pnid = pnid;
        this.puuid = puuid;
        this.familyName = familyName;
        this.givenName = givenName;
        this.dob = dob;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public Integer getPnid() {
        return pnid;
    }

    public void setPnid(Integer pnid) {
        this.pnid = pnid;
    }

    @XmlTransient
    public Collection<Documents> getDocumentsCollection() {
        return documentsCollection;
    }

    public void setDocumentsCollection(Collection<Documents> documentsCollection) {
        this.documentsCollection = documentsCollection;
    }

    @XmlTransient
    public Collection<Documents> getDocumentsCollection1() {
        return documentsCollection1;
    }

    public void setDocumentsCollection1(Collection<Documents> documentsCollection1) {
        this.documentsCollection1 = documentsCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pnid != null ? pnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persons)) {
            return false;
        }
        Persons other = (Persons) object;
        if ((this.pnid == null && other.pnid != null) || (this.pnid != null && !this.pnid.equals(other.pnid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.demo.nb.sim.jpa.Persons[ pnid=" + pnid + " ]";
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPInternalEntryNumber() {
        return pInternalEntryNumber;
    }

    public void setPInternalEntryNumber(String pInternalEntryNumber) {
        this.pInternalEntryNumber = pInternalEntryNumber;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdAssigningFacility() {
        return idAssigningFacility;
    }

    public void setIdAssigningFacility(String idAssigningFacility) {
        this.idAssigningFacility = idAssigningFacility;
    }

    public String getIdAssigningAuthority() {
        return idAssigningAuthority;
    }

    public void setIdAssigningAuthority(String idAssigningAuthority) {
        this.idAssigningAuthority = idAssigningAuthority;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
