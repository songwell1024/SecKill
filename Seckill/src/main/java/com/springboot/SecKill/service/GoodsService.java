package com.springboot.SecKill.service;

import com.springboot.SecKill.dao.GoodsDao;
import com.springboot.SecKill.domain.Goods;
import com.springboot.SecKill.domain.SecKillGoods;
import com.springboot.SecKill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/8/3/003
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {

        SecKillGoods goods1 = new SecKillGoods();
        goods1.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(goods1);
        return ret > 0;
    }
}
