package com.springboot.SecKill.exception;

import com.springboot.SecKill.result.CodeMsg;

/**
 * @author WilsonSong
 * @date 2018/8/2/002
 */
public class GlobalException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException (CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public CodeMsg getCm() {
        return cm;
    }

    public void setCm(CodeMsg cm) {
        this.cm = cm;
    }
}
