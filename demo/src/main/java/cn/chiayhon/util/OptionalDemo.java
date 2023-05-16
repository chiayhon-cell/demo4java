package cn.chiayhon.util;

import cn.chiayhon.pojo.User;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static cn.chiayhon.enums.Gender.MAN;
import static cn.chiayhon.enums.Gender.WOMAN;

@Slf4j
public class OptionalDemo {

    public static User ifPresentTest() {
        final User emptyUer = User.builder().username("空用户").build();

        final Supplier<Stream<User>> streamSupplier = () ->
                Stream.of(
                        User.builder().username("马云").gender(MAN).age(58).build(),
                        User.builder().username("马化腾").gender(MAN).age(51).build(),
                        User.builder().username("马思纯").gender(WOMAN).age(32).build(),
                        User.builder().username("马克思").gender(MAN).age(500).build(),
                        User.builder().username("马保国").gender(MAN).age(70).build()
                );

        final Optional<User> userOpt1 = streamSupplier.get()
                .sorted((o1, o2) -> o2.getAge() - o1.getAge())
                .filter(user -> user.getGender().equals(MAN))
                .findFirst();
        //1
        userOpt1.ifPresent(user ->
                log.info("1.年龄最大的男人：{}", user.getUsername())
        );
        //2
        if (userOpt1.isPresent()) {
            log.info("2.年龄最大的男人：{}", userOpt1.get().getUsername());
        }
        // orElse
        User nullUser = null;

        User user1 = userOpt1.orElse(emptyUer);
        User user2 = Optional.ofNullable(nullUser).orElse(emptyUer);
        log.info("3.年龄最大的男人：{}", JSON.toJSON(user1));
        log.info("4.年龄最大的男人：{}", JSON.toJSON(user2));

        return user1;
    }

    public static void main(String[] args) throws ParseException {
        ifPresentTest();
    }
}
