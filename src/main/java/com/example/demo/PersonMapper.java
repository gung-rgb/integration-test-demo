package com.example.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PersonMapper {

    @Select("select '#{name}' as name, ${id} as age")
    Person selectOne(@Param("id") int id, @Param("name") String name);
}
