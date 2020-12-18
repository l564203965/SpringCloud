/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.user.service.impl;

import com.example.core.base.BaseService;
import com.example.core.utils.PublicUtil;
import com.example.security.context.PasswordHandler;
import com.example.security.entity.SOSUserDetails;
import com.example.core.entity.User;
import com.example.user.entity.SqlTableExtends;
import com.example.user.entity.vo.UserVo;
import com.example.user.mapper.UserMapper;
import com.example.user.service.IUserService;
import org.apache.ibatis.jdbc.SqlBuilder;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.insert.BatchInsertDSL;
import org.mybatis.dynamic.sql.insert.render.BatchInsert;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class UserServiceImpl extends BaseService<User> implements IUserService {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserVo getUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return conversionVo(user);
    }

    @Override
    public UserVo getUserByName(String name) {
        logger.info("name=========>"+name);
        User user = userMapper.getUserByName(name);
        return conversionVo(user);
    }

    @Override
    public SOSUserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        logger.info("loadUserByUsername,name=========>"+name);
        User user = userMapper.getUserByName(name);
        return new SOSUserDetails(user);
    }

    @Override
    public void addUser(User user) {
        // 批量插入值
        /*List<Map<String, Object>> values;
        SqlSession session = null;
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        CtsdbMapper mapper = null;
        SqlTable sqlTable = new SqlTableExtends("targetTable");
        BatchInsert<Map<String, Object>> insertStatement = null;
        // 批量插入列
        List<SqlColumn<Map<String, Object>>> sqlColumns = new ArrayList<>();

        targetFieldList.forEach(field -> {
            // 定义插入表及列名
            sqlColumns.add(new SqlColumn.Builder().withTable(sqlTable).withName(field).build());
        });
        while (!CollectionUtils.isEmpty(ctsdbResult.getHits().getHits())) {
            values = getFieldsAndValuesSort(fieldMappingMap, ctsdbResult, tableName);
            if (!CollectionUtils.isEmpty(values)) {
                // 手动开启事务
                transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
                try {
                    // 批量插入到mysql
                    // ctsdbMapper.dynamicInsertValues(insertParam);

                    // 使用Batch Insert Support提高批量插入效率
                    session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
                    mapper = session.getMapper(CtsdbMapper.class);

                    // 传入插入值
                    BatchInsertDSL<Map<String, Object>> sdl = SqlBuilder.insert(values).into(sqlTable);
                    sqlColumns.forEach(sqlColumn -> {
                        // 定义插入列名
                        sdl.map(sqlColumn).toProperty(sqlColumn.name());
                    });

                    sdl.build().render(RenderingStrategy.MYBATIS3).insertStatements().forEach(mapper::batchInsert);
                    session.commit();
                } catch (Exception e) {
                    // 手动回滚事务
                    dataSourceTransactionManager.rollback(transactionStatus);
                    throw e;
                } finally {
                    if (session != null) {
                        session.clearCache();
                        session.close();
                    }
                }
                // 手动提交事务
                dataSourceTransactionManager.commit(transactionStatus);
                values.clear();
            }
        }*/
        user.setPassword(PasswordHandler.encode(user.getPassword()));
        userMapper.insert(user);

    }

    @Override
    public void updateUserById(User user) {
        // 手动开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            user.setPassword(PasswordHandler.encode(user.getPassword()));
            userMapper.updateByPrimaryKey(user);
        } catch (Exception e) {
            // 手动回滚事务
            dataSourceTransactionManager.rollback(transactionStatus);
            throw e;
        }
        // 手动提交事务
        dataSourceTransactionManager.commit(transactionStatus);
    }

    private UserVo conversionVo(User user) {
        UserVo userVo = null;
        if (PublicUtil.isNotEmpty(user)) {
            userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
        }
        return userVo;
    }
}
