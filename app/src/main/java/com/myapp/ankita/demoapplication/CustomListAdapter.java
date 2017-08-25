package com.myapp.ankita.demoapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter implements Filterable{

    public class ViewHolder {
        ImageView artworkUrl60;
        TextView trackName, artistName, primaryGenreName, trackTimeMillis, trackPrice, previewUrl;
    }

    Context context;
    ValueFilter valueFilter;
    ImageLoader imageLoader;
    private ArrayList<JSONData> arrayJsonData = null;
    private ArrayList<JSONData> filteredArrayJsonData = null;
    Bitmap bmImg = null;

    public CustomListAdapter(Context context, ArrayList<JSONData> arrayJsonData) {
        this.context = context;
        this.arrayJsonData=arrayJsonData;
        imageLoader = new ImageLoader(context);
        this.filteredArrayJsonData = new ArrayList<JSONData>();
        this.filteredArrayJsonData.addAll(arrayJsonData);
    }

    @Override
    public int getCount() {
        return arrayJsonData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayJsonData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayJsonData.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_data, null);
            viewHolder = new ViewHolder();

            viewHolder.artworkUrl60 = (ImageView) convertView.findViewById(R.id.listArtworkImage);
            viewHolder.trackName = (TextView) convertView.findViewById(R.id.listTrackName);
            viewHolder.artistName = (TextView) convertView.findViewById(R.id.listArtistName);
            viewHolder.primaryGenreName = (TextView) convertView.findViewById(R.id.listGenre);
            viewHolder.trackTimeMillis = (TextView) convertView.findViewById(R.id.listDuration);
            viewHolder.trackPrice = (TextView) convertView.findViewById(R.id.listPrice);
            viewHolder.previewUrl = (TextView) convertView.findViewById(R.id.listPreviewUrl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JSONData data = (JSONData) getItem(position);
        imageLoader.DisplayImage(data.getArtworkUrl60(),viewHolder.artworkUrl60);
        //viewHolder.artworkUrl60.setImageBitmap(bmImg);
        viewHolder.trackName.setText(arrayJsonData.get(position).getTrackName() + "");
        viewHolder.artistName.setText(arrayJsonData.get(position).getArtistName() + "");
        viewHolder.primaryGenreName.setText(arrayJsonData.get(position).getPrimaryGenreName() + "");
        viewHolder.trackTimeMillis.setText(arrayJsonData.get(position).getTrackTimeMillis() + "");
        viewHolder.trackPrice.setText(arrayJsonData.get(position).getTrackPrice() + "");
        //viewHolder.previewUrl.setText(arrayJsonData.get(position).getPreviewUrl()+"");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SingleItemViewActivity.class);
                intent.putExtra("artworkUrl60",arrayJsonData.get(position).getArtworkUrl60());
                intent.putExtra("trackName",arrayJsonData.get(position).getTrackName());
                intent.putExtra("artistName",arrayJsonData.get(position).getArtistName());
                intent.putExtra("primaryGenreName",arrayJsonData.get(position).getPrimaryGenreName());
                intent.putExtra("trackTimeMillis",arrayJsonData.get(position).getTrackTimeMillis());
                intent.putExtra("trackPrice",arrayJsonData.get(position).getTrackPrice());
                intent.putExtra("previewUrl",arrayJsonData.get(position).getPreviewUrl());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    /*public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredArrayJsonData.clear();
        if (charText.length() == 0){
            filteredArrayJsonData.addAll(arrayJsonData);
        } else{
            for(JSONData detail : filteredArrayJsonData){
                if(charText.length()!=0 && detail.getArtistName().toLowerCase(Locale.getDefault()).contains(charText)){
                    filteredArrayJsonData.add(detail);
                } else if(charText.length()!=0 && detail.getTrackName().toLowerCase(Locale.getDefault()).contains(charText)){
                    filteredArrayJsonData.add(detail);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
       }

    public class ValueFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<JSONData> filterList = new ArrayList<JSONData>();
                for (int i = 0; i < filteredArrayJsonData.size(); i++) {
                    if ((filteredArrayJsonData.get(i).getArtistName().toUpperCase())
                        .contains(constraint.toString().toUpperCase())){

                        JSONData jsonData = new JSONData(filteredArrayJsonData.get(i).getArtworkUrl60(),
                                filteredArrayJsonData.get(i).getTrackName(),
                                filteredArrayJsonData.get(i).getArtistName(),
                                filteredArrayJsonData.get(i).getPrimaryGenreName(),
                                filteredArrayJsonData.get(i).getTrackTimeMillis(),
                                filteredArrayJsonData.get(i).getTrackPrice(),
                                filteredArrayJsonData.get(i).getPreviewUrl());

                        filterList.add(jsonData);
                    } else if ((filteredArrayJsonData.get(i).getTrackName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())){
                        JSONData jsonData = new JSONData(filteredArrayJsonData.get(i).getArtworkUrl60(),
                                filteredArrayJsonData.get(i).getTrackName(),
                                filteredArrayJsonData.get(i).getArtistName(),
                                filteredArrayJsonData.get(i).getPrimaryGenreName(),
                                filteredArrayJsonData.get(i).getTrackTimeMillis(),
                                filteredArrayJsonData.get(i).getTrackPrice(),
                                filteredArrayJsonData.get(i).getPreviewUrl());

                        filterList.add(jsonData);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }else {
                results.count = filteredArrayJsonData.size();
                results.values = filteredArrayJsonData;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayJsonData = (ArrayList<JSONData>)results.values;
            notifyDataSetChanged();
        }
    }
}