package net.xeill.elpuig;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

public class Main {

    public static void main(String[] args) throws Exception {
        Menu menu = new Menu();
        menu.start();
    }
}

