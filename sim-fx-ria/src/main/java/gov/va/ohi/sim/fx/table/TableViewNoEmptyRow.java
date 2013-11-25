/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.table;
/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import com.sun.javafx.collections.transformation.SortableList;
import com.sun.javafx.collections.transformation.TransformationList;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import com.sun.javafx.collections.annotations.ReturnsUnmodifiableCollection;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.WeakListChangeListener;
import java.util.HashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The TableViewNoEmptyRow control is designed to visualize an unlimited number of rows
 * of data, broken out into columns. A TableViewNoEmptyRow is therefore very similar to the
 * {@link ListView} control, with the addition of support for columns. For an
 * example on how to create a TableViewNoEmptyRow, refer to the 'Creating a TableViewNoEmptyRow'
 * control section below.
 *
 * <p>The TableViewNoEmptyRow control has a number of features, including:
 * <ul>
 * <li>Powerful {@link TableColumnNoEmptyRow} API:
 *   <ul>
 *   <li>Support for {@link TableColumnNoEmptyRow#cellFactoryProperty() cell factories} to
 *      easily customize {@link Cell cell} contents in both rendering and editing
 *      states.
 *   <li>Specification of {@link #minWidthProperty() minWidth}/
 *      {@link #prefWidthProperty() prefWidth}/{@link #maxWidthProperty() maxWidth},
 *      and also {@link TableColumnNoEmptyRow#resizableProperty() fixed width columns}.
 *   <li>Width resizing by the user at runtime.
 *   <li>Column reordering by the user at runtime.
 *   <li>Built-in support for {@link TableColumnNoEmptyRow#getColumns() column nesting}
 *   </ul>
 * <li>Different {@link #columnResizePolicyProperty() resizing policies} to 
 *      dictate what happens when the user resizes columns.
 * <li>Support for {@link #getSortOrder() multiple column sorting} by clicking 
 *      the column header (hold down Shift keyboard key whilst clicking on a 
 *      header to sort by multiple columns).
 * </ul>
 * </p>
 *
 * <p>Note that TableViewNoEmptyRow is intended to be used for visualize data - it is not
 * intended to be used for laying out your user interface. If you want to lay
 * your user interface out in a grid-like fashion, consider the 
 * {@link GridPane} layout.</p>
 *
 * <h2>Creating a TableViewNoEmptyRow</h2>
 *
 * <p>Creating a TableViewNoEmptyRow is a multi-step process, and also depends on the
 * underlying data model needing to be represented. For this example we'll use
 * an ObservableList<Person>, as it is the simplest way of showing data in a 
 * TableViewNoEmptyRow. The <code>Person</code> class will consist of a first
 * name and last name properties. That is:
 * 
 * <pre>
 * {@code
 * public class Person {
 *     private StringProperty firstName;
 *     public void setFirstName(String value) { firstNameProperty().set(value); }
 *     public String getFirstName() { return firstNameProperty().get(); }
 *     public StringProperty firstNameProperty() { 
 *         if (firstName == null) firstName = new SimpleStringProperty(this, "firstName");
 *         return firstName; 
 *     }
 * 
 *     private StringProperty lastName;
 *     public void setLastName(String value) { lastNameProperty().set(value); }
 *     public String getLastName() { return lastNameProperty().get(); }
 *     public StringProperty lastNameProperty() { 
 *         if (lastName == null) lastName = new SimpleStringProperty(this, "lastName");
 *         return lastName; 
 *     } 
 * }}</pre>
 * 
 * <p>Firstly, a TableViewNoEmptyRow instance needs to be defined, as such:
 * 
 * <pre>
 * {@code
 * TableViewNoEmptyRow<Person> table = new TableViewNoEmptyRow<Person>();}</pre>
 *
 * <p>With the basic table defined, we next focus on the data model. As mentioned,
 * for this example, we'll be using a ObservableList<Person>. We can immediately
 * set such a list directly in to the TableViewNoEmptyRow, as such:
 *
 * <pre>
 * {@code
 * ObservableList<Person> teamMembers = getTeamMembers();
 * table.setItems(teamMembers);}</pre>
 * 
 * <p>With the items set as such, TableViewNoEmptyRow will automatically update whenever
 * the <code>teamMembers</code> list changes. If the items list is available
 * before the TableViewNoEmptyRow is instantiated, it is possible to pass it directly into
 * the constructor. 
 * 
 * <p>At this point we now have a TableViewNoEmptyRow hooked up to observe the 
 * <code>teamMembers</code> observableList. The missing ingredient 
 * now is the means of splitting out the data contained within the model and 
 * representing it in one or more {@link TableColumnNoEmptyRow TableColumnNoEmptyRow} instances. To 
 * create a two-column TableViewNoEmptyRow to show the firstName and lastName properties,
 * we extend the last code sample as follows:
 * 
 * <pre>
 * {@code
 * ObservableList<Person> teamMembers = ...;
 * table.setItems(teamMembers);
 * 
 * TableColumnNoEmptyRow<Person,String> firstNameCol = new TableColumnNoEmptyRow<Person,String>("First Name");
 * firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
 * TableColumnNoEmptyRow<Person,String> lastNameCol = new TableColumnNoEmptyRow<Person,String>("Last Name");
 * lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
 * 
 * table.getColumns().setAll(firstNameCol, lastNameCol);}</pre>
 * 
 * <p>With the code shown above we have fully defined the minimum properties
 * required to create a TableViewNoEmptyRow instance. Running this code (assuming the
 * people ObservableList is appropriately created) will result in a TableViewNoEmptyRow being
 * shown with two columns for firstName and lastName. Any other properties of the
 * Person class will not be shown, as no TableColumns are defined.
 * 
 * <h3>TableViewNoEmptyRow support for classes that don't contain properties</h3>
 *
 * <p>The code shown above is the shortest possible code for creating a TableViewNoEmptyRow
 * when the domain objects are designed with JavaFX properties in mind 
 * (additionally, {@link javafx.scene.control.cell.PropertyValueFactory} supports
 * normal JavaBean properties too, although there is a caveat to this, so refer 
 * to the class documentation for more information). When this is not the case, 
 * it is necessary to provide a custom cell value factory. More information
 * about cell value factories can be found in the {@link TableColumnNoEmptyRow} API 
 * documentation, but briefly, here is how a TableColumnNoEmptyRow could be specified:
 * 
 * <pre>
 * {@code
 * firstNameCol.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
 *     public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
 *         // p.getValue() returns the Person instance for a particular TableViewNoEmptyRow row
 *         return p.getValue().firstNameProperty();
 *     }
 *  });
 * }}</pre>
 * 
 * <h3>TableViewNoEmptyRow Selection / Focus APIs</h3>
 * <p>To track selection and focus, it is necessary to become familiar with the
 * {@link SelectionModel} and {@link FocusModel} classes. A TableViewNoEmptyRow has at most
 * one instance of each of these classes, available from 
 * {@link #selectionModelProperty() selectionModel} and 
 * {@link #focusModelProperty() focusModel} properties respectively.
 * Whilst it is possible to use this API to set a new selection model, in
 * most circumstances this is not necessary - the default selection and focus
 * models should work in most circumstances.
 * 
 * <p>The default {@link SelectionModel} used when instantiating a TableViewNoEmptyRow is
 * an implementation of the {@link MultipleSelectionModel} abstract class. 
 * However, as noted in the API documentation for
 * the {@link MultipleSelectionModel#selectionModeProperty() selectionMode}
 * property, the default value is {@link SelectionMode#SINGLE}. To enable 
 * multiple selection in a default TableViewNoEmptyRow instance, it is therefore necessary
 * to do the following:
 * 
 * <pre>
 * {@code 
 * tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);}</pre>
 *
 * <h3>Customizing TableViewNoEmptyRow Visuals</h3>
 * <p>The visuals of the TableViewNoEmptyRow can be entirely customized by replacing the 
 * default {@link #rowFactoryProperty() row factory}. A row factory is used to
 * generate {@link TableRow} instances, which are used to represent an entire
 * row in the TableViewNoEmptyRow. 
 * 
 * <p>In many cases, this is not what is desired however, as it is more commonly
 * the case that cells be customized on a per-column basis, not a per-row basis.
 * It is therefore important to note that a {@link TableRow} is not a 
 * {@link TableCell}. A  {@link TableRow} is simply a container for zero or more
 * {@link TableCell}, and in most circumstances it is more likely that you'll 
 * want to create custom TableCells, rather than TableRows. The primary use case
 * for creating custom TableRow instances would most probably be to introduce
 * some form of column spanning support.
 * 
 * <p>You can create custom {@link TableCell} instances per column by assigning 
 * the appropriate function to the TableColumnNoEmptyRow
 * {@link TableColumnNoEmptyRow#cellFactoryProperty() cell factory} property.
 * 
 * <p>See the {@link Cell} class documentation for a more complete
 * description of how to write custom Cells.
 *
 * @see TableColumnNoEmptyRow
 * @see TablePositionNoEmptyRow
 * @param <S> The type of the objects contained within the TableViewNoEmptyRow items list.
 */
@DefaultProperty("items")
public class TableViewNoEmptyRow<S> extends Control {
    
    /***************************************************************************
     *                                                                         *
     * Static properties and methods                                           *
     *                                                                         *
     **************************************************************************/

    // strings used to communicate via the TableViewNoEmptyRow properties map between
    // the control and the skin. Because they are private here, the strings
    // are also duplicated in the TableViewSkin class - so any changes to these
    // strings must also be duplicated there
    private static final String SET_CONTENT_WIDTH = "TableView.contentWidth";
    private static final String REFRESH = "TableView.refresh";
    
//    /**
//     * Parent event for any TableViewNoEmptyRow edit event.
//     */
//    @SuppressWarnings("unchecked")
//    public static <S> EventType<EditEvent<S>> editAnyEvent() {
//        return (EventType<EditEvent<S>>) EDIT_ANY_EVENT;
//    }
//    private static final EventType<?> EDIT_ANY_EVENT =
//            new EventType<EditEvent<Object>>(Event.ANY, "EDIT");
//
//    /**
//     * Indicates that the user has performed some interaction to start an edit
//     * event, or alternatively the {@link #edit(int, javafx.scene.control.TableColumnNoEmptyRow)}
//     * method has been called.
//     */
//    @SuppressWarnings("unchecked")
//    public static <S> EventType<EditEvent<S>> editStartEvent() {
//        return (EventType<EditEvent<S>>) EDIT_START_EVENT;
//    }
//    private static final EventType<?> EDIT_START_EVENT =
//            new EventType<EditEvent<Object>>(editAnyEvent(), "EDIT_START");
//
//    /**
//     * Indicates that the editing has been canceled, meaning that no change should
//     * be made to the backing data source.
//     */
//    @SuppressWarnings("unchecked")
//    public static <S> EventType<EditEvent<S>> editCancelEvent() {
//        return (EventType<EditEvent<S>>) EDIT_CANCEL_EVENT;
//    }
//    private static final EventType<?> EDIT_CANCEL_EVENT =
//            new EventType<EditEvent<Object>>(editAnyEvent(), "EDIT_CANCEL");
//
//    /**
//     * Indicates that the editing has been committed by the user, meaning that
//     * a change should be made to the backing data source to reflect the new
//     * data.
//     */
//    @SuppressWarnings("unchecked")
//    public static <S> EventType<EditEvent<S>> editCommitEvent() {
//        return (EventType<EditEvent<S>>) EDIT_COMMIT_EVENT;
//    }
//    private static final EventType<?> EDIT_COMMIT_EVENT =
//            new EventType<EditEvent<Object>>(editAnyEvent(), "EDIT_COMMIT");

    /**
     * <p>Very simple resize policy that just resizes the specified column by the
     * provided delta and shifts all other columns (to the right of the given column)
     * further to the right (when the delta is positive) or to the left (when the
     * delta is negative).
     *
     * <p>It also handles the case where we have nested columns by sharing the new space,
     * or subtracting the removed space, evenly between all immediate children columns.
     * Of course, the immediate children may themselves be nested, and they would
     * then use this policy on their children.
     */
    public static final Callback<ResizeFeatures, Boolean> UNCONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>() {
        @Override public String toString() {
            return "unconstrained-resize";
        }
        
        @Override public Boolean call(ResizeFeatures prop) {
            double result = resize(prop.getColumn(), prop.getDelta());
            return Double.compare(result, 0.0) == 0;
        }
    };

    /**
     * <p>Simple policy that ensures the width of all visible leaf columns in 
     * this table sum up to equal the width of the table itself.
     * 
     * <p>When the user resizes a column width with this policy, the table automatically
     * adjusts the width of the right hand side columns. When the user increases a
     * column width, the table decreases the width of the rightmost column until it
     * reaches its minimum width. Then it decreases the width of the second
     * rightmost column until it reaches minimum width and so on. When all right
     * hand side columns reach minimum size, the user cannot increase the size of
     * resized column any more.
     */
    public static final Callback<ResizeFeatures, Boolean> CONSTRAINED_RESIZE_POLICY = new Callback<ResizeFeatures, Boolean>() {

        private boolean isFirstRun = true;
        
        @Override public String toString() {
            return "constrained-resize";
        }
        
        @Override public Boolean call(ResizeFeatures prop) {
            TableViewNoEmptyRow<?> table = prop.getTable();
            TableColumnNoEmptyRow<?,?> column = prop.getColumn();
            double delta = prop.getDelta();
            
            /*
             * There are two phases to the constrained resize policy:
             *   1) Ensuring internal consistency (i.e. table width == sum of all visible
             *      columns width). This is often called when the table is resized.
             *   2) Resizing the given column by __up to__ the given delta.
             *
             * It is possible that phase 1 occur and there be no need for phase 2 to
             * occur.
             */

            boolean isShrinking;
            double target;
            double totalLowerBound = 0;
            double totalUpperBound = 0;
            
            double tableWidth = table.contentWidth;
            if (tableWidth == 0) return false;

            /*
             * PHASE 1: Check to ensure we have internal consistency. Based on the
             *          Swing JTable implementation.
             */
            // determine the width of all visible columns, and their preferred width
            double colWidth = 0;
            for (TableColumnNoEmptyRow<?,?> col : table.getVisibleLeafColumns()) {
                colWidth += col.getWidth();
            }

            if (Math.abs(colWidth - tableWidth) > 1) {
                isShrinking = colWidth > tableWidth;
                target = tableWidth;

                if (isFirstRun) {
                    // if we are here we have an inconsistency - these two values should be
                    // equal when this resizing policy is being used.
                    for (TableColumnNoEmptyRow<?,?> col : table.getVisibleLeafColumns()) {
                        totalLowerBound += col.getMinWidth();
                        totalUpperBound += col.getMaxWidth();
                    }

                    // We run into trouble if the numbers are set to infinity later on
                    totalUpperBound = totalUpperBound == Double.POSITIVE_INFINITY ?
                        Double.MAX_VALUE :
                        (totalUpperBound == Double.NEGATIVE_INFINITY ? Double.MIN_VALUE : totalUpperBound);

                    for (TableColumnNoEmptyRow<?,?> col : table.getVisibleLeafColumns()) {
                        double lowerBound = col.getMinWidth();
                        double upperBound = col.getMaxWidth();

                        // Check for zero. This happens when the distribution of the delta
                        // finishes early due to a series of "fixed" entries at the end.
                        // In this case, lowerBound == upperBound, for all subsequent terms.
                        double newSize;
                        if (totalLowerBound == totalUpperBound) {
                            newSize = lowerBound;
                        } else {
                            double f = (target - totalLowerBound) / (totalUpperBound - totalLowerBound);
                            newSize = Math.round(lowerBound + f * (upperBound - lowerBound));
                        }

                        double remainder = resize(col, newSize - col.getWidth());

                        target -= newSize + remainder;
                        totalLowerBound -= lowerBound;
                        totalUpperBound -= upperBound;
                    }
                    
                    isFirstRun = false;
                } else {
                    double actualDelta = tableWidth - colWidth;
                    List<TableColumnNoEmptyRow<?,?>> cols = ((TableViewNoEmptyRow)table).getVisibleLeafColumns();
                    resizeColumns(cols, actualDelta);
                }
            }

            // At this point we can be happy in the knowledge that we have internal
            // consistency, i.e. table width == sum of the width of all visible
            // leaf columns.

            /*
             * Column may be null if we just changed the resize policy, and we
             * just wanted to enforce internal consistency, as mentioned above.
             */
            if (column == null) {
                return false;
            }

            /*
             * PHASE 2: Handling actual column resizing (by the user). Based on my own
             *          implementation (based on the UX spec).
             */

            isShrinking = delta < 0;

            // need to find the first leaf column of the given column - it is this
            // column that we actually resize from. If this column is a leaf, then we
            // use it.
            TableColumnNoEmptyRow<?,?> leafColumn = column;
            while (leafColumn.getColumns().size() > 0) {
                leafColumn = leafColumn.getColumns().get(0);
            }

            int colPos = table.getVisibleLeafColumns().indexOf(leafColumn);
            int endColPos = table.getVisibleLeafColumns().size() - 1;

//            System.out.println("resizing " + leafColumn.getText() + ". colPos: " + colPos + ", endColPos: " + endColPos);

            // we now can split the observableArrayList into two subobservableArrayLists, representing all
            // columns that should grow, and all columns that should shrink
            //    var growingCols = if (isShrinking)
            //        then table.visibleLeafColumns[colPos+1..endColPos]
            //        else table.visibleLeafColumns[0..colPos];
            //    var shrinkingCols = if (isShrinking)
            //        then table.visibleLeafColumns[0..colPos]
            //        else table.visibleLeafColumns[colPos+1..endColPos];


            double remainingDelta = delta;
            while (endColPos > colPos && remainingDelta != 0) {
                TableColumnNoEmptyRow<?,?> resizingCol = table.getVisibleLeafColumns().get(endColPos);
                endColPos--;

                // if the column width is fixed, break out and try the next column
                if (! resizingCol.isResizable()) continue;

                // for convenience we discern between the shrinking and growing columns
                TableColumnNoEmptyRow<?,?> shrinkingCol = isShrinking ? leafColumn : resizingCol;
                TableColumnNoEmptyRow<?,?> growingCol = !isShrinking ? leafColumn : resizingCol;

                //        (shrinkingCol.width == shrinkingCol.minWidth) or (growingCol.width == growingCol.maxWidth)

                if (growingCol.getWidth() > growingCol.getPrefWidth()) {
                    // growingCol is willing to be generous in this case - it goes
                    // off to find a potentially better candidate to grow
                    List seq = table.getVisibleLeafColumns().subList(colPos + 1, endColPos + 1);
                    for (int i = seq.size() - 1; i >= 0; i--) {
                        TableColumnNoEmptyRow<?,?> c = (TableColumnNoEmptyRow)seq.get(i);
                        if (c.getWidth() < c.getPrefWidth()) {
                            growingCol = c;
                            break;
                        }
                    }
                }
                //
                //        if (shrinkingCol.width < shrinkingCol.prefWidth) {
                //            for (c in reverse table.visibleLeafColumns[colPos+1..endColPos]) {
                //                if (c.width > c.prefWidth) {
                //                    shrinkingCol = c;
                //                    break;
                //                }
                //            }
                //        }



                double sdiff = Math.min(Math.abs(remainingDelta), shrinkingCol.getWidth() - shrinkingCol.getMinWidth());

//                System.out.println("\tshrinking " + shrinkingCol.getText() + " and growing " + growingCol.getText());
//                System.out.println("\t\tMath.min(Math.abs("+remainingDelta+"), "+shrinkingCol.getWidth()+" - "+shrinkingCol.getMinWidth()+") = " + sdiff);

                double delta1 = resize(shrinkingCol, -sdiff);
                double delta2 = resize(growingCol, sdiff);
                remainingDelta += isShrinking ? sdiff : -sdiff;
            }
            return remainingDelta == 0;
        }
    };

    // function used to actually perform the resizing of the given column,
    // whilst ensuring it stays within the min and max bounds set on the column.
    // Returns the remaining delta if it could not all be applied.
    private static double resize(TableColumnNoEmptyRow<?,?> column, double delta) {
        if (delta == 0) return 0.0F;
        if (! column.isResizable()) return delta;

        final boolean isShrinking = delta < 0;
        final List<TableColumnNoEmptyRow<?,?>> resizingChildren = getResizableChildren(column, isShrinking);

        if (resizingChildren.size() > 0) {
            return resizeColumns(resizingChildren, delta);
        } else {
            double newWidth = column.getWidth() + delta;

            if (newWidth > column.getMaxWidth()) {
                column.impl_setWidth(column.getMaxWidth());
                return newWidth - column.getMaxWidth();
            } else if (newWidth < column.getMinWidth()) {
                column.impl_setWidth(column.getMinWidth());
                return newWidth - column.getMinWidth();
            } else {
                column.impl_setWidth(newWidth);
                return 0.0F;
            }
        }
    }

    // Returns all children columns of the given column that are able to be
    // resized. This is based on whether they are visible, resizable, and have
    // not space before they hit the min / max values.
    private static List<TableColumnNoEmptyRow<?,?>> getResizableChildren(TableColumnNoEmptyRow<?,?> column, boolean isShrinking) {
        if (column == null || column.getColumns().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<TableColumnNoEmptyRow<?,?>> tablecolumns = new ArrayList<TableColumnNoEmptyRow<?,?>>();
        for (TableColumnNoEmptyRow<?,?> c : column.getColumns()) {
            if (! c.isVisible()) continue;
            if (! c.isResizable()) continue;

            if (isShrinking && c.getWidth() > c.getMinWidth()) {
                tablecolumns.add(c);
            } else if (!isShrinking && c.getWidth() < c.getMaxWidth()) {
                tablecolumns.add(c);
            }
        }
        return tablecolumns;
    }

    private static double resizeColumns(List<TableColumnNoEmptyRow<?,?>> columns, double delta) {
        // distribute space between all visible children who can be resized.
        // To do this we need to work out if we're shrinking or growing the
        // children, and then which children can be resized based on their
        // min/pref/max/fixed properties. The results of this are in the
        // resizingChildren observableArrayList above.
        final int columnCount = columns.size();

        // work out how much of the delta we should give to each child. It should
        // be an equal amount (at present), although perhaps we'll allow for
        // functions to calculate this at a later date.
        double colDelta = delta / columnCount;

        // we maintain a count of the amount of delta remaining to ensure that
        // the column resize operation accurately reflects the location of the
        // mouse pointer. Every time this value is not 0, the UI is a teeny bit
        // more inaccurate whilst the user continues to resize.
        double remainingDelta = delta;

        // We maintain a count of the current column that we're on in case we
        // need to redistribute the remainingDelta among remaining sibling.
        int col = 0;

        // This is a bit hacky - often times the leftOverDelta is zero, but
        // remainingDelta doesn't quite get down to 0. In these instances we
        // short-circuit and just return 0.0.
        boolean isClean = true;
        for (TableColumnNoEmptyRow<?,?> childCol : columns) {
            col++;

            // resize each child column
            double leftOverDelta = resize(childCol, colDelta);

            // calculate the remaining delta if the was anything left over in
            // the last resize operation
            remainingDelta = remainingDelta - colDelta + leftOverDelta;

            //      println("\tResized {childCol.text} with {colDelta}, but {leftOverDelta} was left over. RemainingDelta is now {remainingDelta}");

            if (leftOverDelta != 0) {
                isClean = false;
                // and recalculate the distribution of the remaining delta for
                // the remaining siblings.
                colDelta = remainingDelta / (columnCount - col);
            }
        }

        // see isClean above for why this is done
        return isClean ? 0.0 : remainingDelta;
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a default TableViewNoEmptyRow control with no content.
     * 
     * <p>Refer to the {@link TableViewNoEmptyRow} class documentation for details on the
     * default state of other properties.
     */
    public TableViewNoEmptyRow() {
        this(FXCollections.<S>observableArrayList());
    }

    /**
     * Creates a TableViewNoEmptyRow with the content provided in the items ObservableList.
     * This also sets up an observer such that any changes to the items list
     * will be immediately reflected in the TableViewNoEmptyRow itself.
     * 
     * <p>Refer to the {@link TableViewNoEmptyRow} class documentation for details on the
     * default state of other properties.
     * 
     * @param items The items to insert into the TableViewNoEmptyRow, and the list to watch
     *          for changes (to automatically show in the TableViewNoEmptyRow).
     */
    public TableViewNoEmptyRow(ObservableList<S> items) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);

        // we quite happily accept items to be null here
        setItems(items);

        // install default selection and focus models
        // it's unlikely this will be changed by many users.
        setSelectionModel(new TableViewArrayListSelectionModel<S>(this));
        setFocusModel(new TableViewFocusModel<S>(this));

        // we watch the columns list, such that when it changes we can update
        // the leaf columns and visible leaf columns lists (which are read-only).
        getColumns().addListener(weakColumnsObserver);
        getColumns().addListener(new ListChangeListener<TableColumnNoEmptyRow<S,?>>() {
            @Override
            public void onChanged(Change<? extends TableColumnNoEmptyRow<S,?>> c) {
                while (c.next()) {
                    // update the TableColumnNoEmptyRow.tableView property
                    for (TableColumnNoEmptyRow<S,?> tc : c.getRemoved()) {
                        tc.setTableView(null);
                    }
                    for (TableColumnNoEmptyRow<S,?> tc : c.getAddedSubList()) {
                        tc.setTableView(TableViewNoEmptyRow.this);
                    }

                    // set up listeners
                    removeTableColumnListener(c.getRemoved());
                    addTableColumnListener(c.getAddedSubList());

                    removeColumnsListener(c.getRemoved(), weakColumnsObserver);
                    addColumnsListener(c.getAddedSubList(), weakColumnsObserver);
                }
                    
                // We don't maintain a bind for leafColumns, we simply call this update
                // function behind the scenes in the appropriate places.
                updateVisibleLeafColumns();
            }
        });

        // watch for changes to the sort order list - and when it changes run
        // the sort method.
        getSortOrder().addListener(new ListChangeListener<TableColumnNoEmptyRow<S,?>>() {
            @Override public void onChanged(Change<? extends TableColumnNoEmptyRow<S,?>> c) {
                sort();
            }
        });

        // We're watching for changes to the content width such
        // that the resize policy can be run if necessary. This comes from
        // TreeViewSkin.
        getProperties().addListener(new MapChangeListener<Object, Object>() {
            @Override
            public void onChanged(Change<? extends Object, ? extends Object> c) {
                if (c.wasAdded() && SET_CONTENT_WIDTH.equals(c.getKey())) {
                    if (c.getValueAdded() instanceof Number) {
                        setContentWidth((Double) c.getValueAdded());
                    }
                    getProperties().remove(SET_CONTENT_WIDTH);
                }
            }
        });

        isInited = true;
    }

    
    
    /***************************************************************************
     *                                                                         *
     * Instance Variables                                                      *
     *                                                                         *
     **************************************************************************/    

    // this is the only publicly writable list for columns. This represents the
    // columns as they are given initially by the developer.
    private final ObservableList<TableColumnNoEmptyRow<S,?>> columns = FXCollections.observableArrayList();

    // Finally, as convenience, we also have an observable list that contains
    // only the leaf columns that are currently visible.
    private final ObservableList<TableColumnNoEmptyRow<S,?>> visibleLeafColumns = FXCollections.observableArrayList();
    private final ObservableList<TableColumnNoEmptyRow<S,?>> unmodifiableVisibleLeafColumns = FXCollections.unmodifiableObservableList(visibleLeafColumns);
    
    
    // Allows for multiple column sorting based on the order of the TableColumns
    // in this observableArrayList. Each TableColumnNoEmptyRow is responsible for whether it is
    // sorted using ascending or descending order.
    private ObservableList<TableColumnNoEmptyRow<S,?>> sortOrder = FXCollections.observableArrayList();

    // width of VirtualFlow minus the vbar width
    private double contentWidth;
    
    // Used to minimise the amount of work performed prior to the table being
    // completely initialised. In particular it reduces the amount of column
    // resize operations that occur, which slightly improves startup time.
    private boolean isInited = false;
    
    
    
    /***************************************************************************
     *                                                                         *
     * Callbacks and Events                                                    *
     *                                                                         *
     **************************************************************************/
    
    private final ListChangeListener columnsObserver = new ListChangeListener() {
        @Override public void onChanged(Change c) {
            updateVisibleLeafColumns();
            
            // Fix for RT-15194: Need to remove removed columns from the 
            // sortOrder list.
            while (c.next()) {
                if (c.wasRemoved()) {
                    for (int i = 0; i < c.getRemovedSize(); i++) {
                        getSortOrder().remove(c.getRemoved().get(i));
                    }
                }
            }
        }
    };
    
    private final InvalidationListener columnVisibleObserver = new InvalidationListener() {
        @Override public void invalidated(Observable valueModel) {
            updateVisibleLeafColumns();
        }
    };
    
    private final InvalidationListener columnSortableObserver = new InvalidationListener() {
        @Override public void invalidated(Observable valueModel) {
            TableColumnNoEmptyRow col = (TableColumnNoEmptyRow) ((BooleanProperty)valueModel).getBean();
            if (! getSortOrder().contains(col)) return;
            sort();
        }
    };

    private final InvalidationListener columnSortTypeObserver = new InvalidationListener() {
        @Override public void invalidated(Observable valueModel) {
            TableColumnNoEmptyRow col = (TableColumnNoEmptyRow) ((ObjectProperty)valueModel).getBean();
            if (! getSortOrder().contains(col)) return;
            sort();
        }
    };
    
    
    private final WeakInvalidationListener weakColumnVisibleObserver = 
            new WeakInvalidationListener(columnVisibleObserver);
    
    private final WeakInvalidationListener weakColumnSortableObserver = 
            new WeakInvalidationListener(columnSortableObserver);
    
    private final WeakInvalidationListener weakColumnSortTypeObserver = 
            new WeakInvalidationListener(columnSortTypeObserver);
    
    private final WeakListChangeListener weakColumnsObserver = 
            new WeakListChangeListener(columnsObserver);
    
    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/


    // --- Items
    /**
     * The underlying data model for the TableViewNoEmptyRow. Note that it has a generic
     * type that must match the type of the TableViewNoEmptyRow itself.
     */
    public final ObjectProperty<ObservableList<S>> itemsProperty() { return items; }
    private ObjectProperty<ObservableList<S>> items = 
        new SimpleObjectProperty<ObservableList<S>>(this, "items") {
            @Override protected void invalidated() {
                // FIXME temporary fix for RT-15793. This will need to be
                // properly fixed when time permits
                if (getSelectionModel() instanceof TableViewArrayListSelectionModel) {
                    ((TableViewArrayListSelectionModel)getSelectionModel()).updateItemsObserver(null, getItems());
                }
                if (getFocusModel() instanceof TableViewFocusModel) {
                    ((TableViewFocusModel)getFocusModel()).updateItemsObserver(null, getItems());
                }
                if (getSkin() instanceof TableViewSkinNoEmptyRow) {
                    TableViewSkinNoEmptyRow skin = (TableViewSkinNoEmptyRow) getSkin();
                    skin.updateTableItems(null, getItems());
                }
            }
        };
    public final void setItems(ObservableList<S> value) { itemsProperty().set(value); }
    public final ObservableList<S> getItems() {return items.get(); }
    
    
    // --- Table menu button visible
    private BooleanProperty tableMenuButtonVisible;
    /**
     * This controls whether a menu button is available when the user clicks
     * in a designated space within the TableViewNoEmptyRow, within which is a radio menu
     * item for each TableColumnNoEmptyRow in this table. This menu allows for the user to
     * show and hide all TableColumns easily.
     */
    public final BooleanProperty tableMenuButtonVisibleProperty() {
        if (tableMenuButtonVisible == null) {
            tableMenuButtonVisible = new SimpleBooleanProperty(this, "tableMenuButtonVisible");
        }
        return tableMenuButtonVisible;
    }
    public final void setTableMenuButtonVisible (boolean value) {
        tableMenuButtonVisibleProperty().set(value);
    }
    public final boolean isTableMenuButtonVisible() {
        return tableMenuButtonVisible == null ? false : tableMenuButtonVisible.get();
    }
    
    
    // --- Column Resize Policy
    private ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicy;
    public final void setColumnResizePolicy(Callback<ResizeFeatures, Boolean> callback) {
        columnResizePolicyProperty().set(callback);
    }
    public final Callback<ResizeFeatures, Boolean> getColumnResizePolicy() {
        return columnResizePolicy == null ? UNCONSTRAINED_RESIZE_POLICY : columnResizePolicy.get();
    }

    /**
     * This is the function called when the user completes a column-resize
     * operation. The two most common policies are available as static functions
     * in the TableViewNoEmptyRow class: {@link #UNCONSTRAINED_RESIZE_POLICY} and
     * {@link #CONSTRAINED_RESIZE_POLICY}.
     */
    public final ObjectProperty<Callback<ResizeFeatures, Boolean>> columnResizePolicyProperty() {
        if (columnResizePolicy == null) {
            columnResizePolicy = new ObjectPropertyBase<Callback<ResizeFeatures, Boolean>>(UNCONSTRAINED_RESIZE_POLICY) {
                private Callback<ResizeFeatures, Boolean> oldPolicy;
                
                @Override protected void invalidated() {
                    if (isInited) {
                        get().call(new ResizeFeatures(TableViewNoEmptyRow.this, null, 0.0));
                        refresh();
                
                        if (oldPolicy != null) {
                            impl_pseudoClassStateChanged(oldPolicy.toString());
                        }
                        if (get() != null) {
                            impl_pseudoClassStateChanged(get().toString());
                        }
                        oldPolicy = get();
                    }
                }

                @Override
                public Object getBean() {
                    return TableViewNoEmptyRow.this;
                }

                @Override
                public String getName() {
                    return "columnResizePolicy";
                }
            };
        }
        return columnResizePolicy;
    }
    
    
    // --- Row Factory
    private ObjectProperty<Callback<TableViewNoEmptyRow<S>, TableRow<S>>> rowFactory;

    /**
     * A function which produces a TableRow. The system is responsible for
     * reusing TableRows. Return from this function a TableRow which
     * might be usable for representing a single row in a TableViewNoEmptyRow.
     * <p>
     * Note that a TableRow is <b>not</b> a TableCell. A TableRow is
     * simply a container for a TableCell, and in most circumstances it is more
     * likely that you'll want to create custom TableCells, rather than
     * TableRows. The primary use case for creating custom TableRow
     * instances would most probably be to introduce some form of column
     * spanning support.
     * <p>
     * You can create custom TableCell instances per column by assigning the
     * appropriate function to the cellFactory property in the TableColumnNoEmptyRow class.
     */
    public final ObjectProperty<Callback<TableViewNoEmptyRow<S>, TableRow<S>>> rowFactoryProperty() {
        if (rowFactory == null) {
            rowFactory = new SimpleObjectProperty<Callback<TableViewNoEmptyRow<S>, TableRow<S>>>(this, "rowFactory");
        }
        return rowFactory;
    }
    public final void setRowFactory(Callback<TableViewNoEmptyRow<S>, TableRow<S>> value) {
        rowFactoryProperty().set(value);
    }
    public final Callback<TableViewNoEmptyRow<S>, TableRow<S>> getRowFactory() {
        return rowFactory == null ? null : rowFactory.get();
    }
    
    
    // --- Placeholder Node
    private ObjectProperty<Node> placeholder;
    /**
     * This Node is shown to the user when the table has no content to show.
     * This may be the case because the table model has no data in the first
     * place, that a filter has been applied to the table model, resulting
     * in there being nothing to show the user, or that there are no currently
     * visible columns.
     */
    public final ObjectProperty<Node> placeholderProperty() {
        if (placeholder == null) {
            placeholder = new SimpleObjectProperty<Node>(this, "placeholder");
        }
        return placeholder;
    }
    public final void setPlaceholder(Node value) {
        placeholderProperty().set(value);
    }
    public final Node getPlaceholder() {
        return placeholder == null ? null : placeholder.get();
    }


    // --- Selection Model
    private ObjectProperty<TableViewSelectionModel<S>> selectionModel 
            = new SimpleObjectProperty<TableViewSelectionModel<S>>(this, "selectionModel");
    /**
     * The SelectionModel provides the API through which it is possible
     * to select single or multiple items within a TableViewNoEmptyRow, as  well as inspect
     * which items have been selected by the user. Note that it has a generic
     * type that must match the type of the TableViewNoEmptyRow itself.
     */
    public final ObjectProperty<TableViewSelectionModel<S>> selectionModelProperty() {
        return selectionModel;
    }
    public final void setSelectionModel(TableViewSelectionModel<S> value) {
        selectionModelProperty().set(value);
    }

    public final TableViewSelectionModel<S> getSelectionModel() {
        return selectionModel.get();
    }

    
    // --- Focus Model
    private ObjectProperty<TableViewFocusModel<S>> focusModel;
    public final void setFocusModel(TableViewFocusModel<S> value) {
        focusModelProperty().set(value);
    }
    public final TableViewFocusModel<S> getFocusModel() {
        return focusModel == null ? null : focusModel.get();
    }
    /**
     * Represents the currently-installed {@link TableViewFocusModel} for this
     * TableViewNoEmptyRow. Under almost all circumstances leaving this as the default
     * focus model will suffice.
     */
    public final ObjectProperty<TableViewFocusModel<S>> focusModelProperty() {
        if (focusModel == null) {
            focusModel = new SimpleObjectProperty<TableViewFocusModel<S>>(this, "focusModel");
        }
        return focusModel;
    }
    
    
    // --- Editable
    private BooleanProperty editable;
    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }
    public final boolean isEditable() {
        return editable == null ? false : editable.get();
    }
    /**
     * Specifies whether this TableViewNoEmptyRow is editable - only if the TableViewNoEmptyRow, the
     * TableColumnNoEmptyRow (if applicable) and the TableCells within it are both 
     * editable will a TableCell be able to go into their editing state.
     */
    public final BooleanProperty editableProperty() {
        if (editable == null) {
            editable = new SimpleBooleanProperty(this, "editable", false);
        }
        return editable;
    }
    
    
    // --- Editing Cell
    private ReadOnlyObjectWrapper<TablePositionNoEmptyRow<S,?>> editingCell;
    private void setEditingCell(TablePositionNoEmptyRow<S,?> value) {
        editingCellPropertyImpl().set(value);
    }
    public final TablePositionNoEmptyRow<S,?> getEditingCell() {
        return editingCell == null ? null : editingCell.get();
    }

    /**
     * Represents the current cell being edited, or null if
     * there is no cell being edited.
     */
    public final ReadOnlyObjectProperty<TablePositionNoEmptyRow<S,?>> editingCellProperty() {
        return editingCellPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TablePositionNoEmptyRow<S,?>> editingCellPropertyImpl() {
        if (editingCell == null) {
            editingCell = new ReadOnlyObjectWrapper<TablePositionNoEmptyRow<S,?>>(this, "editingCell");
        }
        return editingCell;
    }


//    // --- On Edit Start
//    private ObjectProperty<EventHandler<EditEvent<S>>> onEditStart;
//
//    /**
//     * This event handler will be fired when the user successfully initiates
//     * editing.
//     */
//    public final void setOnEditStart(EventHandler<EditEvent<S>> value) {
//        onEditStartProperty().set(value);
//    }
//
//    public final EventHandler<EditEvent<S>> getOnEditStart() {
//        return onEditStart == null ? null : onEditStart.get();
//    }
//
//    public ObjectProperty<EventHandler<EditEvent<S>>> onEditStartProperty() {
//        if (onEditStart == null) {
//            onEditStart = new ObjectPropertyBase<EventHandler<EditEvent<S>>>() {
//                @Override protected void invalidated() {
//                    setEventHandler(TableViewNoEmptyRow.<S>editStartEvent(), get());
//                }
//
//                @Override
//                public Object getBean() {
//                    return TableViewNoEmptyRow.this;
//                }
//
//                @Override
//                public String getName() {
//                    return "onEditStart";
//                }
//            };
//        }
//        return onEditStart;
//    }
//
//
//    // --- On Edit Commit
//    private ObjectProperty<EventHandler<EditEvent<S>>> onEditCommit;
//    public final void setOnEditCommit(EventHandler<EditEvent<S>> value) {
//        onEditCommitProperty().set(value);
//    }
//
//    public final EventHandler<EditEvent<S>> getOnEditCommit() {
//        return onEditCommit == null ? null : onEditCommit.get();
//    }
//
//    public ObjectProperty<EventHandler<EditEvent<S>>> onEditCommitProperty() {
//        if (onEditCommit == null) {
//            onEditCommit = new ObjectPropertyBase<EventHandler<EditEvent<S>>>() {
//                @Override protected void invalidated() {
//                    setEventHandler(TableViewNoEmptyRow.<S>editCommitEvent(), get());
//                }
//
//                @Override
//                public Object getBean() {
//                    return TableViewNoEmptyRow.this;
//                }
//
//                @Override
//                public String getName() {
//                    return "onEditCommit";
//                }
//            };
//        }
//        return onEditCommit;
//    }
//
//
//    // --- On Edit Cancel
//    private ObjectProperty<EventHandler<EditEvent<S>>> onEditCancel;
//
//    /**
//     * This event handler will be fired when the user cancels editing a cell.
//     */
//    public final void setOnEditCancel(EventHandler<EditEvent<S>> value) {
//        onEditCancelProperty().set(value);
//    }
//
//    public final EventHandler<EditEvent<S>> getOnEditCancel() {
//        return onEditCancel == null ? null : onEditCancel.get();
//    }
//
//    public ObjectProperty<EventHandler<EditEvent<S>>> onEditCancelProperty() {
//        if (onEditCancel == null) {
//            onEditCancel = new ObjectPropertyBase<EventHandler<EditEvent<S>>>() {
//                @Override protected void invalidated() {
//                    setEventHandler(TableViewNoEmptyRow.<S>editCancelEvent(), get());
//                }
//
//                @Override
//                public Object getBean() {
//                    return TableViewNoEmptyRow.this;
//                }
//
//                @Override
//                public String getName() {
//                    return "onEditCancel";
//                }
//            };
//        }
//        return onEditCancel;
//    }
    

    
    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/
    /**
     * The TableColumns that are part of this TableViewNoEmptyRow. As the user reorders
     * the TableViewNoEmptyRow columns, this list will be updated to reflect the current
     * visual ordering.
     *
     * <p>Note: to display any data in a TableViewNoEmptyRow, there must be at least one
     * TableColumnNoEmptyRow in this ObservableList.</p>
     */
    public final ObservableList<TableColumnNoEmptyRow<S,?>> getColumns() {
        return columns;
    }
    
    /**
     * The sortOrder list defines the order in which {@link TableColumnNoEmptyRow} instances
     * are sorted. An empty sortOrder list means that no sorting is being applied
     * on the TableViewNoEmptyRow. If the sortOrder list has one TableColumnNoEmptyRow within it, 
     * the TableViewNoEmptyRow will be sorted using the 
     * {@link TableColumnNoEmptyRow#sortTypeProperty() sortType} and
     * {@link TableColumnNoEmptyRow#comparatorProperty() comparator} properties of this
     * TableColumnNoEmptyRow (assuming 
     * {@link TableColumnNoEmptyRow#sortableProperty() TableColumnNoEmptyRow.sortable} is true).
     * If the sortOrder list contains multiple TableColumnNoEmptyRow instances, then
     * the TableViewNoEmptyRow is firstly sorted based on the properties of the first 
     * TableColumnNoEmptyRow. If two elements are considered equal, then the second
     * TableColumnNoEmptyRow in the list is used to determine ordering. This repeats until
     * the results from all TableColumnNoEmptyRow comparators are considered, if necessary.
     * 
     * @return An ObservableList containing zero or more TableColumnNoEmptyRow instances.
     */
    public final ObservableList<TableColumnNoEmptyRow<S,?>> getSortOrder() {
        return sortOrder;
    }
    
    /**
     * Scrolls the TableViewNoEmptyRow so that the given index is visible within the viewport.
     * @param index The index of an item that should be visible to the user.
     */
    public void scrollTo(int index) {
       getProperties().put(VirtualContainerBaseNoEmptyRow.SCROLL_TO_INDEX, index);
    }

    /**
     * Applies the currently installed resize policy against the given column,
     * resizing it based on the delta value provided.
     */
    public boolean resizeColumn(TableColumnNoEmptyRow<S,?> column, double delta) {
        if (column == null || Double.compare(delta, 0.0) == 0) return false;

        boolean allowed = getColumnResizePolicy().call(new ResizeFeatures<S>(TableViewNoEmptyRow.this, column, delta));
        if (!allowed) return false;

        // This fixes the issue where if the column width is reduced and the
        // table width is also reduced, horizontal scrollbars will begin to
        // appear at the old width. This forces the VirtualFlow.maxPrefBreadth
        // value to be reset to -1 and subsequently recalculated. Of course
        // ideally we'd just refreshView, but for the time-being no such function
        // exists.
        refresh();
        return true;
    }

    /**
     * Causes the cell at the given row/column view indexes to switch into
     * its editing state, if it is not already in it, and assuming that the 
     * TableViewNoEmptyRow and column are also editable.
     */
    public void edit(int row, TableColumnNoEmptyRow<S,?> column) {
        if (!isEditable() || (column != null && ! column.isEditable())) return;
        setEditingCell(new TablePositionNoEmptyRow(this, row, column));
    }
    
    /**
     * Returns an unmodifiable list containing the currently visible leaf columns.
     */
    @ReturnsUnmodifiableCollection
    public ObservableList<TableColumnNoEmptyRow<S,?>> getVisibleLeafColumns() {
        return unmodifiableVisibleLeafColumns;
    }
    
    /**
     * Returns the position of the given column, relative to all other 
     * visible leaf columns.
     */
    public int getVisibleLeafIndex(TableColumnNoEmptyRow<S,?> column) {
        return getVisibleLeafColumns().indexOf(column);
    }

    /**
     * Returns the TableColumnNoEmptyRow in the given column index, relative to all other
     * visible leaf columns.
     */
    public TableColumnNoEmptyRow<S,?> getVisibleLeafColumn(int column) {
        if (column < 0 || column >= visibleLeafColumns.size()) return null;
        return visibleLeafColumns.get(column);
    }

    /***************************************************************************
     *                                                                         *
     * Private Implementation                                                  *
     *                                                                         *
     **************************************************************************/


//    private int getRowCount() {
//        return getItems() == null ? 0 : getItems().size();
//    }
    
    /**
     * Call this function to force the TableViewNoEmptyRow to re-evaluate itself. This is
     * useful when the underlying data model is provided by a TableModel, and
     * you know that the data model has changed. This will force the TableViewNoEmptyRow
     * to go back to the dataProvider and get the row count, as well as update
     * the view to ensure all sorting is still correct based on any changes to
     * the data model.
     */
    private void refresh() {
        getProperties().put(REFRESH, Boolean.TRUE);
    }

    /**
     * Sometimes we want to force a sort to run - this is the recommended way
     * of doing it internally. External users of the TableViewNoEmptyRow API should just
     * stick to modifying the TableViewNoEmptyRow.sortOrder ObservableList (or the contents
     * of the TableColumns within it - in particular the
     * TableColumnNoEmptyRow.sortAscending boolean).
     */
    private void sort() {
        // build up a new comparator based on the current table columms
        TableColumnComparatorNoEmptyRow comparator = new TableColumnComparatorNoEmptyRow();
        for (TableColumnNoEmptyRow<S,?> tc : getSortOrder()) {
            comparator.getColumns().add(tc);
        }

        // If the items are a TransformationList, but not a SortableList, then we need
        // to get the source of the TransformationList and check it.
        if (getItems() instanceof TransformationList) {
            // FIXME this is temporary code whilst I await for similar functionality
            // within FXCollections.sort, such that it does the unwrapping that is
            // shown below
            List list = getItems();
            while (list != null) {
                if (list instanceof SortableList) {
                    break;
                } else if (list instanceof TransformationList) {
                    list = ((TransformationList)list).getDirectSource();
                } else {
                    break;
                }
            }

            if (list instanceof SortableList) {
                SortableList sortableList = (SortableList) list;
                // TODO review - note that we're changing the comparator based on
                // what columns the user has set.
                sortableList.setComparator(comparator);
                
                if (sortableList.getMode() == SortableList.SortMode.BATCH) {
                    sortableList.sort();
                }
                
                return;
            }
        }

        // If we are here, we will use the default sort functionality available
        // in FXCollections
        FXCollections.sort(getItems(), comparator);
    }


    // --- Content width
    private void setContentWidth(double contentWidth) {
        this.contentWidth = contentWidth;
        if (isInited) {
            // sometimes the current column resize policy will have to modify the
            // column width of all columns in the table if the table width changes,
            // so we short-circuit the resize function and just go straight there
            // with a null TableColumnNoEmptyRow, which indicates to the resize policy function
            // that it shouldn't actually do anything specific to one column.
            getColumnResizePolicy().call(new ResizeFeatures<S>(TableViewNoEmptyRow.this, null, 0.0));
            refresh();
        }
    }
    
    /**
     * Recomputes the currently visible leaf columns in this TableViewNoEmptyRow.
     */
    private void updateVisibleLeafColumns() {
        // update visible leaf columns list
        List<TableColumnNoEmptyRow<S,?>> cols = new ArrayList<TableColumnNoEmptyRow<S,?>>();
        buildVisibleLeafColumns(getColumns(), cols);
        visibleLeafColumns.setAll(cols);

        // sometimes the current column resize policy will have to modify the
        // column width of all columns in the table if the table width changes,
        // so we short-circuit the resize function and just go straight there
        // with a null TableColumnNoEmptyRow, which indicates to the resize policy function
        // that it shouldn't actually do anything specific to one column.
        getColumnResizePolicy().call(new ResizeFeatures<S>(TableViewNoEmptyRow.this, null, 0.0));
        refresh();
    }

    private void buildVisibleLeafColumns(List<TableColumnNoEmptyRow<S,?>> cols, List<TableColumnNoEmptyRow<S,?>> vlc) {
        for (TableColumnNoEmptyRow<S,?> c : cols) {
            if (c == null) continue;

            boolean hasChildren = ! c.getColumns().isEmpty();

            if (hasChildren) {
                buildVisibleLeafColumns(c.getColumns(), vlc);
            } else if (c.isVisible()) {
                vlc.add(c);
            }
        }
    }

    private void removeTableColumnListener(List<? extends TableColumnNoEmptyRow<S,?>> list) {
        if (list == null) return;

        for (TableColumnNoEmptyRow<S,?> col : list) {
            col.visibleProperty().removeListener(weakColumnVisibleObserver);
            col.sortableProperty().removeListener(weakColumnSortableObserver);
            col.sortTypeProperty().removeListener(weakColumnSortTypeObserver);

            removeTableColumnListener(col.getColumns());
        }
    }

    private void addTableColumnListener(List<? extends TableColumnNoEmptyRow<S,?>> list) {
        if (list == null) return;
        for (TableColumnNoEmptyRow<S,?> col : list) {
            col.visibleProperty().addListener(weakColumnVisibleObserver);
            col.sortableProperty().addListener(weakColumnSortableObserver);
            col.sortTypeProperty().addListener(weakColumnSortTypeObserver);
            
            addTableColumnListener(col.getColumns());
        }
    }

    private void removeColumnsListener(List<? extends TableColumnNoEmptyRow<S,?>> list, ListChangeListener cl) {
        if (list == null) return;

        for (TableColumnNoEmptyRow<S,?> col : list) {
            col.getColumns().removeListener(cl);
            removeColumnsListener(col.getColumns(), cl);
        }
    }

    private void addColumnsListener(List<? extends TableColumnNoEmptyRow<S,?>> list, ListChangeListener cl) {
        if (list == null) return;

        for (TableColumnNoEmptyRow<S,?> col : list) {
            col.getColumns().addListener(cl);
            addColumnsListener(col.getColumns(), cl);
        }
    }
    

    
    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "table-view";
    
    private static final String PSEUDO_CLASS_CELL_SELECTION = "cell-selection";
    private static final String PSEUDO_CLASS_ROW_SELECTION = "row-selection";


    private static final long CELL_SELECTION_PSEUDOCLASS_STATE =
            StyleManager.getInstance().getPseudoclassMask(PSEUDO_CLASS_CELL_SELECTION);
    private static final long ROW_SELECTION_PSEUDOCLASS_STATE =
            StyleManager.getInstance().getPseudoclassMask(PSEUDO_CLASS_ROW_SELECTION);

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated @Override public long impl_getPseudoClassState() {
        long mask = super.impl_getPseudoClassState();
        if (getSelectionModel() != null) {
            mask |= (getSelectionModel().isCellSelectionEnabled()) ?
                CELL_SELECTION_PSEUDOCLASS_STATE : ROW_SELECTION_PSEUDOCLASS_STATE;
        }
        return mask;
    }


    /***************************************************************************
     *                                                                         *
     * Support Interfaces                                                      *
     *                                                                         *
     **************************************************************************/

     /**
      * An immutable wrapper class for use in the TableViewNoEmptyRow 
     * {@link TableViewNoEmptyRow#columnResizePolicyProperty() column resize} functionality.
      */
     public static class ResizeFeatures<S> {
        private TableViewNoEmptyRow<S> table;
        private TableColumnNoEmptyRow<S,?> column;
        private Double delta;

        /**
         * Creates an instance of this class, with the provided TableViewNoEmptyRow, 
         * TableColumnNoEmptyRow and delta values being set and stored in this immutable
         * instance.
         * 
         * @param table The TableViewNoEmptyRow upon which the resize operation is occurring.
         * @param column The column upon which the resize is occurring, or null
         *      if this ResizeFeatures instance is being created as a result of a
         *      TableViewNoEmptyRow resize operation.
         * @param delta The amount of horizontal space added or removed in the 
         *      resize operation.
         */
        public ResizeFeatures(TableViewNoEmptyRow<S> table, TableColumnNoEmptyRow<S,?> column, Double delta) {
            this.table = table;
            this.column = column;
            this.delta = delta;
        }
        
        /**
         * Returns the column upon which the resize is occurring, or null
         * if this ResizeFeatures instance was created as a result of a
         * TableViewNoEmptyRow resize operation.
         */
        public TableColumnNoEmptyRow<S,?> getColumn() { return column; }
        
        /**
         * Returns the amount of horizontal space added or removed in the 
         * resize operation.
         */
        public Double getDelta() { return delta; }
        
        /**
         * Returns the TableViewNoEmptyRow upon which the resize operation is occurring.
         */
        public TableViewNoEmptyRow<S> getTable() { return table; }
    }



    /***************************************************************************
     *                                                                         *
     * Support Classes                                                         *
     *                                                                         *
     **************************************************************************/

     
    /**
     * A simple extension of the {@link SelectionModel} abstract class to
     * allow for special support for TableViewNoEmptyRow controls.
     */
    public static abstract class TableViewSelectionModel<S> extends MultipleSelectionModel<S> {

        private final TableViewNoEmptyRow<S> tableView;

        /**
         * Builds a default TableViewSelectionModel instance with the provided
         * TableViewNoEmptyRow.
         * @param tableView The TableViewNoEmptyRow upon which this selection model should
         *      operate.
         * @throws NullPointerException TableViewNoEmptyRow can not be null.
         */
        public TableViewSelectionModel(final TableViewNoEmptyRow<S> tableView) {
            if (tableView == null) {
                throw new NullPointerException("TableViewNoScroll can not be null");
            }

            this.tableView = tableView;
            
            selectedIndexProperty().addListener(new InvalidationListener() {
                @Override public void invalidated(Observable valueModel) {
                    // we used to lazily retrieve the selected item, but now we just
                    // do it when the selection changes. This is hardly likely to be
                    // expensive, and we still lazily handle the multiple selection
                    // cases over in MultipleSelectionModel.
                    if (getTableModel() == null) return;

                    int index = getSelectedIndex();
                    if (index < 0 || index >= getTableModel().size()) {
                        setSelectedItem(null);
                    } else {
                        setSelectedItem(getTableModel().get(index));
                    }
                }
            });
        }

        /**
         * A read-only ObservableList representing the currently selected cells 
         * in this TableViewNoEmptyRow. Rather than directly modify this list, please
         * use the other methods provided in the TableViewSelectionModel.
         */
        public abstract ObservableList<TablePositionNoEmptyRow> getSelectedCells();

        /**
         * Convenience function which tests whether the given row and column index
         * is currently selected in this TableViewNoEmptyRow instance.
         */
        public abstract boolean isSelected(int row, TableColumnNoEmptyRow<S,?> column);

        /**
         * Selects the cell at the given row/column intersection.
         */
        public abstract void select(int row, TableColumnNoEmptyRow<S,?> column);

        /**
         * Clears all selection, and then selects the cell at the given row/column
         * intersection.
         */
        public abstract void clearAndSelect(int row, TableColumnNoEmptyRow<S,?> column);

        /**
         * Removes selection from the specified row/column position (in view indexes).
         * If this particular cell (or row if the column value is -1) is not selected,
         * nothing happens.
         */
        public abstract void clearSelection(int row, TableColumnNoEmptyRow<S,?> column);

        /**
         * Selects the cell to the left of the currently selected cell.
         */
        public abstract void selectLeftCell();

        /**
         * Selects the cell to the right of the currently selected cell.
         */
        public abstract void selectRightCell();

        /**
         * Selects the cell directly above the currently selected cell.
         */
        public abstract void selectAboveCell();

        /**
         * Selects the cell directly below the currently selected cell.
         */
        public abstract void selectBelowCell();

        /**
         * A boolean property used to represent whether the TableViewNoEmptyRow is in
         * row or cell selection modes. By default a TableViewNoEmptyRow is in row selection
         * mode which means that individual cells can not be selected. Setting
         * <code>cellSelectionEnabled</code> to be true results in cells being
         * able to be selected (but not rows).
         */
        private BooleanProperty cellSelectionEnabled;
        public final BooleanProperty cellSelectionEnabledProperty() {
            if (cellSelectionEnabled == null) {
                cellSelectionEnabled = new BooleanPropertyBase() {
                    @Override protected void invalidated() {
                        get();
                        clearSelection();
                        tableView.impl_pseudoClassStateChanged(TableViewNoEmptyRow.PSEUDO_CLASS_CELL_SELECTION);
                        tableView.impl_pseudoClassStateChanged(TableViewNoEmptyRow.PSEUDO_CLASS_ROW_SELECTION);
                    }

                    @Override
                    public Object getBean() {
                        return TableViewSelectionModel.this;
                    }

                    @Override
                    public String getName() {
                        return "cellSelectionEnabled";
                    }
                };
            }
            return cellSelectionEnabled;
        }
        public final void setCellSelectionEnabled(boolean value) {
            cellSelectionEnabledProperty().set(value);
        }
        public final boolean isCellSelectionEnabled() {
            return cellSelectionEnabled == null ? false : cellSelectionEnabled.get();
        }
        
        
        /**
         * Returns the TableViewNoEmptyRow instance that this selection model is installed in.
         */
        public TableViewNoEmptyRow<S> getTableView() {
            return tableView;
        }

        /**
         * Convenience method that returns <code>getTableView().getItems()</code>.
         * @return The {@link TableViewNoEmptyRow#itemsProperty() items} list of the current
         *      TableViewNoEmptyRow.
         */
        protected List<S> getTableModel() {
            return tableView.getItems();
        }
    }
    
    

    /**
     * A primitive selection model implementation, using a List<Integer> to store all
     * selected indices.
     */
    // package for testing
    static class TableViewArrayListSelectionModel<S> extends TableViewSelectionModel<S> {

        /***********************************************************************
         *                                                                     *
         * Constructors                                                        *
         *                                                                     *
         **********************************************************************/

        public TableViewArrayListSelectionModel(final TableViewNoEmptyRow<S> tableView) {
            super(tableView);
            this.tableView = tableView;
            
            final MappingChange.Map<TablePositionNoEmptyRow,S> cellToItemsMap = new MappingChange.Map<TablePositionNoEmptyRow, S>() {

                @Override
                public S map(TablePositionNoEmptyRow f) {
                    return getModelItem(f.getRow());
                }
            };
            
            final MappingChange.Map<TablePositionNoEmptyRow,Integer> cellToIndicesMap = new MappingChange.Map<TablePositionNoEmptyRow, Integer>() {

                @Override
                public Integer map(TablePositionNoEmptyRow f) {
                    return f.getRow();
                }
            };
            
            selectedCells = FXCollections.<TablePositionNoEmptyRow>observableArrayList();
            selectedCells.addListener(new ListChangeListener<TablePositionNoEmptyRow>() {
                @Override
                public void onChanged(final Change<? extends TablePositionNoEmptyRow> c) {
                    // when the selectedCells observableArrayList changes, we manually call
                    // the observers of the selectedItems, selectedIndices and
                    // selectedCells lists.
                    
                    // create an on-demand list of the removed objects contained in the
                    // given rows
                    selectedItems.callObservers(new MappingChange<TablePositionNoEmptyRow, S>(c, cellToItemsMap, selectedItems));
                    c.reset();

                    selectedIndices.callObservers(new MappingChange<TablePositionNoEmptyRow, Integer>(c, cellToIndicesMap, selectedIndices));
                    c.reset();

                    selectedCellsSeq.callObservers(new MappingChange<TablePositionNoEmptyRow, TablePositionNoEmptyRow>(c, MappingChange.NOOP_MAP, selectedCellsSeq));
                    c.reset();
                }
            });

            selectedIndices = new ReadOnlyUnbackedObservableList<Integer>() {
                @Override public Integer get(int i) {
                    return selectedCells.get(i).getRow();
                }

                @Override public int size() {
                    return selectedCells.size();
                }
            };

            selectedItems = new ReadOnlyUnbackedObservableList<S>() {
                @Override public S get(int i) {
                    return getModelItem(selectedIndices.get(i));
                }

                @Override public int size() {
                    return selectedIndices.size();
                }
            };
            
            selectedCellsSeq = new ReadOnlyUnbackedObservableList<TablePositionNoEmptyRow>() {
                @Override public TablePositionNoEmptyRow get(int i) {
                    return selectedCells.get(i);
                }

                @Override public int size() {
                    return selectedCells.size();
                }
            };


            /*
             * The following two listeners are used in conjunction with
             * SelectionModel.select(T obj) to allow for a developer to select
             * an item that is not actually in the data model. When this occurs,
             * we actively try to find an index that matches this object, going
             * so far as to actually watch for all changes to the items list,
             * rechecking each time.
             */

            // watching for changes to the items list
            tableView.itemsProperty().addListener(weakItemsPropertyListener);
            
            // watching for changes to the items list content
            if (tableView.getItems() != null) {
                tableView.getItems().addListener(weakItemsContentListener);
            }
        }
        
        private final TableViewNoEmptyRow<S> tableView;
        
        private ChangeListener<ObservableList<S>> itemsPropertyListener = new ChangeListener<ObservableList<S>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<S>> observable, 
                ObservableList<S> oldList, ObservableList<S> newList) {
                    updateItemsObserver(oldList, newList);
            }
        };
        
        private WeakChangeListener<ObservableList<S>> weakItemsPropertyListener = 
                new WeakChangeListener<ObservableList<S>>(itemsPropertyListener);

        final ListChangeListener<S> itemsContentListener = new ListChangeListener<S>() {
            @Override public void onChanged(Change<? extends S> c) {
                if (tableView.getItems() == null || tableView.getItems().isEmpty()) {
                    setSelectedIndex(-1);
                } else if (getSelectedIndex() == -1 && getSelectedItem() != null) {
                    int newIndex = tableView.getItems().indexOf(getSelectedItem());
                    if (newIndex != -1) {
                        setSelectedIndex(newIndex);
                    }
                }
                updateSelection(c);
            }
        };
        
        final WeakListChangeListener weakItemsContentListener 
                = new WeakListChangeListener(itemsContentListener);
        
        private void updateItemsObserver(ObservableList<S> oldList, ObservableList<S> newList) {
            // the listview items list has changed, we need to observe
            // the new list, and remove any observer we had from the old list
            if (oldList != null) oldList.removeListener(weakItemsContentListener);
            if (newList != null) newList.addListener(weakItemsContentListener);

            // when the items list totally changes, we should clear out
            // the selection
            setSelectedIndex(-1);
        }
        

        /***********************************************************************
         *                                                                     *
         * Observable properties (and getters/setters)                         *
         *                                                                     *
         **********************************************************************/
        
        // the only 'proper' internal observableArrayList, selectedItems and selectedIndices
        // are both 'read-only and unbacked'.
        private final ObservableList<TablePositionNoEmptyRow> selectedCells;

        // NOTE: represents selected ROWS only - use selectedCells for more data
        private final ReadOnlyUnbackedObservableList<Integer> selectedIndices;
        @Override public ObservableList<Integer> getSelectedIndices() {
            return selectedIndices;
        }

        // used to represent the _row_ backing data for the selectedCells
        private final ReadOnlyUnbackedObservableList<S> selectedItems;
        @Override public ObservableList<S> getSelectedItems() {
            return selectedItems;
        }

        private final ReadOnlyUnbackedObservableList<TablePositionNoEmptyRow> selectedCellsSeq;
        @Override public ObservableList<TablePositionNoEmptyRow> getSelectedCells() {
            return selectedCellsSeq;
        }


        /***********************************************************************
         *                                                                     *
         * Internal properties                                                 *
         *                                                                     *
         **********************************************************************/

        
        // Listen to changes in the tableview items list, such that when it 
        // changes we can update the selected indices list to refer to the 
        // new indices.
        private void updateSelection(ListChangeListener.Change<? extends S> c) {
            while (c.next()) {
                if (c.wasReplaced()) {
                    // Fix for RT-18969: the items list had setAll called on it
                    if (getSelectedIndex() < getRowCount()) {
                        int selectedIndex = getSelectedIndex();
                        clearSelection(selectedIndex);
                        select(selectedIndex);
                    }
                } else if (c.wasAdded() || c.wasRemoved()) {
                    int position = c.getFrom();
                    int shift = c.wasAdded() ? c.getAddedSize() : -c.getRemovedSize();
                    
                    if (position < 0) return;
                    
                    List<TablePositionNoEmptyRow> newIndices = new ArrayList<TablePositionNoEmptyRow>(selectedCells.size());
        
                    for (int i = 0; i < selectedCells.size(); i++) {
                        TablePositionNoEmptyRow old = selectedCells.get(i);
                        int newRow = old.getRow() < position ? old.getRow() : old.getRow() + shift;
                        if (newRow < 0) continue;
                        newIndices.add(new TablePositionNoEmptyRow(getTableView(), newRow, old.getTableColumn()));
                    }
                    
                    quietClearSelection();
                    selectedCells.setAll(newIndices);
                    selectedCellsSeq.callObservers(
                            new NonIterableChange.SimpleAddChange<TablePositionNoEmptyRow>(0, 
                            newIndices.size(), selectedCellsSeq));
                } else if (c.wasPermutated()) {
                    // General approach:
                    //   -- detected a sort has happened
                    //   -- Create a permutation lookup map (1)
                    //   -- dump all the selected indices into a list (2)
                    //   -- clear the selected items / indexes (3)
                    //   -- create a list containing the new indices (4)
                    //   -- for each previously-selected index (5)
                    //     -- if index is in the permutation lookup map
                    //       -- add the new index to the new indices list
                    //   -- Perform batch selection (6)

                    // (1)
                    int length = c.getTo() - c.getFrom();
                    HashMap<Integer, Integer> pMap = new HashMap<Integer, Integer> (length);
                    for (int i = c.getFrom(); i < c.getTo(); i++) {
                        pMap.put(i, c.getPermutation(i));
                    }

                    // (2)
                    List<TablePositionNoEmptyRow> selectedIndices = new ArrayList<TablePositionNoEmptyRow>(getSelectedCells());


                    // (3)
                    clearSelection();

                    // (4)
                    List<TablePositionNoEmptyRow> newIndices = new ArrayList<TablePositionNoEmptyRow>(getSelectedIndices().size());

                    // (5)
                    for (int i = 0; i < selectedIndices.size(); i++) {
                        TablePositionNoEmptyRow oldIndex = selectedIndices.get(i);

                        if (pMap.containsKey(oldIndex.getRow())) {
                            Integer newIndex = pMap.get(oldIndex.getRow());
                            newIndices.add(new TablePositionNoEmptyRow(oldIndex.getTableView(), newIndex, oldIndex.getTableColumn()));
                        }
                    }

                    // (6)
                    quietClearSelection();
                    selectedCells.setAll(newIndices);
                    selectedCellsSeq.callObservers(new NonIterableChange.SimpleAddChange<TablePositionNoEmptyRow>(0, newIndices.size(), selectedCellsSeq));
                }
            }
        }

        /***********************************************************************
         *                                                                     *
         * Public selection API                                                *
         *                                                                     *
         **********************************************************************/

        @Override public void clearAndSelect(int row) {
            clearAndSelect(row, null);
        }

        @Override public void clearAndSelect(int row, TableColumnNoEmptyRow<S,?> column) {
            quietClearSelection();
            select(row, column);
        }

        @Override public void select(int row) {
            select(row, null);
        }

        @Override
        public void select(int row, TableColumnNoEmptyRow<S,?> column) {
            if (row < 0 || row >= getRowCount()) return;

            // if I'm in cell selection mode but the column is null, I don't want
            // to select the whole row instead...
            if (isCellSelectionEnabled() && column == null) return;
//            
//            // If I am not in cell selection mode (so I want to select rows only),
//            // if a column is given, I return
//            if (! isCellSelectionEnabled() && column != null) return;

            TablePositionNoEmptyRow pos = new TablePositionNoEmptyRow(getTableView(), row, column);
            
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }

            if (! selectedCells.contains(pos)) {
                selectedCells.add(pos);
            }

            updateSelectedIndex(row);
            focus(row, column);
        }

        @Override public void select(S obj) {
            if (getTableModel() == null) return;

            // We have no option but to iterate through the model and select the
            // first occurrence of the given object. Once we find the first one, we
            // don't proceed to select any others.
            S rowObj = null;
            for (int i = 0; i < getRowCount(); i++) {
                rowObj = getTableModel().get(i);
                if (rowObj == null) continue;

                if (rowObj.equals(obj)) {
                    if (isSelected(i)) {
                        return;
                    }

                    if (getSelectionMode() == SelectionMode.SINGLE) {
                        quietClearSelection();
                    }

                    select(i);
                    return;
                }
            }

            // if we are here, we did not find the item in the entire data model.
            // Even still, we allow for this item to be set to the give object.
            // We expect that in concrete subclasses of this class we observe the
            // data model such that we check to see if the given item exists in it,
            // whilst SelectedIndex == -1 && SelectedItem != null.
            setSelectedItem(obj);
        }

        @Override public void selectIndices(int row, int... rows) {
            if (rows == null) {
                select(row);
                return;
            }

            /*
             * Performance optimisation - if multiple selection is disabled, only
             * process the end-most row index.
             */
            int rowCount = getRowCount();

            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();

                for (int i = rows.length - 1; i >= 0; i--) {
                    int index = rows[i];
                    if (index >= 0 && index < rowCount) {
                        select(index);
                        break;
                    }
                }

                if (selectedCells.isEmpty()) {
                    if (row > 0 && row < rowCount) {
                        select(row);
                    }
                }
            } else {
                int lastIndex = -1;
                List<TablePositionNoEmptyRow> positions = new ArrayList<TablePositionNoEmptyRow>();

                if (row >= 0 && row < rowCount) {
                    positions.add(new TablePositionNoEmptyRow(getTableView(), row, null));
                    lastIndex = row;
                }

                for (int i = 0; i < rows.length; i++) {
                    int index = rows[i];
                    if (index < 0 || index >= rowCount) continue;
                    lastIndex = index;
                    TablePositionNoEmptyRow pos = new TablePositionNoEmptyRow(getTableView(), index, null);
                    if (selectedCells.contains(pos)) continue;

                    positions.add(pos);
                }

                selectedCells.addAll(positions);

                if (lastIndex != -1) {
                    select(lastIndex);
                }
            }
        }

        @Override public void selectAll() {
            if (getSelectionMode() == SelectionMode.SINGLE) return;

            quietClearSelection();
            if (getTableModel() == null) return;

            if (isCellSelectionEnabled()) {
                List<TablePositionNoEmptyRow> indices = new ArrayList<TablePositionNoEmptyRow>();
                TableColumnNoEmptyRow column;
                TablePositionNoEmptyRow tp = null;
                for (int col = 0; col < getTableView().getVisibleLeafColumns().size(); col++) {
                    column = getTableView().getVisibleLeafColumns().get(col);
                    for (int row = 0; row < getRowCount(); row++) {
                        tp = new TablePositionNoEmptyRow(getTableView(), row, column);
                        indices.add(tp);
                    }
                }
                selectedCells.setAll(indices);
                
                if (tp != null) {
                    select(tp.getRow(), tp.getTableColumn());
                    focus(tp.getRow(), tp.getTableColumn());
                }
            } else {
                List<TablePositionNoEmptyRow> indices = new ArrayList<TablePositionNoEmptyRow>();
                for (int i = 0; i < getRowCount(); i++) {
                    indices.add(new TablePositionNoEmptyRow(getTableView(), i, null));
                }
                selectedCells.setAll(indices);
                select(getRowCount() - 1);
                focus(indices.get(indices.size() - 1));
            }
        }

        @Override public void clearSelection(int index) {
            clearSelection(index, null);
        }

        @Override
        public void clearSelection(int row, TableColumnNoEmptyRow<S,?> column) {
            TablePositionNoEmptyRow tp = new TablePositionNoEmptyRow(getTableView(), row, column);

            boolean csMode = isCellSelectionEnabled();
            
            for (TablePositionNoEmptyRow pos : getSelectedCells()) {
                if ((! csMode && pos.getRow() == row) || (csMode && pos.equals(tp))) {
                    selectedCells.remove(pos);

                    // give focus to this cell index
                    focus(row);

                    return;
                }
            }
        }

        @Override public void clearSelection() {
            updateSelectedIndex(-1);
            focus(-1);
            quietClearSelection();
        }

        private void quietClearSelection() {
            selectedCells.clear();
        }

        @Override public boolean isSelected(int index) {
            return isSelected(index, null);
        }

        @Override
        public boolean isSelected(int row, TableColumnNoEmptyRow<S,?> column) {
            // When in cell selection mode, we currently do NOT support selecting
            // entire rows, so a isSelected(row, null) 
            // should always return false.
            if (isCellSelectionEnabled() && (column == null)) return false;
            
            for (TablePositionNoEmptyRow tp : getSelectedCells()) {
                boolean columnMatch = ! isCellSelectionEnabled() || 
                        (column == null && tp.getTableColumn() == null) || 
                        (column != null && column.equals(tp.getTableColumn()));
                
                if (tp.getRow() == row && columnMatch) {
                    return true;
                }
            }
            return false;
        }

        @Override public boolean isEmpty() {
            return selectedCells.isEmpty();
        }

        @Override public void selectPrevious() {
            if (isCellSelectionEnabled()) {
                // in cell selection mode, we have to wrap around, going from
                // right-to-left, and then wrapping to the end of the previous line
                TablePositionNoEmptyRow<S,?> pos = getFocusedCell();
                if (pos.getColumn() - 1 >= 0) {
                    // go to previous row
                    select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
                } else if (pos.getRow() < getRowCount() - 1) {
                    // wrap to end of previous row
                    select(pos.getRow() - 1, getTableColumn(getTableView().getVisibleLeafColumns().size() - 1));
                }
            } else {
                int focusIndex = getFocusedIndex();
                if (focusIndex == -1) {
                    select(getRowCount() - 1);
                } else if (focusIndex > 0) {
                    select(focusIndex - 1);
                }
            }
        }

        @Override public void selectNext() {
            if (isCellSelectionEnabled()) {
                // in cell selection mode, we have to wrap around, going from
                // left-to-right, and then wrapping to the start of the next line
                TablePositionNoEmptyRow<S,?> pos = getFocusedCell();
                if (pos.getColumn() + 1 < getTableView().getVisibleLeafColumns().size()) {
                    // go to next column
                    select(pos.getRow(), getTableColumn(pos.getTableColumn(), 1));
                } else if (pos.getRow() < getRowCount() - 1) {
                    // wrap to start of next row
                    select(pos.getRow() + 1, getTableColumn(0));
                }
            } else {
                int focusIndex = getFocusedIndex();
                if (focusIndex == -1) {
                    select(0);
                } else if (focusIndex < getRowCount() -1) {
                    select(focusIndex + 1);
                }
            }
        }

        @Override public void selectAboveCell() {
            TablePositionNoEmptyRow pos = getFocusedCell();
            if (pos.getRow() == -1) {
                select(getRowCount() - 1);
            } else if (pos.getRow() > 0) {
                select(pos.getRow() - 1, pos.getTableColumn());
            }
        }

        @Override public void selectBelowCell() {
            TablePositionNoEmptyRow pos = getFocusedCell();

            if (pos.getRow() == -1) {
                select(0);
            } else if (pos.getRow() < getRowCount() -1) {
                select(pos.getRow() + 1, pos.getTableColumn());
            }
        }

        @Override public void selectFirst() {
            TablePositionNoEmptyRow focusedCell = getFocusedCell();

            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }

            if (getRowCount() > 0) {
                if (isCellSelectionEnabled()) {
                    select(0, focusedCell.getTableColumn());
                } else {
                    select(0);
                }
            }
        }

        @Override public void selectLast() {
            TablePositionNoEmptyRow focusedCell = getFocusedCell();

            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }

            int numItems = getRowCount();
            if (numItems > 0 && getSelectedIndex() < numItems - 1) {
                if (isCellSelectionEnabled()) {
                    select(numItems - 1, focusedCell.getTableColumn());
                } else {
                    select(numItems - 1);
                }
            }
        }

        @Override
        public void selectLeftCell() {
            if (! isCellSelectionEnabled()) return;

            TablePositionNoEmptyRow pos = getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
            }
        }

        @Override
        public void selectRightCell() {
            if (! isCellSelectionEnabled()) return;

            TablePositionNoEmptyRow pos = getFocusedCell();
            if (pos.getColumn() + 1 < getTableView().getVisibleLeafColumns().size()) {
                select(pos.getRow(), getTableColumn(pos.getTableColumn(), 1));
            }
        }



        /***********************************************************************
         *                                                                     *
         * Support code                                                        *
         *                                                                     *
         **********************************************************************/
        
        private TableColumnNoEmptyRow<S,?> getTableColumn(int pos) {
            return getTableView().getVisibleLeafColumn(pos);
        }
        
//        private TableColumnNoEmptyRow<S,?> getTableColumn(TableColumnNoEmptyRow<S,?> column) {
//            return getTableColumn(column, 0);
//        }

        // Gets a table column to the left or right of the current one, given an offset
        private TableColumnNoEmptyRow<S,?> getTableColumn(TableColumnNoEmptyRow<S,?> column, int offset) {
            int columnIndex = getTableView().getVisibleLeafIndex(column);
            int newColumnIndex = columnIndex + offset;
            return getTableView().getVisibleLeafColumn(newColumnIndex);
        }

        private void updateSelectedIndex(int row) {
            setSelectedIndex(row);
            setSelectedItem(getModelItem(row));
        }
        
        private void focus(int row) {
            focus(row, null);
        }

        private void focus(int row, TableColumnNoEmptyRow<S,?> column) {
            focus(new TablePositionNoEmptyRow(getTableView(), row, column));
        }

        private void focus(TablePositionNoEmptyRow pos) {
            if (getTableView().getFocusModel() == null) return;

            getTableView().getFocusModel().focus(pos.getRow(), pos.getTableColumn());
        }

        private int getFocusedIndex() {
            return getFocusedCell().getRow();
        }

        private TablePositionNoEmptyRow getFocusedCell() {
            if (getTableView().getFocusModel() == null) {
                return new TablePositionNoEmptyRow(getTableView(), -1, null);
            }
            return getTableView().getFocusModel().getFocusedCell();
        }

        private int getRowCount() {
            return getTableModel() == null ? -1 : getTableModel().size();
        }

        private S getModelItem(int index) {
            if (getTableModel() == null) return null;

            if (index < 0 || index >= getRowCount()) return null;

            return getTableModel().get(index);
        }
    }
    
    
    
    
    /**
     * A {@link FocusModel} with additional functionality to support the requirements
     * of a TableViewNoEmptyRow control.
     * 
     * @see TableViewNoEmptyRow
     */
    public static class TableViewFocusModel<S> extends FocusModelNoEmptyRow<S> {

        private final TableViewNoEmptyRow<S> tableView;

        private final TablePositionNoEmptyRow EMPTY_CELL;

        /**
         * Creates a default TableViewFocusModel instance that will be used to
         * manage focus of the provided TableViewNoEmptyRow control.
         * 
         * @param tableView The tableView upon which this focus model operates.
         * @throws NullPointerException The TableViewNoEmptyRow argument can not be null.
         */
        public TableViewFocusModel(final TableViewNoEmptyRow<S> tableView) {
            if (tableView == null) {
                throw new NullPointerException("TableViewNoScroll can not be null");
            }

            this.tableView = tableView;
            
            this.tableView.itemsProperty().addListener(weakItemsPropertyListener);
            if (tableView.getItems() != null) {
                this.tableView.getItems().addListener(weakItemsContentListener);
            }

            TablePositionNoEmptyRow pos = new TablePositionNoEmptyRow(tableView, -1, null);
            setFocusedCell(pos);
            EMPTY_CELL = pos;
        }
        
        private ChangeListener<ObservableList<S>> itemsPropertyListener = new ChangeListener<ObservableList<S>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<S>> observable, 
                ObservableList<S> oldList, ObservableList<S> newList) {
                    updateItemsObserver(oldList, newList);
            }
        };
        
        private WeakChangeListener<ObservableList<S>> weakItemsPropertyListener = 
                new WeakChangeListener<ObservableList<S>>(itemsPropertyListener);
        
        // Listen to changes in the tableview items list, such that when it
        // changes we can update the focused index to refer to the new indices.
        private final ListChangeListener<S> itemsContentListener = new ListChangeListener<S>() {
            @Override public void onChanged(Change<? extends S> c) {
                c.next();
                if (c.getFrom() > getFocusedIndex()) return;
                c.reset();
                boolean added = false;
                boolean removed = false;
                int addedSize = 0;
                int removedSize = 0;
                while (c.next()) {
                    added |= c.wasAdded();
                    removed |= c.wasRemoved();
                    addedSize += c.getAddedSize();
                    removedSize += c.getRemovedSize();
                }
                if (added && ! removed) {
                    focus(getFocusedIndex() + addedSize);
                } else if (!added && removed) {
                    focus(getFocusedIndex() - removedSize);
                }
            }
        };
        
        private WeakListChangeListener<S> weakItemsContentListener 
                = new WeakListChangeListener<S>(itemsContentListener);
        
        private void updateItemsObserver(ObservableList<S> oldList, ObservableList<S> newList) {
            // the tableview items list has changed, we need to observe
            // the new list, and remove any observer we had from the old list
            if (oldList != null) oldList.removeListener(weakItemsContentListener);
            if (newList != null) newList.addListener(weakItemsContentListener);
        }

        /** {@inheritDoc} */
        @Override protected int getItemCount() {
            if (tableView.getItems() == null) return -1;
            return tableView.getItems().size();
        }

        /** {@inheritDoc} */
        @Override protected S getModelItem(int index) {
            if (tableView.getItems() == null) return null;

            if (index < 0 || index >= getItemCount()) return null;

            return ((List<S>)tableView.getItems()).get(index);
        }

        /**
         * The position of the current item in the TableViewNoEmptyRow which has the focus.
         */
        private ReadOnlyObjectWrapper<TablePositionNoEmptyRow> focusedCell;
        public final ReadOnlyObjectProperty<TablePositionNoEmptyRow> focusedCellProperty() {
            return focusedCellPropertyImpl().getReadOnlyProperty();
        }
        private void setFocusedCell(TablePositionNoEmptyRow value) { focusedCellPropertyImpl().set(value);  }
        public final TablePositionNoEmptyRow getFocusedCell() { return focusedCell == null ? EMPTY_CELL : focusedCell.get(); }

        private ReadOnlyObjectWrapper<TablePositionNoEmptyRow> focusedCellPropertyImpl() {
            if (focusedCell == null) {
                focusedCell = new ReadOnlyObjectWrapper<TablePositionNoEmptyRow>(EMPTY_CELL) {
                    private TablePositionNoEmptyRow old;
                    @Override protected void invalidated() {
                        if (get() == null) return;

                        if (old == null || (old != null && !old.equals(get()))) {
                            setFocusedIndex(get().getRow());
                            setFocusedItem(getModelItem(getValue().getRow()));
                            
                            old = get();
                        }
                    }

                    @Override
                    public Object getBean() {
                        return TableViewFocusModel.this;
                    }

                    @Override
                    public String getName() {
                        return "focusedCell";
                    }
                };
            }
            return focusedCell;
        }


        /**
         * Causes the item at the given index to receive the focus.
         *
         * @param row The row index of the item to give focus to.
         * @param column The column of the item to give focus to. Can be null.
         */
        public void focus(int row, TableColumnNoEmptyRow<S,?> column) {
            if (row < 0 || row >= getItemCount()) {
                setFocusedCell(EMPTY_CELL);
            } else {
                setFocusedCell(new TablePositionNoEmptyRow(tableView, row, column));
            }
        }

        /**
         * Convenience method for setting focus on a particular row or cell
         * using a {@link TablePositionNoEmptyRow}.
         * 
         * @param pos The table position where focus should be set.
         */
        public void focus(TablePositionNoEmptyRow pos) {
            if (pos == null) return;
            focus(pos.getRow(), pos.getTableColumn());
        }


        /***********************************************************************
         *                                                                     *
         * Public API                                                          *
         *                                                                     *
         **********************************************************************/

        /**
         * Tests whether the row / cell at the given location currently has the
         * focus within the TableViewNoEmptyRow.
         */
        public boolean isFocused(int row, TableColumnNoEmptyRow<S,?> column) {
            if (row < 0 || row >= getItemCount()) return false;

            TablePositionNoEmptyRow cell = getFocusedCell();
            boolean columnMatch = column == null || (column != null && column.equals(cell.getTableColumn()));

            return cell.getRow() == row && columnMatch;
        }

        /**
         * Causes the item at the given index to receive the focus. This does not
         * cause the current selection to change. Updates the focusedItem and
         * focusedIndex properties such that <code>focusedIndex = -1</code> unless
         * <pre><code>0 <= index < model size</code></pre>.
         *
         * @param index The index of the item to get focus.
         */
        @Override public void focus(int index) {
            if (index < 0 || index >= getItemCount()) {
                setFocusedCell(EMPTY_CELL);
            } else {
                setFocusedCell(new TablePositionNoEmptyRow(tableView, index, null));
            }
        }

        /**
         * Attempts to move focus to the cell above the currently focused cell.
         */
        public void focusAboveCell() {
            TablePositionNoEmptyRow cell = getFocusedCell();

            if (getFocusedIndex() == -1) {
                focus(getItemCount() - 1, cell.getTableColumn());
            } else if (getFocusedIndex() > 0) {
                focus(getFocusedIndex() - 1, cell.getTableColumn());
            }
        }

        /**
         * Attempts to move focus to the cell below the currently focused cell.
         */
        public void focusBelowCell() {
            TablePositionNoEmptyRow cell = getFocusedCell();
            if (getFocusedIndex() == -1) {
                focus(0, cell.getTableColumn());
            } else if (getFocusedIndex() != getItemCount() -1) {
                focus(getFocusedIndex() + 1, cell.getTableColumn());
            }
        }

        /**
         * Attempts to move focus to the cell to the left of the currently focused cell.
         */
        public void focusLeftCell() {
            TablePositionNoEmptyRow cell = getFocusedCell();
            if (cell.getColumn() <= 0) return;
            focus(cell.getRow(), getTableColumn(cell.getTableColumn(), -1));
        }

        /**
         * Attempts to move focus to the cell to the right of the the currently focused cell.
         */
        public void focusRightCell() {
            TablePositionNoEmptyRow cell = getFocusedCell();
            if (cell.getColumn() == getColumnCount() - 1) return;
            focus(cell.getRow(), getTableColumn(cell.getTableColumn(), 1));
        }



         /***********************************************************************
         *                                                                     *
         * Private Implementation                                              *
         *                                                                     *
         **********************************************************************/

        private int getColumnCount() {
            return tableView.getVisibleLeafColumns().size();
        }

        // Gets a table column to the left or right of the current one, given an offset
        private TableColumnNoEmptyRow<S,?> getTableColumn(TableColumnNoEmptyRow<S,?> column, int offset) {
            int columnIndex = tableView.getVisibleLeafIndex(column);
            int newColumnIndex = columnIndex + offset;
            return tableView.getVisibleLeafColumn(newColumnIndex);
        }
    }
}
