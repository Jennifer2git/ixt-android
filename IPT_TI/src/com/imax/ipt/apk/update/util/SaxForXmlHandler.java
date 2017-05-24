package com.imax.ipt.apk.update.util;

/**
 * Created by yanli on 2016/2/24.
 */


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaxForXmlHandler extends DefaultHandler {
    private String[] _needTag;
    private ArrayList<Map<String,String>> _notics;
    private Map<String,String> current;
    private String preTag;
    private String _nodeTag;

    //���캯��
    public SaxForXmlHandler(String tag)
    {
        this._nodeTag = tag;
    }
    public SaxForXmlHandler(String[] need)
    {
        this._needTag = need;
    }
    public SaxForXmlHandler(String tag,String[] need)
    {
        this._nodeTag = tag;
        this._needTag = need;
    }

    //��ȡ����ÿ���ڵ����ݵı�ǩ����
    public void setNodeTag(String tag)
    {
        this._nodeTag = tag;
    }
    public String getNodeTag()
    {
        return this._nodeTag;
    }

    //��ȡ���ð������ݵı�ǩ��������
    public void setNeedTag(String[] need)
    {
        this._needTag = need;
    }
    public String[] getNeedTag()
    {
        return this._needTag;
    }

    //������մ���������
    public ArrayList<Map<String,String>> getResult()
    {
        return this._notics;
    }

    //�ĵ���ʼ
    @Override
    public void startDocument() throws SAXException {
        this._notics = new ArrayList<Map<String,String>>();
        this.preTag = null;
        this.current = null;
        if(this._nodeTag == null){
            throw new IllegalArgumentException("�ڵ��ǩ����δ��ֵ");
        }else if(this._needTag == null){
            throw new IllegalArgumentException("���ݱ�ǩ����δ��ֵ");
        }
        super.startDocument();
    }

    //�ڵ㿪ͷ
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if(_nodeTag.equals(localName)){
            //ʵ����һ��Map����
            current = new HashMap<String,String>();
        }
        //����ǰ����ı�ǩ���Ʊ�����preTag��
        preTag = localName;
    }

    //�ڵ��е��ı�
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //��ȡ��ǩ�е��ı�
        String data = new String(ch,start,length);
        String dedata = "";
        for(String item : this._needTag)
        {
            if(item.equals(preTag))
            {
                try {
                    //�����ݽ���URL����
                    dedata = URLDecoder.decode(data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    dedata = data;
                }finally{
                    //����ǰ�����ݷ���map������
                    current.put(item, dedata);
                }
                return;
            }
        }
    }

    //�ڵ����
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if(this._nodeTag.equals(localName))
        {
            //����ǰmap�������ArrayList������
            this._notics.add(current);
            current = null;
        }
        //����ǰ��ǩ����Ϊnull
        preTag = null;
    }

    //�ĵ�����
    @Override
    public void endDocument() throws SAXException {
        current = null;
        preTag = null;
        super.endDocument();
    }
}