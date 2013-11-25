/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.table;

/*
 * Copyright (c) 2010, 2011, Oracle and/or its affiliates. All rights reserved.
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

import java.text.Collator;
import java.util.Comparator;
import javafx.beans.Observable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.util.Callback;

import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.WeakListChangeListener;

import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.scene.control.Cell;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * A {@link TableViewNoEmptyRow} is made up of a number of TableColumnNoEmptyRow instances. Each
 * TableColumnNoEmptyRow in a table is responsible for displaying (and editing) the contents
 * of that column. As well as being responsible for displaying and editing data 
 * for a single column, a TableColumnNoEmptyRow also contains the necessary properties to:
 * <ul>
 *    <li>Be resized (using {@link #minWidthProperty() minWidth}/
 *      {@link #prefWidthProperty() prefWidth}/{@link #maxWidthProperty() maxWidth}
 *      and {@link #widthProperty() width} properties)
 *    <li>Have its {@link #visibleProperty() visibility} toggled
 *    <li>Display {@link #textProperty() header text}
 *    <li>Display any {@link #getColumns() nested columns} it may contain
 *    <li>Have a {@link #contextMenuProperty() context menu} when the user 
 *      right-clicks the column header area
 *    <li>Have the contents of the table be sorted (using 
 *      {@link #comparatorProperty() comparator}, {@link #sortable sortable} and
 *      {@link #sortTypeProperty() sortType})
 * </ul>
 * </p>
 * 
 * When creating a TableColumnNoEmptyRow instance, perhaps the two most important properties
 * to set are the column {@link #textProperty() text} (what to show in the column
 * header area), and the column {@link #cellValueFactory cell value factory}
 * (which is used to populate individual cells in the column). This can be 
 * achieved using some variation on the following code:
 * 
 * <pre>
 * {@code 
 * ObservableList<Person> data = ...
 * TableViewNoEmptyRow<Person> tableView = new TableViewNoEmptyRow<Person>(data);
 * 
 * TableColumnNoEmptyRow<Person,String> firstNameCol = new TableColumnNoEmptyRow<Person,String>("First Name");
 * firstNameCol.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
 *     public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
 *         // p.getValue() returns the Person instance for a particular TableViewNoEmptyRow row
 *         return p.getValue().firstNameProperty();
 *     }
 *  });
 * }
 * tableView.getColumns().add(firstNameCol);}</pre>
 * 
 * This approach assumes that the object returned from <code>p.getValue()</code>
 * has a JavaFX {@link ObservableValue} that can simply be returned. The benefit of this
 * is that the TableViewNoEmptyRow will internally create bindings to ensure that,
 * should the returned {@link ObservableValue} change, the cell contents will be
 * automatically refreshed. 
 * 
 * <p>In situations where a TableColumnNoEmptyRow must interact with classes created before
 * JavaFX, or that generally do not wish to use JavaFX apis for properties, it is
 * possible to wrap the returned value in a {@link ReadOnlyObjectWrapper} instance. For
 * example:
 * 
 * <pre>
 * {@code 
 * firstNameCol.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
 *     public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
 *         return new ReadOnlyObjectWrapper(p.getValue().getFirstName());
 *     }
 *  });}</pre>
 * 
 * It is hoped that over time there will be convenience cell value factories 
 * developed and made available to developers. As of the JavaFX 2.0 release, 
 * there is one such convenience class: {@link PropertyValueFactory}. This class
 * removes the need to write the code above, instead relying on reflection to 
 * look up a given property from a String. Refer to the 
 * <code>PropertyValueFactory</code> class documentation for more information
 * on how to use this with a TableColumnNoEmptyRow.
 * 
 * Finally, for more detail on how to use TableColumnNoEmptyRow, there is further documentation in
 * the {@link TableViewNoEmptyRow} class documentation.
 * 
 * @param <S> The type of the TableViewNoEmptyRow generic type (i.e. S == TableViewNoEmptyRow&lt;S&gt;)
 * @param <T> The type of the content in all cells in this TableColumnNoEmptyRow.
 * @see TableViewNoEmptyRow
 * @see TableCell
 * @see TablePositionNoEmptyRow
 */
public class TableColumnNoEmptyRow<S,T> implements EventTarget {
    
    /***************************************************************************
     *                                                                         *
     * Static properties and methods                                           *
     *                                                                         *
     **************************************************************************/
    
    // NOTE: If these numbers change, update the copy of this value in TableColumnHeader
    private static final double DEFAULT_WIDTH = 80.0F;
    private static final double DEFAULT_MIN_WIDTH = 10.0F;
    private static final double DEFAULT_MAX_WIDTH = 5000.0F;
    
    
    /**
     * Parent event for any TableColumnNoEmptyRow edit event.
     */
    @SuppressWarnings("unchecked")
    public static <S,T> EventType<CellEditEvent<S,T>> editAnyEvent() {
        return (EventType<CellEditEvent<S,T>>) EDIT_ANY_EVENT;
    }
    private static final EventType<?> EDIT_ANY_EVENT =
            new EventType<CellEditEvent<Object,Object>>(Event.ANY, "EDIT");

