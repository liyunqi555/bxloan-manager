package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.common.Page;
import com.coamctech.bxloan.manager.common.PageList;
import com.coamctech.bxloan.manager.domain.User;
import com.coamctech.bxloan.manager.utils.StringUtils;
import com.coamctech.bxloan.manager.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BaseService<T, ID extends Serializable> {
    private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${top.level.column.id.news}")
    public Long topLevelColumnIdNews;
    @Value("${top.level.column.id.doc}")
    public Long topLevelColumnIdDoc;
    @Value("${top.level.column.id.report}")
    public Long topLevelColumnIdReport;
    @Autowired
    private PagingAndSortingRepository<T, ID> pagingAndSortingRepository;
    @Autowired
    EntityManagerFactory entityManagerFactory;
    T save(T var1) {
        return pagingAndSortingRepository.save(var1);
    }

    <S extends T> Iterable<T> save(Iterable<T> var1) {
        return pagingAndSortingRepository.save(var1);
    }

    T findOne(ID var1) {
        return pagingAndSortingRepository.findOne(var1);
    }

    boolean exists(ID var1) {
        return pagingAndSortingRepository.exists(var1);
    }

    Iterable<T> findAll() {
        return pagingAndSortingRepository.findAll();
    }

    Iterable<T> findAll(Iterable<ID> var1) {
        return pagingAndSortingRepository.findAll(var1);
    }

    long count() {
        return pagingAndSortingRepository.count();
    }

    void delete(ID var1) {
        pagingAndSortingRepository.delete(var1);
    }

    void delete(T var1) {
        pagingAndSortingRepository.delete(var1);
    }

    void delete(Iterable<? extends T> var1) {
        pagingAndSortingRepository.delete(var1);
    }

    //    Page<T> findAll(Pageable var1) {
//        return pagingAndSortingRepository.findAll(var1);
    public PageList pageList(Page page, String sql, Map<String, Object> param) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        PageList pageList = new PageList(page);
        try {

            page.setTotalCount(count(sql,param));
            page.initPage(page.getTotalCount());
            Query query = entityManager.createQuery(sql);
            if (null != param && param.size() > 0) {
                for (String key : param.keySet()) {
                    query.setParameter(key, param.get(key));
                }
            }
            query.setFirstResult(page.getStart());
            query.setMaxResults(page.getLimit());
            List result = query.getResultList();
            pageList.setList(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return pageList;
    }

    void deleteAll() {
        pagingAndSortingRepository.deleteAll();
    }

    Iterable<T> findAll(Sort var1) {
        return pagingAndSortingRepository.findAll(var1);
    }

    //    }
    Long count(String sql,Map<String, Object> param) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        String countSql = sql;
        int orderIndex = countSql.indexOf("order");
        if(orderIndex != -1){
            countSql = countSql.substring(0,orderIndex);
        }
        countSql = "select count(1) " + countSql;
        Query query = entityManager.createQuery(countSql);
        try {
            if (null != param && param.size() > 0) {
                for (String key : param.keySet()) {
                    query.setParameter(key, param.get(key));
                }
            }
            return (Long) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return null;
    }
}
