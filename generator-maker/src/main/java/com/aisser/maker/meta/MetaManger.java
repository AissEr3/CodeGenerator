package com.aisser.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/27-15:23
 */
public class MetaManger {

    private static volatile Meta meta;

    private MetaManger(){

    }

    public static Meta getMetaInstance(){
        if(meta == null){
            synchronized (MetaManger.class){
                if(meta == null){
                    meta = initMeta();
                }
            }
        }

        return meta;
    }

    private static Meta initMeta(){
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        // todo 校验和处理默认值

        return newMeta;
    }


}
