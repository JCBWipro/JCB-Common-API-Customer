package com.wipro.jcb.livelink.app.machines.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.repo.MobileAppVersionRepo;
import com.wipro.jcb.livelink.app.machines.repo.WidgetRepo;
import com.wipro.jcb.livelink.app.machines.service.response.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:27-09-2024
 * project: JCB-Common-API-Customer
 */
@PropertySource("configurability.properties")
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppConfiguration {
    private  MachineRepository machineRepo;
    private  MobileAppVersionRepo mobileAppVersionRepo;
    private  WidgetRepo widgetRepo;

    @Getter
    @JsonIgnore
    @Value("${help.call}")
    String calls;

    private CallInfo call;
    private List<UserRole> userTypes;
    private List<Language> language;
    private List<Country> country;
    private Long timeout;
    private Long lastSync;
    private Integer pageSize;
    private List<Filter> models;
    private Integer notificationPageSize;
    private BlockedVersion blockedVersion;
    private CurrentVersion currentVersion;
    private RecentVersion recentVersion;
    private WidgetsResponse widgetsResponse;
    private WidgetsResponseData widgetsResponseData;
    private LiveLocationData liveLocationData;

    /*@Autowired
    public AppConfiguration(MachineRepository machineRepo, MobileAppVersionRepo mobileAppVersionRepo, WidgetRepo widgetRepo) {
        this.machineRepo = machineRepo;
        this.mobileAppVersionRepo = mobileAppVersionRepo;
        this.widgetRepo = widgetRepo;
    }

    // Getters with lazy loading and data retrieval logic

    public CallInfo getCall() {
        if (call == null) {
            JSONObject callJson = new JSONObject(getCalls()).getJSONObject("call");
            call = new CallInfo(callJson.getString("service"), callJson.getString("parts"),
                    callJson.getString("sales"), callJson.getString("other"));
        }
        return call;
    }

    @Schema(description = "List of UserType/UserRole", required = true)
    public List<UserRole> getUserTypes() {
        if (userTypes == null) {
            userTypes = new ArrayList<>();
            JSONArray userJTypes = new JSONObject(getCalls()).getJSONArray("userTypes");
            for (int i = 0; i < userJTypes.length(); i++) {
                userTypes.add(new UserRole(userJTypes.getJSONObject(i).getString("name")));
            }
        }
        return userTypes;
    }

    @Schema(description = "List of language", required = true)
    public List<Language> getLanguage() {
        if (language == null) {
            language = new ArrayList<>();
            JSONArray languages = new JSONObject(getCalls()).getJSONArray("language");
            for (int i = 0; i < languages.length(); i++) {
                language.add(new Language(languages.getJSONObject(i).getString("name")));
            }
        }
        return language;
    }

    @Schema(description = "List of country", required = true)
    public List<Country> getCountry() {
        if (country == null) {
            country = new ArrayList<>();
            JSONArray countries = new JSONObject(getCalls()).getJSONArray("country");
            for (int i = 0; i < countries.length(); i++) {
                country.add(new Country(countries.getJSONObject(i).getString("name")));
            }
        }
        return country;
    }

    @Schema(description = "Api timeout", required = true)
    public Long getTimeout() {
        if (timeout == null) {
            timeout = new JSONObject(getCalls()).getLong("timeout");
        }
        return timeout;
    }

    @Schema(description = "when app last sync", required = true)
    public Long getLastSync() {
        if (lastSync == null) {
            lastSync = new JSONObject(getCalls()).getLong("lastSync");
        }
        return lastSync;
    }

    @Schema(description = "default page size", required = true)
    public Integer getPageSize() {
        if (pageSize == null) {
            pageSize = new JSONObject(getCalls()).getInt("pageSize");
        }
        return pageSize;
    }

    @Schema(description = "Models for filter", required = true)
    public List<Filter> getModels() {
        if (models == null) {
            models = machineRepo.findDistinctModel().stream()
                    .map(Filter::new)
                    .collect(Collectors.toList());
        }
        return models;
    }

    @Schema(description = "default page size", required = true)
    public Integer getNotificationPageSize() {
        if (notificationPageSize == null) {
            notificationPageSize = new JSONObject(getCalls()).getInt("notificationPageSize");
        }
        return notificationPageSize;
    }

    public BlockedVersion getBlockedVersion() {
        if (blockedVersion == null) {
            blockedVersion = loadBlockedVersion();
        }
        return blockedVersion;
    }

    public CurrentVersion getCurrentVersion() {
        if (currentVersion == null) {
            currentVersion = loadCurrentVersion();
        }
        return currentVersion;
    }

    public RecentVersion getRecentVersion() {
        if (recentVersion == null) {
            recentVersion = loadRecentVersion();
        }
        return recentVersion;
    }

    public WidgetsResponse getWidgetsResponse() {
        if (widgetsResponse == null) {
            widgetsResponse = loadWidgetsResponse();
        }
        return widgetsResponse;
    }

    public WidgetsResponseData getWidgetsResponseData() {
        if (widgetsResponseData == null) {
            widgetsResponseData = loadWidgetsResponseData();
        }
        return widgetsResponseData;
    }

    public LiveLocationData getLiveLocationData() {
        if (liveLocationData == null) {
            liveLocationData = loadLiveLocationData();
        }
        return liveLocationData;
    }

    // Helper methods to load data

    private BlockedVersion loadBlockedVersion() {
        BlockedVersion blockedVersion = new BlockedVersion();
        List<MobileAppVersion> blockedDetails = mobileAppVersionRepo.getBlockedVersionDetails();
        if (blockedDetails != null && !blockedDetails.isEmpty()) {
            Map<String, String> blocked = blockedDetails.stream()
                    .collect(Collectors.toMap(MobileAppVersion::getOs, MobileAppVersion::getBlockedVersion));
            blockedVersion.setAndroid(blocked.get("android"));
            blockedVersion.setIos(blocked.get("ios"));
        } else {
            JSONObject blockedVersionObj = new JSONObject(getCalls()).getJSONObject("blockedVersion");
            blockedVersion.setAndroid(blockedVersionObj.getString("android"));
            blockedVersion.setIos(blockedVersionObj.getString("ios"));
        }
        return blockedVersion;
    }

    private CurrentVersion loadCurrentVersion() {
        CurrentVersion currentVersion = new CurrentVersion();
        List<MobileAppVersion> currentDetails = mobileAppVersionRepo.getCurrentVersionDetails();
        if (currentDetails != null && !currentDetails.isEmpty()) {
            Map<String, String> current = currentDetails.stream()
                    .collect(Collectors.toMap(MobileAppVersion::getOs, MobileAppVersion::getCurrentVersion));
            currentVersion.setAndroid(current.get("android"));
            currentVersion.setIos(current.get("ios"));
        } else {
            JSONObject currentVersionObj = new JSONObject(getCalls()).getJSONObject("currentVersion");
            currentVersion.setAndroid(currentVersionObj.getString("android"));
            currentVersion.setIos(currentVersionObj.getString("ios"));
        }
        return currentVersion;
    }

    private RecentVersion loadRecentVersion() {
        MobileAppVersion androidVersion = mobileAppVersionRepo.findByOs("android");
        MobileAppVersion iosVersion = mobileAppVersionRepo.findByOs("ios");
        if (androidVersion == null && iosVersion == null) {
            JSONObject versionObj = new JSONObject(getCalls()).getJSONObject("recentVersion");
            androidVersion = new MobileAppVersion("android", versionObj.getString("android"), null, versionObj.getString("android"));
            iosVersion = new MobileAppVersion("ios", versionObj.getString("ios"), null, versionObj.getString("ios"));
        } else if ((androidVersion != null && androidVersion.getRecentVersion() == null) ||
                (iosVersion != null && iosVersion.getRecentVersion() == null)) {
            JSONObject versionObj = new JSONObject(getCalls()).getJSONObject("recentVersion");
            if (androidVersion != null && androidVersion.getRecentVersion() == null) {
                androidVersion.setRecentVersion(versionObj.getString("android"));
            }
            if (iosVersion != null && iosVersion.getRecentVersion() == null) {
                iosVersion.setRecentVersion(versionObj.getString("ios"));
            }
        }
        return new RecentVersion(androidVersion != null ? androidVersion.getRecentVersion() : null,
                iosVersion != null ? iosVersion.getRecentVersion() : null);
    }

    private WidgetsResponse loadWidgetsResponse() {
        WidgetsResponse widgetsResponse = new WidgetsResponse();
        List<Widgets> widgetDetails = widgetRepo.getWidgetDetails();
        if (widgetDetails != null && !widgetDetails.isEmpty()) {
            Map<String, String> widgets = widgetDetails.stream()
                    .collect(Collectors.toMap(Widgets::getMachine_type, Widgets::getValue));
            widgetsResponse.setStandard(widgets.get("Standard"));
            widgetsResponse.setPremium(widgets.get("Premium"));
        }
        return widgetsResponse;
    }

    private WidgetsResponseData loadWidgetsResponseData() {
        WidgetsResponseData widgetsResponseData = new WidgetsResponseData();
        List<Widgets> widget = widgetRepo.getJsonData();
        if (!widget.isEmpty()) {
            String data1 = widget.get(0).getValue().replaceAll("\n", "");
            JSONObject data = new JSONObject(data1).getJSONObject("widgetsResponse");
            widgetsResponseData.setStandardData(parseStandardData(data.getJSONArray("Standard")));
            widgetsResponseData.setPremiumData(parseStandardData(data.getJSONArray("Premium")));
            widgetsResponseData.setLlPlusData(parseStandardData(data.getJSONArray("LLPlus")));
        }
        return widgetsResponseData;
    }

    private LiveLocationData loadLiveLocationData() {
        LiveLocationData liveLocationData = new LiveLocationData();
        List<Widgets> widget = widgetRepo.getLiveLocationJsonData();
        if (!widget.isEmpty()) {
            String data1 = widget.get(0).getValue().replaceAll("\n", "");
            JSONArray datas = new JSONObject(data1).getJSONObject("widgetsResponse").getJSONArray("datas");
            liveLocationData.setResponseData(parseStandardData(datas));
        }
        return liveLocationData;
    }

    private List<StandardData> parseStandardData(JSONArray jsonArray) {
        List<StandardData> standardDataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject responseobj = jsonArray.getJSONObject(i);
            standardDataList.add(new StandardData(
                    responseobj.getString("label"),
                    responseobj.getString("field_name"),
                    responseobj.getBoolean("isActive")
            ));
        }
        return standardDataList;
    }*/
}