    /**
     * Indicates that the user has performed some interaction to start an edit
     * event, or alternatively the {@link TableViewNoEmptyRow#edit(int, javafx.scene.control.TableColumnNoEmptyRow)}
     * method has been called.
     */
    @SuppressWarnings("unchecked")
    public static <S,T> EventType<CellEditEvent<S,T>> editStartEvent() {
        return (EventType<CellEditEvent<S,T>>) EDIT_START_EVENT;
    }
    private static final EventType<?> EDIT_START_EVENT =
            new EventType<CellEditEvent<Object,Object>>(editAnyEvent(), "EDIT_START");

    /**
     * Indicates that the editing has been canceled, meaning that no change should
     * be made to the backing data source.
     */
    @SuppressWarnings("unchecked")
    public static <S,T> EventType<CellEditEvent<S,T>> editCancelEvent() {
        return (EventType<CellEditEvent<S,T>>) EDIT_CANCEL_EVENT;
    }
    private static final EventType<?> EDIT_CANCEL_EVENT =
            new EventType<CellEditEvent<Object,Object>>(editAnyEvent(), "EDIT_CANCEL");

    /**
     * Indicates that the editing has been committed by the user, meaning that
     * a change should be made to the backing data source to reflect the new
     * data.
     */
    @SuppressWarnings("unchecked")
    public static <S,T> EventType<CellEditEvent<S,T>> editCommitEvent() {
        return (EventType<CellEditEvent<S,T>>) EDIT_COMMIT_EVENT;
    }
    private static final EventType<?> EDIT_COMMIT_EVENT =
            new EventType<CellEditEvent<Object,Object>>(editAnyEvent(), "EDIT_COMMIT");
    
    
    
    /**
     * If no cellFactory is specified on a TableColumnNoEmptyRow instance, then this one
     * will be used by default. At present it simply renders the TableCell item 
     * property within the {@link TableCell#graphicProperty() graphic} property
     * if the {@link Cell#item item} is a Node, or it simply calls 
     * <code>toString()</code> if it is not null, setting the resulting string
     * inside the {@link Cell#textProperty() text} property.
     */
    public static final Callback<TableColumnNoEmptyRow<?,?>, TableCell<?,?>> DEFAULT_CELL_FACTORY = new Callback<TableColumnNoEmptyRow<?,?>, TableCell<?,?>>() {
        @Override public TableCell<?,?> call(TableColumnNoEmptyRow<?,?> param) {
            return new TableCell() {
                @Override protected void updateItem(Object item, boolean empty) {
                    if (item == getItem()) return;

                    super.updateItem(item, empty);

                    if (item == null) {
                        super.setText(null);
                        super.setGraphic(null);
                    } else if (item instanceof Node) {
                        super.setText(null);
                        super.setGraphic((Node)item);
                    } else {
                        super.setText(item.toString());
                        super.setGraphic(null);
                    }
                }
            };
        }
    };

    /**
     * By default all columns will use this comparator to perform sorting. This
     * comparator simply performs null checks, and checks if the object is 
     * {@link Comparable}. If it is, the {@link Comparable#compareTo(java.lang.Object)}
     * method is called, otherwise this method will defer to
     * {@link Collator#compare(java.lang.String, java.lang.String)}.
     */
    public static final Comparator DEFAULT_COMPARATOR = new Comparator() {
        @Override public int compare(Object obj1, Object obj2) {
            if (obj1 == null && obj2 == null) return 0;
            if (obj1 == null) return -1;
            if (obj2 == null) return 1;
            
            if (obj1 instanceof Comparable) {
                return ((Comparable)obj1).compareTo(obj2);
            }

            return Collator.getInstance().compare(obj1.toString(), obj2.toString());
        }
    };
    
    
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/    

