package com.example.xapiservice.app.mapper;

import com.example.xapiservice.app.common.BaseMapper;

public interface AppMapper<T> extends BaseMapper<T> {

    String findAppSecretByAppkey(String appKey);

}
