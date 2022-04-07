package com.xinlvyao.managercontroller;

import com.google.gson.Gson;
import com.xinlvyao.commons.EasyUIDatagrid;
import com.xinlvyao.commons.EasyUITree;
import com.xinlvyao.commons.FtpUtil;
import com.xinlvyao.commons.JdResult;
import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbItemCat;
import com.xinlvyao.pojo.TbItemDesc;
import com.xinlvyao.pojo.TbItemParamItem;
import com.xinlvyao.managerservice.TbItemService;
import com.xinlvyao.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 提供后台全部商品中的各个操作（全部商品，新增商品，上架商品，下架商品，删除商品）的Controller
 */
@RestController//该注解中包含了@ResponseBody,表示该类中所有的方法都将响应json数据到页面
public class TbItemController {

    //注入本地接口对象
    @Autowired
    private TbItemService tbItemService;
    //注入消息发送者对象，用来向消息队列发送消息
    @Autowired
    private Sender sender;

    /**
     *查询Tb_Item表中所有的数据并使用分页
     * @param page 页码
     * @param rows 每页显示数据条数
     * @return
     */
    @RequestMapping("/item/list")
    public EasyUIDatagrid getTbItemList(Integer page,Integer rows){
        //获取Tb_Item表中数据总数
        long totalTbItem = tbItemService.selectTbItemCount();
        //获取Tb_Item表中所有的数据并使用分页
        List<TbItem> TbItems = tbItemService.selectAllTbItem(page, rows);
        //封装并返回响应数据
        return new EasyUIDatagrid(totalTbItem,TbItems);
    }

