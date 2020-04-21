package mongo_java;

import com.AppConfig;
import com.mongo_java.pojo.Address;
import com.mongo_java.pojo.Hobby;
import com.mongo_java.pojo.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class MongoCUD {

    private static String id;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void init(){
        mongoTemplate.remove(new Query(), Person.class);

        mongoTemplate.insert(new Person("Meow", new Address("street", "保定", "0529"),
                Arrays.asList(new Hobby("1", "dance"), new Hobby("2", "sing"))));

        id = Objects.requireNonNull(mongoTemplate.findOne(new Query(Criteria.where("name").is("Meow")), Person.class)).getPersonId();
    }

    /**
     * 插入
     */
    @Test
    public void insert() {
        mongoTemplate.insert(new Person("Meow0", new Address("street", "保定", "0529"),
                Arrays.asList(new Hobby("1", "dance"), new Hobby("2", "sing"))));

        Person person = mongoTemplate.findOne(new Query(Criteria.where("name").is("Meow0")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow0", person.getName());
    }

    /**
     * 按主键删除
     */
    @Test
    public void deleteById() {
        mongoTemplate.remove(new Query(Criteria.where("personId").is(id)), Person.class);
        Assert.assertEquals(0, mongoTemplate.count(new Query(), Person.class));
    }

    /**
     * 更新(实体替换)
     */
    @Test
    public void update() {
        mongoTemplate.findAndReplace(new Query(Criteria.where("name").is("Meow")), new Person("Katherine", new Address("street", "保定", "0529"),
                Arrays.asList(new Hobby("1", "dance"), new Hobby("2", "sing"))));
        Person person = mongoTemplate.findOne(new Query(Criteria.where("name").is("Katherine")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Katherine", person.getName());
    }

    /**
     * 部分字段更新
     */
    @Test
    public void updatePart() {
        mongoTemplate.updateMulti(new Query(Criteria.where("name").is("Meow")), Update.update("name", "Katherine").set("addr.city", "喵星"), Person.class);
        Person person = mongoTemplate.findOne(new Query(Criteria.where("name").is("Katherine")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Katherine", person.getName());
    }
}
