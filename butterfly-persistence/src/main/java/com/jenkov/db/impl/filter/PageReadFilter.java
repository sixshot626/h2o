/*
    Copyright 2008 Jenkov Development

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/



package com.jenkov.db.impl.filter;

import com.jenkov.db.itf.IReadFilter;
import com.jenkov.db.itf.PersistenceException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implements a paged read filter. This is useful when you need to read
 * for instance record 100 to 120 (or any other number) from a ResultSet.
 * This is often used in web apps when displaying data that cannot fit
 * into a single page. Then you only want to read the data from the
 * ResultSet that corresponds to the page number the user is viewing.
 *
 *
 *
 * @author Jakob Jenkov
 *
 */
public class PageReadFilter implements IReadFilter {

    protected int pageNumber = 0;
    protected int pageSize   = 0;

    protected int rowsAccepted   = 0;

    public PageReadFilter(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize   = pageSize;
    }

    public void init(ResultSet result) throws SQLException, PersistenceException {
        int rowNumber = (pageNumber * pageSize) + 1;
        if(result.getType() == ResultSet.TYPE_FORWARD_ONLY){
            for(int i=0; i<rowNumber; i++){
                result.next();
            }
        } else {
            result.absolute(rowNumber);
        }
    }

    public boolean accept(ResultSet result) throws SQLException, PersistenceException {
        boolean accept = rowsAccepted < pageSize;
        rowsAccepted++;
        return accept;
    }

    public boolean acceptMore() {
        return rowsAccepted < pageSize;
    }

    public void acceptedByAllFilters(boolean wasAcceptedByAllFilters) {
        if(!wasAcceptedByAllFilters){
            rowsAccepted--;
        }
    }

    public void clear() {
    }
}
