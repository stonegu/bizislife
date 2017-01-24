package org.bizislife.resource.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/test2")
public class Test2Controller {
	
	@RequestMapping(method = RequestMethod.GET, value = "/get")
	public @ResponseBody
	ResponseEntity<Map<String, String>> getTest() {
		Map<String, String> results = new HashMap<>();
		results.put("projectname", "resource-main");
		results.put("version", "1.0");
		results.put("class", this.getClass().getSimpleName());
		
		return new ResponseEntity<Map<String,String>>(results, HttpStatus.OK);
	}

	@PreAuthorize("#oauth2.hasScope('foo') and #oauth2.hasScope('read')")
	@RequestMapping(method = RequestMethod.GET, value = "/get2")
	public @ResponseBody
	ResponseEntity<Map<String, String>> getTest2() {
		Map<String, String> results = new HashMap<>();
		results.put("projectname", "resource-main");
		results.put("version", "1.0");
		results.put("class", this.getClass().getSimpleName());
		
		return new ResponseEntity<Map<String,String>>(results, HttpStatus.OK);
	}

}