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

import com.sun.javafx.scene.control.WeakListChangeListener;
import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <p>This class is used to construct the header of a TableView. We take the approach
 * that every TableView header is nested - even if it isn't. This allows for us
 * to use the same code for building a single row of TableColumns as we would
 * with a heavily nested sequences of TableColumns. Because of this, the
 * TableHeaderRow class consists of just one instance of a NestedTableColumnHeader.
 *
 */
class NestedTableColumnHeaderNoEmptyRow extends TableColumnHeaderNoEmptyRow {
    
    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/
    
    public NestedTableColumnHeaderNoEmptyRow(TableView table, TableColumn tc) {
        super(table, tc);

        getStyleClass().setAll("nested-column-header");
        setFocusTraversable(false);

        initUI();

        // watching for changes to the view columns in either table or tableColumn.
        if (getTableColumn() == null && getTableView() != null) {
            setColumns(getTableView().getColumns());
        } else if (getTableColumn() != null) {
            setColumns(getTableColumn().getColumns());
        }
        getColumns().addListener(weakColumnsListener);
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Listeners                                                               *
     *                                                                         *
     **************************************************************************/
    
    private final ListChangeListener<TableColumn> columnsListener = new ListChangeListener<TableColumn>() {
        @Override public void onChanged(Change<? extends TableColumn> c) {
            setColumns(c.getList());
        }
    };
    
    private final InvalidationListener columnTextListener = new InvalidationListener() {
        @Override public void invalidated(Observable property) {
            label.setVisible(getTableColumn().getText() != null && ! getTableColumn().getText().isEmpty());
        }
    };
    
    private final InvalidationListener resizePolicyListener = new InvalidationListener() {
        @Override public void invalidated(Observable property) {
            updateContent();
        }
    };
    
    private final WeakListChangeListener weakColumnsListener =
            new WeakListChangeListener(columnsListener);
    
    private final WeakInvalidationListener weakColumnTextListener =
            new WeakInvalidationListener(columnTextListener);
    
    private final WeakInvalidationListener weakResizePolicyListener =
            new WeakInvalidationListener(resizePolicyListener);
    
    

    private static final int DRAG_RECT_WIDTH = 4;

    @Override public void setTableHeaderRow(TableHeaderRowNoEmptyRow header) {
        super.setTableHeaderRow(header);

        label.setTableHeaderRow(header);

        // tell all children columns what TableHeader they belong to
        for (TableColumnHeaderNoEmptyRow c : getColumnHeaders()) {
            c.setTableHeaderRow(header);
        }
    }

    @Override public void setParentHeader(NestedTableColumnHeaderNoEmptyRow parentHeader) {
        super.setParentHeader(parentHeader);
        label.setParentHeader(parentHeader);
    }

    /**
     * Represents the actual columns directly contained in this nested column.
     * It does NOT include ANY of the children of these columns, if any exist.
     */
    private ObservableList<? extends TableColumn> columns;
    public ObservableList<? extends TableColumn> getColumns() { return columns; }
    public void setColumns(ObservableList<? extends TableColumn> newColumns) {
        this.columns = newColumns;  
        
        // update the column headers. 
        // We make a copy of all current headers
        ArrayList<TableColumnHeaderNoEmptyRow> copy = new ArrayList(getColumnHeaders());
        
        // then clear out the column headers
        getColumnHeaders().clear();
        
        // then iterate through all columns. We try to reuse the column if it
        // previously existed, rather than recreate it.
        for (int i = 0; i < getColumns().size(); i++) {
            TableColumnHeaderNoEmptyRow header = null;
            TableColumn<?,?> column = getColumns().get(i);
            
            if (column == null) continue;
            
            for (int j = 0; j < copy.size(); j++) {
                if (column.equals(copy.get(j).getTableColumn())) {
                    header = copy.get(j);
                    break;
                }
            }
            
            if (header == null) {
                header = createColumnHeader(column);
            }
            
            getColumnHeaders().add(header);
        }
        
//        // we put all column headers into the list, even if they are not visible.
//        // This ensures that we can accurately calculate the height of the 
//        // table header row, even when no columns are visible. See RT-14434 for
//        // the bug report (in addition to this change, I also changed the 
//        // prefHeight calculations to ignore whether or not the column is 
//        // visible).
//        for (TableColumn c : getColumns()) {
//            getColumnHeaders().add(createColumnHeader(c));
//        }

        // update the content
        updateContent();
    }

    private TableColumnHeaderNoEmptyRow label;

    private List<TableColumnHeaderNoEmptyRow> columnHeaders;
    public List<TableColumnHeaderNoEmptyRow> getColumnHeaders() { 
        if (columnHeaders == null) columnHeaders = new ArrayList<TableColumnHeaderNoEmptyRow>();
        return columnHeaders; 
    }

    private void initUI() {
        label = new TableColumnHeaderNoEmptyRow(getTableView(), getTableColumn());
        label.setTableHeaderRow(getTableHeaderRow());
        label.setParentHeader(getParentHeader());
        label.setNestedColumnHeader(this);

        if (getTableColumn() != null) {
            getTableColumn().textProperty().addListener(weakColumnTextListener);
        }
        
        getTableView().columnResizePolicyProperty().addListener(weakResizePolicyListener);

        updateContent();
    }

    private void updateContent() {
        // create a temporary list so we only do addAll into the main content
        // observableArrayList once.
        List<Node> content = new ArrayList<Node>();

        // the label is the region that sits above the children columns
        content.add(label);

        // all children columns
        content.addAll(getColumnHeaders());

        // Small transparent overlays that sit at the start and end of each
        // column to intercept user drag gestures to enable column resizing.
        rebuildDragRects();
        content.addAll(dragRects);

        getChildren().setAll(content);
    }

    private void rebuildDragRects() {
        // drag rectangle overlays
        dragRects = new ArrayList<Rectangle>();

        if (getColumns() == null) {
            return;
        }

        boolean isConstrainedResize = TableView.CONSTRAINED_RESIZE_POLICY.equals(
                getTableView().getColumnResizePolicy());
        
        for (int col = 0; col < getColumns().size(); col++) {
            if (isConstrainedResize && col == getColumns().size() - 1) {
                break;
            }
            
            final TableColumn c = getColumns().get(col);
            final Rectangle rect = new Rectangle();
            rect.setWidth(DRAG_RECT_WIDTH);
            rect.setHeight(getHeight() - label.getHeight());
            rect.setFill(Color.TRANSPARENT);
            rect.setVisible(false);
            rect.setSmooth(false);
            rect.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent me) {
                    if (me.getClickCount() == 2 && me.isPrimaryButtonDown()) {
                        // the user wants to resize the column such that its 
                        // width is equal to the widest element in the column
                        resizeToFit(c, -1);
                    } else {
                        // rather than refer to the rect variable, we just grab
                        // it from the source to prevent a small memory leak.
                        Rectangle innerRect = (Rectangle) me.getSource();
                        double startX = getTableHeaderRow().sceneToLocal(innerRect.localToScene(innerRect.getBoundsInLocal())).getMinX() + 2;
                        dragAnchorX = me.getSceneX();
                        columnResizingStarted(startX);
                    }
                    me.consume();
                }
            });
            rect.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent me) {
                    columnResizing(c, me);
                    me.consume();
                }
            });
            rect.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent me) {
                    columnResizingComplete(c, me);
                    me.consume();
                }
            });
            
            EventHandler<MouseEvent> cursorChangeListener = new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent t) {
                    rect.setCursor(rect.isHover() && c.isResizable() ? Cursor.H_RESIZE : Cursor.DEFAULT);
                }
            };
            rect.setOnMouseEntered(cursorChangeListener);
            rect.setOnMouseExited(cursorChangeListener);

            dragRects.add(rect);
        }
    }

    /* *******************/
    /* COLUMN RESIZING   */
    /* *******************/
    private double lastX = 0.0F;

    private double dragAnchorX = 0.0;

    private List<Rectangle> dragRects;

    private void columnResizingStarted(double startX) {
        getTableHeaderRow().getColumnReorderLine().setLayoutX(startX);
    }

    private void columnResizing(TableColumn col, MouseEvent me) {
        double draggedX = me.getSceneX() - dragAnchorX;
        double delta = draggedX - lastX;
        boolean allowed = getTableView().resizeColumn(col, delta);
        if (allowed) {
            lastX = draggedX;
        }
    }

    private void columnResizingComplete(TableColumn col, MouseEvent me) {
//        getTableHeaderRow().getColumnReorderLine().setVisible(true);
        getTableHeaderRow().getColumnReorderLine().setTranslateX(0.0F);
        getTableHeaderRow().getColumnReorderLine().setLayoutX(0.0F);
        lastX = 0.0F;
    }
    

    /* **************************/
    /* END OF COLUMN RESIZING   */
    /* **************************/

    @Override protected void layoutChildren() {
        double w = snapSize(getWidth()) - snapSpace(getInsets().getLeft()) - snapSpace(getInsets().getRight());
        double h = getHeight() - getInsets().getTop() - getInsets().getBottom();
        
        int labelHeight = (int) label.prefHeight(-1);

        if (label.isVisible()) {
            // label gets to span whole width and sits at top
            label.resize(w, labelHeight);
            label.relocate(getInsets().getLeft(), getInsets().getTop());
        }

        // children columns need to share the total available width
        double x = getInsets().getLeft();
        int i = 0;
        for (TableColumnHeaderNoEmptyRow n : getColumnHeaders()) {
            if (! n.isVisible()) continue;
            
            double prefWidth = snapSize(n.prefWidth(-1));
//            double prefHeight = n.prefHeight(-1);

            // position the column header in the default location...
            n.resize(prefWidth, snapSize(h - labelHeight));
            n.relocate(x, labelHeight + getInsets().getTop());

//            // ...but, if there are no children of this column, we should ensure
//            // that it is resized vertically such that it goes to the very
//            // bottom of the table header row.
//            if (getTableHeaderRow() != null && n.getCol().getColumns().isEmpty()) {
//                Bounds bounds = getTableHeaderRow().sceneToLocal(n.localToScene(n.getBoundsInLocal()));
//                prefHeight = getTableHeaderRow().getHeight() - bounds.getMinY();
//                n.resize(prefWidth, prefHeight);
//            }

            // shuffle along the x-axis appropriately
            x += prefWidth;

            // position drag overlay to intercept column resize requests
            if (i < dragRects.size()) {
                Rectangle dragRect = dragRects.get(i++);
                dragRect.setVisible(true);
                dragRect.setHeight(getHeight() - label.getHeight());
                dragRect.relocate(x - DRAG_RECT_WIDTH / 2, getInsets().getTop() + labelHeight);
            }
        }
    }

    // sum up all children columns
    @Override protected double computePrefWidth(double height) {
        double width = 0.0F;

        if (getColumns() != null) {
            for (TableColumnHeaderNoEmptyRow c : getColumnHeaders()) {
                if (c.isVisible()) {
                    width += c.computePrefWidth(height);
                }
            }
        }
        
        return width;
    }

    @Override protected double computePrefHeight(double width) {
        double height = 0.0F;

        if (getColumnHeaders() != null) {
            for (TableColumnHeaderNoEmptyRow n : getColumnHeaders()) {
                height = Math.max(height, n.prefHeight(-1));
            }
        }

        return height + label.prefHeight(-1) + getInsets().getTop() + getInsets().getBottom();
    }

    private TableColumnHeaderNoEmptyRow createColumnHeader(TableColumn col) {
        TableColumnHeaderNoEmptyRow newCol = col.getColumns().isEmpty() ?
            new TableColumnHeaderNoEmptyRow(getTableView(), col) :
            new NestedTableColumnHeaderNoEmptyRow(getTableView(), col);

        newCol.setTableHeaderRow(getTableHeaderRow());
        newCol.setParentHeader(this);

        return newCol;
    }
}
