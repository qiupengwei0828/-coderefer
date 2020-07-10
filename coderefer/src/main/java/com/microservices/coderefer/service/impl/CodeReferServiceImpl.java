package com.microservices.coderefer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.coderefer.mapper.ReferCodeMapper;
import com.microservices.coderefer.po.ReferCode;
import com.microservices.coderefer.service.CodeReferService;
@Service
public class CodeReferServiceImpl implements CodeReferService{

	@Autowired
	ReferCodeMapper referCodeMapper;
	@Override
	public List<ReferCode> listReferCode() {
		return referCodeMapper.listReferCode();
	}

}
