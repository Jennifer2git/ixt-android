package com.imax.ipt.ui.activity.menu;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.R.integer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import android.widget.TextView;
import com.imax.ipt.IPT;
import com.imax.ipt.R;
import com.imax.ipt.common.Constants;
import com.imax.ipt.controller.eventbus.handler.ui.MediaMenuLibraryEvent;
import com.imax.ipt.controls.IPTCheckableTextView;
import com.imax.ipt.ui.layout.IPTTextView;
import com.imax.ipt.ui.layout.MenuView;
import com.imax.ipt.ui.util.TypeFaces;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;
import com.imax.iptevent.EventBus;

public class MenuLibraryFragment extends Fragment implements OnTouchListener, OnItemClickListener {
    private RelativeLayout mMenuLibraryLayout;
    //   private RelativeLayout mMenuSubLayout;
//   private ScrollView mMenuLayoutAnimation;
    private MenuView mMenuView; //the menu background no use in new ui.

    private IPTTextView textViewTitle;
    private List<MenuLibraryElement> mMenuOptions = new ArrayList<MenuLibraryElement>();
    private String mTitle = "";
    private Typeface face;
    private EventBus eventBus;
    private int width;

    private ListView checkableMenu;
    private ListView checkableSubMenu;

    private static volatile int position = 0;

    public MenuView getMenuView() {
        return mMenuView;
    }

    /**
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMenuLibraryLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_menu_library, null);
        //      mMenuSubLayout = (RelativeLayout) mMenuLibraryLayout.findViewById(R.id.layoutSubMenu);
//      mMenuLayoutAnimation = (ScrollView) mMenuLibraryLayout.findViewById(R.id.scrollViewAnimated);
//        mMenuView = (MenuView) mMenuLibraryLayout.findViewById(R.id.menuLayout);
//        mMenuView.setOnTouchListener(this);
        face = TypeFaces.get(getActivity(), Constants.FONT_HAIRLINE_PATH);
        checkableMenu = (ListView) mMenuLibraryLayout.findViewById(R.id.checkableMenu);
        MenuItemAdapter adapter = new MenuItemAdapter(getActivity(), mMenuOptions, false);
        checkableMenu.setAdapter(adapter);
        checkableMenu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        checkableMenu.setSelector(getResources().getDrawable(color.transparent));
        checkableMenu.setSelector(getResources().getDrawable(R.drawable.selector_item_menu_background));
        checkableMenu.setOnItemClickListener(this);
        checkableMenu.setItemChecked(0, true);

        checkableSubMenu = (ListView) mMenuLibraryLayout.findViewById(R.id.checkableSubMenu);

        this.init(mMenuLibraryLayout);
        this.eventBus = IPT.getInstance().getEventBus();

        return mMenuLibraryLayout;
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
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_HAIRLINE_PATH);
        textViewTitle = (IPTTextView) view.findViewById(R.id.txtTitle);
        textViewTitle.setTypeface(face);
        textViewTitle.setText(mTitle);
        for (MenuLibraryElement menu : mMenuOptions) {
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

            if (menu.getSubMenus() != null) {
                createSubMenu(menu.getSubMenus());
            }
        }

        this.setWidth();
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
        checkableSubMenu.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        checkableSubMenu.setSelector(getResources().getDrawable(color.transparent));
        checkableSubMenu.setAdapter(adapter);
        checkableSubMenu.setOnItemClickListener(this);
    }

    private void setWidth() {
        checkableMenu.getLayoutParams().width = getWidth();
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
//                    IPTCheckableTextView title = (IPTCheckableTextView)v.findViewById(R.id.menuText);
//            title.setTextColor(getResources().getColor(R.color.gold));
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

}
