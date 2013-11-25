/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.arena;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeCell;

/**
 *
 * @author kec
 */
public class ConceptViewCell extends TreeCell<ConceptViewComponentBI> {

    public ConceptViewCell() {
        this.setDisclosureNode(null);
        this.setSkinClassName("gov.va.ohi.sim.fx.arena.ConceptViewCellSkin");
    }

    @Override
    protected void updateItem(ConceptViewComponentBI t, boolean bln) {
        super.updateItem(t, bln);
        if (t != null) {
            setText(t.getText());
            try {
                setGraphic(t.getNode());
            } catch (IOException ex) {
                Logger.getLogger(ConceptViewCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            setText("");
            setGraphic(null);
        }
    }
}
