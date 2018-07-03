package com.example.xapiservice.app.service.impl;

import com.example.xapiservice.app.entity.App;
import com.example.xapiservice.app.mapper.AppMapper;
import com.example.xapiservice.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppServiceImpl implements AppService {

	@Autowired
	private AppMapper<App> appMapper;

	@Override
	public boolean insert(App app) {
		try {
			appMapper.insert(app);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(App app) {
		try {
			appMapper.update(app);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateBySelective(App app) {
		try {
			appMapper.updateBySelective(app);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteById(Object id) {
		try {
			appMapper.deleteById(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public App getById(Object id){
		return appMapper.getById(id);
	}

	@Override
	public List<App> findBySelective(App app) {
		return appMapper.findBySelective(app);
	}

	@Override
	public List<App> findPageList(App app) {
		return appMapper.findPageList(app);
	}

	@Override
	public int findPageCount(App app) {
		return appMapper.findPageCount(app);
	}

	@Override
	public String findAppSecretByAppkey(String appKey) {
		return appMapper.findAppSecretByAppkey(appKey);
	}

}
