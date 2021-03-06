package ${package.Service};


import ${groupId}.client.query.${entity}Query;
import ${groupId}.core.dao.${entity}DAO;
import ${groupId}.core.entity.${entity}DO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import tech.muyi.common.constant.enumtype.RowStatusEnum;
import tech.muyi.db.PageQueryHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
/**
* <p>
    * ${table.comment!}
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Component
public class ${entity}Manager  extends ServiceImpl<${entity}DAO, ${entity}DO>{

    @Resource
    private ${entity}DAO ${entity?uncap_first}DAO;

    public Boolean deleteById(Long id){
        return this.update(Wrappers.<${entity}DO>lambdaUpdate().set(${entity}DO::getRowStatus, RowStatusEnum.DELETE.getCode()).eq(${entity}DO::getId,id));
    }

    public Boolean update(${entity}DO ${entity?uncap_first}DO) {
        LambdaUpdateWrapper<${entity}DO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(${entity}DO::getId,${entity?uncap_first}DO.getId());
        return this.update(${entity?uncap_first}DO,updateWrapper);
    }
    
    public List<${entity}DO> query(${entity}Query ${entity?uncap_first}Query) {
        QueryWrapper<${entity}DO> queryWrapper = new QueryWrapper<>();
        ${entity}DO ${entity?uncap_first}DO = new ${entity}DO();
        BeanUtils.copyProperties(${entity?uncap_first}Query, ${entity?uncap_first}DO);
        queryWrapper.setEntity(${entity?uncap_first}DO);
        return this.list(queryWrapper); 
    }

    public List<${entity}DO> pageSelect(${entity}Query ${entity?uncap_first}Query){
        IPage<${entity}DO> page = new Page<>(${entity?uncap_first}Query.getCurrent(),${entity?uncap_first}Query.getSize());
        QueryWrapper<${entity}DO> lambdaQueryWrapper = new QueryWrapper<>();
        ${entity}DO ${entity?uncap_first}DO = new ${entity}DO();
        BeanUtils.copyProperties(${entity?uncap_first}Query, ${entity?uncap_first}DO);
        lambdaQueryWrapper.setEntity(${entity?uncap_first}DO);
        this.page(page);
        PageQueryHelper.queryPageConfig(page,${entity?uncap_first}Query);
        return page.getRecords(); 
    }

    public Long queryCount(${entity}Query ${entity?uncap_first}Query) {
        LambdaQueryWrapper<${entity}DO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        ${entity}DO ${entity?uncap_first}DO = new ${entity}DO();
        BeanUtils.copyProperties(${entity?uncap_first}Query, ${entity?uncap_first}DO);
        lambdaQueryWrapper.setEntity(${entity?uncap_first}DO);
        Integer count =  this.count(lambdaQueryWrapper);
        return count.longValue();
    }
}