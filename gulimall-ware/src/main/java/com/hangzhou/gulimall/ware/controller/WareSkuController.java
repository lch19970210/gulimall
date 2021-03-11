package com.hangzhou.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hangzhou.gulimall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hangzhou.gulimall.ware.entity.WareSkuEntity;
import com.hangzhou.gulimall.ware.service.WareSkuService;
import com.hangzhou.common.utils.PageUtils;
import com.hangzhou.common.utils.R;



/**
 * 商品库存
 *
 * @author linchenghui
 * @email linchenghui@gmail.com
 * @date 2021-02-10 14:29:23
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping("/hasstock")
    public R<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> vos =  wareSkuService.getSkusHasStock(skuIds);
        R ok = new R();
        ok.put("data",vos);
        return ok;
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
