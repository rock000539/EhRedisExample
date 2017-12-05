package com.redixExample.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import generated.Cache;
import generated.Ehcache;
import net.sf.itcb.addons.cachemanager.ItcbEhCacheManagerFactoryBean;

//@Configuration
public class EhCacheConfig {

	//Spring Cache 
	@Bean
	public CacheManager cacheManager()  {
//		 return new EhCacheCacheManager(ehCacheCacheManager().getObject());
		return new EhCacheCacheManager(itcbEhCacheManagerFactoryBean().getObject());
	}

	//EhCache 
//	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
//		cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}

	//ItcbEhCach 
	@Bean
	public ItcbEhCacheManagerFactoryBean itcbEhCacheManagerFactoryBean() {
		ItcbEhCacheManagerFactoryBean cmfb = new ItcbEhCacheManagerFactoryBean();
		Jaxb2Marshaller ehcacheMarshaller = new Jaxb2Marshaller();
		ehcacheMarshaller.setClassesToBeBound(generated.BootstrapCacheLoaderFactory.class, generated.Cache.class,
				generated.CacheDecoratorFactory.class, generated.CacheEventListenerFactory.class,
				generated.CacheExceptionHandlerFactory.class, generated.CacheExtensionFactory.class,
				generated.CacheLoaderFactory.class, generated.CacheManagerEventListenerFactory.class,
				generated.CacheManagerPeerListenerFactory.class, generated.CacheManagerPeerProviderFactory.class,
				generated.CacheWriter.class,generated.CacheWriterFactory.class,generated.ConsistencyType.class, generated.CopyStrategy.class,
				generated.DefaultCache.class, generated.DiskStore.class, generated.Ehcache.class,
				generated.ElementValueComparator.class, generated.MonitoringType.class, generated.Nonstop.class,
				generated.NotificationScope.class, generated.Searchable.class, generated.SearchAttribute.class,
				generated.StorageStrategyType.class, generated.Terracotta.class,
				generated.TerracottaCacheValueType.class, generated.TerracottaConfig.class,
				generated.TimeoutBehavior.class, generated.TimeoutBehaviorType.class, generated.TransactionalMode.class,
				generated.TransactionManagerLookup.class, generated.WriteModeType.class);
		try {
//			cmfb.setConfigLocations("classpath*:ehcache*.xml");
			cmfb.setEhcacheMarshaller(ehcacheMarshaller);
			cmfb.setShared(true);
			cmfb.setConfigLocations("classpath*:config/ehcache*.xml");
//			cmfb.setCacheManagerName("itcbEhCacheManagerFactoryBean");
			cmfb.afterPropertiesSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cmfb;
	}
	
	
//	@Bean
	public net.sf.ehcache.CacheManager manualCacheManager()  {
		net.sf.ehcache.CacheManager returnCache=null;
		try {
		Jaxb2Marshaller ehcacheMarshaller = new Jaxb2Marshaller();
		ehcacheMarshaller.setClassesToBeBound(generated.BootstrapCacheLoaderFactory.class, generated.Cache.class,
				generated.CacheDecoratorFactory.class, generated.CacheEventListenerFactory.class,
				generated.CacheExceptionHandlerFactory.class, generated.CacheExtensionFactory.class,
				generated.CacheLoaderFactory.class, generated.CacheManagerEventListenerFactory.class,
				generated.CacheManagerPeerListenerFactory.class, generated.CacheManagerPeerProviderFactory.class,
				generated.CacheWriter.class,generated.CacheWriterFactory.class,generated.ConsistencyType.class, generated.CopyStrategy.class,
				generated.DefaultCache.class, generated.DiskStore.class, generated.Ehcache.class,
				generated.ElementValueComparator.class, generated.MonitoringType.class, generated.Nonstop.class,
				generated.NotificationScope.class, generated.Searchable.class, generated.SearchAttribute.class,
				generated.StorageStrategyType.class, generated.Terracotta.class,
				generated.TerracottaCacheValueType.class, generated.TerracottaConfig.class,
				generated.TimeoutBehavior.class, generated.TimeoutBehaviorType.class, generated.TransactionalMode.class,
				generated.TransactionManagerLookup.class, generated.WriteModeType.class);
		PathMatchingResourcePatternResolver testJob=new PathMatchingResourcePatternResolver();
		
		String path ="classpath*:config/ehcache*.xml";
		Resource[] result = testJob.getResources(path);
		for(int i=0;i<result.length;i++) {
			System.out.println("result["+i+"] ====="+result[i].toString());	
		}
		Ehcache ehcache;
		Ehcache ehcacheToReturn=null;
		for(Resource ehcacheXml : result) {
			if(ehcacheToReturn==null) {
				ehcacheToReturn = (Ehcache)ehcacheMarshaller.unmarshal(new StreamSource(ehcacheXml.getInputStream()));
			}
			else {
				ehcache= (Ehcache)ehcacheMarshaller.unmarshal(new StreamSource(ehcacheXml.getInputStream()));
				ehcacheToReturn.getCache().addAll(ehcache.getCache());
			}
		}
		for(Cache cache:ehcacheToReturn.getCache()) {
			System.out.println("cache =="+cache.getName());
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamResult stream = new StreamResult(bos);
		ehcacheMarshaller.marshal(ehcacheToReturn, stream);
		InputStream io = new ByteArrayInputStream(bos.toByteArray());
		returnCache = new net.sf.ehcache.CacheManager(io);
		}catch(Exception e){
			e.printStackTrace();
			}
		
		return returnCache;
	}
}
