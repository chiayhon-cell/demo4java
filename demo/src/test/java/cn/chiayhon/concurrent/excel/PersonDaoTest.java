package cn.chiayhon.concurrent.excel;

import cn.chiayhon.App;
import cn.chiayhon.concurrent.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
class PersonDaoTest {

    @Autowired
    private PersonDao personDao;

    @Test
    void queryById() {
        Person person = personDao.queryById(128312L);
        System.out.println(person);
    }
}