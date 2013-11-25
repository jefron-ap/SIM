/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.taxonomy;

import gov.va.ohi.sim.fx.helper.IconHelper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author kec
 */
public enum SimTreeIcons {
    
        PRIMITIVE_SINGLE_PARENT(IconHelper.setupImage("/fugue/16x16/icons-shadowless/navigation-nowhere-button-white.png")),
        PRIMITIVE_MULTI_PARENT_CLOSED(IconHelper.setupImage("/fugue/16x16/icons-shadowless/navigation-090-button-white.png")),
        PRIMITIVE_MULTI_PARENT_OPEN(IconHelper.setupImage("/fugue/16x16/icons-shadowless/navigation-045-button-white.png")),
        DEFINED_SINGLE_PARENT(IconHelper.setupImage("/fugue/16x16/icons-shadowless/navigation-nowhere-2.png")),
        DEFINED_MULTI_PARENT_CLOSED(IconHelper.setupImage("/fugue/16x16/icons-shadowless/navigation-090.png")),
        DEFINED_MULTI_PARENT_OPEN(IconHelper.setupImage("/fugue/16x16/icons-shadowless/navigation-045.png")),
        ROOT(IconHelper.setupImage("/fugue/16x16/icons-shadowless/node.png")),
        
        GREEN_TICK(IconHelper.setupImage("/fugue/16x16/icons-shadowless/tick.png")),
        RED_X(IconHelper.setupImage("/fugue/16x16/icons-shadowless/cross.png")),
 
        TAXONOMY_OPEN(IconHelper.setupImage("/fugue/16x16/icons-shadowless/plus-small.png")),
        TAXONOMY_CLOSE(IconHelper.setupImage("/fugue/16x16/icons-shadowless/minus-small.png")),
        
        
        ;
        private Image icon;

        //~--- constructors -----------------------------------------------------
        private SimTreeIcons(Image icon) {
            this.icon = icon;
        }
        
    public ImageView getImageView() {
        return IconHelper.getImageView(icon);
    }
 
}