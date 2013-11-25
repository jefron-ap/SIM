/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.demo.nb.sim.jpa;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kec
 */
@Entity
@Table(name = "ASSERTIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assertions.findAll", query = "SELECT a FROM Assertions a"),
    @NamedQuery(name = "Assertions.findByAuuid", query = "SELECT a FROM Assertions a WHERE a.auuid = :auuid"),
    @NamedQuery(name = "Assertions.findByDnid", query = "SELECT a FROM Assertions a WHERE a.dnid = :dnid ORDER BY a.seqInDoc"),
    @NamedQuery(name = "Assertions.findBySeqInDoc", query = "SELECT a FROM Assertions a WHERE a.seqInDoc = :seqInDoc")})
public class Assertions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "AUUID")
    private String auuid;
    @Basic(optional = false)
    @Column(name = "SEQ_IN_DOC")
    private short seqInDoc;
    @JoinColumn(name = "INID", referencedColumnName = "INID")
    @ManyToOne(optional = false)
    private Intervals inid;
    @JoinColumn(name = "VALUE_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions valueEnid;
    @JoinColumn(name = "QUALIFIER_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions qualifierEnid;
    @JoinColumn(name = "DISCERNABLE_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions discernableEnid;
    @JoinColumn(name = "SECTION_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions sectionEnid;
    @JoinColumn(name = "DNID", referencedColumnName = "DNID")
    @ManyToOne(optional = false)
    private Documents dnid;

    public Assertions() {
    }

    public Assertions(String auuid) {
        this.auuid = auuid;
    }

    public Assertions(String auuid, short seqInDoc) {
        this.auuid = auuid;
        this.seqInDoc = seqInDoc;
    }

    public String getAuuid() {
        return auuid;
    }

    public void setAuuid(String auuid) {
        this.auuid = auuid;
    }

    public short getSeqInDoc() {
        return seqInDoc;
    }

    public void setSeqInDoc(short seqInDoc) {
        this.seqInDoc = seqInDoc;
    }

    public Intervals getInid() {
        return inid;
    }

    public void setInid(Intervals inid) {
        this.inid = inid;
    }

    public Expressions getValueEnid() {
        return valueEnid;
    }

    public void setValueEnid(Expressions valueEnid) {
        this.valueEnid = valueEnid;
    }

    public Expressions getQualifierEnid() {
        return qualifierEnid;
    }

    public void setQualifierEnid(Expressions qualifierEnid) {
        this.qualifierEnid = qualifierEnid;
    }

    public Expressions getDiscernableEnid() {
        return discernableEnid;
    }

    public void setDiscernableEnid(Expressions discernableEnid) {
        this.discernableEnid = discernableEnid;
    }

    public Expressions getSectionEnid() {
        return sectionEnid;
    }

    public void setSectionEnid(Expressions sectionEnid) {
        this.sectionEnid = sectionEnid;
    }

    public Documents getDnid() {
        return dnid;
    }

    public void setDnid(Documents dnid) {
        this.dnid = dnid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (auuid != null ? auuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Assertions)) {
            return false;
        }
        Assertions other = (Assertions) object;
        if ((this.auuid == null && other.auuid != null) || (this.auuid != null && !this.auuid.equals(other.auuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.demo.nb.sim.jpa.Assertions[ auuid=" + auuid + " ]";
    }
    
}
