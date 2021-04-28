package com.cross.geosearch.utils;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.cross.geosearch.R;
import com.cross.geosearch.pojo.Page;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Page> pages;
    private String lat = "";
    private String lon = "";

    public PageAdapter(List<Page> arrayList, String latitude, String longitude) {
        pages = arrayList;
        lat = latitude;
        lon = longitude;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
            return new PageViewHolder(view);
        }  else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        }  else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.coordinates.setText(lat + "," + lon);
        } else if (holder instanceof PageViewHolder) {
            PageViewHolder itemViewHolder = (PageViewHolder) holder;

            Page object = pages.get(position-1);
            String name = object.getTitle();
            itemViewHolder.name.setText(name);
            itemViewHolder.distance.setText(getDistance(object.getCoordinates().get(0).getLat(), object.getCoordinates().get(0).getLon()));
            if (object.getThumbnail() != null){
                itemViewHolder.image.setImageUrl(object.getThumbnail().getSource(), VolleySingleton.getInstance(holder.itemView.getContext()).getImageLoader());
            } else {
                itemViewHolder.image.setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Wikipedia-logo-v2.svg/526px-Wikipedia-logo-v2.svg.png", VolleySingleton.getInstance(holder.itemView.getContext()).getImageLoader());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return pages.size()+1;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView coordinates;

        public HeaderViewHolder(View view) {
            super(view);
            coordinates = (TextView) view.findViewById(R.id.header_coordinates);
        }
    }

    public class PageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView distance;
        private NetworkImageView image;

        public PageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.list_name);
            distance = (TextView) itemView.findViewById(R.id.list_distance);
            image =  (NetworkImageView) itemView.findViewById(R.id.list_image);
        }

        @Override
        public void onClick(View view) {
            Page current = pages.get(getAdapterPosition());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://en.wikipedia.org/?curid=" + current.getPageid()));
            view.getContext().startActivity(i);
        }
    }

    private String getDistance(double latitude, double longitude){
        Location to = new Location("To");
        to.setLatitude(latitude);
        to.setLongitude(longitude);

        Location from = new Location("From");
        from.setLatitude(Double.parseDouble(lat));
        from.setLongitude(Double.parseDouble(lon));

        return  Math.round(from.distanceTo(to)) + "m";
    }
}

