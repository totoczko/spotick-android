//package com.example.spotick;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {
//
//    Context mContext;
//    List<cardItem> mData;
//
//    public Adapter(Context mContext, List<cardItem> mData) {
//        this.mContext = mContext;
//        this.mData = mData;
//    }
//
//    @NonNull
//    @Override
//    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View v = inflater.inflate(R.layout.card_item, parent, false);
//        return new myViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
//        holder.profile_photo.setImageResource(mData.get(position).getProfilePhoto());
//        holder.tv_title.setText(mData.get(position).getProfileName());
//        holder.tv_nbFollowers.setText(Integer.toString(mData.get(position).getNbFollowers()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    public class myViewHolder extends RecyclerView.ViewHolder{
//
//        ImageView profile_photo;
//        TextView tv_title, tv_nbFollowers;
//
//
//        public myViewHolder(View itemView){
//            super(itemView);
//            profile_photo = itemView.findViewById(R.id.cardview_image);
//            tv_title = itemView.findViewById(R.id.short_description);
//            tv_nbFollowers = itemView.findViewById(R.id.likes_counter);
//
//        }
//    }
//}
