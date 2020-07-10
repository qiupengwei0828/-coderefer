package com.microservices.coderefer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microservices.coderefer.po.ReferCode;
import com.microservices.coderefer.service.CodeReferService;

@Controller
@RequestMapping("refer")
public class CodeReferController {

    @Autowired
	CodeReferService  codeReferService;
	/**
	 * 获取全部的ReferCode值
	 * @return
	 */
	@GetMapping("/getAllReferCode")
	@ResponseBody
	public List<ReferCode>  getAllReferCode() {
		return codeReferService.listReferCode();
	}
	/**
	 * 根据码值获取code值
	 * @return
	 */
	@GetMapping("/getReferCodeByCode/{code}")
	public String getReferCodeByCode(@PathVariable String code) {
		return "";
	}
	

}
