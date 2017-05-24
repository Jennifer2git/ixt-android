package com.imax.ipt.apk.update.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

//SAX�ࣺDefaultHandler����ʵ����ContentHandler�ӿڡ���ʵ�ֵ�ʱ��ֻ��Ҫ�̳и��࣬������Ӧ�ķ������ɡ�
public class XMLContentHandler extends DefaultHandler {

    private List<VersionInfor> versionInfors = null;
    private VersionInfor currentVersionInfor;
    private String tagName = null;//��ǰ������Ԫ�ر�ǩ

    public List<VersionInfor> getVersionInfors() {

        return versionInfors;
    }

    //�����ĵ���ʼ��֪ͨ���������ĵ��Ŀ�ͷ��ʱ�򣬵������������������������һЩԤ����Ĺ�����
    @Override
    public void startDocument() throws SAXException {
        versionInfors = new ArrayList<VersionInfor>();
    }

    //����Ԫ�ؿ�ʼ��֪ͨ��������һ����ʼ��ǩ��ʱ�򣬻ᴥ���������������namespaceURI��ʾԪ�ص������ռ䣻
    //localName��ʾԪ�صı������ƣ�����ǰ׺����qName��ʾԪ�ص��޶�������ǰ׺����atts ��ʾԪ�ص����Լ���
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        if(localName.equals("versionInfor")){
            currentVersionInfor = new VersionInfor();
//            currentVersionInfor.setId(Integer.parseInt(atts.getValue("id")));
        }

        this.tagName = localName;
    }

    //�����ַ����ݵ�֪ͨ���÷�������������XML�ļ��ж��������ݣ���һ���������ڴ���ļ������ݣ�
    //�������������Ƕ������ַ�������������е���ʼλ�úͳ��ȣ�ʹ��new String(ch,start,length)�Ϳ��Ի�ȡ���ݡ�
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if(tagName!=null){
            String data = new String(ch, start, length);
            if(tagName.equals("apkName")) {
                this.currentVersionInfor.setApkName(data);
            }else if(tagName.equals("appName")){
                this.currentVersionInfor.setAppName(data);
            }else if(tagName.equals("verCode")){
                this.currentVersionInfor.setVerCode(Short.parseShort(data));
            }else if(tagName.equals("verName")){
                this.currentVersionInfor.setVerName(data);
            }else if(tagName.equals("forceUpdate")){
                this.currentVersionInfor.setForceUpdate(Boolean.parseBoolean(data));
            }else if(tagName.equals("description")){
                this.currentVersionInfor.setDescription(data);
            }
        }
    }

    //�����ĵ��Ľ�β��֪ͨ��������������ǩ��ʱ�򣬵���������������У�uri��ʾԪ�ص������ռ䣻
    //localName��ʾԪ�صı������ƣ�����ǰ׺����name��ʾԪ�ص��޶�������ǰ׺��
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {

        if(localName.equals("versionInfor")){
            versionInfors.add(currentVersionInfor);
            currentVersionInfor = null;
        }

        this.tagName = null;
    }
}