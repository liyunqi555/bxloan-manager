package com.coamctech.bxloan.manager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.*;

@Service
public class DataCenterService {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    EntityManagerFactory entityManagerFactory;
    public JsonResult entityList(String conceptUri){

        List<Map<String,Object>> list = this.list("entityid,name,多媒体ID", " from entity_info t where t.concept_uri=?1 ", conceptUri);
        list.forEach(map->{
            map.put("mediaId", map.get("多媒体ID"));
        });
        return JsonResult.success(list);
    }
    public JsonResult detail(String conceptUri,String entityId){
       // List<Map<String,Object>> properInfos = this.list("name,datatype","from entity_property_info t where t.entityid=?1",entityId);
        //List<Map<String,Object>> propers = this.list("name,datatype","from ont_property t where t.concept_uri=?1",conceptUri);
//        String sql = "select  from entity_property_info t1 ,ont_property t2 \n" +
//                "where t1.entityid='黄继光' and t2.concept_uri='人物'and t1.property_uri=t2.name";
        String sql = "from entity_property_info t1 ,ont_property t2 " +
                " where t1.entityid=?1 and t2.concept_uri=?2 and t1.property_uri=t2.name";
        List<String> fields = new ArrayList<>();//,t2.name,t1.v_string,t1.v_datetime
        fields.add("t1.entityid");
        fields.add("t2.name");
        fields.add("t1.v_string");
        fields.add("t1.v_datetime");
        List<Map<String,Object>> data = this.list(fields,sql,entityId,conceptUri);
        return JsonResult.success(data);
    }
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
