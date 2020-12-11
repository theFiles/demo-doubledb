package com.example.demodoubledb.controller;

import com.example.demodoubledb.mapper.base.BaseUserMapper;
import com.example.demodoubledb.mapper.dic.DicUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lidaye
 * @date 2020/12/3
 */
@RestController
public class TestController {
    @Resource
    private BaseUserMapper baseUserMapper;

    @Resource
    private DicUserMapper dicUserMapper;

    @GetMapping("/getUser")
    public Object getUser(String dbType) {
        return "dic".equals(dbType) ? dicUserMapper.findTenUser() : baseUserMapper.findTenUser();
    }
}
