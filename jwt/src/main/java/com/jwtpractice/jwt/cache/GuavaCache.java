package com.jwtpractice.jwt.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jwtpractice.jwt.entity.UserData;
import com.jwtpractice.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class GuavaCache {

    @Autowired
    UserRepository userRepository;

    LoadingCache<Long, Optional<UserData>> loadingCacheByUserId;

    LoadingCache<String, List<UserData>> loadingCacheByUserName;

    public GuavaCache(){
        this.loadingCacheByUserId = CacheBuilder
                .newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<Long, Optional<UserData>>() {
                            @Override
                            public Optional<UserData> load(Long id) throws Exception {
                                System.out.println("get users from database hit");
                                return userRepository.findById(id);
                            }
                        }
                );

        this.loadingCacheByUserName = CacheBuilder
                .newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, List<UserData>>() {
                            @Override
                            public List<UserData> load(String userName) throws Exception {
                                System.out.println("...Returning object from Database....for key " + userName);
                                Iterable<UserData> allUsers =  userRepository.findAll();
                                List<UserData> matchedUsers = new ArrayList<>();
                                for(UserData user : allUsers){
                                    System.out.println(" Printing users " + user);
                                    if(user.getUserName() != null && user.getUserName().equals(userName)){
                                        System.out.println(" Printing not null users " + user);
                                        matchedUsers.add(user);
                                    }
                                }
                                return matchedUsers;
                            }
                        }
                );
    }

    public Optional<UserData> findByUserId(Long userId) throws Exception{
        System.out.println("...Returning object from cache....for key " + userId);
        return loadingCacheByUserId.get(userId);
    }

    public List<UserData> findByUserName(String userName) throws Exception{
        System.out.println("...Returning object from cache....for key " + userName);
        return loadingCacheByUserName.get(userName);
    }
}
