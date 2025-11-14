package com.sky.utils;

import java.lang.reflect.Field;

public class BeanTrimAndNullConvertUtils {

    /**
     * 将对象中所有 String 类型的字段执行：
     * 1. trim()
     * 2. 若结果为空字符串，则设为 null
     *
     * @param bean 要处理的 Java Bean
     */
    public static void trimAndConvertEmptyToNull(Object bean) {
        if (bean == null) return;

        Class<?> clazz = bean.getClass();
        while (clazz != null && clazz != Object.class) {

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                // 只处理 String 类型
                if (field.getType() != String.class) continue;

                field.setAccessible(true);

                try {
                    String value = (String) field.get(bean);
                    if (value == null) continue;

                    // trim
                    String trimmed = value.trim();

                    // "" → null
                    if (trimmed.isEmpty()) {
                        field.set(bean, null);
                    } else {
                        field.set(bean, trimmed);
                    }
                } catch (IllegalAccessException ignored) {}
            }

            // 继续处理父类字段（支持继承结构）
            clazz = clazz.getSuperclass();
        }
    }

}
