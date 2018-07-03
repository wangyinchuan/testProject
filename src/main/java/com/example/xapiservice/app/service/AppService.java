package com.example.xapiservice.app.service;

import com.example.xapiservice.app.common.BaseService;
import com.example.xapiservice.app.entity.App;

public interface AppService extends BaseService<App> {

    String findAppSecretByAppkey(String appKey);
}