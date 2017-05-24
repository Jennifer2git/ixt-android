package com.imax.ipt.ui.activity.menu;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.layout.MenuView;
import com.imax.ipt.ui.util.TypeFaces;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListView;
import com.imax.ipt.ui.widget.horizontallistview.HorizontalListViewAdapter;
import com.imax.iptevent.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MenuLibraryFragment_custom extends MenuLibraryFragment implements OnTouchListener, OnItemClickListener {
    private static final String TAG = MenuLibraryFragment_custom.class.getSimpleName();
    private RelativeLayout mMenuLibraryLayout;
       private RelativeLayout mMenuSubLayout;
//   private ScrollView mMenuLayoutAnimation;
    private MenuView mMenuView;
    private HorizontalListView menuHListView;
    private HorizontalListViewAdapter menuHListViewAdapter;
    private HorizontalListView subMenuHListView;
    private HorizontalListViewAdapter subMenuHListViewAdapter;

    private IPTTextView textViewTitle;
    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    private String mTitle = "";
    private Typeface face;
    private EventBus eventBus;
    private int width;

    private ListView checkableMenu;
        private ListView checkableSubMenu;
//    private HorizontalListView checkableMenu;
//    private HorizontalListView checkableSubMenu;

    private static volatile int position = 0;

    public MenuView getMenuView() {
        return mMenuView;
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMenuLibraryLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_menu_library_custom, null);

//add by jennifer
        menuHListView = (HorizontalListView) mMenuLibraryLayout.findViewById(R.id.list_movie_sort);
        face = TypeFaces.get(getActivity(), "fonts/Montserrat-Regular.otf");

        // movie title genre,actor,, checkable menu
//        checkableMenu = (ListView) mMenuLibraryLayout.findViewById(R.id.checkableMenu);
//        checkableMenu = (ListView) mMenuLibraryLayout.findViewById(R.id.list_movie_sort);
//        MenuItemAdapter adapter = new MenuItemAdapter(getActivity(), mMenuOptions, false);

//        MenuItemAdapter adapter = new MenuItemAdapter(getActivity(), mMenuOptions, false);
//        checkableMenu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        checkableMenu.setSelector(getResources().getDrawable(android.R.color.transparent));
//        checkableMenu.setAdapter(adapter);
//        checkableMenu.setOnItemClickListener(this);
//        checkableMenu.setItemChecked(0, true);

        subMenuHListView = (HorizontalListView) mMenuLibraryLayout.findViewById(R.id.list_movie_sub_sort);
        initMenuItem(getActivity(),mMenuLibraryLayout);

//        this.init(mMenuLibraryLayout);
        this.eventBus = IPT.getInstance().getEventBus();
        return mMenuLibraryLayout;
    }

    public void initMenuItem(Context context, View layout){
//        menuHListViewAdapter = new HorizontalListViewAdapter(context,menu);
        //        MenuItemAdapter adapter = new MenuItemAdapter(getActivity(), menu, true);

        menuHListViewAdapter = new HorizontalListViewAdapter(context,mMenuOptions, false);
        menuHListView.setAdapter(menuHListViewAdapter);
        menuHListViewAdapter.setSelectIndex(0);//set default selected position
        menuHListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                menuHListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        MenuLibraryElement menu = mMenuOptions.get(position);

                        eventBus.post(new MediaMenuLibraryEvent(menu.getEvent(), menu));
                        Log.d(TAG,"CLL menu is " + menu.getName() +"," + menu.getEvent());
                        if (menu.getSubMenus() != null) {

                            initSubMenuItem(context, layout, menu.getSubMenus(), position);
                            subMenuHListView.setVisibility(View.VISIBLE);
//                            menuHListView.setVisibility(View.INVISIBLE);
                        } else {
                            subMenuHListView.setVisibility(View.INVISIBLE);
                        }
//                        mItemViewPager.setCurrentItem(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d(TAG, "onNothingSelected;" + "the menu item position :" + position);
//                        mItemViewPager.setCurrentItem(DEFAULT_PAGER_TITLE);

                    }
                });
                menuHListViewAdapter.setSelectIndex(position);
                menuHListViewAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * @param view
     */
    private void init(RelativeLayout view) {
//        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/EurostileLTStd.otf");
//        textViewTitle = (IPTTextView) view.findViewById(R.id.txtTitle);
//        textViewTitle.setTextAppearance(getActivity(), R.style.smallActiveText);
//        textViewTitle.setTypeface(face);
//        textViewTitle.setText(mTitle);
//        textViewTitle.setTextColor(getResources().getColor(R.color.electric_blue));
/*      int i = 1;*/
//        for (MenuLibraryElement menu : mMenuOptions) {
/*         IPTTextView textView = new IPTTextView(getActivity());
         textView.setText(menu.getName());
         textView.setOnClickListener(this);
         textView.setTextAppearance(getActivity(), R.style.InActiveText);
         textView.setTextSize(32);
         textView.setTag(menu);
         textView.setTypeface(face);

         textView.setTextColor(getResources().getColor(R.color.blue));

         RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
         params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
         params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
         params.topMargin = 200 * i;
         params.leftMargin = 230;
         view.addView(textView, params);*/

//            if (menu.getSubMenus() != null) {
//                createSubMenu(menu.getSubMenus());
//            }
//         i++;
//        }

//        this.setWidth();
    }

    private void createSubMenu(List<MenuLibraryElement> subMenu) {
//      int i = 1;
//      for (MenuLibraryElement menu : subMenu)
//      {
//         IPTTextView textView = new IPTTextView(getActivity());
//         textView.setText(menu.getName());
//         textView.setOnClickListener(this);
//         textView.setTextAppearance(getActivity(), R.style.InActiveText);
//         textView.setTextSize(18);
//         textView.setTag(menu);
//         textView.setTypeface(face);
//         textView.setEllipsize(TruncateAt.END);
//         textView.setSingleLine(true);
//         RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//         params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//         params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//         params.topMargin = 100 * i;
//         params.leftMargin = 10;
//         mMenuSubLayout.addView(textView, params);
//         textView.setOnClickListener(this);
//
//         i++;
//      }

        MenuItemAdapter adapter = new MenuItemAdapter(getActivity(), subMenu, true);
//        checkableSubMenu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        checkableSubMenu.setSelector(getResources().getDrawable(android.R.color.transparent));
        checkableSubMenu.setAdapter(adapter);
        checkableSubMenu.setOnItemClickListener(this);
    }

    private void setWidth() {
//        mMenuView.getLayoutParams().width = getWidth();
//        checkableMenu.getLayoutParams().width = getWidth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     *
     */
//   @Override
//   public void onClick(View view)
//   {
//      MenuLibraryElement menu = (MenuLibraryElement) view.getTag();
//      eventBus.post(new MediaMenuLibraryEvent(menu.getEvent(), menu));
//      if (menu.isHasItems())
//      {
//         mMenuSubLayout.setVisibility(View.VISIBLE);
//         mMenuLayoutAnimation.setVisibility(View.VISIBLE);
//         mMenuLayoutAnimation.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_library_submenu_appear));
//
//         checkableSubMenu.setVisibility(View.VISIBLE);
//      }
//
//      else if (mMenuLayoutAnimation.getVisibility() == View.VISIBLE)
//      {
//         mMenuSubLayout.setVisibility(View.INVISIBLE);
//         mMenuLayoutAnimation.setVisibility(View.INVISIBLE);
//         mMenuLayoutAnimation.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_library_submenu_disappear));
//
//         checkableSubMenu.setVisibility(View.INVISIBLE);
//      }
//
//   }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public static int getPosition() {
        return position;
    }

    public static void setPosition(int pos) {
        position = pos;
    }

    public List<MenuLibraryElement> getmMenuOptions() {
        return mMenuOptions;
    }

    public void setmMenuOptions(List<MenuLibraryElement> mMenuOptions) {
        this.mMenuOptions = mMenuOptions;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == checkableMenu) {
            this.position = position;
            MenuLibraryElement menu = mMenuOptions.get(position);
            eventBus.post(new MediaMenuLibraryEvent(menu.getEvent(), menu));
            if (menu.isHasItems()) {
                checkableSubMenu.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_library_submenu_appear));
                checkableSubMenu.setVisibility(View.VISIBLE);
            } else if (checkableSubMenu.getVisibility() == View.VISIBLE) {
                checkableSubMenu.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_library_submenu_disappear));
                checkableSubMenu.setVisibility(View.INVISIBLE);

                if (checkableSubMenu.getCheckedItemPosition() != ListView.INVALID_POSITION)
                    checkableSubMenu.setItemChecked(checkableSubMenu.getCheckedItemPosition(), false);
            }
        } else if (parent == checkableSubMenu) {
            MenuLibraryElement menu = mMenuOptions.get(checkableMenu.getCheckedItemPosition()).getSubMenus().get(position);
            eventBus.post(new MediaMenuLibraryEvent(menu.getEvent(), menu));

            checkableSubMenu.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_library_submenu_disappear));
            checkableSubMenu.setVisibility(View.INVISIBLE);
        }
    }

    public void initSubMenuItem(Context context,View layout,List subMenu, int selectedPosition){

        subMenuHListView = (HorizontalListView) layout.findViewById(R.id.list_movie_sub_sort);
        subMenuHListViewAdapter = new HorizontalListViewAdapter(context,subMenu, true);
        subMenuHListView.setAdapter(subMenuHListViewAdapter);
        subMenuHListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                subMenuHListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        MenuLibraryElement menu = ((MenuLibraryElement)subMenu.get(menuHListView.getSelectedItemPosition())).getSubMenus().get(position);
                        MenuLibraryElement menu = mMenuOptions.get(selectedPosition).getSubMenus().get(position);
                        eventBus.post(new MediaMenuLibraryEvent(menu.getEvent(), menu));
//                        mItemViewPager.setCurrentItem(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d(TAG, "cll onNothingSelected;" + "the item position :" + position);
//                        mItemViewPager.setCurrentItem(DEFAULT_PAGER_TITLE);
                    }
                });
                subMenuHListViewAdapter.setSelectIndex(position);
                subMenuHListViewAdapter.notifyDataSetChanged();

            }
        });
    }

}
