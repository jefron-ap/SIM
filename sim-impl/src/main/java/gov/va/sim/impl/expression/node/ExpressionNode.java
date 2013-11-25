
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.sim.impl.expression.node;

//~--- non-JDK imports --------------------------------------------------------
import gov.va.sim.act.expression.ExpressionRelBI;
import gov.va.sim.act.expression.node.ExpressionNodeBI;

import org.ihtsdo.tk.api.concept.ConceptVersionBI;

import gov.va.sim.impl.expression.ExpressionRel;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyVetoException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 *
 * @author kec
 */
public abstract class ExpressionNode<T extends Object> implements ExpressionNodeBI<T> {

    List<ExpressionRelBI> rels = new ArrayList<>();
    T value;

    //~--- methods -------------------------------------------------------------
    @Override
    public ExpressionRelBI addRel(ConceptVersionBI relType, ExpressionNodeBI<?> relDestination)
            throws PropertyVetoException {
        ExpressionRel rel = new ExpressionRel();

        rel.setOrigin(this);
        rel.setType(relType);
        rel.setDestination(relDestination);
        rels.add(rel);

        return rel;
    }

    //~--- get methods ---------------------------------------------------------
    @Override
    public ExpressionRelBI[] getAllRels() throws IOException {
        return rels.toArray(new ExpressionRelBI[rels.size()]);
    }

    @Override
    public final T getValue() {
        return value;
    }

    //~--- set methods ---------------------------------------------------------
    public void setValue(T value) {
        this.value = value;
    }

    public abstract void addTag(StringBuilder sb);

    public abstract void addAttributes(StringBuilder sb, boolean verbose) throws IOException;

    public abstract void appendForHash(StringBuilder sb) throws IOException;

    @Override
    public void generateXml(StringBuilder sb, boolean verbose) throws IOException {
        sb.append("<");
        addTag(sb);
        addAttributes(sb, verbose);

        if (rels.isEmpty()) {
            sb.append("/>");
        } else {
            sb.append(">");

            for (ExpressionRelBI r : rels) {
                sb.append("<rel");
                if (verbose) {
                    sb.append(" typeFsn=\"");
                    sb.append(StringEscapeUtils.escapeXml(r.getType().getFsnDescsActive().iterator().next().getText()));
                    sb.append("\" typeUuid=\"");
                } else {
                    sb.append(" typeUuid=\"");
                }
                sb.append(r.getType().getPrimUuid());
                sb.append("\">");
                r.getDestination().generateXml(sb, verbose);
                sb.append("</rel>");
            }

            sb.append("</");
            addTag(sb);
            sb.append(">");
        }
    }

    @Override
    public void generateHtml(StringBuilder sb, boolean verbose) throws IOException {
        generateHtml(sb, verbose, 1);
    }

    @Override
    public void generateHtml(StringBuilder sb, boolean verbose, int depth) throws IOException {
        if (verbose) {
            sb.append(getFullySpecifiedText());
        } else {
            sb.append(getPreferredText());
        }

        if (rels.isEmpty()) {
            ;
        } else {
            for (ExpressionRelBI r : rels) {
                sb.append("<br/>");
                pad(sb, depth);
                if (verbose) {
                    sb.append(StringEscapeUtils.escapeXml(r.getType().getFsnDescsActive().iterator().next().getText()));
                } else {
                    sb.append(StringEscapeUtils.escapeXml(r.getType().getPrefDescsActive().iterator().next().getText()));
                }
                sb.append(": ");
                r.getDestination().generateHtml(sb, verbose, depth+1);
            }
        }
    }
    
    private static void pad(StringBuilder sb, int depth)  {
        int padding = 5;
        for (int i = 0; i < depth * padding; i++) {
            sb.append("&nbsp;");
        }
    }

    @Override
    public void appendStringForUuidHash(StringBuilder sb) throws IOException {
        sb.append("[");
        appendForHash(sb);
        if (!rels.isEmpty()) {
            for (ExpressionRelBI r : rels) {
                sb.append("(");
                sb.append(r.getType().getPrimUuid());
                r.getDestination().appendStringForUuidHash(sb);
                sb.append(")");
            }
        }
        sb.append("]");
    }
}
