package com.aisser.maker.meta;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/29-22:29
 */
public class MetaException extends RuntimeException{
    public MetaException(){

    }

    public MetaException(String msg){
        super(msg);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
