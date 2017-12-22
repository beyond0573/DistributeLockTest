package com.zlh.redistest;

import com.alibaba.druid.pool.DruidDataSource;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.*;
import redis.clients.jedis.Transaction;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;
import java.util.Date;

@RestController
@SpringBootApplication
@EnableTransactionManagement
public class RedistestApplication {


	static int count=0;
	@RequestMapping("/")
	public String index(){
		Jedis jedis = new Jedis("redis://localhost:6379/9");
		for(int i=0;i<20000;i++){
			 //int count=Integer.parseInt(jedis.get("count"));
			// count=count+1;
			 jedis.incr("count");
			 //jedis.set("count",Integer.toString(count));
			//System.out.println(count);
		 }
		jedis.close();
		return "Hello Spring boot";
	}


	@RequestMapping("/hello")
	public String hello(){
		Jedis jedis = new Jedis("redis://localhost:6379/9");
		Date now = new Date();
		System.out.println("start time: "+ now+"thread id"+Thread.currentThread().getId());
		for(int i=0;i<7;i++){
			//count=count+1;
			jedis.incr("count");
		}
		jedis.close();
		Date end = new Date();
		System.out.println("end time: "+ end+"thread id"+Thread.currentThread().getId());
		//System.out.println("Hello Spring boot"+count);
		return "Hello Spring boot"+count;
	}


	@RequestMapping("/transaction")
	public String transaction(){
		Jedis jedis = new Jedis("redis://localhost:6379/9");
	//	int count=0;
		//jedis.watch("count");

		Transaction tx=jedis.multi();

		for(int i=0;i<200000;i++){
			 Object count=tx.get("count");
			//count=count+1;
			//tx.set("count",Integer.toString(count));
			System.out.println(count);
		}
		tx.exec();
		jedis.close();
		return "Hello Spring boot";
	}


	@RequestMapping("/distribute")
	public String distribute(){
		RedisLock redisLock=new RedisLock("distribute");
		//Jedis jedis = new Jedis("redis://localhost:6379/9");
        for (int i=0;i<7;i++){
        	String identifier=redisLock.tryLock2(2);
        	if(!identifier.equals("false")){
				int count=Integer.parseInt(redisLock.jedis.get("count"));
				count=count+1;
				redisLock.jedis.set("count",Integer.toString(count));
				redisLock.unlock("distribute",identifier);
			}

		}
		redisLock.jedis.close();
		return "Hello Spring boot";
	}


	@RequestMapping("/redisson")
	public String redisson(){
		Config config = new Config();
		config.useSingleServer().setAddress("http://127.0.0.1:6379");
		RedissonClient client = Redisson.create(config);

		//Jedis jedis = new Jedis("redis://localhost:6379/9");
		RLock lock = client.getLock("lock");
		lock.lock();
		//int count=0;
		for (int i=0;i<2;i++){
			count=count+1;
		}
		lock.unlock();
		client.shutdown();
		System.out.println("count value:"+count);
		return "Hello Spring boot "+count;
	}


	@Autowired
	private Environment env;

	//destroy-method="close"的作用是当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.
	@Bean(destroyMethod =  "close")
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));//用户名
		dataSource.setPassword(env.getProperty("spring.datasource.password"));//密码
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setInitialSize(2);//初始化时建立物理连接的个数
		dataSource.setMaxActive(20);//最大连接池数量
		dataSource.setMinIdle(0);//最小连接池数量
		dataSource.setMaxWait(60000);//获取连接时最大等待时间，单位毫秒。
		dataSource.setValidationQuery("SELECT 1");//用来检测连接是否有效的sql
		dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效
		dataSource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。
		dataSource.setPoolPreparedStatements(false);//是否缓存preparedStatement，也就是PSCache
		return dataSource;
	}





	public static void main(String[] args) {
		SpringApplication.run(RedistestApplication.class, args);
	}
}
