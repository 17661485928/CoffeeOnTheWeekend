//package com.coffee.kafeisummary.utils;
//
//import org.apache.zookeeper.WatchedEvent;
//import org.apache.zookeeper.Watcher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author kafei
// * @Title:
// * @Package
// * @Description:
// * @date 2020/7/3 15:09
// */
//public class WatcherApi implements Watcher {
//    private static final Logger logger = LoggerFactory.getLogger(WatcherApi.class);
//
//    @Override
//    public void process(WatchedEvent event) {
//        logger.info("【Watcher监听事件】={}",event.getState());
//        logger.info("【监听路径为】={}",event.getPath());
//        logger.info("【监听的类型为】={}",event.getType()); //  三种监听类型： 创建，删除，更新
//    }
//}
