package com.luckykuang.auth.service;

import com.luckykuang.auth.record.SignInRec;
import com.luckykuang.auth.record.SignOnRec;
import com.luckykuang.auth.record.JwtRspRec;

/**
 * @author luckykuang
 * @date 2023/4/18 17:51
 */
public interface SignService {
    JwtRspRec signIn(SignInRec sign);

    void signOn(SignOnRec sign);
}
