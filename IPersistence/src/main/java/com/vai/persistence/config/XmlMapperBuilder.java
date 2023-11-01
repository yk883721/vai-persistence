package com.vai.persistence.config;

import com.vai.persistence.io.Resources;
import com.vai.persistence.pojo.Configuration;
import com.vai.persistence.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;

/**
 * @author yangk
 * @since 2023/11/1
 */
public class XmlMapperBuilder {

    private Configuration configuration;

    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");

        for (Iterator<Element> it = rootElement.elementIterator("select"); it.hasNext();) {
            Element selectElement = it.next();
            String id = selectElement.attributeValue("id");
            String resultType = selectElement.attributeValue("resultType");
            String parameterType = selectElement.attributeValue("parameterType");
            String sqlText = selectElement.getTextTrim();

            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sqlText);

            String key = namespace + "." + id;
            configuration.getMappedStatementMap().put(key, mappedStatement);

        }

    }

}
