package top.tbay.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tbay.manage.mapper.ItemParamMapper;
import top.tbay.manage.pojo.EasyUIResult;
import top.tbay.manage.pojo.ItemParam;

import java.util.List;

/**
 * 商品规格
 * @author 571
 * @create 2018-2-8 11:03:15
 */
@Service
public class ItemParamService extends BaseService<ItemParam>{

    @Autowired
    private ItemParamMapper itemParamMapper;

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        Example example = new Example(ItemParam.class);
        example.setOrderByClause("updated DESC");
        PageHelper.startPage(page,rows);
        List<ItemParam> itemParams = itemParamMapper.selectByExample(example);
        PageInfo<ItemParam> pageInfo = new PageInfo<>(itemParams);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
