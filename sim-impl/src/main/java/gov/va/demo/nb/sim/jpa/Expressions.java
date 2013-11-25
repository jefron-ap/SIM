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
@Table(name = "EXPRESSIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Expressions.findAll", query = "SELECT e FROM Expressions e"),
    @NamedQuery(name = "Expressions.findByExpression", query = "SELECT e FROM Expressions e WHERE e.expression = :expression"),
    @NamedQuery(name = "Expressions.findByEuuid", query = "SELECT e FROM Expressions e WHERE e.euuid = :euuid"),
    @NamedQuery(name = "Expressions.findEnidFromEuuid", query = "SELECT e.enid FROM Expressions e WHERE e.euuid = :euuid"),
    @NamedQuery(name = "Expressions.countEuuid", query = "SELECT count(e) FROM Expressions e WHERE e.euuid = :euuid"),
    @NamedQuery(name = "Expressions.findByEnid", query = "SELECT e FROM Expressions e WHERE e.enid = :enid"),
    @NamedQuery(name = "Expressions.findByCnid", query = "SELECT e FROM Expressions e WHERE e.cnid = :cnid")})
public class Expressions implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discernableEnid")
    private Collection<PncsLegoMap> pncsLegoMapCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qualifierEnid")
    private Collection<PncsLegoMap> pncsLegoMapCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valueEnid")
    private Collection<PncsLegoMap> pncsLegoMapCollection2;
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "EXPRESSION")
    private String expression;
    @Basic(optional = false)
    @Column(name = "EUUID")
    private String euuid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ENID")
    private Integer enid;
    @Basic(optional = false)
    @Column(name = "CNID")
    private int cnid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valueEnid")
    private Collection<Assertions> assertionsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qualifierEnid")
    private Collection<Assertions> assertionsCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discernableEnid")
    private Collection<Assertions> assertionsCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sectionEnid")
    private Collection<Assertions> assertionsCollection3;

    public Expressions() {
    }

    public Expressions(Integer enid) {
        this.enid = enid;
    }

    public Expressions(Integer enid, String expression, String euuid, int cnid) {
        this.enid = enid;
        this.expression = expression;
        this.euuid = euuid;
        this.cnid = cnid;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getEuuid() {
        return euuid;
    }

    public void setEuuid(String euuid) {
        this.euuid = euuid;
    }

    public Integer getEnid() {
        return enid;
    }

    public void setEnid(Integer enid) {
        this.enid = enid;
    }

    public int getCnid() {
        return cnid;
    }

    public void setCnid(int cnid) {
        this.cnid = cnid;
    }

    @XmlTransient
    public Collection<Assertions> getAssertionsCollection() {
        return assertionsCollection;
    }

    public void setAssertionsCollection(Collection<Assertions> assertionsCollection) {
        this.assertionsCollection = assertionsCollection;
    }

    @XmlTransient
    public Collection<Assertions> getAssertionsCollection1() {
        return assertionsCollection1;
    }

    public void setAssertionsCollection1(Collection<Assertions> assertionsCollection1) {
        this.assertionsCollection1 = assertionsCollection1;
    }

    @XmlTransient
    public Collection<Assertions> getAssertionsCollection2() {
        return assertionsCollection2;
    }

    public void setAssertionsCollection2(Collection<Assertions> assertionsCollection2) {
        this.assertionsCollection2 = assertionsCollection2;
    }

    @XmlTransient
    public Collection<Assertions> getAssertionsCollection3() {
        return assertionsCollection3;
    }

    public void setAssertionsCollection3(Collection<Assertions> assertionsCollection3) {
        this.assertionsCollection3 = assertionsCollection3;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (enid != null ? enid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Expressions)) {
            return false;
        }
        Expressions other = (Expressions) object;
        if ((this.enid == null && other.enid != null) || (this.enid != null && !this.enid.equals(other.enid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.demo.nb.sim.jpa.Expressions[ enid=" + enid + " ]";
    }

    @XmlTransient
    public Collection<PncsLegoMap> getPncsLegoMapCollection() {
        return pncsLegoMapCollection;
    }

    public void setPncsLegoMapCollection(Collection<PncsLegoMap> pncsLegoMapCollection) {
        this.pncsLegoMapCollection = pncsLegoMapCollection;
    }

    @XmlTransient
    public Collection<PncsLegoMap> getPncsLegoMapCollection1() {
        return pncsLegoMapCollection1;
    }

    public void setPncsLegoMapCollection1(Collection<PncsLegoMap> pncsLegoMapCollection1) {
        this.pncsLegoMapCollection1 = pncsLegoMapCollection1;
    }

    @XmlTransient
    public Collection<PncsLegoMap> getPncsLegoMapCollection2() {
        return pncsLegoMapCollection2;
    }

    public void setPncsLegoMapCollection2(Collection<PncsLegoMap> pncsLegoMapCollection2) {
        this.pncsLegoMapCollection2 = pncsLegoMapCollection2;
    }
    
}
