package top.tbay.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.tbay.manage.mapper.ItemDescMapper;
import top.tbay.manage.pojo.ItemDesc;

@Service
public class ItemDescService extends BaseService<ItemDesc> {

    @Autowired
    private ItemDescMapper itemDescMapper;
}
