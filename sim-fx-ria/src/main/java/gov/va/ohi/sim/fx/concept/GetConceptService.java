/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.concept;

import java.util.UUID;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ihtsdo.fxmodel.concept.FxConcept;
import org.ihtsdo.fxmodel.fetchpolicy.RefexPolicy;
import org.ihtsdo.fxmodel.fetchpolicy.RelationshipPolicy;
import org.ihtsdo.fxmodel.fetchpolicy.VersionPolicy;

/**
 *
 * @author kec
 */
public class GetConceptService extends Service<FxConcept> {

    private UUID conceptUuid;
    private VersionPolicy versionPolicy = VersionPolicy.ALL_VERSIONS;
    private RefexPolicy refexPolicy = RefexPolicy.REFEX_MEMBERS;
    private RelationshipPolicy relationshipPolicy = RelationshipPolicy.ORIGINATING_AND_DESTINATION_RELATIONSHIPS;

    @Override
    protected Task<FxConcept> createTask() {
        return new GetConceptTask(conceptUuid, versionPolicy, refexPolicy, relationshipPolicy);
    }

    public UUID getConceptUuid() {
        return conceptUuid;
    }

    public void setConceptUuid(UUID conceptUuid) {
        this.conceptUuid = conceptUuid;
    }

    public VersionPolicy getVersionPolicy() {
        return versionPolicy;
    }

    public void setVersionPolicy(VersionPolicy versionPolicy) {
        this.versionPolicy = versionPolicy;
    }

    public RefexPolicy getRefexPolicy() {
        return refexPolicy;
    }

    public void setRefexPolicy(RefexPolicy refexPolicy) {
        this.refexPolicy = refexPolicy;
    }

    public RelationshipPolicy getRelationshipPolicy() {
        return relationshipPolicy;
    }

    public void setRelationshipPolicy(RelationshipPolicy relationshipPolicy) {
        this.relationshipPolicy = relationshipPolicy;
    }
}
