package top.tbay.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tbay.manage.mapper.ItemCatMapper;
import top.tbay.manage.pojo.ItemCat;

import java.util.List;

/**
 * @author 571
 * @create 2017-11-22 13:44:12
 */
@Service
public class ItemCatService extends BaseService<ItemCat>{

    @Autowired
    private ItemCatMapper itemCatMapper;

//    public List<ItemCat> queryItemCategroyList(ItemCat record) {
//        return itemCatMapper.select(record);
//    }
}
