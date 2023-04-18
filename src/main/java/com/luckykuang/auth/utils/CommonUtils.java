package com.luckykuang.auth.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.luckykuang.auth.constant.ErrorCodeEnum;
import com.luckykuang.auth.exception.BusinessException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 公用的一个工具类
 * @author luckykuang
 * @date 2023/4/12 16:30
 */
public class CommonUtils {
    private static final Random random =  new Random();

    private CommonUtils(){}

    /**
     * 1. 基本类型对应的的包装类型
     * 2. Java对象
     * 3. String字符串
     * 4. JsonObject对象
     * 5. List集合
     * @param params
     * @return 为空返回true，非空返回false
     * @param <T>
     */
    @SafeVarargs // 使用@SafeVarargs,抑制unchecked警告，方法必须声明为static或final。如果不是static或final方法，要抑制unchecked警告，可以使用@SuppressWarnings注解
    public static <T> boolean isEmpty(T... params){
        for (T param : params) {
            if (param == null || "".equals(param) ||
                    (param instanceof Character character && Character.isSpaceChar(character)) ||
                    (param instanceof JsonObject jsonObject && jsonObject.size() == 0) ||
                    (param instanceof JsonArray jsonArray && jsonArray.size() == 0) ||
                    (param instanceof List<?> list && list.isEmpty())){
                return true;
            }
        }
        return false;
    }
    @SafeVarargs
    public static <T> boolean isNotEmpty(T... params){
        return !isEmpty(params);
    }

    /**
     * 获取去除"-"后的uuid
     * @param flag true-获取去除"-"后的uuid  false-获取原始uuid
     * @return
     */
    public static String getUUID(boolean flag){
        if (flag){
            return UUID.randomUUID().toString().replace("-","");
        }
        return UUID.randomUUID().toString();
    }

    /**
     * 随机生成指定区间的数字 左右闭包
     * @param max 生成数字的最大值
     * @param min 生成数字的最小值
     * @return
     */
    public static Integer getRandomInt(Integer max,Integer min) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 模糊匹配
     * @param str 需要匹配的字符串
     * @param regEx 正则表达式
     * @return 真-true 假-false
     */
    public static boolean checkRegEx(String str, String regEx){
        Pattern pattern = Pattern.compile(regEx);
        return pattern.matcher(str).matches();
    }

    /**
     * 集合内实体所有字段判空
     * @param params 集合
     * @return 如果有字段为空，则返回true；所有字段都不为空，则返回false
     * @param <T>
     */
    public static <T> boolean isEmptyToEntityList(List<T> params) {
        if (params == null || params.isEmpty()){
            return true;
        }
        for (T param : params) {
            if (param == null || "".equals(param)){
                return true;
            }
            boolean emptyToEntity = isEmptyToEntity(param);
            if (Boolean.TRUE.equals(emptyToEntity)){
                return true;
            }
        }
        return false;
    }

    /**
     * 单个实体类中的所有字段判空
     * @param param 实体类
     * @return 如果有字段为空，则返回true；所有字段都不为空，则返回false
     * @param <T>
     */
    public static <T> boolean isEmptyToEntity(T param) {
        // 获取所有的属性数组
        Field[] fields = param.getClass().getDeclaredFields();
        if (fields.length == 0){
            return true;
        }
        for (Field field : fields) {
            // 设置允许通过反射访问私有变量
            field.setAccessible(true);
            try {
                // 获取字段的值
                Object fieldValue = field.get(param);
                if (fieldValue == null || "".equals(fieldValue)){
                    return true;
                }
            } catch (IllegalAccessException e) {
                throw new BusinessException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
            }
        }
        return false;
    }

    /**
     * 获取实体类中的字段及类型
     * @param param 实体类
     * @return map(fieldValue,fieldType)
     * @param <T>
     */
    public static <T> Map<Object,String> getClassToFields(T param) {
        Map<Object,String> map = new HashMap<>();
        // 获取所有的属性数组
        Field[] fields = param.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 设置允许通过反射访问私有变量
            field.setAccessible(true);
            try {
                // 获取字段的值
                Object fieldValue = field.get(param);
                // 获取字段的类型
                String fieldType = field.getType().getSimpleName();
                map.put(fieldValue,fieldType);
            } catch (IllegalAccessException e) {
                throw new BusinessException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
            }
        }
        return map;
    }
}
