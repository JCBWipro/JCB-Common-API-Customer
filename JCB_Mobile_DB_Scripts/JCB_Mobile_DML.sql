INSERT INTO `microservices_db`.`alert`
(`alert_id`,
`created_at`,
`created_by`,
`event_description`,
`event_generated_time`,
`event_level`,
`event_name`,
`event_type`,
`is_customer_visible`,
`is_dtc_alert`,
`is_generated`,
`is_open`,
`is_updated`,
`latitude`,
`location`,
`longitude`,
`read_flag`,
`updated_at`,
`vin`)
VALUES
(<{alert_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{created_by: }>,
<{event_description: }>,
<{event_generated_time: }>,
<{event_level: }>,
<{event_name: }>,
<{event_type: }>,
<{is_customer_visible: }>,
<{is_dtc_alert: }>,
<{is_generated: }>,
<{is_open: }>,
<{is_updated: }>,
<{latitude: }>,
<{location: }>,
<{longitude: }>,
<{read_flag: }>,
<{updated_at: CURRENT_TIMESTAMP}>,
<{vin: }>);

INSERT INTO `microservices_db`.`all_users_feedback_data`
(`users_feedback_data_id`,
`created_at`,
`email_sent_on`,
`feedback`,
`user_name`)
VALUES
(<{users_feedback_data_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{email_sent_on: }>,
<{feedback: }>,
<{user_name: }>);

INSERT INTO `microservices_db`.`customer`
(`id`,
`address`,
`country`,
`created_at`,
`first_name`,
`last_name`,
`name`,
`phonenumber`)
VALUES
(<{id: }>,
<{address: }>,
<{country: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{first_name: }>,
<{last_name: }>,
<{name: }>,
<{phonenumber: }>);

INSERT INTO `microservices_db`.`dashboard_data`
(`id`,
`data`,
`type`)
VALUES
(<{id: }>,
<{data: }>,
<{type: }>);

INSERT INTO `microservices_db`.`dealer`
(`id`,
`address`,
`country`,
`created_at`,
`first_name`,
`last_name`,
`name`,
`phonenumber`)
VALUES
(<{id: }>,
<{address: }>,
<{country: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{first_name: }>,
<{last_name: }>,
<{name: }>,
<{phonenumber: }>);

INSERT INTO `microservices_db`.`dealer_dashboard_customer`
(`dealer_dashboard_customer_id`,
`created_at`,
`customer_id`,
`customer_name`,
`machine_count`,
`user_id`)
VALUES
(<{dealer_dashboard_customer_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{customer_id: }>,
<{customer_name: }>,
<{machine_count: }>,
<{user_id: }>);

INSERT INTO `microservices_db`.`dealer_dashboard_customer_platform`
(`dealer_dashboard_customer_platform_id`,
`created_at`,
`customer_id`,
`machine_count`,
`platform_name`)
VALUES
(<{dealer_dashboard_customer_platform_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{customer_id: }>,
<{machine_count: }>,
<{platform_name: }>);

INSERT INTO `microservices_db`.`dealer_dashboard_data`
(`dealer_dashboard_data_id`,
`category`,
`created_at`,
`graph_type`,
`machine_count`,
`user_id`)
VALUES
(<{dealer_dashboard_data_id: }>,
<{category: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{graph_type: }>,
<{machine_count: }>,
<{user_id: }>);

INSERT INTO `microservices_db`.`live_link_user`
(`user_id`,
`activity_completed`,
`address`,
`analytic_last_visited_time`,
`country`,
`created_at`,
`email`,
`feed_date`,
`first_name`,
`image`,
`is_secret_question`,
`language`,
`last_name`,
`machine_update_notification_enabled`,
`password`,
`phone_number`,
`role_name`,
`service_history_status`,
`sms_language`,
`sys_gen_pass`,
`thumbnail`,
`time_zone`,
`user_app_version`,
`user_type`)
VALUES
(<{user_id: }>,
<{activity_completed: }>,
<{address: }>,
<{analytic_last_visited_time: }>,
<{country: }>,
<{created_at: }>,
<{email: }>,
<{feed_date: }>,
<{first_name: }>,
<{image: }>,
<{is_secret_question: }>,
<{language: }>,
<{last_name: }>,
<{machine_update_notification_enabled: }>,
<{password: }>,
<{phone_number: }>,
<{role_name: }>,
<{service_history_status: }>,
<{sms_language: }>,
<{sys_gen_pass: }>,
<{thumbnail: }>,
<{time_zone: }>,
<{user_app_version: }>,
<{user_type: }>);

INSERT INTO `microservices_db`.`LiveLinkUser`
(`USER_ID`,
`activityCompleted`,
`address`,
`analytic_last_visited_time`,
`country`,
`createdAt`,
`email`,
`feed_date`,
`firstName`,
`image`,
`isSecretQuestion`,
`language`,
`lastName`,
`livelinkPersonName`,
`machineUpdateNotificationEnabled`,
`password`,
`phoneNumber`,
`roleName`,
`serviceHistoryStatus`,
`smsLanguage`,
`sysGenPass`,
`thumbnail`,
`timeZone`,
`userAppVersion`,
`userType`,
`login_failed_count`,
`reset_pass_count`,
`lockedOutTime`)
VALUES
(<{USER_ID: }>,
<{activityCompleted: }>,
<{address: }>,
<{analytic_last_visited_time: }>,
<{country: }>,
<{createdAt: }>,
<{email: }>,
<{feed_date: }>,
<{firstName: }>,
<{image: }>,
<{isSecretQuestion: }>,
<{language: }>,
<{lastName: }>,
<{livelinkPersonName: }>,
<{machineUpdateNotificationEnabled: }>,
<{password: }>,
<{phoneNumber: }>,
<{roleName: }>,
<{serviceHistoryStatus: }>,
<{smsLanguage: }>,
<{sysGenPass: }>,
<{thumbnail: }>,
<{timeZone: }>,
<{userAppVersion: }>,
<{userType: }>,
<{login_failed_count: }>,
<{reset_pass_count: }>,
<{lockedOutTime: }>);

INSERT INTO `microservices_db`.`LiveLinkUser_livelinkPersonName`
(`LiveLinkUser_USER_ID`,
`livelinkPersonName`)
VALUES
(<{LiveLinkUser_USER_ID: }>,
<{livelinkPersonName: }>);

INSERT INTO `microservices_db`.`machin_user`
(`vin`,
`user_id`)
VALUES
(<{vin: }>,
<{user_id: }>);

INSERT INTO `microservices_db`.`machine`
(`vin`,
`air_filter_alert_status`,
`battery_charge_high_status`,
`battery_charge_low_status`,
`battery_charging_status`,
`battery_connected_status`,
`battery_voltage`,
`center_lat`,
`center_long`,
`connectivity`,
`coolant_temperature_alert_status`,
`created_at`,
`created_by`,
`customer_address`,
`customer_email`,
`customer_first_name`,
`customer_id`,
`customer_last_name`,
`customer_name`,
`customer_number`,
`dealer_address`,
`dealer_email`,
`dealer_first_name`,
`dealer_id`,
`dealer_last_name`,
`dealer_name`,
`dealer_number`,
`end_time`,
`engine_oil_pressure_alert_status`,
`engine_status`,
`firmware_version`,
`fuel_capacity`,
`fuel_level`,
`fuel_level_status`,
`image`,
`imei_number`,
`imsi_number`,
`last_communication_time`,
`last_modified`,
`latitude`,
`location`,
`longitude`,
`machine_type`,
`model`,
`operator`,
`platform`,
`premium_end_date`,
`premium_feature`,
`premium_flag`,
`premium_start_date`,
`radius`,
`renewal_date`,
`renewal_flag`,
`rolloff_date`,
`service_done_date`,
`service_done_hours`,
`service_due_date`,
`service_due_hours`,
`service_over_due_hours`,
`service_type`,
`site`,
`start_time`,
`status_as_on_time`,
`tag`,
`thumbnail`,
`total_machine_hours`,
`transit_mode`,
`zone`,
`customer_username`,
`dealer_username`)
VALUES
(<{vin: }>,
<{air_filter_alert_status: }>,
<{battery_charge_high_status: }>,
<{battery_charge_low_status: }>,
<{battery_charging_status: }>,
<{battery_connected_status: }>,
<{battery_voltage: }>,
<{center_lat: }>,
<{center_long: }>,
<{connectivity: }>,
<{coolant_temperature_alert_status: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{created_by: }>,
<{customer_address: }>,
<{customer_email: }>,
<{customer_first_name: }>,
<{customer_id: }>,
<{customer_last_name: }>,
<{customer_name: }>,
<{customer_number: }>,
<{dealer_address: }>,
<{dealer_email: }>,
<{dealer_first_name: }>,
<{dealer_id: }>,
<{dealer_last_name: }>,
<{dealer_name: }>,
<{dealer_number: }>,
<{end_time: }>,
<{engine_oil_pressure_alert_status: }>,
<{engine_status: }>,
<{firmware_version: }>,
<{fuel_capacity: }>,
<{fuel_level: }>,
<{fuel_level_status: }>,
<{image: }>,
<{imei_number: }>,
<{imsi_number: }>,
<{last_communication_time: }>,
<{last_modified: }>,
<{latitude: }>,
<{location: }>,
<{longitude: }>,
<{machine_type: }>,
<{model: }>,
<{operator: }>,
<{platform: }>,
<{premium_end_date: }>,
<{premium_feature: 0}>,
<{premium_flag: }>,
<{premium_start_date: }>,
<{radius: }>,
<{renewal_date: }>,
<{renewal_flag: 1}>,
<{rolloff_date: }>,
<{service_done_date: }>,
<{service_done_hours: }>,
<{service_due_date: }>,
<{service_due_hours: }>,
<{service_over_due_hours: }>,
<{service_type: }>,
<{site: }>,
<{start_time: }>,
<{status_as_on_time: }>,
<{tag: }>,
<{thumbnail: }>,
<{total_machine_hours: }>,
<{transit_mode: }>,
<{zone: }>,
<{customer_username: }>,
<{dealer_username: }>);

INSERT INTO `microservices_db`.`machine_address`
(`vin`,
`creation_date`,
`last_modified_date`,
`location`,
`status_as_on_time`)
VALUES
(<{vin: }>,
<{creation_date: }>,
<{last_modified_date: }>,
<{location: }>,
<{status_as_on_time: }>);

INSERT INTO `microservices_db`.`machine_bhl_data`
(`day`,
`vin_id`,
`active_mode_hrs`,
`attachment`,
`average_fuel_consumption`,
`average_speed_in_roading`,
`creation_at`,
`distance_travelled_in_roading`,
`economy_mode_hrs`,
`excavation`,
`forward_direction`,
`fuel_used_at_excavation_eco_mode`,
`fuel_used_at_excavation_eco_mode_perct`,
`fuel_used_at_high_idle`,
`fuel_used_at_high_idle_perct`,
`fuel_used_at_loading_mode`,
`fuel_used_at_loading_mode_perct`,
`fuel_used_at_roading_mode`,
`fuel_used_at_roading_mode_perct`,
`fuel_used_in_excavation`,
`fuel_used_in_excavation_perct`,
`fuel_used_in_idle`,
`fuel_used_in_idle_perct`,
`fuel_used_in_low_idle`,
`fuel_used_in_low_idle_perct`,
`gear1utilization`,
`gear2utilization`,
`gear3utilization`,
`gear4utilization`,
`idling`,
`loading`,
`model`,
`neutral_direction`,
`pdrbem1`,
`pdrbem2`,
`pdrbem3`,
`pdrbem4`,
`pdrbem5`,
`pdrbem6`,
`platform`,
`power_mode_hrs`,
`prb1`,
`prb2`,
`prb3`,
`prb4`,
`prb5`,
`prb6`,
`prbem1`,
`prbem2`,
`prbem3`,
`prbem4`,
`prbem5`,
`prbem6`,
`reverse_direction`,
`roading`,
`subid_fuel_used_at_excavation_plus_mode`,
`subid_fuel_used_at_excavation_plus_mode_perct`,
`subid_fuel_used_at_excavation_standard_mode`,
`subid_fuel_used_at_excavation_standard_mode_perct`,
`total_fuel_used_in_ltrs`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{active_mode_hrs: }>,
<{attachment: }>,
<{average_fuel_consumption: }>,
<{average_speed_in_roading: }>,
<{creation_at: CURRENT_TIMESTAMP}>,
<{distance_travelled_in_roading: }>,
<{economy_mode_hrs: }>,
<{excavation: }>,
<{forward_direction: }>,
<{fuel_used_at_excavation_eco_mode: }>,
<{fuel_used_at_excavation_eco_mode_perct: }>,
<{fuel_used_at_high_idle: }>,
<{fuel_used_at_high_idle_perct: }>,
<{fuel_used_at_loading_mode: }>,
<{fuel_used_at_loading_mode_perct: }>,
<{fuel_used_at_roading_mode: }>,
<{fuel_used_at_roading_mode_perct: }>,
<{fuel_used_in_excavation: }>,
<{fuel_used_in_excavation_perct: }>,
<{fuel_used_in_idle: }>,
<{fuel_used_in_idle_perct: }>,
<{fuel_used_in_low_idle: }>,
<{fuel_used_in_low_idle_perct: }>,
<{gear1utilization: }>,
<{gear2utilization: }>,
<{gear3utilization: }>,
<{gear4utilization: }>,
<{idling: }>,
<{loading: }>,
<{model: }>,
<{neutral_direction: }>,
<{pdrbem1: }>,
<{pdrbem2: }>,
<{pdrbem3: }>,
<{pdrbem4: }>,
<{pdrbem5: }>,
<{pdrbem6: }>,
<{platform: }>,
<{power_mode_hrs: }>,
<{prb1: }>,
<{prb2: }>,
<{prb3: }>,
<{prb4: }>,
<{prb5: }>,
<{prb6: }>,
<{prbem1: }>,
<{prbem2: }>,
<{prbem3: }>,
<{prbem4: }>,
<{prbem5: }>,
<{prbem6: }>,
<{reverse_direction: }>,
<{roading: }>,
<{subid_fuel_used_at_excavation_plus_mode: }>,
<{subid_fuel_used_at_excavation_plus_mode_perct: }>,
<{subid_fuel_used_at_excavation_standard_mode: }>,
<{subid_fuel_used_at_excavation_standard_mode_perct: }>,
<{total_fuel_used_in_ltrs: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machine_champions_details`
(`id`,
`champions_email`,
`dealer_id`,
`dealer_name`)
VALUES
(<{id: }>,
<{champions_email: }>,
<{dealer_id: }>,
<{dealer_name: }>);

INSERT INTO `microservices_db`.`machine_compactor_data`
(`day`,
`vin_id`,
`average_fuel_consumption`,
`created_at`,
`fuel_used_in_high_power_band_ltrs`,
`fuel_used_in_idle_power_band_ltrs`,
`fuel_used_in_low_power_band_ltrs`,
`fuel_used_in_medium_power_band_ltrs`,
`high_vibration_hrs`,
`low_vibration_hrs`,
`model`,
`platform`,
`static_pass_hrs`,
`total_fuel_used_in_ltrs`,
`vibration_off_hrs`,
`vibration_on_hrs`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{average_fuel_consumption: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{fuel_used_in_high_power_band_ltrs: }>,
<{fuel_used_in_idle_power_band_ltrs: }>,
<{fuel_used_in_low_power_band_ltrs: }>,
<{fuel_used_in_medium_power_band_ltrs: }>,
<{high_vibration_hrs: }>,
<{low_vibration_hrs: }>,
<{model: }>,
<{platform: }>,
<{static_pass_hrs: }>,
<{total_fuel_used_in_ltrs: }>,
<{vibration_off_hrs: }>,
<{vibration_on_hrs: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machine_down_question`
(`identification`,
`question`)
VALUES
(<{identification: }>,
<{question: }>);

INSERT INTO `microservices_db`.`machine_excavator_data`
(`day`,
`vin_id`,
`auto_idle_evt_count`,
`average_fuel_consumption`,
`creation_at`,
`engine_on_count`,
`fuel_loss`,
`fuel_used_inhpbltrs`,
`fuel_used_inlpbltrs`,
`fuel_used_inmpbltrs`,
`hammer_abuse_count`,
`hammer_used_time_hrs`,
`hot_engine_shut_down_count`,
`long_engine_idling_count`,
`model`,
`periodgband_hrs`,
`periodhband_hrs`,
`periodhplus_band_hrs`,
`periodlband_hrs`,
`platform`,
`power_boost_time`,
`slew_hrs`,
`total_fuel_used_in_ltrs`,
`total_hrs`,
`travel_hrs`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{auto_idle_evt_count: }>,
<{average_fuel_consumption: }>,
<{creation_at: CURRENT_TIMESTAMP}>,
<{engine_on_count: }>,
<{fuel_loss: }>,
<{fuel_used_inhpbltrs: }>,
<{fuel_used_inlpbltrs: }>,
<{fuel_used_inmpbltrs: }>,
<{hammer_abuse_count: }>,
<{hammer_used_time_hrs: }>,
<{hot_engine_shut_down_count: }>,
<{long_engine_idling_count: }>,
<{model: }>,
<{periodgband_hrs: }>,
<{periodhband_hrs: }>,
<{periodhplus_band_hrs: }>,
<{periodlband_hrs: }>,
<{platform: }>,
<{power_boost_time: }>,
<{slew_hrs: }>,
<{total_fuel_used_in_ltrs: }>,
<{total_hrs: }>,
<{travel_hrs: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machine_feature_info`
(`type`,
`vin`,
`created_at`,
`flag`)
VALUES
(<{type: }>,
<{vin: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{flag: }>);

INSERT INTO `microservices_db`.`machine_feedparser_data`
(`vin`,
`battery_voltage`,
`creation_date`,
`engine_status`,
`fuel_level`,
`fuel_packet_time`,
`hmr_packet_time`,
`last_modified_date`,
`status_as_on_time`,
`total_machine_hours`)
VALUES
(<{vin: }>,
<{battery_voltage: }>,
<{creation_date: }>,
<{engine_status: }>,
<{fuel_level: }>,
<{fuel_packet_time: }>,
<{hmr_packet_time: }>,
<{last_modified_date: }>,
<{status_as_on_time: }>,
<{total_machine_hours: }>);

INSERT INTO `microservices_db`.`machine_feedparser_location_data`
(`vin`,
`creation_date`,
`last_modified_date`,
`latitude`,
`longitude`,
`status_as_on_time`)
VALUES
(<{vin: }>,
<{creation_date: }>,
<{last_modified_date: }>,
<{latitude: }>,
<{longitude: }>,
<{status_as_on_time: }>);

INSERT INTO `microservices_db`.`machine_geofence`
(`vin`,
`address`,
`created_at`,
`is_arrival`,
`is_depature`,
`landmark_id`,
`landmark_name`,
`latitude`,
`longitude`,
`machine_type`,
`mobile_number`,
`push`,
`radius`,
`sms`,
`updated_at`)
VALUES
(<{vin: }>,
<{address: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{is_arrival: }>,
<{is_depature: }>,
<{landmark_id: }>,
<{landmark_name: }>,
<{latitude: }>,
<{longitude: }>,
<{machine_type: }>,
<{mobile_number: }>,
<{push: }>,
<{radius: }>,
<{sms: }>,
<{updated_at: CURRENT_TIMESTAMP}>);

INSERT INTO `microservices_db`.`machine_live_location`
(`id`,
`created_at`,
`day`,
`expiry_time`,
`hit_count`,
`link`,
`slot`,
`status`,
`unique_id`,
`updated_at`,
`user_id`,
`vin`)
VALUES
(<{id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{day: }>,
<{expiry_time: }>,
<{hit_count: }>,
<{link: }>,
<{slot: }>,
<{status: }>,
<{unique_id: }>,
<{updated_at: }>,
<{user_id: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machine_live_location_seq`
(`next_val`)
VALUES
(<{next_val: }>);

INSERT INTO `microservices_db`.`machine_service_history`
(`job_card_number`,
`comments`,
`created_at`,
`service_done`,
`service_done_at`,
`vin`)
VALUES
(<{job_card_number: }>,
<{comments: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{service_done: }>,
<{service_done_at: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machine_service_schedule`
(`vin`,
`created_at`,
`current_cmh`,
`dealer_name`,
`due_date`,
`due_hours`,
`over_due_date`,
`over_due_hours`,
`schedule_name`,
`service_name`,
`status`)
VALUES
(<{vin: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{current_cmh: }>,
<{dealer_name: }>,
<{due_date: }>,
<{due_hours: }>,
<{over_due_date: }>,
<{over_due_hours: }>,
<{schedule_name: }>,
<{service_name: }>,
<{status: }>);

INSERT INTO `microservices_db`.`machine_summary`
(`vin`,
`created_at`,
`fuel_level`,
`image`,
`latitude`,
`location`,
`longitude`,
`model`,
`platform`,
`site`,
`status_as_on_time`,
`tag`,
`thumbnail`,
`total_machine_hours`)
VALUES
(<{vin: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{fuel_level: }>,
<{image: }>,
<{latitude: }>,
<{location: }>,
<{longitude: }>,
<{model: }>,
<{platform: }>,
<{site: }>,
<{status_as_on_time: }>,
<{tag: }>,
<{thumbnail: }>,
<{total_machine_hours: }>);

INSERT INTO `microservices_db`.`machine_telehandler_data`
(`day`,
`vin_id`,
`average_fuel_consumption`,
`created_at`,
`fuel_used_in_high_power_band_ltrs`,
`fuel_used_in_idle_power_band_ltrs`,
`fuel_used_in_low_power_band_ltrs`,
`fuel_used_in_medium_power_band_ltrs`,
`model`,
`platform`,
`total_fuel_used_in_ltrs`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{average_fuel_consumption: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{fuel_used_in_high_power_band_ltrs: }>,
<{fuel_used_in_idle_power_band_ltrs: }>,
<{fuel_used_in_low_power_band_ltrs: }>,
<{fuel_used_in_medium_power_band_ltrs: }>,
<{model: }>,
<{platform: }>,
<{total_fuel_used_in_ltrs: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machine_wls_data`
(`day`,
`vin_id`,
`average_fuel_consumption`,
`created_at`,
`cumulative_loaded_weight`,
`fuel_loss`,
`fuel_used_inhpbltrs`,
`fuel_used_inlpbltrs`,
`fuel_used_inmpbltrs`,
`gear1bkwd_utilization`,
`gear1fwd_utilization`,
`gear2bkwd_utilization`,
`gear2fwd_utilization`,
`gear3bkwd_utilization`,
`gear3fwd_utilization`,
`gear4bkwd_utilization`,
`gear4fwd_utilization`,
`load_bucket_weight`,
`model`,
`number_of_buckets`,
`platform`,
`total_fuel_used_in_ltrs`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{average_fuel_consumption: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{cumulative_loaded_weight: }>,
<{fuel_loss: }>,
<{fuel_used_inhpbltrs: }>,
<{fuel_used_inlpbltrs: }>,
<{fuel_used_inmpbltrs: }>,
<{gear1bkwd_utilization: }>,
<{gear1fwd_utilization: }>,
<{gear2bkwd_utilization: }>,
<{gear2fwd_utilization: }>,
<{gear3bkwd_utilization: }>,
<{gear3fwd_utilization: }>,
<{gear4bkwd_utilization: }>,
<{gear4fwd_utilization: }>,
<{load_bucket_weight: }>,
<{model: }>,
<{number_of_buckets: }>,
<{platform: }>,
<{total_fuel_used_in_ltrs: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machineenginestatushistorydata`
(`date_time`,
`vin_id`,
`created_at`,
`is_engine_on`,
`vin`)
VALUES
(<{date_time: }>,
<{vin_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{is_engine_on: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machinefuelconsumption_data`
(`day`,
`vin_id`,
`created_at`,
`fuel_consumed`,
`fuel_level`,
`machine_type`,
`model`,
`platform`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{fuel_consumed: }>,
<{fuel_level: }>,
<{machine_type: }>,
<{model: }>,
<{platform: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machinefuelhistorydata`
(`date_time`,
`vin_id`,
`created_at`,
`fuel_level`,
`vin`)
VALUES
(<{date_time: }>,
<{vin_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{fuel_level: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machinelocationhistorydata`
(`date_time`,
`vin_id`,
`created_at`,
`latitude`,
`longitude`,
`vin`)
VALUES
(<{date_time: }>,
<{vin_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{latitude: }>,
<{longitude: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machineperformancedata`
(`day`,
`vin_id`,
`created_at`,
`model`,
`platform`,
`power_band_high_in_hours`,
`power_band_low_in_hours`,
`power_band_medium_in_hours`,
`vin`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{model: }>,
<{platform: }>,
<{power_band_high_in_hours: }>,
<{power_band_low_in_hours: }>,
<{power_band_medium_in_hours: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`machineutilizationdata`
(`day`,
`vin_id`,
`created_at`,
`idle_hours`,
`model`,
`off_hours`,
`platform`,
`vin`,
`working_hours`)
VALUES
(<{day: }>,
<{vin_id: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{idle_hours: }>,
<{model: }>,
<{off_hours: }>,
<{platform: }>,
<{vin: }>,
<{working_hours: }>);

INSERT INTO `microservices_db`.`mobile_app_version`
(`os`,
`blocked_version`,
`recent_version`,
`current_version`)
VALUES
(<{os: }>,
<{blocked_version: }>,
<{recent_version: }>,
<{current_version: }>);

INSERT INTO `microservices_db`.`notification_details`
(`id`,
`alert_desc`,
`alert_id`,
`alert_date_time`,
`alert_title`,
`created_at`,
`day`,
`deleted_at`,
`flag`,
`type`,
`updated_at`,
`user_id`,
`vin`)
VALUES
(<{id: }>,
<{alert_desc: }>,
<{alert_id: }>,
<{alert_date_time: }>,
<{alert_title: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{day: }>,
<{deleted_at: }>,
<{flag: }>,
<{type: }>,
<{updated_at: }>,
<{user_id: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`notification_details_seq`
(`next_val`)
VALUES
(<{next_val: }>);

INSERT INTO `microservices_db`.`operater`
(`operatorid`,
`created_at`,
`hours`,
`jcb_certified`,
`operator_name`,
`phone_number`,
`work_end`,
`work_start`)
VALUES
(<{operatorid: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{hours: }>,
<{jcb_certified: }>,
<{operator_name: }>,
<{phone_number: }>,
<{work_end: }>,
<{work_start: }>);

INSERT INTO `microservices_db`.`pdf_analytics_data`
(`id`,
`count`,
`day`,
`vin`)
VALUES
(<{id: }>,
<{count: }>,
<{day: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`premium_request`
(`id`,
`count`,
`created_at`,
`user_id`,
`vin`)
VALUES
(<{id: }>,
<{count: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{user_id: }>,
<{vin: }>);

INSERT INTO `microservices_db`.`refresh_token_mob`
(`id`,
`expiry_date`,
`token`,
`user_id`)
VALUES
(<{id: }>,
<{expiry_date: }>,
<{token: }>,
<{user_id: }>);

INSERT INTO `microservices_db`.`service_call_json`
(`id`,
`field`,
`field_name`,
`label`,
`required`,
`type`,
`value`)
VALUES
(<{id: }>,
<{field: }>,
<{field_name: }>,
<{label: }>,
<{required: }>,
<{type: }>,
<{value: }>);

INSERT INTO `microservices_db`.`service_call_request`
(`id`,
`contact_name`,
`contract_status`,
`created_at`,
`customer_alternative_phone`,
`customer_name`,
`customer_phone`,
`images`,
`machine_hmr`,
`machine_location`,
`machine_status`,
`model`,
`remarks`,
`service_dealer_name`,
`vin`,
`warranty_status`)
VALUES
(<{id: }>,
<{contact_name: }>,
<{contract_status: }>,
<{created_at: CURRENT_TIMESTAMP}>,
<{customer_alternative_phone: }>,
<{customer_name: }>,
<{customer_phone: }>,
<{images: }>,
<{machine_hmr: }>,
<{machine_location: }>,
<{machine_status: }>,
<{model: }>,
<{remarks: }>,
<{service_dealer_name: }>,
<{vin: }>,
<{warranty_status: }>);

INSERT INTO `microservices_db`.`user_livelink_person_name`
(`user_user_id`,
`livelink_person_name`)
VALUES
(<{user_user_id: }>,
<{livelink_person_name: }>);

INSERT INTO `microservices_db`.`user_notification_detail`
(`push_notification_token`,
`access_token`,
`created_at`,
`enable_machine_update`,
`enable_notification`,
`os`,
`user_name`,
`user_type`,
`working_machine_count`)
VALUES
(<{push_notification_token: }>,
<{access_token: }>,
<{created_at: }>,
<{enable_machine_update: }>,
<{enable_notification: }>,
<{os: }>,
<{user_name: }>,
<{user_type: }>,
<{working_machine_count: }>);

INSERT INTO `microservices_db`.`utilization_legend_wise_customer_machine_count`
(`customer_id`,
`legend`,
`cust_id`,
`machine_count`)
VALUES
(<{customer_id: }>,
<{legend: }>,
<{cust_id: }>,
<{machine_count: }>);

INSERT INTO `microservices_db`.`utilization_legend_wise_machine_count`
(`legend`,
`machine_count`)
VALUES
(<{legend: }>,
<{machine_count: }>);

INSERT INTO `microservices_db`.`utilization_platform_wise_customer_machine_count`
(`customer_id`,
`platform`,
`legend`,
`cust_id`,
`machine_count`)
VALUES
(<{customer_id: }>,
<{platform: }>,
<{legend: }>,
<{cust_id: }>,
<{machine_count: }>);

INSERT INTO `microservices_db`.`utilization_platform_wise_machine_count`
(`platform`,
`legend`,
`machine_count`)
VALUES
(<{platform: }>,
<{legend: }>,
<{machine_count: }>);

INSERT INTO `microservices_db`.`widgets`
(`id`,
`machine_type`,
`value`)
VALUES
(<{id: }>,
<{machine_type: }>,
<{value: }>);