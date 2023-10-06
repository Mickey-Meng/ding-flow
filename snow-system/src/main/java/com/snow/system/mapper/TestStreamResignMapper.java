package com.snow.system.mapper;

import java.util.List;
import com.snow.system.domain.TestStreamResign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 工作流测试Mapper接口
 * 
 * @author Agee
 * @date 2022-11-30
 */
public interface TestStreamResignMapper extends BaseMapper<TestStreamResign>
{
    /**
     * 查询工作流测试
     * 
     * @param id 工作流测试ID
     * @return 工作流测试
     */
    public TestStreamResign selectTestStreamResignById(Long id);

    /**
     * 查询工作流测试列表
     * 
     * @param testStreamResign 工作流测试
     * @return 工作流测试集合
     */
    public List<TestStreamResign> selectTestStreamResignList(TestStreamResign testStreamResign);

    /**
     * 新增工作流测试
     * 
     * @param testStreamResign 工作流测试
     * @return 结果
     */
    public int insertTestStreamResign(TestStreamResign testStreamResign);

    /**
     * 修改工作流测试
     * 
     * @param testStreamResign 工作流测试
     * @return 结果
     */
    public int updateTestStreamResign(TestStreamResign testStreamResign);

    /**
     * 删除工作流测试
     * 
     * @param id 工作流测试ID
     * @return 结果
     */
    public int deleteTestStreamResignById(Long id);

    /**
     * 批量删除工作流测试
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTestStreamResignByIds(String[] ids);
}