    /**
     * Creates a default TableColumnNoEmptyRow with default cell factory, comparator, and
     * onEditCommit implementation.
     */
    public TableColumnNoEmptyRow() {
        setOnEditCommit(DEFAULT_EDIT_COMMIT_HANDLER);

        // we listen to the columns list here to ensure that widths are
        // maintained properly, and to also set the column hierarchy such that
        // all children columns know that this TableColumnNoEmptyRow is their parent.
        getColumns().addListener(weakColumnsListener);

        tableViewProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                // set all children of this tableView to have the same TableViewNoEmptyRow
                // as this column
                for (TableColumnNoEmptyRow<S, ?> tc : getColumns()) {
                    tc.setTableView(getTableView());
                }
                
                // set the parent of this column to also have this tableView
                if (getParentColumn() != null) {
                    getParentColumn().setTableView(getTableView());
                }
            }
        });
    }

    /**
     * Creates a TableColumnNoEmptyRow with the text set to the provided string, with
     * default cell factory, comparator, and onEditCommit implementation.
     * @param text The string to show when the TableColumnNoEmptyRow is placed within the TableViewNoEmptyRow.
     */
    public TableColumnNoEmptyRow(String text) {
        this();
        setText(text);
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Listeners                                                               *
     *                                                                         *
     **************************************************************************/
    
    private EventHandler<CellEditEvent<S,T>> DEFAULT_EDIT_COMMIT_HANDLER = new EventHandler<CellEditEvent<S,T>>() {
        @Override public void handle(CellEditEvent<S,T> t) {
            int index = t.getTablePosition().getRow();
            List<S> list = t.getTableView().getItems();
            if (list == null || index < 0 || index >= list.size()) return;
            S rowData = list.get(index);
            ObservableValue<T> ov = getCellObservableValue(rowData);
            
            if (ov instanceof WritableValue) {
                ((WritableValue)ov).setValue(t.getNewValue());
            }
        }
    };
    
    private ListChangeListener columnsListener = new ListChangeListener<TableColumnNoEmptyRow<S,?>>() {
        @Override public void onChanged(Change<? extends TableColumnNoEmptyRow<S,?>> c) {
            while (c.next()) {
                // update the TableColumnNoEmptyRow.tableView property
                for (TableColumnNoEmptyRow<S,?> tc : c.getRemoved()) {
                    // Fix for RT-16978. In TableColumnHeader we add before we
                    // remove when moving a TableColumnNoEmptyRow. This means that for
                    // a very brief moment the tc is duplicated, and we can prevent
                    // nulling out the tableview and parent column. Without this
                    // here, in a very special circumstance it is possible to null
                    // out the entire content of a column by reordering and then
                    // sorting another column.
                    if (getColumns().contains(tc)) continue;
                    
                    tc.setTableView(null);
                    tc.setParentColumn(null);
                }
                for (TableColumnNoEmptyRow<S,?> tc : c.getAddedSubList()) {
                    tc.setTableView(getTableView());
                    tc.setVisible(isVisible()); // See visible property TODO, we probably don't want this
                }

                if (! getColumns().isEmpty()) {
                    // zero out the width and min width values, and iterate to 
                    // ensure the new value is equal to the sum of all children
                    // columns
                    double minWidth = 0.0f;
                    double prefWidth = 0.0f;
                    double maxWidth = 0.0f;

                    for (TableColumnNoEmptyRow col : getColumns()) {
                        col.setParentColumn(TableColumnNoEmptyRow.this);

                        minWidth += col.getMinWidth();
                        prefWidth += col.getPrefWidth();
                        maxWidth += col.getMaxWidth();
                    }

                    setMinWidth(minWidth);
                    setPrefWidth(prefWidth);
                    setMaxWidth(maxWidth);
                }
            }
        }
    };
    
    private WeakListChangeListener weakColumnsListener = 
            new WeakListChangeListener(columnsListener);
    
    
    /***************************************************************************
     *                                                                         *
     * Instance Variables                                                      *
     *                                                                         *
     **************************************************************************/
    
    // Contains any children columns that should be nested within this column
    private final ObservableList<TableColumnNoEmptyRow<S,?>> columns = FXCollections.<TableColumnNoEmptyRow<S,?>>observableArrayList();
    
    // Made static based on findings of RT-18344 - EventHandlerManager is an
    // expensive class and should be reused amongst classes if at all possible.
    private final EventHandlerManager eventHandlerManager = new EventHandlerManager(this);
    
    
    
    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/
    
    
    // --- TableViewNoEmptyRow
    /**
     * The TableViewNoEmptyRow that this TableColumnNoEmptyRow belongs to.
     */
    private ReadOnlyObjectWrapper<TableViewNoEmptyRow<S>> tableView = new ReadOnlyObjectWrapper<TableViewNoEmptyRow<S>>(this, "tableView");
    public final ReadOnlyObjectProperty<TableViewNoEmptyRow<S>> tableViewProperty() {
        return tableView.getReadOnlyProperty();
    }
    final void setTableView(TableViewNoEmptyRow<S> value) { tableView.set(value); }
    public final TableViewNoEmptyRow<S> getTableView() { return tableView.get(); }

    
    // --- Text
    /**
     * This is the text to show in the header for this column.
     */
    private StringProperty text = new SimpleStringProperty(this, "text", "");
    public final StringProperty textProperty() { return text; }
    public final void setText(String value) { text.set(value); }
    public final String getText() { return text.get(); }
    
    
    // --- Visible
    /**
     * Toggling this will immediately toggle the visibility of this column,
     * and all children columns.
     */
    private BooleanProperty visible = new BooleanPropertyBase(true) {
        @Override protected void invalidated() {
            // set all children columns to be the same visibility. This isn't ideal,
            // for example if a child column is hidden, then the parent hidden and
            // shown, all columns will be visible again.
            //
            // TODO It may make sense for us to cache the visibility so that we may
            // return to exactly the same state.
            // set all children columns to be the same visibility. This isn't ideal,
            // for example if a child column is hidden, then the parent hidden and
            // shown, all columns will be visible again.
            //
            // TODO It may make sense for us to cache the visibility so that we may
            // return to exactly the same state.
            for (TableColumnNoEmptyRow<S,?> col : getColumns()) {
                col.setVisible(isVisible());
            }
        }

        @Override
        public Object getBean() {
            return TableColumnNoEmptyRow.this;
        }

        @Override
        public String getName() {
            return "visible";
        }
    };
    public final void setVisible(boolean value) { visibleProperty().set(value); }
    public final boolean isVisible() { return visible.get(); }
    public final BooleanProperty visibleProperty() { return visible; }
    
    
    // --- Parent Column
    /**
     * This read-only property will always refer to the parent of this column,
     * in the situation where nested columns are being used. To create a nested
     * column is simply a matter of placing TableColumnNoEmptyRow instances inside the
     * {@link #columns} ObservableList of a TableColumnNoEmptyRow.
     */
    private ReadOnlyObjectWrapper<TableColumnNoEmptyRow<S,?>> parentColumn;
    private void setParentColumn(TableColumnNoEmptyRow<S,?> value) { parentColumnPropertyImpl().set(value); }
    public final TableColumnNoEmptyRow<S,?> getParentColumn() {
        return parentColumn == null ? null : parentColumn.get();
    }

    public final ReadOnlyObjectProperty<TableColumnNoEmptyRow<S,?>> parentColumnProperty() {
        return parentColumnPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<TableColumnNoEmptyRow<S,?>> parentColumnPropertyImpl() {
        if (parentColumn == null) {
            parentColumn = new ReadOnlyObjectWrapper<TableColumnNoEmptyRow<S,?>>(this, "parentColumn");
        }
        return parentColumn;
    }
    
    
    // --- Menu
    /**
     * This menu will be shown whenever the user right clicks within the header
     * area of this TableColumnNoEmptyRow.
     */
    private ObjectProperty<ContextMenu> contextMenu;
    public final void setContextMenu(ContextMenu value) { contextMenuProperty().set(value); }
    public final ContextMenu getContextMenu() { return contextMenu == null ? null : contextMenu.get(); }
    public final ObjectProperty<ContextMenu> contextMenuProperty() {
        if (contextMenu == null) {
            contextMenu = new SimpleObjectProperty<ContextMenu>(this, "contextMenu");
        }
        return contextMenu;
    }
    
    
    // --- Cell value factory
    /**
     * The cell value factory needs to be set to specify how to populate all
     * cells within a single TableColumnNoEmptyRow. A cell value factory is a {@link Callback}
     * that provides a {@link CellDataFeatures} instance, and expects an
     * {@link ObservableValue} to be returned. The returned ObservableValue instance
     * will be observed internally to allow for immediate updates to the value
     * to be reflected on screen.
     * 
     * An example of how to set a cell value factory is:
     * 
     * <pre><code>
     * lastNameCol.setCellValueFactory(new Callback&lt;CellDataFeatures&lt;Person, String&gt;, ObservableValue&lt;String&gt;&gt;() {
     *     public ObservableValue&lt;String&gt; call(CellDataFeatures&lt;Person, String&gt; p) {
     *         // p.getValue() returns the Person instance for a particular TableViewNoEmptyRow row
     *         return p.getValue().lastNameProperty();
     *     }
     *  });
     * }
     * </code></pre>
     * 
     * A common approach is to want to populate cells in a TableColumnNoEmptyRow using
     * a single value from a Java bean. To support this common scenario, there
     * is the {@link PropertyValueFactory} class. Refer to this class for more 
     * information on how to use it, but briefly here is how the above use case
     * could be simplified using the PropertyValueFactory class:
     * 
     * <pre><code>
     * lastNameCol.setCellValueFactory(new PropertyValueFactory&lt;Person,String&gt;("lastName"));
     * </code></pre>
     * 
     * @see PropertyValueFactory
     */
    private ObjectProperty<Callback<CellDataFeatures<S,T>, ObservableValue<T>>> cellValueFactory;
    public final void setCellValueFactory(Callback<CellDataFeatures<S,T>, ObservableValue<T>> value) {
        cellValueFactoryProperty().set(value);
    }
    public final Callback<CellDataFeatures<S,T>, ObservableValue<T>> getCellValueFactory() {
        return cellValueFactory == null ? null : cellValueFactory.get();
    }
    public final ObjectProperty<Callback<CellDataFeatures<S,T>, ObservableValue<T>>> cellValueFactoryProperty() {
        if (cellValueFactory == null) {
            cellValueFactory = new SimpleObjectProperty<Callback<CellDataFeatures<S,T>, ObservableValue<T>>>(this, "cellValueFactory");
        }
        return cellValueFactory;
    }
    
    
    // --- Cell Factory
    /**
     * The cell factory for all cells in this column. The cell factory
     * is responsible for rendering the data contained within each TableCell for
     * a single table column.
     * By default TableColumnNoEmptyRow uses the {@link #DEFAULT_CELL_FACTORY default cell
     * factory}, but this can be replaced with a custom implementation, for 
     * example to show data in a different way or to support editing.
     *
     * <p>There is a lot of documentation on creating custom cell factories
     * elsewhere (see {@link Cell} and {@link TableViewNoEmptyRow} for example).</p>
     *
     */
    private final ObjectProperty<Callback<TableColumnNoEmptyRow<S,T>, TableCell<S,T>>> cellFactory =
            new SimpleObjectProperty<Callback<TableColumnNoEmptyRow<S,T>, TableCell<S,T>>>(this, "cellFactory", (Callback<TableColumnNoEmptyRow<S,T>, TableCell<S,T>>) ((Callback) DEFAULT_CELL_FACTORY));

    public final void setCellFactory(Callback<TableColumnNoEmptyRow<S,T>, TableCell<S,T>> value) {
        cellFactory.set(value);
    }

    public final Callback<TableColumnNoEmptyRow<S,T>, TableCell<S,T>> getCellFactory() {
        return cellFactory.get();
    }

    public final ObjectProperty<Callback<TableColumnNoEmptyRow<S,T>, TableCell<S,T>>> cellFactoryProperty() {
        return cellFactory;
    }
    
    
    // --- Width
    /**
     * The width of this column. Modifying this will result in the column width
     * adjusting visually. It is recommended to not bind this property to an
     * external property, as that will result in the column width not being
     * adjustable by the user through dragging the left and right borders of
     * column headers.
     */
    public final ReadOnlyDoubleProperty widthProperty() { return width.getReadOnlyProperty(); }
    public final double getWidth() { return width.get(); }
    private void setWidth(double value) { width.set(value); }
    private ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper(this, "width", DEFAULT_WIDTH);
    
    
    // --- Minimum Width
    private DoubleProperty minWidth;
    public final void setMinWidth(double value) { minWidthProperty().set(value); }
    public final double getMinWidth() { return minWidth == null ? DEFAULT_MIN_WIDTH : minWidth.get(); }

    /**
     * The minimum width the TableColumnNoEmptyRow is permitted to be resized to.
     */
    public final DoubleProperty minWidthProperty() {
        if (minWidth == null) {
            minWidth = new DoublePropertyBase(DEFAULT_MIN_WIDTH) {
                @Override protected void invalidated() {
                    if (getMinWidth() < 0) {
                        setMinWidth(0.0F);
                    }

                    impl_setWidth(getWidth());
                }

                @Override
                public Object getBean() {
                    return TableColumnNoEmptyRow.this;
                }

                @Override
                public String getName() {
                    return "minWidth";
                }
            };
        }
        return minWidth;
    }
    
    
    // --- Preferred Width
    /**
     * The preferred width of the TableColumnNoEmptyRow.
     */
    public final DoubleProperty prefWidthProperty() { return prefWidth; }
    public final void setPrefWidth(double value) { prefWidthProperty().set(value); }
    public final double getPrefWidth() { return prefWidth.get(); }
    private final DoubleProperty prefWidth = new DoublePropertyBase(DEFAULT_WIDTH) {
        @Override protected void invalidated() {
            impl_setWidth(getPrefWidth());
        }

        @Override
        public Object getBean() {
            return TableColumnNoEmptyRow.this;
        }

        @Override
        public String getName() {
            return "prefWidth";
        }
    };
    
    
    // --- Maximum Width
    // The table does not resize properly if this is set to Number.MAX_VALUE,
    // so I've arbitrarily chosen a better, smaller number.
    /**
     * The maximum width the TableColumnNoEmptyRow is permitted to be resized to.
     */
    public final DoubleProperty maxWidthProperty() { return maxWidth; }
    public final void setMaxWidth(double value) { maxWidthProperty().set(value); }
    public final double getMaxWidth() { return maxWidth.get(); }
    private DoubleProperty maxWidth = new DoublePropertyBase(DEFAULT_MAX_WIDTH) {
        @Override protected void invalidated() {
            impl_setWidth(getWidth());
        }

        @Override
        public Object getBean() {
            return TableColumnNoEmptyRow.this;
        }

        @Override
        public String getName() {
            return "maxWidth";
        }
    };
    
    
    // --- Resizable
    /**
     * Used to indicate whether the width of this column can change. It is up
     * to the resizing policy to enforce this however.
     */
    private BooleanProperty resizable;
    public final BooleanProperty resizableProperty() {
        if (resizable == null) {
            resizable = new SimpleBooleanProperty(this, "resizable", true);
        }
        return resizable;
    }
    public final void setResizable(boolean value) {
        resizableProperty().set(value);
    }
    public final boolean isResizable() {
        return resizable == null ? true : resizable.get();
    }

    
    // --- Sort Type
    /**
     * Used to state whether this column, if it is part of the 
     * {@link TableViewNoEmptyRow#sortOrder} ObservableList, should be sorted in ascending 
     * or descending order. Simply toggling
     * this property will result in the sort order changing in the TableViewNoEmptyRow,
     * assuming of course that this column is in the sortOrder ObservableList to
     * begin with.
     */
    private ObjectProperty<SortType> sortType;
    public final ObjectProperty<SortType> sortTypeProperty() {
        if (sortType == null) {
            sortType = new SimpleObjectProperty<SortType>(this, "sortType", SortType.ASCENDING);
        }
        return sortType;
    }
    public final void setSortType(SortType value) {
        sortTypeProperty().set(value);
    }
    public final SortType getSortType() {
        return sortType == null ? SortType.ASCENDING : sortType.get();
    }
    
    
    // --- Sortable
    /**
     * <p>A boolean property to toggle on and off the sortability of this column.
     * When this property is true, this column can be included in sort
     * operations. If this property is false, it will not be included in sort
     * operations, even if it is contained within {@link TableViewNoEmptyRow#sortOrder}.</p>
     *
     * <p>If a TableColumnNoEmptyRow instance is contained within the TableViewNoEmptyRow sortOrder
     * ObservableList, and its sortable property toggles state, it will force the
     * TableViewNoEmptyRow to perform a sort, as it is likely the view will need updating.</p>
     */
    private BooleanProperty sortable;
    public final BooleanProperty sortableProperty() {
        if (sortable == null) {
            sortable = new SimpleBooleanProperty(this, "sortable", true);
        }
        return sortable;
    }
    public final void setSortable(boolean value) {
        sortableProperty().set(value);
    }
    public final boolean isSortable() {
        return sortable == null ? true : sortable.get();
    }
    
    
    // --- Comparator
    /**
     * Comparator function used when sorting this TableColumnNoEmptyRow. The two Objects
     * given as arguments are the cell data for two individual cells in this
     * column.
     */
    private ObjectProperty<Comparator<T>> comparator;
    public final ObjectProperty<Comparator<T>> comparatorProperty() {
        if (comparator == null) {
            comparator = new SimpleObjectProperty<Comparator<T>>(this, "comparator", DEFAULT_COMPARATOR);
        }
        return comparator;
    }
    public final void setComparator(Comparator<T> value) {
        comparatorProperty().set(value);
    }
    public final Comparator<T> getComparator() {
        return comparator == null ? DEFAULT_COMPARATOR : comparator.get();
    }

    
    // --- Editable
    private BooleanProperty editable;
    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }
    public final boolean isEditable() {
        return editable == null ? true : editable.get();
    }
    /**
     * Specifies whether this TableColumnNoEmptyRow allows editing. This, unlike TableViewNoEmptyRow,
     * is true by default.
     */
    public final BooleanProperty editableProperty() {
        if (editable == null) {
            editable = new SimpleBooleanProperty(this, "editable", true);
        }
        return editable;
    }
    
    
    // --- On Edit Start
    private ObjectProperty<EventHandler<CellEditEvent<S,T>>> onEditStart;
    public final void setOnEditStart(EventHandler<CellEditEvent<S,T>> value) {
        onEditStartProperty().set(value);
    }
    public final EventHandler<CellEditEvent<S,T>> getOnEditStart() {
        return onEditStart == null ? null : onEditStart.get();
    }
    /**
     * This event handler will be fired when the user successfully initiates
     * editing.
     */
    public final ObjectProperty<EventHandler<CellEditEvent<S,T>>> onEditStartProperty() {
        if (onEditStart == null) {
            onEditStart = new ObjectPropertyBase<EventHandler<CellEditEvent<S,T>>>() {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(TableColumnNoEmptyRow.<S,T>editStartEvent(), get());
                }

                @Override
                public Object getBean() {
                    return TableColumnNoEmptyRow.this;
                }

                @Override
                public String getName() {
                    return "onEditStart";
                }
            };
        }
        return onEditStart;
    }


    // --- On Edit Commit
    private ObjectProperty<EventHandler<CellEditEvent<S,T>>> onEditCommit =
            new ObjectPropertyBase<EventHandler<CellEditEvent<S,T>>>() {
        @Override protected void invalidated() {
            eventHandlerManager.setEventHandler(TableColumnNoEmptyRow.<S,T>editCommitEvent(), get());
        }

        @Override public Object getBean() {
            return TableColumnNoEmptyRow.this;
        }

        @Override public String getName() {
            return "onEditCommit";
        }
    };
    public final void setOnEditCommit(EventHandler<CellEditEvent<S,T>> value) {
        onEditCommit.set(value);
    }
    public final EventHandler<CellEditEvent<S,T>> getOnEditCommit() {
        return onEditCommit.get();
    }
    /**
     * This event handler will be fired when the user successfully commits their
     * editing.
     */
    public final ObjectProperty<EventHandler<CellEditEvent<S,T>>> onEditCommitProperty() {
        return onEditCommit;
    }


    // --- On Edit Cancel
    private ObjectProperty<EventHandler<CellEditEvent<S,T>>> onEditCancel;
    public final void setOnEditCancel(EventHandler<CellEditEvent<S,T>> value) {
        onEditCancelProperty().set(value);
    }
    public final EventHandler<CellEditEvent<S,T>> getOnEditCancel() {
        return onEditCancel == null ? null : onEditCancel.get();
    }
    /**
     * This event handler will be fired when the user cancels editing a cell.
     */
    public final ObjectProperty<EventHandler<CellEditEvent<S,T>>> onEditCancelProperty() {
        if (onEditCancel == null) {
            onEditCancel = new ObjectPropertyBase<EventHandler<CellEditEvent<S,T>>>() {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(TableColumnNoEmptyRow.<S,T>editCancelEvent(), get());
                }

                @Override
                public Object getBean() {
                    return TableColumnNoEmptyRow.this;
                }

                @Override
                public String getName() {
                    return "onEditCancel";
                }
            };
        }
        return onEditCancel;
    }
    
    
    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/    
    
    /**
     * This enables support for nested columns, which can be useful to group
     * together related data. For example, we may have a 'Name' column with
     * two nested columns for 'First' and 'Last' names.
     *
     * <p>This has no impact on the table as such - all column indices point to the
     * leaf columns only, and it isn't possible to sort using the parent column,
     * just the leaf columns. In other words, this is purely a visual feature.</p>
     *
     * <p>Modifying the order or contents of this ObservableList will result in the
     * viewColumns ObservableList being reset to equal this ObservableList.</p>
     * 
     * @return An ObservableList containing TableColumnNoEmptyRow instances that are the
     *      children of this TableColumnNoEmptyRow. If these children TableColumnNoEmptyRow instances
     *      are set as visible, they will appear beneath this TableColumnNoEmptyRow.
     */
    public final ObservableList<TableColumnNoEmptyRow<S,?>> getColumns() {
        return columns;
    }
    
    /**
     * Returns the actual value for a cell at a given row index (and which 
     * belongs to this TableColumnNoEmptyRow).
     * 
     * @param index The row index for which the data is required.
     * @return The data that belongs to the cell at the intersection of the given
     *      row index and the TableColumnNoEmptyRow that this method is called on.
     */
    public final T getCellData(final int index) {
        ObservableValue<T> result = getCellObservableValue(index);
        return result == null ? null : result.getValue();
    }

    /**
     * Returns the actual value for a cell from the given item.
     * 
     * @param item The item from which a value of type T should be extracted.
     * @return The data that should be used in a specific cell in this 
     *      column, based on the item passed in as an argument.
     */
    public final T getCellData(final S item) {
        ObservableValue<T> result = getCellObservableValue(item);
        return result == null ? null : result.getValue();
    }

    /**
     * Attempts to return an ObservableValue&lt;T&gt; for the item in the given
     * index (which is of type S). In other words, this method expects to receive
     * an integer value that is greater than or equal to zero, and less than the
     * size of the {@link TableViewNoEmptyRow#itemsProperty() items} list. If the index is
     * valid, this method will return an ObservableValue&lt;T&gt; for this 
     * specific column.
     * 
     * <p>This is achieved by calling the 
     * {@link #cellFactoryProperty() cell value factory}, and returning whatever
     * it returns when passed a {@link CellDataFeatures} containing the current
     * {@link TableViewNoEmptyRow}, as well as the TableColumnNoEmptyRow and <code>item</code>
     * provided to this method.
     * 
     * @param index The index of the item (of type S) for which an 
     *      ObservableValue&lt;T&gt; is sought.
     * @return An ObservableValue&lt;T&gt; for this specific TableColumnNoEmptyRow.
     */
    public final ObservableValue<T> getCellObservableValue(int index) {
        if (index < 0) return null;
        // Get the table
        final TableViewNoEmptyRow<S> table = getTableView();
        if (table == null || table.getItems() == null) return null;
        // Get the rowData
        final List<S> items = table.getItems();
        if (index >= items.size()) return null; // Out of range
        final S rowData = items.get(index);
        return getCellObservableValue(rowData);
    }
    
    /**
     * Attempts to return an ObservableValue&lt;T&gt; for the given item (which
     * is of type S). In other words, this method expects to receive an object
     * that is of the same type as the TableViewNoEmptyRow&lt;S&gt;, and returning an
     * ObservableValue&lt;T&gt; for this specific column.
     * 
     * <p>This is achieved by calling the 
     * {@link #cellFactoryProperty() cell value factory}, and returning whatever
     * it returns when passed a {@link CellDataFeatures} containing the current
     * {@link TableViewNoEmptyRow}, as well as the TableColumnNoEmptyRow and <code>item</code>
     * provided to this method.
     * 
     * @param item The item (of type S) for which an ObservableValue&lt;T&gt; is
     *      sought.
     * @return An ObservableValue&lt;T&gt; for this specific TableColumnNoEmptyRow.
     */
    public final ObservableValue<T> getCellObservableValue(S item) {
        // Get the factory
        final Callback<CellDataFeatures<S,T>, ObservableValue<T>> factory = getCellValueFactory();
        if (factory == null) return null;
        // Get the table
        final TableViewNoEmptyRow<S> table = getTableView();
        if (table == null) return null;
        // Call the factory
        final CellDataFeatures<S,T> cdf = new CellDataFeatures<S,T>(table, this, item);
        return factory.call(cdf);
    }

    /** {@inheritDoc} */
    @Override public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.prepend(eventHandlerManager);
    }

    /**
     * Registers an event handler to this TableColumnNoEmptyRow. The TableColumnNoEmptyRow class allows 
     * registration of listeners which will be notified when editing occurs.
     * Note however that a TableColumnNoEmptyRow is <b>not</b> a Node, and therefore no visual
     * events will be fired on the TableColumnNoEmptyRow.
     *
     * @param eventType the type of the events to receive by the handler
     * @param eventHandler the handler to register
     * @throws NullPointerException if the event type or handler is null
     */
    public <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        eventHandlerManager.addEventHandler(eventType, eventHandler);
    }
    
    /**
     * Unregisters a previously registered event handler from this TableColumnNoEmptyRow. One
     * handler might have been registered for different event types, so the
     * caller needs to specify the particular event type from which to
     * unregister the handler.
     *
     * @param eventType the event type from which to unregister
     * @param eventHandler the handler to unregister
     * @throws NullPointerException if the event type or handler is null
     */
    public <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<E> eventHandler) {
        eventHandlerManager.removeEventHandler(eventType, eventHandler);
    }

    
    /***************************************************************************
     *                                                                         *
     * Private Implementation                                                  *
     *                                                                         *
     **************************************************************************/        

    /**
     * @treatAsPrivate implementation detail
     * @deprecated This is an internal API that is not intended for use and will be removed in the next version
     */
    @Deprecated
    public void impl_setWidth(double width) {
        setWidth(Utils.boundedSize(width, getMinWidth(), getMaxWidth()));
    }



    /***************************************************************************
     *                                                                         *
     * Support Interfaces                                                      *
     *                                                                         *
     **************************************************************************/

    /**
     * Enumeration that specifies the type of sorting being applied to a specific
     * column.
     */
    public static enum SortType {
        /**
         * Column will be sorted in an ascending order.
         */
        ASCENDING,
        
        /**
         * Column will be sorted in a descending order.
         */
        DESCENDING;
        
        // UNSORTED
    }
    
    /**
     * A support class used in TableColumnNoEmptyRow as a wrapper class 
     * to provide all necessary information for a particular {@link Cell}. Once
     * instantiated, this class is immutable.
     * 
     * @param <S> The TableViewNoEmptyRow type
     * @param <T> The TableColumnNoEmptyRow type
     */
    public static class CellDataFeatures<S,T> {
        private final TableViewNoEmptyRow<S> tableView;
        private final TableColumnNoEmptyRow<S,T> tableColumn;
        private final S value;

        /**
         * Instantiates a CellDataFeatures instance with the given properties
         * set as read-only values of this instance.
         * 
         * @param tableView The TableViewNoEmptyRow that this instance refers to.
         * @param tableColumn The TableColumnNoEmptyRow that this instance refers to.
         * @param value The value for a row in the TableViewNoEmptyRow.
         */
        public CellDataFeatures(TableViewNoEmptyRow<S> tableView,
                TableColumnNoEmptyRow<S,T> tableColumn, S value) {
            this.tableView = tableView;
            this.tableColumn = tableColumn;
            this.value = value;
        }

        /**
         * Returns the value passed in to the constructor.
         */
        public S getValue() {
            return value;
        }

        /**
         * Returns the {@link TableColumnNoEmptyRow} passed in to the constructor.
         */
        public TableColumnNoEmptyRow<S,T> getTableColumn() {
            return tableColumn;
        }

        /**
         * Returns the {@link TableViewNoEmptyRow} passed in to the constructor.
         */
        public TableViewNoEmptyRow<S> getTableView() {
            return tableView;
        }
    }
    
    
    
    /**
     * An event that is fired when a user performs an edit on a table cell.
     */
    public static class CellEditEvent<S,T> extends Event {

        // represents the new value input by the end user. This is NOT the value
        // to go back into the TableViewNoEmptyRow.items list - this new value represents
        // just the input for a single cell, so it is likely that it needs to go
        // back into a property within an item in the TableViewNoEmptyRow.items list.
        private final T newValue;

        // The location of the edit event
        private transient final TablePositionNoEmptyRow<S,T> pos;

        /**
         * Creates a new event that can be subsequently fired to the relevant listeners.
         *
         * @param table The TableViewNoEmptyRow on which this event occurred.
         * @param pos The position upon which this event occurred.
         * @param eventType The type of event that occurred.
         * @param newValue The value input by the end user.
         */
        public CellEditEvent(TableViewNoEmptyRow<S> table, TablePositionNoEmptyRow<S,T> pos,
                EventType<CellEditEvent> eventType, T newValue) {
            super(table, Event.NULL_SOURCE_TARGET, eventType);

            if (table == null) {
                throw new NullPointerException("TableViewNoScroll can not be null");
            }
            if (pos == null) {
                throw new NullPointerException("TablePositionNoScroll can not be null");
            }
            this.pos = pos;
            this.newValue = newValue;
        }

        /**
         * Returns the TableViewNoEmptyRow upon which this event occurred.
         * @return The TableViewNoEmptyRow control upon which this event occurred.
         */
        public TableViewNoEmptyRow<S> getTableView() {
            return pos.getTableView();
        }

        /**
         * Returns the TableColumnNoEmptyRow upon which this event occurred.
         *
         * @return The TableColumnNoEmptyRow that the edit occurred in.
         */
        public TableColumnNoEmptyRow<S,T> getTableColumn() {
            return pos.getTableColumn();
        }

        /**
         * Returns the position upon which this event occurred.
         * @return The position upon which this event occurred.
         */
        public TablePositionNoEmptyRow<S,T> getTablePosition() {
            return pos;
        }

        /**
         * Returns the new value input by the end user. This is <b>not</b> the value
         * to go back into the TableViewNoEmptyRow.items list - this new value represents
         * just the input for a single cell, so it is likely that it needs to go
         * back into a property within an item in the TableViewNoEmptyRow.items list.
         *
         * @return An Object representing the new value input by the user.
         */
        public T getNewValue() {
            return newValue;
        }

        /**
         * Attempts to return the old value at the position referred to in the
         * TablePositionNoEmptyRow returned by {@link #getTablePosition()}. This may return
         * null for a number of reasons.
         *
         * @return Returns the value stored in the position being edited, or null
         *     if it can not be retrieved.
         */
        public T getOldValue() {
            S rowData = getRowValue();
            if (rowData == null || pos.getTableColumn() == null) {
                return null;
            }

            // if we are here, we now need to get the data for the specific column
            return (T) pos.getTableColumn().getCellData(rowData);
        }
        
        /**
         * Convenience method that returns the value for the row (that is, from
         * the TableViewNoEmptyRow {@link TableViewNoEmptyRow#itemsProperty() items} list), for the
         * row contained within the {@link TablePositionNoEmptyRow} returned in
         * {@link #getTablePosition()}.
         */
        public S getRowValue() {
            List<S> items = getTableView().getItems();
            if (items == null) return null;

            int row = pos.getRow();
            if (row < 0 || row >= items.size()) return null;

            return items.get(row);
        }
    }
}
