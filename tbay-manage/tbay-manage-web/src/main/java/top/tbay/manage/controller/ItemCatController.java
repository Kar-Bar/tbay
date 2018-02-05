package top.tbay.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import top.tbay.manage.pojo.ItemCat;
import top.tbay.manage.service.ItemCatService;

import java.util.List;

/**
 * 商品类目
 * @author 571
 * @create 2017-11-22 11:01:48
 */
@RequestMapping("/item/cat")
@Controller
public class ItemCatController {

    private Logger logger = LoggerFactory.getLogger(ItemCatController.class);

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 由父节点id查询商品类目
     * @param parentId
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        ItemCat record;
        List<ItemCat> itemCats;
        try {
            record = new ItemCat();
            record.setParentId(parentId);
            itemCats = this.itemCatService.queryItemCategroyList(record);
            if(itemCats == null || itemCats.isEmpty()){
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCats);
        }catch (Exception e){
            logger.info("查询商品类目异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
