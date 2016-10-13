package derpyhooves.dipvlom.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.Space;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import derpyhooves.dipvlom.Adapters.PagerAdapter;
import derpyhooves.dipvlom.Fragments.ScheduleFragment;
import derpyhooves.dipvlom.Fragments.ShowHouseInfoFragment;
import derpyhooves.dipvlom.Fragments.ShowHouseMapFragment;
import derpyhooves.dipvlom.R;

public class HouseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Spannable> infoFromCurrentHouse = new ArrayList<>();
    private String title;
    private double latitude;
    private double longitude;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        int position = getIntent().getIntExtra("position", 0);
        String[] allHousing = getResources().getStringArray(R.array.Housing);
        title=allHousing[position];
        setTitle(allHousing[position]);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer7);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        infoFromHouse(position);
        setupViewPager(viewPager);
    }

    private void infoFromHouse(int position)
    {
        switch (position)
        {
            // Административный корпус
            case 0:

                getSpannableString("Скорочена назва: АК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Бухгалтерія (2 пов., к.1)",0);
                getSpannableString("   Канцелярія (2 пов., к.10,12)",0);
                getSpannableString("   Відділ кадрів (3 пов., к.18)",0);
                getSpannableString("   Відділ коммандировок (2 пов., к.13)",0);
                getSpannableString("   Перший відділ (3 пов., к.15,15а)",0);
                getSpannableString("   Стипіндіальний відділ (2 пов., к.2,3)",0);
                getSpannableString("   Типографія (1 пов.)",0);
                getSpannableString("   Фінансо-плановий відділ (3 пов., к.23-25)",0);
                getSpannableString("   Юридичний відділ (2 пов.)",0);
                getSpannableString("   Відділ договірної практики (2 пов.)",0);
                getSpannableString("   МІПО (3 пов., к.305)",0);
                getSpannableString("   Підготовчі курси (3 пов., к.302)",0);
                latitude=50.001024;
                longitude=36.250337;
                break;


            // Вечерний корпус
            case 1:

                getSpannableString("Скорочена назва: ВК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Деканати:",9);
                getSpannableString("   Комп'ютерних та інформаційних технологій",0);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Центр нових інформаційних технологій (1 пов.)",0);
                latitude=50.000916;
                longitude=36.251466;
                break;

            // Главный аудиторный корпус
            case 2:

                getSpannableString("Скорочена назва: ГАК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Рік будівництва: 1885",16);
                getSpannableString("Буфет: 1 поверх (праворуч)",5);
                getSpannableString("Деканати:",9);
                getSpannableString("   Механіко-технологічний",0);
                getSpannableString("   Транспортного машинобудування",0);
                getSpannableString("   Машинобуднівний",0);
                getSpannableString("   Енергомашинобудівний",0);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Бібліотека (1 пов.)",0);
                latitude=49.997488;
                longitude=36.249079;
                break;

            //Інженерний корпус
            case 3:

                getSpannableString("Скорочена назва: ІК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Рік будівництва: 1906",16);
                getSpannableString("Архітектор: Величко В.В",11);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Центр підготовки іноземних громадян",0);
                latitude=49.999502;
                longitude=36.248827;
                break;

            //Корпус громадських організацій
            case 4:

                getSpannableString("Скорочена назва: КГО",16);
                getSpannableString("Кількість поверхів: 2",19);
                getSpannableString("Рік будівництва: 1902",16);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Перукарня (2 пов.)",0);
                getSpannableString("   Профсоюз співробітників (2 пов.)",0);
                getSpannableString("   Профсоюз студентів (2 пов.)",0);
                getSpannableString("   Столова (1 пов.)",0);
                latitude=49.998378;
                longitude=36.246434;
                break;

            //Математичний корпус
            case 5:

                getSpannableString("Скорочена назва: МК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Науково-дослідна частина",0);
                getSpannableString("   Підготовче відділення",0);
                getSpannableString("   Редакційно-видавничий відділ",0);
                latitude=49.999837;
                longitude=36.250152;
                break;

            //Палац студентів
            case 6:

                getSpannableString("Скорочена назва: ПС",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Адреса: вул. Пушкінська 79/1 (за аркою)",7);
                getSpannableString("Рік будівництва: 1925",16);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Бібліотека (1 пов.)",0);
                getSpannableString("   Борцівський зал",0);
                latitude=50.006675;
                longitude=36.248948;
                break;

            //Радіокорпус
            case 7:

                getSpannableString("Скорочена назва: РК",16);
                getSpannableString("Кількість поверхів: 4 + цокольний",19);
                getSpannableString("Ліфт: один",5);
                latitude=49.999533;
                longitude=36.252317;
                break;

            // Ректорський корпус
            case 8:

                getSpannableString("Скорочена назва: ГК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Рік будівництва: 1901",16);
                getSpannableString("Архітектор: Ловцов М.І.",11);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Аспірантура, докторантура (2 пов., к.21)",0);
                getSpannableString("   Газета \"Політехнік\" (3 пов., к.5)",0);
                getSpannableString("   Диспечерьска (3 пов., к.4)",0);
                getSpannableString("   Міжнародний відділ (к.23,38)",0);
                getSpannableString("   Музей (3 пов., к.8)",0);
                getSpannableString("   Прес-служба (1 пов., к.39)",0);
                getSpannableString("   Навчальний відділ (1 пов., к.39)",0);
                getSpannableString("   Навчальна рада (к.7)",0);
                getSpannableString("   Центр кар'єри (1 пов., к.26)",0);
                getSpannableString("   Центр незалежного тестування (2 пов.)",0);
                getSpannableString("   Україно-Американський центр (1 пов., к.39)",0);
                latitude=49.998985;
                longitude=36.248358;
                break;

            //Спортивний корпус
            case 9:

                getSpannableString("Скорочена назва: СК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Адреса: вул. Алчевських 50а",7);
                getSpannableString("Рік будівництва: 1991",16);
                getSpannableString("Архітектор: Пундик Ю.Л.",11);
                getSpannableString("Буфет: 1 поверх",5);
                latitude=50.009393;
                longitude=36.248526;
                break;

            //Технічний корпус
            case 10:

                getSpannableString("Скорочена назва: ТК",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Рік будівництва: 1889",16);
                getSpannableString("Архітектор: Шпилегь А.К., Ловцов М.І.",11);
                getSpannableString("Буфет: 1 поверх",5);
                getSpannableString("Деканати:",9);
                getSpannableString("   Технологій неорганічних речовин",0);
                latitude=49.999387;
                longitude=36.251520;
                break;

            //Український корпус
            case 11:

                getSpannableString("Скорочена назва: КУМ",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Адреса: вул. Весніна 5а",7);
                latitude=50.013443;
                longitude=36.249453;
                break;

            //Учбовий корпус №1
            case 12:

                getSpannableString("Скорочена назва: У1",16);
                getSpannableString("Кількість поверхів: 14",19);
                getSpannableString("Рік будівництва: 1977",16);
                getSpannableString("Архітектор: Овсянкін І.Д., Лившиц В.І.",11);
                getSpannableString("Висота: 60м",7);
                getSpannableString("Ліфт: шість",5);
                getSpannableString("Буфет: 3 поверх",5);
                getSpannableString("Деканати:",9);
                getSpannableString("   Бізнесу та фінансів",0);
                getSpannableString("   Інтегрованих технологій і хімічної техніки",0);
                getSpannableString("   Економічний",0);
                latitude=49.998457;
                longitude=36.251492;
                break;

            //Учбовий корпус №2
            case 13:

                getSpannableString("Скорочена назва: У2",16);
                getSpannableString("Кількість поверхів: 7 + цокольний",19);
                getSpannableString("Рік будівництва: 1984",16);
                getSpannableString("Ліфт: чотири",5);
                getSpannableString("Буфет: 1 поверх (праворуч)",5);
                getSpannableString("Деканати:",9);
                getSpannableString("   Інженерно-фізичний",0);
                getSpannableString("   Інформатики і управління",0);
                getSpannableString("   Фізико-технічний",0);
                getSpannableString("Відділи та підрозділи:",22);
                getSpannableString("   Вісник НТУ \"ХПІ\" (2 пов.)",0);
                getSpannableString("   Канцторвари (1 пов.)",0);
                getSpannableString("   Приймальна коміссія (1 пов.)",0);
                getSpannableString("   Студенський Альянс (к.104)",0);
                latitude=49.998364;
                longitude=36.247801;
                break;

            //Учбовий корпус №3
            case 14:

                getSpannableString("Скорочена назва: У3",16);
                getSpannableString("Кількість поверхів: 4",19);
                getSpannableString("Адреса: вул. Гамарника 2",7);
                latitude=49.9869675;
                longitude=36.2342108;
                break;

            //Учбовий корпус №4
            case 15:

                getSpannableString("Скорочена назва: У4",16);
                getSpannableString("Кількість поверхів: 3",19);
                getSpannableString("Адреса: вул. Пушкінська 85",7);
                latitude=50.012113;
                longitude=36.255035;
                break;

            //Учбовий корпус №5
            case 16:

                getSpannableString("Скорочена назва: У5",16);
                getSpannableString("Кількість поверхів: 4",19);
                getSpannableString("Адреса: вул. Пушкінська 79/2 (напроти ПС)",7);
                getSpannableString("Деканати:",9);
                getSpannableString("   Інтегральної підготовки",0);
                latitude=50.007151;
                longitude=36.247711;
                break;

            //Фізичний корпус
            case 17:

                getSpannableString("Скорочена назва: ФК",16);
                getSpannableString("Кількість поверхів: 4",19);
                getSpannableString("Рік будівництва: 1875-1877",16);
                getSpannableString("Архітектор: Генріхсен Р.Р.",11);
                latitude=49.997946;
                longitude=36.247860;
                break;

            //Хімічний корпус
            case 18:

                getSpannableString("Скорочена назва: ХК",16);
                getSpannableString("Кількість поверхів: 4",19);
                getSpannableString("Рік будівництва: 1875-1877",16);
                getSpannableString("Архітектор: Генріхсен Р.Р.",11);
                getSpannableString("Висота: 18м",7);
                getSpannableString("Деканати:",9);
                getSpannableString("   Технології неорганічних речовин",0);
                latitude=49.998129;
                longitude=36.250332;
                break;

            //Електротехнічний корпус
            case 19:

                getSpannableString("Скорочена назва: ЕК",16);
                getSpannableString("Кількість поверхів: 3 + цокольний",19);
                getSpannableString("Рік будівництва: 1930-ті",16);
                getSpannableString("Архітектор: Бекетов А.Н.",11);
                getSpannableString("Буфет: 1 поверх",5);
                getSpannableString("Деканати:",9);
                getSpannableString("   Автоматики та приладобудування",0);
                getSpannableString("   Німецький технічний",0);
                getSpannableString("   Електромашинобудівний",0);
                getSpannableString("   Електроенергетичний",0);
                latitude=50.000136;
                longitude=36.249887;
                break;
        }
    }

    private void getSpannableString(String str,int endIndex)
    {
        Spannable sb = new SpannableString(str);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        infoFromCurrentHouse.add(sb);
    }

    private void setupViewPager(ViewPager viewPager) {

        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Fragment showHouseInfoFragment = new ShowHouseInfoFragment(infoFromCurrentHouse);
        Fragment showHouseMapFragment = new ShowHouseMapFragment(latitude,longitude,title);

        adapter.addFragment(showHouseInfoFragment, "Опис");
        adapter.addFragment(showHouseMapFragment, "Карта");
        viewPager.setAdapter(adapter);

        boolean isSubjectActivity = getIntent().getBooleanExtra("isSubjectActivity", false);
        if (isSubjectActivity) viewPager.setCurrentItem(1);

        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_OK, intent);
        int request=-1;

        if (id == R.id.nav_gallery) request=11;
        else if (id == R.id.nav_slideshow) request=12;
        else if (id == R.id.nav_manage) request=13;
        else if (id == R.id.nav_share) request=14;
        else if (id == R.id.nav_send) request=15;

        drawer.closeDrawer(GravityCompat.START);
        intent.putExtra("request", request);
        startActivity(intent);
        return true;
    }
}
