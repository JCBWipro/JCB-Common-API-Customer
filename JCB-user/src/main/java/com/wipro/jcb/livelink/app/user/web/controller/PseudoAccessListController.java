package com.wipro.jcb.livelink.app.user.web.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.jcb.livelink.app.user.web.service.PseudoAccessListService;

@RestController
@RequestMapping("/api/user")
public class PseudoAccessListController {

	@Autowired
	PseudoAccessListService pseudoAccessListService;
	
	/*
		This End Point is used to fetch MAF Customers
	*/
	@GetMapping("/PseudoAccessListService/PseudoTenancyList")
	public List<HashMap<String, String>> getPseudoTenancyList() {
		 List<HashMap<String, String>> pseudoTenancyList = pseudoAccessListService.getPseudoTenancyList();
		return pseudoTenancyList;
	}
}
