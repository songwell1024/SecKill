package com.springboot.SecKill.vo;

import com.springboot.SecKill.domain.Goods;

import java.util.Date;

/**
 * 拼接商品表和秒杀的商品表
 * @author WilsonSong
 * @date 2018/8/3/003
 */
public class GoodsVo extends Goods{

    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}
