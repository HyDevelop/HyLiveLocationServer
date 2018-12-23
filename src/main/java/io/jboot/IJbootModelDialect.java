/*
 * Copyright (c) 2015-2018, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot;

import java.util.List;

public interface IJbootModelDialect
{
    public String forFindByColumns(String table, String loadColumns, List<Column> columns, String orderBy, Object limit);

    public String forPaginateSelect(String loadColumns);

    public String forPaginateFrom(String table, List<Column> columns, String orderBy);
}