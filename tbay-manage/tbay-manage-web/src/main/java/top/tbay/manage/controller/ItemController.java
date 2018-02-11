package top.tbay.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import top.tbay.manage.pojo.*;
import top.tbay.manage.service.ItemCatService;
import top.tbay.manage.service.ItemDescService;
import top.tbay.manage.service.ItemParamService;
import top.tbay.manage.service.ItemService;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品类
 * @author 571
 * @create 2018-2-7 10:25:05
 */
@RequestMapping("/item")
@Controller
public class ItemController {

    private Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemCatService itemCatService;

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamService itemParamService;

    /**
     * 商品类目——查询商品类目列表
     * @param parentId
     */
    @RequestMapping(value = "/cat", method = RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
        ItemCat record;
        List<ItemCat> itemCats;
        try {
            record = new ItemCat();
            record.setParentId(parentId);
            itemCats = itemCatService.queryListByWhere(record);
            if(itemCats == null || itemCats.isEmpty()){
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemCats);
        }catch (Exception e){
            logger.info("查询商品类目异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品——新增商品数据
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc")String desc){
        logger.info("新增商品：item={},desc={}",item,desc);
        try {
            this.itemService.saveItem(item,desc);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.info("商品数据发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品——查询商品列表(倒叙)
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(@RequestParam(value="page", defaultValue="1")Integer page,
                                                      @RequestParam(value="rows", defaultValue="30")Integer rows){
        try {
            EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            logger.info("查询商品列表发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品——查询商品描述
     * @param itemid
     * @return
     */
    @RequestMapping(value = "/desc/{itemid}", method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemDesc(@PathVariable("itemid")Long itemid){
        logger.info("商品id：{}",itemid);
        if(null == itemid){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            ItemDesc itemDesc = itemDescService.queryById(itemid);
            if(null == itemDesc){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            logger.info("查询商品描述发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品——删除商品内容
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ResponseEntity<Void> deleteItem(@RequestParam("ids")String ids){
        if(StringUtils.isEmpty(ids)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            List<Long> idArr = splitMutilId(ids);
            if(idArr.size() > 0){
                for(int i = 0; i < idArr.size(); i++){
                    Long itemId = idArr.get(i);
                    Item item = this.itemService.queryById(itemId);
                    if(null == item){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                    this.itemService.deleteById(itemId);
                }
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            logger.info("删除商品内容发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品——商品修改更新
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc")String desc){
        try {
            this.itemService.updateItem(item,desc);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            logger.info("商品修改更新发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 商品规格参数模板——查询商品规格参数模板
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "/param/query/{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryItemParam(@PathVariable("itemCatId")Long itemCatId){
        if(null == itemCatId){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            ItemParam record = new ItemParam();
            ItemParam itemParam = itemParamService.queryOne(record);
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            logger.info("获取商品规格参数模板发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品规格参数模板——检测是否已存在
     * @return
     */
    @RequestMapping(value = "/param/check/{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> checkItemParam(@PathVariable("itemCatId")Long itemCatId){
        if(null == itemCatId){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            ItemParam param = new ItemParam();
            param.setItemCatId(itemCatId);
            ItemParam itemParam = this.itemParamService.queryOne(param);
            if (null == itemParam) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            logger.info("检测商品规格参数模板发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品规格参数模板——新增模板数据
     *
     * @param itemParam
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "/param/add/{itemCatId}", method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(ItemParam itemParam, @PathVariable("itemCatId") Long itemCatId) {
        try {
            itemParam.setItemCatId(itemCatId);
            itemParam.setId(null);
            this.itemParamService.save(itemParam);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.info("新增商品规格参数模板发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 商品规格参数模板——查询商品规格列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/param/list", method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParamList(@RequestParam(value="page", defaultValue="1")Integer page,
                                                           @RequestParam(value="rows", defaultValue="30")Integer rows){
        try {
            EasyUIResult easyUIResult = this.itemParamService.queryItemList(page, rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            logger.info("查询商品列表发生异常，{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 商品规格参数模板——删除商品模板
     * @param ids
     * @return
     */
    @RequestMapping(value = "/param/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteItemParam(@RequestParam("ids")String ids){
        if(StringUtils.isEmpty(ids)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            List<Long> idArr = splitMutilId(ids);
            if(idArr.size() > 0){
                for(int i = 0; i < idArr.size(); i++){
                    Long itemParamId = idArr.get(i);
                    ItemParam itemParam = this.itemParamService.queryById(itemParamId);
                    if(null == itemParam){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                    this.itemParamService.deleteById(itemParamId);
                }
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            logger.info("删除ids:{}的商品模板发生异常:{}",ids,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 多id切分
     * @param ids
     */
    private List<Long> splitMutilId(String ids) {
        List<Long> list = new ArrayList<>();
        try {
            String[] split = ids.split(",");
            for(int i = 0; i < split.length; i++){
                String s = split[i];
                if(StringUtils.isEmpty(s)){
                    continue;
                }
                list.add(Long.parseLong(s));
            }
        } catch (Exception e) {
            logger.info("切分id：{}发生异常：{}",ids,e);
        }
        return list;
    }
}
