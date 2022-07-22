package cn.chiayhon.jdk.collection.list;

import cn.chiayhon.pojo.User;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingDemo {

    public static void main(String[] args) {

        List<User> newList = Stream.of(
                User.builder().username("王小明1").age(11).build(),
                User.builder().username("王小明2").age(11).build(),
                User.builder().username("王小明3").age(11).build(),
                User.builder().username("王小明1").age(11).build(),
                User.builder().username("王小明2").age(22).build()
        ).collect(Collectors.toList());

        System.out.println("原集合：" + JSON.toJSON(newList));

        final List<User> remove1 = testRemove1(newList);
        System.out.println("结果1：" + JSON.toJSON(remove1));

        final List<User> remove2 = testRemove2(newList);
        System.out.println("结果2：" + JSON.toJSON(remove2));

        final List<User> remove3 = testRemove3(newList);
        System.out.println("结果3：" + JSON.toJSON(remove3));
    }

    //1
    public static List<User> testRemove1(List<User> newList) {
        return newList.stream().filter(distinctByKey(User::getUsername)).collect(Collectors.toList());
    }

    public static Predicate<User> distinctByKey(Function<User, String> keyExtractor) {
        Map<String, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    //2
    public static List<User> testRemove2(List<User> newList) {
        Set<String> set = new ConcurrentHashSet<>();
        return newList.stream().filter(user -> set.add(user.getUsername())).collect(Collectors.toList());
    }

    //3
    public static List<User> testRemove3(List<User> newList) {
        return newList.stream()
                .collect(Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(User::getUsername))),
                                ArrayList::new
                        )
                );
    }
}
