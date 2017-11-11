package com.coamctech.bxloan.manager.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class QueryEntity {
    private Class queryClass;
    
    private Map<String, List<Attr>> queryMap;
    
    private List<Map<String, String>> orderList;
    
    private List<String> lazyEntity;
    
    /**
     * 创建查询 
     * @param c 要查询的类型
     */
    public QueryEntity(Class c) {
        this.queryClass = c;
        queryMap = new HashMap<String, List<Attr>>();
    }
    
    /**
     * 增加查询条件，“=”
     * @param name 参数名
     * @param value 参数值
     */
    public void eq(String name, Object value) {
        addQuery(name, QueryType.EQ.toString(), value, null);
    }

    /**
     * 增加查询条件，“!=”
     * @param name 参数名
     * @param value 参数值
     */
    public void notEq(String name, Object value) {
        addQuery(name, QueryType.NOTEQ.toString(), value, null);
    }

    /**
     * 增加查询条件，“in”
     * @param name 参数名
     * @param value 参数值（需要集合参数）
     */
    public void in(String name, Object value) {
        addQuery(name, QueryType.IN.toString(), value, null);
    }

    /**
     * 增加查询条件，“not in”
     * @param name 参数名
     * @param value 参数值（需要集合参数）
     */
    public void notIn(String name, Object value) {
        addQuery(name, QueryType.NOTIN.toString(), value, null);
    }

    /**
     * 增加查询条件，“<”
     * @param name 参数名
     * @param value 参数值
     */
    public void lt(String name, Object value) {
        addQuery(name, QueryType.LT.toString(), value, null);
    }

    /**
     * 增加查询条件，“>”
     * @param name 参数名
     * @param value 参数值
     */
    public void gt(String name, Object value) {
        addQuery(name, QueryType.GT.toString(), value, null);
    }

    /**
     * 增加查询条件，“<=”
     * @param name 参数名
     * @param value 参数值
     */
    public void ltEq(String name, Object value) {
        addQuery(name, QueryType.LTEQ.toString(), value, null);
    }

    /**
     * 增加查询条件，“>=”
     * @param name 参数名
     * @param value 参数值
     */
    public void gtEq(String name, Object value) {
        addQuery(name, QueryType.GTEQ.toString(), value, null);
    }

    /**
     * 增加查询条件，“between ... and ... ”
     * @param name 参数名
     * @param value1 参数值1
     * @param value2 参数值2
     */
    public void betweenAnd(String name, Object value1, Object value2) {
        addQuery(name, QueryType.BETWEENAND.toString(), value1, value2);
    }

    /**
     * 增加查询条件，“like '%...%'”，模糊匹配
     * @param name 参数名
     * @param value 参数值
     */
    public void like(String name, Object value) {
        addQuery(name, QueryType.LIKE.toString(), value, null);
    }

    /**
     * 增加查询条件，“like '%...'”，模糊匹配值前半段
     * @param name 参数名
     * @param value 参数值
     */
    public void likeStart(String name, Object value) {
        addQuery(name, QueryType.STARTLIKE.toString(), value, null);
    }

    /**
     * 增加查询条件，“like '...%'”，模糊匹配值后半段
     * @param name 参数名
     * @param value 参数值
     */
    public void likeEnd(String name, Object value) {
        addQuery(name, QueryType.ENDLIKE.toString(), value, null);
    }

    /**
     * 增加查询条件，“$name$ is null”
     * @param name 参数名
     */
    public void isNull(String name) {
        addQuery(name, QueryType.ISNULL.toString(), null, null);
    }

    /**
     * 增加查询条件，“$name$ is not null”
     * @param name 参数名
     */
    public void notNull(String name) {
        addQuery(name, QueryType.NOTNULL.toString(), null, null);
    }

    /**
     * 搜索，分词方法
     * @param name 根据搜索引擎的分词进行查询
     * @param value
     */
    public void analysis(String name, Object value) {
        addQuery(name, QueryType.ANALYSIS.toString(), value, null);
    }
    
    protected void addQuery(String name, String type, Object value1, Object value2){
        Attr attr = new Attr(name, type, value1, value2);
        List<Attr> attrList = queryMap.get(name);
        if (attrList == null) {
            attrList = new ArrayList<>();
        }
        attrList.add(attr);
        queryMap.put(name, attrList);
    }
    
    /**
     * 添加查询结果排序
     * @param name 排序字段
     * @param order 排序类型
     */
    public void addOrderBy(String name, OrderType order) {
        if (orderList == null) {
            orderList = new ArrayList<Map<String, String>>();
        }
        Map<String, String> orderByMap = new HashMap<>();
        orderByMap.put(name, order.toString());
        orderList.add(orderByMap);
    }
    
    /**
     * 添加需要提前查询延迟加载的属性名
     * @param fieldName 
     */
    public void addInitializeEntity(String fieldName) {
        if (lazyEntity == null) {
            lazyEntity = new ArrayList<>();
        }
        lazyEntity.add(fieldName);
    }
    
    public Map<String, List<Attr>> getQueryMap() {
        return queryMap;
    }
    
    public Class getQueryClass() {
        return queryClass;
    }

    public List<Map<String, String>> getOrderList() {
        return orderList;
    }

    public List<String> getLazyEntity() {
        return lazyEntity;
    }

    public class Attr{
        private String name;
        private String type;
        private Object value1;
        private Object value2;
        
        public Attr(String name, String type, Object value1, Object value2){
            this.name = name;
            this.type = type;
            this.value1 = value1;
            this.value2 = value2;
        }
        
        public Attr(String name, String type, Object value1){
            this(name, type, value1, null);
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Object getValue1() {
            return value1;
        }

        public Object getValue2() {
            return value2;
        }
    }
    
    /**
     * 排序类型枚举
     * @author xjt
     *
     */
    public static enum OrderType{
        
        /**
         * 升序
         */
        ASC("asc"),
        /**
         * 降序
         */
        DESC("desc");
        
        private String type;
        
        private OrderType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
        
        @Override
        public String toString() {
            return this.type.toString();
        }
    }
    
    /**
     * 查询类型枚举
     * @author xjt
     *
     */
    public static enum QueryType{
        
        /**
         * =
         */
        EQ("eq"),
        /**
         * !=
         */
        NOTEQ("noteq"),
        /**
         * in()
         */
        IN("in"),
        /**
         * not in()
         */
        NOTIN("notin"),
        /**
         * <
         */
        LT("lt"),
        /**
         * >
         */
        GT("gt"),
        /**
         * <=
         */
        LTEQ("lteq"),
        /**
         * >=
         */
        GTEQ("gteq"),
        /**
         * between ... and ...
         */
        BETWEENAND("betweenand"),
        /**
         * like '%...%'
         */
        LIKE("like"),
        /**
         * like '%...'
         */
        STARTLIKE("startlike"),
        /**
         * like '...%'
         */
        ENDLIKE("endlike"),
        /**
         * is null
         */
        ISNULL("isnull"),
        /**
         * is not null
         */
        NOTNULL("notnull"),
        /**
         * 分词字段
         */
        ANALYSIS("analysis");
        
        private String type;
        
        private QueryType(String type) {
            this.type = type;
        }
        
        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return this.type.toString();
        }
    }
    
    
}
