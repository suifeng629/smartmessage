package com.ctid.intelsmsapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.activity.DetailMessageActivity;
import com.ctid.intelsmsapp.bean.MessageHolder;
import com.ctid.intelsmsapp.bean.MessageInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * ClassName: 类名称<br>
 * Description: 类描述<br>
 *
 * @author sunzhiwei<br>
 * @date 2017/10/10
 * @email sunzhiwei@ctid.com.cn
 */
public class MessageListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext = null;

    //存储所有短信信息的列表
    private List<MessageInfo> messageInfoList = new ArrayList<MessageInfo>();
//    private ImageLoaderConfiguration configuration;
//    ImageLoader imageLoader;

    private ImageLoadingListener animateFirstListener = new SimpleImageLoadingListener();

    private DisplayImageOptions imageOptions;

    //MessageListAdapter初始化构造方法
    public MessageListAdapter(Context context, List<MessageInfo> messageInfoList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.messageInfoList = messageInfoList;
//        configuration = ImageLoaderConfiguration
//                .createDefault(mContext);
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon)
                .showImageForEmptyUri(R.drawable.icon)
                .showImageOnFail(R.drawable.icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

    public Object getItem(int arg0) {
        return arg0;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return messageInfoList.size();
    }

    public View getView(final int position, View convertView, android.view.ViewGroup parent) {
        MessageHolder messageHolder = null;

        //判断convertView是否已创建，若已存在则不必重新创建新视图，节省系统资源
        if (convertView == null) {
            // 和item_custom.xml脚本关联
            convertView = mInflater.inflate(R.layout.list_item, null);

            messageHolder = new MessageHolder();

            //加载控件到信息载体中
            messageHolder.setIvImage((ImageView) convertView.findViewById(R.id.contact_image));
            messageHolder.setTvTitle((TextView) convertView.findViewById(R.id.titleTextView));
            messageHolder.setTvDesc((TextView) convertView.findViewById(R.id.descTextView));
            messageHolder.setTvCount((TextView) convertView.findViewById(R.id.countTextView));
            messageHolder.setTvTime((TextView) convertView.findViewById(R.id.timeTextView));

            //将联系人信息载体Holder放入convertView视图
            convertView.setTag(messageHolder);

        } else {
            //从convertView视图取出联系人信息载体Holder
            messageHolder = (MessageHolder) convertView.getTag();
        }

        // 通过短信Holder设置item中4个TextView的文本与联系人头像
        //绘制短信联系人信息，内容为其名称或号码
        messageHolder.getTvTitle().setText(messageInfoList.get(position).getContactMes());
        //在信息正文区域绘制信息内容
        messageHolder.getTvDesc().setText(messageInfoList.get(position).getSmsbody());

        messageHolder.getTvCount().setText(messageInfoList.get(position).getMessageCout());
        messageHolder.getTvTime().setText(messageInfoList.get(position).getDate());

        String imageUri = messageInfoList.get(position).getPhoneUrl();
        if (imageUri == null) {
            messageHolder.getIvImage().setImageBitmap(messageInfoList.get(position).getContactPhoto());
        } else {
            final ImageView imageView = messageHolder.getIvImage();
            imageView.setTag(imageUri);
            ImageLoader.getInstance().displayImage(imageUri, imageView, imageOptions, animateFirstListener);

//            imageLoader = ImageLoader.getInstance();
//            if (!imageLoader.isInited()) {
//                imageLoader.init(configuration);
//            }
//            imageLoader.loadImage(imageUri, new SimpleImageLoadingListener() {
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    super.onLoadingFailed(imageUri, view, failReason);
//                    imageView.setImageBitmap(BitmapFactory.decodeResource(
//                            mContext.getResources(), R.drawable.icon));
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view,
//                                              Bitmap loadedImage) {
//                    super.onLoadingComplete(imageUri, view, loadedImage);
//                    if (imageView1.getTag() != null && imageUri.equals(imageView1.getTag())) {
//                        imageView1.setImageBitmap(loadedImage);
//                    }
//                }
//            });
            //imageLoader.getInstance().displayImage(imageUri, imageView1);
        }


        /*
         * 在短信主界面为每个短信会话设置监听事件，当选择点击某条会话时，跳转到显示该会话包含的所有信息记录的页面
         * */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //通过Intent向显示短信会话包含的信息的Activity传递会话id
                intent.putExtra("threadId", messageInfoList.get(position).getThreadId());
                intent.putExtra("number", messageInfoList.get(position).getContactNumber());
                intent.setClass(mContext, DetailMessageActivity.class);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
