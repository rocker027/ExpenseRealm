/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coors.expenserealm;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;

class ExpenseRecyclerViewAdapter extends RealmRecyclerViewAdapter<Expense, ExpenseRecyclerViewAdapter.ViewHolder>{

    public static interface OnRecyclerViewItemClickListener {
//        void onItemClick(View view, Expense expense);
        void clickEdit(Expense expense);
        void clickDelete(Expense expense);
    }

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public OnRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public ExpenseRecyclerViewAdapter(@Nullable OrderedRealmCollection<Expense> data) {
        super(data, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
//        itemView.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Expense expense = getItem(position);
        holder.tv_item_date.setText(expense.getDate());
        holder.tv_item_info.setText(expense.getInfo());
        holder.tv_item_amount.setText(expense.getAmount() + "");
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.clickEdit(expense);
                holder.setButtonVisibility(View.GONE);
            }
        });
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerViewItemClickListener.clickDelete(expense);
                holder.setButtonVisibility(View.GONE);
            }
        });
//        holder.iv_edit.setOnClickListener(this);
//        holder.itemView.setTag(expense);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_item_date;
        private TextView tv_item_info;
        private TextView tv_item_amount;
        private ImageView iv_edit;
        private ImageView iv_delete;
        private LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            tv_item_date = (TextView) itemView.findViewById(R.id.tv_item_date);
            tv_item_info = (TextView) itemView.findViewById(R.id.tv_item_info);
            tv_item_amount = (TextView) itemView.findViewById(R.id.tv_item_amount);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_recycler_edit);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_recycler_del);
            linearLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.linearlayout:
                    Log.d("adaper", "click");
                    setButtonVisibility(View.VISIBLE);
                    break;
                case R.id.iv_recycler_edit:
                    Log.d("adaper", "click");
                    onRecyclerViewItemClickListener.clickEdit((Expense) view.getTag());

                    break;
                case R.id.iv_recycler_del:
                    Log.d("adaper", "click");
                    onRecyclerViewItemClickListener.clickDelete((Expense) view.getTag());
                    break;
            }
        }

        public void setButtonVisibility(int visibility) {
            iv_edit.setVisibility(visibility);
            iv_delete.setVisibility(visibility);
        }

    }


}
