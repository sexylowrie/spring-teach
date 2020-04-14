package xin.sunce.spring.service;

import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2010-2020, xxx payment. Co., Ltd.
 * <p>
 * 测试方法
 *
 * @author lowrie
 * @version 1.0.0
 * @date 2020-04-13
 */
@Service
public class TestService {

    public static class Result {
        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public Result getResult() {
        throw new IllegalArgumentException("参数错误");
    }
}
