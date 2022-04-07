package com.xinlvyao.commons;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
/**
 * @description:发送短信的工具类,第三方平台:https://www.smschinese.com
 */

public class SendSms {
    //封装一个发送短信的方法
    public static void sendMessage(String phone,String msg)throws Exception{

        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("Uid", "sunwer173"),new NameValuePair("Key", "d41d8cd98f00b204e983"),new NameValuePair("smsMob",phone),new NameValuePair("smsText",msg)};
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:"+statusCode);
        for(Header h : headers)
        {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        System.out.println("状态: "+result); //打印返回消息状态
        post.releaseConnection();

    }

    public static void main(String[] args) throws Exception {
        //测试
        sendMessage("电话号","短信内容");
    }

}
