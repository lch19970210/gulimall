package com.hangzhou.gulimall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.hangzhou.gulimall.product.config.MyThreadConfig;
import com.hangzhou.gulimall.product.config.ThreadPoolConfigProperties;
import com.hangzhou.gulimall.product.dao.AttrGroupDao;
import com.hangzhou.gulimall.product.entity.BrandEntity;
import com.hangzhou.gulimall.product.service.BrandService;
import com.hangzhou.gulimall.product.service.CategoryService;
import com.hangzhou.gulimall.product.vo.SkuItemVo;
import com.hangzhou.gulimall.product.vo.SpuItemAttrGroupVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

//    @Autowired
//    OSSClient ossClient;

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient RedissonClient;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Test
    public void IoCTest(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ThreadPoolConfigProperties.class);
        applicationContext.refresh();
        ThreadPoolConfigProperties bean = applicationContext.getBean(ThreadPoolConfigProperties.class);
        System.out.println(bean);
    }

    @Test
    public void getAttrGroupWithAttrsBySpuId(){
        List<SpuItemAttrGroupVo> vos = attrGroupDao.getAttrGroupWithAttrsBySpuId(4L, 225L);
        System.out.println(vos);
    }

    @Test
    public void Redisson(){
        System.out.println(RedissonClient);
    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        System.out.println(Arrays.asList(catelogPath));
    }

//    @Test
//    void testUpload() throws FileNotFoundException {
////        // Endpoint以杭州为例，其它Region请按实际情况填写。
////        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
////        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
////        String accessKeyId = "LTAI4G518dGevkoShoyoH5RT";
////        String accessKeySecret = "gqBv7tw5iOvrfsXWmlkb0As9hmCb9W";
////
////        // 创建OSSClient实例。
////        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        String bucketName = "gulimall-202102";
//        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
//        String objectName = "mbp16touch.jpeg";
//
//        // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
//        InputStream inputStream = new FileInputStream("/Users/faye/Tools/gulimall/oss/mbp16touch.jpeg");
//        ossClient.putObject(bucketName, objectName, inputStream);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        System.out.println("success");
//    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("测试成功...");
    }


}
