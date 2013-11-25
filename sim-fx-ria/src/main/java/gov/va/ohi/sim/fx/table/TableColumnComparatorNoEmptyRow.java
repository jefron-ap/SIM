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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TableColumnComparatorNoEmptyRow implements Comparator<Object> {

    private final ObservableList<TableColumnNoEmptyRow<?,?>> columns;

    public TableColumnComparatorNoEmptyRow() {
        this.columns = FXCollections.observableArrayList();
    }

    public ObservableList<TableColumnNoEmptyRow<?,?>> getColumns() {
        return columns;
    }

    @Override
    public int compare(Object o1, Object o2) {
        for (TableColumnNoEmptyRow tc : columns) {
            Comparator c = tc.getComparator();

            Object value1 = tc.getCellData(o1);
            Object value2 = tc.getCellData(o2);

            int result = 0;
            switch (tc.getSortType()) {
                case ASCENDING: result = c.compare(value1, value2); break;
                case DESCENDING: result = c.compare(value2, value1); break;    
            }
            
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
