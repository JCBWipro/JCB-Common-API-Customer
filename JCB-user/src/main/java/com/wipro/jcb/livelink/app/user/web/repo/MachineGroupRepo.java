package com.wipro.jcb.livelink.app.user.web.repo;

import com.wipro.jcb.livelink.app.user.web.entity.MachineGroupEntity;
import com.wipro.jcb.livelink.app.user.web.reponse.MachineGroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MachineGroupRepo extends JpaRepository<MachineGroupEntity, Long> {
    /**
     *   Retrieves the group_id and group_name associated with a given  user_id.
     *   This query is executed as a native SQL query against the "wise.custom_asset_group_snapshot" table.
     */
    @Query(nativeQuery = true, value = "SELECT distinct(cags.group_id) as GroupId, group_name as GroupName FROM custom_asset_group_snapshot cags, custom_asset_group cag WHERE user_id=:userName and cags.group_id = cag.group_id")
    public List<MachineGroupResponse> getMachineGroupList(@Param("userName") String userName);
}
