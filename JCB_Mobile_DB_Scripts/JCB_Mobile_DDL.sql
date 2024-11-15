CREATE TABLE `alert` (
  `alert_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `event_description` varchar(255) DEFAULT NULL,
  `event_generated_time` datetime(6) DEFAULT NULL,
  `event_level` enum('RED','YELLOW') DEFAULT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `event_type` enum('Health','Landmark','Security','Service','Utilization') DEFAULT NULL,
  `is_customer_visible` bit(1) DEFAULT NULL,
  `is_dtc_alert` bit(1) DEFAULT NULL,
  `is_generated` bit(1) DEFAULT NULL,
  `is_open` bit(1) DEFAULT NULL,
  `is_updated` bit(1) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `read_flag` bit(1) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`alert_id`),
  UNIQUE KEY `UKp6vp3l9c2dl14bi4t0k5n6utd` (`event_generated_time`,`event_type`,`vin`,`event_name`),
  KEY `FKth26s7medtyymguljq88d626x` (`vin`),
  CONSTRAINT `FKth26s7medtyymguljq88d626x` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `all_users_feedback_data` (
  `users_feedback_data_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `email_sent_on` datetime(6) DEFAULT NULL,
  `feedback` text,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`users_feedback_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=latin1;

CREATE TABLE `customer` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phonenumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customerIndexes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `dashboard_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(1000) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

CREATE TABLE `dealer` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phonenumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dealerIndexes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `dealer_dashboard_customer` (
  `dealer_dashboard_customer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_id` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `machine_count` int(11) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`dealer_dashboard_customer_id`),
  KEY `DealerDashboardCustomer` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `dealer_dashboard_customer_platform` (
  `dealer_dashboard_customer_platform_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_id` varchar(255) DEFAULT NULL,
  `machine_count` int(11) DEFAULT NULL,
  `platform_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dealer_dashboard_customer_platform_id`),
  KEY `DealerDashboardCustomerPlatform` (`customer_id`,`platform_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

CREATE TABLE `dealer_dashboard_data` (
  `dealer_dashboard_data_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `graph_type` varchar(255) DEFAULT NULL,
  `machine_count` int(11) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`dealer_dashboard_data_id`),
  UNIQUE KEY `DealerDashboardData` (`user_id`,`category`,`graph_type`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

CREATE TABLE `live_link_user` (
  `user_id` varchar(64) NOT NULL,
  `activity_completed` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `analytic_last_visited_time` datetime(6) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `feed_date` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_secret_question` bit(1) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `machine_update_notification_enabled` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `service_history_status` varchar(255) DEFAULT NULL,
  `sms_language` varchar(255) DEFAULT NULL,
  `sys_gen_pass` bit(1) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `user_app_version` varchar(255) DEFAULT NULL,
  `user_type` enum('Customer','Dealer','JCB') DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `LiveLinkUser_email` (`email`),
  KEY `LiveLinkUser_userId` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `LiveLinkUser` (
  `USER_ID` varchar(64) NOT NULL,
  `activityCompleted` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `analytic_last_visited_time` datetime(6) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `feed_date` datetime(6) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `isSecretQuestion` bit(1) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `livelinkPersonName` varbinary(255) DEFAULT NULL,
  `machineUpdateNotificationEnabled` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  `serviceHistoryStatus` varchar(255) DEFAULT NULL,
  `smsLanguage` varchar(255) DEFAULT NULL,
  `sysGenPass` int(11) NOT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `timeZone` varchar(255) DEFAULT NULL,
  `userAppVersion` varchar(255) DEFAULT NULL,
  `userType` enum('Customer','Dealer','JCB') DEFAULT NULL,
  `login_failed_count` int(11) DEFAULT NULL,
  `reset_pass_count` int(11) DEFAULT NULL,
  `lockedOutTime` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  KEY `LiveLinkUser_email` (`email`),
  KEY `LiveLinkUser_userId` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `LiveLinkUser_livelinkPersonName` (
  `LiveLinkUser_USER_ID` varchar(64) NOT NULL,
  `livelinkPersonName` varchar(255) DEFAULT NULL,
  KEY `FKjc9nargn8v98vmoda0p6v4fv4` (`LiveLinkUser_USER_ID`),
  CONSTRAINT `FKjc9nargn8v98vmoda0p6v4fv4` FOREIGN KEY (`LiveLinkUser_USER_ID`) REFERENCES `LiveLinkUser` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machin_user` (
  `vin` varchar(255) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  PRIMARY KEY (`vin`,`user_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `FK48dlya68iyn9dahqchqmgqw19` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`),
  CONSTRAINT `FKawp9ls35auhcs35i0rng9svp6` FOREIGN KEY (`user_id`) REFERENCES `live_link_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine` (
  `vin` varchar(255) NOT NULL,
  `air_filter_alert_status` bit(1) DEFAULT NULL,
  `battery_charge_high_status` bit(1) DEFAULT NULL,
  `battery_charge_low_status` bit(1) DEFAULT NULL,
  `battery_charging_status` bit(1) DEFAULT NULL,
  `battery_connected_status` bit(1) DEFAULT NULL,
  `battery_voltage` double DEFAULT NULL,
  `center_lat` double DEFAULT NULL,
  `center_long` double DEFAULT NULL,
  `connectivity` bit(1) DEFAULT NULL,
  `coolant_temperature_alert_status` bit(1) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(255) DEFAULT NULL,
  `customer_address` varchar(255) DEFAULT NULL,
  `customer_email` varchar(255) DEFAULT NULL,
  `customer_first_name` varchar(255) DEFAULT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  `customer_last_name` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_number` varchar(255) DEFAULT NULL,
  `dealer_address` varchar(255) DEFAULT NULL,
  `dealer_email` varchar(255) DEFAULT NULL,
  `dealer_first_name` varchar(255) DEFAULT NULL,
  `dealer_id` varchar(255) DEFAULT NULL,
  `dealer_last_name` varchar(255) DEFAULT NULL,
  `dealer_name` varchar(255) DEFAULT NULL,
  `dealer_number` varchar(255) DEFAULT NULL,
  `end_time` varchar(255) DEFAULT NULL,
  `engine_oil_pressure_alert_status` bit(1) DEFAULT NULL,
  `engine_status` varchar(255) DEFAULT NULL,
  `firmware_version` varchar(255) DEFAULT NULL,
  `fuel_capacity` double DEFAULT NULL,
  `fuel_level` double DEFAULT NULL,
  `fuel_level_status` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `imei_number` varchar(255) DEFAULT NULL,
  `imsi_number` varchar(255) DEFAULT NULL,
  `last_communication_time` datetime(6) DEFAULT NULL,
  `last_modified` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `machine_type` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `operator` varbinary(1000) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `premium_end_date` date DEFAULT NULL,
  `premium_feature` tinyint(1) DEFAULT '0',
  `premium_flag` varchar(255) DEFAULT NULL,
  `premium_start_date` date DEFAULT NULL,
  `radius` bigint(20) DEFAULT NULL,
  `renewal_date` datetime(6) DEFAULT NULL,
  `renewal_flag` tinyint(1) DEFAULT '1',
  `rolloff_date` datetime(6) DEFAULT NULL,
  `service_done_date` datetime(6) DEFAULT NULL,
  `service_done_hours` double DEFAULT NULL,
  `service_due_date` datetime(6) DEFAULT NULL,
  `service_due_hours` double DEFAULT NULL,
  `service_over_due_hours` double DEFAULT NULL,
  `service_type` varchar(255) DEFAULT NULL,
  `site` varchar(255) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  `status_as_on_time` datetime(6) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `total_machine_hours` double DEFAULT NULL,
  `transit_mode` tinyint(4) DEFAULT NULL,
  `zone` varchar(255) DEFAULT NULL,
  `customer_username` varchar(255) DEFAULT NULL,
  `dealer_username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`vin`),
  UNIQUE KEY `UK_ansuxgpr134q6rnj31jpalh72` (`customer_username`),
  UNIQUE KEY `UK_q1k04urpt2i8g0k7b3l47ij4n` (`dealer_username`),
  KEY `vin_platform_indexes` (`vin`,`platform`),
  KEY `machineindexes` (`location`,`platform`,`model`,`tag`),
  FULLTEXT KEY `ft_location` (`location`),
  CONSTRAINT `FKhdjyuukqe3dkax3o2nyougeb8` FOREIGN KEY (`customer_username`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKt55encnbk4wi1i9cwiuf6j18p` FOREIGN KEY (`dealer_username`) REFERENCES `dealer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_address` (
  `vin` varchar(255) NOT NULL,
  `creation_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `location` text,
  `status_as_on_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`vin`),
  KEY `machine_address_indexes` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_bhl_data` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `active_mode_hrs` double DEFAULT NULL,
  `attachment` double DEFAULT NULL,
  `average_fuel_consumption` double DEFAULT NULL,
  `average_speed_in_roading` double DEFAULT NULL,
  `creation_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `distance_travelled_in_roading` double DEFAULT NULL,
  `economy_mode_hrs` double DEFAULT NULL,
  `excavation` double DEFAULT NULL,
  `forward_direction` double DEFAULT NULL,
  `fuel_used_at_excavation_eco_mode` double DEFAULT NULL,
  `fuel_used_at_excavation_eco_mode_perct` double DEFAULT NULL,
  `fuel_used_at_high_idle` double DEFAULT NULL,
  `fuel_used_at_high_idle_perct` double DEFAULT NULL,
  `fuel_used_at_loading_mode` double DEFAULT NULL,
  `fuel_used_at_loading_mode_perct` double DEFAULT NULL,
  `fuel_used_at_roading_mode` double DEFAULT NULL,
  `fuel_used_at_roading_mode_perct` double DEFAULT NULL,
  `fuel_used_in_excavation` double DEFAULT NULL,
  `fuel_used_in_excavation_perct` double DEFAULT NULL,
  `fuel_used_in_idle` double DEFAULT NULL,
  `fuel_used_in_idle_perct` double DEFAULT NULL,
  `fuel_used_in_low_idle` double DEFAULT NULL,
  `fuel_used_in_low_idle_perct` double DEFAULT NULL,
  `gear1utilization` double DEFAULT NULL,
  `gear2utilization` double DEFAULT NULL,
  `gear3utilization` double DEFAULT NULL,
  `gear4utilization` double DEFAULT NULL,
  `idling` double DEFAULT NULL,
  `loading` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `neutral_direction` double DEFAULT NULL,
  `pdrbem1` double DEFAULT NULL,
  `pdrbem2` double DEFAULT NULL,
  `pdrbem3` double DEFAULT NULL,
  `pdrbem4` double DEFAULT NULL,
  `pdrbem5` double DEFAULT NULL,
  `pdrbem6` double DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `power_mode_hrs` double DEFAULT NULL,
  `prb1` double DEFAULT NULL,
  `prb2` double DEFAULT NULL,
  `prb3` double DEFAULT NULL,
  `prb4` double DEFAULT NULL,
  `prb5` double DEFAULT NULL,
  `prb6` double DEFAULT NULL,
  `prbem1` double DEFAULT NULL,
  `prbem2` double DEFAULT NULL,
  `prbem3` double DEFAULT NULL,
  `prbem4` double DEFAULT NULL,
  `prbem5` double DEFAULT NULL,
  `prbem6` double DEFAULT NULL,
  `reverse_direction` double DEFAULT NULL,
  `roading` double DEFAULT NULL,
  `subid_fuel_used_at_excavation_plus_mode` double DEFAULT NULL,
  `subid_fuel_used_at_excavation_plus_mode_perct` double DEFAULT NULL,
  `subid_fuel_used_at_excavation_standard_mode` double DEFAULT NULL,
  `subid_fuel_used_at_excavation_standard_mode_perct` double DEFAULT NULL,
  `total_fuel_used_in_ltrs` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machine_BHL_data_vin_day` (`vin`,`day`),
  CONSTRAINT `FKq3146pj8ba8mdm7o84fiak9r4` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_champions_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `champions_email` varchar(255) DEFAULT NULL,
  `dealer_id` varchar(255) DEFAULT NULL,
  `dealer_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `machine_compactor_data` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `average_fuel_consumption` double DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fuel_used_in_high_power_band_ltrs` double DEFAULT NULL,
  `fuel_used_in_idle_power_band_ltrs` double DEFAULT NULL,
  `fuel_used_in_low_power_band_ltrs` double DEFAULT NULL,
  `fuel_used_in_medium_power_band_ltrs` double DEFAULT NULL,
  `high_vibration_hrs` double DEFAULT NULL,
  `low_vibration_hrs` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `static_pass_hrs` double DEFAULT NULL,
  `total_fuel_used_in_ltrs` double DEFAULT NULL,
  `vibration_off_hrs` double DEFAULT NULL,
  `vibration_on_hrs` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machine_compactor_data_vin_day` (`vin`,`day`),
  CONSTRAINT `FKuv0sexbg0gjhqksbogy8drpb` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_down_question` (
  `identification` varchar(255) NOT NULL,
  `question` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`identification`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_excavator_data` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `auto_idle_evt_count` double DEFAULT NULL,
  `average_fuel_consumption` double DEFAULT NULL,
  `creation_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `engine_on_count` double DEFAULT NULL,
  `fuel_loss` double DEFAULT NULL,
  `fuel_used_inhpbltrs` double DEFAULT NULL,
  `fuel_used_inlpbltrs` double DEFAULT NULL,
  `fuel_used_inmpbltrs` double DEFAULT NULL,
  `hammer_abuse_count` double DEFAULT NULL,
  `hammer_used_time_hrs` double DEFAULT NULL,
  `hot_engine_shut_down_count` double DEFAULT NULL,
  `long_engine_idling_count` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `periodgband_hrs` double DEFAULT NULL,
  `periodhband_hrs` double DEFAULT NULL,
  `periodhplus_band_hrs` double DEFAULT NULL,
  `periodlband_hrs` double DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `power_boost_time` double DEFAULT NULL,
  `slew_hrs` double DEFAULT NULL,
  `total_fuel_used_in_ltrs` double DEFAULT NULL,
  `total_hrs` double DEFAULT NULL,
  `travel_hrs` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machine_excavator_data_vin_day` (`vin`,`day`),
  CONSTRAINT `FKrtnkvs22icij68r7wgv84gm85` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_feature_info` (
  `type` varchar(255) NOT NULL,
  `vin` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `flag` bit(1) NOT NULL,
  PRIMARY KEY (`type`,`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_feedparser_data` (
  `vin` varchar(255) NOT NULL,
  `battery_voltage` double DEFAULT NULL,
  `creation_date` datetime(6) DEFAULT NULL,
  `engine_status` varchar(255) DEFAULT NULL,
  `fuel_level` double DEFAULT NULL,
  `fuel_packet_time` datetime(6) DEFAULT NULL,
  `hmr_packet_time` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `status_as_on_time` datetime(6) DEFAULT NULL,
  `total_machine_hours` double DEFAULT NULL,
  PRIMARY KEY (`vin`),
  KEY `machine_feedparser_indexes` (`vin`,`last_modified_date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_feedparser_location_data` (
  `vin` varchar(255) NOT NULL,
  `creation_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `status_as_on_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`vin`),
  KEY `location_data_indexes_vin` (`vin`,`last_modified_date`),
  KEY `location_data_indexes_statusAsOnTime` (`status_as_on_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_geofence` (
  `vin` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_arrival` varchar(255) DEFAULT NULL,
  `is_depature` varchar(255) DEFAULT NULL,
  `landmark_id` int(11) DEFAULT NULL,
  `landmark_name` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `machine_type` varchar(255) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `push` varchar(255) DEFAULT NULL,
  `radius` double DEFAULT NULL,
  `sms` varchar(255) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_live_location` (
  `id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `day` date DEFAULT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  `hit_count` int(11) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `slot` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `unique_id` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `vin` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `machine_live_location_vin_link` (`vin`,`unique_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_live_location_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_service_history` (
  `job_card_number` varchar(255) NOT NULL,
  `comments` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `service_done` varchar(255) DEFAULT NULL,
  `service_done_at` datetime(6) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_card_number`),
  KEY `machine_service_history_vin_jobCardNumber` (`vin`,`job_card_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_service_schedule` (
  `vin` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `current_cmh` varchar(255) DEFAULT NULL,
  `dealer_name` varchar(255) DEFAULT NULL,
  `due_date` datetime(6) DEFAULT NULL,
  `due_hours` varchar(255) DEFAULT NULL,
  `over_due_date` datetime(6) DEFAULT NULL,
  `over_due_hours` varchar(255) DEFAULT NULL,
  `schedule_name` varchar(255) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_summary` (
  `vin` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fuel_level` double DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `site` varchar(255) DEFAULT NULL,
  `status_as_on_time` datetime(6) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `total_machine_hours` double DEFAULT NULL,
  PRIMARY KEY (`vin`),
  KEY `summaryindexes` (`location`,`platform`,`model`,`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_telehandler_data` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `average_fuel_consumption` double DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fuel_used_in_high_power_band_ltrs` double DEFAULT NULL,
  `fuel_used_in_idle_power_band_ltrs` double DEFAULT NULL,
  `fuel_used_in_low_power_band_ltrs` double DEFAULT NULL,
  `fuel_used_in_medium_power_band_ltrs` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `total_fuel_used_in_ltrs` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machine_telehandler_data_vin_day` (`vin`,`day`),
  CONSTRAINT `FKp6fy1wsbggxab577cuo5xlllm` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machine_wls_data` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `average_fuel_consumption` double DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `cumulative_loaded_weight` double DEFAULT NULL,
  `fuel_loss` double DEFAULT NULL,
  `fuel_used_inhpbltrs` double DEFAULT NULL,
  `fuel_used_inlpbltrs` double DEFAULT NULL,
  `fuel_used_inmpbltrs` double DEFAULT NULL,
  `gear1bkwd_utilization` double DEFAULT NULL,
  `gear1fwd_utilization` double DEFAULT NULL,
  `gear2bkwd_utilization` double DEFAULT NULL,
  `gear2fwd_utilization` double DEFAULT NULL,
  `gear3bkwd_utilization` double DEFAULT NULL,
  `gear3fwd_utilization` double DEFAULT NULL,
  `gear4bkwd_utilization` double DEFAULT NULL,
  `gear4fwd_utilization` double DEFAULT NULL,
  `load_bucket_weight` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `number_of_buckets` double DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `total_fuel_used_in_ltrs` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machine_wls_data_vin_day` (`vin`,`day`),
  CONSTRAINT `FKcw29nslnia4qo361fcogu6p6c` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machineenginestatushistorydata` (
  `date_time` datetime(6) NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_engine_on` bit(1) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`date_time`,`vin_id`),
  KEY `machineenginestatushistorydata_vin_dateTime` (`vin`,`date_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machinefuelconsumption_data` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fuel_consumed` double DEFAULT NULL,
  `fuel_level` varchar(255) DEFAULT NULL,
  `machine_type` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machinefuelconsumptionData_vin_day` (`vin`,`day`),
  CONSTRAINT `FKptglro06ym75o72b5j3gl9hdn` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machinefuelhistorydata` (
  `date_time` datetime(6) NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fuel_level` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`date_time`,`vin_id`),
  KEY `machinefuelhistorydata_vin_dateTime` (`vin`,`date_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machinelocationhistorydata` (
  `date_time` datetime(6) NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`date_time`,`vin_id`),
  KEY `machinelocationhistorydata_vin_dateTime` (`vin`,`date_time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machineperformancedata` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `model` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `power_band_high_in_hours` double DEFAULT NULL,
  `power_band_low_in_hours` double DEFAULT NULL,
  `power_band_medium_in_hours` double DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machineperformancedata_vin_day` (`vin`,`day`),
  CONSTRAINT `FKjlygi4icuwc4v8ddgvwk28d01` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `machineutilizationdata` (
  `day` date NOT NULL,
  `vin_id` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `idle_hours` double DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `off_hours` double DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  `working_hours` double DEFAULT NULL,
  PRIMARY KEY (`day`,`vin_id`),
  UNIQUE KEY `machineutilizationdata_vin_day` (`vin`,`day`),
  KEY `machineutilizationdata_vin_day_workingHour` (`vin`,`day`,`working_hours`),
  CONSTRAINT `FK425aesg2nexef4fxcyf1in4ch` FOREIGN KEY (`vin`) REFERENCES `machine` (`vin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `mobile_app_version` (
  `os` varchar(255) NOT NULL,
  `blocked_version` varchar(255) DEFAULT NULL,
  `recent_version` varchar(255) DEFAULT NULL,
  `current_version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`os`),
  KEY `MobileAppVersionIndexes` (`os`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `notification_details` (
  `id` int(11) NOT NULL,
  `alert_desc` varchar(255) DEFAULT NULL,
  `alert_id` varchar(255) DEFAULT NULL,
  `alert_date_time` datetime(6) DEFAULT NULL,
  `alert_title` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `day` date DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `flag` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `notification_details_seq` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `operater` (
  `operatorid` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `hours` varchar(255) DEFAULT NULL,
  `jcb_certified` bit(1) DEFAULT NULL,
  `operator_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `work_end` datetime(6) DEFAULT NULL,
  `work_start` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`operatorid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `pdf_analytics_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` bigint(20) DEFAULT NULL,
  `day` date DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

CREATE TABLE `premium_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` bigint(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` varchar(255) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `refresh_token_mob` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKykwyg18aesa1wsow3ttmhd2j` (`user_id`),
  CONSTRAINT `FKo8xbi9qh5ntbqcmty7uqpn9h2` FOREIGN KEY (`user_id`) REFERENCES `LiveLinkUser` (`USER_ID`),
  CONSTRAINT `FKykwyg18aesa1wsow3ttmhd2j` FOREIGN KEY (`user_id`) REFERENCES `live_link_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=591 DEFAULT CHARSET=latin1;

CREATE TABLE `service_call_json` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field` varchar(255) DEFAULT NULL,
  `field_name` varchar(255) DEFAULT NULL,
  `label` varchar(1000) DEFAULT NULL,
  `required` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

CREATE TABLE `service_call_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_name` varchar(255) DEFAULT NULL,
  `contract_status` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_alternative_phone` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone` varchar(255) DEFAULT NULL,
  `images` varchar(255) DEFAULT NULL,
  `machine_hmr` varchar(255) DEFAULT NULL,
  `machine_location` varchar(255) DEFAULT NULL,
  `machine_status` varchar(255) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `service_dealer_name` varchar(255) DEFAULT NULL,
  `vin` varchar(255) DEFAULT NULL,
  `warranty_status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

CREATE TABLE `user_livelink_person_name` (
  `user_user_id` varchar(64) NOT NULL,
  `livelink_person_name` varchar(255) DEFAULT NULL,
  KEY `FK7qx3qn0x3pg9qcwxk19g4us9j` (`user_user_id`),
  CONSTRAINT `FK7qx3qn0x3pg9qcwxk19g4us9j` FOREIGN KEY (`user_user_id`) REFERENCES `live_link_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user_notification_detail` (
  `push_notification_token` varchar(255) NOT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `enable_machine_update` bit(1) DEFAULT NULL,
  `enable_notification` bit(1) DEFAULT NULL,
  `os` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  `working_machine_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`push_notification_token`),
  KEY `notificationindex` (`user_name`,`push_notification_token`,`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `utilization_legend_wise_customer_machine_count` (
  `customer_id` varchar(255) NOT NULL,
  `legend` enum('HEAVILY_USED','LESSER_USED','MODERATED_USED','NO_DATA_AVAILABLE') NOT NULL,
  `cust_id` varchar(255) DEFAULT NULL,
  `machine_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`legend`),
  KEY `FKt8fxlw0l0xak00jxoy3m0e312` (`cust_id`),
  CONSTRAINT `FKt8fxlw0l0xak00jxoy3m0e312` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `utilization_legend_wise_machine_count` (
  `legend` enum('HEAVILY_USED','LESSER_USED','MODERATED_USED','NO_DATA_AVAILABLE') NOT NULL,
  `machine_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`legend`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `utilization_platform_wise_customer_machine_count` (
  `customer_id` varchar(255) NOT NULL,
  `platform` varchar(255) NOT NULL,
  `legend` enum('HEAVILY_USED','LESSER_USED','MODERATED_USED','NO_DATA_AVAILABLE') NOT NULL,
  `cust_id` varchar(255) DEFAULT NULL,
  `machine_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`platform`,`legend`),
  KEY `FK86i2p7uwyl3rl5d75u83an5bh` (`cust_id`),
  CONSTRAINT `FK86i2p7uwyl3rl5d75u83an5bh` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `utilization_platform_wise_machine_count` (
  `platform` varchar(255) NOT NULL,
  `legend` enum('HEAVILY_USED','LESSER_USED','MODERATED_USED','NO_DATA_AVAILABLE') NOT NULL,
  `machine_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`platform`,`legend`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `widgets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `machine_type` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;