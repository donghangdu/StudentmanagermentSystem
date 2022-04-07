package com.wip.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;


@Log
public class ConvertUtil {

    /**
     * Transformation between object entities
     *
     * @param object Input object
     * @param clazz  The type of data entity bytecode to be converted to
     * @param <T>    Entity generics that need to be converted to
     * @return
     */
    public static <T> T convert(Object object, Class<T> clazz) {
        if (null == object) {
            return null;
        }
        try {
            return JSONObject.parseObject(JSONObject.toJSONString(object), clazz);
        } catch (Exception e) {
            log.severe("ConvertTool.convert,[param]:" + "object = [" + object + "], clazz = [" + clazz
                    + "], excepton=[" + e + "]");
            throw e;
        }
    }

    /**
     * Transformation between object entities
     *
     * @param list  Input parameter list
     * @param clazz The type of data entity bytecode to be converted to
     * @param <T>   Entity generics that need to be converted to
     * @return
     */
    public static <T> List<T> convert(List<?> list, Class<T> clazz) {
        if (null == list) {
            return new ArrayList<>();
        }
        try {
            return JSONObject.parseArray(JSONObject.toJSONString(list), clazz);
        } catch (Exception e) {
            log.info("ConvertTool.convert,[param]:" + "list = [" + list + "], clazz = [" + clazz
                    + "], excepton=[" + e + "]");
            throw e;
        }
    }
}