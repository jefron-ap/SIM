
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.oia.smart.rest;

import gov.va.demo.nb.sim.jpa.Assertions;
import gov.va.demo.nb.sim.jpa.Documents;
import gov.va.demo.nb.sim.jpa.Intervals;
import gov.va.demo.nb.sim.jpa.JpaManager;
import gov.va.demo.nb.sim.jpa.Persons;
import gov.va.demo.nb.sim.jpa.PncsLegoMap;
import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.ihtsdo.helper.uuid.Type5UuidFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 *
 * @author kec
 */
public class ProcessPncsDoc {
   private Documents checkDocumentTable(UUID docUuid) {
      EntityManager em              = JpaManager.getEntityManager();
      Query         countEuuidQuery = em.createNamedQuery("Documents.findByDuuid");

      countEuuidQuery.setParameter("duuid", docUuid.toString());

      List obs = countEuuidQuery.getResultList();

      if (!obs.isEmpty()) {
         return (Documents) obs.get(0);
      }

      return null;
   }

   private Intervals checkIntervalTable(UUID intervalUuid) {
      EntityManager em              = JpaManager.getEntityManager();
      Query         countIuuidQuery = em.createNamedQuery("Intervals.findByIuuid");

      countIuuidQuery.setParameter("iuuid", intervalUuid.toString());

      List<Intervals> obs = countIuuidQuery.getResultList();

      if (!obs.isEmpty()) {
         return (Intervals) obs.get(0);
      }

      return null;
   }

   private PncsLegoMap checkPcnsMap(String idStr, String valueStr) {
      if ((idStr == null) || (idStr.length() < 1)) {
         System.out.println("Bad idStr for value: " + valueStr);
      }

      EntityManager em            = JpaManager.getEntityManager();
      Query         countMapQuery = em.createNamedQuery("PncsLegoMap.findByPncsIdPncsValue");

      countMapQuery.setParameter("pncsValue", valueStr);
      countMapQuery.setParameter("pncsId", Long.parseLong(idStr));

      List obs = countMapQuery.getResultList();

      if (!obs.isEmpty()) {
         return (PncsLegoMap) obs.get(0);
      }

      return null;
   }

   private Persons checkPersonTable(UUID docUuid) {
      EntityManager em              = JpaManager.getEntityManager();
      Query         countEuuidQuery = em.createNamedQuery("Persons.findByPuuid");

      countEuuidQuery.setParameter("puuid", docUuid.toString());

      List<Persons> obs = countEuuidQuery.getResultList();

      if (!obs.isEmpty()) {
         return (Persons) obs.get(0);
      }

      return null;
   }

