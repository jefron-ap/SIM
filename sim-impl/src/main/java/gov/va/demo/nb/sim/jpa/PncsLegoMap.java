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
@Table(name = "PNCS_LEGO_MAP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PncsLegoMap.findAll", query = "SELECT p FROM PncsLegoMap p"),
    @NamedQuery(name = "PncsLegoMap.findByMapId", query = "SELECT p FROM PncsLegoMap p WHERE p.mapId = :mapId"),
    @NamedQuery(name = "PncsLegoMap.findByPncsId", query = "SELECT p FROM PncsLegoMap p WHERE p.pncsId = :pncsId"),
    @NamedQuery(name = "PncsLegoMap.findByPncsValue", query = "SELECT p FROM PncsLegoMap p WHERE p.pncsValue = :pncsValue"),
    @NamedQuery(name = "PncsLegoMap.countPncsIdPncsValue", query = "SELECT count(p) FROM PncsLegoMap p WHERE p.pncsValue = :pncsValue and p.pncsId = :pncsId"),
    @NamedQuery(name = "PncsLegoMap.findByPncsIdPncsValue", query = "SELECT p FROM PncsLegoMap p WHERE p.pncsValue = :pncsValue and p.pncsId = :pncsId")})


public class PncsLegoMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MAP_ID")
    private Integer mapId;
    @Column(name = "PNCS_ID")
    private Integer pncsId;
    @Basic(optional = false)
    @Column(name = "PNCS_VALUE")
    private String pncsValue;
    @JoinColumn(name = "DISCERNABLE_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions discernableEnid;
    @JoinColumn(name = "QUALIFIER_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions qualifierEnid;
    @JoinColumn(name = "VALUE_ENID", referencedColumnName = "ENID")
    @ManyToOne(optional = false)
    private Expressions valueEnid;

    public PncsLegoMap() {
    }

    public PncsLegoMap(Integer mapId) {
        this.mapId = mapId;
    }

    public PncsLegoMap(Integer mapId, String pncsValue) {
        this.mapId = mapId;
        this.pncsValue = pncsValue;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public Integer getPncsId() {
        return pncsId;
    }

    public void setPncsId(Integer pncsId) {
        this.pncsId = pncsId;
    }

    public String getPncsValue() {
        return pncsValue;
    }

    public void setPncsValue(String pncsValue) {
        this.pncsValue = pncsValue;
    }

    public Expressions getDiscernableEnid() {
        return discernableEnid;
    }

    public void setDiscernableEnid(Expressions discernableEnid) {
        this.discernableEnid = discernableEnid;
    }

    public Expressions getQualifierEnid() {
        return qualifierEnid;
    }

    public void setQualifierEnid(Expressions qualifierEnid) {
        this.qualifierEnid = qualifierEnid;
    }

    public Expressions getValueEnid() {
        return valueEnid;
    }

    public void setValueEnid(Expressions valueEnid) {
        this.valueEnid = valueEnid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mapId != null ? mapId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PncsLegoMap)) {
            return false;
        }
        PncsLegoMap other = (PncsLegoMap) object;
        if ((this.mapId == null && other.mapId != null) || (this.mapId != null && !this.mapId.equals(other.mapId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gov.va.demo.nb.sim.jpa.PncsLegoMap[ mapId=" + mapId + " ]";
    }
    
}
