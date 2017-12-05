package com.redixExample.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redixExample.demo.entity.Item;

public interface ItemDAO extends JpaRepository<Item, Long>{

}