   /**
    *
    * @param pncsDocStr
    * @return HDR XML document as string.
    * @throws Exception
    */
   protected Documents process(String pncsDocStr) throws Exception {
      DocumentBuilderFactory factory                  = DocumentBuilderFactory.newInstance();
      DocumentBuilder        builder                  = factory.newDocumentBuilder();
      Document               pncsDoc                  =
         builder.parse(new ByteArrayInputStream(pncsDocStr.getBytes()));
      int                    whatToShow               = NodeFilter.SHOW_ELEMENT;
      NodeFilter             filter                   = new SectionNodeFilter();
      boolean                entityReferenceExpansion = false;
      TreeWalker             walker                   =
         ((DocumentTraversal) pncsDoc).createTreeWalker(pncsDoc, whatToShow, filter,
            entityReferenceExpansion);
      Node    domNode           = walker.nextNode();
      Node    sectionNode       = null;
      boolean headerSection     = false;
      String  patientName       = null;
      String  patientIEN        = null;
      String  authorName        = null;
      String  authorIEN         = null;
      String  systemSource      = null;
      String  documentTimestamp = null;
      String  institutionName   = null;
      String  institutionNumber = null;
      Date    timestamp;
      UUID    providerUuid    = null;
      UUID    patientUuid     = null;
      UUID    documentUuid    = null;
      UUID    intervalUuid    = null;
      int     patientPnid     = Integer.MAX_VALUE;
      int     providerPnid    = Integer.MAX_VALUE;
      int     documentNid     = Integer.MAX_VALUE;
      int     intervalNid     = Integer.MAX_VALUE;
      String  sectionIdStr    = null;
      String  sectionValueStr = null;

      // 03/07/2012@19:36
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy@HH:mm");

      // Jan 12, 2011@12:34
      SimpleDateFormat sdf2          = new SimpleDateFormat("MMM DD, yyyy@HH:mm");
      int              mappedCount   = 0;
      int              unmappedCount = 0;
      short            sequenceInDoc = 0;
      Documents        doc           = null;

      while (domNode != null) {
         sequenceInDoc++;

         if (domNode.getNodeName().equals("section")) {
            sectionNode = domNode;

            Node attr = sectionNode.getAttributes().getNamedItem("text");

            if (attr.getTextContent().equalsIgnoreCase("Section=Header")) {
               headerSection = true;
            } else {
               if (headerSection) {
                  headerSection = false;
                  System.out.println("patientName: " + patientName);
                  System.out.println("patientIEN: " + patientIEN);
                  System.out.println("authorName: " + authorName);
                  System.out.println("authorIEN: " + authorIEN);
                  System.out.println("systemSource: " + systemSource);
                  System.out.println("timeStamp: " + documentTimestamp);
                  System.out.println("institutionName: " + institutionName);
                  System.out.println("institutionNumber: " + institutionNumber);
                  providerUuid =
                     Type5UuidFactory.get(UUID.fromString("59c115d0-79c5-11e1-b0c4-0800200c9a66"),
                                          authorName);
                  patientUuid = Type5UuidFactory.get(UUID.fromString("59c115d2-79c5-11e1-b0c4-0800200c9a66"),
                                                     patientIEN);

                  try {
                     timestamp = sdf.parse(documentTimestamp);
                  } catch (ParseException parseException) {
                     timestamp = sdf2.parse(documentTimestamp);
                  }

                  String intervalStart = timestamp.toString();
                  String intervalEnd   = timestamp.toString();

                  intervalUuid =
                     Type5UuidFactory.get(UUID.fromString("6703d801-7b54-11e1-b0c4-0800200c9a66"),
                                          intervalStart + intervalEnd);
                  documentUuid =
                     Type5UuidFactory.get(UUID.fromString("59c115d3-79c5-11e1-b0c4-0800200c9a66"),
                                          authorName + patientIEN + intervalUuid.toString());
                  System.out.println("providerUuid: " + providerUuid);
                  System.out.println("patientUuid: " + patientUuid);
                  System.out.println("documentUuid: " + documentUuid);

                  Persons patient = getPatient(patientUuid, patientName, patientIEN, institutionName);

                  patientPnid = patient.getPnid();

                  Persons provider = getProvider(providerUuid, authorName, authorIEN, institutionName);

                  providerPnid = provider.getPnid();

                  Intervals interval = getInterval(intervalUuid, timestamp);

                  doc = getDocument(doc, documentUuid, interval, patient, provider, institutionName,
                                    institutionNumber);
                  System.out.println("in doc table: " + checkDocumentTable(documentUuid));
                  System.out.println("patient in person table: " + checkPersonTable(patientUuid));
                  System.out.println("provider in person table: " + checkPersonTable(providerUuid));
               }    // Section header but not "Section=Header"

               Node sectionValueNode = sectionNode.getAttributes().getNamedItem("value");

               if (sectionValueNode != null) {
                  sectionValueStr = sectionValueNode.getTextContent();
               } else {
                  sectionValueStr = null;
               }

               Node sectionIdNode = sectionNode.getAttributes().getNamedItem("id");

               if (sectionIdNode != null) {
                  sectionIdStr = sectionIdNode.getTextContent();
               } else {
                  sectionIdStr = null;
               }
            }

            System.out.println(attr.getTextContent());
         } else {
            String     idStr       = domNode.getAttributes().getNamedItem("id").getNodeValue();
            TreeWalker valueWalker = ((DocumentTraversal) pncsDoc).createTreeWalker(domNode,
                                        NodeFilter.SHOW_TEXT | NodeFilter.SHOW_CDATA_SECTION,
                                        new TextNodeFilter(), entityReferenceExpansion);
            Node   value    = valueWalker.nextNode();
            String valueStr = value.getNodeValue();

            switch (idStr) {
            case "45456" :
               patientName = valueStr;

               break;

            case "1234556" :
               patientIEN = valueStr;

               break;

            case "43325456" :
               authorName = valueStr;

               break;

            case "433254561" :
               authorIEN = valueStr;

               break;

            case "4332545615" :
               systemSource = valueStr;

               break;

            case "3345456" :
               documentTimestamp = valueStr;

               break;

            case "8845456" :
               institutionName = valueStr;

               break;

            case "7745456" :
               institutionNumber = valueStr;

               break;
            }

            System.out.println(sectionNode.getAttributes().getNamedItem("text").getNodeValue() + " "
                               + domNode.getAttributes().getNamedItem("text").getNodeValue() + " id: "
                               + idStr + " value: " + valueStr);

            PncsLegoMap pcnsLegoMap = checkPcnsMap(idStr, valueStr);

            if (pcnsLegoMap != null) {
               System.out.println("   Mapped");
               mappedCount++;

               if (documentUuid != null) {
                  PncsLegoMap pcnsLegoSectionMap = checkPcnsMap(sectionIdStr, sectionValueStr);

                  if (pcnsLegoSectionMap == null) {
                     System.out.println("   Section NOT Mapped.  sectionIdStr: " + sectionIdStr
                                        + " sectionValueStr: '" + sectionValueStr
                                        + "'\n   Assertion will not persist because section is not mapped.");
                  } else {
                     EntityManager     em   = JpaManager.getEntityManager();
                     EntityTransaction entr = em.getTransaction();

                     if (!entr.isActive()) {
                        entr.begin();
                     }

                     doc = checkDocumentTable(documentUuid);

                     Intervals  interval      = checkIntervalTable(intervalUuid);
                     Assertions assertion     = new Assertions();
                     UUID       assertionUuid =
                        Type5UuidFactory.get(UUID.fromString("c7a482e1-7b90-11e1-b0c4-0800200c9a66"),
                                             documentUuid.toString() + sequenceInDoc);

                     assertion.setAuuid(assertionUuid.toString());
                     assertion.setDiscernableEnid(pcnsLegoMap.getDiscernableEnid());
                     assertion.setDnid(doc);
                     assertion.setInid(interval);
                     assertion.setQualifierEnid(pcnsLegoMap.getQualifierEnid());
                     assertion.setSectionEnid(pcnsLegoSectionMap.getDiscernableEnid());
                     assertion.setSeqInDoc(sequenceInDoc);
                     assertion.setValueEnid(pcnsLegoMap.getValueEnid());

                     if (!doc.getAssertionsCollection().contains(assertion)) {
                        doc.getAssertionsCollection().add(assertion);
                        em.persist(doc);
                        System.out.println("Updated doc: " + doc + " with: " + assertion);
                     } else {
                        System.out.println("\n\nDoc: " + doc + " already contains: " + assertion + "\n");
                     }

                     entr.commit();
                  }
               }
            } else {
               System.out.println("   NOT Mapped");
               unmappedCount++;
            }
         }

         domNode = walker.nextNode();
      }

      System.out.println("   Mapped: " + mappedCount);
      System.out.println("   NOT Mapped: " + unmappedCount);

      return doc;
   }

