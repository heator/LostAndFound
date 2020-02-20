package com.yf107.teamwork.lostandfound.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.yf107.teamwork.lostandfound.adapter.SearchItemAdapter;
import com.yf107.teamwork.lostandfound.network.AllURI;
import com.yf107.teamwork.lostandfound.services.ActivityManager;
import com.yf107.teamwork.lostandfound.view.interfaces.ISearchFragment;
import com.yyydjk.library.DropDownMenu;
import com.yf107.teamwork.lostandfound.R;
import com.yf107.teamwork.lostandfound.adapter.ConstellationAdapter;
import com.yf107.teamwork.lostandfound.adapter.GirdDropDownAdapter;
import com.yf107.teamwork.lostandfound.adapter.ListDropDownAdapter;
import com.yf107.teamwork.lostandfound.bean.DynamicItemBean;
import com.yf107.teamwork.lostandfound.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.yf107.teamwork.lostandfound.view.activity.SignInActivity.SESSION;


/**
 * Description: 搜索界面

 */
public class SearchFragment extends Fragment implements ISearchFragment {

    private AutoCompleteTextView searchInput;
    private Button sure;
    private DropDownMenu dropDownMenu;
    private List<View> popupViews = new ArrayList<>();
    private View view;
    private Context context;

    ArrayAdapter<String> adapter;

    private String[] headers = {"启事类型", "丢失地点", "物品类型"};
    private String[] diushitypes = {"不限", "失物", "招领"};
    private List<String> places = new ArrayList<>();
    private List<String> thingstypes = new ArrayList<>();
    private Integer diushiTypePosition = 0;
    private Integer placePosition = 0;
    private Integer thingsTypePosition = -1;

    private Integer thingsPosition = 0;

    private GirdDropDownAdapter diushitypeAdapter;
    private ListDropDownAdapter placesAdapter;
    private ConstellationAdapter thingsAdapter;
    private ListView diushitypesView;
    private ListView placesView;
    private View thingsView;
    private GridView thingsConstellationView;
    private RecyclerView recyclerView;
    private TextView ok;
    private GridLayoutManager gridLayoutManager;

    private SearchItemAdapter searchItemAdapter;
    private ArrayList<DynamicItemBean> searchItemBeanArrayList = new ArrayList<>();

    private SearchPresenter iSearchPresenter;

    private SharedPreferences sharedPreferences;
    private Unbinder unbinder;

    public static Fragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container,false);
        context = getContext();
        sharedPreferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        iSearchPresenter = new SearchPresenter(SearchFragment.this);
        ActivityManager.getActivityManager().addF(this);
        initView();
        setOnClick();
        search("");

        initAutoComplete("history", searchInput);
        Editable etext = searchInput.getText();
        Selection.setSelection(etext, etext.length());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        iSearchPresenter.dettachActivity();
        unbinder.unbind();
        super.onDestroyView();
    }

    /**
     * Description: 初始化控件
     */
    private void initView(){
        places.add("不限");
        places.addAll(AllURI.allPlaceBeanList);
        thingstypes.add("不限");
        thingstypes.addAll(AllURI.allTypeBeanList);
        
        unbinder=ButterKnife.bind(view);
        searchInput = view.findViewById(R.id.search_input);
        //searchInput.setSubmitButtonEnabled(false);
      // searchInput.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackground(null);
       // searchInput.findViewById(android.support.v7.appcompat.R.id.submit_area).setBackground(null);
        sure = view.findViewById(R.id.search_sure);
        dropDownMenu = view.findViewById(R.id.search_dropDownMenu);

        //初始化启事类型过滤
        diushitypesView = new ListView(context);
        diushitypeAdapter = new GirdDropDownAdapter(context, Arrays.asList(diushitypes));
        diushitypesView.setDividerHeight(0);
        diushitypesView.setAdapter(diushitypeAdapter);

        //初始化丢失地点过滤
        placesView = new ListView(context);
        placesAdapter = new ListDropDownAdapter(context, places);
        placesView.setDividerHeight(0);
        placesView.setAdapter(placesAdapter);

        //初始化物品类型过滤
        thingsView = getLayoutInflater().inflate(R.layout.custom_layout,null);
        thingsConstellationView = thingsView.findViewById( R.id.constellation);
        thingsAdapter = new ConstellationAdapter(context, thingstypes);
        thingsConstellationView.setAdapter(thingsAdapter);
        ok = thingsView.findViewById(R.id.ok);

        //init popupViews
        popupViews= Arrays.asList(diushitypesView, placesView, thingsView);

        //初始化recyclerView
        List<Integer> list = new ArrayList<>();
        searchItemAdapter = new SearchItemAdapter(searchItemBeanArrayList,list,getActivity(),false);
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        gridLayoutManager = new GridLayoutManager(context,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(searchItemAdapter);
//        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(context);

        Log.e("SearchFragment","headers大小:"+Integer.toString(headers.length) + "popupViews大小:"+Integer.toString(popupViews.size()));

        //设置dropDownMenu
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerView);
    }

    /**
     * Description: 设置Listener
     */
    private void setOnClick(){
        diushitypesView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)->{
                diushitypeAdapter.setCheckItem(position);
                dropDownMenu.setTabText(position == 0 ? headers[0] : diushitypes[position]);
                diushiTypePosition = position;
                dropDownMenu.closeMenu();
            }
        );

        placesView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)->{
                placesAdapter.setCheckItem(position);
                dropDownMenu.setTabText(position == 0 ? headers[1] : places.get(position));
                placePosition = position;
                dropDownMenu.closeMenu();
            });

        ok.setOnClickListener(v->{
                //其内部已经设置了记录当前tab位置的参数，该参数会随tab被点击而改变，所以这里直接设置tab值即可
                //此处若想获得constellations第一个值“不限”，可修改constellationPosition初始值为-1，且这里代码改为constellationPosition == -1)
                dropDownMenu.setTabText((thingsPosition == 0) ? headers[2] : thingstypes.get(thingsPosition));
//                thingstype = thingstypes[thingsPosition];
                dropDownMenu.closeMenu();
//                changeContentView();   //在这里可以请求获得经筛选后要显示的内容
            });

        thingsConstellationView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)->{
                thingsAdapter.setCheckItem(position);
                thingsPosition = position;
            });

