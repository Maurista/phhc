package mongo_java;


import com.AppConfig;
import com.mongo_java.pojo.Address;
import com.mongo_java.pojo.Employee;
import com.mongo_java.pojo.Hobby;
import com.mongo_java.pojo.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class Demo {

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
        mongoTemplate.insert(new Person("Meow", new Address("street", "保定", "0529"),
                Arrays.asList(new Hobby("1", "dance"), new Hobby("2", "sing"))));

        Person person = mongoTemplate.findOne(new Query(Criteria.where("name").is("Meow")), Person.class);
        Assert.assertNotNull(person);
        Assert.assertEquals("Meow", person.getName());
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
        person = mongoTemplate.findOne(new Query(Criteria.where("name").gt("Meov")), Person.class);
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

    /**
     * 按条件删除
     */
    @Test
    public void delete() {
        mongoTemplate.remove(new Query(Criteria.where("addr.city").is("保定")), Person.class);
        Assert.assertEquals(0, mongoTemplate.count(new Query(), Person.class));
    }

    /**
     * 按主键删除
     */
    @Test
    public void deleteById() {
        mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), Person.class);
        Assert.assertEquals(0, mongoTemplate.count(new Query(), Person.class));
    }

    /**
     * 事务
     */
    @Test
    @Transactional
    public void trans() {
        mongoTemplate.remove(new Query(Criteria.where("addr.city").is("保定")), Person.class);
        Assert.assertEquals(0, mongoTemplate.count(new Query(), Person.class));
    }

    /**
     * 多表
     * 1.insertEmployee() 向 employee表中插入一条数据
     * 2.multi() 执行多表查询
     */
    @Test
    public void insertEmployee() {
        mongoTemplate.insert(new Employee("Meow"));
        Employee employee = mongoTemplate.findOne(new Query(Criteria.where("employeeName").is("Meow")), Employee.class);
        Assert.assertNotNull(employee);
        Assert.assertEquals("Meow", employee.getEmployeeName());
    }

    @Test
    public void multi() {
        LookupOperation lookupOperation = LookupOperation.newLookup().from("employee")
                .localField("employeeName").foreignField("name").as("employeeInfo");
        TypedAggregation aggregation = Aggregation.newAggregation(Person.class, lookupOperation);
        AggregationResults<Object> results = mongoTemplate.aggregate(aggregation, Object.class);
        List<Map<String,Object>> list = (List)results.getRawResults().get("results");
        List list1 = (List) list.get(0).get("employeeInfo");
        Map<String,String> map = (Map<String,String>) list1.get(0);
        Assert.assertEquals("Meow", map.get("employeeName"));
    }
}
