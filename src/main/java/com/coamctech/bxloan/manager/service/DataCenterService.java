package com.coamctech.bxloan.manager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.domain.UserStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.*;

@Service
public class DataCenterService {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private UserStoreService userStoreService;

    public JsonResult entityList(String conceptUri){

        List<Map<String,Object>> list = this.list("entityid,name,多媒体ID", " from entity_info t where t.concept_uri=?1 ", conceptUri);
        list.forEach(map->{
            map.put("mediaId", map.get("多媒体ID"));
        });
        return JsonResult.success(list);
    }
    public JsonResult myStore(Page page,Long userId){
        List<UserStore> userStores = userStoreService.pageUserStoreData(page, userId);
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        return JsonResult.success(userStores);
    }
    public JsonResult detail(String conceptUri,String entityId){
        String sql = "from entity_property_info t1 ,ont_property t2 " +
                " where t1.entityid=?1 and t2.concept_uri=?2 and t1.property_uri=t2.name";
        List<String> fields = new ArrayList<>();//,t2.name,t1.v_string,t1.v_datetime
        fields.add("t1.entityid");
        fields.add("t2.name");
        fields.add("t2.datatype");
        fields.add("t1.v_boolean");
        fields.add("t1.v_string");
        fields.add("t1.v_long");
        fields.add("t1.v_float");
        fields.add("t1.v_datetime");
        fields.add("t1.v_datetime_start");
        fields.add("t1.v_datetime_end");
        fields.add("t1.v_enum");
        fields.add("t1.v_geo");
        List<Map<String,Object>> data = this.list(fields,sql,entityId,conceptUri);
        return JsonResult.success(getValue(data));
    }
    private Map<String,Object> getValue(List<Map<String,Object>> list){
        Map<String,Object> retData = new HashMap<>();
        if(list==null || list.size()==0){
            return retData;
        }
        list.forEach(m->{
            String key = String.valueOf(m.get("name"));
            int datatype = Integer.valueOf(String.valueOf(m.get("datatype")));
            Object value = "";
            switch (datatype){
                case 1:
                    value=m.get("v_boolean");break;
                case 2:
                    value=m.get("v_string");break;
                case 3:
                    value=m.get("v_long");break;
                case 4:
                    value=m.get("v_float");break;
                case 5:
                    value=m.get("v_datetime");break;
                case 6:
                    value=m.get("v_datetime_start")+"-"+m.get("v_datetime_end");break;
                case 7:
                    value=m.get("v_enum");break;
                case 8:
                    value=m.get("v_geo");break;
                default:
                    value="";
            }
            retData.put(key,value);
        });
        return retData;
    }

    /**
     * 数据类型。0：对象型(仅用于具有子属性的父属性)，1：布尔型，2：字符串型，3：整数型，4：浮点型，5：日期时间型，6：时间区间型，7：枚举型，8：GEO型
     * @param columns
     * @param nativeSql
     * @param params
     * @return
     */
    private List<Map<String,Object>> list(String columns,String nativeSql,String... params){
        List<String> fields = Arrays.asList(columns.split(","));
        return this.list(fields,nativeSql,params);
    }
    private  List<Map<String,Object>> list(List<String> fields,String nativeSql,String... params){

        EntityManager entityManager = null;
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            List<String> keys = new ArrayList<>();
            fields.forEach(field->{
                sql.append(field+", ");
                keys.add(field.substring(field.indexOf(".")));
            });
            sql.append(nativeSql);
            Query query = entityManager.createNativeQuery(sql.toString());
            if(params!=null){
                for(int i=1;i<params.length;i++){
                    query.setParameter(i,params[i]);
                }
            }
            List<Object[]> objecArraytList = query.getResultList();
            objecArraytList.forEach(objectArray->{
                Map<String,Object> m = new HashMap<>();
                for(int i=0;i<keys.size();i++){
                    m.put(keys.get(i), objectArray[i]);
                }
                list.add(m);
            });
        }finally {
            closeEntityManager(entityManager);
        }
        return list;
    }
    private void closeEntityManager(EntityManager entityManager){
        if(entityManager!=null){
            entityManager.close();
        }
    }
}
