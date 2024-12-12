package com.wipro.jcb.livelink.app.dataprocess.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Slf4j
@Setter
@Getter
@PropertySource("packetconfig.properties")
@Component
public class AppConfiguration {

    @Value("${packet.format}")
    private String packetStr;

    @Value("${eventpacket.msgid}")
    private String eventmsgid;

    @Value("${ignition.msgid}")
    private String ignitionmsgid;

    private JSONObject packetConfigJson = null ;

	public JSONObject getPacketStr() {
		try {
			if (null == packetConfigJson) {
				log.debug("packetConfigJson loading" );
				packetConfigJson = new JSONObject(packetStr);
			}
			log.debug("packetConfigJson getting" );
			return packetConfigJson;

		} catch (final JSONException e) {
			log.error("Error while parsing packetconfig.properties", e);
		}
		return new JSONObject();
	}

    public List<Integer> getEventmsgid() {
		final String[] eventmsgidarray = eventmsgid.split(",");
		final ArrayList<Integer> eventMsgIds = new ArrayList<>();
		for (final String fav : eventmsgidarray) {
			eventMsgIds.add(Integer.parseInt(fav.trim()));
		}
		return eventMsgIds;
	}

    public List<Integer> getIgnitionmsgid() {
		final String[] eventmsgidarray = ignitionmsgid.split(",");
		final ArrayList<Integer> eventMsgIds = new ArrayList<>();
		for (final String fav : eventmsgidarray) {
			eventMsgIds.add(Integer.parseInt(fav.trim()));
		}
		return eventMsgIds;
	}
}
