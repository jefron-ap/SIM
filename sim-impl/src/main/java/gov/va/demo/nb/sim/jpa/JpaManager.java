/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.demo.nb.sim.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kec
 */
public class JpaManager {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static JpaManager mgr = null;

    public JpaManager() {
       emf=Persistence.createEntityManagerFactory("gov.va.demo_nb-sim-demo-sample_nbm_1.0-SNAPSHOTPU");
       em=emf.createEntityManager();
    }
    
    public static EntityManager getEntityManager() {
        if (mgr == null) {
            mgr = new JpaManager();
        }
        return em;
    }
    
}
