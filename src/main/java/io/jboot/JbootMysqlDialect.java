/*
 * Copyright (c) 2015-2018, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot;

import com.jfinal.plugin.activerecord.dialect.MysqlDialect;

import java.util.List;

public class JbootMysqlDialect extends MysqlDialect implements IJbootModelDialect
{
    @Override
    public String forFindByColumns(String table, String loadColumns, List<Column> columns, String orderBy, Object limit)
    {
        StringBuilder sqlBuilder = new StringBuilder("SELECT ");
        sqlBuilder.append(loadColumns)
                .append(" FROM  `")
                .append(table).append("` ");

        appIfNotEmpty(columns, sqlBuilder);

        if (StringUtils.isNotBlank(orderBy))
            sqlBuilder.append(" ORDER BY ").append(orderBy);

        if (limit != null)
            sqlBuilder.append(" LIMIT ").append(limit);

        return sqlBuilder.toString();
    }

    @Override
    public String forPaginateSelect(String loadColumns)
    {
        return "SELECT " + loadColumns;
    }

    @Override
    public String forPaginateFrom(String table, List<Column> columns, String orderBy)
    {
        StringBuilder sqlBuilder = new StringBuilder(" FROM `").append(table).append("`");
        appIfNotEmpty(columns, sqlBuilder);

        if (StringUtils.isNotBlank(orderBy)) sqlBuilder.append(" ORDER BY ").append(orderBy);

        return sqlBuilder.toString();
    }

    private void appIfNotEmpty(List<Column> columns, StringBuilder sqlBuilder)
    {
        if (ArrayUtils.isNotEmpty(columns))
        {
            sqlBuilder.append(" WHERE ");

            int index = 0;
            for (Column column : columns)
            {
                sqlBuilder.append(String.format(" `%s` %s ? ", column.getName(), column.getLogic()));
                if (index != columns.size() - 1)
                    sqlBuilder.append(" AND ");
                index++;
            }
        }
    }
}

