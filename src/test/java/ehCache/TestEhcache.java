package ehCache;


import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.redixExample.demo.Application;
import com.redixExample.demo.entity.Item;
import com.redixExample.demo.service.ItemService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestEhcache {
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private ItemService itemService;
	
	private Cache demoCache; 
	private Cache demoTimeToIdleSeconds; 
	private Cache demoTimeToLiveSeconds; 
	private Cache myCache;
	private Integer maxElements;
	@Before
	public void setUp() {
		maxElements=10;
		demoCache = cacheManager.getCache("demo");
		demoTimeToIdleSeconds = cacheManager.getCache("demoTimeToIdleSeconds");
		demoTimeToLiveSeconds = cacheManager.getCache("demoTimeToLiveSeconds");
		myCache = cacheManager.getCache("myCache");
	}
	
	/*For test Ehcache setting 
	 * */
	@SuppressWarnings("unchecked")
	@Test
	public void test_maxElementsInMemory()  {
		
		for(int i=1;i<=maxElements;i++) {
			demoCache.put(new Element("DemoKey"+i, "Value"+i));	
		}
		
		List<Element>  keyList = demoCache.getKeys();
		System.out.println("maxElementsInMemory =="+keyList.size());
		
		for(int i=0;i<keyList.size();i++) {
			Element cacheElement = demoCache.get(keyList.get(i));
			
			if(cacheElement!=null) {
				System.out.println("key == "+cacheElement.getKey()+ " value == "+cacheElement.getValue());	
			}else {
				System.out.println("cacheElement=="+cacheElement);
			}
			
		}
	}
	
	/*For test Ehcache timeToIdle setting 
	 * */
//	@Test
	@SuppressWarnings("unchecked")
	public void test_timeToIdleSeconds() throws InterruptedException {
		for(int i=1;i<=maxElements;i++) {
			demoTimeToIdleSeconds.put(new Element("testKey"+i, "testValue"+i));	
		}
		
		Thread.sleep(5000);
		List<Element> keyList = demoTimeToIdleSeconds.getKeys();
		
		for(int i=0;i<keyList.size();i++) {
			Element cacheElement = demoTimeToIdleSeconds.get(keyList.get(i));
			
			if(cacheElement!=null) {
				System.out.println("value == "+cacheElement.getValue());	
			}else {
				System.out.println("cacheElement=="+cacheElement);
			}
		}
	}
	
	/*For test Ehcache timeToLive setting 
	 * */
//	@Test
	@SuppressWarnings("unchecked")
	public void test_timeToLiveSeconds() throws InterruptedException {
		for(int i=1;i<=maxElements;i++) {
			demoTimeToLiveSeconds.put(new Element("testKey"+i, "testValue"+i));	
		}
		
		Thread.sleep(3000);
		List<Element> keyList = demoTimeToLiveSeconds.getKeys();

		
		for(int i=0;i<keyList.size();i++) {
			Element cacheElement = demoTimeToLiveSeconds.get(keyList.get(i));
			
			if(cacheElement!=null) {
				System.out.println("value == "+cacheElement.getValue());	
			}else {
				System.out.println("cacheElement=="+cacheElement);
			}
			
		}
	}
	
		
	/*For test hibernate second level Cache
	 * Only for ehcache setting
	 * */
//	@Test
	public void test_hibernateCache() {
		
		for(int i=0;i<=maxElements;i++) {
			Item tempBean=new Item();
			tempBean.setId(Long.valueOf(i+""));
			tempBean.setName("ItemName"+i);
			tempBean.setType("ItemType"+i);
			itemService.save(tempBean);
		}
		
		for(int i=0;i<=10;i++) {
			System.out.println(itemService.find(1L).getName());	
		}
	}
	
	
	/*For test cluster Cache 
	 * */
//	@Test
	public void test_remoteCache() throws InterruptedException {
		for(int i=1;i<=10;i++) {
			myCache.put(new Element("Key"+i, "Value"+i));	
			Thread.sleep(1000);
		}
		System.out.println("myCache size is =="+myCache.getSize());
		
		Thread.sleep(3000);
		System.out.println("After remove myCache size is =="+myCache.getSize());
	}

}

