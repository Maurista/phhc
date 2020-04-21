package mongo_java;

import com.AppConfig;
import com.mongo_java.pojo.Address;
import com.mongo_java.pojo.Hobby;
import com.mongo_java.pojo.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class MongoQuery {

    private static String id;

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoQuery(){
        mongoTemplate.remove(new Query(), Person.class);

        mongoTemplate.insert(new Person("Meow", new Address("street", "保定", "0529"),
                Arrays.asList(new Hobby("1", "dance"), new Hobby("2", "sing"))));

        id = Objects.requireNonNull(mongoTemplate.findOne(new Query(Criteria.where("name").is("Meow")), Person.class)).getPersonId();
    }

    /**
     * 按id查找
     */
    @Test
    public void findById() {
        Person person = mongoTemplate.findById(id, Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());
    }

    /**
     * 单一查询条件用例
     */
    @Test
    public void findOne() {
        //等于匹配
        Person person = mongoTemplate.findOne(new Query(Criteria.where("name").is("Meow")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());

        //大小匹配
        //gt > ,le < ,gte >= ,lte <=
        person = mongoTemplate.findOne(new Query(Criteria.where("addr.zip").gt("0528")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());

        //like，正则/模糊匹配
        person = mongoTemplate.findOne(new Query(Criteria.where("addr.zip").regex(Pattern.compile("^.*0529.*$"))), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());

        //in,not in
        List<Person> people = mongoTemplate.find(new Query(Criteria.where("addr.zip").in("1010","1001")), Person.class);
        Assert.assertEquals(0, people.size());

        people = mongoTemplate.find(new Query(Criteria.where("addr.zip").nin("1010","1001")), Person.class);
        Assert.assertEquals(1, people.size());

        //子文档查询，中间用.隔开
        person = mongoTemplate.findOne(new Query(Criteria.where("hobbyList.hobbyName").is("dance")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());
    }

    /**
     * 多条件
     * 1.Criteria criteria = new Criteria();
     * 2.criteria.addOperator();    cirteria.orOperator();  criteria.norOperator();
     */
    @Test
    public void findMultiCondition(){
        //(A AND B) || (C AND C)
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("addr.zip").nin("1010","1001").and("name").is("Meow"),
                Criteria.where("addr.zip").nin("2010","2001").and("name").is("Katherine"));
        Person person = mongoTemplate.findOne(new Query(criteria), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());

    }

    /**
     * 分页查询以及排序
     */
    @Test
    public void findPageSort() {
        List<Person> people = mongoTemplate.find(new Query(Criteria.where("addr.zip").is("0529"))
                .skip(0).limit(1)       //分页
                .with(Sort.by(Sort.Order.asc("addr.zip"))), Person.class);  //排序
        Assert.assertEquals(1, people.size());
    }
}
