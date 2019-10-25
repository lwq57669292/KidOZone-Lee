package com.hellofit.kidozone.puzzleGame.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.hellofit.kidozone.R;
import com.hellofit.kidozone.puzzleGame.gameUtils.Utils;
import com.hellofit.kidozone.puzzleGame.module.ImageSoures;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/***
 *  This class is the dialog to select sport image
 *
 *  Created by Weiqiang Li on 09/13/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.1
 *
 *  Final modified date: 09/13/2019 by Weiqiang Li
 */

public class SelectImageDialogSport extends DialogFragment {

    private View view;
    private RecyclerView imageList;
    private OnItemClickListener itemClickListener;
    private Activity activity;
    private int selectRes;
    private ImageListAdapter imageListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_select_image, container);
        imageList = (RecyclerView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        imageList.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
        imageListAdapter = new ImageListAdapter();
        imageList.setAdapter(imageListAdapter);
    }

    /**
     * Set the sport images to the Adapter
     */
    public class ImageListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageViewHolder(LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            Bitmap bitmap = Utils.readBitmap(activity.getApplicationContext(), ImageSoures.imageSoursSport[position], 3);
            viewHolder.imageView.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return ImageSoures.imageSoursSport.length;
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.itemImg);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != -1 && itemClickListener != null) {
                        itemClickListener.itemClick(getAdapterPosition(), ImageSoures.imageSoursSport[getAdapterPosition()]);
                        dismiss();
                    }
                }
            });
        }
    }


    public void showDialog(FragmentManager fragmentManager, String tag, int res) {
        show(fragmentManager, "tag");
        selectRes = res;
    }

    public void addItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        public void itemClick(int postion, int res);
    }


}
