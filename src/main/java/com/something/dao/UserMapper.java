package com.something.dao;

import com.something.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from sys_user")
    List<User> findAll();

    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    User selectByUsername(String username);

    @Delete("DELETE FROM sys_user WHERE username = #{username}")
    int deleteByUsername(String username);

    @Insert("INSERT INTO sys_user(username, password,name,email) VALUES (#{username}, #{password}, #{name}, #{email})")
    int insert(User user);

    @Update("Update sys_user set name=#{name}, email=#{email}, password=#{password} where username=#{username}")
    int update(User user);
}
