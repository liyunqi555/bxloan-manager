package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.UserStore;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserStoreDao extends JpaSpecificationExecutor<UserStore>,PagingAndSortingRepository<UserStore,Long> {
    UserStore findByUserIdAndDocInfoId(Long userId,Long docInfoId);
    List<UserStore> findByUserIdAndIdIn(Long userId,Collection<Long> id);

    UserStore findByUserIdAndConceptUriAndEntityId(Long userId,String conceptUri,String entityId);
    List<UserStore> findByUserIdAndDocInfoIdIn(Long userId,Collection<Long> docInfoId);
}
