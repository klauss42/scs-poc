package com.demo.scs.poc.shared.usercontext.service;

import java.util.Map;

public interface UserContextService {

    Map<String, String> getContext(String userId);

    void putContext(String userId, Map<String, String> map);
}
