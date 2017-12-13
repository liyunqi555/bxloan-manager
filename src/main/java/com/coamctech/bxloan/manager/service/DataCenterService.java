package com.coamctech.bxloan.manager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.coamctech.bxloan.manager.common.JsonResult;
import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.domain.UserStore;
import com.coamctech.bxloan.manager.utils.ChineseToPinYin;
import com.coamctech.bxloan.manager.utils.CommonHelper;
import com.coamctech.bxloan.manager.utils.Encodes;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.stream.FileImageOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
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
            map.put("namePinYin", ChineseToPinYin.getPingYin(String.valueOf(map.get("name"))));
        });
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return String.valueOf(o1.get("namePinYin")).compareTo(String.valueOf(o2.get("namePinYin")));
            }
        });
        return JsonResult.success(list);
    }
    public JsonResult entityList(String conceptUri,String propertyUri){

        List<Map<String,Object>> list = this.list("e.entityid,e.name,e.多媒体ID"," from entity_info e ,  entity_property_info ep " +
                " where e.concept_uri=?1   and e.entityid=ep.entityid and ep.property_uri='所属洲' and ep.v_string=?2  ", conceptUri,propertyUri);
        list.forEach(map->{
            map.put("mediaId", map.get("多媒体ID"));
            map.put("namePinYin", ChineseToPinYin.getPingYin(String.valueOf(map.get("name"))));
        });
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return String.valueOf(o1.get("namePinYin")).compareTo(String.valueOf(o2.get("namePinYin")));
            }
        });
        return JsonResult.success(list);
    }
    public JsonResult myStore(Page page,Long userId){
//        List<UserStore> userStores = userStoreService.pageUserStoreData(page, userId);
        String sql = " from t_user_store ts " +
                " left join entity_info t on t.concept_uri=ts.concept_uri and t.entityid=ts.entity_id " +
                " where ts.user_id=?1 and ts.doc_column_parent_id is null " +
                " order by ts.create_time desc ";
        List<String> fields = new ArrayList<>();//,t2.name,t1.v_string,t1.v_datetime
        fields.add("ts.id");
        fields.add("ts.user_id");
        fields.add("ts.concept_uri");
        fields.add("ts.entity_id");
        fields.add("t.多媒体ID");
        List<Map<String,Object>> list = this.list(fields,sql,userId);
        list.forEach(m->{
            m.put("mediaId", m.get("多媒体ID"));
        });
        return JsonResult.success(list);
    }
    public JsonResult detail(Long userId,String conceptUri,String entityId){
        String sql = "from entity_property_info t1 ,ont_property t2 " +
                " where t1.entityid=?1 and t2.concept_uri=?2 and t1.property_uri=t2.uri";
        List<String> fields = new ArrayList<>();//,t2.name,t1.v_string,t1.v_datetime
        fields.add("t1.entityid");
        fields.add("t2.name");
        fields.add("t2.datatype");
        fields.add("t1.v_bool");
        fields.add("t1.v_string");
        fields.add("t1.v_long");
        fields.add("t1.v_float");
        fields.add("t1.v_datetime");
        fields.add("t1.v_datetime_start");
        fields.add("t1.v_datetime_end");
        fields.add("t1.v_enum");
        fields.add("t1.v_geo");
        fields.add("t2.dataunit");
        List<Map<String,Object>> data = this.list(fields,sql,entityId,conceptUri);
        Map<String,Object> retData = getValue(data);
        UserStore userStore = userStoreService.findByUserIdAndConceptUriAndEntityId(userId, conceptUri, entityId);
        retData.put("storeFlag",userStore==null?0:1);
        return JsonResult.success(retData);
    }
    private Map<String,Object> getValue(List<Map<String,Object>> list){
        Map<String,Object> retData = new HashMap<>();
        if(list==null || list.size()==0){
            return retData;
        }
        list.forEach(m->{
            String key = String.valueOf(m.get("name"));
            logger.info("datatype=",String.valueOf(m.get("datatype")));
            int datatype = Integer.valueOf(String.valueOf(m.get("datatype")));
            Object value = "";
            switch (datatype){
                case 1:
                    value=m.get("v_bool");break;
                case 2:
                    value=m.get("v_string");break;
                case 3:
                    value=m.get("v_long");break;
                case 4:
                    value=m.get("v_float");break;
                case 5:
                    if(m.get("v_datetime")!=null){
                        value = CommonHelper.date2Str((Date) m.get("v_datetime"),CommonHelper.DF_DATE);
                    }
                    break;
                case 6:
                    value=m.get("v_datetime_start")+"-"+m.get("v_datetime_end");break;
                case 7:
                    value=m.get("v_enum");break;
                case 8:
                    value=m.get("v_geo");break;
                default:
                    value="";
            }
            String dataunit = m.get("dataunit")!=null?String.valueOf(m.get("dataunit")):"";
            retData.put(key,value+dataunit);
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
        List<String> fields = Arrays.asList(columns.trim().split(","));
        return this.list(fields,nativeSql,params);
    }
    private  List<Map<String,Object>> list(List<String> fields,String nativeSql,Object... params){

        EntityManager entityManager = null;
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            StringBuilder sql = new StringBuilder();
            sql.append("select ");
            List<String> keys = new ArrayList<>();
            fields.forEach(field->{
                sql.append(field+",");
                if(field.indexOf(".")!=-1){
                    keys.add(field.substring(field.indexOf(".")+1));
                }else{
                    keys.add(field);
                }
            });
            String sqlEnd = sql.substring(0,sql.length()-1)+" "+nativeSql;
            Query query = entityManager.createNativeQuery(sqlEnd);
            if(params!=null){
                for(int i=0;i<params.length;i++){
                    query.setParameter(i+1,params[i]);
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
    public List<byte[]> getMedia(Long mediaId){
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        String sql = "select 文件块内容 from FileBlock t where t.多媒体ID=?1 order by t.文件块位置 asc ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1,mediaId);
        List<byte[]> list = query.getResultList();
        return list;
       /* FileImageOutputStream imageOutput = null;
        try {
            imageOutput = new FileImageOutputStream(new File("f://e.jpg"));
            for(byte[] arr:list){
                imageOutput.write(arr, 0, arr.length);
            }
            imageOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }
    private void closeEntityManager(EntityManager entityManager){
        if(entityManager!=null){
            entityManager.close();
        }
    }

}
