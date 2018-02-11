package top.tbay.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tbay.manage.mapper.ItemMapper;
import top.tbay.manage.pojo.EasyUIResult;
import top.tbay.manage.pojo.Item;
import top.tbay.manage.pojo.ItemDesc;

import java.util.List;

/**
 * @author 571
 * @create 2018-2-6 14:39:37
 */
@Service
public class ItemService extends BaseService<Item>{

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemMapper itemMapper;

    /**
     * 保存商品数据
     * @param item
     * @param desc
     */
    public void saveItem(Item item, String desc) throws Exception {
        //设置初始数据
        item.setStatus(1);
        //强制置空，防止漏洞
        item.setId(null);
        //保存商品数据
        super.save(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        //保存商品描述
        this.itemDescService.save(itemDesc);
    }

    /**
     * 查询商品数据(倒叙)
     * @param page
     * @param rows
     * @return
     */
    public EasyUIResult queryItemList(Integer page, Integer rows) {
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        PageHelper.startPage(page,rows);
        List<Item> items = itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<>(items);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 商品修改更新
     * @param item
     * @param desc
     */
    public void updateItem(Item item, String desc)throws Exception {
        //强制设置不能修改字段为null TODO
        item.setStatus(null);
        super.updateSelective(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);
    }
}
