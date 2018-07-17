package com.software.phone.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * 将sourceList中的一些属性合并到targetList中
 * 基于biFunction的条件，合入逻辑实现为biConsumer
 *
 * @ClassName MergeUtil
 * @Description
 * @Author 徐旭
 * @Date 2018/7/17 15:03
 * @Version 1.0
 */
public class MergeUtil {
    public static <T, S> void merge (List<T> targetList, List<S> sourceList, BiFunction<? super T, ? super S, Boolean> biFunction,
                                     BiConsumer<? super T, ? super S> biConsumer) {
        targetList.forEach(t -> {
            Optional<S> optional = sourceList.stream().filter(s -> biFunction.apply(t, s)).findFirst();
            if (optional.isPresent()) {
                biConsumer.accept(t, optional.get());
            }
        });
    }
}
