package com.spectre.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.activity.AddWorkActivity;
import com.spectre.activity.AddWorkSectionActivity;
import com.spectre.beans.ImageData;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.helper.PathUtils;
import com.spectre.other.Constant;
import com.spectre.utility.ConvetBitmap;
import com.spectre.utility.PermissionsUtils;
import com.spectre.utility.Utility;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_OK;
import static com.zhihu.matisse.MimeType.JPEG;
import static com.zhihu.matisse.MimeType.PNG;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkImagesFragment extends Fragment implements View.OnClickListener {
    private View view;
    RecyclerView mRecyclerView, mRecyclerViewAfter;
    private LinearLayoutManager mLayoutManager, mLayoutManagerAfter;
    private RecyclerView.Adapter mAdapter, mAdapterAfter;
    private ArrayList<String> showImage = new ArrayList<>();
    private ArrayList<ImageData> bitMapList = new ArrayList<>();
    private ArrayList<ImageData> bitMapList1 = new ArrayList<>();
    private boolean canEdit, refresh, canEdit2;
    private int position = -1;
    private Context context;
    private ProgressView progressDialog1, progressDialogAfter;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CHOOSE1 = 24;
    private CustomRayMaterialTextView btn_work_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_work_images, container, false);
        view = inflater.inflate(R.layout.fragment_work_images, container, false);
        context = getActivity();
        initView();
        return view;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        progressDialog1 = (ProgressView) view.findViewById(R.id.progress_pv_linear_colors);
        progressDialogAfter = (ProgressView) view.findViewById(R.id.progress_pv_linear_colors_after);

        mRecyclerViewAfter = (RecyclerView) view.findViewById(R.id.recycler_after);
        mRecyclerViewAfter.setHasFixedSize(true);
        mLayoutManagerAfter = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewAfter.setLayoutManager(mLayoutManagerAfter);
        mRecyclerViewAfter.getItemAnimator().setChangeDuration(0);
        progressDialogAfter = (ProgressView) view.findViewById(R.id.progress_pv_linear_colors_after);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerViewAfter.setNestedScrollingEnabled(false);

        ImageData imageData = new ImageData();
        bitMapList.add(imageData);
        mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
        mRecyclerView.setAdapter(mAdapter);

        ImageData imageData1 = new ImageData();
        bitMapList1.add(imageData1);
        mAdapterAfter = new ShowImagesAdapter(context, bitMapList1, 1);
        mRecyclerViewAfter.setAdapter(mAdapterAfter);


        btn_work_submit = (CustomRayMaterialTextView) view.findViewById(R.id.btn_work_submit);
        btn_work_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_work_submit:
                checkCondition();
                break;
        }
    }

    public JSONObject getData() {
        JSONObject jsonObject = ((AddWorkSectionActivity) context).jsonObject;

        try {
            JSONArray jsonArray = new JSONArray();
            for (ImageData imageData : bitMapList) {
                if (imageData.getBytes() != null) {
                    jsonArray.put(imageData.getBytes());
                }
            }
            jsonObject.put(Constant.IMAGE, jsonArray);
            JSONArray jsonArray1 = new JSONArray();
            for (ImageData imageData : bitMapList1) {
                if (imageData.getBytes() != null) {
                    jsonArray1.put(imageData.getBytes());
                }
            }
            jsonObject.put(Constant.AFTER_IMAGE, jsonArray1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private class ShowImagesAdapter extends RecyclerView.Adapter<ShowImagesAdapter.ViewHolder> {
        Context appContext;
        private int selectedPos = 0;
        ArrayList<ImageData> AL = new ArrayList<>();

        public ShowImagesAdapter(Context appContext, ArrayList<ImageData> arraylist, int selectedPos) {
            this.appContext = appContext;
            this.AL = arraylist;
            this.selectedPos = selectedPos;
        }

        @Override
        public ShowImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_images, parent, false);
            ShowImagesAdapter.ViewHolder viewHolder = new ShowImagesAdapter.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ShowImagesAdapter.ViewHolder holder, final int position) {
            //    holder.itemView.btn_choose_file.
            if (AL.get(position).getBitmap() != null) {
                new AQuery(context).id(holder.btn_choose_file).image(AL.get(position).getBitmap());
                holder.btn_delete.setVisibility(View.VISIBLE);
                holder.btn_choose_file.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                holder.btn_choose_file.setImageResource(R.mipmap.postadd);
                holder.btn_choose_file.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.btn_delete.setVisibility(View.GONE);
            }

            holder.btn_choose_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AL.get(position).getBitmap() == null)
                        checkPermission(selectedPos);
                }
            });

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos == 0) {
                        int size = bitMapList.size();
                        Log.d(AddWorkActivity.class.getName() + " List Size " + bitMapList.size(), bitMapList.toString() + "");
                        bitMapList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        notifyDataSetChanged();

                        if (size == 5 && bitMapList.size() < 5 && bitMapList.get(bitMapList.size() - 1).getBitmap() != null) {
                            ImageData imageData2 = new ImageData();
                            imageData2.setBitmap(null);
                            imageData2.setBytes(null);
                            bitMapList.add(imageData2);
                            notifyDataSetChanged();
                        }
                    } else {
                        int size = bitMapList1.size();
                        Log.d(AddWorkActivity.class.getName() + " List Size " + bitMapList1.size(), bitMapList1.toString() + "");
                        bitMapList1.remove(position);
                        mAdapterAfter.notifyItemRemoved(position);
                        notifyDataSetChanged();

                        if (size == 5 && bitMapList1.size() < 5 && bitMapList1.get(bitMapList1.size() - 1).getBitmap() != null) {
                            ImageData imageData2 = new ImageData();
                            imageData2.setBitmap(null);
                            imageData2.setBytes(null);
                            bitMapList1.add(imageData2);
                            notifyDataSetChanged();
                        }
                    }
                }

            });
        }

        @Override
        public int getItemCount() {
            return AL.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView btn_choose_file;
            ImageView btn_delete;
            RelativeLayout layout_image;

            public ViewHolder(View itemView) {
                super(itemView);
                btn_choose_file = ((ImageView) itemView.findViewById(R.id.btn_choose_file));
                layout_image = ((RelativeLayout) itemView.findViewById(R.id.layout_image));
               /* int imageSide = Utility.dpToPx(context, 100);
                int imageBorderPadding = Utility.dpToPx(context, 2);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageSide, imageSide);
                layoutParams.setMargins(imageBorderPadding, 0, imageBorderPadding, 0);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(Utility.dpToPx(context, 104), Utility.dpToPx(context, 104));
                btn_choose_file.setLayoutParams(layoutParams);
                layout_image.setLayoutParams(layoutParams1);
                btn_choose_file.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            */
                btn_delete = ((ImageView) itemView.findViewById(R.id.btn_delete));
            }
        }
    }


    private void checkPermission(int selectedPos) {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
           /* if (!PermissionsUtils.getInstance(context).requiredPermissionsGranted(context)) {
                return;
            }*/

            if (!PermissionsUtils.getInstance(context).requiredPermissionsGranted(context, (Fragment) this)) {
                return;
            }

        }


        if (selectedPos == 0) {
            int size = 5 - bitMapList.size();

            if (bitMapList.get(0).getBitmap() == null || bitMapList.get(bitMapList.size() - 1).getBitmap() == null) {
                size++;
            }

            if (size <= 0) {
                return;
            }


            Matisse.from(this)
                    .choose(ofImage())
                    .countable(true)
                    .capture(true)
                    .maxSelectable(size)
                    .captureStrategy(new CaptureStrategy(true, context.getApplicationContext().getPackageName() + ".provider"))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Dracula)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            int size = 5 - bitMapList1.size();

            if (bitMapList1.get(0).getBitmap() == null || bitMapList1.get(bitMapList1.size() - 1).getBitmap() == null) {
                size++;
            }

            if (size <= 0) {
                return;
            }


            Matisse.from(this)
                    .choose(ofImage())
                    .countable(true)
                    .capture(true)
                    .maxSelectable(size)
                    .captureStrategy(new CaptureStrategy(true, context.getApplicationContext().getPackageName() + ".provider"))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Dracula)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE1);
        }

    }

    public static Set<MimeType> ofImage() {
        return EnumSet.of(JPEG, PNG);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            int size = bitMapList.size() - 1;
            // mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));

            List<Uri> list = Matisse.obtainResult(data);
            if (list != null && list.size() > 0) {

                for (Uri uri : list) {
                    ImageData imageData = setImage(uri);
                    if (imageData.getBitmap() != null && imageData.getBitmap().getByteCount() > 0) {
                        if (bitMapList.get(size).getBitmap() == null)
                            bitMapList.set(size, imageData);
                        else
                            bitMapList.add(imageData);
                    }
                }


            }

            if ((bitMapList.size() > 1 || bitMapList.get(0).getBitmap() != null) && bitMapList.size() < 5) {
                ImageData imageData = new ImageData();
                bitMapList.add(bitMapList.size(), imageData);
            }

            mAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_CHOOSE1 && resultCode == RESULT_OK) {
            int size = bitMapList1.size() - 1;
            // mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));

            List<Uri> list = Matisse.obtainResult(data);
            if (list != null && list.size() > 0) {

                for (Uri uri : list) {
                    ImageData imageData = setImage(uri);
                    if (imageData.getBitmap() != null && imageData.getBitmap().getByteCount() > 0) {
                        if (bitMapList1.get(size).getBitmap() == null)
                            bitMapList1.set(size, imageData);
                        else
                            bitMapList1.add(imageData);
                    }
                }


            }

            if ((bitMapList1.size() > 1 || bitMapList1.get(0).getBitmap() != null) && bitMapList1.size() < 5) {
                ImageData imageData = new ImageData();
                bitMapList1.add(bitMapList1.size(), imageData);
            }

            mAdapterAfter.notifyDataSetChanged();
        }
    }


    private ImageData setImage(Uri destination) {
        ImageData imageData = new ImageData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), destination);
            Log.d("Bitmap", bitmap.getHeight() + " " + bitmap.getWidth());
            bitmap = ConvetBitmap.Mytransform(bitmap);
            //Bitmap bitmap1 = Utility.rotateImage(bitmap, new File(destination.getPath()));
            String path = null;
            try {
                path = PathUtils.getPath(getActivity(), destination);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (path != null && !path.isEmpty()) {
                Bitmap bitmap1 = Utility.rotateImage(bitmap, new File(path));
                if (bitmap1 != null && bitmap1.getByteCount() > 0) {
                    bitmap = bitmap1;
                }
            }
            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, datasecond);
            Log.d("Bitmap", bitmap.getHeight() + " " + bitmap.getWidth());
            //  bitmap.compress(Bitmap.CompressFormat.PNG, 20, datasecond);
            String encodedString = Base64.encodeToString(datasecond.toByteArray(), Base64.DEFAULT);
            imageData.setBytes(encodedString);
            imageData.setBitmap(bitmap);

            //  ivProfile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
            // img1.setImageBitmap(bitmap);
            return imageData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageData;
    }


    private void checkCondition() {

        if (bitMapList.size() == 1) {
            Utility.showToast(context, getString(R.string.empty_before_image));
            return;
        }

        if (bitMapList1.size() == 1) {
            Utility.showToast(context, getString(R.string.empty_after_image));
            return;
        }

        if (bitMapList.size() != bitMapList1.size()) {
            Utility.showToast(context, getString(R.string.image_should_be_same));
            return;
        }

        if (bitMapList.size() == 5 && bitMapList1.size() == 5) {
            int count1 = 0, count2 = 0;
            for (ImageData imageData : bitMapList1) {
                if (imageData.getBytes() != null) {
                    count1++;
                }
            }

            for (ImageData imageData : bitMapList) {
                if (imageData.getBytes() != null) {
                    count2++;
                }
            }

            if (count1 != count2) {
                Utility.showToast(context, getString(R.string.image_should_be_same));
                return;
            }
        }

        ((AddWorkSectionActivity) context).submitForm();

    }


    @Override
    public void onResume() {

        super.onResume();
    }
}