   //~--- get methods ---------------------------------------------------------

   private Documents getDocument(Documents doc, UUID documentUuid, Intervals interval, Persons patient,
                                 Persons provider, String institutionName, String institutionNumber) {
      int documentNid;

      doc = checkDocumentTable(documentUuid);

      if (doc == null) {

         // add document to document table
         EntityManager     em   = JpaManager.getEntityManager();
         EntityTransaction entr = em.getTransaction();

         entr.begin();

         Documents docObj = new Documents();

         docObj.setDuuid(documentUuid.toString());
         docObj.setInid(interval);
         docObj.setPatientnid(patient);
         docObj.setProvidernid(provider);
         docObj.setInstitutionName(institutionName);
         docObj.setInstitutionNumber(institutionNumber);
         em.persist(docObj);
         entr.commit();
         System.out.println("added doc: " + docObj);
         doc         = docObj;
         documentNid = doc.getDnid();
      }

      return doc;
   }

   private Intervals getInterval(UUID intervalUuid, Date timestamp) {
      int       intervalNid;
      Intervals interval = checkIntervalTable(intervalUuid);

      if (interval == null) {
         EntityManager     em   = JpaManager.getEntityManager();
         EntityTransaction entr = em.getTransaction();

         entr.begin();

         Intervals intervalObj = new Intervals();

         intervalObj.setIstart(timestamp);
         intervalObj.setIend(timestamp);
         intervalObj.setIuuid(intervalUuid.toString());
         em.persist(intervalObj);
         entr.commit();
         System.out.println("added interval for doc: " + intervalObj);
         interval    = intervalObj;
         intervalNid = interval.getInid();
      }

      return interval;
   }

