package cn.jdimage.wechat.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.jdimage.wechat.util.CheckUtil;
import cn.jdimage.wechat.util.MessageUtil;

@RestController
public class WechatController {

	@Autowired
	private WechatService wechatService;

	/**
	 * 
	 * @param signature
	 *            微信后台传来的加密标签，用于与生成的标签比较
	 * @param timestamp
	 * @param nonce
	 *            随机数
	 * @param echostr
	 *            随机字符串
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String chat(String signature, String timestamp, String nonce, String echostr) {
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			return echostr;
		}
		return null;
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public String post(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Map<String, String> map = MessageUtil.xmlToMap(request);
			String toUserName = map.get("ToUserName");
			String fromUerName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");

			String message = null;
			if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				if ("1".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUerName, MessageUtil.firstMenu());
				} else if ("2".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUerName, MessageUtil.secondMenu());
				} else if ("?".equals(content) || "？".equals(content)) {
					message = MessageUtil.initText(toUserName, fromUerName, MessageUtil.menuText());
				}
			} else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
				String eventType = map.get("Event");
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)) {
					message = MessageUtil.initText(toUserName, fromUerName, MessageUtil.menuText());
				}
			}
			return message;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return "";
	}
}
