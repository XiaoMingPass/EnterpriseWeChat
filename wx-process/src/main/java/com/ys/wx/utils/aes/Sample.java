package com.ys.wx.utils.aes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class Sample {
/*
    注意事项
		1.com\qq\weixin\mp\aes目录下是用户需要用到的接入企业微信的接口，其中WXBizMsgCrypt.java文件提供的WXBizMsgCrypt类封装了用户接入企业微信的三个接口，
		其它的类文件用户用于实现加解密，用户无须关心。sample.java文件提供了接口的使用求例。
		2.WXBizMsgCrypt封装了VerifyURL, DecryptMsg, EncryptMsg三个接口，分别用于开发者验证回调url，
		收到用户回复消息的解密以及开发者回复消息的加密过程。使用方法可以参考Sample.java文件。
		3.加解密协议请参考企业微信官方文档。
		4.请开发者使用jdk1.6以上的版本。针对org.apache.commons.codec.binary.Base64，需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本），
		我们有提供，官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
		****请特别注意******
		5.异常java.security.InvalidKeyException:illegal Key Size的解决方案：
		在官方网站下载JCE无限制权限策略文件
		（JDK7的下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
		  JDK8的下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html）
		下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt。
		如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件，
		如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件
*/

    public static void main(String[] args) throws Exception {

        String sToken = "QDG6eK";// 可由企业任意填写，用于生成签名
        String sCorpID = "wx5823bf96d3bd56c7";// 企业号的CorpID
        String sEncodingAESKey = "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C";//用于消息体的加密，是AES密钥的Base64编码

        String KK = "PHhtbD4KPEVuY3J5cHQ+PCFbQ0RBVEFbemlxV0I2WXlVeld4M3JrNm5UNW1neDF0OHlJZVpBYnVIeTQzYmVhNS9ZQTdXZlZ4QmxLWmZkV3VSVzFDR3VzSU5xL042U2g2YjZmTHVDbUptZGRFSVRuTTlUdEova25HU251Q201WWFpL2JZalVBeFh6WTFaMk5SUWZkY01XdWZUdmJrR0x3RkZQWGFVM1BJRWcrRnVVM2RieW8xYWtRdTVvWjJyVy8rNDA5ZDRaWHhtVkdwaXh6VHYrdStJZFVITVpFSG9wWFVIZkppMy83T1IwRXhrVFJWS1hXNE8yMzN5cDdjdjRTSkd2ZUJBbk9UbjRSdUFyWW5EY3NYdkg2L0NIYm5WQlgrTjlYRTJHUG91Skd6b21kUm9WeVFZLzNEZkx6eUxYc3RvU2FLWFhYb3BTeS9ZZzhXM3pKNVplbDd1TDJRTEk0U0d2Zmxsb0xRK282NGs2MzA1eWdFcGR0K3RpN21hdEVvN09KV0pmR1RoVWdkcnd6OEZMZEhNL2kzbzE1bk1nTHRoOXVYTWF6eUJNLzBqK3VUTVNCTzdNb1NFNWZBdjhxNWtEUVA0MzIzUVBuZHc2QncveEhreFBaRXoxdzl0cU9MZTNFcS9hTlQzSmdZbmc9PV1dPjwvRW5jcnlwdD4KPE1zZ1NpZ25hdHVyZT48IVtDREFUQVtlNWRjNDRmZjRiOTcxNTM4OTk1YWRiMWI2MjQ0ODM3N2YwMzczYjRkXV0+PC9Nc2dTaWduYXR1cmU+CjxUaW1lU3RhbXA+MTQwOTY1OTgxMzwvVGltZVN0YW1wPgo8Tm9uY2U+PCFbQ0RBVEFbMTM3MjYyMzE0OV1dPjwvTm9uY2U+CjwveG1sPg==";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);


        /*
        ------------使用示例一：验证回调URL---------------
		*企业开启回调模式时，企业号会向验证url发送一个get请求
		假设点击验证时，企业收到类似请求：
		* GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
		* HTTP/1.1 Host: qy.weixin.qq.com

		接收到该请求时，企业应
		1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及公众平台推送过来的随机加密字符串(echostr),这一步注意作URL解码。
		2.验证消息体签名的正确性
		3.解密出echostr原文，将原文当作Get请求的response，返回给公众平台
		第2，3步可以用公众平台提供的库函数VerifyURL来实现。

		*/

//        // 解析出url上的参数值如下：
//        // String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
//        String sVerifyMsgSig = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
//        // String sVerifyTimeStamp = HttpUtils.ParseUrl("timestamp");
//        String sVerifyTimeStamp = "1409659589";
//        // String sVerifyNonce = HttpUtils.ParseUrl("nonce");
//        String sVerifyNonce = "263014780";
//        // String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");
//        String sVerifyEchoStr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";
//        String sEchoStr; //需要返回的明文
//        try {
//            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
//                    sVerifyNonce, sVerifyEchoStr);
//            System.out.println("verifyurl echostr: " + sEchoStr);
//            // 验证URL成功，将sEchoStr返回
//            // HttpUtils.SetResponse(sEchoStr);
//        } catch (Exception e) {
//            //验证URL失败，错误原因请查看异常
//            e.printStackTrace();
//        }

		/*
        ------------使用示例二：对用户回复的消息解密---------------
		用户回复消息或者点击事件响应时，企业会收到回调消息，此消息是经过公众平台加密之后的密文以post形式发送给企业，密文格式请参考官方文档
		假设企业收到公众平台的回调消息如下：
		POST /cgi-bin/wxpush? msg_signature=477715d11cdb4164915debcba66cb864d751f3e6&timestamp=1409659813&nonce=1372623149 HTTP/1.1
		Host: qy.weixin.qq.com
		Content-Length: 613
		<xml>		<ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName><Encrypt><![CDATA[RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt>
		<AgentID><![CDATA[218]]></AgentID>
		</xml>

		企业收到post请求之后应该
		1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce)
		2.验证消息体签名的正确性。
		3.将post请求的数据进行xml解析，并将<Encrypt>标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档
		第2，3步可以用公众平台提供的库函数DecryptMsg来实现。
		*/

        // String sReqMsgSig = HttpUtils.ParseUrl("msg_signature");
        String sReqMsgSig = "477715d11cdb4164915debcba66cb864d751f3e6";
        // String sReqTimeStamp = HttpUtils.ParseUrl("timestamp");
        String sReqTimeStamp = "1409659813";
        // String sReqNonce = HttpUtils.ParseUrl("nonce");
        String sReqNonce = "1372623149";
        // post请求的密文数据
        // sReqData = HttpUtils.PostData();
        String sReqData = "<xml><ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName><Encrypt><![CDATA[RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt><AgentID><![CDATA[218]]></AgentID></xml>";

        try {
            String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
            System.out.println("after decrypt msg: " + sMsg);
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Content");
            String Content = nodelist1.item(0).getTextContent();
            System.out.println("Content：" + Content);

        } catch (Exception e) {
            // TODO
            // 解密失败，失败原因请查看异常
            e.printStackTrace();
        }

		/*
        ------------使用示例三：企业回复用户消息的加密---------------
		企业被动回复用户的消息也需要进行加密，并且拼接成密文格式的xml串。
		假设企业需要回复用户的明文如下：
		<xml>
		<ToUserName><![CDATA[mycreate]]></ToUserName>
		<FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName>
		<CreateTime>1348831860</CreateTime>
		<MsgType><![CDATA[text]]></MsgType>
		<Content><![CDATA[this is a test]]></Content>
		<MsgId>1234567890123456</MsgId>
		<AgentID>128</AgentID>
		</xml>

		为了将此段明文回复给用户，企业应：
		1.自己生成时间时间戳(timestamp),随机数字串(nonce)以便生成消息体签名，也可以直接用从公众平台的post url上解析出的对应值。
		2.将明文加密得到密文。
		3.用密文，步骤1生成的timestamp,nonce和企业在公众平台设定的token生成消息体签名。
		4.将密文，消息体签名，时间戳，随机数字串拼接成xml格式的字符串，发送给企业。
		以上2，3，4步可以用公众平台提供的库函数EncryptMsg来实现。
		*/

//        String sRespData = "<xml><ToUserName><![CDATA[mycreate]]></ToUserName><FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId><AgentID>128</AgentID></xml>";
//        try {
//            String sEncryptMsg = wxcpt.EncryptMsg(sRespData, sReqTimeStamp, sReqNonce);
//            System.out.println("after encrypt sEncrytMsg: " + sEncryptMsg);
//            // 加密成功
//            // TODO:
//            // HttpUtils.SetResponse(sEncryptMsg);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 加密失败
//        }
    }
}
