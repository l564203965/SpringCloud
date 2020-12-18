//package com.example.mq;
//
//import org.springframework.stereotype.Service;
//
///**
// * @author: ljy  Date: 2020/10/8.
// */
//@Service
//public class ReceiveService {
//    /**
//     * 默认是input，在Sink类中指定，如果想要多个input，需要写一个实现Sink的类
//     * @param receiveMsg
//     */
//    @StreamListener("input")
//    public void receiveInput1(String receiveMsg) {
//        System.out.println("input receive: " + receiveMsg);
//    }
//}
