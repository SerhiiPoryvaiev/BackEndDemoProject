package de.ait;

import de.ait.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

public class ParamTestDemo {

    @ParameterizedTest(name="execute test with: {arguments}")
    @ValueSource(strings = {"1a","2a","3a"})
    void test1_endWith(String str){
        Assertions.assertTrue(str.endsWith("a"));
    }


    @ParameterizedTest(name="test 1: {arguments}")
    @CsvFileSource(resources = "/data.csv")
    void test2_parseInt(int i, String str){
        Assertions.assertEquals(i, Integer.parseInt(str));
    }


    @ParameterizedTest(name="test 1: int: {0} str: {1}")
    @CsvSource({"1,qwe","2,qwe1","2,2"})
    void test_parseInt(int i, String str){
        Assertions.assertEquals(i, Integer.parseInt(str));
    }


    @ParameterizedTest(name="test 1: int: {0} str:{1} ")
    @MethodSource("argsProviderMethod")
    void test_user_constructor(String name, String email, User user){
        Assertions.assertEquals(user, new User(name,email));
    }

    public static Stream<Arguments>  argsProviderMethod() {
        return Stream.of(
                Arguments.of("jack","jack@mail.com", new User("jack","jack@mail.com")),
                Arguments.of("ann","ann@mail.com",new User("ann","ann@mail.com")),
                Arguments.of("nick","nick@mail.com", new User("nick","nick@mail.com"))
        );
    }





}
