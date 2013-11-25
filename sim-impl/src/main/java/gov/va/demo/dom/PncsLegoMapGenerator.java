
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.demo.dom;

import gov.va.demo.expression.ExpressionManager;
import gov.va.demo.nb.sim.jpa.Expressions;
import gov.va.demo.nb.sim.jpa.JpaManager;
import gov.va.demo.nb.sim.jpa.PncsLegoMap;
import gov.va.sim.impl.expression.Expression;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.ihtsdo.tk.api.TerminologySnapshotDI;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 *
 * @author kec
 */
public class PncsLegoMapGenerator {
   private EntityManager em;
   private String        filename;
   private int           pncsId;
   private String        pncsValue;

   //~--- constructors --------------------------------------------------------

   public PncsLegoMapGenerator(String filename) {
      this.filename = filename;
   }

   //~--- methods -------------------------------------------------------------

   public void addToMap(Document assertionDoc, TerminologySnapshotDI terminologySnapshot)
           throws IOException, PropertyVetoException, NoSuchAlgorithmException {
      Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.INFO, "Processing: {0}", filename);
      em = JpaManager.getEntityManager();

      int        whatToShow               = NodeFilter.SHOW_ELEMENT;
      NodeFilter filter                   = new ExpressionRootNodeFilter();
      boolean    entityReferenceExpansion = false;
      TreeWalker walker                   = ((DocumentTraversal) assertionDoc).createTreeWalker(assertionDoc,
                                               whatToShow, filter, entityReferenceExpansion);
      Node              domNode = walker.nextNode();
      PncsLegoMap       map     = null;
      EntityTransaction entr    = null;

      while (domNode != null) {
         switch (domNode.getNodeName()) {
         case "pncs" :
            if (domNode.getAttributes().getNamedItem("value") != null) {
               pncsValue = domNode.getAttributes().getNamedItem("value").getNodeValue();
            } else {
               pncsValue = "null";
            }

            try {
               pncsId = Integer.parseInt(domNode.getAttributes().getNamedItem("id").getNodeValue());
            } catch (NumberFormatException numberFormatException) {
               Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.SEVERE,
                                "NumberFormatException: {0} Processing: {1}",
                                new Object[] { numberFormatException.getLocalizedMessage(),
                                               filename });
               throw numberFormatException;
            }

            break;

         case "discernable" :
            entr = em.getTransaction();

            if (entr.isActive()) {
               entr.rollback();
            }

            entr.begin();

            if (pncsId > Integer.MIN_VALUE) {
               map = new PncsLegoMap();
               map.setPncsId(pncsId);
               map.setPncsValue(pncsValue);

               try {
                  Expressions dbExpression = processExpression(assertionDoc, domNode, whatToShow,
                                                entityReferenceExpansion, terminologySnapshot);

                  map.setDiscernableEnid(dbExpression);
               } catch (DOMException | IOException | PropertyVetoException | NoSuchAlgorithmException ex) {
                  Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.SEVERE,
                                   "Processing: " + filename, ex);
                  map = null;
               }
            }

            break;

         case "qualifier" :
            if (map != null) {
               Expressions dbExpression = processExpression(assertionDoc, domNode, whatToShow,
                                             entityReferenceExpansion, terminologySnapshot);

               map.setQualifierEnid(dbExpression);
            }

            break;

         case "value" :
            if (map != null) {
               Expressions dbExpression = processExpression(assertionDoc, domNode, whatToShow,
                                             entityReferenceExpansion, terminologySnapshot);

               if (dbExpression != null) {
                  map.setValueEnid(dbExpression);

                  Query countPncsIdPncsValueQuery = em.createNamedQuery("PncsLegoMap.countPncsIdPncsValue");

                  countPncsIdPncsValueQuery.setParameter("pncsValue", map.getPncsValue());
                  countPncsIdPncsValueQuery.setParameter("pncsId", map.getPncsId());

                  List obs   = countPncsIdPncsValueQuery.getResultList();
                  long count = (Long) obs.get(0);

                  if (count == 0) {
                     em.persist(map);
                     entr.commit();
                     System.out.println("wrote map: " + map);
                  } else {
                     entr.rollback();
                     System.out.println("  dup map: " + map);
                  }
               } else {
                  StringBuilder sb = new StringBuilder();

                  sb.append("Processing: ").append(filename);
                  sb.append("dbExpression is null:\n");
                  sb.append("assertionDoc: ").append(assertionDoc);
                  sb.append("\ndomNode: ").append(domNode);
                  sb.append("\nwhatToShow: ").append(whatToShow);
                  sb.append("\nentityReferenceExpansion: ").append(entityReferenceExpansion);
                  sb.append("\nterminologySnapshot: ").append(terminologySnapshot);
                  Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.WARNING, sb.toString());
               }

               map = null;
            } else {
               entr.rollback();
            }

            break;
         }

         domNode = walker.nextNode();
      }
   }

   private Expressions processExpression(Document assertionDoc, Node domNode, int whatToShow,
           boolean entityReferenceExpansion, TerminologySnapshotDI terminologySnapshot)
           throws DOMException, IOException, PropertyVetoException, UnsupportedEncodingException,
                  NoSuchAlgorithmException {
      for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
         Node child = domNode.getChildNodes().item(i);

         if (child.getNodeName().equals("concept")) {
            TreeWalker expressionWalker = ((DocumentTraversal) assertionDoc).createTreeWalker(child,
                                             whatToShow, null, entityReferenceExpansion);
            Expression exp             = DomExpressionAdapter.convertToExpression(expressionWalker,
                                            terminologySnapshot);
            Query      countEuuidQuery = em.createNamedQuery("Expressions.findByEuuid");

            countEuuidQuery.setParameter("euuid", exp.getUuid().toString());

            List obs = countEuuidQuery.getResultList();

            if (obs.isEmpty()) {
               EntityTransaction entr = em.getTransaction();

               if (!entr.isActive()) {
                  entr.begin();
               }

               Expressions expressionObj = new Expressions();

               expressionObj.setCnid(ExpressionManager.getCnid(exp));
               expressionObj.setEuuid(exp.getUuid().toString());
               expressionObj.setExpression(exp.getXml());
               em.persist(expressionObj);

               return expressionObj;
            } else {
               Expressions dbExpression = (Expressions) obs.get(0);

               return dbExpression;
            }
         }
      }

      return null;
   }

   //~--- inner classes -------------------------------------------------------

   public static class ExpressionRootNodeFilter implements NodeFilter {
      @Override
      public short acceptNode(Node n) {
         if (n.getNodeName().equals("pncs")) {
            return NodeFilter.FILTER_ACCEPT;
         }

         if (n.getNodeName().equals("discernable")) {
            return NodeFilter.FILTER_ACCEPT;
         }

         if (n.getNodeName().equals("qualifier")) {
            return NodeFilter.FILTER_ACCEPT;
         }

         if (n.getNodeName().equals("value")) {
            return NodeFilter.FILTER_ACCEPT;
         }

         if (n.getNodeName().equals("concept")) {
            return NodeFilter.FILTER_REJECT;
         }

         return NodeFilter.FILTER_SKIP;
      }
   }
}
