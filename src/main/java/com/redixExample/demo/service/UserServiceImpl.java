package com.redixExample.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.redixExample.demo.entity.User;
import com.redixExample.demo.entity.User.Sex;
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Cacheable("userCache")
    @Override
    public User findById(long id) {
        System.err.println("visit business service findById,id:{}");
        System.out.println("");
        User user = new User();
        user.setId(id);
        user.setUserName("parker");
        user.setPassWord("******");
        user.setSex(Sex.M);
        user.setAge(32);
        //耗时操作
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }


    @Override
    public List<User> findByPage(int startIndex, int limit) {
        return null;
    }

    @Cacheable("userCache")
    @Override
    public List<User> findBySex(Sex sex) {
        LOG.info("visit business service findBySex,sex:{}",sex);
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i);
            user.setUserName("parker"+i);
            user.setPassWord("******");
            user.setSex(sex);
            user.setAge(32+i);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> findByAge(int lessAge) {
        // TODO Auto-generated method stub
        return null;
    }

    @Cacheable("userCache")
    @Override
    public List<User> findByUsers(List<User> users) {
        LOG.info("visit business service findByUsers,users:{}",users);
        return users;
    }


    @CacheEvict("userCache")
    @Override
    public boolean update(User user) {
        return true;
    }

    @CacheEvict("userCache")
    @Override
    public boolean deleteById(long id) {
        return false;
    }

}
