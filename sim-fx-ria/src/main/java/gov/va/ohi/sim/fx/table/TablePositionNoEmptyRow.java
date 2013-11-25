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

/**
 * This class is used to represent a single row/column/cell in a TableView.
 * This is used throughout the TableView API to represent which rows/columns/cells
 * are currently selected, focused, being edited, etc. Note that this class is
 * immutable once it is created.
 *
 * <p>Because the TableView can have different
 * {@link SelectionMode selection modes}, the row and column properties in
 * TablePosition can be 'disabled' to represent an entire row or column. This is
 * done by setting the unrequired property to -1 or null.
 *
 * @param <S> The type of the items contained within the TableView (i.e. the same
 *      generic type as the S in TableView&lt;S&gt;).
 * @param <T> The type of the items contained within the TableColumnNoEmptyRow.
 * @see TableView
 * @see TableColumnNoEmptyRow
 */
public class TablePositionNoEmptyRow<S,T> {
    
    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/  

    /**
     * Constructs a TablePosition instance to represent the given row/column
     * position in the given TableView instance.
     * 
     * @param tableView The TableView that this position is related to.
     * @param row The row that this TablePosition is representing.
     * @param tableColumn The TableColumnNoEmptyRow instance that this TablePosition represents.
     */
    public TablePositionNoEmptyRow(TableViewNoEmptyRow<S> tableView, int row, TableColumnNoEmptyRow<S,T> tableColumn) {
        this.row = row;
        this.tableColumn = tableColumn;
        this.tableView = tableView;
    }
    
    
    
    /***************************************************************************
     *                                                                         *
     * Instance Variables                                                      *
     *                                                                         *
     **************************************************************************/

    private final int row;
    private final TableColumnNoEmptyRow<S,T> tableColumn;
    private final TableViewNoEmptyRow<S> tableView;



    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/
    
    /**
     * The row that this TablePosition represents in the TableView.
     */
    public final int getRow() {
        return row;
    }
    
    /**
     * The column index that this TablePosition represents in the TableView. It
     * is -1 if the TableView or TableColumnNoEmptyRow instances are null.
     */
    public final int getColumn() {
        return tableView == null || tableColumn == null ? -1 : 
                tableView.getVisibleLeafIndex(tableColumn);
    }
    
    /**
     * The TableView that this TablePosition is related to.
     */
    public final TableViewNoEmptyRow<S> getTableView() {
        return tableView;
    }
    
    /**
     * The TableColumnNoEmptyRow that this TablePosition represents in the TableView.
     */
    public final TableColumnNoEmptyRow<S,T> getTableColumn() {
        return tableColumn;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is equal to the {@code obj} argument; {@code false} otherwise.
     */
    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final TablePositionNoEmptyRow<S,T> other = (TablePositionNoEmptyRow<S,T>) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.tableColumn != other.tableColumn && (this.tableColumn == null || !this.tableColumn.equals(other.tableColumn))) {
            return false;
        }
        if (this.tableView != other.tableView && (this.tableView == null || !this.tableView.equals(other.tableView))) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this {@code TablePosition} object.
     * @return a hash code for this {@code TablePosition} object.
     */ 
    @Override public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.row;
        hash = 79 * hash + (this.tableColumn != null ? this.tableColumn.hashCode() : 0);
        hash = 79 * hash + (this.tableView != null ? this.tableView.hashCode() : 0);
        return hash;
    }

    /**
     * Returns a string representation of this {@code TablePosition} object.
     * @return a string representation of this {@code TablePosition} object.
     */ 
    @Override public String toString() {
        return "TablePosition [ row: " + row + ", column: " + tableColumn + ", "
                + "tableView: " + tableView + " ]";
    }
}
