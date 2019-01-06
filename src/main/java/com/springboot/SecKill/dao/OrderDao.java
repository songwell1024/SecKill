package com.springboot.SecKill.dao;

import com.springboot.SecKill.domain.OrderInfo;
import com.springboot.SecKill.domain.SecKillOrder;
import org.apache.ibatis.annotations.*;

/**
 * @author WilsonSong
 * @date 2018/8/4/004
 */
@Mapper
public interface OrderDao {

    @Select("select * from miaosha_order where id=#{userId} and goods_id=#{goodsId}")
    SecKillOrder getOrderByUserIdGoodsId(@Param("userId") long userId,@Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
    int insertSecKillOrder(SecKillOrder secKillOrder);

    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId")long orderId);
}
