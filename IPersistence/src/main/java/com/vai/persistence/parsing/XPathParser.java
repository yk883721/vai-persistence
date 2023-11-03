package com.vai.persistence.parsing;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 基于 Java XPath 的 xml 解析器
 * @author yangk
 * @since 2023/11/3
 */
public class XPathParser {

    /**
     * XML Document 对象
     */
    private final Document document;

    /**
     * 是否校验
     */
    private boolean validation;

    /**
     * XML 实体解析器
     */
    private EntityResolver entityResolver;

    /**
     * 变量 Properties 对象
     */
    private Properties variables;

    /**
     * Java XPath 对象
     */
    private XPath xPath;

    public XPathParser(InputStream inputStream){
        commonConstructor(false, null, null);
        this.document = createDocument(new InputSource(inputStream));
    }

    public XPathParser(Document document){
        commonConstructor(false, null, null);
        this.document = document;
    }

    public List<XNode> evalNodes(String expression) { // Node 数组
        return evalNodes(document, expression);
    }

    public List<XNode> evalNodes(Object root, String expression) { // Node 数组
        // 获取 Node 数组
        NodeList nodes = (NodeList) eval(expression, root, XPathConstants.NODESET);
        // 封装 XNode 数组
        List<XNode> xnodex = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            xnodex.add(new XNode(this, nodes.item(i), variables));
        }
        return xnodex;
    }


    public XNode evalNode(String expression) { // Node 对象
        return evalNode(document, expression);
    }

    public XNode evalNode(Object root, String expression) { // Node 对象

        Node node = (Node) eval(expression, root, XPathConstants.NODE);
        if (node == null) {
            return null;
        }
        return new XNode(this, node, variables);
    }

    /**
     * 获得指定元素或节点的值, 封装原生 jdk 的方法
     *
     * @param expression 表达式
     * @param root 指定节点
     * @param returnType 返回类型
     * @return 值
     */
    private Object eval(String expression, Object root, QName returnType){
        try {
            return xPath.evaluate(expression, root, returnType);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Error evaluating XPath. Cause: " + e, e);
        }
    }

    /**
     * 创建 Document 对象
     *
     * @param inputSource XML 的 InputSource 对象
     * @return Document 对象
     */
    private Document createDocument(InputSource inputSource){
        // important: this must only be called AFTER common constructor
        try {
            // 1. 创建 DocumentBuilderFactory 对象
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validation); // 设置是否校验 xml

            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);

            // 2. 创建 DocumentBuilder 对象
            DocumentBuilder builder = factory.newDocumentBuilder();
            // todo 设置实体解析器
            builder.setEntityResolver(null);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {

                }

                @Override
                public void error(SAXParseException exception) throws SAXException {

                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {

                }
            });

            // 3. 解析 XML 文件
            return builder.parse(inputSource);

        } catch (Exception e){
            throw new RuntimeException("Error creating document instance, Cause: " + e, e);
        }
    }

    /**
     * 通用前置构造方法
     * @param validation 是否校验 XML
     * @param variables 变量 Properties 对象, 用于在 xml 文件中引入 properties 配置文件, 以 ${} 形式取值
     * @param entityResolver XML 实体解析器, dtd/schema 形式校验 xml 内容
     */
    private void commonConstructor(boolean validation, Properties variables, EntityResolver entityResolver){
        this.validation = validation;
        this.variables = variables;
        this.entityResolver = entityResolver;

        // 原生 jdk XPath, 创建 XPathFactory 对象
        XPathFactory factory = XPathFactory.newInstance();
        this.xPath = factory.newXPath();

    }





}
