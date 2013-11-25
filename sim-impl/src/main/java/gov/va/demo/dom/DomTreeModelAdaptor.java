
/*
 * @(#)DomEcho02.java	1.9 98/11/10
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package gov.va.demo.dom;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This file is derived from DomEcho02.java. License is as described above.
 *
 * @author kec
 */
public class DomTreeModelAdaptor {
    // Global value so it can be ref'd by the tree-adapter

    Document document;
    DomToTreeModelAdapter adaptor;

    public DomTreeModelAdaptor(Document document) {
        this.document = document;
        this.adaptor = new DomToTreeModelAdapter();
    }

    public TreeModel getTreeModel() {
        return adaptor;
    }
    // An array of names for DOM node-types
    static final String[] typeName = {
        "none",
        "Element",
        "Attr",
        "Text",
        "CDATA",
        "EntityRef",
        "Entity",
        "ProcInstr",
        "Comment",
        "Document",
        "DocType",
        "DocFragment",
        "Notation",};

    // This class wraps a DOM node and returns the text we want to
    // display in the tree. It also returns children, index values,
    // and child counts.
    public class AdapterNode {

        org.w3c.dom.Node domNode;

        // Construct an Adapter node from a DOM node
        public AdapterNode(org.w3c.dom.Node node) {
            domNode = node;
        }

        // Return a string that identifies this node in the tree
        // *** Refer to table at top of org.w3c.dom.Node ***
        @Override
        public String toString() {
            if (domNode == null) {
                return "null";
            }
            String s = typeName[domNode.getNodeType()];
            String nodeName = domNode.getNodeName();
            if (!nodeName.startsWith("#")) {
                s += ": " + nodeName;
            }
            if (domNode.getNodeValue() != null) {
                if (s.startsWith("ProcInstr")) {
                    s += ", ";
                } else {
                    s += ": ";
                }
                // Trim the value to get rid of NL's at the front
                String t = domNode.getNodeValue().trim();
                int x = t.indexOf("\n");
                if (x >= 0) {
                    t = t.substring(0, x);
                }
                s += t;
            }
            return s;
        }


        /*
         * Return children, index, and count values
         */
        public int index(AdapterNode childToTest) {
            int index = 0;
            int count = childCount();
            for (int i = 0; index < count; i++) {
                Node child = domNode.getChildNodes().item(i);
                if (child.getNodeType() == 1) {
                    if (childToTest.domNode == childToTest.domNode) {
                        return index;
                    }
                    index++;
                }
            }
            return -1; // Should never get here.
        }

        public AdapterNode child(int searchIndex) {
            int index = 0;
            Node child = null;
            for (int i = 0; index <= searchIndex; i++) {
                child = domNode.getChildNodes().item(i);
                if (child.getNodeType() == 1) {
                    index++;
                }
            }

            return new AdapterNode(child);
        }

        public int childCount() {
            int count = 0;
            if (domNode == null || domNode.getChildNodes() == null) {
                return 0;
            }
            for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
                Node child = domNode.getChildNodes().item(i);
                if (child.getNodeType() == 1) {
                    count++;
                }
            }
            return count;
        }
    }

    // This adapter converts the current Document (a DOM) into 
    // a JTree model. 
    public class DomToTreeModelAdapter
            implements TreeModel {
        // Basic TreeModel operations

        @Override
        public Object getRoot() {
            //System.err.println("Returning root: " +document);
            return new AdapterNode(document);
        }

        @Override
        public boolean isLeaf(Object aNode) {
            // Determines whether the icon shows up to the left.
            // Return true for any node with no children
            AdapterNode node = (AdapterNode) aNode;
            if (node.childCount() > 0) {
                return false;
            }
            return true;
        }

        @Override
        public int getChildCount(Object parent) {
            AdapterNode node = (AdapterNode) parent;
            return node.childCount();
        }

        @Override
        public Object getChild(Object parent, int index) {
            AdapterNode node = (AdapterNode) parent;
            return node.child(index);
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            AdapterNode node = (AdapterNode) parent;
            return node.index((AdapterNode) child);
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            // Null. We won't be making changes in the GUI
            // If we did, we would ensure the new value was really new,
            // adjust the model, and then fire a TreeNodesChanged event.
        }

        /*
         * Use these methods to add and remove event listeners. (Needed to satisfy TreeModel interface, but
         * not used.)
         */
        private Vector listenerList = new Vector();

        @Override
        public void addTreeModelListener(TreeModelListener listener) {
            if (listener != null
                    && !listenerList.contains(listener)) {
                listenerList.addElement(listener);
            }
        }

        @Override
        public void removeTreeModelListener(TreeModelListener listener) {
            if (listener != null) {
                listenerList.removeElement(listener);
            }
        }

        // Note: Since XML works with 1.1, this example uses Vector.
        // If coding for 1.2 or later, though, I'd use this instead:
        //   private List listenerList = new LinkedList();
        // The operations on the List are then add(), remove() and
        // iteration, via:
        //  Iterator it = listenerList.iterator();
        //  while ( it.hasNext() ) {
        //    TreeModelListener listener = (TreeModelListener)it.next();
        //    ...
        //  }

        /*
         * Invoke these methods to inform listeners of changes. (Not needed for this example.) Methods taken
         * from TreeModelSupport class described at
         * http://java.sun.com/products/jfc/tsc/articles/jtree/index.html That architecture (produced by Tom
         * Santos and Steve Wilson) is more elegant. I just hacked 'em in here so they are immediately at
         * hand.
         */
        public void fireTreeNodesChanged(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener =
                        (TreeModelListener) listeners.nextElement();
                listener.treeNodesChanged(e);
            }
        }

        public void fireTreeNodesInserted(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener =
                        (TreeModelListener) listeners.nextElement();
                listener.treeNodesInserted(e);
            }
        }

        public void fireTreeNodesRemoved(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener =
                        (TreeModelListener) listeners.nextElement();
                listener.treeNodesRemoved(e);
            }
        }

        public void fireTreeStructureChanged(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener =
                        (TreeModelListener) listeners.nextElement();
                listener.treeStructureChanged(e);
            }
        }
    }
}
