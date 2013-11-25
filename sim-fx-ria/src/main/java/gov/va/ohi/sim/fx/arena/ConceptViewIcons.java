/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.arena;

import gov.va.ohi.sim.fx.helper.IconHelper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author kec
 */
public enum ConceptViewIcons {

    TOGGLE_SMALL(IconHelper.setupImage("/fugue/16x16/icons-shadowless/toggle-small.png")),
    TOGGLE_SMALL_EXPAND(IconHelper.setupImage("/fugue/16x16/icons-shadowless/toggle-small-expand.png")),
    
    ;
    private Image icon;

    //~--- constructors -----------------------------------------------------
    private ConceptViewIcons(Image icon) {
        this.icon = icon;
    }

    public ImageView getImageView() {
        return IconHelper.getImageView(icon);
    }
}
