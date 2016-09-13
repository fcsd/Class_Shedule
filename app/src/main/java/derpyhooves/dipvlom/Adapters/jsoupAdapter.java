package derpyhooves.dipvlom.Adapters;


import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import derpyhooves.dipvlom.Activities.ScheduleActivity;
import derpyhooves.dipvlom.R;


public class jsoupAdapter extends AsyncTask<Void, Void, Map<Integer, ArrayList<String>>> {

    public interface AsyncResponse {
        void processFinish(Map<Integer, ArrayList<String>> map);
    }

    private String mURL;
    private int m_mode;

    public AsyncResponse delegate = null;
    private boolean isOneProgressDialogShow = false;

    private ProgressDialog dialog;

    public jsoupAdapter(String URL, int mode, Activity activity, AsyncResponse delegate) {
        mURL = URL;
        m_mode = mode;
        this.delegate = delegate;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        if (m_mode==1 && !isOneProgressDialogShow)
        {
            dialog.setMessage("Завантаження...");
            dialog.show();
            isOneProgressDialogShow = true;
        }
        if (m_mode==2 || m_mode==3 || m_mode==4)
        {
            dialog.setMessage("Завантаження...");
            dialog.show();
        }
    }


    @Override
    protected Map<Integer, ArrayList<String>> doInBackground(Void... params) {

        Document doc = null;
        try {
            doc = Jsoup.connect(mURL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Integer, ArrayList<String>> map = new HashMap();


        if (m_mode == 1) {
            //Document doc = Jsoup.connect("http://www.lazada.com.my/").followRedirects(true).get();
            Elements links = doc.select("#form1").select("a[href]");


            ArrayList<String> GroupLinks = new ArrayList<>();
            ArrayList<String> GroupNames = new ArrayList<>();

            for (Element link : links) {
                GroupLinks.add(link.attr("abs:href"));
                GroupNames.add(link.text());
            }

            map.put(1, GroupLinks);
            map.put(2, GroupNames);
        }

        if (m_mode == 2) {

            ArrayList<String> scheduleForFirstWeek = new ArrayList<>();
            ArrayList<String> scheduleForSecondWeek = new ArrayList<>();
            ArrayList<String> type = new ArrayList<>();
            String replace;


            for (Element row : doc.select("div.tab-content").select("#home").select("table").select("tr")) {
                for (Element item : row.select("td")) {

                    if (item.select("#css_time").hasText()) scheduleForFirstWeek.add(item.text());

                    else if (item.select("#css_lec").hasText()) {
                        replace = item.select("#css_lec").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForFirstWeek.add(s[i]);
                    } else if (item.select("#css_sem").hasText()) {
                        replace = item.select("#css_sem").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForFirstWeek.add(s[i]);
                    } else if (item.select("#css_lab").hasText()) {
                        replace = item.select("#css_lab").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForFirstWeek.add(s[i]);
                    } else if (item.select("#css_pra").hasText()) {
                        replace = item.select("#css_pra").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForFirstWeek.add(s[i]);
                    } else for (int i = 0; i < 3; i++) scheduleForFirstWeek.add("");

                }
            }

            for (Element row : doc.select("div.tab-content").select("#profile").select("table").select("tr")) {
                for (Element item : row.select("td")) {

                    if (item.select("#css_time").hasText()) scheduleForSecondWeek.add(item.text());

                    else if (item.select("#css_lec").hasText()) {
                        replace = item.select("#css_lec").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForSecondWeek.add(s[i]);
                    } else if (item.select("#css_sem").hasText()) {
                        replace = item.select("#css_sem").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForSecondWeek.add(s[i]);
                    } else if (item.select("#css_lab").hasText()) {
                        replace = item.select("#css_lab").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForSecondWeek.add(s[i]);
                    } else if (item.select("#css_pra").hasText()) {
                        replace = item.select("#css_pra").html();
                        String[] s = replace.split("<br>");
                        for (int i = 0; i < 3; i++) scheduleForSecondWeek.add(s[i]);
                    } else for (int i = 0; i < 3; i++) scheduleForSecondWeek.add("");

                }
            }

            Elements season = doc.select("div.tab-content").select("#profile").select("h3.text-center");
            replace = season.text();
            String[] s = replace.split(" ");
            type.add(s[9]);

            map.put(1, scheduleForFirstWeek);
            map.put(2, scheduleForSecondWeek);
            map.put(3, type);
        }

        if (m_mode == 3) {
            ArrayList<String> TeacherName = new ArrayList<>();
            ArrayList<String> TeacherLinks = new ArrayList<>();
            Elements links = doc.select("div.sc_team_member_inner").select("div.sc_team_member_name").select("a[href]");

            for (Element link : links) {
                TeacherLinks.add(link.attr("abs:href"));
                TeacherName.add(link.text());
            }

            map.put(1, TeacherLinks);
            map.put(2, TeacherName);
        }

        if (m_mode == 4) {
            ArrayList<String> TeacherInfo = new ArrayList<>();
            String shmi;
            Elements link = doc.select("#content");
            shmi = link.select("p,ol").html();
            String[] s = shmi.split("<br>|\n");
            for (int i = 0; i < s.length; i++) {
                s[i] = s[i].replace("&nbsp;", " ");
                s[i] = s[i].replaceAll("\\<.*?>", "");
                s[i] = s[i].trim();
                TeacherInfo.add(s[i]);
            }

            for (int i = 0; i < TeacherInfo.size(); i++) {
                if (TeacherInfo.get(i).isEmpty()) {
                    TeacherInfo.remove(i);
                    i--;
                }
            }

            Element image = doc.select("#content").select("img").first();
            ArrayList<String> url = new ArrayList<>();
            url.add(image.absUrl("src"));

            map.put(1, TeacherInfo);
            map.put(2, url);

        }

        return map;

    }


    @Override
    protected void onPostExecute(Map<Integer, ArrayList<String>> map) {
            if (dialog.isShowing()) dialog.dismiss();
            delegate.processFinish(map);
        }
    }



