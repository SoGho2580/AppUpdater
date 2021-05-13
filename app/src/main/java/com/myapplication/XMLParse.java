package com.myapplication;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class XMLParse extends AsyncTask {
    URL url;
    ArrayList<Integer> verCode = new ArrayList();
    ArrayList<String> verName = new ArrayList();
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            url = new URL("https://raw.githubusercontent.com/SoGho2580/AppUpdater/master/app/update.xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(url.openConnection().getInputStream(),"UTF_8");
            boolean insideItem = false;
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("update")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("verName")) {
                        if (insideItem)
                            verName.add(xpp.nextText());
                    } else if (xpp.getName().equalsIgnoreCase("verCode")) {
                        if (insideItem)
                            verCode.add(Integer.valueOf(xpp.nextText()));
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("update")) {
                    insideItem = false;
                }
                eventType = xpp.next();
            }
            } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            }
        return verCode;
    }
}