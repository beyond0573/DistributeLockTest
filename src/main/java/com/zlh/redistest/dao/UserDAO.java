package com.zlh.redistest.dao;

import com.zlh.redistest.model.User;
import org.apache.ibatis.annotations.*;


/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
public interface UserDAO {
    // 注意空格
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, age, version, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id} for update"})
    User selectByIdForUpdate(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Update({"update ", TABLE_NAME, " set age=#{age} where id=#{id}"})
    void updateAge(User user);


    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);


    //如果没有登录就是拉取所有人里面最新的几条.maxid增量更新 feed id要小于maxid
//    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
//                               @Param("userIds") List<Integer> userIds,
//                               @Param("count") int count);

    void updateUserVersion(User user);
}
