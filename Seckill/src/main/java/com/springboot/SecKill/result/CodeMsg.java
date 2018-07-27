package com.springboot.SecKill.result;

/**
 * @author WilsonSong
 * @date 2018/7/27/027
 */
public class CodeMsg {
    private int code;
    private String msg;

    //模块化定义，通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");

    //登陆模块 5002xx

    //商品模块 5003xx

    //订单模块  5004xx

    //秒杀模块  5005xx

    private CodeMsg(int code, String msg){
        this.code  = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
