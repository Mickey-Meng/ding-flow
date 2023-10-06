package com.snow.system.service.impl;

import java.util.List;
import java.util.ArrayList;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snow.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import com.snow.system.mapper.TestStreamResignMapper;
import com.snow.system.domain.TestStreamResign;
import com.snow.system.service.ITestStreamResignService;
import com.snow.common.core.text.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 工作流测试Service业务层处理
 *
 * @author Agee
 * @date 2022-11-30
 */
@Service
public class TestStreamResignServiceImpl extends ServiceImpl<TestStreamResignMapper, TestStreamResign> implements ITestStreamResignService {
    @Resource
    private TestStreamResignMapper testStreamResignMapper;

    /**
     * 查询工作流测试
     *
     * @param id 工作流测试ID
     * @return 工作流测试
     */
    @Override
    public TestStreamResign selectTestStreamResignById(Long id) {
        return testStreamResignMapper.selectById(id);
    }

    /**
     * 查询工作流测试列表
     *
     * @param testStreamResign 工作流测试
     * @return 工作流测试
     */
    @Override
    public List<TestStreamResign> selectTestStreamResignList(TestStreamResign testStreamResign) {
        LambdaQueryWrapper<TestStreamResign> lambda = new QueryWrapper<TestStreamResign>().lambda();
        lambda.like(ObjectUtil.isNotEmpty(testStreamResign.getName()),TestStreamResign::getName,testStreamResign.getName());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getReason()),TestStreamResign::getReason,testStreamResign.getReason());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getTransitionPerson()),TestStreamResign::getTransitionPerson,testStreamResign.getTransitionPerson());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getResignTime()),TestStreamResign::getResignTime,testStreamResign.getResignTime());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getProcessStatus()),TestStreamResign::getProcessStatus,testStreamResign.getProcessStatus());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getApplyPerson()),TestStreamResign::getApplyPerson,testStreamResign.getApplyPerson());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getIsDelete()),TestStreamResign::getIsDelete,testStreamResign.getIsDelete());
        lambda.eq(ObjectUtil.isNotEmpty(testStreamResign.getFileUrl()),TestStreamResign::getFileUrl,testStreamResign.getFileUrl());
        return testStreamResignMapper.selectList(lambda);
    }

    /**
     * 新增工作流测试
     *
     * @param testStreamResign 工作流测试
     * @return 结果
     */
    @Override
    public int insertTestStreamResign(TestStreamResign testStreamResign) {
        testStreamResign.setCreateTime(DateUtils.getNowDate());
        return testStreamResignMapper.insert(testStreamResign);
    }

    /**
     * 修改工作流测试
     *
     * @param testStreamResign 工作流测试
     * @return 结果
     */
    @Override
    public int updateTestStreamResign(TestStreamResign testStreamResign) {
        testStreamResign.setUpdateTime(DateUtils.getNowDate());
        return testStreamResignMapper.updateById(testStreamResign);
    }

    /**
     * 删除工作流测试对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteTestStreamResignByIds(String ids) {
        return testStreamResignMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除工作流测试信息
     *
     * @param id 工作流测试ID
     * @return 结果
     */
    @Override
    public int deleteTestStreamResignById(Long id) {
        return testStreamResignMapper.deleteById(id);
    }
}
