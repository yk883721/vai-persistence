package com.vai.persistence.parsing;

import com.vai.persistence.io.Resources;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author yangk
 * @since 2023/11/3
 */
public class XPathParserTest {

    @Test
    public void shouldTestXPathParserMethods() throws Exception {

        InputStream inputStream = Resources.getResourceAsStream("nodelet_test.xml");
        System.out.println(inputStream);

    }

}
