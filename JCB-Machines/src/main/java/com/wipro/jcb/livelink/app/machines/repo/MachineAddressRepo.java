package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */

public interface MachineAddressRepo extends CrudRepository<MachineAddress, String> {
    //@Override
    @Async
    public <S extends MachineAddress> Iterable<S> save(Iterable<S> entities);

    @Override
    public <S extends MachineAddress> S save(S entity);


}
