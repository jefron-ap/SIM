
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.sim.impl.expression.node;

//~--- non-JDK imports --------------------------------------------------------
import gov.va.sim.act.expression.node.ConceptNodeBI;

import org.ihtsdo.tk.api.concept.ConceptVersionBI;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author kec
 */
public class ConceptNode extends ExpressionNode<ConceptVersionBI> implements ConceptNodeBI {

    @Override
    public UUID getConceptUuid() {
        if (value == null) {
            return null;
        }
        return value.getPrimUuid();
    }

    @Override
    public String getFullySpecifiedText() throws IOException {
        return getFullySpecifiedDesc();
    }

    @Override
    public String getPreferredText() throws IOException {
        return getPreferredDesc();
    }

    @Override
    public String getFullySpecifiedDesc() throws IOException {
        if (value == null) {
            return "Null value in concept node.";
        }
        if (value.getFsnDescsActive() != null) {
            return value.getFsnDescsActive().iterator().next().getText();
        }
        return "No fsn for: " + value.toLongString();
    }

    @Override
    public String getPreferredDesc() throws IOException {
        return value.getPrefDescsActive().iterator().next().getText();
    }

    @Override
    public void addTag(StringBuilder sb) {
        sb.append("concept");
    }

    @Override
    public void addAttributes(StringBuilder sb, boolean verbose) throws IOException {
        if (verbose) {
            sb.append(" fsn=\"");
            sb.append(StringEscapeUtils.escapeXml(getFullySpecifiedDesc()));
            sb.append("\" uuid=\"");
        } else {
            sb.append(" uuid=\"");
        }
        sb.append(getConceptUuid());
        sb.append("\"");
    }

    @Override
    public void appendForHash(StringBuilder sb) throws IOException {
        sb.append(getConceptUuid().toString());
    }

    @Override
    public String toString() {
        try {
            return "ConceptNode{" + getFullySpecifiedDesc() + '}';
        } catch (IOException ex) {
            return "ConceptNode{" + ex.getLocalizedMessage() + '}';
        }
    }
}
