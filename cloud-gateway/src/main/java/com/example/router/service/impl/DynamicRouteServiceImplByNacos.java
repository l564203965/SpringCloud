package com.example.router.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.example.router.config.NacosGatewayProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author: ljy  Date: 2020/9/17.
 */
@Component
public class DynamicRouteServiceImplByNacos implements CommandLineRunner {

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    @Autowired
    private NacosGatewayProperties nacosGatewayProperties;

    /**
     * 监听Nacos Server下发的动态路由配置
     */
    public void dynamicRouteByNacosListener (){
        try {
            ConfigService configService= NacosFactory.createConfigService(nacosGatewayProperties.getAddress());
            String content = configService.getConfig(nacosGatewayProperties.getDataId(), nacosGatewayProperties.getGroupId(), nacosGatewayProperties.getTimeout());
            System.out.println(content);
            configService.addListener(nacosGatewayProperties.getDataId(), nacosGatewayProperties.getGroupId(), new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    List<RouteDefinition> list = JSON.parseArray(configInfo, RouteDefinition.class);
                    list.forEach(definition->{
                        if (2 <= definition.getPredicates().size()) {
                            // 遍历条件分别设置
                            definition.getPredicates().forEach(predicate ->{
                                RouteDefinition definitionCopy = new RouteDefinition();
                                BeanUtils.copyProperties(definition, definitionCopy);
                                List<PredicateDefinition> listCopy = new ArrayList<>();
                                listCopy.add(predicate);
                                definitionCopy.setPredicates(listCopy);
                                // 防止id重复
                                definitionCopy.setId(definitionCopy.getId() + definition.getPredicates().indexOf(predicate));
                                if (predicate.getArgs().get("pattern").contains(",")) {
                                    parsArgs(definitionCopy,predicate.getArgs().get("pattern"));
                                } else {
                                    dynamicRouteService.update(definitionCopy);
                                }
                            });
                        } else if (definition.getPredicates().get(0).getArgs().get("pattern").contains(",")) {
                            parsArgs(definition,definition.getPredicates().get(0).getArgs().get("pattern"));
                        } else {
                            dynamicRouteService.update(definition);
                        }
                    });
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    private void parsArgs(RouteDefinition definition,String args) {
        if (args.contains(",")) {
            String[] argsArray = args.split(",");
            List<String> argsList = Arrays.asList(argsArray);
            argsList.forEach(arg -> {
                RouteDefinition definitionCopy = new RouteDefinition();
                BeanUtils.copyProperties(definition, definitionCopy);
                Map<String,String> map = new HashMap<>();
                map.put("pattern",arg);

                // PredicateDefinition防止重复引用
                PredicateDefinition predicateCopy = new PredicateDefinition();
                BeanUtils.copyProperties(definitionCopy.getPredicates().get(0), predicateCopy);
                List<PredicateDefinition> listCopy = new ArrayList<>();
                listCopy.add(predicateCopy);
                predicateCopy.setArgs(map);
                definitionCopy.setPredicates(listCopy);

                // 防止id重复
                definitionCopy.setId(definitionCopy.getId() + argsList.indexOf(arg));
                dynamicRouteService.update(definitionCopy);
            });
        }
    }

    @Override
    public void run(String... args) throws Exception {
        dynamicRouteByNacosListener();

    }

}