//        searchInput.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                Logger.e(query);
////                Toast.makeText(getActivity(),query,Toast.LENGTH_SHORT).show();
////                search(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });


        //点击搜索按键后
        sure.setOnClickListener(v->{

                 saveHistory("history", searchInput);
                String keyword = searchInput.getText().toString();
                search(keyword);
            });
    }

    @Override
    public void showSearchResult(Boolean status, List<DynamicItemBean> arrayList) {
        if(status){
            searchItemBeanArrayList.clear();
            searchItemBeanArrayList.addAll(arrayList);
            searchItemAdapter.notifyDataSetChanged();
//            searchItemAdapter.notifyItemChanged(this.searchItemBeanArrayList.size()-1);
//            recyclerView.scrollToPosition(msgList.size() - 1);
        }else {
            FancyToast.makeText(context,"出现了问题",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }
    }

    private void search(String str){
        String keyword = str;
        if(keyword.length()==0){
            keyword = "";
        }
        String session = sharedPreferences.getString(SESSION,"null");
        if(placePosition==0){
            placePosition=-1;
        }
//                if(diushiTypePosition==0){
//                    diushiTypePosition=-1;
//                }
        if(thingsPosition==0){
            thingsPosition=-1;
        }
        iSearchPresenter.getSearchResult(keyword, diushiTypePosition-1, placePosition, thingsPosition, session);

    }


    private final class MyOnClickListener implements View.OnClickListener {

        @Override

        public void onClick(View v) {


            saveHistory("history", searchInput);
            adapter.notifyDataSetChanged();



        }

    }



    /**

     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段

     *

     * @param field

     *            保存在sharedPreference中的字段名

     * @param autoCompleteTextView

     *            要操作的AutoCompleteTextView

     */

    private void saveHistory(String field, AutoCompleteTextView autoCompleteTextView) {

        String text = autoCompleteTextView.getText().toString();

        SharedPreferences sp = getActivity().getSharedPreferences("network_url", 0);

        String longhistory = sp.getString(field, "nothing");

        if (!longhistory.contains(text + ",")) {

            StringBuilder sb = new StringBuilder(longhistory);

            sb.insert(0, text + ",");

            sp.edit().putString("history", sb.toString()).commit();

        }

    }



    /**

     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示

     *

     * @param field

     *            保存在sharedPreference中的字段名

     * @param autoCompleteTextView

     *            要操作的AutoCompleteTextView

     */

    private void initAutoComplete(String field, final AutoCompleteTextView autoCompleteTextView) {

        SharedPreferences sp =getActivity(). getSharedPreferences("network_url", 0);

        String longhistory = sp.getString("history", "nothing");

        String[] histories = longhistory.split(",");

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, histories);

        // 只保留最近的50条的记录

        if (histories.length > 50) {

            String[] newHistories = new String[50];

            System.arraycopy(histories, 0, newHistories, 0, 50);

            adapter = new ArrayAdapter<String>(getContext(),

                    android.R.layout.simple_dropdown_item_1line, newHistories);

        }

        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override

            public void onFocusChange(View v, boolean hasFocus) {

                AutoCompleteTextView view = (AutoCompleteTextView) v;

                if (hasFocus) {

                    view.showDropDown();

                }

            }

        });

        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AutoCompleteTextView view = (AutoCompleteTextView) v;
                view.showDropDown();

            }
        });


    }
}