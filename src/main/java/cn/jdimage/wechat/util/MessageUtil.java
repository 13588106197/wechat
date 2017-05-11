package cn.jdimage.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import cn.jdimage.wechat.model.TextMessage;

public class MessageUtil {

	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";// 关注
	public static final String MESSAGE_UNSUBSRIBE = "unsubscribe";// 未关注
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";

	/**
	 * xml转为map集合
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
		Map<String, String> map = new HashMap<>();
		SAXReader reader = new SAXReader();
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}

	/**
	 * 将文本消息对象转换为xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", textMessage.getClass());// 一定要将根标签改为<xml></xml>，不然会是类目录
		return xStream.toXML(textMessage);
	}

	/**
	 * 主菜单内容
	 * 
	 * @return
	 */
	public static String menuText() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
		sb.append("1、课程介绍\n");
		sb.append("2、慕课网介绍\n\n");
		sb.append("回复？调出此菜单。");
		return sb.toString();
	}

	/**
	 * 调出内容
	 * 
	 * @param toUserName
	 * @param fromUerName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName, String fromUerName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);// 服务器传来的目标用户就是我们的来源用户
		text.setToUserName(fromUerName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToXml(text);
	}

	/**
	 * 回复"1"时返回的内容
	 * 
	 * @return
	 */
	public static String firstMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("本套课程介绍微信公众号开发，主要涉及公众号介绍、编辑模式介绍、开发模式介绍等");
		return sb.toString();
	}

	/**
	 * 回复"2"时返回的内容
	 * 
	 * @return
	 */
	public static String secondMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。\n");
		sb.append(
				"慕课网课程涵盖前端开发、PHP、Html5、Android、iOS、Swift等IT前沿技术语言，包括基础课程、实用案例、高级分享三大类型，适合不同阶段的学习人群。以纯干货、短视频的形式为平台特点，为在校学生、职场白领提供了一个迅速提升技能、共同分享进步的学习平台");
		return sb.toString();
	}
}
