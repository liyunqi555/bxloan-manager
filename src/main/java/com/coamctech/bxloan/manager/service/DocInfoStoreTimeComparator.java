package com.coamctech.bxloan.manager.service;

import com.coamctech.bxloan.manager.domain.DocInfo;

import java.util.Comparator;

/**
 * 浏览历史时间比较器
 * Created by Administrator on 2017/11/25.
 */
public class DocInfoStoreTimeComparator implements Comparator<DocInfo>{

    @Override
    public int compare(DocInfo o1, DocInfo o2) {
        if(o1==null || o2==null){
            return 0;
        }
        if(o1.getStoreTime()==null || o2.getStoreTime()==null){
            return 0;
        }
        return o1.getStoreTime().getTime()>o2.getStoreTime().getTime()?1:-1;
    }
}
