package com.redixExample.demo.cache;

import java.io.IOException;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import net.sf.ehcache.CacheManager;
import net.sf.itcb.addons.cachemanager.ItcbEhCacheManagerFactoryBean;

public class ItcbEhCacheManagerFactoryBeanInstance extends EhCacheManagerFactoryBean{
	
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
	
	@Override
	public CacheManager getObject() {
		return new EhCacheCacheManager(itcbEhCacheManagerFactoryBean().getObject()).getCacheManager();
	}
}
