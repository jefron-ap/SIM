/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.arena;

import java.io.IOException;
import javafx.scene.Node;

/**
 *
 * @author kec
 */
public interface ConceptViewComponentBI extends Comparable<ConceptViewComponentBI> {
    ViewComponentType getViewComponentType();
    Node getNode() throws IOException;
    String getText();
}
