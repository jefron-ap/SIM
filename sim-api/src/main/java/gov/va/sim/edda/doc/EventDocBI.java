/*
 * United States Government Work
 *
 * Veterans Health Administration
 * US Department of Veterans Affairs
 *
 * US Federal Government Agencies are required
 * to release their works as Public Domain.
 *
 * http://www.copyright.gov/title17/92chap1.html#105
 */
package gov.va.sim.edda.doc;

import gov.va.sim.act.ActBI;
import gov.va.sim.id.IdentifiableInstanceBI;
import gov.va.sim.measurement.MeasurementBI;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.UUID;
import org.ihtsdo.tk.api.concept.ConceptVersionBI;

/**
 * Documented Event: An event documented and included in the health data archive
 * 
 * a Documented Event contain one or more Acts
 * 
 * Acts can be requests or assertions...
 * @author Keith E. Campbell
 */
public interface EventDocBI extends IdentifiableInstanceBI {

    /**
     *
     * @return a mutable collection of acts
     */
    Collection<ActBI> getActs();

    ConceptVersionBI getEncodingPath();

    long getEncodingPathPosition();

    EventDocBI getParentEventDoc();

    void setParentEventDoc(EventDocBI parent) throws PropertyVetoException;

    Collection<EventDocBI> getChildEventDocs();

    ConceptVersionBI getEventType();

    void setEventType(ConceptVersionBI eventTypeUuid) throws PropertyVetoException;

    UUID getPrimaryObserver();

    void setPrimaryObserver(UUID primaryObserverUuid) throws PropertyVetoException;

    /**
     *
     * @return an unmodifiable collection of identifiers for observers.
     */
    Collection<UUID> getObservers();

    UUID getPrincipalSubject();

    void setPrincipalSubject(UUID principalSubjectUuid) throws PropertyVetoException;

    /**
     *
     * @return a mutable collection of identifiers for subjects.
     */
    Collection<UUID> getSubjects();

    ConceptVersionBI getServiceUnit();

    void setServiceUnit(ConceptVersionBI serviceUnitUuid) throws PropertyVetoException;

    ConceptVersionBI getStatus();

    void setStatus(UUID statusUuid) throws PropertyVetoException;

    MeasurementBI getEncounterInterval();

    void setEncounterInterval(MeasurementBI interval) throws PropertyVetoException;
}
