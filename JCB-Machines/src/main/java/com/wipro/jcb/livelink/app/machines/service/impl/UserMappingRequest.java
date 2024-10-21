package com.wipro.jcb.livelink.app.machines.service.impl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


import com.wipro.jcb.livelink.app.machines.constants.AppServerConstants;
import com.wipro.jcb.livelink.app.machines.entity.Machine;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.repo.UserRepository;
import com.wipro.jcb.livelink.app.machines.service.response.UserVinList;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.core.MultivaluedMap;

@Component
public class UserMappingRequest {
    @Value("${livelinkserver.connectTimeout}")
    int connectTimeout;
    @Value("${livelinkserver.readTimeout}")
    int readTimeout;
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UserMappingRequest.class);

    public void userMapping(String userName) throws ProcessCustomError {
        logger.info("userMapping API for {}", userName);
        long start = System.currentTimeMillis();
        boolean isMappingSaved = false;
        int unauthorisedAttemptCount = 0;
        try {
            do {
                final MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
                headers.add("OrgKey", AppServerConstants.livelinkAppServerOrgKey);
                headers.add("Content-Type", "application/json");
                headers.add("Accept", "application/json");
                headers.add("TokenId", "37aa1b15_20240522150705");

                final Client client = Client.create();
                client.setConnectTimeout(connectTimeout);
                client.setReadTimeout(readTimeout);
                logger.info("VIN Mapping API {}-{}/userVinDetailsServiceV2-{}", userName, AppServerConstants.livelinkAppServerBaseUrl, headers);
                final WebResource webResource = client
                        .resource(AppServerConstants.livelinkAppServerBaseUrl + "/userVinDetailsServiceV2")
                        .queryParams(headers);
                final ClientResponse out = webResource.accept("application/json")
                        .header("OrgKey", AppServerConstants.livelinkAppServerOrgKey).header("TokenId", "37aa1b15_20240522150705")
                        .type("application/json").get(ClientResponse.class);
                logger.info("VIN Mapping Response {}-{}", userName, out);
                if (out.getStatus() == HttpServletResponse.SC_OK) {
                    unauthorisedAttemptCount = 0;
                    String responseentity = out.getEntity(String.class);
                    final ObjectMapper mapper = new ObjectMapper();
                    final UserVinList userVinList1 = mapper.readValue(responseentity, UserVinList.class);
                    long end = System.currentTimeMillis();
                    long elapsedTime = end - start;
                    logger.info("UserVinDetailsService Duration:{}-{}-{}", userName, userVinList1.getVin().size(), elapsedTime);
                    if (userVinList1.getVin() == null || userVinList1.getVin().isEmpty()) {
                        logger.info("UserVinDetailsService Empty response for user{} size{} ", userName, userVinList1.getVin().size());
                    }
                    saveUserMapping(userVinList1.getVin(), userName);
                    responseentity = "";
                    userVinList1.getVin().clear();
                    isMappingSaved = true;

                } else {
                    if (unauthorisedAttemptCount < 3 && out.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
                        unauthorisedAttemptCount++;
                        final User user = userRepository.findByUserName(userName);
                        if (user != null) {
                            //livelinkToken = utilities.updateLivelinkServerToken(user, true);
                        }
                    } else {
                        logger.error("Exception while processing userVinDetailsService API with status code {}", out.getStatus());
                        throw new ProcessCustomError("Unable to process request", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            } while (!isMappingSaved && unauthorisedAttemptCount < 3);
        } catch (final ProcessCustomError exception) {
            logger.info("Exception for VIN MApping API :{}-{}", userName, exception.getMessage());
            logger.error("ProcessCustomError while processing userVinDetailsDervice API {} ", userName, exception);
            throw exception;
        } catch (final IOException e) {
            logger.error("IOException while processing userVinDetailsService API {} ", userName, e);
        } catch (final JSONException e) {
            logger.error("Json parsing Exception while processing userVinDetailsService API {}", userName, e);
        } catch (final Exception e) {
            logger.error("Exception while processing userVinDetailsService API {}", userName, e);
        }
    }

    private void saveUserMapping(final List<String> vinList, final String userName) {
        final User usr = userRepository.findByUserName(userName);
        logger.info("User Details {}", usr.getUserName());
        final List<String> allMachinesVinListWithUser = machineRepository.findVinByUsersUserName(userName);
        logger.info("VIN List Size :{}-{}-{}", usr.getUserName(), vinList.size(), allMachinesVinListWithUser.size());
        final List<String> vinListToSave = new LinkedList<>(vinList);

        logger.info("VIN List Size :{}-{}-{}", usr.getUserName(), vinList.size(), allMachinesVinListWithUser.size());
        final List<String> nonexistvin = new LinkedList<>();
        final List<Machine> machineList = new LinkedList<>();
        Collections.sort(vinListToSave);
        Collections.sort(allMachinesVinListWithUser);
        // addition of user mapping
        if (!vinListToSave.isEmpty()) {
            logger.info("saveUserMapping API for list size {} - {} - {}", userName, vinListToSave.size(), allMachinesVinListWithUser.size());
            if (!vinListToSave.equals(allMachinesVinListWithUser)) {
                vinListToSave.removeAll(allMachinesVinListWithUser);
                logger.info("removeAll :{}-{}", vinListToSave.size(), allMachinesVinListWithUser.size());
                for (final String vin : vinListToSave) {
                    if (null != vin && !machineRepository.existsById(vin.trim())) {
                        nonexistvin.add(vin);
                    }
                }
                logger.info("nonexistvin API for User {} - VIN {}-{}", userName, vinListToSave.size(), nonexistvin.size());
                vinListToSave.removeAll(nonexistvin);
                logger.info("Aftermoving  VINList : {} - {}", userName, vinListToSave.size());
                if (!vinListToSave.isEmpty() && !vinListToSave.contains(null)) {
                    logger.info("vinListToSaveCmp {} - {}", userName, vinListToSave.size());
                    alluserMapping(vinListToSave, userName);
                } else {
                    logger.info("Wipro return null values {}", vinListToSave.size());
                }
                if (!machineList.isEmpty()) {
                    machineRepository.saveAll(machineList);
                }
                reallocatedMachines(allMachinesVinListWithUser, vinList, usr);
            } else {
                logger.info("user mapping is same");
            }
        } else {
            logger.info("VIN List is empty for {} - {}", userName, vinListToSave.size());

        }
    }

    private void reallocatedMachines(final List<String> allMachinesVinListWithUser, final List<String> vinList,
                                     final User usr) {
        try {
            if (!allMachinesVinListWithUser.isEmpty()) {
                allMachinesVinListWithUser.removeAll(vinList);
                logger.info(" after removal {} vin list {}", allMachinesVinListWithUser.size(), vinList.size());
                if (!allMachinesVinListWithUser.isEmpty()) {
                    final List<Machine> machineListToRemove = new LinkedList<>();
                    for (final String vin : allMachinesVinListWithUser) {
                        machineRepository.deleteUserMapping(vin, usr.getUserName());
                    }
                    if (!machineListToRemove.isEmpty()) {
                        machineRepository.saveAll(machineListToRemove);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while machine reallocate :" + usr.getUserName());
            e.printStackTrace();
        }
    }

    public void adduserMapping(final String userName) {
        logger.info(" adduserMapping started :{}", userName);
        final long userMachinesCount = machineRepository.countByUsersUserName(userName);
        if (userMachinesCount == 0) {
            logger.info(" mapAllMachineTouser :{}", userName);
            machineRepository.mapAllMachineTouser(userName);
            logger.info("mapAllMachineTouser end :{}", userName);

        } else {
            logger.info(" else allMachinesVinListWithUser :{}", userName);
            machineRepository.mapMachineToUser(userName);
            logger.info(" else allMachinesVinListWithUser end :{}", userName);
        }
        logger.info(" findVinByUsersUserName end :{}", userName);
    }

    public void alluserMapping(List<String> packets, String userName) {
        try {
            jdbcTemplate.batchUpdate("insert into  machin_user(vin,user_id)values (?,?)on conflict do nothing",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            String p = packets.get(i);
                            ps.setString(1, p);
                            ps.setString(2, userName);
                        }

                        @Override
                        public int getBatchSize() {
                            return packets.size();
                        }
                    });
        } catch (final Exception ex) {
            logger.error("Failed to update updateBatchMachineWithoutFuel with message -{} - {}", userName, ex);
            ex.printStackTrace();
        }
    }

}
