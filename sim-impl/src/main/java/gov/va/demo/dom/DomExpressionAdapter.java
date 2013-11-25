
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.demo.dom;

import gov.va.sim.impl.expression.Expression;
import gov.va.sim.impl.expression.node.ConceptNode;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.ihtsdo.tk.api.TerminologySnapshotDI;
import org.ihtsdo.tk.api.concept.ConceptVersionBI;
import org.ihtsdo.tk.binding.TermAux;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author kec
 */
public class DomExpressionAdapter {
   public static Expression convertToExpression(String expression, TerminologySnapshotDI terminologySnapshot)
           throws ParserConfigurationException, SAXException, IOException {
      StringBuilder docBuilder = new StringBuilder("<doc><objective><assertion><discernable>");

      docBuilder.append(expression);
      docBuilder.append("</discernable></assertion></objective></doc>");

      DocumentBuilderFactory factory                  = DocumentBuilderFactory.newInstance();
      DocumentBuilder        builder                  = factory.newDocumentBuilder();
      Document               assertionDoc             =
         builder.parse(new InputSource(new StringReader(docBuilder.toString())));
      int                    whatToShow               = NodeFilter.SHOW_ELEMENT;
      NodeFilter             filter                   = new ExpressionRootNodeFilter();
      boolean                entityReferenceExpansion = false;
      TreeWalker             walker                   =
         ((DocumentTraversal) assertionDoc).createTreeWalker(assertionDoc, whatToShow, filter,
            entityReferenceExpansion);
      Node domNode = walker.nextNode();

      while (domNode != null) {
         TreeWalker expressionWalker = ((DocumentTraversal) assertionDoc).createTreeWalker(domNode,
                                          whatToShow, null, entityReferenceExpansion);
         String nodeString = expressionWalker.getCurrentNode().getParentNode().getParentNode().toString();

         nodeString = nodeString + expressionWalker.getCurrentNode().getParentNode().toString();
         nodeString = nodeString + expressionWalker.getCurrentNode().toString();
         nodeString = nodeString + expressionWalker.toString();

         try {
            return convertToExpression(expressionWalker, terminologySnapshot);
         } catch (PropertyVetoException ex) {
            Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.SEVERE, nodeString, ex);
         }

         domNode = walker.nextNode();
      }