   private Persons getPatient(UUID patientUuid, String patientName, String patientIEN,
                              String institutionName) {
      Persons patient = checkPersonTable(patientUuid);

      if (patient == null) {

         // add patient to person table
         EntityManager     em   = JpaManager.getEntityManager();
         EntityTransaction entr = em.getTransaction();

         if (!entr.isActive()) {
            entr.begin();
         }

         Persons personObj = new Persons();

         personObj.setDob(new Date());

         String[] names = patientName.split(",");

         if (names[1].contains(" ")) {
            String[] nameParts = names[1].split(" ", 2);

            personObj.setGivenName(nameParts[0]);
            personObj.setMiddleName(nameParts[1]);
         } else {
            personObj.setGivenName(names[1]);
         }

         personObj.setFamilyName(names[0]);
         personObj.setPuuid(patientUuid.toString());
         personObj.setPInternalEntryNumber(patientIEN);
         personObj.setIdentity(patientIEN);
         personObj.setIdAssigningAuthority("USVHA");
         personObj.setIdAssigningFacility(institutionName);
         em.persist(personObj);
         entr.commit();
         System.out.println("added patient Persons: " + personObj);
         patient = personObj;
      }

      return patient;
   }

   private Persons getProvider(UUID providerUuid, String authorName, String authorIEN,
                               String institutionName) {
      Persons provider = checkPersonTable(providerUuid);

      if (provider == null) {

         // add provider to person table
         EntityManager     em   = JpaManager.getEntityManager();
         EntityTransaction entr = em.getTransaction();

         entr.begin();

         Persons personObj = new Persons();

         personObj.setDob(new Date());

         String[] names = authorName.split(",");

         if (names[1].contains(" ")) {
            String[] nameParts = names[1].split(" ", 2);

            personObj.setGivenName(nameParts[0]);
            personObj.setMiddleName(nameParts[1]);
         } else {
            personObj.setGivenName(names[1]);
         }

         personObj.setFamilyName(names[0]);
         personObj.setPuuid(providerUuid.toString());
         personObj.setIdentity(authorIEN);
         personObj.setPInternalEntryNumber(authorIEN);
         personObj.setIdAssigningAuthority("USVHA");
         personObj.setIdAssigningFacility(institutionName);
         em.persist(personObj);
         entr.commit();
         System.out.println("added provider Persons: " + personObj);
         provider = personObj;
      }

      return provider;
   }

   //~--- inner classes -------------------------------------------------------

   private static class SectionNodeFilter implements NodeFilter {
      @Override
      public short acceptNode(Node n) {
         if (n.getNodeName().equals("section")) {
            return NodeFilter.FILTER_ACCEPT;
         }

         if (n.getNodeName().equals("obj")) {
            return NodeFilter.FILTER_ACCEPT;
         }

         return NodeFilter.FILTER_SKIP;
      }
   }


   private static class TextNodeFilter implements NodeFilter {
      @Override
      public short acceptNode(Node n) {
         if (n.getNodeType() == Node.TEXT_NODE) {
            return NodeFilter.FILTER_ACCEPT;
         }

         if (n.getNodeType() == Node.CDATA_SECTION_NODE) {
            return NodeFilter.FILTER_ACCEPT;
         }

         return NodeFilter.FILTER_SKIP;
      }
   }
}
