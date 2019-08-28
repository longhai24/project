package com.tianxing.springboot.action;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.util.security.Credential.MD5;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tianxing.springboot.util.HttpUtils;
@RestController
@RequestMapping("/user/api")
public class UserAction {
	private static String vcode;
	
	
	/*获取短信验证码*/
	public Map<String, String> getVerCode(String mobile){
		/*结果消息集*/
		Map<String, String> result=new Hashtable<>();
		/*随机短信验证码*/
		StringBuffer code=new StringBuffer();
		Random ra;
		synchronized(this){
			ra=new Random();
			for (int i = 0; i < 6; i++) {
				code.append(ra.nextInt(10));
			}
		}
		vcode=code.toString();
		String host = "http://dingxin.market.alicloudapi.com";
	    String path = "/dx/sendSms";
	    String method = "POST";
	    String appcode = "8f3b6b16fd4741d2be09c9b615c4cf24";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    Map<String, String> querys = new HashMap<String, String>();
	    querys.put("mobile", mobile);
	    querys.put("param", "code:"+code);
	    querys.put("tpl_id", "TP1711063");
	    Map<String, String> bodys = new HashMap<String, String>();
	    try {
	    	/**
	    	* 重要提示如下:
	    	* HttpUtils请从
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
	    	* 下载
	    	*
	    	* 相应的依赖请参照
	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
	    	*/
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	Map<String, String> map=JSON.parseObject(EntityUtils.toString(response.getEntity()), Map.class);
	    	if("00000".equals(map.get("return_code"))) 
	    	{
	    		result.put("vercode", MD5.digest(code.toString()).replaceAll("MD5:", ""));
	    	    result.put("msg", "发送成功");
	    	}
	    	else
	    	{
	    	    result.put("vercode", "500");
	    	    result.put("msg", "发送失败");
	    	}
	    	//System.out.println(response.toString());
	    	//获取response的body
	    	//System.out.println(EntityUtils.toString(response.getEntity()));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return result;
	}
	
	/*发送短信验证码到前台*/
	@PostMapping("/sendVerCode/{mobile}")
	public Map<String, String> sendVerCode(@PathVariable("mobile") String mobile){
		return this.getVerCode(mobile);
	};
	/*发送短信验证码到前台*/
	@PostMapping("/show")
	public void show(){
		System.out.println(vcode);
	};

}
