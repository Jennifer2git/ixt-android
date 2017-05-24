package com.imax.ipt.ui.activity.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.imax.ipt.R;
import com.imax.ipt.ui.viewmodel.MenuLibraryElement;

import java.util.List;

public class MenuItemAdapter extends BaseAdapter {
    private Context context;
    private List<MenuLibraryElement> menuOptions;
    private boolean isSubMenu;//has submenu

    private static LayoutInflater inflater = null;

    public MenuItemAdapter(Context context, List<MenuLibraryElement> menuOptions, boolean isSubMenu) {
        this.context = context;
        this.menuOptions = menuOptions;
        this.isSubMenu = isSubMenu;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            if (isSubMenu) {
                view = inflater.inflate(R.layout.list_row_checkable_menu_item_22pt, parent, false);
            } else {
                view = inflater.inflate(R.layout.list_row_checkable_menu_item, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.menuText = (TextView) view.findViewById(R.id.menuText);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.menuText.setText(menuOptions.get(position).getName());

        return view;
    }

    static class ViewHolder {
        TextView menuText;
    }
}
