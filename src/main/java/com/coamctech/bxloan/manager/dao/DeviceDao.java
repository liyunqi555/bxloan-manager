package com.coamctech.bxloan.manager.dao;


import com.coamctech.bxloan.manager.domain.Device;
import com.coamctech.bxloan.manager.domain.UserCustomDocColumn;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDao extends JpaSpecificationExecutor<Device>,PagingAndSortingRepository<Device,Long> {
    List<Device> findByUserId(Long userId);
    Device findByDeviceCode(String deviceCode);
}
