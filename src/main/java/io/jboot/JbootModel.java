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

import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.dialect.*;

import java.util.*;

@SuppressWarnings({"serial", "unchecked"})
public class JbootModel<M extends JbootModel<M>> extends Model<M>
{
    public static final String AUTO_COPY_MODEL = "_auto_copy_model_";
    private transient Table table;
    private transient String primaryKey;
    private transient Class<?> primaryType;
    private IJbootModelDialect jbootDialect;

    /**
     * 复制一个新的model
     * 主要是用在 从缓存取出数据的时候，如果直接修改，在ehcache会抛异常
     * 如果要对model进行修改，可以先copy一份新的，然后再修改
     *
     * @return 结果
     */
    public M copy()
    {
        M m = null;
        try
        {
            m = (M) _getUsefulClass().newInstance();
            m.put(_getAttrs());
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * 在 RPC 传输的时候，通过 Controller 传入到Service
     * 不同的序列化方案 可能导致 getModifyFlag 并未设置，可能造成无法保存到数据库
     * 因此需要 通过这个方法 拷贝数据库对于字段，然后再进行更新或保存
     *
     * @return 结果
     */
    public M copyModel()
    {
        M m = null;
        try
        {
            m = (M) _getUsefulClass().newInstance();
            Table table = _getTable(true);
            Set<String> attrKeys = table.getColumnTypeMap().keySet();
            for (String attrKey : attrKeys)
            {
                Object o = this.get(attrKey);
                if (o != null)
                {
                    m.set(attrKey, o);
                }
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return m;
    }

    /**
     * 修复 jfinal use 可能造成的线程安全问题
     *
     * @param configName Config Name
     * @return 结果
     */
    @Override
    public M use(String configName)
    {
        M m = this.get("__ds__" + configName);
        if (m == null)
        {
            m = this.copy().superUse(configName);

            this.put("__ds__" + configName, m);
        }
        return m;
    }

    M superUse(String configName)
    {
        return super.use(configName);
    }

    /**
     * 更新或者保存
     * 有主键就更新，没有就保存
     *
     * @return 结果
     */
    public boolean saveOrUpdate()
    {
        if (null == get(_getPrimaryKey()))
            return this.save();
        return this.update();
    }

    /**
     * 保存数据
     *
     * @return 结果
     */
    @Override
    public boolean save()
    {
        boolean needInitPrimaryKey = (String.class == _getPrimaryType() && null == get(_getPrimaryKey()));

        if (needInitPrimaryKey)
            set(_getPrimaryKey(), generatePrimaryValue());

        boolean saveSuccess;

        Boolean autoCopyModel = get(AUTO_COPY_MODEL);

        if (autoCopyModel != null && autoCopyModel)
        {
            M copyModel = copyModel();
            saveSuccess = copyModel.superSave();

            if (saveSuccess && !needInitPrimaryKey)
                this.set(_getPrimaryKey(), copyModel.get(_getPrimaryKey()));
        }
        else saveSuccess = this.superSave();
        return saveSuccess;
    }

    protected boolean superSave()
    {
        return super.save();
    }

    protected String generatePrimaryValue()
    {
        return StringUtils.uuid();
    }

    /**
     * 更新
     *
     * @return 结果
     */
    @Override
    public boolean update()
    {
        Boolean autoCopyModel = get(AUTO_COPY_MODEL);
        return (autoCopyModel != null && autoCopyModel) ? copyModel().updateNormal() : this.updateNormal();
    }

    boolean updateNormal()
    {
        return super.update();
    }

    private Dialect getDialect()
    {
        return _getConfig().getDialect();
    }

    private IJbootModelDialect getJbootDialect()
    {
        if (jbootDialect != null) return jbootDialect;

        Dialect dialect = getDialect();
        if (dialect instanceof AnsiSqlDialect) return jbootDialect = new JbootAnsiSqlDialect();
        if (dialect instanceof MysqlDialect) return jbootDialect = new JbootMysqlDialect();
        if (dialect instanceof OracleDialect) return jbootDialect = new JbootOracleDialect();
        if (dialect instanceof PostgreSqlDialect) return jbootDialect = new JbootPostgreSqlDialect();
        if (dialect instanceof Sqlite3Dialect) return jbootDialect = new JbootSqlite3Dialect();
        if (dialect instanceof SqlServerDialect) return jbootDialect = new JbootSqlServerDialect();

        throw new RuntimeException("Dialect type not found : " + dialect.getClass());
    }

    /**
     * 根据列名和值，查找1条数据
     *
     * @param column Column
     * @param value  Value
     * @return 结果
     */
    public M findFirstByColumn(String column, Object value)
    {
        String sql = getJbootDialect().forFindByColumns(_getTableName(), "*", Columns.create(column, value).getList(), null, 1);
        return findFirst(sql, value);
    }

    /**
     * 根据 列和值 查询1条数据
     *
     * @param column Column
     * @return 结果
     */
    public M findFirstByColumn(Column column)
    {
        String sql = getJbootDialect().forFindByColumns(_getTableName(), "*", Columns.create(column).getList(), null, 1);
        return findFirst(sql, column.getValue());
    }

    /**
     * 根据 多列和值，查询1条数据
     *
     * @param columns Columns
     * @return 结果
     */
    public M findFirstByColumns(Columns columns)
    {
        String sql = getJbootDialect().forFindByColumns(_getTableName(), "*", columns.getList(), null, 1);
        LinkedList<Object> params = new LinkedList<>();

        if (ArrayUtils.isNotEmpty(columns.getList()))
        {
            for (Column column : columns.getList())
            {
                params.add(column.getValue());
            }
        }
        return findFirst(sql, params.toArray());
    }

    /**
     * 查找全部数据
     *
     * @return 结果
     */
    public List<M> findAll()
    {
        String sql = getJbootDialect().forFindByColumns(_getTableName(), "*", null, null, null);
        return find(sql);
    }

    /**
     * 根据列名和值 查询一个列表
     *
     * @param column Column
     * @param value  Value
     * @param count  最多查询多少条数据
     * @return 结果
     */
    public List<M> findListByColumn(String column, Object value, Integer count)
    {
        List<Column> columns = new ArrayList<>();
        columns.add(Column.create(column, value));
        return findListByColumns(columns, count);
    }

    /**
     * 根据 列信息 查找数据列表
     *
     * @param column Column
     * @param count  Count
     * @return 结果
     */
    public List<M> findListByColumn(Column column, Integer count)
    {
        return findListByColumns(Columns.create(column).getList(), count);
    }

    public List<M> findListByColumn(String column, Object value)
    {
        return findListByColumn(column, value, null);
    }

    public List<M> findListByColumn(Column column)
    {
        return findListByColumn(column, null);
    }

    public List<M> findListByColumns(List<Column> columns)
    {
        return findListByColumns(columns, null, null);
    }

    public List<M> findListByColumns(List<Column> columns, String orderBy)
    {
        return findListByColumns(columns, orderBy, null);
    }

    public List<M> findListByColumns(List<Column> columns, Integer count)
    {
        return findListByColumns(columns, null, count);
    }

    public List<M> findListByColumns(Columns columns)
    {
        return findListByColumns(columns.getList());
    }

    public List<M> findListByColumns(Columns columns, String orderBy)
    {
        return findListByColumns(columns.getList(), orderBy);
    }

    public List<M> findListByColumns(Columns columns, Integer count)
    {
        return findListByColumns(columns.getList(), count);
    }

    public List<M> findListByColumns(Columns columns, String orderBy, Integer count)
    {
        return findListByColumns(columns.getList(), orderBy, count);
    }

    /**
     * 根据列信心查询列表
     *
     * @param columns Columns
     * @param orderBy Order By
     * @param count   Count
     * @return 结果
     */
    public List<M> findListByColumns(List<Column> columns, String orderBy, Integer count)
    {
        LinkedList<Object> params = new LinkedList<>();

        if (ArrayUtils.isNotEmpty(columns))
        {
            for (Column column : columns)
            {
                params.add(column.getValue());
            }
        }

        String sql = getJbootDialect().forFindByColumns(_getTableName(), "*", columns, orderBy, count);
        return params.isEmpty() ? find(sql) : find(sql, params.toArray());
    }

    /**
     * 分页查询数据
     *
     * @param pageNumber Page Number
     * @param pageSize   Page Size
     * @return 结果
     */
    public Page<M> paginate(int pageNumber, int pageSize)
    {
        return paginateByColumns(pageNumber, pageSize, null, null);
    }

    /**
     * 分页查询数据
     *
     * @param pageNumber Page Number
     * @param pageSize   Page Size
     * @return 结果
     */
    public Page<M> paginate(int pageNumber, int pageSize, String orderBy)
    {
        return paginateByColumns(pageNumber, pageSize, null, orderBy);
    }

    /**
     * 根据某列信息，分页查询数据
     *
     * @param pageNumber Page Number
     * @param pageSize   Page Size
     * @param column     Column
     * @return 结果
     */
    public Page<M> paginateByColumn(int pageNumber, int pageSize, Column column)
    {
        return paginateByColumns(pageNumber, pageSize, Columns.create(column).getList(), null);
    }

    /**
     * 根据某列信息，分页查询数据
     *
     * @param pageNumber Page Number
     * @param pageSize   Page Size
     * @param column     Column
     * @return 结果
     */
    public Page<M> paginateByColumn(int pageNumber, int pageSize, Column column, String orderBy)
    {
        return paginateByColumns(pageNumber, pageSize, Columns.create(column).getList(), orderBy);
    }

    /**
     * 根据列信息，分页查询数据
     *
     * @param pageNumber Page Number
     * @param pageSize   Page Size
     * @param columns    Columns
     * @return 结果
     */
    public Page<M> paginateByColumns(int pageNumber, int pageSize, List<Column> columns)
    {
        return paginateByColumns(pageNumber, pageSize, columns, null);
    }

    /**
     * 根据列信息，分页查询数据
     *
     * @param pageNumber Page Number
     * @param pageSize   Page Size
     * @param columns    Columns
     * @param orderBy    Order By
     * @return 结果
     */
    public Page<M> paginateByColumns(int pageNumber, int pageSize, List<Column> columns, String orderBy)
    {
        String selectPartSql = getJbootDialect().forPaginateSelect("*");
        String fromPartSql = getJbootDialect().forPaginateFrom(_getTableName(), columns, orderBy);

        LinkedList<Object> params = new LinkedList<>();

        if (ArrayUtils.isNotEmpty(columns))
        {
            for (Column column : columns)
            {
                params.add(column.getValue());
            }
        }
        return params.isEmpty() ? paginate(pageNumber, pageSize, selectPartSql, fromPartSql)
                : paginate(pageNumber, pageSize, selectPartSql, fromPartSql, params.toArray());
    }

    protected String _getTableName()
    {
        return _getTable(true).getName();
    }

    protected Table _getTable()
    {
        return _getTable(false);
    }

    protected Table _getTable(boolean validateNull)
    {
        if (table == null)
        {
            table = super._getTable();
            if (table == null && validateNull)
            {
                throw new RuntimeException(String.format("class %s can not mapping to database table, maybe cannot connection to database or not use correct datasource, " +
                        "please check jboot.properties or correct config @Table(datasource=xxx) if you use multi datasource.", _getUsefulClass().getName()));
            }
        }
        return table;
    }

    protected String _getPrimaryKey()
    {
        if (primaryKey != null) return primaryKey;

        String[] primaryKeys = _getTable(true).getPrimaryKey();

        if (null != primaryKeys && primaryKeys.length == 1) primaryKey = primaryKeys[0];

        JbootAssert.assertTrue(primaryKey != null, String.format("get PrimaryKey is error in[%s]", getClass()));

        return primaryKey;
    }

    protected Class<?> _getPrimaryType()
    {
        if (primaryType == null)
            primaryType = _getTable(true).getColumnType(_getPrimaryKey());
        return primaryType;
    }

    protected boolean hasColumn(String columnLabel)
    {
        return _getTable(true).hasColumnLabel(columnLabel);
    }

    @Override
    public Page<M> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect)
    {
        return super.paginate(pageNumber, pageSize, select, sqlExceptSelect);
    }

    @Override
    public Page<M> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras)
    {
        return super.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    @Override
    public Page<M> paginate(int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras)
    {
        return super.paginate(pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    @Override
    public List<M> find(String sql, Object... paras)
    {
        debugPrintParas(paras);
        return super.find(sql, paras);
    }

    @Override
    public M findFirst(String sql, Object... paras)
    {
        debugPrintParas(paras);
        return super.findFirst(sql, paras);
    }

    @Override
    public List<M> findByCache(String cacheName, Object key, String sql, Object... paras)
    {
        return super.findByCache(cacheName, key, sql, paras);
    }

    @Override
    public M findFirstByCache(String cacheName, Object key, String sql, Object... paras)
    {
        return super.findFirstByCache(cacheName, key, sql, paras);
    }

    @Override
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras)
    {
        return super.paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    @Override
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, boolean isGroupBySql, String select, String sqlExceptSelect, Object... paras)
    {
        return super.paginateByCache(cacheName, key, pageNumber, pageSize, isGroupBySql, select, sqlExceptSelect, paras);
    }

    @Override
    public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect)
    {
        return super.paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect);
    }

    private void debugPrintParas(Object... objects)
    {
        if (JFinal.me().getConstants().getDevMode())
            System.out.println("\r\n---------------Paras: " + Arrays.toString(objects) + "----------------");
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (!(o instanceof JbootModel)) return false;

        Object id = ((JbootModel) o).get(_getPrimaryKey());

        if (id == null) return false;
        return id.equals(get(_getPrimaryKey()));
    }

}
