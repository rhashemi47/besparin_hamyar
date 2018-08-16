package com.besparina.it.hamyar;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CustomeExpandableListAdapter extends BaseExpandableListAdapter {

	// Define activity context
	private Context mContext;

	/*
	 * Here we have a Hashmap containing a String key
	 * (can be Integer or other type but I was testing
	 * with contacts so I used contact name as the key)
	*/
	private HashMap<String, List<String>> mListDataChild;

	// ArrayList that is what each key in the above
	// hashmap points to
	private List<String> mListDataGroup;

	// Hashmap for keeping track of our checkbox check states
	private HashMap<Integer, boolean[]> mChildCheckStates;

	// Our getChildView & getGroupView use the viewholder patter
	// Here are the viewholders defined, the inner classes are
	// at the bottom
	private ChildViewHolder childViewHolder;
	private GroupViewHolder groupViewHolder;

	/*
     	 *  For the purpose of this document, I'm only using a single
	 *	textview in the group (parent) and child, but you're limited only
	 *	by your XML view for each group item :)
	*/
	private String groupText;
	private String childText;

	private DatabaseHelper dbh;
	private SQLiteDatabase db;

	/*  Here's the constructor we'll use to pass in our calling
	 *  activity's context, group items, and child items
	*/
	public CustomeExpandableListAdapter(Context context, List<String> listDataGroup, HashMap<String, List<String>> listDataChild) {

		mContext = context;
		mListDataGroup = listDataGroup;
		mListDataChild = listDataChild;

		// Initialize our hashmap containing our check states here
		mChildCheckStates = new HashMap<Integer, boolean[]>();
		dbh=new DatabaseHelper(context.getApplicationContext());
		try {

			dbh.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			dbh.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;
		}
	}

	@Override
	public int getGroupCount() {
		return mListDataGroup.size();
	}

	/*
	 * This defaults to "public object getGroup" if you auto import the methods
	 * I've always make a point to change it from "object" to whatever item
	 * I passed through the constructor
	*/
	@Override
	public Object getGroup(int groupPosition) {
		return mListDataGroup.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {

		//  I passed a text string into an activity holding a getter/setter
		//  which I passed in through "ExpListGroupItems".
		//  Here is where I call the getter to get that text
		groupText = (String)getGroup(groupPosition);

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group_expertise, null);

			// Initialize the GroupViewHolder defined at the bottom of this document
			groupViewHolder = new GroupViewHolder();

			groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);

			convertView.setTag(groupViewHolder);
		} else {

			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}

		groupViewHolder.mGroupText.setText(groupText);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
	}

	/*
	 * This defaults to "public object getChild" if you auto import the methods
	 * I've always make a point to change it from "object" to whatever item
	 * I passed through the constructor
	*/
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		final int mGroupPosition = groupPosition;
		final int mChildPosition = childPosition;

		//  I passed a text string into an activity holding a getter/setter
		//  which I passed in through "ExpListChildItems".
		//  Here is where I call the getter to get that text
		childText = (String)getChild(mGroupPosition, mChildPosition);

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) this.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);

			childViewHolder = new ChildViewHolder();

			childViewHolder.mChildText = (TextView) convertView.findViewById(R.id.lblListItem);

			childViewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox1);

			convertView.setTag(R.layout.list_item, childViewHolder);

		} else {

			childViewHolder = (ChildViewHolder) convertView.getTag(R.layout.list_item);
		}

		childViewHolder.mChildText.setText(childText);

		/*
		 * You have to set the onCheckChangedListener to null
		 * before restoring check states because each call to
		 * "setChecked" is accompanied by a call to the
		 * onCheckChangedListener
		*/
		childViewHolder.mCheckBox.setTag(childText);
		childViewHolder.mCheckBox.setOnCheckedChangeListener(null);

		if (mChildCheckStates.containsKey(mGroupPosition)) {
			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
			 * the value of the parent view (group) of this child (aka, the key),
			 * then retrive the boolean array getChecked[]
			*/
			boolean getChecked[] = mChildCheckStates.get(mGroupPosition);

			// set the check state of this position's checkbox based on the
			// boolean value of getChecked[position]
			childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);

		} else {

			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
			 * contain the value of the parent view (group) of this child (aka, the key),
			 * (aka, the key), then initialize getChecked[] as a new boolean array
			 *  and set it's size to the total number of children associated with
			 *  the parent group
			*/
			boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

			// add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
			mChildCheckStates.put(mGroupPosition, getChecked);

			// set the check state of this position's checkbox based on the
			// boolean value of getChecked[position]
			childViewHolder.mCheckBox.setChecked(false);
		}

		childViewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {

					boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
					getChecked[mChildPosition] = isChecked;
					mChildCheckStates.put(mGroupPosition, getChecked);
					db=dbh.getReadableDatabase();
					Cursor coursors=db.rawQuery("SELECT * FROM servicesdetails WHERE name='"+buttonView.getTag().toString()+"'", null);
					db=dbh.getWritableDatabase();
					coursors.moveToNext();
					db.execSQL("INSERT INTO exprtise (code) VALUES('"+coursors.getString(coursors.getColumnIndex("code")) +"')");

				} else {

					boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
					getChecked[mChildPosition] = isChecked;
					mChildCheckStates.put(mGroupPosition, getChecked);
					db=dbh.getReadableDatabase();
					Cursor coursors=db.rawQuery("SELECT * FROM servicesdetails WHERE name='"+buttonView.getTag().toString()+"'", null);
					db=dbh.getWritableDatabase();
					coursors.moveToNext();
					db.execSQL("DELETE FROM exprtise WHERE code='"+coursors.getString(coursors.getColumnIndex("code")) +"'");
				}
			}
		});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	public final class GroupViewHolder {

		TextView mGroupText;
	}

	public final class ChildViewHolder {

		TextView mChildText;
		CheckBox mCheckBox;
	}
}