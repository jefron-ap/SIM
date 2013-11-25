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
@Table(name = "INTERVALS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Intervals.findAll", query = "SELECT i FROM Intervals i"),
    @NamedQuery(name = "Intervals.findByIstart", query = "SELECT i FROM Intervals i WHERE i.istart = :istart"),
    @NamedQuery(name = "Intervals.findByIend", query = "SELECT i FROM Intervals i WHERE i.iend = :iend"),
    @NamedQuery(name = "Intervals.findByIuuid", query = "SELECT i FROM Intervals i WHERE i.iuuid = :iuuid"),
    @NamedQuery(name = "Intervals.findByInid", query = "SELECT i FROM Intervals i WHERE i.inid = :inid")})
public class Intervals implements Serializable {
    @Basic(optional = false)
    @Column(name = "ISTART")
    @Temporal(TemporalType.DATE)
    private Date istart;
    @Basic(optional = false)
    @Column(name = "IEND")
    @Temporal(TemporalType.DATE)
    private Date iend;
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "IUUID")
    private String iuuid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "INID")
    private Integer inid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inid")
    private Collection<Documents> documentsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inid")
    private Collection<Assertions> assertionsCollection;

    public Intervals() {
    }

    public Intervals(Integer inid) {
        this.inid = inid;
    }

    public Intervals(Integer inid, Date istart, Date iend, String iuuid) {
        this.inid = inid;
        this.istart = istart;
        this.iend = iend;
        this.iuuid = iuuid;
    }

    public String getIuuid() {
        return iuuid;
    }

    public void setIuuid(String iuuid) {
        this.iuuid = iuuid;
    }

    public Integer getInid() {
        return inid;
    }

    public void setInid(Integer inid) {
        this.inid = inid;
    }

    @XmlTransient
    public Collection<Documents> getDocumentsCollection() {
        return documentsCollection;
    }

    public void setDocumentsCollection(Collection<Documents> documentsCollection) {
        this.documentsCollection = documentsCollection;
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
        hash += (inid != null ? inid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Intervals)) {
            return false;
        }
        Intervals other = (Intervals) object;
        if ((this.inid == null && other.inid != null) || (this.inid != null && !this.inid.equals(other.inid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.demo.nb.sim.jpa.Intervals[ inid=" + inid + " ]";
    }

    public Date getIstart() {
        return istart;
    }

    public void setIstart(Date istart) {
        this.istart = istart;
    }

    public Date getIend() {
        return iend;
    }

    public void setIend(Date iend) {
        this.iend = iend;
    }
    
}