      return null;
   }

   public static Expression convertToExpression(TreeWalker expressionWalker,
           TerminologySnapshotDI terminologySnapshot)
           throws IOException, PropertyVetoException {
      Node             node = expressionWalker.getCurrentNode();
      Expression       exp  = new Expression();
      ConceptVersionBI cv;

      if (node.getAttributes().getNamedItem("sctid") != null) {
         String sctId = node.getAttributes().getNamedItem("sctid").getNodeValue();

         cv = terminologySnapshot.getConceptForNid(
            terminologySnapshot.getNidFromAlternateId(TermAux.SNOMED_IDENTIFIER.getUuids()[0], sctId));
      } else {
         String uuid = node.getAttributes().getNamedItem("uuid").getNodeValue();

         cv = terminologySnapshot.getConceptVersion(UUID.fromString(uuid));
      }

      ConceptNode expNode = new ConceptNode();

      expNode.setValue(cv);
      exp.setFocus(expNode);
      traverseRelTypeLevel(terminologySnapshot, expressionWalker, 0, expNode);

      return exp;
   }

   public static List<Expression> convertToExpressionList(Document assertionDoc,
           TerminologySnapshotDI terminologySnapshot) {
      ArrayList<Expression> expressionList           = new ArrayList<Expression>();
      int                   whatToShow               = NodeFilter.SHOW_ELEMENT;
      NodeFilter            filter                   = new ExpressionRootNodeFilter();
      boolean               entityReferenceExpansion = false;
      TreeWalker            walker                   =
         ((DocumentTraversal) assertionDoc).createTreeWalker(assertionDoc, whatToShow, filter,
            entityReferenceExpansion);
      Node domNode = walker.nextNode();

      while (domNode != null) {
         TreeWalker expressionWalker = ((DocumentTraversal) assertionDoc).createTreeWalker(domNode,
                                          whatToShow, null, entityReferenceExpansion);
         String nodeString = expressionWalker.getCurrentNode().getParentNode().getParentNode().toString();

         nodeString = nodeString + expressionWalker.getCurrentNode().getParentNode().toString();
         nodeString = nodeString + expressionWalker.getCurrentNode().toString();
         nodeString = nodeString + expressionWalker.toString();

         try {
            Expression exp = convertToExpression(expressionWalker, terminologySnapshot);

            expressionList.add(exp);
         } catch (IOException | PropertyVetoException iOException) {
            Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.SEVERE, nodeString, iOException);
         }

         domNode = walker.nextNode();
      }

      return expressionList;
   }

   private static void traverseRelDestLevel(TerminologySnapshotDI terminologySnapshot, TreeWalker walker,
           int level, ConceptNode origin, ConceptVersionBI typeCv)
           throws IOException, PropertyVetoException {
      Node parent = walker.getCurrentNode();

      // traverse children:
      for (Node destinationDomNode = walker.firstChild(); destinationDomNode != null;
              destinationDomNode = walker.nextSibling()) {
         ConceptVersionBI descCv = null;

         if (destinationDomNode.getAttributes().getNamedItem("sctid") != null) {
            String sctId = destinationDomNode.getAttributes().getNamedItem("sctid").getNodeValue();

            descCv = terminologySnapshot.getConceptForNid(
               terminologySnapshot.getNidFromAlternateId(TermAux.SNOMED_IDENTIFIER.getUuids()[0], sctId));
         } else {
            if (destinationDomNode.getAttributes().getNamedItem("uuid") != null) {
               String uuid = destinationDomNode.getAttributes().getNamedItem("uuid").getNodeValue();

               descCv = terminologySnapshot.getConceptVersion(UUID.fromString(uuid));
            } else {
               Logger.getLogger(DomExpressionAdapter.class.getName()).log(Level.WARNING,
                                "No uuid for relationship on ConceptNode: {0}", origin);
            }
         }

         if (descCv != null) {
            ConceptNode destNode = new ConceptNode();

            destNode.setValue(descCv);
            origin.addRel(typeCv, destNode);
            traverseRelTypeLevel(terminologySnapshot, walker, level + 1, destNode);
         }
      }

      // return position to the current (level up):
      walker.setCurrentNode(parent);
   }

   private static void traverseRelTypeLevel(TerminologySnapshotDI terminologySnapshot, TreeWalker walker,
           int level, ConceptNode expNode)
           throws IOException, PropertyVetoException {
      Node parent = walker.getCurrentNode();

      // traverse children:
      for (Node typeDomNode = walker.firstChild(); typeDomNode != null; typeDomNode = walker.nextSibling()) {
         ConceptVersionBI typeCv;

         if (typeDomNode.getAttributes().getNamedItem("sctid") != null) {
            String sctId = typeDomNode.getAttributes().getNamedItem("sctid").getNodeValue();

            typeCv = terminologySnapshot.getConceptForNid(
               terminologySnapshot.getNidFromAlternateId(TermAux.SNOMED_IDENTIFIER.getUuids()[0], sctId));
         } else {
            String uuid = typeDomNode.getAttributes().getNamedItem("typeUuid").getNodeValue();

            typeCv = terminologySnapshot.getConceptVersion(UUID.fromString(uuid));
         }

         traverseRelDestLevel(terminologySnapshot, walker, level + 1, expNode, typeCv);
      }

      // return position to the current (level up):
      walker.setCurrentNode(parent);
   }

   //~--- inner classes -------------------------------------------------------

   public static class ExpressionRootNodeFilter implements NodeFilter {
      @Override
      public short acceptNode(Node n) {
         if (n.getNodeName().equals("concept")) {
            Node parent = n.getParentNode();

            if (parent.getNodeName().equals("discernable") || parent.getNodeName().equals("qualifier")
                    || parent.getNodeName().equals("value")) {
               return NodeFilter.FILTER_ACCEPT;
            }
         } else if (n.getNodeName().equals("rel")) {
            return NodeFilter.FILTER_REJECT;
         }

         return NodeFilter.FILTER_SKIP;
      }
   }
}
