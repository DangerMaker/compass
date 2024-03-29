package com.ez08.compass.ui.trader.sort;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ez08.compass.R;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private int colorNormal,textContentColor,redColor,red2Color;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
		TypedArray a = mContext.obtainStyledAttributes(null, R.styleable.main_attrs,0, 0);
		colorNormal = mContext.getResources().getColor(a.getResourceId(R.styleable.main_attrs_lable_list_style,0));
		textContentColor = mContext.getResources().getColor(a.getResourceId(R.styleable.main_attrs_lable_item_style,0));

		redColor = mContext.getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color,0));
		red2Color = mContext.getResources().getColor(a.getResourceId(R.styleable.main_attrs_red_main_color,0));
//		red2Color = mContext.getResources().getColor(R.color.lable_stock_red);
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_sort_lar, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvAdd = (TextView) view.findViewById(R.id.sort_has_tv);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			if(mContent.getSortLetters().contains("#")){
				viewHolder.tvLetter.setText("推荐券商");
			}else{
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			}
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		String lName=this.list.get(position).getName();
		boolean recommend=false;
		if(lName.contains("#")){
			recommend=true;
			lName=lName.replace("#","");
		}
		viewHolder.tvTitle.setText(lName);

		if(mContent.isHasAdd()){
			viewHolder.tvAdd.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tvAdd.setVisibility(View.GONE);
		}
		if(mContent.getSortLetters().contains("#")){
			viewHolder.tvTitle.setTextColor(redColor);
		}else{
			viewHolder.tvTitle.setTextColor(colorNormal);
		}
		return view;

	}



	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvAdd;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}