    /**
     * 根据商品id下架商品,状态码(1-正常，2-下架，3-删除)
     * @param ids 是需要修改状态的id
     */
    @PostMapping("/rest/item/instock")
    public JdResult inStockTbItem(long[] ids){
        //根据商品id下架商品
        int n = tbItemService.downUpdateTbItem(ids,2,new Date());
        //封装响应数据
        JdResult jdResult = new JdResult();
        //成功
        if (n>0){
            jdResult.setStatus(200);
            jdResult.setMsg("下架成功");
            jdResult.setData(200);
        }else {
            //失败
            jdResult.setStatus(400);
            jdResult.setMsg("下架失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 根据商品id上架商品,状态码(1-正常，2-下架，3-删除)
     * @param ids 是需要修改状态的id
     */
    @PostMapping("/rest/item/reshelf")
    public JdResult reShelfTbItem(long[] ids){
        //根据商品id上架商品
        int n = tbItemService.upUpdateTbItem(ids,1,new Date());
        //封装响应数据
        JdResult jdResult = new JdResult();
        //成功
        if (n>0){
            jdResult.setStatus(200);
            jdResult.setMsg("上架成功");
            jdResult.setData(200);
        }else {
            //失败
            jdResult.setStatus(400);
            jdResult.setMsg("上架失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 根据商品id删除商品,状态码(1-正常，2-下架，3-删除)
     * @param ids 是需要修改状态的id
     */
    @PostMapping("/rest/item/delete")
    public JdResult deleteTbItem(long[] ids){
        //根据商品id删除商品
        int n = tbItemService.deleteUpdateTbItem(ids,3,new Date());
        //封装响应数据
        JdResult jdResult = new JdResult();
        //成功
        if (n>0){
            jdResult.setStatus(200);
            jdResult.setMsg("删除成功");
            jdResult.setData(200);
        }else {
            //失败
            jdResult.setStatus(400);
            jdResult.setMsg("删除失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 新增商品时的类目选择
     * @param id  @RequestParam(defaultValue = "0") 表示请求中没有传id值的时候使用默认值0
     */
    @PostMapping("/item/cat/list")
    public List<EasyUITree> getCatList(@RequestParam(defaultValue = "0") long id){
        //传入id，查询parentId等于id的所有的商品
        List<TbItemCat> tbItemCats = tbItemService.selectCat(id);
        //集合封装要响应到页面的数据
        List<EasyUITree> easyUITreeList = new ArrayList<>();
        for (TbItemCat tbItemCat:tbItemCats){
            EasyUITree easyUITree = new EasyUITree();
            easyUITree.setId(tbItemCat.getId());
            easyUITree.setText(tbItemCat.getName());
            Byte isParent = tbItemCat.getIsParent();
            //"closed"表示有子目录则收起子目录标签；"open"无子目录，直接展示
            easyUITree.setState(isParent==1?"closed":"open");
            easyUITreeList.add(easyUITree);
        }
        return easyUITreeList;
    }

    /**
     * 图片文件上传
     * @param uploadFile：上传的文件名
     * @return
     */
    @RequestMapping("/pic/upload")
    public Map<String,Object> uploadPic(MultipartFile uploadFile){
        //获取文件原始名
        String originalFilename = uploadFile.getOriginalFilename();
        //文件名唯一化处理（防止文件名重名）
        String newPicName = UUID.randomUUID()+originalFilename;
        //将文件上传到文件服务器（vsftpd）
        InputStream in = null;
        boolean b = false;
        try {
            in = uploadFile.getInputStream();
            b = FtpUtil.uploadFile("192.168.175.129", 21, "ftpuser", "ftpuser", "/home/ftpuser", "/images", newPicName, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //封装响应数据
        Map<String,Object> map = new HashMap<>();
        if (b){
            //上传成功，返回图片回显url
            map.put("error",0);
            map.put("url","http://192.168.175.129:9996/"+newPicName);
        }else {
            //上传失败，提示错误
            map.put("error",1);
            map.put("message", "图片上传失败");
        }
        return map;
    }

    /**
     * 提交新增商品（提交商品数据到TbItem表，提交商品描述到TbItemDesc表，提交商品参数到TbItemParamItem表）
     * @param title：商品标题
     * @param sellPoint：商品卖点
     * @param price：商品价格
     * @param num：商品库存
     * @param barcode：商品条形码
     * @param image：商品图片路径
     * @param cid：商品所属类目id
     * @param desc：商品描述
     * @param itemParams:商品参数
     * @return
     */
    @RequestMapping("/item/save")
    public JdResult saveProduct(String title,String sellPoint, Long price, Integer num,String barcode,String image,Long cid,String desc,String itemParams){
        //商品图片路径在desc字段中，没有直接从image中提交，所以需要从desc中获取图片路径，并将路径赋值给image
        int index = desc.indexOf("src=");
        int index1 = desc.indexOf("alt=");
        image = desc.substring(index + 5, index1 - 2);
        //TbItem tbItem = new TbItem()封装新增商品的数据
        long itemId = System.currentTimeMillis() + new Random().nextInt(999);
        TbItem tbItem = new TbItem();
        tbItem.setId(itemId);
        tbItem.setTitle(title);
        tbItem.setSellPoint(sellPoint);
        tbItem.setPrice(price);
        tbItem.setNum(num);
        tbItem.setBarcode(barcode);
        tbItem.setImage(image);
        tbItem.setCid(cid);
        tbItem.setStatus((byte)1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //封装新增商品的描述
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        //封装新增商品的参数信息
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(new Date());
        tbItemParamItem.setUpdated(new Date());
        //执行新增商品数据上传
        int n = tbItemService.insertProduct(tbItem,tbItemDesc,tbItemParamItem);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //新增成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("新增成功");
            jdResult.setData(200);
            //发送消息到消息队列，同步更新es索引库数据
            sender.updateEsItemMsg("updateEsItem","新增商品成功,更新es-"+new Gson().toJson(tbItem));
        }else {
            //新增失败
            jdResult.setStatus(400);
            jdResult.setMsg("新增失败");
            jdResult.setData(400);
        }
        return jdResult;
    }
}
