package com.vai.persistence.parsing;
import org.w3c.dom.CharacterData;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Node} 的封装，主要为了配置值的动态替换
 *
 * @author yangk
 * @since 2023/11/3
 */
public class XNode {

    private Node node;

    /**
     * 变量 Properties 对象
     */
    private Properties variables;

//    private final XPathParser xPathParser;

    public XNode(XPathParser xPathParser, Node node, Properties variables) {


    }

}
