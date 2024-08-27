package com.wipro.jcb.livelink.app.user.repo;

import com.wipro.jcb.livelink.app.user.entity.MachineGroupEntity;
import com.wipro.jcb.livelink.app.user.reponse.MachineGroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachineGroupRepo extends JpaRepository<MachineGroupEntity, Long> {
    @Query(nativeQuery = true, value = "SELECT distinct(cags.group_id) as GroupId, group_name as GroupName FROM custom_asset_group_snapshot cags, custom_asset_group cag WHERE user_id=:userName and cags.group_id = cag.group_id")
    public List<MachineGroupResponse> getMachineGroupList(@Param("userName") String userName);
}
