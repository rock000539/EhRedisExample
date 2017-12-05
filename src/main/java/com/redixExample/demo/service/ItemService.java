package com.redixExample.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.redixExample.demo.dao.ItemDAO;
import com.redixExample.demo.entity.Item;

import javassist.NotFoundException;

@Service
public class ItemService {
	@Autowired
	private ItemDAO itemDAO;

	public static final String CACHE_KEY = "'hibernateCacheInfo'";
	public static final String HIBERNATE_CACHE_NAME = "hibernateCache";

	@CacheEvict(value = HIBERNATE_CACHE_NAME, key = CACHE_KEY)
	public Item save(Item item) {
		return itemDAO.save(item);
	}

	@CachePut(value = HIBERNATE_CACHE_NAME, key = "'hibernateCache_'+#itemTO.getId()")
	public Item update(Item itemTO) throws NotFoundException {
		Item item = itemDAO.findOne(itemTO.getId());
		if (item == null) {
			throw new NotFoundException("No find");
		}
		item.setName(itemTO.getName());
		item.setType(itemTO.getType());
		return item;
	}

	@Cacheable(value=HIBERNATE_CACHE_NAME,key="'hibernateCache_'+#id") 
	public Item find(Long id) {
		System.err.println("get Data from DB " + id);
		return itemDAO.findOne(id);
	}

	public List<Item> findAll() {
		return itemDAO.findAll();
	}

	@CacheEvict(value = HIBERNATE_CACHE_NAME, key = "'hibernateCache_'+#id")
	public void delete(Long id) {
		itemDAO.delete(id);
	}

}
