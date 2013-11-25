/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.demo.nb.sim.jpa;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kec
 */
@Entity
@Table(name = "DOCUMENTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documents.findAll", query = "SELECT d FROM Documents d"),
    @NamedQuery(name = "Documents.findByDuuid", query = "SELECT d FROM Documents d WHERE d.duuid = :duuid"),
    @NamedQuery(name = "Documents.findByDnid", query = "SELECT d FROM Documents d WHERE d.dnid = :dnid")})
public class Documents implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @Column(name = "DUUID")
    private String duuid;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DNID")
    private Integer dnid;
    
    @JoinColumn(name = "PROVIDERNID", referencedColumnName = "PNID")
    @ManyToOne
    private Persons providernid;
    
    @JoinColumn(name = "PATIENTNID", referencedColumnName = "PNID")
    @ManyToOne(optional = false)
    private Persons patientnid;
    
    @JoinColumn(name = "INID", referencedColumnName = "INID")
    @ManyToOne(optional = false)
    private Intervals inid;
    
    
    @Basic(optional = false)
    @Column(name = "INST_NAME")
    private String institutionName;

    @Basic(optional = false)
    @Column(name = "INST_NUMBER")
    private String institutionNumber;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dnid")
    private Collection<Assertions> assertionsCollection;

    public Documents() {
    }

    public Documents(Integer dnid) {
        this.dnid = dnid;
    }

    public Documents(Integer dnid, String duuid) {
        this.dnid = dnid;
        this.duuid = duuid;
    }

    public String getDuuid() {
        return duuid;
    }

    public void setDuuid(String duuid) {
        this.duuid = duuid;
    }

    public Integer getDnid() {
        return dnid;
    }

    public void setDnid(Integer dnid) {
        this.dnid = dnid;
    }

    public Persons getProvidernid() {
        return providernid;
    }

    public void setProvidernid(Persons providernid) {
        this.providernid = providernid;
    }

    public Persons getPatientnid() {
        return patientnid;
    }

    public void setPatientnid(Persons patientnid) {
        this.patientnid = patientnid;
    }

    public Intervals getInid() {
        return inid;
    }

    public void setInid(Intervals inid) {
        this.inid = inid;
    }

    @XmlTransient
    public Collection<Assertions> getAssertionsCollection() {
        return assertionsCollection;
    }

    public void setAssertionsCollection(Collection<Assertions> assertionsCollection) {
        this.assertionsCollection = assertionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dnid != null ? dnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documents)) {
            return false;
        }
        Documents other = (Documents) object;
        if ((this.dnid == null && other.dnid != null) || (this.dnid != null && !this.dnid.equals(other.dnid))) {
            return false;
        }
        return true;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInstitutionNumber() {
        return institutionNumber;
    }

    public void setInstitutionNumber(String institutionNumber) {
        this.institutionNumber = institutionNumber;
    }
    

    @Override
    public String toString() {
        return "gov.va.demo.nb.sim.jpa.Documents[ dnid=" + dnid + " ]";
    }
    
}
