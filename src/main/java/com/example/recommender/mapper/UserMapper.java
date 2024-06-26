package com.example.recommender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.recommender.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    // @Select("select * from user where id=#{id} ")
    // public User selectById(Integer id);
}
