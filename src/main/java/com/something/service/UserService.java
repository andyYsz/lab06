package com.something.service;

import com.something.dao.UserMapper;
import com.something.entity.User;
import com.something.pojo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * getUserByUsername
     * @param username
     * @return User
     */
    public UserVo getUserByUsername(String username) {
        UserVo userVo = null;
        User user = userMapper.selectByUsername(username);
        if(null != user){
            userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
        }
        return userVo;
    }

    /**
     * register
     * @param userVo
     */
    public void register(UserVo userVo) {
        User user = null;
        if(null != userVo){
            user = new User();
            BeanUtils.copyProperties(userVo, user);
        }
        userMapper.insert(user);
    }

    /**
     * update
     * @param userVo
     */
    public void update(UserVo userVo) {
        User user = null;
        if(null != userVo){
            user = new User();
            BeanUtils.copyProperties(userVo, user);
        }
        userMapper.update(user);
    }
}
